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

import java.util.HashSet;

public class RaisedTerrainTilemap extends DungeonTilemap {
	
	public RaisedTerrainTilemap() {
		super(Assets.Environment.RAISED_TERRAIN);
		skipCells.clear();
		map( Dungeon.level.map, Dungeon.level.width() );
	}

	public static HashSet<Integer> skipCells = new HashSet<>();

	@Override
	protected int getTileVisual(int pos, int tile, boolean flat) {
		
		if (flat) return -1;

		if (skipCells.contains(pos)){
			return -1;
		}

		int region = (Dungeon.depth-1)/5;
		int regionOffset = region*4;

		if (tile == Terrain.HIGH_GRASS){
			if (DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.RAISED_HIGH_GRASS, pos) == DungeonTileSheet.RAISED_HIGH_GRASS_ALT){
				return regionOffset + 2;
			} else {
				return regionOffset;
			}
		} else if (tile == Terrain.FURROWED_GRASS){
			if (DungeonTileSheet.getVisualWithAlts(DungeonTileSheet.RAISED_FURROWED_GRASS, pos) == DungeonTileSheet.RAISED_FURROWED_ALT){
				return regionOffset + 1 + 2;
			} else {
				return regionOffset + 1;
			}
		}
		
		return -1;
	}
}
