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
package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.List;

public class DungeonWallsTilemap extends Tilemap {

	public static final int SIZE = 16;

	private static DungeonWallsTilemap instance;

	//These tiles count as wall for the purposes of wall stitching
	public static List wallStitcheable = Arrays.asList(
			Terrain.WALL, Terrain.WALL_DECO, Terrain.SECRET_DOOR,
			Terrain.LOCKED_EXIT, Terrain.UNLOCKED_EXIT
	);

	private int[] map;
	private float[] tileVariance;

	public DungeonWallsTilemap(){
		super(
				Dungeon.level.tilesTex(),
				new TextureFilm( Dungeon.level.tilesTex(), SIZE, SIZE ) );

		Random.seed( Dungeon.seedCurDepth());
		tileVariance = new float[Dungeon.level.map.length];
		for (int i = 0; i < tileVariance.length; i++)
			tileVariance[i] = Random.Float();
		Random.seed();

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
			data[i] = getTileVisual(i ,map[i]);
	}

	@Override
	public synchronized void updateMapCell(int cell) {
		//update in a 3x3 grid to account for neighbours which might also be affected
		if (Dungeon.level.insideMap(cell)) {
			super.updateMapCell(cell - mapWidth - 1);
			super.updateMapCell(cell + mapWidth + 1);
			for (int i : PathFinder.NEIGHBOURS9)
				data[cell + i] = getTileVisual(cell + i, map[cell + i]);

			//unless we're at the level's edge, then just do the one tile.
		} else {
			super.updateMapCell(cell);
			data[cell] = getTileVisual(cell, map[cell]);
		}
	}

	private int getTileVisual(int pos, int tile){

		if (wallStitcheable.contains(tile)) {
			if (pos + mapWidth < size && !wallStitcheable.contains(map[pos + mapWidth])){

				if (map[pos + mapWidth] == Terrain.DOOR){
					return DungeonTileSheet.DOOR_SIDEWAYS;
				} else if (map[pos + mapWidth] == Terrain.LOCKED_DOOR){
					return DungeonTileSheet.DOOR_SIDEWAYS_LOCKED;
				}

			} else {
				//otherwise, need to stitch with right, bottom-right, bottom-left, and left.
				int visual = DungeonTileSheet.WALLS_INTERNAL;
				if (pos % mapWidth != 0 && !wallStitcheable.contains(map[pos - 1]))
					visual += 8;
				if (pos % mapWidth != 0 && pos + mapWidth < size && !wallStitcheable.contains(map[pos - 1 + mapWidth]))
					visual += 4;
				if ((pos+1) % mapWidth != 0 && pos + mapWidth < size && !wallStitcheable.contains(map[pos + 1 + mapWidth]))
					visual += 2;
				if ((pos+1) % mapWidth != 0 && !wallStitcheable.contains(map[pos + 1]))
					visual += 1;
				return visual;
			}

		} else if (Dungeon.level.insideMap(pos) && wallStitcheable.contains(map[pos+mapWidth])) {

			int visual;
			if (map[pos] == Terrain.DOOR || map[pos] == Terrain.LOCKED_DOOR)
				visual = DungeonTileSheet.DOOR_SIDEWAYS_OVERHANG;
			else if (map[pos] == Terrain.OPEN_DOOR)
				visual = DungeonTileSheet.DOOR_SIDEWAYS_OVERHANG_OPEN;
			else
				visual = DungeonTileSheet.WALL_OVERHANG;

			if (!wallStitcheable.contains(map[pos - 1 + mapWidth]))
				visual += 2;
			if (!wallStitcheable.contains(map[pos + 1 + mapWidth]))
				visual += 1;

			return visual;

		} else if (Dungeon.level.insideMap(pos) && (map[pos+mapWidth] == Terrain.DOOR || map[pos+mapWidth] == Terrain.LOCKED_DOOR) ) {
			return DungeonTileSheet.DOOR_OVERHANG;
		} else if (Dungeon.level.insideMap(pos) && map[pos+mapWidth] == Terrain.OPEN_DOOR ) {
			return DungeonTileSheet.DOOR_OVERHANG_OPEN;
		}

		return -1;
	}

	@Override
	public boolean overlapsPoint( float x, float y ) {
		return true;
	}

	@Override
	public boolean overlapsScreenPoint( int x, int y ) {
		return true;
	}

	@Override
	protected boolean needsRender(int pos) {
		return data[pos] != -1;
	}
}
