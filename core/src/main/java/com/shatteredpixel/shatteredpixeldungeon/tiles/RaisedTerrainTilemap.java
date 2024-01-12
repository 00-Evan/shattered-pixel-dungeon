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

public class RaisedTerrainTilemap extends DungeonTilemap {
	
	public RaisedTerrainTilemap() {
		super(Dungeon.level.tilesTex());
		map( Dungeon.level.map, Dungeon.level.width() );
	}
	
	@Override
	protected int getTileVisual(int pos, int tile, boolean flat) {
		
		if (flat) return -1;

		if (DungeonWallsTilemap.skipCells.contains(pos)){
			return -1;
		}

		if (tile == Terrain.HIGH_GRASS){
			return DungeonTileSheet.getVisualWithAlts(
					DungeonTileSheet.HIGH_GRASS_UNDERHANG,
					pos);
		} else if (tile == Terrain.FURROWED_GRASS){
			return DungeonTileSheet.getVisualWithAlts(
					DungeonTileSheet.FURROWED_UNDERHANG,
					pos);
		}
		
		return -1;
	}
}
