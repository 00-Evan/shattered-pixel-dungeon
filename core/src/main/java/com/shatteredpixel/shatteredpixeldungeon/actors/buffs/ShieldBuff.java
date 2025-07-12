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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class ShieldBuff extends Buff {
	
	private int shielding;

	//higher priority shielding buffs are consumed first if multiple exist
	//currently we have the following:
	// 2: relatively weak and short term shields like blocking buff
	// 1: larger but still short-term shields from Cleric's ascended form
	// 0: everything else, mostly the various sources of generic barrier
	protected int shieldUsePriority = 0;
	protected boolean detachesAtZero = true;
	
	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {
			target.needsShieldUpdate = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		target.needsShieldUpdate = true;
		super.detach();
	}
	
	public int shielding(){
		return shielding;
	}
	
	public void setShield( int shield ) {
		if (this.shielding <= shield) this.shielding = shield;
		if (target != null) target.needsShieldUpdate = true;
	}
	
	public void incShield(){
		incShield(1);
	}

	public void incShield( int amt ){
		shielding += amt;
		if (target != null) target.needsShieldUpdate = true;
	}

	//doesn't add shield, but postpones it detereorating
	public void delay( float value ){
		spend(value);
	}
	
	public void decShield(){
		decShield(1);
	}

	public void decShield( int amt ){
		shielding -= amt;
		if (target != null) target.needsShieldUpdate = true;
	}
	
	//returns the amount of damage leftover
	public int absorbDamage( int dmg ){
		if (shielding >= dmg){
			shielding -= dmg;
			dmg = 0;
		} else {
			dmg -= shielding;
			shielding = 0;
		}
		if (shielding <= 0 && detachesAtZero){
			detach();
		}
		if (target != null) target.needsShieldUpdate = true;
		return dmg;
	}

	public static int processDamage( Char target, int damage, Object src ){
		//hunger damage is not affected by shielding
		if (src instanceof Hunger){
			return damage;
		}

		ArrayList<ShieldBuff> buffs = new ArrayList<>(target.buffs(ShieldBuff.class));
		if (!buffs.isEmpty()){
			//sort in descending order based on shield use priority
			Collections.sort(buffs, new Comparator<ShieldBuff>() {
				@Override
				public int compare(ShieldBuff a, ShieldBuff b) {
					return b.shieldUsePriority - a.shieldUsePriority;
				}
			});
			for (ShieldBuff buff : buffs){
				if (buff.shielding() <= 0) continue;
				damage = buff.absorbDamage(damage);
				if (buff.shielding() <= 0){
					if (target instanceof Hero && ((Hero) target).hasTalent(Talent.PROVOKED_ANGER)){
						Buff.affect(target, Talent.ProvokedAngerTracker.class, 5f);
					}
				}
				if (damage == 0) break;
			}
		}

		return damage;
	}
	
	private static final String SHIELDING = "shielding";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( SHIELDING, shielding);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		shielding = bundle.getInt( SHIELDING );
	}
	
}
