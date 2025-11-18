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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Momentum;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ParchmentScrap;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ShardOfOblivion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Explosive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.InventoryPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

abstract public class MissileWeapon extends Weapon {

	{
		stackable = true;
		quantity = defaultQuantity();
		
		bones = true;

		defaultAction = AC_THROW;
		usesTargeting = true;
	}

	//TODO maybe make this like actor IDs, instead of random? collisions unlikely, but it's messy
	public long setID = new SecureRandom().nextLong();

	//whether or not this instance of the item exists purely to trigger its effect. i.e. no dropping
	public boolean spawnedForEffect = false;
	
	protected boolean sticky = true;
	
	public static final float MAX_DURABILITY = 100;
	protected float durability = MAX_DURABILITY;
	protected float baseUses = 8;
	
	public boolean holster;
	
	//used to reduce durability from the source weapon stack, rather than the one being thrown.
	public MissileWeapon parent;
	
	public int tier;

	protected int usesToID(){
		return 10; //half of a melee weapon
	}
	
	@Override
	public int min() {
		if (Dungeon.hero != null){
			return Math.max(0, min(buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero)));
		} else {
			return Math.max(0 , min( buffedLvl() ));
		}
	}
	
	@Override
	public int min(int lvl) {
		return  2 * tier +                      //base
				lvl;                            //level scaling
	}
	
	@Override
	public int max() {
		if (Dungeon.hero != null){
			return Math.max(0, max( buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) ));
		} else {
			return Math.max(0 , max( buffedLvl() ));
		}
	}
	
	@Override
	public int max(int lvl) {
		return  5 * tier +                      //base
				tier*lvl;                       //level scaling
	}
	
	public int STRReq(int lvl){
		int req = STRReq(tier, lvl) - 1; //1 less str than normal for their tier
		if (masteryPotionBonus){
			req -= 2;
		}
		return req;
	}

	//use the parent item if this has been thrown from a parent
	public int buffedLvl(){
		if (parent != null) {
			return parent.buffedLvl();
		} else {
			return super.buffedLvl();
		}
	}

	public Item upgrade( boolean enchant ) {
		if (!bundleRestoring) {
			durability = MAX_DURABILITY;
			extraThrownLeft = false;
			quantity = defaultQuantity();
			Buff.affect(Dungeon.hero, UpgradedSetTracker.class).levelThresholds.put(setID, trueLevel()+1);
		}
		//thrown weapons don't get curse weakened
		boolean wasCursed = cursed;
		super.upgrade( enchant );
		if (wasCursed && hasCurseEnchant()){
			cursed = wasCursed;
		}
		return this;
	}
	
	@Override
	public Item upgrade() {
		if (!bundleRestoring) {
			durability = MAX_DURABILITY;
			extraThrownLeft = false;
			quantity = defaultQuantity();
			Buff.affect(Dungeon.hero, UpgradedSetTracker.class).levelThresholds.put(setID, trueLevel()+1);
		}
		return super.upgrade();
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove( AC_EQUIP );
		return actions;
	}
	
	@Override
	public boolean collect(Bag container) {
		if (container instanceof MagicalHolster) holster = true;
		return super.collect(container);
	}

	public boolean isSimilar( Item item ) {
		return trueLevel() == item.trueLevel() && getClass() == item.getClass() && setID == (((MissileWeapon) item).setID);
	}
	
	@Override
	public int throwPos(Hero user, int dst) {

		int projecting = 0;
		if (hasEnchant(Projecting.class, user)){
			projecting += 4;
		}
		if ((!(this instanceof SpiritBow.SpiritArrow) && Random.Int(3) < user.pointsInTalent(Talent.SHARED_ENCHANTMENT))){
			SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);
			if (bow != null && bow.hasEnchant(Projecting.class, user)) {
				projecting += 4;
			}
		}

		if (projecting > 0
				&& (Dungeon.level.passable[dst] || Dungeon.level.avoid[dst] || Actor.findChar(dst) != null)
				&& Dungeon.level.distance(user.pos, dst) <= Math.round(projecting * Enchantment.genericProcChanceMultiplier(user))){
			return dst;
		} else {
			return super.throwPos(user, dst);
		}
	}

	@Override
	public float accuracyFactor(Char owner, Char target) {
		float accFactor = super.accuracyFactor(owner, target);

		accFactor *= adjacentAccFactor(owner, target);

		return accFactor;
	}

	protected float adjacentAccFactor(Char owner, Char target){
		if (Dungeon.level.adjacent( owner.pos, target.pos )) {
			if (owner instanceof Hero){
				return (0.5f + 0.25f*((Hero) owner).pointsInTalent(Talent.POINT_BLANK));
			} else {
				return 0.5f;
			}
		} else {
			return 1.5f;
		}
	}

	@Override
	public void doThrow(Hero hero) {
		parent = null; //reset parent before throwing, just in case
		if (((levelKnown && level() > 0) || hasGoodEnchant() || masteryPotionBonus || enchantHardened)
				&& !extraThrownLeft && quantity() == 1 && durabilityLeft() <= durabilityPerUse()){
			GameScene.show(new WndOptions(new ItemSprite(this), Messages.titleCase(title()),
					Messages.get(MissileWeapon.class, "break_upgraded_warn_desc"),
					Messages.get(MissileWeapon.class, "break_upgraded_warn_yes"),
					Messages.get(MissileWeapon.class, "break_upgraded_warn_no")){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						MissileWeapon.super.doThrow(hero);
					} else {
						QuickSlotButton.cancel();
						InventoryPane.cancelTargeting();
					}
				}

				@Override
				public void onBackPressed() {
					super.onBackPressed();
					QuickSlotButton.cancel();
					InventoryPane.cancelTargeting();
				}
			});

		} else {
			super.doThrow(hero);
		}
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
			parent = null;

			//metamorphed seer shot logic
			if (curUser.hasTalent(Talent.SEER_SHOT)
					&& curUser.heroClass != HeroClass.HUNTRESS
					&& curUser.buff(Talent.SeerShotCooldown.class) == null){
				if (Actor.findChar(cell) == null) {
					RevealedArea a = Buff.affect(curUser, RevealedArea.class, 5 * curUser.pointsInTalent(Talent.SEER_SHOT));
					a.depth = Dungeon.depth;
					a.pos = cell;
					Buff.affect(curUser, Talent.SeerShotCooldown.class, 20f);
				}
			}

			if (!spawnedForEffect) super.onThrow( cell );
		} else {
			if (!curUser.shoot( enemy, this )) {
				rangedMiss( cell );
			} else {
				
				rangedHit( enemy, cell );

			}
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (attacker == Dungeon.hero && Random.Int(3) < Dungeon.hero.pointsInTalent(Talent.SHARED_ENCHANTMENT)){
			SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);
			if (bow != null && bow.enchantment != null && Dungeon.hero.buff(MagicImmune.class) == null) {
				damage = bow.enchantment.proc(this, attacker, defender, damage);
			}
		}

		if ((cursed || hasCurseEnchant()) && !cursedKnown){
			GLog.n(Messages.get(this, "curse_discover"));
		}
		cursedKnown = true;
		if (parent != null) parent.cursedKnown = true;

		//instant ID with the right talent
		if (attacker == Dungeon.hero && Dungeon.hero.pointsInTalent(Talent.SURVIVALISTS_INTUITION) == 2){
			usesLeftToID = Math.min(usesLeftToID, 0);
			availableUsesToID =  Math.max(usesLeftToID, 0);
		}

		int result = super.proc(attacker, defender, damage);

		//handle ID progress over parent/child
		if (parent != null && parent.usesLeftToID > usesLeftToID){
			float diff = parent.usesLeftToID - usesLeftToID;
			parent.usesLeftToID -= diff;
			parent.availableUsesToID -= diff;
			if (usesLeftToID <= 0) {
				if (ShardOfOblivion.passiveIDDisabled()){
					parent.setIDReady();
				} else {
					parent.identify();
				}
			}
		}

		if (!isIdentified() && ShardOfOblivion.passiveIDDisabled()){
			Buff.prolong(curUser, ShardOfOblivion.ThrownUseTracker.class, 50f);
		}

		return result;
	}

	@Override
	public Item virtual() {
		Item item = super.virtual();

		((MissileWeapon)item).setID = setID;

		return item;
	}

	public int defaultQuantity(){
		return 3;
	}

	//mainly used to track warnings relating to throwing the last upgraded one, not super accurate
	public boolean extraThrownLeft = false;

	@Override
	public Item random() {
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		int n = 0;
		if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}
		level(n);

		//we use a separate RNG here so that variance due to things like parchment scrap
		//does not affect levelgen
		Random.pushGenerator(Random.Long());

			//30% chance to be cursed
			//10% chance to be enchanted
			float effectRoll = Random.Float();
			if (effectRoll < 0.3f * ParchmentScrap.curseChanceMultiplier()) {
				enchant(Enchantment.randomCurse());
				cursed = true;
			} else if (effectRoll >= 1f - (0.1f * ParchmentScrap.enchantChanceMultiplier())){
				enchant();
			}

		Random.popGenerator();

		return this;
	}

	public String status() {
		//show quantity even when it is 1
		return Integer.toString( quantity );
	}
	
	@Override
	public float castDelay(Char user, int cell) {
		if (Actor.findChar(cell) != null && Actor.findChar(cell) != user){
			return delayFactor( user );
		} else {
			return super.castDelay(user, cell);
		}
	}
	
	protected void rangedHit( Char enemy, int cell ){
		decrementDurability();
		if (durability > 0 && !spawnedForEffect){
			//attempt to stick the missile weapon to the enemy, just drop it if we can't.
			if (sticky && enemy != null && enemy.isActive() && enemy.alignment != Char.Alignment.ALLY){
				PinCushion p = Buff.affect(enemy, PinCushion.class);
				if (p.target == enemy){
					p.stick(this);
					return;
				}
			}
			Dungeon.level.drop( this, cell ).sprite.drop();
		}
	}
	
	protected void rangedMiss( int cell ) {
		parent = null;
		if (!spawnedForEffect) super.onThrow(cell);
	}

	public float durabilityLeft(){
		return durability;
	}

	public void repair( float amount ){
		durability += amount;
		durability = Math.min(durability, MAX_DURABILITY);
	}

	public void damage( float amount ){
		durability -= amount;
		durability = Math.max(durability, 1); //cannot break from doing this
	}

	public final float durabilityPerUse(){
		return durabilityPerUse(level());
	}

	//classes that add steps onto durabilityPerUse can turn rounding off, to do their own rounding after more logic
	protected boolean useRoundingInDurabilityCalc = true;

	public float durabilityPerUse( int level ){
		float usages = baseUses * (float)(Math.pow(1.5f, level));

		//+50%/75% durability
		if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.DURABLE_PROJECTILES)){
			usages *= 1.25f + (0.25f*Dungeon.hero.pointsInTalent(Talent.DURABLE_PROJECTILES));
		}
		if (holster) {
			usages *= MagicalHolster.HOLSTER_DURABILITY_FACTOR;
		}

		//+50% durability on speed aug, -33% durability on damage aug
		usages /= augment.delayFactor(1f);

		if (Dungeon.hero != null) usages *= RingOfSharpshooting.durabilityMultiplier( Dungeon.hero );

		//at 100 uses, items just last forever.
		if (usages >= 100f) return 0;

		if (useRoundingInDurabilityCalc){
			usages = Math.round(usages);
			//add a tiny amount to account for rounding error for calculations like 1/3
			return (MAX_DURABILITY/usages) + 0.001f;
		} else {
			//rounding can be disabled for classes that override durability per use
			return MAX_DURABILITY/usages;
		}
	}
	
	protected void decrementDurability(){
		//if this weapon was thrown from a source stack, degrade that stack.
		//unless a weapon is about to break, then break the one being thrown
		if (parent != null){
			if (parent.durability <= parent.durabilityPerUse()){
				durability = 0;
				parent.durability = MAX_DURABILITY;
				parent.extraThrownLeft = false;
				if (parent.durabilityPerUse() < 100f) {
					GLog.n(Messages.get(this, "has_broken"));
				}
			} else {
				parent.durability -= parent.durabilityPerUse();
				if (parent.durability > 0 && parent.durability <= parent.durabilityPerUse()){
					GLog.w(Messages.get(this, "about_to_break"));
				}
			}
			parent = null;
		} else {
			durability -= durabilityPerUse();
			if (durability > 0 && durability <= durabilityPerUse()){
				GLog.w(Messages.get(this, "about_to_break"));
			} else if (durabilityPerUse() < 100f && durability <= 0){
				GLog.n(Messages.get(this, "has_broken"));
			}
		}
	}
	
	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));
		
		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Hero.heroDamageIntRange( 0, exStr );
			}
			if (owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()) {
				damage = Math.round(damage * (1f + 0.15f * ((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM)));
			}
		}
		
		return damage;
	}
	
	@Override
	public void reset() {
		super.reset();
		durability = MAX_DURABILITY;
	}
	
	@Override
	public Item merge(Item other) {
		super.merge(other);
		if (isSimilar(other)) {
			extraThrownLeft = false;

			durability += ((MissileWeapon)other).durability;
			durability -= MAX_DURABILITY;
			while (durability <= 0){
				quantity -= 1;
				durability += MAX_DURABILITY;
			}

			//hashcode check is for pre-3.2 saves, 0 check is for darts
			if (quantity > defaultQuantity() && setID != 0 && setID != getClass().getSimpleName().hashCode()){
				quantity = defaultQuantity();
				durability = MAX_DURABILITY;
			}

			levelKnown = levelKnown || other.levelKnown;
			cursedKnown = cursedKnown || other.cursedKnown;
			if (((Weapon)other).readyToIdentify()){
				setIDReady();
			}

			masteryPotionBonus = masteryPotionBonus || ((MissileWeapon) other).masteryPotionBonus;
			enchantHardened = enchantHardened || ((MissileWeapon) other).enchantHardened;

			//if other has a curse/enchant status that's a higher priority, copy it. in the following order:
			//curse infused
			if (!curseInfusionBonus && ((MissileWeapon) other).curseInfusionBonus && ((MissileWeapon) other).hasCurseEnchant()){
				enchantment = ((MissileWeapon) other).enchantment;
				curseInfusionBonus = true;
				cursed = cursed || other.cursed;
			//enchanted
			} else if (!curseInfusionBonus && !hasGoodEnchant() && ((MissileWeapon) other).hasGoodEnchant()){
				enchantment = ((MissileWeapon) other).enchantment;
				cursed = other.cursed;
			//nothing
			} else if (!curseInfusionBonus && hasCurseEnchant() && !((MissileWeapon) other).hasCurseEnchant()){
				enchantment = ((MissileWeapon) other).enchantment;
				cursed = other.cursed;
			}
			//cursed (no copy as other cannot have a higher priority status)

			//special case for explosive, as it tracks a variable
			if (((MissileWeapon) other).enchantment instanceof Explosive
				&& enchantment instanceof Explosive){
				((Explosive) enchantment).merge((Explosive) ((MissileWeapon) other).enchantment);
			}
		}
		return this;
	}
	
	@Override
	public Item split(int amount) {
		bundleRestoring = true;
		Item split = super.split(amount);
		bundleRestoring = false;

		//unless the thrown weapon will break, split off a max durability item and
		//have it reduce the durability of the main stack. Cleaner to the player this way
		if (split != null){
			MissileWeapon m = (MissileWeapon)split;
			m.durability = MAX_DURABILITY;
			m.parent = this;
			extraThrownLeft = m.extraThrownLeft = true;

			//explosive durability is tracked only in the parent
			if (m.enchantment instanceof Explosive){
				((Explosive) m.enchantment).clear();
			}
		}
		
		return split;
	}
	
	@Override
	public boolean doPickUp(Hero hero, int pos) {
		parent = null;
		if (!UpgradedSetTracker.pickupValid(hero, this)){
			Sample.INSTANCE.play( Assets.Sounds.ITEM );
			hero.spendAndNext( pickupDelay() );
			GLog.w(Messages.get(this, "dust"));
			quantity(0);
			return true;
		} else {
			extraThrownLeft = false;
			return super.doPickUp(hero, pos);
		}
	}
	
	@Override
	public boolean isIdentified() {
		return levelKnown && cursedKnown;
	}
	
	@Override
	public String info() {

		String info = super.info();

		if (levelKnown) {
			info += "\n\n" + Messages.get(MissileWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
			if (Dungeon.hero != null) {
				if (STRReq() > Dungeon.hero.STR()) {
					info += " " + Messages.get(Weapon.class, "too_heavy");
				} else if (Dungeon.hero.STR() > STRReq()) {
					info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
				}
			}
		} else {
			info += "\n\n" + Messages.get(MissileWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
			if (Dungeon.hero != null && STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MissileWeapon.class, "probably_too_heavy");
			}
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.capitalize(Messages.get(Weapon.class, "enchanted", enchantment.name()));
			if (enchantHardened) info += " " + Messages.get(Weapon.class, "enchant_hardened");
			info += " " + enchantment.desc();
		} else if (enchantHardened){
			info += "\n\n" + Messages.get(Weapon.class, "hardened_no_enchant");
		}

		if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}

		info += "\n\n";
		String statsInfo = Messages.get(this, "stats_desc");
		if (!statsInfo.equals("")) info += statsInfo + " ";
		info += Messages.get(MissileWeapon.class, "distance");

		switch (augment) {
			case SPEED:
				info += " " + Messages.get(Weapon.class, "faster");
				break;
			case DAMAGE:
				info += " " + Messages.get(Weapon.class, "stronger");
				break;
			case NONE:
		}

		if (levelKnown) {
			if (durabilityPerUse() > 0) {
				info += "\n\n" + Messages.get(this, "uses_left",
						(int) Math.ceil(durability / durabilityPerUse()),
						(int) Math.ceil(MAX_DURABILITY / durabilityPerUse()));
			} else {
				info += "\n\n" + Messages.get(this, "unlimited_uses");
			}
		}  else {
			if (durabilityPerUse(0) > 0) {
				info += "\n\n" + Messages.get(this, "unknown_uses", (int) Math.ceil(MAX_DURABILITY / durabilityPerUse(0)));
			} else {
				info += "\n\n" + Messages.get(this, "unlimited_uses");
			}
		}
		
		return info;
	}
	
	@Override
	public int value() {
		int price = 5 * tier * quantity;
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

	private static final String SET_ID = "set_id";

	private static final String SPAWNED = "spawned";
	private static final String DURABILITY = "durability";
	private static final String EXTRA_LEFT = "extra_left";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SET_ID, setID);
		bundle.put(SPAWNED, spawnedForEffect);
		bundle.put(DURABILITY, durability);
		bundle.put(EXTRA_LEFT, extraThrownLeft);
	}
	
	private static boolean bundleRestoring = false;
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		bundleRestoring = true;
		super.restoreFromBundle(bundle);
		bundleRestoring = false;

		if (bundle.contains(SET_ID)){
			setID = bundle.getLong(SET_ID);
		//pre v3.2.0 logic
		} else {
			//if we have a higher than 0 level, assume that this was a solitary thrown wep upgrade
			//turn it into a set of full quantity
			if (level() > 0){
				//set ID will be a random long
				quantity = defaultQuantity();

			//otherwise treat all currently spawned thrown weapons of the same class as if they are part of the same set
			//darts already do this though and need no conversion
			} else if (!(this instanceof Dart)){
				levelKnown = cursedKnown = true;
				setID = getClass().getSimpleName().hashCode();
			}
		}

		spawnedForEffect = bundle.getBoolean(SPAWNED);
		durability = bundle.getFloat(DURABILITY);
		extraThrownLeft = bundle.getBoolean(EXTRA_LEFT);
	}

	public static class PlaceHolder extends MissileWeapon {

		{
			image = ItemSpriteSheet.MISSILE_HOLDER;
		}

		@Override
		public boolean isSimilar(Item item) {
			//yes, even though it uses a dart outline
			return item instanceof MissileWeapon && !(item instanceof Dart);
		}

		@Override
		public String status() {
			return null;
		}

		@Override
		public String info() {
			return "";
		}
	}

	//also used by liquid metal crafting to track when a set is consumed
	public static class UpgradedSetTracker extends Buff {

		{
			revivePersists = true;
		}

		public HashMap<Long, Integer> levelThresholds = new HashMap<>();

		public static boolean pickupValid(Hero h, MissileWeapon w){
			if (h.buff(UpgradedSetTracker.class) != null){
				HashMap<Long, Integer> levelThresholds = h.buff(UpgradedSetTracker.class).levelThresholds;
				if (levelThresholds.containsKey(w.setID)){
					return w.trueLevel() >= levelThresholds.get(w.setID);
				}
				return true;
			}
			return true;
		}

		public static final String SET_IDS = "set_ids";
		public static final String SET_LEVELS = "set_levels";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			long[] IDs = new long[levelThresholds.size()];
			int[] levels = new int[levelThresholds.size()];
			int i = 0;
			for (Long ID : levelThresholds.keySet()){
				IDs[i] = ID;
				levels[i] = levelThresholds.get(ID);
				i++;
			}
			bundle.put(SET_IDS, IDs);
			bundle.put(SET_LEVELS, levels);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			long[] IDs = bundle.getLongArray(SET_IDS);
			int[] levels = bundle.getIntArray(SET_LEVELS);
			levelThresholds.clear();
			for (int i = 0; i <IDs.length; i++){
				levelThresholds.put(IDs[i], levels[i]);
			}
		}
	}
}
