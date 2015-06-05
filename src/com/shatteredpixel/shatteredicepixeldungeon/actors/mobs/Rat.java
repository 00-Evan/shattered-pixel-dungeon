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
package com.shatteredpixel.shatteredicepixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.RatSprite;
import com.watabou.utils.Random;

public class Rat extends Mob {

	{
		name = "marsupial rat";
		spriteClass = RatSprite.class;
		
		HP = HT = 8;
		defenseSkill = 3;
		
		maxLvl = 5;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 8;
	}
	
	@Override
	public int dr() {
		return 1;
	}
	
	@Override
	public String description() {
		return
			"Marsupial rats are aggressive but rather weak denizens " +
			"of the sewers. They have a nasty bite, but are only life threatening in large numbers.";
	}
}
