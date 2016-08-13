/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FireTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.LightningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ParalyticTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.utils.SparseArray;

public class Terrain {

	public static final int CHASM			= 0;
	public static final int EMPTY			= 1;
	public static final int GRASS			= 2;
	public static final int EMPTY_WELL		= 3;
	public static final int WALL			= 4;
	public static final int DOOR			= 5;
	public static final int OPEN_DOOR		= 6;
	public static final int ENTRANCE		= 7;
	public static final int EXIT			= 8;
	public static final int EMBERS			= 9;
	public static final int LOCKED_DOOR		= 10;
	public static final int PEDESTAL		= 11;
	public static final int WALL_DECO		= 12;
	public static final int BARRICADE		= 13;
	public static final int EMPTY_SP		= 14;
	public static final int HIGH_GRASS		= 15;

	public static final int SECRET_DOOR	    = 16;
	public static final int SECRET_TRAP     = 17;
	public static final int TRAP            = 18;
	public static final int INACTIVE_TRAP   = 19;

	public static final int EMPTY_DECO		= 20;
	public static final int LOCKED_EXIT		= 21;
	public static final int UNLOCKED_EXIT	= 22;
	public static final int SIGN			= 23;
	public static final int WELL			= 24;
	public static final int STATUE			= 25;
	public static final int STATUE_SP		= 26;
	public static final int BOOKSHELF		= 27;
	public static final int ALCHEMY			= 28;

	public static final int CHASM_FLOOR		= 29;
	public static final int CHASM_FLOOR_SP	= 30;
	public static final int CHASM_WALL		= 31;
	public static final int CHASM_WATER		= 32;

	public static final int WATER_TILES	    = 48;
	public static final int WATER		    = 63;
	
	public static final int PASSABLE		= 0x01;
	public static final int LOS_BLOCKING	= 0x02;
	public static final int FLAMABLE		= 0x04;
	public static final int SECRET			= 0x08;
	public static final int SOLID			= 0x10;
	public static final int AVOID			= 0x20;
	public static final int LIQUID			= 0x40;
	public static final int PIT				= 0x80;
	
	public static final int UNSTITCHABLE	= 0x100;
	
	public static final int[] flags = new int[256];
	static {
		flags[CHASM]		= AVOID	| PIT									| UNSTITCHABLE;
		flags[EMPTY]		= PASSABLE;
		flags[GRASS]		= PASSABLE | FLAMABLE;
		flags[EMPTY_WELL]	= PASSABLE;
		flags[WATER]		= PASSABLE | LIQUID 							| UNSTITCHABLE;
		flags[WALL]			= LOS_BLOCKING | SOLID 							| UNSTITCHABLE;
		flags[DOOR]			= PASSABLE | LOS_BLOCKING | FLAMABLE | SOLID	| UNSTITCHABLE;
		flags[OPEN_DOOR]	= PASSABLE | FLAMABLE 							| UNSTITCHABLE;
		flags[ENTRANCE]		= PASSABLE/* | SOLID*/;
		flags[EXIT]			= PASSABLE;
		flags[EMBERS]		= PASSABLE;
		flags[LOCKED_DOOR]	= LOS_BLOCKING | SOLID 							| UNSTITCHABLE;
		flags[PEDESTAL]		= PASSABLE 										| UNSTITCHABLE;
		flags[WALL_DECO]	= flags[WALL];
		flags[BARRICADE]	= FLAMABLE | SOLID | LOS_BLOCKING;
		flags[EMPTY_SP]		= flags[EMPTY]									| UNSTITCHABLE;
		flags[HIGH_GRASS]	= PASSABLE | LOS_BLOCKING | FLAMABLE;

		flags[SECRET_DOOR]  = flags[WALL]  | SECRET	            			| UNSTITCHABLE;
		flags[SECRET_TRAP]  = flags[EMPTY] | SECRET;
		flags[TRAP]         = AVOID;
		flags[INACTIVE_TRAP]= flags[EMPTY];

		flags[EMPTY_DECO]	= flags[EMPTY];
		flags[LOCKED_EXIT]	= SOLID;
		flags[UNLOCKED_EXIT]= PASSABLE;
		flags[SIGN]			= PASSABLE | FLAMABLE;
		flags[WELL]			= AVOID;
		flags[STATUE]		= SOLID;
		flags[STATUE_SP]	= flags[STATUE] 								| UNSTITCHABLE;
		flags[BOOKSHELF]	= flags[BARRICADE]								| UNSTITCHABLE;
		flags[ALCHEMY]		= PASSABLE;
		
		flags[CHASM_WALL]		= flags[CHASM];
		flags[CHASM_FLOOR]		= flags[CHASM];
		flags[CHASM_FLOOR_SP]	= flags[CHASM];
		flags[CHASM_WATER]		= flags[CHASM];
		
		for (int i=WATER_TILES; i < WATER_TILES + 16; i++) {
			flags[i] = flags[WATER];
		}
	};

	public static int discover( int terr ) {
		switch (terr) {
		case SECRET_DOOR:
			return DOOR;
		case SECRET_TRAP:
			return TRAP;
		default:
			return terr;
		}
	}

	//converts terrain values from pre versioncode 44 (0.3.0c) saves
	//TODO: remove when no longer supporting saves from 0.3.0b and under
	public static int[] convertTrapsFrom43( int[] map, SparseArray<Trap> traps){
		for (int i = 0; i < map.length; i++){

			int c = map[i];

			//non-trap tiles getting their values shifted around
			if (c >= 24 && c <= 26) {
				c -= 4; //24-26 becomes 20-22
			} else if (c == 29) {
				c = 23; //29 becomes 23
			} else if ( c >= 34 && c <= 36) {
				c -= 10; //34-36 becomes 24-26
			} else if ( c >= 41 && c <= 46) {
				c -= 14; //41-46 becomes 27-32
			}

			//trap tiles, must be converted to general trap tiles and specific traps instantiated
			else if (c >= 17 && c <= 40){
				//this is going to be messy...
				Trap trap = null;
				switch(c){
					case 17: trap = new ToxicTrap().reveal(); break;
					case 18: trap = new ToxicTrap().hide(); break;

					case 19: trap = new FireTrap().reveal(); break;
					case 20: trap = new FireTrap().hide(); break;

					case 21: trap = new ParalyticTrap().reveal(); break;
					case 22: trap = new ParalyticTrap().hide(); break;

					case 23:
						c = INACTIVE_TRAP;
						trap = null;
						break;

					case 27: trap = new PoisonTrap().reveal(); break;
					case 28: trap = new PoisonTrap().hide(); break;

					case 30: trap = new AlarmTrap().reveal(); break;
					case 31: trap = new AlarmTrap().hide(); break;

					case 32: trap = new LightningTrap().reveal(); break;
					case 33: trap = new LightningTrap().hide(); break;

					case 37: trap = new GrippingTrap().reveal(); break;
					case 38: trap = new GrippingTrap().hide(); break;

					case 39: trap = new SummoningTrap().reveal(); break;
					case 40: trap = new SummoningTrap().hide(); break;
				}
				if (trap != null){
					trap.set( i );
					traps.put( trap.pos, trap );
					if (trap.visible)
						c = TRAP;
					else
						c = SECRET_TRAP;
 				}
			}

			map[i] = c;
		}
		return map;
	}


}
