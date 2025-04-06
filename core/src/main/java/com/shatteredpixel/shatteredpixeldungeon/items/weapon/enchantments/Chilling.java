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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Chilling extends Weapon.Enchantment {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x00FFFF );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.buffedLvl() );
		float procChance;

		// lvl 0 - 33%
		// lvl 1 - 43%
		// lvl 2 - 50%
		procChance = (level+2f)/(level+6f) * procChanceMultiplier(attacker);

		// lvl 0 - 25%
		// lvl 1 - 32%
		// lvl 2 - 38%
		if(!Dungeon.level.water[defender.pos]) procChance /= 1.33f;

		if (Random.Float() < procChance) {

			float powerMulti = Math.max(1f, procChance);
			float durationToAdd = (Chill.Polished.CHILL_TICK +1) * powerMulti;
			float max = Chill.Polished.WATER_TICK-1;

			Chill existing = defender.buff(Chill.class);
			if (existing != null){
				durationToAdd = Math.min(durationToAdd, max-existing.cooldown());
				durationToAdd = Math.max(durationToAdd, 0);
			}

			//Buff.Polished.affectAligned(defender, Chill.class, durationToAdd);
			Buff.affect(defender, Chill.class, durationToAdd);
			Splash.at( defender.sprite.center(), 0xFFB2D6FF, 5);

		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return TEAL;
	}

}
