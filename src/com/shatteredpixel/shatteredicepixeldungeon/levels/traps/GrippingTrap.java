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
package com.shatteredpixel.shatteredicepixeldungeon.levels.traps;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Wound;
import com.watabou.utils.Random;

public class GrippingTrap {
	
	public static void trigger( int pos, Char c ) {
		
		if (c != null) {
			int damage = Math.max( 0,  (Dungeon.depth + 3) - Random.IntRange( 0, c.dr() / 2 ) );
			Buff.affect( c, Bleeding.class ).set( damage );
			Buff.prolong( c, Cripple.class, Cripple.DURATION );
			Wound.hit( c );
		} else {
			Wound.hit( pos );
		}
		
	}
}
