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

package com.shatteredpixel.shatteredpixeldungeon.tiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

public class WallOcclusionTilemap extends DungeonTilemap {

	public static final int SIZE = 16;

	//direct values
	private static final int CLEARED        = 0;
	private static final int EMPTY          = 0;
	private static final int DOOR_VERT      = 6;
	private static final int DOOR_HORIZ     = 7;

	//modifiers, added together to form tile
	private static final int WALL_ABOVE     = 40;
	private static final int WALL_LEFT_BELOW= 1;
	private static final int WALL_LEFT      = 2;
	private static final int WALL_LEFT_ABOVE= 3;
	private static final int WALL_LEFT_BOTH = 4; //both above and below
	private static final int WALL_RIGHT_BELOW= 8;
	private static final int WALL_RIGHT     = 16;
	private static final int WALL_RIGHT_ABOVE= 24;
	private static final int WALL_RIGHT_BOTH = 32; //both above and below

	public WallOcclusionTilemap() {
		super(Assets.Environment.OCCLUSION_SHADOWS);
		map( Dungeon.level.map, Dungeon.level.width());
	}

	protected int getTileVisual( int pos, int tile, boolean flat ){
		if (!Dungeon.level.insideMap(pos) || !Dungeon.level.discoverable[pos]){
			return CLEARED;
		}

		int curr = EMPTY;
		if (DungeonTileSheet.doorTile(tile)){

			if (wall(pos-1) && wall(pos+1)){
				curr = DOOR_HORIZ;
			} else {
				curr = DOOR_VERT;
			}

		} else if (!DungeonTileSheet.wallStitcheable(tile) && !Dungeon.level.pit[pos]) {

			//branch for above wall
			//we specifically skip this for alchemy pots to preserve their passthrough to water layer
			if (wall(pos-mapWidth) && map[pos] != Terrain.ALCHEMY) {
				curr += WALL_ABOVE;

				//left
				if (wall(pos-1)){
					curr += WALL_LEFT;
				} else if (wall(pos+mapWidth-1)){
					curr += WALL_LEFT_BELOW;
				}

				//right
				if (wall(pos+1)){
					curr += WALL_RIGHT;
				} else if (wall(pos+mapWidth+1)){
					curr += WALL_RIGHT_BELOW;
				}

				//more cases for no wall above
			} else {

				//left
				if (wall(pos - 1)) {
					curr += WALL_LEFT;
				} else if (wall(pos + mapWidth - 1)) {
					if (wall(pos - mapWidth - 1)) {
						curr += WALL_LEFT_BOTH;
					} else {
						curr += WALL_LEFT_BELOW;
					}
				} else if (wall(pos - mapWidth - 1)) {
					curr += WALL_LEFT_ABOVE;
				}

				//right
				if (wall(pos + 1)) {
					curr += WALL_RIGHT;
				} else if (wall(pos + mapWidth + 1)) {
					if (wall(pos - mapWidth + 1)) {
						curr += WALL_RIGHT_BOTH;
					} else {
						curr += WALL_RIGHT_BELOW;
					}
				} else if (wall(pos - mapWidth + 1)) {
					curr += WALL_RIGHT_ABOVE;
				}

			}

		} else {
			curr = EMPTY;
		}
		return curr;
	}

	private boolean wall(int cell) {
		return DungeonTileSheet.wallStitcheable(map[cell]);
	}

	private boolean door(int cell) {
		return DungeonTileSheet.doorTile(map[cell]);
	}
}
