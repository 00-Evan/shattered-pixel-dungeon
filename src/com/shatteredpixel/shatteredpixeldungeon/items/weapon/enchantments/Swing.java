/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Random;

public class Swing extends Enchantment {
	
	private static final String TXT_WILD	= "Wild %s";
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		
		int level = Math.max( 0, weapon.level );
		
		int maxDamage = (int)(damage * Math.pow( 2, -1d / (level + 1) ));
		if (maxDamage >= 1) {
			
			int p = attacker.pos;
			int[] neighbours = {
				p+1, p-1, p+Level.WIDTH, p-Level.WIDTH, 
				p+1+Level.WIDTH, p+1-Level.WIDTH, p-1+Level.WIDTH, p-1-Level.WIDTH};
			
			for (int n : neighbours) {
				Char ch = Actor.findChar( n );
				if (ch != null && ch != defender && ch.isAlive()) {
					
					int dr = Random.IntRange( 0, ch.dr() );
					int dmg = Random.Int( 1, maxDamage );
					int effectiveDamage = Math.max( dmg - dr, 0 );
					
					ch.damage( effectiveDamage, this );
					
					ch.sprite.bloodBurstA( attacker.sprite.center(), effectiveDamage );
					ch.sprite.flash();
					
				}
			}
			
			return true;
			
		} else {
		
			return false;
			
		}
	}
	
	@Override
	public String name( String weaponName) {
		return String.format( TXT_WILD, weaponName );
	}

}
