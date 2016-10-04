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
		defaultVisuals.put(Terrain.CHASM,           0);
		defaultVisuals.put(Terrain.EMPTY,           1);
		defaultVisuals.put(Terrain.GRASS,           2);
		defaultVisuals.put(Terrain.EMPTY_WELL,      3);
		defaultVisuals.put(Terrain.WALL,            4);
		defaultVisuals.put(Terrain.DOOR,            5);
		defaultVisuals.put(Terrain.OPEN_DOOR,       6);
		defaultVisuals.put(Terrain.ENTRANCE,        7);
		defaultVisuals.put(Terrain.EXIT,            8);
		defaultVisuals.put(Terrain.EMBERS,          9);
		defaultVisuals.put(Terrain.LOCKED_DOOR,     10);
		defaultVisuals.put(Terrain.PEDESTAL,        11);
		defaultVisuals.put(Terrain.WALL_DECO,       12);
		defaultVisuals.put(Terrain.BARRICADE,       13);
		defaultVisuals.put(Terrain.EMPTY_SP,        14);
		defaultVisuals.put(Terrain.HIGH_GRASS,      15);

		defaultVisuals.put(Terrain.SECRET_DOOR,     defaultVisuals.get(Terrain.WALL));
		defaultVisuals.put(Terrain.SECRET_TRAP,     defaultVisuals.get(Terrain.EMPTY));
		defaultVisuals.put(Terrain.TRAP,            defaultVisuals.get(Terrain.EMPTY));
		defaultVisuals.put(Terrain.INACTIVE_TRAP,   defaultVisuals.get(Terrain.EMPTY));

		defaultVisuals.put(Terrain.EMPTY_DECO,      16);
		defaultVisuals.put(Terrain.LOCKED_EXIT,     17);
		defaultVisuals.put(Terrain.UNLOCKED_EXIT,   18);
		defaultVisuals.put(Terrain.SIGN,            19);
		defaultVisuals.put(Terrain.WELL,            20);
		defaultVisuals.put(Terrain.STATUE,          21);
		defaultVisuals.put(Terrain.STATUE_SP,       22);
		defaultVisuals.put(Terrain.BOOKSHELF,       23);
		defaultVisuals.put(Terrain.ALCHEMY,         24);

		defaultVisuals.put(Terrain.WATER,           63);
	}

	//These alt visuals will trigger 50% of the time
	public static SparseIntArray commonAltVisuals = new SparseIntArray(32);
	static {
		commonAltVisuals.put(1,                 38);
		commonAltVisuals.put(2,                 39);
		commonAltVisuals.put(4,                 40);
		commonAltVisuals.put(9,                 41);
		commonAltVisuals.put(12,                42);
		commonAltVisuals.put(14,                43);
		commonAltVisuals.put(15,                44);
		commonAltVisuals.put(16,                45);
		commonAltVisuals.put(23,                46);
	}

	//These alt visuals trigger 10% of the time (and also override common alts when they show up)
	public static SparseIntArray rareAltVisuals = new SparseIntArray(32);
	static {
		rareAltVisuals.put(1,                   47);
	}

	//These tiles can stitch with water
	public static List waterStitcheable = Arrays.asList(
			Terrain.EMPTY, Terrain.GRASS, Terrain.EMPTY_WELL,
			Terrain.ENTRANCE, Terrain.EXIT, Terrain.EMBERS,
			Terrain.BARRICADE, Terrain.HIGH_GRASS, Terrain.SECRET_TRAP,
			Terrain.TRAP, Terrain.INACTIVE_TRAP, Terrain.EMPTY_DECO,
			Terrain.SIGN, Terrain.WELL, Terrain.STATUE, Terrain.ALCHEMY
	);

	//tiles that can stitch with chasms (from above), and which visual represents the stitching
	public static SparseIntArray chasmStitcheable = new SparseIntArray(32);
	static {
		//floor
		chasmStitcheable.put( Terrain.EMPTY, 32 );
		chasmStitcheable.put( Terrain.GRASS, 32 );
		chasmStitcheable.put( Terrain.EMPTY_WELL, 32 );
		chasmStitcheable.put( Terrain.HIGH_GRASS, 32 );
		chasmStitcheable.put( Terrain.EMPTY_DECO, 32 );
		chasmStitcheable.put( Terrain.SIGN, 32 );
		chasmStitcheable.put( Terrain.EMPTY_WELL, 32 );
		chasmStitcheable.put( Terrain.STATUE, 32 );

		//special floor
		chasmStitcheable.put( Terrain.EMPTY_SP, 33 );
		chasmStitcheable.put( Terrain.STATUE_SP, 33 );

		//wall
		chasmStitcheable.put( Terrain.WALL, 34 );
		chasmStitcheable.put( Terrain.DOOR, 34 );
		chasmStitcheable.put( Terrain.OPEN_DOOR, 34 );
		chasmStitcheable.put( Terrain.LOCKED_DOOR, 34 );
		chasmStitcheable.put( Terrain.WALL_DECO, 34 );

		//water
		chasmStitcheable.put( Terrain.WATER, 35 );
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
		int visual = defaultVisuals.get(tile);

		if (tile == Terrain.WATER){
			for (int i = 0; i < PathFinder.CIRCLE4.length; i++){
				if (waterStitcheable.contains(map[pos + PathFinder.CIRCLE4[i]])) {
					//equivalent to: cell -= 2^i
					visual -= (1 << i);
				}
			}
			return visual;

		} else if (tile == Terrain.CHASM && pos >= mapWidth) {
			return chasmStitcheable.get(map[pos - mapWidth], visual);

		} else if (tileVariance[pos] > 0.9f
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
		img.frame( instance.tileset.get( instance.getTileVisual( pos, tile ) ) );
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
