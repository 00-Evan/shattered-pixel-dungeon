/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.PinCushion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SnipersMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

abstract public class MissileWeapon extends Weapon {

	{
		stackable = true;
		levelKnown = true;

		defaultAction = AC_THROW;
		usesTargeting = true;
	}
	
	//weapons which don't use durability should set it to -1
	protected float durability = 100;
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove( AC_EQUIP );
		return actions;
	}

	@Override
	public int throwPos(Hero user, int dst) {
		if (hasEnchant(Projecting.class)
				&& !Dungeon.level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4){
			return dst;
		} else {
			return super.throwPos(user, dst);
		}
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
				super.onThrow( cell );
		} else {
			if (!curUser.shoot( enemy, this )) {
				rangedMiss( cell );
			} else {

				//int bonus = RingOfSharpshooting.getBonus(curUser, RingOfSharpshooting.Aim.class);

				//if (curUser.heroClass == HeroClass.HUNTRESS && enemy.buff(PinCushion.class) == null)
				//	bonus += 3;
				
				rangedHit( enemy );

			}
		}
	}
	
	@Override
	public float castDelay(Char user, int dst) {
		float delay = speedFactor( user );
		
		Char enemy = Actor.findChar(dst);
		
		if (enemy != null) {
			SnipersMark mark = user.buff( SnipersMark.class );
			if (mark != null) {
				if (mark.object == enemy.id()) {
					delay *= 0.5f;
				}
			}
		}
		
		return delay;
	}
	
	protected void rangedHit(Char enemy ){
		reduceDurability();
		if (durability > 0){
			if (enemy.isAlive())
				Buff.affect(enemy, PinCushion.class).stick(this);
			else
				Dungeon.level.drop( this, enemy.pos).sprite.drop();
		}
	}
	
	protected void reduceDurability(){
		//do nothing by default
	}
	
	protected void rangedMiss(int cell ) {
		int bonus = RingOfSharpshooting.getBonus(curUser, RingOfSharpshooting.Aim.class);

		//degraded ring of sharpshooting will even make missed shots break.
		if (Random.Float() < Math.pow(0.6, -bonus))
			super.onThrow( cell );
	}
	
	@Override
	public Item random() {
		if (durability != -1) durability = Random.NormalIntRange(70, 100);
		return this;
	}
	
	@Override
	public void reset() {
		super.reset();
		durability = 100;
	}
	
	@Override
	public Item merge(Item other) {
		super.merge(other);
		if (isSimilar(other) && durability != -1) {
			durability += ((MissileWeapon)other).durability;
			durability -= 100;
			while (durability <= 0){
				quantity -= 1;
				durability += 100;
			}
		}
		return this;
	}
	
	@Override
	public Item split(int amount) {
		Item split = super.split(amount);
		
		//the split item will retain lost durability
		if (split != null && durability != -1){
			durability = 100;
		}
		
		return split;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {

		String info = desc();
		
		info += "\n\n" + Messages.get( MissileWeapon.class, "stats", imbue.damageFactor(min()), imbue.damageFactor(max()), STRReq());

		if (STRReq() > Dungeon.hero.STR()) {
			info += " " + Messages.get(Weapon.class, "too_heavy");
		} else if (Dungeon.hero.heroClass == HeroClass.HUNTRESS && Dungeon.hero.STR() > STRReq()){
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
		}

		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");
		
		info += "\n\n" + Messages.get(this, "durability");
		
		//weapons which don't use durability should set it to -1, and have their own text
		if (durability >= 100)      info += "\n\n" + Messages.get(this, "dur_100");
		else if (durability >= 80)  info += "\n\n" + Messages.get(this, "dur_80");
		else if (durability >= 60)  info += "\n\n" + Messages.get(this, "dur_60");
		else if (durability >= 40)  info += "\n\n" + Messages.get(this, "dur_40");
		else if (durability >= 20)  info += "\n\n" + Messages.get(this, "dur_20");
		else if (durability >= 0)   info += "\n\n" + Messages.get(this, "dur_0");
		
		
		return info;
	}
	
	private static final String DURABILITY = "durability";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DURABILITY, durability);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		//compatibility with pre-0.6.3 saves
		if (bundle.contains(DURABILITY)) {
			durability = bundle.getInt(DURABILITY);
		}
	}
}
