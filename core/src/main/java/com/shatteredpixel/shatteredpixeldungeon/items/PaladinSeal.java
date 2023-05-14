/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class PaladinSeal extends Item {

	public static final String AC_AFFIX = "AFFIX";

	//only to be used from the quickslot, for tutorial purposes mostly.
	public static final String AC_INFO = "INFO_WINDOW";

	{
		image = ItemSpriteSheet.PALADINSEAL;

		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;

		defaultAction = AC_INFO;
	}

	private Weapon.Enchantment enchantment;

	public Weapon.Enchantment getEnchantment(){
		return enchantment;
	}

	public void setEnchantment( Weapon.Enchantment enchantment ){
		this.enchantment = enchantment;
	}

	public int maxShield( int armTier, int armLvl ){
		return armTier + armLvl + Dungeon.hero.pointsInTalent(Talent.IRON_WILL);
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null ? enchantment.glowing() : null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_AFFIX);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_AFFIX)){
			curItem = this;
			GameScene.selectItem(weaponSelector);
		} else if (action.equals(AC_INFO)) {
			GameScene.show(new WndUseItem(null, this));
		}
	}

	@Override
	//scroll of upgrade can be used directly once, same as upgrading weapon the seal is affixed to then removing it.
	public boolean isUpgradable() {
		return level() == 0;
	}

	protected static WndBag.ItemSelector weaponSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return  Messages.get(PaladinSeal.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Weapon;
		}

		@Override
		public void onSelect( Item item ) {
			PaladinSeal seal = (PaladinSeal) curItem;

			if (item != null && item instanceof MeleeWeapon ){
				Weapon weapon = (Weapon)item;
				if (!weapon.levelKnown){
					GLog.w(Messages.get(PaladinSeal.class, "unknown_weapon"));

				} else if (weapon.cursed && (seal.getEnchantment() == null || !seal.getEnchantment().curse())){
					GLog.w(Messages.get(PaladinSeal.class, "cursed_weapon"));

				} else if (weapon.enchantment != null && seal.getEnchantment() != null
						&& weapon.enchantment.getClass() != seal.getEnchantment().getClass()) {
					GameScene.show(new WndOptions(new ItemSprite(seal),
							Messages.get(PaladinSeal.class, "choose_title"),
							Messages.get(PaladinSeal.class, "choose_desc"),
							weapon.enchantment.name(),
							seal.getEnchantment().name()){
						@Override
						protected void onSelect(int index) {
							if (index == 0) seal.setEnchantment(null);
							//if index is 1, then the enchantment transfer happens in affixSeal

							GLog.p(Messages.get(PaladinSeal.class, "affix"));
							Dungeon.hero.sprite.operate(Dungeon.hero.pos);
							Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
							weapon.affixSeal(seal);
							seal.detach(Dungeon.hero.belongings.backpack);
						}
					});

				} else {
					GLog.p(Messages.get(PaladinSeal.class, "affix"));
					Dungeon.hero.sprite.operate(Dungeon.hero.pos);
					Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
					weapon.affixSeal((PaladinSeal)curItem);
					curItem.detach(Dungeon.hero.belongings.backpack);
				}
			}
		}
	};

	private static final String ENCHANTMENT = "enchantment";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ENCHANTMENT, enchantment);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		enchantment = (Weapon.Enchantment)bundle.get(ENCHANTMENT);
	}

}
