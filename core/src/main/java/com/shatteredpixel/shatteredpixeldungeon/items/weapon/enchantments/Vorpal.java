/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Vorpal extends Weapon.Enchantment {

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xAA6666 );

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		//TODO this doesn't count kills from bonus power fx like smite
		// should probably use a tracker. This also applies to other enchants like corrupting
		if (defender.isImmune(Bleeding.class)){
			return damage;
		}

		//flat 25% proc chance, effect scales with damage dealt
		float procChance = 1/4f * procChanceMultiplier(attacker);
		if (Random.Float() < procChance) {

			float powerMulti = Math.max(1f, procChance);

			//we use a buff to track so we can know the final dmg
			Buff.affect(attacker, VorpalTracker.class).powerMulti = powerMulti;
		}

		return damage;
	}

	public static class VorpalTracker extends Buff {
		{
			actPriority = Actor.VFX_PRIO;
		}

		public float powerMulti;

		@Override
		public boolean act() {
			detach();
			return true;
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return RED;
	}
}
