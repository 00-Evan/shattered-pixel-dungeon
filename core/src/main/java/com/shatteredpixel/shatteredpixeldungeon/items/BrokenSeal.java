/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Combo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HoldFast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.util.ArrayList;
import java.util.Arrays;

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

	public boolean canTransferGlyph(){
		if (glyph == null){
			return false;
		}
		if (Dungeon.hero.pointsInTalent(Talent.RUNIC_TRANSFERENCE) == 2){
			return true;
		} else if (Dungeon.hero.pointsInTalent(Talent.RUNIC_TRANSFERENCE) == 1
			&& (Arrays.asList(Armor.Glyph.common).contains(glyph.getClass())
				|| Arrays.asList(Armor.Glyph.uncommon).contains(glyph.getClass()))){
			return true;
		} else {
			return false;
		}
	}

	public Armor.Glyph getGlyph(){
		return glyph;
	}

	public void setGlyph( Armor.Glyph glyph ){
		this.glyph = glyph;
	}

	public int maxShield( int armTier, int armLvl ){
		// 5-15, based on equip tier and iron will
		return 3 + 2*armTier + Dungeon.hero.pointsInTalent(Talent.IRON_WILL);
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
			GameScene.selectItem(armorSelector);
		} else if (action.equals(AC_INFO)) {
			GameScene.show(new WndUseItem(null, this));
		}
	}

	//outgoing is either the seal itself as an item, or an armor the seal is affixed to
	public void affixToArmor(Armor armor, Item outgoing){
		if (armor != null) {
			if (!armor.cursedKnown){
				GLog.w(Messages.get(BrokenSeal.class, "unknown_armor"));

			} else if (armor.cursed && (getGlyph() == null || !getGlyph().curse())){
				GLog.w(Messages.get(BrokenSeal.class, "cursed_armor"));

			}else if (armor.glyph != null && getGlyph() != null
					&& canTransferGlyph()
					&& armor.glyph.getClass() != getGlyph().getClass()) {

				GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.SEAL),
						Messages.get(BrokenSeal.class, "choose_title"),
						Messages.get(BrokenSeal.class, "choose_desc", armor.glyph.name(), getGlyph().name()),
						armor.glyph.name(),
						getGlyph().name()){
					@Override
					protected void onSelect(int index) {
						if (index == -1) return;

						if (outgoing == BrokenSeal.this) {
							detach(Dungeon.hero.belongings.backpack);
						} else if (outgoing instanceof Armor){
							((Armor) outgoing).detachSeal();
						}

						if (index == 0) setGlyph(null);
						//if index is 1, then the glyph transfer happens in affixSeal

						GLog.p(Messages.get(BrokenSeal.class, "affix"));
						Dungeon.hero.sprite.operate(Dungeon.hero.pos);
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						armor.affixSeal(BrokenSeal.this);
					}

					@Override
					public void hide() {
						super.hide();
						Dungeon.hero.next();
					}
				});

			} else {
				if (outgoing == this) {
					detach(Dungeon.hero.belongings.backpack);
				} else if (outgoing instanceof Armor){
					((Armor) outgoing).detachSeal();
				}

				GLog.p(Messages.get(BrokenSeal.class, "affix"));
				Dungeon.hero.sprite.operate(Dungeon.hero.pos);
				Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
				armor.affixSeal(this);
				Dungeon.hero.next();
			}
		}
	}

	@Override
	public String name() {
		return glyph != null ? glyph.name( super.name() ) : super.name();
	}

	@Override
	public String info() {
		String info = super.info();
		if (glyph != null){
			info += "\n\n" + Messages.get(this, "inscribed", glyph.name());
			info += " " + glyph.desc();
		}
		return info;
	}

	@Override
	//scroll of upgrade can be used directly once, same as upgrading armor the seal is affixed to then removing it.
	public boolean isUpgradable() {
		return level() == 0;
	}

	protected static WndBag.ItemSelector armorSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return  Messages.get(BrokenSeal.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Armor;
		}

		@Override
		public void onSelect( Item item ) {
			if (item instanceof Armor) {
				BrokenSeal seal = (BrokenSeal) curItem;
				seal.affixToArmor((Armor)item, seal);
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

		{
			type = buffType.POSITIVE;

			detachesAtZero = false;
			shieldUsePriority = 2;
		}

		private Armor armor;

		private int cooldown = 0;
		private float turnsSinceEnemies = 0;
		private int initialShield = 0;

		private static int COOLDOWN_START = 150;

		@Override
		public int icon() {
			if (coolingDown() || shielding() > 0 || cooldown < 0){
				return BuffIndicator.SEAL_SHIELD;
			} else {
				return BuffIndicator.NONE;
			}
		}

		@Override
		public void tintIcon(Image icon) {
			icon.resetColor();
			if (coolingDown() && shielding() == 0){
				icon.brightness(0.3f);
			} else if (cooldown < 0) {
				icon.invert();
			}
		}

		@Override
		public float iconFadePercent() {
			if (shielding() > 0){
				return GameMath.gate(0, 1f - shielding()/(float)initialShield, 1);
			} else if (coolingDown()){
				return GameMath.gate(0, cooldown / (float)COOLDOWN_START, 1);
			} else if (cooldown < 0) {
				return GameMath.gate(0, (COOLDOWN_START+cooldown) / (float)COOLDOWN_START, 1);
			} else {
				return 0;
			}
		}

		@Override
		public String iconTextDisplay() {
			if (shielding() > 0){
				return Integer.toString(shielding());
			} else if (coolingDown() || cooldown < 0){
				return Integer.toString(cooldown);
			} else {
				return "";
			}
		}

		@Override
		public String desc() {
			if (shielding() > 0) {
				return Messages.get(this, "desc_active", shielding(), cooldown);
			} else if (cooldown < 0) {
				return Messages.get(this, "desc_negative_cooldown", cooldown);
			} else {
				return Messages.get(this, "desc_cooldown", cooldown);
			}
		}

		@Override
		public synchronized boolean act() {
			if (cooldown > 0 && Regeneration.regenOn()){
				cooldown--;
			}

			if (shielding() > 0){
				if (Dungeon.hero.visibleEnemies() == 0 && Dungeon.hero.buff(Combo.class) == null){
					turnsSinceEnemies += HoldFast.buffDecayFactor(target);
					if (turnsSinceEnemies >= 5){
						if (cooldown > 0) {
							float percentLeft = shielding() / (float)initialShield;
							//max of 50% cooldown refund
							cooldown = Math.max(0, (int)(cooldown - COOLDOWN_START * (percentLeft / 2f)));
						}
						decShield(shielding());
					}
				} else {
					turnsSinceEnemies = 0;
				}
			}
			
			if (shielding() <= 0 && maxShield() <= 0 && cooldown == 0){
				detach();
			}
			
			spend(TICK);
			return true;
		}

		public synchronized void activate() {
			incShield(maxShield());
			cooldown = Math.max(0, cooldown+COOLDOWN_START);
			turnsSinceEnemies = 0;
			initialShield = maxShield();
		}

		public boolean coolingDown(){
			return cooldown > 0;
		}

		public void reduceCooldown(float percentage){
			cooldown -= Math.round(COOLDOWN_START*percentage);
			cooldown = Math.max(cooldown, -COOLDOWN_START);
		}

		public synchronized void setArmor(Armor arm){
			armor = arm;
		}

		public synchronized int maxShield() {
			//metamorphed iron will logic
			if (((Hero)target).heroClass != HeroClass.WARRIOR && ((Hero) target).hasTalent(Talent.IRON_WILL)){
				return ((Hero) target).pointsInTalent(Talent.IRON_WILL);
			}

			if (armor != null && armor.isEquipped((Hero)target) && armor.checkSeal() != null) {
				return armor.checkSeal().maxShield(armor.tier, armor.level());
			} else {
				return 0;
			}
		}

		public static final String COOLDOWN = "cooldown";
		public static final String TURNS_SINCE_ENEMIES = "turns_since_enemies";
		public static final String INITIAL_SHIELD = "initial_shield";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(COOLDOWN, cooldown);
			bundle.put(TURNS_SINCE_ENEMIES, turnsSinceEnemies);
			bundle.put(INITIAL_SHIELD, initialShield);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			if (bundle.contains(COOLDOWN)) {
				cooldown = bundle.getInt(COOLDOWN);
				turnsSinceEnemies = bundle.getFloat(TURNS_SINCE_ENEMIES);
				initialShield = bundle.getInt(INITIAL_SHIELD);

			//if we have shield from pre-3.1, have it last a bit
			} else if (shielding() > 0) {
				turnsSinceEnemies = -100;
				initialShield = shielding();
			}
		}
	}
}
