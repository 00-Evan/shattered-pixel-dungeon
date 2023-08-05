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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
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
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.NinjaClothe;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PaladinHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

abstract public class MissileWeapon extends Weapon {

	{
		stackable = true;
		levelKnown = true;
		
		bones = true;

		defaultAction = AC_THROW;
		usesTargeting = true;
	}
	
	protected boolean sticky = true;
	
	public static final float MAX_DURABILITY = 100;
	protected float durability = MAX_DURABILITY;
	protected float baseUses = 10;
	
	public boolean holster;
	
	//used to reduce durability from the source weapon stack, rather than the one being thrown.
	protected MissileWeapon parent;
	
	public int tier;
	
	@Override
	public int min() {
		return Math.max(0, min( buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) ));
	}
	
	@Override
	public int min(int lvl) {
		return  2 * tier +                      //base
				(tier == 1 ? lvl : 2*lvl);      //level scaling
	}
	
	@Override
	public int max() {
		return Math.max(0, max( buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) ));
	}
	
	@Override
	public int max(int lvl) {
		return  5 * tier +                      //base
				(tier == 1 ? 2*lvl : tier*lvl); //level scaling
	}
	
	public int STRReq(int lvl){
		return STRReq(tier, lvl) - 1; //1 less str than normal for their tier
	}
	
	@Override
	//FIXME some logic here assumes the items are in the player's inventory. Might need to adjust
	public Item upgrade() {
		if (!bundleRestoring) {
			durability = MAX_DURABILITY;
			if (quantity > 1) {
				MissileWeapon upgraded = (MissileWeapon) split(1);
				upgraded.parent = null;
				
				upgraded = (MissileWeapon) upgraded.upgrade();
				
				//try to put the upgraded into inventory, if it didn't already merge
				if (upgraded.quantity() == 1 && !upgraded.collect()) {
					Dungeon.level.drop(upgraded, Dungeon.hero.pos);
				}
				updateQuickslot();
				return upgraded;
			} else {
				super.upgrade();
				
				Item similar = Dungeon.hero.belongings.getSimilar(this);
				if (similar != null){
					detach(Dungeon.hero.belongings.backpack);
					Item result = similar.merge(this);
					updateQuickslot();
					return result;
				}
				updateQuickslot();
				return this;
			}
			
		} else {
			return super.upgrade();
		}
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
	
	@Override
	public int throwPos(Hero user, int dst) {

		boolean projecting = hasEnchant(Projecting.class, user);
		if (!projecting && Random.Int(3) < user.pointsInTalent(Talent.SHARED_ENCHANTMENT)){
			if (this instanceof Dart && ((Dart) this).crossbowHasEnchant(Dungeon.hero)){
				//do nothing
			} else {
				SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);
				if (bow != null && bow.hasEnchant(Projecting.class, user)) {
					projecting = true;
				}
			}
		}

		if (projecting
				&& (Dungeon.level.passable[dst] || Dungeon.level.avoid[dst])
				&& Dungeon.level.distance(user.pos, dst) <= Math.round(4 * Enchantment.genericProcChanceMultiplier(user))){
			return dst;
		} else {
			return super.throwPos(user, dst);
		}
	}

	@Override
	public float accuracyFactor(Char owner, Char target) {
		float accFactor = super.accuracyFactor(owner, target);
		if (owner instanceof Hero && owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()){
			accFactor *= 1f + 0.2f*((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM);
		}

		accFactor *= adjacentAccFactor(owner, target);

		return accFactor;
	}

	protected float adjacentAccFactor(Char owner, Char target){
		if (Dungeon.level.adjacent( owner.pos, target.pos )) {
			if (owner instanceof Hero){
				return (0.5f + 0.2f*((Hero) owner).pointsInTalent(Talent.POINT_BLANK));
			} else {
				return 0.5f;
			}
		} else {
			return 1.5f;
		}
	}

	@Override
	public void doThrow(Hero hero) {
		parent = null; //reset parent before throwing, just incase
		super.doThrow(hero);
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

			super.onThrow( cell );
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
			if (this instanceof Dart && ((Dart) this).crossbowHasEnchant(Dungeon.hero)){
				//do nothing
			} else {
				SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);
				if (bow != null && bow.enchantment != null && Dungeon.hero.buff(MagicImmune.class) == null) {
					damage = bow.enchantment.proc(this, attacker, defender, damage);
				}
			}
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public Item random() {
		if (!stackable) return this;
		
		//2: 66.67% (2/3)
		//3: 26.67% (4/15)
		//4: 6.67%  (1/15)
		quantity = 2;
		if (Random.Int(3) == 0) {
			quantity++;
			if (Random.Int(5) == 0) {
				quantity++;
			}
		}
		return this;
	}

	public String status() {
		//show quantity even when it is 1
		return Integer.toString( quantity );
	}
	
	@Override
	public float castDelay(Char user, int dst) {
		return delayFactor( user );
	}
	
	protected void rangedHit( Char enemy, int cell ){
		decrementDurability();
		if (durability > 0){
			//attempt to stick the missile weapon to the enemy, just drop it if we can't.
			if (sticky && enemy != null && enemy.isAlive() && enemy.alignment != Char.Alignment.ALLY){
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
		super.onThrow(cell);
	}

	public float durabilityLeft(){
		return durability;
	}

	public void repair( float amount ){
		durability += amount;
		durability = Math.min(durability, MAX_DURABILITY);
	}
	
	public float durabilityPerUse(){
		float usages = baseUses * (float)(Math.pow(3, level()));

		//+50%/75% durability
		if (Dungeon.hero.hasTalent(Talent.DURABLE_PROJECTILES)){
			usages *= 1.25f + (0.25f*Dungeon.hero.pointsInTalent(Talent.DURABLE_PROJECTILES));
		}
		if (holster) {
			usages *= MagicalHolster.HOLSTER_DURABILITY_FACTOR;
		}
		
		usages *= RingOfSharpshooting.durabilityMultiplier( Dungeon.hero );
		
		//at 100 uses, items just last forever.
		if (usages >= 100f) return 0;

		usages = Math.round(usages);
		
		//add a tiny amount to account for rounding error for calculations like 1/3
		return (MAX_DURABILITY/usages) + 0.001f;
	}
	
	protected void decrementDurability(){
		//if this weapon was thrown from a source stack, degrade that stack.
		//unless a weapon is about to break, then break the one being thrown
		if (parent != null){
			if (parent.durability <= parent.durabilityPerUse()){
				durability = 0;
				parent.durability = MAX_DURABILITY;
			} else {
				parent.durability -= parent.durabilityPerUse();
				if (parent.durability > 0 && parent.durability <= parent.durabilityPerUse()){
					if (level() <= 0)GLog.w(Messages.get(this, "about_to_break"));
					else             GLog.n(Messages.get(this, "about_to_break"));
				}
			}
			parent = null;
		} else {
			durability -= durabilityPerUse();
			if (durability > 0 && durability <= durabilityPerUse()){
				if (level() <= 0)GLog.w(Messages.get(this, "about_to_break"));
				else             GLog.n(Messages.get(this, "about_to_break"));
			}
		}
	}
	
	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));
		
		if (owner instanceof Hero) {
			int exStr = ((Hero)owner).STR() - STRReq();
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
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
			durability += ((MissileWeapon)other).durability;
			durability -= MAX_DURABILITY;
			while (durability <= 0){
				quantity -= 1;
				durability += MAX_DURABILITY;
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
		}
		
		return split;
	}
	
	@Override
	public boolean doPickUp(Hero hero, int pos) {
		parent = null;
		return super.doPickUp(hero, pos);
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {

		String info = desc();
		
		info += "\n\n" + Messages.get( MissileWeapon.class, "stats",
				tier,
				Math.round(augment.damageFactor(min())),
				Math.round(augment.damageFactor(max())),
				STRReq());

		if (STRReq() > Dungeon.hero.STR()) {
			info += " " + Messages.get(Weapon.class, "too_heavy");
		} else if (Dungeon.hero.STR() > STRReq()){
			info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}

		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");
		
		info += "\n\n" + Messages.get(this, "durability");
		
		if (durabilityPerUse() > 0){
			info += " " + Messages.get(this, "uses_left",
					(int)Math.ceil(durability/durabilityPerUse()),
					(int)Math.ceil(MAX_DURABILITY/durabilityPerUse()));
		} else {
			info += " " + Messages.get(this, "unlimited_uses");
		}
		
		
		return info;
	}
	
	@Override
	public int value() {
		return 6 * tier * quantity * (level() + 1);
	}
	
	private static final String DURABILITY = "durability";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DURABILITY, durability);
	}
	
	private static boolean bundleRestoring = false;
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		bundleRestoring = true;
		super.restoreFromBundle(bundle);
		bundleRestoring = false;
		durability = bundle.getFloat(DURABILITY);
	}



	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			MissileWeapon weapon1 = null;
			MissileWeapon weapon2 = null;
			int level = -1;
			int count = 0;
			for (Item ingredient : ingredients) {
				if (ingredient.quantity() > 0 && ingredient instanceof MissileWeapon) {
					MissileWeapon missile = (MissileWeapon) ingredient;
					if (weapon1 == null) {
						weapon1 = missile;
						level = missile.level();
						count++;
					} else if (missile.getClass() == weapon1.getClass() && missile.level() == level && weapon2 == null) {
						weapon2 = missile;
						count++;
					}
				}
			}
			return (count == 2 && ((weapon1.quantity() == 1 && weapon2.quantity() == 1) || (weapon1.quantity() == 2)));
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 0;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) {
				return null;
			}

			MissileWeapon weapon1 = null;
			MissileWeapon weapon2 = null;
			int level = -1;
			int count = 0;
			for (Item ingredient : ingredients) {
				if (ingredient.quantity() > 0 && ingredient instanceof MissileWeapon) {
					MissileWeapon missile = (MissileWeapon) ingredient;
					if (weapon1 == null) {
						weapon1 = missile;
						level = missile.level();
						count++;
					} else if (missile.getClass() == weapon1.getClass() && missile.level() == level && weapon2 == null) {
						weapon2 = missile;
						count++;
					}
				}
			}

			if (count == 2) {
				if (weapon1.quantity() == 1 && weapon2.quantity() == 1) {
					weapon1.detach(null);
					weapon2.detach(null);

					Item upgradedWeapon = weapon1.upgrade();

					upgradedWeapon.quantity(1);
					return upgradedWeapon;
				} else if (weapon1.quantity() == 2) {
					weapon1.quantity(1);
					Item upgradedWeapon = weapon1.upgrade();
					upgradedWeapon.quantity(1);
					return upgradedWeapon;
				}
			}

			return null;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			MissileWeapon weapon1 = null;
			MissileWeapon weapon2 = null;
			int level = -1;
			for (Item ingredient : ingredients) {
				if (ingredient instanceof MissileWeapon) {
					MissileWeapon missile = (MissileWeapon) ingredient;
					if (weapon1 == null) {
						weapon1 = missile;
						level = missile.level();
					} else if (missile.getClass() == weapon1.getClass() && missile.level() == level && weapon2 == null) {
						weapon2 = missile;
					}
				}
			}

			if (weapon1 != null && weapon2 != null) {
				Item upgradedWeapon = weapon1.upgrade();
				return upgradedWeapon;
			} else {
				return null;
			}
		}
	}

	/*
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {
		Item missile;
		Item missile2;

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean missle = false;
			boolean missle2 = false;
			boolean liqued = false;

			for (Item ingredient : ingredients){

				if ( missile == null && ingredient instanceof MissileWeapon ) {
					missile = ingredient;
					if (ingredient.quantity() > 0) {
						missle = true;
					}
				} else if ( missile != null && ingredient instanceof MissileWeapon ) {
					missile2 = ingredient;
					if ( ingredient.quantity() > 0  && missile2.level() == missile.level() ) {
						missle2 = true;
					}
				} /*else if (missile != null && missile2 == null && ingredient instanceof MissileWeapon) {
					missile2 = ingredient;
					if (ingredient.quantity() > 0 && missile2.level() == missile.level()) {
						missle2 = true;
					} //&& missile2 == missile
				} else if (ingredient instanceof LiquidMetal) {
					if( missile!=null && missile instanceof MissileWeapon && missile2 !=null ){
						if ( ingredient.quantity() >= (missile.level()*10-5)) {
							liqued = true;
						}
					}
				}
			}
			return missle && liqued && missle2;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 0;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			//if (!testIngredients(ingredients)) return null;

			for (Item ingredient : ingredients){
				if (ingredient instanceof MissileWeapon) {
					//missile.quantity( missile.quantity() - 1 );
					//missile2.quantity( missile2.quantity() - 1 );
					if (ingredient.quantity() >= 1 ) {
						ingredient.quantity(ingredient.quantity() - 1 );
					}
				} else if (ingredient instanceof LiquidMetal) {
					if (ingredient.quantity() >= ( missile.level()*10-5 ) ) {
						ingredient.quantity(ingredient.quantity() - (missile.level()*10-5) );
					}
				}
			}

			return sampleOutput( null );
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			//这里是输出预览效果，你以前在这里写的会导致反复升级的时候，
			// 输出的等级会越来越高
			//return missile.quantity(1).upgrade(1);
			//所以这里你应该直接返回一个你的missile
			return new MeatPie();
		}
		//Reflection.newInstance( missile.upgrade( 1 ).quantity(1).getClass(  ) )
		//存在的问题是：两个武器等级不同的时候也能用，暂时我找不到解决策略
		//以及我建议控制液金数量来控制等级，而不是控制投掷武器数量。
	}
	*/
/*
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

				if ( ingredient instanceof MissileWeapon ) {
					missile.detach( Dungeon.hero.belongings.backpack );
					missile2.detach( Dungeon.hero.belongings.backpack );
					//missile2.quantity( missile2.quantity() - 1 );
				} else

			for (Item ingredient : ingredients){
				if (ingredient instanceof MissileWeapon) {
					missile.quantity( missile.quantity() - 1 ) ;
					missile2.quantity( missile2.quantity() - 1 );
					//由于你的两个武器都是同一个类型，所以地牢移除的时候总是会在视觉显示这里忽略一个。但其实是移除了
					ShatteredPixelDungeon.scene().add(new WndHardNotification(new ItemSprite(ItemSpriteSheet.MISSILE_HOLDER),
							"炼金完成",
							"你的投掷武器成功升级了",
							"点击任意区域继续",
							0){
						@Override
						public void hide() {
							super.hide();
							//这里用一个悬浮框是因为刷新屏幕会导致看不见东西，如果这里有一个对话框在确定前是看得见炼金输出效果的
							//而当屏幕一旦刷新，即便是同一个类型，也会完全清空这个屏幕的元素，达到效果。算临时方法吧,不过这个方法也不错
							ShatteredPixelDungeon.resetScene();
						}
					});

				} else if (ingredient instanceof LiquidMetal) {
					if (ingredient.quantity() >= (missile.level()*10-5) ) {
						ingredient.quantity(ingredient.quantity() - (missile.level()*10-5) );
					}
				}
			}

			//真正的输出效果在这里
			missile.level(missile.level()+1);
			missile.quantity(1);
			return sampleOutput( null );
		}

 */



	/*
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {
		Item missile;
		Item missile2;
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean missle = false;
			boolean missle2 = false;
			boolean liqued = false;

			for (Item ingredient : ingredients){
				if (ingredient instanceof MissileWeapon) {
					missile = ingredient;
					if (ingredient.quantity() > 0) {
						missle = true;
					}
				}
				if ( missile!=null && ingredient instanceof MissileWeapon ) {
					missile2 = ingredient;
					if ( ingredient.quantity() > 0 && missile2 == missile && missile2.buffedLvl() == missile.buffedLvl() ) {
						missle2 = true;
					}

				} else if (ingredient instanceof LiquidMetal) {
					if( missile!=null && missile instanceof MissileWeapon && missile2 !=null ){
						if ( ingredient.quantity() >= (missile.level()*10-5)) {
							liqued = true;
						}
					}
				}
			}
			return missle && liqued && missle2;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 2;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item ingredient : ingredients){
				if (ingredient instanceof MissileWeapon) {
					missile.quantity( missile.quantity() - 1 );
					missile2.quantity( missile2.quantity() - 1 );
				} else if (ingredient instanceof LiquidMetal) {
					if (ingredient.quantity() >= (5+missile.level()*10) ) {
						ingredient.quantity(ingredient.quantity() - (missile.level()*10-5) );
					}
				}
			}

			return sampleOutput( null );
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) { return missile.quantity(1).upgrade(1); }
	}
	*/


	public static class PlaceHolder extends MissileWeapon {

		{
			image = ItemSpriteSheet.MISSILE_HOLDER;
		}

		@Override
		public boolean isSimilar(Item item) {
			return item instanceof MissileWeapon;
		}

		@Override
		public String info() {
			return "";
		}
	}
}
