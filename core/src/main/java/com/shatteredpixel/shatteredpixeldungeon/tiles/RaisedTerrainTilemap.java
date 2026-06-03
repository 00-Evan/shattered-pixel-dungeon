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

public class RaisedTerrainTilemap extends DungeonTilemap {
	
	public RaisedTerrainTilemap() {
		super(Assets.Environment.RAISED_TERRAIN);
		map( Dungeon.level.map, Dungeon.level.width() );
	}
	
	@Override
	protected int getTileVisual(int pos, int tile, boolean flat) {
		
		if (flat) return -1;

		if (DungeonWallsTilemap.skipCells.contains(pos)){
			return -1;
		}

		int region = (Dungeon.depth-1)/5;
		int regionOffset = region*16;

		if (tile == Terrain.HIGH_GRASS){
			return regionOffset + (DungeonTileSheet.tileVariance[pos] >= 50 ? 2 : 0);
		} else if (tile == Terrain.FURROWED_GRASS){
			return regionOffset + 1 + (DungeonTileSheet.tileVariance[pos] >= 50 ? 2 : 0);
		} else if (tile == Terrain.BARRICADE){
			return regionOffset + 4;
		} else if (tile == Terrain.ALCHEMY){
			return regionOffset + 5;
		} else if (tile == Terrain.STATUE || tile == Terrain.STATUE_SP){
			return regionOffset + 6;
		} else if (tile == Terrain.REGION_DECO){
			return regionOffset + 7;
		} else if (tile == Terrain.REGION_DECO_ALT){
			return regionOffset + 8;
		}

		//specific cases for mine quest
		if (tile == Terrain.MINE_CRYSTAL){
			if (DungeonTileSheet.tileVariance[pos] >= 95){
				return regionOffset + 11;
			} else if (DungeonTileSheet.tileVariance[pos] >= 50){
				return regionOffset + 10;
			} else {
				return regionOffset + 9;
			}
		} else if (tile == Terrain.MINE_BOULDER){
			if (DungeonTileSheet.tileVariance[pos] >= 95){
				return regionOffset + 14;
			} else if (DungeonTileSheet.tileVariance[pos] >= 50){
				return regionOffset + 13;
			} else {
				return regionOffset + 12;
			}
		}
		
		return -1;
	}
}
