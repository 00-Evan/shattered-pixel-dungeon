/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;

public class DungeonTerrainTilemap extends DungeonTilemap {

	static DungeonTerrainTilemap instance;

	public DungeonTerrainTilemap(){
		super(Dungeon.level.tilesTex());

		map( Dungeon.level.map, Dungeon.level.width() );

		instance = this;
	}

	@Override
	protected int getTileVisual(int pos, int tile, boolean flat) {
		int visual = DungeonTileSheet.directVisuals.get(tile, -1);
		if (visual != -1) return DungeonTileSheet.getVisualWithAlts(visual, pos);

		if (tile == Terrain.WATER) {
			return DungeonTileSheet.stitchWaterTile(
					map[pos + PathFinder.CIRCLE4[0]],
					map[pos + PathFinder.CIRCLE4[1]],
					map[pos + PathFinder.CIRCLE4[2]],
					map[pos + PathFinder.CIRCLE4[3]]
			);

		} else if (tile == Terrain.CHASM) {
			return DungeonTileSheet.stitchChasmTile( pos > mapWidth ? map[pos - mapWidth] : -1);
		}

		if (!flat) {
			if ((DungeonTileSheet.doorTile(tile))) {
				return DungeonTileSheet.getRaisedDoorTile(tile, map[pos - mapWidth]);
			} else if (DungeonTileSheet.wallStitcheable(tile)){
				return DungeonTileSheet.getRaisedWallTile(
						tile,
						pos,
						(pos+1) % mapWidth != 0 ?   map[pos + 1] : -1,
						pos + mapWidth < size ?     map[pos + mapWidth] : -1,
						pos % mapWidth != 0 ?       map[pos - 1] : -1
						);
			} else if (tile == Terrain.SIGN) {
				return DungeonTileSheet.RAISED_SIGN;
			} else if (tile == Terrain.STATUE) {
				return DungeonTileSheet.RAISED_STATUE;
			} else if (tile == Terrain.STATUE_SP) {
				return DungeonTileSheet.RAISED_STATUE_SP;
			} else if (tile == Terrain.ALCHEMY) {
				return DungeonTileSheet.RAISED_ALCHEMY_POT;
			} else if (tile == Terrain.BARRICADE) {
				return DungeonTileSheet.RAISED_BARRICADE;
			} else if (tile == Terrain.HIGH_GRASS) {
				return DungeonTileSheet.getVisualWithAlts(
						DungeonTileSheet.RAISED_HIGH_GRASS,
						pos);
			} else if (tile == Terrain.FURROWED_GRASS) {
				return DungeonTileSheet.getVisualWithAlts(
						DungeonTileSheet.RAISED_FURROWED_GRASS,
						pos);
			} else {
				return DungeonTileSheet.NULL_TILE;
			}
		} else {
			return DungeonTileSheet.getVisualWithAlts(
					DungeonTileSheet.directFlatVisuals.get(tile),
					pos);
		}

	}

	public static Image tile(int pos, int tile ) {
		Image img = new Image( instance.texture );
		img.frame( instance.tileset.get( instance.getTileVisual( pos, tile, true ) ) );
		return img;
	}

	@Override
	protected boolean needsRender(int pos) {
		return super.needsRender(pos) && data[pos] != DungeonTileSheet.WATER;
	}
}
