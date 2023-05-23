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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MonkEnergy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MeleeWeapon extends Weapon {

	public static String AC_ABILITY = "ABILITY";

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.DUELIST){
			Buff.affect(ch, Charger.class);
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
		super.execute(hero, action);

		if (action.equals(AC_ABILITY)){
			if (!isEquipped(hero)) {
				if (hero.hasTalent(Talent.SWIFT_EQUIP)){
					if (hero.buff(Talent.SwiftEquipCooldown.class) == null
						|| hero.buff(Talent.SwiftEquipCooldown.class).hasSecondUse()){
						execute(hero, AC_EQUIP);
					} else {
						GLog.w(Messages.get(this, "ability_need_equip"));
						usesTargeting = false;
					}
				} else {
					GLog.w(Messages.get(this, "ability_need_equip"));
					usesTargeting = false;
				}
			} else if (STRReq() > hero.STR()){
				GLog.w(Messages.get(this, "ability_low_str"));
				usesTargeting = false;
			} else if (hero.belongings.weapon == this &&
					(Buff.affect(hero, Charger.class).charges + Buff.affect(hero, Charger.class).partialCharge) < abilityChargeUse(hero)) {
				GLog.w(Messages.get(this, "ability_no_charge"));
				usesTargeting = false;
			} else if (hero.belongings.secondWep == this &&
					(Buff.affect(hero, Charger.class).secondCharges + Buff.affect(hero, Charger.class).secondPartialCharge) < abilityChargeUse(hero)) {
				GLog.w(Messages.get(this, "ability_no_charge"));
				usesTargeting = false;
			} else {

				if (targetingPrompt() == null){
					duelistAbility(hero, hero.pos);
					updateQuickslot();
				} else {
					usesTargeting = useTargeting();
					GameScene.selectCell(new CellSelector.Listener() {
						@Override
						public void onSelect(Integer cell) {
							if (cell != null) {
								duelistAbility(hero, cell);
								updateQuickslot();
							}
						}

						@Override
						public String prompt() {
							return targetingPrompt();
						}
					});
				}
			}
		}
	}

	@Override
	public boolean doEquip(Hero hero) {
		if (super.doEquip(hero)){
			ActionIndicator.refresh();
			return true;
		}
		return false;
	}

	@Override
	public boolean equipSecondary(Hero hero) {
		if (super.equipSecondary(hero)){
			ActionIndicator.refresh();
			return true;
		}
		return false;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			ActionIndicator.refresh();
			return true;
		}
		return false;
	}

	//leave null for no targeting
	public String targetingPrompt(){
		return null;
	}

	public boolean useTargeting(){
		return targetingPrompt() != null;
	}

	@Override
	public int targetingPos(Hero user, int dst) {
		return dst; //weapon abilities do not use projectile logic, no autoaim
	}

	protected void duelistAbility( Hero hero, Integer target ){
		//do nothing by default
	}

	protected void beforeAbilityUsed(Hero hero ){
		hero.belongings.abilityWeapon = this;
		Charger charger = Buff.affect(hero, Charger.class);

		if (Dungeon.hero.belongings.weapon == this) {
			charger.partialCharge -= abilityChargeUse(hero);
			while (charger.partialCharge < 0 && charger.charges > 0) {
				charger.charges--;
				charger.partialCharge++;
			}
		} else {
			charger.secondPartialCharge -= abilityChargeUse(hero);
			while (charger.secondPartialCharge < 0 && charger.secondCharges > 0) {
				charger.secondCharges--;
				charger.secondPartialCharge++;
			}
		}

		if (hero.heroClass == HeroClass.DUELIST
				&& hero.hasTalent(Talent.AGGRESSIVE_BARRIER)
				&& (hero.HP / (float)hero.HT) < 0.20f*(1+hero.pointsInTalent(Talent.AGGRESSIVE_BARRIER))){
			Buff.affect(hero, Barrier.class).setShield(3);
		}

		if (hero.buff(Talent.CombinedLethalityAbilityTracker.class) != null
				&& hero.buff(Talent.CombinedLethalityAbilityTracker.class).weapon != null
				&& hero.buff(Talent.CombinedLethalityAbilityTracker.class).weapon != this){
			Buff.affect(hero, Talent.CombinedLethalityTriggerTracker.class, 5f);
		}

		updateQuickslot();
	}

	protected void afterAbilityUsed( Hero hero ){
		hero.belongings.abilityWeapon = null;
		if (hero.hasTalent(Talent.COMBINED_LETHALITY)) {
			Talent.CombinedLethalityAbilityTracker tracker = hero.buff(Talent.CombinedLethalityAbilityTracker.class);
			if (tracker == null || tracker.weapon == this || tracker.weapon == null){
				Buff.affect(hero, Talent.CombinedLethalityAbilityTracker.class, hero.cooldown()).weapon = this;
			} else {
				//we triggered the talent, so remove the tracker
				tracker.detach();
			}
		}
		if (hero.hasTalent(Talent.COMBINED_ENERGY)){
			Talent.CombinedEnergyAbilityTracker tracker = hero.buff(Talent.CombinedEnergyAbilityTracker.class);
			if (tracker == null || tracker.energySpent == -1){
				Buff.prolong(hero, Talent.CombinedEnergyAbilityTracker.class, hero.cooldown()).wepAbilUsed = true;
			} else {
				tracker.wepAbilUsed = true;
				Buff.affect(hero, MonkEnergy.class).processCombinedEnergy(tracker);
			}
		}
		if (hero.buff(Talent.CounterAbilityTacker.class) != null){
			hero.buff(Talent.CounterAbilityTacker.class).detach();
		}
	}

	public void onAbilityKill( Hero hero ){
		if (hero.hasTalent(Talent.LETHAL_HASTE)){
			//effectively 2/3 turns of haste
			Buff.prolong(hero, Haste.class, 1.67f+hero.pointsInTalent(Talent.LETHAL_HASTE));
		}
	}

	public float abilityChargeUse( Hero hero ){
		float chargeUse = 1f;
		if (hero.buff(Talent.CounterAbilityTacker.class) != null){
			chargeUse = Math.max(0, chargeUse-0.5f*hero.pointsInTalent(Talent.COUNTER_ABILITY));
		}
		if (hero.hasTalent(Talent.LIGHTWEIGHT_CHARGE) && tier <= 3){
			// T1/2/3 get 25/20/15% charge use reduction at +3
			float chargeUseReduction = (0.30f-.05f*tier) * (hero.pointsInTalent(Talent.LIGHTWEIGHT_CHARGE)/3f);
			chargeUse *= 1f - chargeUseReduction;
		}
		return chargeUse;
	}

	public int tier;

	@Override
	public int min(int lvl) {
		return  tier +  //base
				lvl;    //level scaling
	}

	@Override
	public int max(int lvl) {
		return  5*(tier+1) +    //base
				lvl*(tier+1);   //level scaling
	}

	public int STRReq(int lvl){
		return STRReq(tier, lvl);
	}

	private static boolean evaluatingTwinUpgrades = false;
	@Override
	public int buffedLvl() {
		if (!evaluatingTwinUpgrades && isEquipped(Dungeon.hero) && Dungeon.hero.hasTalent(Talent.TWIN_UPGRADES)){
			KindOfWeapon other = null;
			if (Dungeon.hero.belongings.weapon() != this) other = Dungeon.hero.belongings.weapon();
			if (Dungeon.hero.belongings.secondWep() != this) other = Dungeon.hero.belongings.secondWep();

			if (other instanceof MeleeWeapon) {
				evaluatingTwinUpgrades = true;
				int otherLevel = other.buffedLvl();
				evaluatingTwinUpgrades = false;

				//weaker weapon needs to be 2/1/0 tiers lower, based on talent level
				if ((tier + (3 - Dungeon.hero.pointsInTalent(Talent.TWIN_UPGRADES))) <= ((MeleeWeapon) other).tier
						&& otherLevel > super.buffedLvl()) {
					return otherLevel;
				}

			}
		}
		return super.buffedLvl();
	}

	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));

		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
			}
		}
		
		return damage;
	}
	
	@Override
	public String info() {

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			} else if (Dungeon.hero.STR() > STRReq()){
				info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
			}
		} else {
			info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
		}

		String statsInfo = statsInfo();
		if (!statsInfo.equals("")) info += "\n\n" + statsInfo;

		switch (augment) {
			case SPEED:
				info += " " + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += " " + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			info += " " + enchantment.desc();
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			if (enchantment != null && enchantment.curse()) {
				info += "\n\n" + Messages.get(Weapon.class, "weak_cursed");
			} else {
				info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
			}
		}

		//the mage's staff has no ability as it can only be gained by the mage
		if (Dungeon.hero.heroClass == HeroClass.DUELIST && !(this instanceof MagesStaff)){
			info += "\n\n" + Messages.get(this, "ability_desc");
		}
		
		return info;
	}
	
	public String statsInfo(){
		return Messages.get(this, "stats_desc");
	}

	@Override
	public String status() {
		if (isEquipped(Dungeon.hero)
				&& Dungeon.hero.buff(Charger.class) != null) {
			Charger buff = Dungeon.hero.buff(Charger.class);
			if (Dungeon.hero.belongings.weapon == this) {
				return buff.charges + "/" + buff.chargeCap();
			} else {
				return buff.secondCharges + "/" + buff.secondChargeCap();
			}
		} else {
			return super.status();
		}
	}

	@Override
	public int value() {
		int price = 20 * tier;
		if (hasGoodEnchant()) {
			price *= 1.5;
		}
		if (cursedKnown && (cursed || hasCurseEnchant())) {
			price /= 2;
		}
		if (levelKnown && level() > 0) {
			price *= (level() + 1);
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	public static class Charger extends Buff implements ActionIndicator.Action {

		public int charges = 3;
		public float partialCharge;
		//offhand charge as well?

		//champion subclass
		public int secondCharges = 3;
		public float secondPartialCharge;

		@Override
		public boolean act() {
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charges < chargeCap()){
				if (lock == null || lock.regenOn()){
					partialCharge += 1/(40f-(chargeCap()-charges)); // 40 to 30 turns per charge
				}

				int points = ((Hero)target).pointsInTalent(Talent.WEAPON_RECHARGING);
				if (points > 0 && target.buff(Recharging.class) != null || target.buff(ArtifactRecharge.class) != null){
					//1 every 10 turns at +1, 6 turns at +2
					partialCharge += 1/(14f - 4f*points);
				}

				if (partialCharge >= 1){
					charges++;
					partialCharge--;
					updateQuickslot();
				}
			} else {
				partialCharge = 0;
			}

			if (Dungeon.hero.subClass == HeroSubClass.CHAMPION
					&& secondCharges < secondChargeCap()) {
				if (lock == null || lock.regenOn()) {
					// 80 to 60 turns per charge without talent
					// up to 53.333 to 40 turns per charge at max talent level
					secondPartialCharge += secondChargeMultiplier() / (40f-(secondChargeCap()-secondCharges));
				}

				if (secondPartialCharge >= 1) {
					secondCharges++;
					secondPartialCharge--;
					updateQuickslot();
				}

			} else {
				secondPartialCharge = 0;
			}

			if (ActionIndicator.action != this && Dungeon.hero.subClass == HeroSubClass.CHAMPION) {
				ActionIndicator.setAction(this);
			}

			spend(TICK);
			return true;
		}

		@Override
		public void fx(boolean on) {
			if (on && Dungeon.hero.subClass == HeroSubClass.CHAMPION) {
				ActionIndicator.setAction(this);
			}
		}

		@Override
		public void detach() {
			super.detach();
			ActionIndicator.clearAction(this);
		}

		public int chargeCap(){
			return Math.min(10, 3 + (Dungeon.hero.lvl-1)/3);
		}

		public int secondChargeCap(){
			return Math.round(chargeCap() * secondChargeMultiplier());
		}

		public float secondChargeMultiplier(){
			//50% - 75%, depending on talent
			return 0.5f + 0.0834f*Dungeon.hero.pointsInTalent(Talent.SECONDARY_CHARGE);
		}

		public void gainCharge( float charge ){
			if (charges < chargeCap()) {
				partialCharge += charge;
				while (partialCharge >= 1f) {
					charges++;
					partialCharge--;
				}
				charges = Math.min(charges, chargeCap());
				updateQuickslot();
			}
		}

		public static final String CHARGES          = "charges";
		private static final String PARTIALCHARGE   = "partialCharge";

		public static final String SECOND_CHARGES          = "second_charges";
		private static final String SECOND_PARTIALCHARGE   = "second_partialCharge";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CHARGES, charges);
			bundle.put(PARTIALCHARGE, partialCharge);
			bundle.put(SECOND_CHARGES, secondCharges);
			bundle.put(SECOND_PARTIALCHARGE, secondPartialCharge);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			charges = bundle.getInt(CHARGES);
			partialCharge = bundle.getFloat(PARTIALCHARGE);
			secondCharges = bundle.getInt(SECOND_CHARGES);
			secondPartialCharge = bundle.getFloat(SECOND_PARTIALCHARGE);
		}

		@Override
		public String actionName() {
			return Messages.get(MeleeWeapon.class, "swap");
		}

		@Override
		public int actionIcon() {
			return HeroIcon.WEAPON_SWAP;
		}

		@Override
		public Visual primaryVisual() {
			Image ico;
			if (Dungeon.hero.belongings.weapon == null){
				ico = new HeroIcon(this);
 			} else {
				ico = new ItemSprite(Dungeon.hero.belongings.weapon);
			}
			ico.width += 4; //shift slightly to the left to separate from smaller icon
			return ico;
		}

		@Override
		public Visual secondaryVisual() {
			Image ico;
			if (Dungeon.hero.belongings.secondWep == null){
				ico = new HeroIcon(this);
			} else {
				ico = new ItemSprite(Dungeon.hero.belongings.secondWep);
			}
			ico.scale.set(PixelScene.align(0.51f));
			ico.brightness(0.6f);
			return ico;
		}

		@Override
		public int indicatorColor() {
			return 0x5500BB;
		}

		@Override
		public void doAction() {
			if (Dungeon.hero.subClass != HeroSubClass.CHAMPION){
				return;
			}

			if (Dungeon.hero.belongings.secondWep == null && Dungeon.hero.belongings.backpack.items.size() >= Dungeon.hero.belongings.backpack.capacity()){
				GLog.w(Messages.get(MeleeWeapon.class, "swap_full"));
				return;
			}

			KindOfWeapon temp = Dungeon.hero.belongings.weapon;
			Dungeon.hero.belongings.weapon = Dungeon.hero.belongings.secondWep;
			Dungeon.hero.belongings.secondWep = temp;

			Dungeon.hero.sprite.operate(Dungeon.hero.pos);
			Sample.INSTANCE.play(Assets.Sounds.UNLOCK);

			ActionIndicator.setAction(this);
			Item.updateQuickslot();
			AttackIndicator.updateState();
		}
	}

}
