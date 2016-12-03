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
package com.shatteredpixel.shatteredpixeldungeon;

import android.util.SparseIntArray;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.Arrays;
import java.util.List;

public class DungeonTilemap extends Tilemap {

	public static final int SIZE = 16;
	
	private static DungeonTilemap instance;

	//Used to map dungeon tiles to their default visual values
	public static SparseIntArray defaultVisuals = new SparseIntArray(32);
	static {
		defaultVisuals.put(Terrain.CHASM,           DungeonTileSheet.CHASM);
		defaultVisuals.put(Terrain.EMPTY,           DungeonTileSheet.FLOOR);
		defaultVisuals.put(Terrain.GRASS,           DungeonTileSheet.GRASS);
		defaultVisuals.put(Terrain.EMPTY_WELL,      DungeonTileSheet.EMPTY_WELL);
		defaultVisuals.put(Terrain.WALL,            DungeonTileSheet.FLAT_WALL);
		defaultVisuals.put(Terrain.DOOR,            DungeonTileSheet.FLAT_DOOR);
		defaultVisuals.put(Terrain.OPEN_DOOR,       DungeonTileSheet.FLAT_DOOR_OPEN);
		defaultVisuals.put(Terrain.ENTRANCE,        DungeonTileSheet.ENTRANCE);
		defaultVisuals.put(Terrain.EXIT,            DungeonTileSheet.EXIT);
		defaultVisuals.put(Terrain.EMBERS,          DungeonTileSheet.EMBERS);
		defaultVisuals.put(Terrain.LOCKED_DOOR,     DungeonTileSheet.FLAT_DOOR_LOCKED);
		defaultVisuals.put(Terrain.PEDESTAL,        DungeonTileSheet.PEDESTAL);
		defaultVisuals.put(Terrain.WALL_DECO,       DungeonTileSheet.FLAT_WALL_DECO);
		defaultVisuals.put(Terrain.BARRICADE,       DungeonTileSheet.BARRICADE);
		defaultVisuals.put(Terrain.EMPTY_SP,        DungeonTileSheet.FLOOR_SP);
		defaultVisuals.put(Terrain.HIGH_GRASS,      DungeonTileSheet.HIGH_GRASS);

		defaultVisuals.put(Terrain.SECRET_DOOR,     defaultVisuals.get(Terrain.WALL));
		defaultVisuals.put(Terrain.SECRET_TRAP,     defaultVisuals.get(Terrain.EMPTY));
		defaultVisuals.put(Terrain.TRAP,            defaultVisuals.get(Terrain.EMPTY));
		defaultVisuals.put(Terrain.INACTIVE_TRAP,   defaultVisuals.get(Terrain.EMPTY));

		defaultVisuals.put(Terrain.EMPTY_DECO,      DungeonTileSheet.FLOOR_DECO);
		defaultVisuals.put(Terrain.LOCKED_EXIT,     DungeonTileSheet.LOCKED_EXIT);
		defaultVisuals.put(Terrain.UNLOCKED_EXIT,   DungeonTileSheet.UNLOCKED_EXIT);
		defaultVisuals.put(Terrain.SIGN,            DungeonTileSheet.SIGN);
		defaultVisuals.put(Terrain.WELL,            DungeonTileSheet.WELL);
		defaultVisuals.put(Terrain.STATUE,          DungeonTileSheet.STATUE);
		defaultVisuals.put(Terrain.STATUE_SP,       DungeonTileSheet.STATUE_SP);
		defaultVisuals.put(Terrain.BOOKSHELF,       DungeonTileSheet.BOOKSHELF);
		defaultVisuals.put(Terrain.ALCHEMY,         DungeonTileSheet.ALCHEMY_POT);

		defaultVisuals.put(Terrain.WATER,           DungeonTileSheet.WATER);
	}

	//These alt visuals will trigger 50% of the time
	public static SparseIntArray commonAltVisuals = new SparseIntArray(32);
	static {
		commonAltVisuals.put(DungeonTileSheet.FLOOR,            DungeonTileSheet.FLOOR_ALT_1);
		commonAltVisuals.put(DungeonTileSheet.GRASS,            DungeonTileSheet.GRASS_ALT);
		commonAltVisuals.put(DungeonTileSheet.FLAT_WALL,        DungeonTileSheet.FLAT_WALL_ALT);
		commonAltVisuals.put(DungeonTileSheet.EMBERS,           DungeonTileSheet.EMBERS_ALT);
		commonAltVisuals.put(DungeonTileSheet.FLAT_WALL_DECO,   DungeonTileSheet.FLAT_WALL_DECO_ALT);
		commonAltVisuals.put(DungeonTileSheet.FLOOR_SP,         DungeonTileSheet.FLOOR_SP_ALT);
		commonAltVisuals.put(DungeonTileSheet.HIGH_GRASS,       DungeonTileSheet.HIGH_GRASS_ALT);
		commonAltVisuals.put(DungeonTileSheet.FLOOR_DECO,       DungeonTileSheet.FLOOR_DECO_ALT);
		commonAltVisuals.put(DungeonTileSheet.BOOKSHELF,        DungeonTileSheet.BOOKSHELF_ALT);
	}

	//These alt visuals trigger 10% of the time (and also override common alts when they show up)
	public static SparseIntArray rareAltVisuals = new SparseIntArray(32);
	static {
		rareAltVisuals.put(DungeonTileSheet.FLOOR,              DungeonTileSheet.FLOOR_ALT_2);
	}

	//These tiles can stitch with water
	public static List waterStitcheable = Arrays.asList(
			Terrain.EMPTY, Terrain.GRASS, Terrain.EMPTY_WELL,
			Terrain.ENTRANCE, Terrain.EXIT, Terrain.EMBERS,
			Terrain.BARRICADE, Terrain.HIGH_GRASS, Terrain.SECRET_TRAP,
			Terrain.TRAP, Terrain.INACTIVE_TRAP, Terrain.EMPTY_DECO,
			Terrain.SIGN, Terrain.WELL, Terrain.STATUE, Terrain.ALCHEMY,
			Terrain.DOOR, Terrain.OPEN_DOOR, Terrain.LOCKED_DOOR
	);

	//tiles that can stitch with chasms (from above), and which visual represents the stitching
	public static SparseIntArray chasmStitcheable = new SparseIntArray(32);
	static {
		//floor
		chasmStitcheable.put( Terrain.EMPTY,        DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.GRASS,        DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMPTY_WELL,   DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.HIGH_GRASS,   DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMPTY_DECO,   DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.SIGN,         DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.EMPTY_WELL,   DungeonTileSheet.CHASM_FLOOR );
		chasmStitcheable.put( Terrain.STATUE,       DungeonTileSheet.CHASM_FLOOR );

		//special floor
		chasmStitcheable.put( Terrain.EMPTY_SP,     DungeonTileSheet.CHASM_FLOOR_SP );
		chasmStitcheable.put( Terrain.STATUE_SP,    DungeonTileSheet.CHASM_FLOOR_SP );

		//wall
		chasmStitcheable.put( Terrain.WALL,         DungeonTileSheet.CHASM_WALL );
		chasmStitcheable.put( Terrain.DOOR,         DungeonTileSheet.CHASM_WALL );
		chasmStitcheable.put( Terrain.OPEN_DOOR,    DungeonTileSheet.CHASM_WALL );
		chasmStitcheable.put( Terrain.LOCKED_DOOR,  DungeonTileSheet.CHASM_WALL );
		chasmStitcheable.put( Terrain.WALL_DECO,    DungeonTileSheet.CHASM_WALL );

		//water
		chasmStitcheable.put( Terrain.WATER,        DungeonTileSheet.CHASM_WATER );
	}

	private int[] map;
	private float[] tileVariance;

	public DungeonTilemap() {
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

	//These tiles count as wall for the purposes of wall stitching
	public static List wallStitcheable = Arrays.asList(
			Terrain.WALL, Terrain.WALL_DECO, Terrain.SECRET_DOOR,
			Terrain.LOCKED_EXIT, Terrain.UNLOCKED_EXIT
	);

	private int getTileVisual(int pos, int tile, boolean flat) {
		int visual = defaultVisuals.get(tile);

		if (tile == Terrain.WATER) {
			for (int i = 0; i < PathFinder.CIRCLE4.length; i++) {
				if (waterStitcheable.contains(map[pos + PathFinder.CIRCLE4[i]])) {
					//equivalent to: cell += 2^i
					visual += (1 << i);
				}
			}
			return visual;

		} else if (tile == Terrain.CHASM && pos >= mapWidth) {
			return chasmStitcheable.get(map[pos - mapWidth], visual);
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

				if (tileVariance[pos] > 0.5f)
					visual += 16;

				if (pos % mapWidth != 0 && !wallStitcheable.contains(map[pos - 1]))
					visual += 2;
				if (pos % mapWidth != mapWidth-1 && !wallStitcheable.contains(map[pos + 1]))
					visual += 1;

				return visual;
			}
		}

		if (tileVariance[pos] > 0.9f
				&& rareAltVisuals.indexOfKey(visual) >= 0){
			return rareAltVisuals.get(visual);

		} else if (tileVariance[pos] > 0.5f
			&& commonAltVisuals.indexOfKey(visual) >= 0) {
			return commonAltVisuals.get(visual);
		}

		return visual;
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
		return (Level.discoverable[pos] || data[pos] == defaultVisuals.get(Terrain.CHASM))
				&& data[pos] != defaultVisuals.get(Terrain.WATER);
	}
}
