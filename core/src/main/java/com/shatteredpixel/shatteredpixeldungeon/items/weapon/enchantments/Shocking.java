/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Shocking extends Weapon.Enchantment {

	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level + 3 ) >= 2) {
			
			affected.clear();
			affected.add(attacker);

			arcs.clear();
			arcs.add(new Lightning.Arc(attacker.sprite.center(), defender.sprite.center()));
			hit(defender, Random.Int(1, damage / 3));

			attacker.sprite.parent.addToFront( new Lightning( arcs, null ) );
			
		}

		return damage;

	}

	@Override
	public ItemSprite.Glowing glowing() {
		return WHITE;
	}

	private ArrayList<Char> affected = new ArrayList<>();

	private ArrayList<Lightning.Arc> arcs = new ArrayList<>();
	
	private void hit( Char ch, int damage ) {
		
		if (damage < 1) {
			return;
		}
		
		affected.add(ch);
		ch.damage(Level.water[ch.pos] && !ch.flying ? (int) (damage * 2) : damage, LightningTrap.LIGHTNING);
		
		ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		ch.sprite.flash();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar( ch.pos + PathFinder.NEIGHBOURS8[i] );
			if (n != null && !affected.contains( n )) {
				arcs.add(new Lightning.Arc(ch.sprite.center(), n.sprite.center()));
				hit(n, Random.Int(damage / 2, damage));
			}
		}
	}
}
