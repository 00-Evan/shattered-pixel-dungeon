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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RingOfForce extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_FORCE;
	}

	@Override
	protected RingBuff buff( ) {
		return new Force();
	}
	
	public static int armedDamageBonus( Char ch ){
		return getBuffedBonus( ch, Force.class);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (hero.buff(BrawlersStance.class) != null && hero.buff(Force.class) == null){
				//clear brawler's stance if no ring of force is equipped
				hero.buff(BrawlersStance.class).detach();
			}
			return true;
		} else {
			return false;
		}
	}
	
	// *** Weapon-like properties ***

	private static float tier(int str){
		float tier = Math.max(1, (str - 8)/2f);
		//each str point after 18 is half as effective
		if (tier > 5){
			tier = 5 + (tier - 5) / 2f;
		}
		return tier;
	}

	public static int damageRoll( Hero hero ){
		if (hero.buff(Force.class) != null
				&& hero.buff(MonkEnergy.MonkAbility.UnarmedAbilityTracker.class) == null) {
			int level = getBuffedBonus(hero, Force.class);
			float tier = tier(hero.STR());
			return Random.NormalIntRange(min(level, tier), max(level, tier));
		} else {
			//attack without any ring of force influence
			return Random.NormalIntRange(1, Math.max(hero.STR()-8, 1));
		}
	}

	//same as equivalent tier weapon
	private static int min(int lvl, float tier){
		if (lvl <= 0) tier = 1; //tier is forced to 1 if cursed

		return Math.max( 0, Math.round(
				tier +  //base
				lvl     //level scaling
		));
	}

	//same as equivalent tier weapon
	private static int max(int lvl, float tier){
		if (lvl <= 0) tier = 1; //tier is forced to 1 if cursed

		return Math.max( 0, Math.round(
				5*(tier+1) +    //base
				lvl*(tier+1)    //level scaling
		));
	}

	@Override
	public String statsInfo() {
		float tier = tier(Dungeon.hero.STR());
		if (isIdentified()) {
			int level = soloBuffedBonus();
			return Messages.get(this, "stats", min(level, tier), max(level, tier), level);
		} else {
			return Messages.get(this, "typical_stats", min(1, tier), max(1, tier), 1);
		}
	}

	public class Force extends RingBuff {
	}

	//Duelist stuff

	public static String AC_ABILITY = "ABILITY";

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.DUELIST){
			Buff.affect(ch, MeleeWeapon.Charger.class);
		}
	}

	@Override
	public String defaultAction() {
		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.DUELIST){
			return AC_ABILITY;
		} else {
			return super.defaultAction();
		}
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && hero.heroClass == HeroClass.DUELIST){
			actions.add(AC_ABILITY);
		}
		return actions;
	}

	@Override
	public String actionName(String action, Hero hero) {
		if (action.equals(AC_ABILITY)){
			return Messages.upperCase(Messages.get(this, "ability_name"));
		} else {
			return super.actionName(action, hero);
		}
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_ABILITY)){
			if (hero.buff(BrawlersStance.class) != null){
				hero.buff(BrawlersStance.class).detach();
				AttackIndicator.updateState();
			} else if (!isEquipped(hero)) {
				GLog.w(Messages.get(MeleeWeapon.class, "ability_need_equip"));

			} else if ((Buff.affect(hero, MeleeWeapon.Charger.class).charges + Buff.affect(hero, MeleeWeapon.Charger.class).partialCharge)
					< BrawlersStance.HIT_CHARGE_USE){
				GLog.w(Messages.get(MeleeWeapon.class, "ability_no_charge"));

			} else {
				Buff.affect(hero, BrawlersStance.class);
				AttackIndicator.updateState();
			}
		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public String info() {
		String info = super.info();

		if (Dungeon.hero.heroClass == HeroClass.DUELIST
			&& (anonymous || isIdentified() || isEquipped(Dungeon.hero))){
			info += "\n\n" + Messages.get(this, "ability_desc");
		}

		return info;
	}

	public static boolean fightingUnarmed( Hero hero ){
		if (hero.belongings.attackingWeapon() == null
			|| hero.buff(MonkEnergy.MonkAbility.UnarmedAbilityTracker.class) != null){
			return true;
		}
		if (hero.belongings.thrownWeapon != null || hero.belongings.abilityWeapon != null){
			return false;
		}
		BrawlersStance stance = hero.buff(BrawlersStance.class);
		if (stance != null && stance.hitsLeft() > 0){
			//clear the buff if no ring of force is equipped
			if (hero.buff(RingOfForce.Force.class) == null){
				stance.detach();
				AttackIndicator.updateState();
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public static boolean unarmedGetsWeaponEnchantment( Hero hero ){
		if (hero.belongings.attackingWeapon() == null){
			return false;
		}
		if (hero.buff(MonkEnergy.MonkAbility.UnarmedAbilityTracker.class) != null){
			return hero.buff(MonkEnergy.MonkAbility.FlurryEmpowerTracker.class) != null;
		}
		BrawlersStance stance = hero.buff(BrawlersStance.class);
		if (stance != null && stance.hitsLeft() > 0){
			return true;
		}
		return false;
	}

	public static boolean unarmedGetsWeaponAugment(Hero hero ){
		if (hero.belongings.attackingWeapon() == null
			|| hero.buff(MonkEnergy.MonkAbility.UnarmedAbilityTracker.class) != null){
			return false;
		}
		BrawlersStance stance = hero.buff(BrawlersStance.class);
		if (stance != null && stance.hitsLeft() > 0){
			return true;
		}
		return false;
	}

	public static class BrawlersStance extends Buff {

		public static float HIT_CHARGE_USE = 0.25f;

		{
			announced = true;
			type = buffType.POSITIVE;
		}

		public int hitsLeft(){
			MeleeWeapon.Charger charger = Buff.affect(target, MeleeWeapon.Charger.class);
			float charges = charger.charges;
			charges += charger.partialCharge;

			return (int)(charges/HIT_CHARGE_USE);
		}

		@Override
		public int icon() {
			return BuffIndicator.DUEL_BRAWL;
		}

		@Override
		public void tintIcon(Image icon) {
			if (hitsLeft() == 0){
				icon.brightness(0.25f);
			} else {
				icon.resetColor();
			}
		}

		@Override
		public float iconFadePercent() {
			float usableCharges = hitsLeft()*HIT_CHARGE_USE;

			return 1f - (usableCharges /  Buff.affect(target, MeleeWeapon.Charger.class).chargeCap());
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(hitsLeft());
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", hitsLeft());
		}
	}
}

