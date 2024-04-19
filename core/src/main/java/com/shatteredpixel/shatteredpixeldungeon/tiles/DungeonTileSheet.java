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

package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.Arrays;
import java.util.HashSet;

public class DungeonTileSheet {

	private static final int WIDTH = 16;

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	//used in cases like map-edge decision making.
	public static final int NULL_TILE       = -1;



	/**********************************************************************
	 * Floor Tiles
	 **********************************************************************/

	private static final int GROUND         =                               xy(1, 1);   //24 slots
	public static final int FLOOR           = GROUND +0;
	public static final int FLOOR_DECO      = GROUND +1;
	public static final int GRASS           = GROUND +2;
	public static final int EMBERS          = GROUND +3;
	public static final int FLOOR_SP        = GROUND +4;

	public static final int FLOOR_ALT_1     = GROUND +6;
	public static final int FLOOR_DECO_ALT  = GROUND +7;
	public static final int GRASS_ALT       = GROUND +8;
	public static final int EMBERS_ALT      = GROUND +9;
	public static final int FLOOR_SP_ALT    = GROUND +10;

	public static final int FLOOR_ALT_2     = GROUND +12;

	public static final int ENTRANCE        = GROUND +16;
	public static final int EXIT            = GROUND +17;
	public static final int WELL            = GROUND +18;
	public static final int EMPTY_WELL      = GROUND +19;
	public static final int PEDESTAL        = GROUND +20;

	public static final int ENTRANCE_SP     = GROUND +22;

	public static final int CHASM           =                               xy(9, 2);   //8 slots
	//chasm stitching visuals...
	public static final int CHASM_FLOOR     = CHASM+1;
	public static final int CHASM_FLOOR_SP  = CHASM+2;
	public static final int CHASM_WALL      = CHASM+3;
	public static final int CHASM_WATER     = CHASM+4;

	//tiles that can stitch with chasms (from above), and which visual represents the stitching
	public static SparseArray<Integer> chasmStitcheable = new SparseArray<>();
	static {
		//floor
		chasmStitcheable.put( Terrain.EMPTY,        CHASM_FLOOR );
		chasmStitcheable.put( Terrain.GRASS,        CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMBERS,       CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMPTY_WELL,   CHASM_FLOOR );
		chasmStitcheable.put( Terrain.HIGH_GRASS,   CHASM_FLOOR );
		chasmStitcheable.put( Terrain.FURROWED_GRASS,CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMPTY_DECO,   CHASM_FLOOR );
		chasmStitcheable.put( Terrain.CUSTOM_DECO,  CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMPTY_WELL,   CHASM_FLOOR );
		chasmStitcheable.put( Terrain.WELL,         CHASM_FLOOR );
		chasmStitcheable.put( Terrain.STATUE,       CHASM_FLOOR );
		chasmStitcheable.put( Terrain.SECRET_TRAP,  CHASM_FLOOR );
		chasmStitcheable.put( Terrain.INACTIVE_TRAP,CHASM_FLOOR );
		chasmStitcheable.put( Terrain.TRAP,         CHASM_FLOOR );
		chasmStitcheable.put( Terrain.BOOKSHELF,    CHASM_FLOOR );
		chasmStitcheable.put( Terrain.BARRICADE,    CHASM_FLOOR );
		chasmStitcheable.put( Terrain.PEDESTAL,     CHASM_FLOOR );
		chasmStitcheable.put( Terrain.CUSTOM_DECO_EMPTY,CHASM_FLOOR );
		chasmStitcheable.put( Terrain.MINE_BOULDER, CHASM_FLOOR );
		chasmStitcheable.put( Terrain.MINE_CRYSTAL, CHASM_FLOOR );

		//special floor
		chasmStitcheable.put( Terrain.EMPTY_SP,     CHASM_FLOOR_SP );
		chasmStitcheable.put( Terrain.STATUE_SP,    CHASM_FLOOR_SP );

		//wall
		chasmStitcheable.put( Terrain.WALL,         CHASM_WALL );
		chasmStitcheable.put( Terrain.DOOR,         CHASM_WALL );
		chasmStitcheable.put( Terrain.OPEN_DOOR,    CHASM_WALL );
		chasmStitcheable.put( Terrain.LOCKED_DOOR,  CHASM_WALL );
		chasmStitcheable.put( Terrain.SECRET_DOOR,  CHASM_WALL );
		chasmStitcheable.put( Terrain.WALL_DECO,    CHASM_WALL );

		//water
		chasmStitcheable.put( Terrain.WATER,        CHASM_WATER );
	}

	public static int stitchChasmTile(int above){
		return chasmStitcheable.get(above, CHASM);
	}


	/**********************************************************************
	 * Water Tiles
	 **********************************************************************/

	public static final int WATER =                                         xy(1, 3);   //16 slots
	//next 15 slots are all water stitching with ground.

	//These tiles can stitch with water
	public static HashSet<Integer> waterStitcheable = new HashSet<>(Arrays.asList(
			Terrain.EMPTY, Terrain.GRASS, Terrain.EMPTY_WELL,
			Terrain.ENTRANCE, Terrain.EXIT, Terrain.EMBERS,
			Terrain.BARRICADE, Terrain.HIGH_GRASS, Terrain.FURROWED_GRASS, Terrain.SECRET_TRAP,
			Terrain.TRAP, Terrain.INACTIVE_TRAP, Terrain.EMPTY_DECO,
			Terrain.CUSTOM_DECO, Terrain.WELL, Terrain.STATUE, Terrain.ALCHEMY,
			Terrain.CUSTOM_DECO_EMPTY, Terrain.MINE_CRYSTAL, Terrain.MINE_BOULDER,
			Terrain.DOOR, Terrain.OPEN_DOOR, Terrain.LOCKED_DOOR, Terrain.CRYSTAL_DOOR
	));

	//+1 for ground above, +2 for ground right, +4 for ground below, +8 for ground left.
	public static int stitchWaterTile(int top, int right, int bottom, int left){
		int result = WATER;
		if (waterStitcheable.contains(top))     result += 1;
		if (waterStitcheable.contains(right))   result += 2;
		if (waterStitcheable.contains(bottom))  result += 4;
		if (waterStitcheable.contains(left))    result += 8;
		return result;
	}

	public static boolean floorTile(int tile){
		return tile == Terrain.WATER || directVisuals.get(tile, CHASM) < CHASM;
	}


	/**********************************************************************
	 Flat Tiles
	 **********************************************************************/

	private static final int FLAT_WALLS         =                           xy(1, 4);   //16 slots
	public static final int FLAT_WALL           = FLAT_WALLS+0;
	public static final int FLAT_WALL_DECO      = FLAT_WALLS+1;
	public static final int FLAT_BOOKSHELF      = FLAT_WALLS+2;

	public static final int FLAT_WALL_ALT       = FLAT_WALLS+4;
	public static final int FLAT_WALL_DECO_ALT  = FLAT_WALLS+5;
	public static final int FLAT_BOOKSHELF_ALT  = FLAT_WALLS+6;

	public static final int FLAT_DOOR           = FLAT_WALLS+8;
	public static final int FLAT_DOOR_OPEN      = FLAT_WALLS+9;
	public static final int FLAT_DOOR_LOCKED    = FLAT_WALLS+10;
	public static final int FLAT_DOOR_CRYSTAL   = FLAT_WALLS+11;
	public static final int UNLOCKED_EXIT       = FLAT_WALLS+12;
	public static final int LOCKED_EXIT         = FLAT_WALLS+13;

	public static final int FLAT_OTHER          =                           xy(1, 5);   //16 slots
	public static final int FLAT_ALCHEMY_POT    = FLAT_OTHER+0;
	public static final int FLAT_BARRICADE      = FLAT_OTHER+1;
	public static final int FLAT_HIGH_GRASS     = FLAT_OTHER+2;
	public static final int FLAT_FURROWED_GRASS = FLAT_OTHER+3;

	public static final int FLAT_HIGH_GRASS_ALT = FLAT_OTHER+5;
	public static final int FLAT_FURROWED_ALT   = FLAT_OTHER+6;

	public static final int FLAT_STATUE         = FLAT_OTHER+8;
	public static final int FLAT_STATUE_SP      = FLAT_OTHER+9;

	public static final int FLAT_MINE_CRYSTAL         = FLAT_OTHER+12;
	public static final int FLAT_MINE_CRYSTAL_ALT     = FLAT_OTHER+13;
	public static final int FLAT_MINE_CRYSTAL_ALT_2   = FLAT_OTHER+14;
	public static final int FLAT_MINE_BOULDER         = FLAT_OTHER+12;
	public static final int FLAT_MINE_BOULDER_ALT     = FLAT_OTHER+13;
	public static final int FLAT_MINE_BOULDER_ALT_2   = FLAT_OTHER+14;

	/**********************************************************************
	 * Raised Tiles, Lower Layer
	 **********************************************************************/

	private static final int RAISED_WALLS               =                   xy(1, 6);   //32 slots
	//+1 for open to the right, +2 for open to the left
	public static final int RAISED_WALL                 = RAISED_WALLS+0;
	public static final int RAISED_WALL_DECO            = RAISED_WALLS+4;
	//wall that appears behind a top/bottom doorway
	public static final int RAISED_WALL_DOOR            = RAISED_WALLS+8;
	public static final int RAISED_WALL_BOOKSHELF       = RAISED_WALLS+12;

	public static final int RAISED_WALL_ALT             = RAISED_WALLS+16;
	public static final int RAISED_WALL_DECO_ALT        = RAISED_WALLS+20;
	public static final int RAISED_WALL_BOOKSHELF_ALT   = RAISED_WALLS+28;

	//we use an array instead of a collection because the small element count
	// makes array traversal much faster than something like HashSet.contains.

	//These tiles count as wall for the purposes of wall stitching
	private static int[] wallStitcheable = new int[]{
			Terrain.WALL, Terrain.WALL_DECO, Terrain.SECRET_DOOR,
			Terrain.LOCKED_EXIT, Terrain.UNLOCKED_EXIT, Terrain.BOOKSHELF, NULL_TILE
	};

	public static boolean wallStitcheable(int tile){
		for (int i : wallStitcheable)
			if (tile == i)
				return true;
		return false;
	}

	public static int getRaisedWallTile(int tile, int pos, int right, int below, int left){
		int result;
		
		if (below == -1 || wallStitcheable(below))                      return -1;
		else if (doorTile(below))                                       result = RAISED_WALL_DOOR;
		else if (tile == Terrain.WALL || tile == Terrain.SECRET_DOOR)   result = RAISED_WALL;
		else if (tile == Terrain.WALL_DECO)                             result = RAISED_WALL_DECO;
		else if (tile == Terrain.BOOKSHELF)                             result = RAISED_WALL_BOOKSHELF;
		else                                                            return -1;

		result = getVisualWithAlts(result, pos);

		if (!wallStitcheable(right))   result += 1;
		if (!wallStitcheable(left))    result += 2;
		return result;
	}

	private static final int RAISED_DOORS           =                       xy(1, 8);  //8 slots
	public static final int RAISED_DOOR             = RAISED_DOORS+0;
	public static final int RAISED_DOOR_OPEN        = RAISED_DOORS+1;
	public static final int RAISED_DOOR_LOCKED      = RAISED_DOORS+2;
	public static final int RAISED_DOOR_CRYSTAL     = RAISED_DOORS+3;
	//floor tile that appears on a top/bottom doorway
	public static final int RAISED_DOOR_SIDEWAYS    = RAISED_DOORS+4;


	public static int getRaisedDoorTile(int tile, int below){
		if (wallStitcheable(below))             return RAISED_DOOR_SIDEWAYS;
		else if (tile == Terrain.DOOR)          return DungeonTileSheet.RAISED_DOOR;
		else if (tile == Terrain.OPEN_DOOR)     return DungeonTileSheet.RAISED_DOOR_OPEN;
		else if (tile == Terrain.LOCKED_DOOR)   return DungeonTileSheet.RAISED_DOOR_LOCKED;
		else if (tile == Terrain.CRYSTAL_DOOR)   return DungeonTileSheet.RAISED_DOOR_CRYSTAL;
		else return -1;
	}

	private static int[] doorTiles = new int[]{
			Terrain.DOOR, Terrain.LOCKED_DOOR, Terrain.CRYSTAL_DOOR, Terrain.OPEN_DOOR
	};

	public static boolean doorTile(int tile){
		for (int i : doorTiles)
			if (tile == i)
				return true;
		return false;
	}

	private static final int RAISED_OTHER           =                       xy(9, 8);  //24 slots
	public static final int RAISED_ALCHEMY_POT      = RAISED_OTHER+0;
	public static final int RAISED_BARRICADE        = RAISED_OTHER+1;
	public static final int RAISED_HIGH_GRASS       = RAISED_OTHER+2;
	public static final int RAISED_FURROWED_GRASS   = RAISED_OTHER+3;

	public static final int RAISED_HIGH_GRASS_ALT   = RAISED_OTHER+5;
	public static final int RAISED_FURROWED_ALT     = RAISED_OTHER+6;

	public static final int RAISED_STATUE           = RAISED_OTHER+8;
	public static final int RAISED_STATUE_SP        = RAISED_OTHER+9;

	public static final int RAISED_MINE_CRYSTAL     = RAISED_OTHER+12;
	public static final int RAISED_MINE_CRYSTAL_ALT = RAISED_OTHER+13;
	public static final int RAISED_MINE_CRYSTAL_ALT_2=RAISED_OTHER+14;
	public static final int RAISED_MINE_BOULDER     = RAISED_OTHER+12;
	public static final int RAISED_MINE_BOULDER_ALT = RAISED_OTHER+13;
	public static final int RAISED_MINE_BOULDER_ALT_2=RAISED_OTHER+14;


	/**********************************************************************
	 * Raised Tiles, Upper Layer
	 **********************************************************************/

	//+1 for open right, +2 for open right-below, +4 for open left-below, +8 for open left.
	public static final int WALLS_INTERNAL              =                   xy(1, 10);  //48 slots
	private static final int WALL_INTERNAL              = WALLS_INTERNAL+0;
	private static final int WALL_INTERNAL_DECO         = WALLS_INTERNAL+16;
	private static final int WALL_INTERNAL_WOODEN       = WALLS_INTERNAL+32;

	public static int stitchInternalWallTile(int tile, int right, int rightBelow, int below, int leftBelow, int left){
		int result;

		if (tile == Terrain.BOOKSHELF || below == Terrain.BOOKSHELF)        result = WALL_INTERNAL_WOODEN;
		//TODO currently this line on triggers on mining floors, do we want to make it universal?
		else if (Dungeon.branch == 1 && tile == Terrain.WALL_DECO)          result = WALL_INTERNAL_DECO;
		else                                                                result = WALL_INTERNAL;

		if (!wallStitcheable(right))        result += 1;
		if (!wallStitcheable(rightBelow))   result += 2;
		if (!wallStitcheable(leftBelow))    result += 4;
		if (!wallStitcheable(left))         result += 8;
		return result;
	}

	//+1 for open to the down-right, +2 for open to the down-left
	private static final int WALLS_OVERHANG             =                   xy(1, 13);  //32 slots
	public static final int WALL_OVERHANG                   = WALLS_OVERHANG+0;
	public static final int WALL_OVERHANG_DECO              = WALLS_OVERHANG+4;
	public static final int WALL_OVERHANG_WOODEN            = WALLS_OVERHANG+8;
	public static final int DOOR_SIDEWAYS_OVERHANG          = WALLS_OVERHANG+16;
	public static final int DOOR_SIDEWAYS_OVERHANG_CLOSED   = WALLS_OVERHANG+20;
	public static final int DOOR_SIDEWAYS_OVERHANG_LOCKED   = WALLS_OVERHANG+24;
	public static final int DOOR_SIDEWAYS_OVERHANG_CRYSTAL  = WALLS_OVERHANG+28;


	public static int stitchWallOverhangTile(int tile, int rightBelow, int below, int leftBelow){
		int visual;
		if (tile == Terrain.OPEN_DOOR)                              visual = DOOR_SIDEWAYS_OVERHANG;
		else if (tile == Terrain.DOOR)                              visual = DOOR_SIDEWAYS_OVERHANG_CLOSED;
		else if (tile == Terrain.LOCKED_DOOR)                       visual = DOOR_SIDEWAYS_OVERHANG_LOCKED;
		else if (tile == Terrain.CRYSTAL_DOOR)                      visual = DOOR_SIDEWAYS_OVERHANG_CRYSTAL;
		//TODO currently this line on triggers on mining floors, do we want to make it universal?
		else if (Dungeon.branch == 1 && below == Terrain.WALL_DECO) visual = WALL_OVERHANG_DECO;
		else if (below == Terrain.BOOKSHELF)                        visual = WALL_OVERHANG_WOODEN;
		else                                                        visual = WALL_OVERHANG;

		if (!wallStitcheable(rightBelow))  visual += 1;
		if (!wallStitcheable(leftBelow))   visual += 2;

		return visual;
	}

	public static final int DOOR_OVERHANG               =                   xy(1, 15);  //8 slots
	public static final int DOOR_OVERHANG_OPEN          = DOOR_OVERHANG+1;
	public static final int DOOR_OVERHANG_CRYSTAL       = DOOR_OVERHANG+2;
	public static final int DOOR_SIDEWAYS               = DOOR_OVERHANG+3;
	public static final int DOOR_SIDEWAYS_LOCKED        = DOOR_OVERHANG+4;
	public static final int DOOR_SIDEWAYS_CRYSTAL       = DOOR_OVERHANG+5;
	//exit visuals are rendered flat atm, so they actually underhang
	public static final int EXIT_UNDERHANG              = DOOR_OVERHANG+6;


	private static final int OTHER_OVERHANG             =                   xy(9, 15);  //24 slots
	public static final int ALCHEMY_POT_OVERHANG        = OTHER_OVERHANG+0;
	public static final int BARRICADE_OVERHANG          = OTHER_OVERHANG+1;
	public static final int HIGH_GRASS_OVERHANG         = OTHER_OVERHANG+2;
	public static final int FURROWED_OVERHANG           = OTHER_OVERHANG+3;

	public static final int HIGH_GRASS_OVERHANG_ALT     = OTHER_OVERHANG+5;
	public static final int FURROWED_OVERHANG_ALT       = OTHER_OVERHANG+6;

	public static final int STATUE_OVERHANG             = OTHER_OVERHANG+8;
	public static final int STATUE_SP_OVERHANG          = OTHER_OVERHANG+9;

	public static final int MINE_CRYSTAL_OVERHANG       = OTHER_OVERHANG+12;
	public static final int MINE_CRYSTAL_OVERHANG_ALT   = OTHER_OVERHANG+13;
	public static final int MINE_CRYSTAL_OVERHANG_ALT_2 = OTHER_OVERHANG+14;
	public static final int MINE_BOULDER_OVERHANG       = OTHER_OVERHANG+12;
	public static final int MINE_BOULDER_OVERHANG_ALT   = OTHER_OVERHANG+13;
	public static final int MINE_BOULDER_OVERHANG_ALT_2 = OTHER_OVERHANG+14;

	public static final int HIGH_GRASS_UNDERHANG        = OTHER_OVERHANG+18;
	public static final int FURROWED_UNDERHANG          = OTHER_OVERHANG+19;

	public static final int HIGH_GRASS_UNDERHANG_ALT    = OTHER_OVERHANG+21;
	public static final int FURROWED_UNDERHANG_ALT      = OTHER_OVERHANG+22;

	/**********************************************************************
	 * Logic for the selection of tile visuals
	 **********************************************************************/

	//These visuals always directly represent a game tile with no stitching required
	public static SparseArray<Integer> directVisuals = new SparseArray<>();
	static {
		directVisuals.put(Terrain.EMPTY,            FLOOR);
		directVisuals.put(Terrain.GRASS,            GRASS);
		directVisuals.put(Terrain.EMPTY_WELL,       EMPTY_WELL);
		directVisuals.put(Terrain.ENTRANCE,         ENTRANCE);
		directVisuals.put(Terrain.EXIT,             EXIT);
		directVisuals.put(Terrain.EMBERS,           EMBERS);
		directVisuals.put(Terrain.PEDESTAL,         PEDESTAL);
		directVisuals.put(Terrain.EMPTY_SP,         FLOOR_SP);
		directVisuals.put(Terrain.ENTRANCE_SP,      ENTRANCE_SP);

		directVisuals.put(Terrain.SECRET_TRAP,      directVisuals.get(Terrain.EMPTY));
		directVisuals.put(Terrain.TRAP,             directVisuals.get(Terrain.EMPTY));
		directVisuals.put(Terrain.INACTIVE_TRAP,    directVisuals.get(Terrain.EMPTY));
		directVisuals.put(Terrain.CUSTOM_DECO,      directVisuals.get(Terrain.EMPTY));
		directVisuals.put(Terrain.CUSTOM_DECO_EMPTY,directVisuals.get(Terrain.EMPTY));

		directVisuals.put(Terrain.EMPTY_DECO,       FLOOR_DECO);
		directVisuals.put(Terrain.LOCKED_EXIT,      LOCKED_EXIT);
		directVisuals.put(Terrain.UNLOCKED_EXIT,    UNLOCKED_EXIT);
		directVisuals.put(Terrain.WELL,             WELL);

	}

	//These visuals directly represent game tiles (no stitching) when terrain is being shown as flat
	public static SparseArray<Integer> directFlatVisuals = new SparseArray<>();
	static {
		directFlatVisuals.put(Terrain.WALL,             FLAT_WALL);
		directFlatVisuals.put(Terrain.DOOR,             FLAT_DOOR);
		directFlatVisuals.put(Terrain.OPEN_DOOR,        FLAT_DOOR_OPEN);
		directFlatVisuals.put(Terrain.LOCKED_DOOR,      FLAT_DOOR_LOCKED);
		directFlatVisuals.put(Terrain.CRYSTAL_DOOR,     FLAT_DOOR_CRYSTAL);
		directFlatVisuals.put(Terrain.WALL_DECO,        FLAT_WALL_DECO);
		directFlatVisuals.put(Terrain.BOOKSHELF,        FLAT_BOOKSHELF);
		directFlatVisuals.put(Terrain.ALCHEMY,          FLAT_ALCHEMY_POT);
		directFlatVisuals.put(Terrain.BARRICADE,        FLAT_BARRICADE);
		directFlatVisuals.put(Terrain.HIGH_GRASS,       FLAT_HIGH_GRASS);
		directFlatVisuals.put(Terrain.FURROWED_GRASS,   FLAT_FURROWED_GRASS);

		directFlatVisuals.put(Terrain.STATUE,           FLAT_STATUE);
		directFlatVisuals.put(Terrain.STATUE_SP,        FLAT_STATUE_SP);

		directFlatVisuals.put(Terrain.MINE_CRYSTAL,     FLAT_MINE_CRYSTAL);
		directFlatVisuals.put(Terrain.MINE_BOULDER,     FLAT_MINE_BOULDER);

		directFlatVisuals.put(Terrain.SECRET_DOOR,      directFlatVisuals.get(Terrain.WALL));
	}


	/**********************************************************************
	 * Logic for the selection of alternate tile visuals
	 **********************************************************************/

	public static byte[] tileVariance;

	public static void setupVariance(int size, long seed){
		Random.pushGenerator( seed );

			tileVariance = new byte[size];
			for (int i = 0; i < tileVariance.length; i++) {
				tileVariance[i] = (byte) Random.Int(100);
			}

		Random.popGenerator();
	}

	//These alt visuals will trigger 50% of the time (45% of the time if a rare alt is also present)
	public static SparseArray<Integer> commonAltVisuals = new SparseArray<>();
	static {
		commonAltVisuals.put(FLOOR,                 FLOOR_ALT_1);
		commonAltVisuals.put(GRASS,                 GRASS_ALT);
		commonAltVisuals.put(FLAT_WALL,             FLAT_WALL_ALT);
		commonAltVisuals.put(EMBERS,                EMBERS_ALT);
		commonAltVisuals.put(FLAT_WALL_DECO,        FLAT_WALL_DECO_ALT);
		commonAltVisuals.put(FLOOR_SP,              FLOOR_SP_ALT);
		commonAltVisuals.put(FLOOR_DECO,            FLOOR_DECO_ALT);

		commonAltVisuals.put(FLAT_BOOKSHELF,        FLAT_BOOKSHELF_ALT);
		commonAltVisuals.put(FLAT_HIGH_GRASS,       FLAT_HIGH_GRASS_ALT);
		commonAltVisuals.put(FLAT_FURROWED_GRASS,   FLAT_FURROWED_ALT);
		commonAltVisuals.put(FLAT_MINE_CRYSTAL,     FLAT_MINE_CRYSTAL_ALT);
		commonAltVisuals.put(FLAT_MINE_BOULDER,     FLAT_MINE_BOULDER_ALT);

		commonAltVisuals.put(RAISED_WALL,           RAISED_WALL_ALT);
		commonAltVisuals.put(RAISED_WALL_DECO,      RAISED_WALL_DECO_ALT);
		commonAltVisuals.put(RAISED_WALL_BOOKSHELF, RAISED_WALL_BOOKSHELF_ALT);

		commonAltVisuals.put(RAISED_HIGH_GRASS,     RAISED_HIGH_GRASS_ALT);
		commonAltVisuals.put(RAISED_FURROWED_GRASS, RAISED_FURROWED_ALT);
		commonAltVisuals.put(HIGH_GRASS_OVERHANG,   HIGH_GRASS_OVERHANG_ALT);
		commonAltVisuals.put(FURROWED_OVERHANG,     FURROWED_OVERHANG_ALT);
		commonAltVisuals.put(RAISED_MINE_CRYSTAL,   RAISED_MINE_CRYSTAL_ALT);
		commonAltVisuals.put(RAISED_MINE_BOULDER,   RAISED_MINE_BOULDER_ALT);
		commonAltVisuals.put(HIGH_GRASS_UNDERHANG,  HIGH_GRASS_UNDERHANG_ALT);
		commonAltVisuals.put(FURROWED_UNDERHANG,    FURROWED_UNDERHANG_ALT);
		commonAltVisuals.put(MINE_CRYSTAL_OVERHANG, MINE_CRYSTAL_OVERHANG_ALT);
		commonAltVisuals.put(MINE_BOULDER_OVERHANG, MINE_BOULDER_OVERHANG_ALT);
	}

	//These alt visuals trigger 5% of the time (and also override common alts when they show up)
	public static SparseArray<Integer> rareAltVisuals = new SparseArray<>();
	static {
		rareAltVisuals.put(FLOOR,                   FLOOR_ALT_2);
		rareAltVisuals.put(FLAT_MINE_CRYSTAL,       FLAT_MINE_CRYSTAL_ALT_2);
		rareAltVisuals.put(FLAT_MINE_BOULDER,       FLAT_MINE_BOULDER_ALT_2);
		rareAltVisuals.put(RAISED_MINE_CRYSTAL,     RAISED_MINE_CRYSTAL_ALT_2);
		rareAltVisuals.put(RAISED_MINE_BOULDER,     RAISED_MINE_BOULDER_ALT_2);
		rareAltVisuals.put(MINE_CRYSTAL_OVERHANG,   MINE_CRYSTAL_OVERHANG_ALT_2);
		rareAltVisuals.put(MINE_BOULDER_OVERHANG,   MINE_BOULDER_OVERHANG_ALT_2);
	}

	public static int getVisualWithAlts(int visual, int pos){
		if (tileVariance[pos] >= 95 && rareAltVisuals.containsKey(visual))
			return rareAltVisuals.get(visual);
		else if (tileVariance[pos] >= 50 && commonAltVisuals.containsKey(visual))
			return commonAltVisuals.get(visual);
		else
			return visual;
	}

}
