/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class RingOfForce extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_FORCE;
		buffClass = Force.class;
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
				hero.buff(BrawlersStance.class).active = false;
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
			int dmg = Hero.heroDamageIntRange(min(level, tier), max(level, tier));
			if (hero.buff(BrawlersStance.class) != null
				&& hero.buff(BrawlersStance.class).active){
				// 3+tier base dmg, roughly +60%->45% dmg at T1->5
				// lvl*((4+2*tier)/8) scaling, +50% dmg
				dmg += Math.round(3+tier+(level*((4+2*tier)/8f)));
			}
			return dmg;
		} else {
			//attack without any ring of force influence
			return Hero.heroDamageIntRange(1, Math.max(hero.STR()-8, 1));
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
		float tier = tier(Dungeon.hero != null ? Dungeon.hero.STR() : 10);
		if (isIdentified()) {
			int level = soloBuffedBonus();
			String info = Messages.get(this, "stats", min(level, tier), max(level, tier), level);
			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
				level = combinedBuffedBonus(Dungeon.hero);
				info += "\n\n" + Messages.get(this, "combined_stats", min(level, tier), max(level, tier), level);
			}
			return info;
		} else {
			return Messages.get(this, "typical_stats", min(1, tier), max(1, tier), 1);
		}
	}

	@Override
	public String upgradeStat1(int level) {
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		float tier = tier(Dungeon.hero != null ? Dungeon.hero.STR() : 10);
		return min(level+1, tier) + "-" + max(level+1, tier);
	}

	@Override
	public String upgradeStat2(int level) {
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Integer.toString(level+1);
	}

	@Override
	public String upgradeStat3(int level) {
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.DUELIST){
			float tier = tier(Dungeon.hero != null ? Dungeon.hero.STR() : 10);
			int bonus = Math.round(3+tier+(level*((4+2*tier)/8f)));
			return (min(level+1, tier) + bonus) + "-" + (max(level+1, tier) + bonus);
		} else {
			return null;
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
				if (!hero.buff(BrawlersStance.class).active){
					hero.buff(BrawlersStance.class).reset();
				} else {
					hero.buff(BrawlersStance.class).active = false;
				}
				BuffIndicator.refreshHero();
				AttackIndicator.updateState();
			} else if (!isEquipped(hero)) {
				GLog.w(Messages.get(MeleeWeapon.class, "ability_need_equip"));

			} else {
				Buff.affect(hero, BrawlersStance.class).reset();
				AttackIndicator.updateState();
			}
		} else {
			super.execute(hero, action);
		}
	}

	@Override
	public String info() {
		String info = super.info();

		if (Dungeon.hero != null && Dungeon.hero.heroClass == HeroClass.DUELIST
			&& (anonymous || isIdentified() || isEquipped(Dungeon.hero))){
			//0 if unidentified, solo level if unequipped, combined level if equipped
			int level = isIdentified() ? (isEquipped(Dungeon.hero) ? getBuffedBonus(Dungeon.hero, Force.class) : soloBuffedBonus()) : 0;
			float tier = tier(Dungeon.hero.STR());
			int dmgBoost = Math.round(3+tier+(level*((4+2*tier)/8f)));
			if (isIdentified()) {
				info += "\n\n" + Messages.get(this, "ability_desc", min(level, tier)+dmgBoost, max(level, tier)+dmgBoost);
			} else {
				info += "\n\n" + Messages.get(this, "typical_ability_desc",  min(level, tier)+dmgBoost, max(level, tier)+dmgBoost);
			}
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
		if (stance != null && stance.active){
			//clear the buff if no ring of force is equipped
			if (hero.buff(RingOfForce.Force.class) == null){
				stance.active = false;
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
		if (stance != null && stance.active){
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
		if (stance != null && stance.active){
			return true;
		}
		return false;
	}

	public static class BrawlersStance extends Buff {

		{
			announced = true;
			type = buffType.POSITIVE;
		}

		//buff must be active for at least 50 turns, to discourage micro-managing for max charges
		public boolean active;
		private int minTurnsLeft;

		public void reset(){
			active = true;
			minTurnsLeft = 50;
		}

		@Override
		public int icon() {
			return active ? BuffIndicator.DUEL_BRAWL : BuffIndicator.NONE;
		}

		@Override
		public boolean act() {
			minTurnsLeft --;

			if (!active && minTurnsLeft <= 0){
				detach();
			}

			spend(TICK);
			return true;
		}

		public static final String ACTIVE = "active";
		public static final String MIN_TURNS_LEFT = "min_turns_left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ACTIVE, active);
			bundle.put(MIN_TURNS_LEFT, minTurnsLeft);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			active = bundle.getBoolean(ACTIVE);
			minTurnsLeft = bundle.getInt(MIN_TURNS_LEFT);
		}
	}
}

