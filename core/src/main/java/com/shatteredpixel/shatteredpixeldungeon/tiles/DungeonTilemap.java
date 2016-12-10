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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

public class DungeonTilemap extends Tilemap {

	public static final int SIZE = 16;
	
	private static DungeonTilemap instance;

	private int[] map;

	public DungeonTilemap() {
		super(
			Dungeon.level.tilesTex(),
			new TextureFilm( Dungeon.level.tilesTex(), SIZE, SIZE ) );

		map( Dungeon.level.map, Dungeon.level.width() );
		
		instance = this;
	}

	@Override
	//we need to retain two arrays, map is the dungeon tilemap which we can reference.
	// Data is our own internal image representation of the tiles, which may differ.
	public void map(int[] data, int cols) {
		map = data;
		super.map(new int[data.length], cols);
	}

	@Override
	public synchronized void updateMap() {
		super.updateMap();
		for (int i = 0; i < data.length; i++)
			data[i] = getTileVisual(i ,map[i], false);
	}

	@Override
	public synchronized void updateMapCell(int cell) {
		//update in a 3x3 grid to account for neighbours which might also be affected
		if (Dungeon.level.insideMap(cell)) {
			super.updateMapCell(cell - mapWidth - 1);
			super.updateMapCell(cell + mapWidth + 1);
			for (int i : PathFinder.NEIGHBOURS9)
				data[cell + i] = getTileVisual(cell + i, map[cell + i], false);

		//unless we're at the level's edge, then just do the one tile.
		} else {
			super.updateMapCell(cell);
			data[cell] = getTileVisual(cell, map[cell], false);
		}
	}

	private int getTileVisual(int pos, int tile, boolean flat) {
		int visual = DungeonTileSheet.directVisuals.get(tile, -1);

		if (tile == Terrain.WATER) {
			return DungeonTileSheet.getWaterTile(
					map[pos + PathFinder.CIRCLE4[0]],
					map[pos + PathFinder.CIRCLE4[1]],
					map[pos + PathFinder.CIRCLE4[2]],
					map[pos + PathFinder.CIRCLE4[3]]
			);

		} else if (tile == Terrain.CHASM && pos >= mapWidth) {
			return DungeonTileSheet.chasmStitcheable.get(map[pos - mapWidth], DungeonTileSheet.CHASM);
		}

		if (!flat) {
			if ((tile == Terrain.DOOR || tile == Terrain.LOCKED_DOOR || tile == Terrain.OPEN_DOOR) && map[pos - mapWidth] == Terrain.WALL) {
				return DungeonTileSheet.RAISED_DOOR_SIDEWAYS;
			} else if (tile == Terrain.DOOR) {
				return DungeonTileSheet.RAISED_DOOR;
			} else if (tile == Terrain.OPEN_DOOR) {
				return DungeonTileSheet.RAISED_DOOR_OPEN;
			} else if (tile == Terrain.LOCKED_DOOR) {
				return DungeonTileSheet.RAISED_DOOR_LOCKED;
			} else if (tile == Terrain.WALL || tile == Terrain.WALL_DECO){
				if (tile == Terrain.WALL) {
					if (pos + mapWidth < size && (map[pos + mapWidth] == Terrain.DOOR || map[pos + mapWidth] == Terrain.LOCKED_DOOR || map[pos + mapWidth] == Terrain.OPEN_DOOR)){
						visual = DungeonTileSheet.RAISED_WALL_DOOR;
					} else {
						visual = DungeonTileSheet.RAISED_WALL;
					}
				} else
					visual = DungeonTileSheet.RAISED_WALL_DECO;

				visual = DungeonTileSheet.getVisualWithAlts(visual, pos);

				if (pos % mapWidth != 0 && !DungeonTileSheet.wallStitcheable.contains(map[pos - 1]))
					visual += 2;
				if (pos % mapWidth != mapWidth-1 && !DungeonTileSheet.wallStitcheable.contains(map[pos + 1]))
					visual += 1;

				return visual;
			}
		} else {
			if (visual == -1)
				visual = DungeonTileSheet.directFlatVisuals.get(tile);
		}

		return DungeonTileSheet.getVisualWithAlts(visual, pos);
	}

	public int screenToTile(int x, int y ) {
		Point p = camera().screenToCamera( x, y ).
			offset( this.point().negate() ).
			invScale( SIZE ).
			floor();
		return p.x >= 0
				&& p.x < Dungeon.level.width()
				&& p.y >= 0
				&& p.y < Dungeon.level.height() ?
					p.x + p.y * Dungeon.level.width()
					: -1;
	}
	
	@Override
	public boolean overlapsPoint( float x, float y ) {
		return true;
	}
	
	public void discover( int pos, int oldValue ) {
		
		final Image tile = tile( pos, oldValue );
		tile.point( tileToWorld( pos ) );

		parent.add( tile );
		
		parent.add( new AlphaTweener( tile, 0, 0.6f ) {
			protected void onComplete() {
				tile.killAndErase();
				killAndErase();
			};
		} );
	}
	
	public static PointF tileToWorld( int pos ) {
		return new PointF( pos % Dungeon.level.width(), pos / Dungeon.level.width()  ).scale( SIZE );
	}
	
	public static PointF tileCenterToWorld( int pos ) {
		return new PointF(
			(pos % Dungeon.level.width() + 0.5f) * SIZE,
			(pos / Dungeon.level.width() + 0.5f) * SIZE );
	}
	
	public static Image tile( int pos, int tile ) {
		Image img = new Image( instance.texture );
		img.frame( instance.tileset.get( instance.getTileVisual( pos, tile, true ) ) );
		return img;
	}
	
	@Override
	public boolean overlapsScreenPoint( int x, int y ) {
		return true;
	}

	@Override
	protected boolean needsRender(int pos) {
		return (Level.discoverable[pos] || data[pos] == DungeonTileSheet.CHASM)
				&& data[pos] != DungeonTileSheet.WATER;
	}
}
