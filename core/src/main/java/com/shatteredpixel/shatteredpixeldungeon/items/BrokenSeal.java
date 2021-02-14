/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
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

public class BrokenSeal extends Item {

	public static final String AC_AFFIX = "AFFIX";

	//only to be used from the quickslot, for tutorial purposes mostly.
	public static final String AC_INFO = "INFO_WINDOW";

	{
		image = ItemSpriteSheet.SEAL;

		cursedKnown = levelKnown = true;
		unique = true;
		bones = false;

		defaultAction = AC_INFO;
	}

	private Armor.Glyph glyph;

	public Armor.Glyph getGlyph(){
		return glyph;
	}

	public void setGlyph( Armor.Glyph glyph ){
		this.glyph = glyph;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return glyph != null ? glyph.glowing() : null;
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
			GameScene.selectItem(armorSelector, WndBag.Mode.ARMOR, Messages.get(this, "prompt"));
		} else if (action.equals(AC_INFO)) {
			GameScene.show(new WndUseItem(null, this));
		}
	}

	@Override
	//scroll of upgrade can be used directly once, same as upgrading armor the seal is affixed to then removing it.
	public boolean isUpgradable() {
		return level() == 0;
	}

	protected static WndBag.Listener armorSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			BrokenSeal seal = (BrokenSeal) curItem;
			if (item != null && item instanceof Armor) {
				Armor armor = (Armor)item;
				if (!armor.levelKnown){
					GLog.w(Messages.get(BrokenSeal.class, "unknown_armor"));

				} else if ((armor.cursed || armor.level() < 0)
						&& (seal.getGlyph() == null || !seal.getGlyph().curse())){
					GLog.w(Messages.get(BrokenSeal.class, "degraded_armor"));

				} else if (armor.glyph != null && seal.getGlyph() != null
						&& armor.glyph.getClass() != seal.getGlyph().getClass()) {
					GameScene.show(new WndOptions(Messages.get(BrokenSeal.class, "choose_title"),
							Messages.get(BrokenSeal.class, "choose_desc"),
							armor.glyph.name(),
							seal.getGlyph().name()){
						@Override
						protected void onSelect(int index) {
							if (index == 0) seal.setGlyph(null);
							//if index is 1, then the glyph transfer happens in affixSeal

							GLog.p(Messages.get(BrokenSeal.class, "affix"));
							Dungeon.hero.sprite.operate(Dungeon.hero.pos);
							Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
							armor.affixSeal(seal);
							seal.detach(Dungeon.hero.belongings.backpack);
						}
					});

				} else {
					GLog.p(Messages.get(BrokenSeal.class, "affix"));
					Dungeon.hero.sprite.operate(Dungeon.hero.pos);
					Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
					armor.affixSeal((BrokenSeal)curItem);
					curItem.detach(Dungeon.hero.belongings.backpack);
				}
			}
		}
	};

	private static final String GLYPH = "glyph";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(GLYPH, glyph);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		glyph = (Armor.Glyph)bundle.get(GLYPH);
	}

	public static class WarriorShield extends ShieldBuff {

		private Armor armor;
		private float partialShield;

		@Override
		public synchronized boolean act() {
			if (shielding() < maxShield()) {
				partialShield += 1/30f;
			}
			
			while (partialShield >= 1){
				incShield();
				partialShield--;
			}
			
			if (shielding() <= 0 && maxShield() <= 0){
				detach();
			}
			
			spend(TICK);
			return true;
		}
		
		public synchronized void supercharge(int maxShield){
			if (maxShield > shielding()){
				setShield(maxShield);
			}
		}

		public synchronized void setArmor(Armor arm){
			armor = arm;
		}

		public synchronized int maxShield() {
			if (armor != null && armor.isEquipped((Hero)target)) {
				return armor.tier + armor.level() + ((Hero) target).pointsInTalent(Talent.IRON_WILL);
			} else {
				return 0;
			}
		}
		
		@Override
		//logic edited slightly as buff should not detach
		public int absorbDamage(int dmg) {
			if (shielding() <= 0) return dmg;

			if (shielding() >= dmg){
				decShield(dmg);
				dmg = 0;
			} else {
				dmg -= shielding();
				decShield(shielding());
			}
			return dmg;
		}
	}
}
