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
package com.shatteredpixel.shatteredpixeldungeon.tiles;

public class DungeonTileSheet {

	private static final int WIDTH = 16;

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	private static final int GROUND = xy(1, 1); //32 slots
	public static final int FLOOR = GROUND +0;
	public static final int FLOOR_DECO = GROUND +1;
	public static final int HIGH_GRASS = GROUND +2;
	public static final int GRASS = GROUND +3;
	public static final int EMBERS = GROUND +4;
	public static final int FLOOR_SP = GROUND +5;

	public static final int FLOOR_ALT_1 = GROUND +7;
	public static final int FLOOR_DECO_ALT = GROUND +8;
	public static final int HIGH_GRASS_ALT = GROUND +9;
	public static final int GRASS_ALT = GROUND +10;
	public static final int EMBERS_ALT = GROUND +11;
	public static final int FLOOR_SP_ALT = GROUND +12;

	public static final int FLOOR_ALT_2 = GROUND +14;

	public static final int ENTRANCE = GROUND +16;
	public static final int EXIT = GROUND +17;
	public static final int SIGN = GROUND +18;
	public static final int STATUE = GROUND +19;
	public static final int STATUE_SP = GROUND +20;
	public static final int WELL = GROUND +21;
	public static final int EMPTY_WELL = GROUND +22;
	public static final int ALCHEMY_POT = GROUND +23;
	public static final int PEDESTAL = GROUND +24;
	public static final int BARRICADE = GROUND +25;
	public static final int BOOKSHELF = GROUND +26;

	public static final int BOOKSHELF_ALT = GROUND +28;


	public static final int WATER = xy(1, 3); //16 slots
	//next 15 slots are all water stitching with ground.
	//+1 for ground above, +2 for ground right, +4 for ground below, +8 for ground left.


	public static final int CHASM = xy(1, 4); //16 tiles
	//chasm stitching visuals...
	public static final int CHASM_FLOOR = CHASM+1;
	public static final int CHASM_FLOOR_SP = CHASM+2;
	public static final int CHASM_WALL = CHASM+3;
	public static final int CHASM_WATER = CHASM+4;

	/*
		These tiles present wall visuals as flat
	*/
	private static final int FLAT_WALLS = xy(1, 5); //16 slots
	public static final int FLAT_WALL = FLAT_WALLS+0;
	public static final int FLAT_WALL_DECO = FLAT_WALLS+1;

	public static final int FLAT_WALL_ALT = FLAT_WALLS+3;
	public static final int FLAT_WALL_DECO_ALT = FLAT_WALLS+4;

	private static final int FLAT_DOORS = xy(1,6); //16 slots
	public static final int FLAT_DOOR = FLAT_DOORS+0;
	public static final int FLAT_DOOR_OPEN = FLAT_DOORS+1;
	public static final int FLAT_DOOR_LOCKED = FLAT_DOORS+2;
	public static final int UNLOCKED_EXIT = FLAT_DOORS+3;
	public static final int LOCKED_EXIT = FLAT_DOORS+4;


	/*
		These tiles present visuals that are raised and rendered on the lower layer (behind characters)
	*/
	private static final int RAISED_WALLS = xy(1, 8); //32 slots
	//+1 for walls to the right, +2 for walls to the left
	public static final int RAISED_WALL = RAISED_WALLS+0;
	public static final int RAISED_WALL_DECO = RAISED_WALLS+4;
	//wall that appears behind a top/bottom doorway
	public static final int RAISED_WALL_DOOR = RAISED_WALLS+8;

	public static final int RAISED_WALL_ALT = RAISED_WALLS+16;
	public static final int RAISED_WALL_DECO_ALT = RAISED_WALLS+20;

	private static final int RAISED_DOORS = xy(1, 10); //16 slots
	public static final int RAISED_DOOR = RAISED_DOORS+0;
	public static final int RAISED_DOOR_OPEN = RAISED_DOORS+1;
	public static final int RAISED_DOOR_LOCKED = RAISED_DOORS+2;
	//floor tile that appears on a top/bottom doorway
	public static final int RAISED_DOOR_SIDEWAYS = RAISED_DOORS+3;

	/*
		These tiles present visuals that are raised and rendered on the upper layer (above characters)
	*/
	//+1 for wall right, +2 for wall right-below, +4 for wall left-below, +8 for wall left.
	public static final int WALLS_INTERNAL = xy(1, 12); //16 slots

	//+1 for walls to the down-right, +2 for walls to the down-left
	private static final int WALLS_OVERHANG = xy(1, 13); //16 slots
	public static final int WALL_OVERHANG = WALLS_OVERHANG+0;
	public static final int DOOR_SIDEWAYS_OVERHANG = WALL_OVERHANG+4;
	public static final int DOOR_SIDEWAYS_OVERHANG_OPEN = WALL_OVERHANG+8;
	//no attachment to adjacent walls
	public static final int DOOR_OVERHANG = WALL_OVERHANG+12;
	public static final int DOOR_OVERHANG_OPEN = WALL_OVERHANG+13;
	public static final int DOOR_SIDEWAYS = WALL_OVERHANG+14;
	public static final int DOOR_SIDEWAYS_LOCKED = WALL_OVERHANG+15;


}
