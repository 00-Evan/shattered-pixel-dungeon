/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.items.weapon.enchantments;

import java.util.ArrayList;
import java.util.HashSet;

import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredicepixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.traps.LightningTrap;
import com.watabou.utils.Random;

public class Shock extends Weapon.Enchantment {

	private static final String TXT_SHOCKING	= "Shocking %s";
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max( 0, weapon.level );
		
		if (Random.Int( level + 4 ) >= 3) {
			
			points[0] = attacker.pos;
			nPoints = 1;
			
			affected.clear();
			affected.add( attacker );
			
			hit( defender, Random.Int( 1, damage / 2 ) );
			
			attacker.sprite.parent.add( new Lightning( points, nPoints, null ) );
			
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	@Override
	public String name( String weaponName ) {
		return String.format( TXT_SHOCKING, weaponName );
	}

	private ArrayList<Char> affected = new ArrayList<Char>();
	
	private int[] points = new int[20];
	private int nPoints;
	
	private void hit( Char ch, int damage ) {
		
		if (damage < 1) {
			return;
		}
		
		affected.add( ch );
		ch.damage( Level.water[ch.pos] && !ch.flying ? (int)(damage * 2) : damage, LightningTrap.LIGHTNING  );
		
		ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
		ch.sprite.flash();
		
		points[nPoints++] = ch.pos;
		
		HashSet<Char> ns = new HashSet<Char>();
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar( ch.pos + Level.NEIGHBOURS8[i] );
			if (n != null && !affected.contains( n )) {
				ns.add( n );
			}
		}
		
		if (ns.size() > 0) {
			hit( Random.element( ns ), Random.Int( damage / 2, damage ) );
		}
	}
}
