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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Grim extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {

		int level = Math.max( 0, weapon.level() );

		int enemyHealth = defender.HP - damage;
		if (enemyHealth == 0) return damage; //no point in proccing if they're already dead.

		//scales from 0 - 30% based on how low hp the enemy is, plus 1% per level
		int chance = Math.round(((defender.HT - enemyHealth) / (float)defender.HT)*30 + level);
		
		if (Random.Int( 100 ) < chance) {
			
			defender.damage( defender.HP, this );
			defender.sprite.emitter().burst( ShadowParticle.UP, 5 );
			
			if (!defender.isAlive() && attacker instanceof Hero) {
				Badges.validateGrimWeapon();
			}
			
		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return BLACK;
	}

}
