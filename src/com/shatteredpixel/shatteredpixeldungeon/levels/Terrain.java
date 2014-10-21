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
package com.shatteredpixel.shatteredpixeldungeon.levels;

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
	public static final int EMPTY_DECO		= 24;
	public static final int LOCKED_EXIT		= 25;
	public static final int UNLOCKED_EXIT	= 26;
	public static final int SIGN			= 29;
	public static final int WELL			= 34;
	public static final int STATUE			= 35;
	public static final int STATUE_SP		= 36;
	public static final int BOOKSHELF		= 41;
	public static final int ALCHEMY			= 42;
	public static final int CHASM_FLOOR		= 43;
	public static final int CHASM_FLOOR_SP	= 44;
	public static final int CHASM_WALL		= 45;
	public static final int CHASM_WATER		= 46;
	
	public static final int SECRET_DOOR				= 16;
	public static final int TOXIC_TRAP				= 17;
	public static final int SECRET_TOXIC_TRAP		= 18;
	public static final int FIRE_TRAP				= 19;
	public static final int SECRET_FIRE_TRAP		= 20;
	public static final int PARALYTIC_TRAP			= 21;
	public static final int SECRET_PARALYTIC_TRAP	= 22;
	public static final int INACTIVE_TRAP			= 23;
	public static final int POISON_TRAP				= 27;
	public static final int SECRET_POISON_TRAP		= 28;
	public static final int ALARM_TRAP				= 30;
	public static final int SECRET_ALARM_TRAP		= 31;
	public static final int LIGHTNING_TRAP			= 32;
	public static final int SECRET_LIGHTNING_TRAP	= 33;
	public static final int GRIPPING_TRAP			= 37;
	public static final int SECRET_GRIPPING_TRAP	= 38;
	public static final int SUMMONING_TRAP			= 39;
	public static final int SECRET_SUMMONING_TRAP	= 40;
	
	public static final int WATER_TILES	= 48;
	public static final int WATER		= 63;
	
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
		
		flags[SECRET_DOOR]				= flags[WALL] | SECRET				| UNSTITCHABLE;
		flags[TOXIC_TRAP]				= AVOID;
		flags[SECRET_TOXIC_TRAP]		= flags[EMPTY] | SECRET;
		flags[FIRE_TRAP]				= AVOID;
		flags[SECRET_FIRE_TRAP]			= flags[EMPTY] | SECRET;
		flags[PARALYTIC_TRAP]			= AVOID;
		flags[SECRET_PARALYTIC_TRAP]	= flags[EMPTY] | SECRET;
		flags[POISON_TRAP]				= AVOID;
		flags[SECRET_POISON_TRAP]		= flags[EMPTY] | SECRET;
		flags[ALARM_TRAP]				= AVOID;
		flags[SECRET_ALARM_TRAP]		= flags[EMPTY] | SECRET;
		flags[LIGHTNING_TRAP]			= AVOID;
		flags[SECRET_LIGHTNING_TRAP]	= flags[EMPTY] | SECRET;
		flags[GRIPPING_TRAP]			= AVOID;
		flags[SECRET_GRIPPING_TRAP]		= flags[EMPTY] | SECRET;
		flags[SUMMONING_TRAP]			= AVOID;
		flags[SECRET_SUMMONING_TRAP]	= flags[EMPTY] | SECRET;
		flags[INACTIVE_TRAP]			= flags[EMPTY];
		
		for (int i=WATER_TILES; i < WATER_TILES + 16; i++) {
			flags[i] = flags[WATER];
		}
	};
	
	public static int discover( int terr ) {
		switch (terr) {
		case SECRET_DOOR:
			return DOOR;
		case SECRET_FIRE_TRAP:
			return FIRE_TRAP;
		case SECRET_PARALYTIC_TRAP:
			return PARALYTIC_TRAP;
		case SECRET_TOXIC_TRAP:
			return TOXIC_TRAP;
		case SECRET_POISON_TRAP:
			return POISON_TRAP;
		case SECRET_ALARM_TRAP:
			return ALARM_TRAP;
		case SECRET_LIGHTNING_TRAP:
			return LIGHTNING_TRAP;
		case SECRET_GRIPPING_TRAP:
			return GRIPPING_TRAP;
		case SECRET_SUMMONING_TRAP:
			return SUMMONING_TRAP;
		default:
			return terr;
		}
	}
}
