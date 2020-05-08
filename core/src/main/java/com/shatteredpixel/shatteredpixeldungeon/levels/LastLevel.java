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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.Arrays;

public class LastLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = Math.min(4, viewDistance);
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}

	@Override
	public void create() {
		super.create();
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
		for (int i = (height-ROOM_TOP+2)*width; i < length; i++){
			passable[i] = avoid[i] = false;
			solid[i] = true;
		}
		for (int i = (height-ROOM_TOP+1)*width; i < length; i++){
			if (i % width < 4 || i % width > 12 || i >= (length-width)){
				discoverable[i] = false;
			} else {
				visited[i] = true;
			}
		}
	}

	private static final int ROOM_TOP = 10;

	@Override
	protected boolean build() {
		
		setSize(16, 64);
		Arrays.fill( map, Terrain.CHASM );

		final int MID = width/2;

		Painter.fill( this, 0, height-1, width, 1, Terrain.WALL );
		Painter.fill( this, MID - 1, 10, 3, (height-11), Terrain.EMPTY);
		Painter.fill( this, MID - 2, height - 3, 5, 1, Terrain.EMPTY);
		Painter.fill( this, MID - 3, height - 2, 7, 1, Terrain.EMPTY);

		entrance = (height-ROOM_TOP) * width() + MID;
		Painter.fill(this, 0, height - ROOM_TOP, width, 2, Terrain.WALL);
		map[entrance] = Terrain.ENTRANCE;
		map[entrance+width] = Terrain.ENTRANCE;
		Painter.fill(this, 0, height - ROOM_TOP + 2, width, 8, Terrain.EMPTY);
		Painter.fill(this, MID-1, height - ROOM_TOP + 2, 3, 1, Terrain.ENTRANCE);

		exit = 12*(width()) + MID;

		for (int i=0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 5 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			}
		}

		Painter.fill( this, MID - 2, 9, 5, 7, Terrain.EMPTY);
		Painter.fill( this, MID - 3, 10, 7, 5, Terrain.EMPTY);

		feeling = Feeling.NONE;
		viewDistance = 4;

		CustomTilemap vis = new CustomFloor();
		vis.setRect( 5, 0, 7, height - ROOM_TOP);
		customTiles.add(vis);

		vis = new CenterPieceVisuals();
		vis.pos(0, height - ROOM_TOP);
		customTiles.add(vis);

		vis = new CenterPieceWalls();
		vis.pos(0, height - ROOM_TOP-1);
		customWalls.add(vis);

		return true;
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		drop( new Amulet(), exit );
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int cell;
		do {
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals);
		return visuals;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		for (int i=0; i < length(); i++) {
			int flags = Terrain.flags[map[i]];
			if ((flags & Terrain.PIT) != 0){
				passable[i] = avoid[i] = false;
				solid[i] = true;
			}
		}
		for (int i = (height-ROOM_TOP+2)*width; i < length; i++){
			passable[i] = avoid[i] = false;
			solid[i] = true;
		}
		for (int i = (height-ROOM_TOP+1)*width; i < length; i++){
			if (i % width < 4 || i % width > 12 || i >= (length-width)){
				discoverable[i] = false;
			} else {
				visited[i] = true;
			}
		}
	}

	public static class CustomFloor extends CustomTilemap {

		{
			texture = Assets.Environment.HALLS_SP;
		}

		private static final int[] CANDLES = new int[]{
				-1, 42, 46, 46, 46, 43, -1,
				42, 46, 46, 46, 46, 46, 43,
				46, 46, 45, 19, 44, 46, 46,
				46, 46, 19, 19, 19, 46, 46,
				46, 46, 43, 19, 42, 46, 46,
				44, 46, 46, 19, 46, 46, 45,
				-1, 44, 45, 19, 44, 45, -1
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();

			int candlesStart = Dungeon.level.exit - 3 - 3*Dungeon.level.width();

			int cell = tileX + tileY * Dungeon.level.width();
			int[] map = Dungeon.level.map;
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i % tileW == 0){
					cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
				}
				if (cell == candlesStart){
					for (int candle : CANDLES) {
						if (data[i] == 0) data[i] = candle;

						if (data[i] == 46 && DungeonTileSheet.tileVariance[cell] >= 50){
							data[i] ++;
						}

						if (Statistics.amuletObtained && data[i] > 40){
							data[i] += 8;
						}

						if (map[cell] != Terrain.CHASM && map[cell+Dungeon.level.width] == Terrain.CHASM) {
							data[i+tileW] = 6;
						}

						i++;
						cell++;
						if (i % tileW == 0){
							cell = tileX + (tileY + i / tileW) * Dungeon.level.width();
						}
					}
				}
				if (map[cell] == Terrain.EMPTY_DECO) {
					if (Statistics.amuletObtained){
						data[i] = 31;
					} else {
						data[i] = 27;
					}
				} else if (map[cell] == Terrain.EMPTY) {
					data[i] = 19;
				} else if (data[i] == 0) {
					data[i] = -1;
				}
				cell++;
			}
			v.map( data, tileW );
			return v;
		}

	}

	public static class CenterPieceVisuals extends CustomTilemap {

		{
			texture = Assets.Environment.HALLS_SP;

			tileW = 16;
			tileH = 10;
		}

		private static final int[] map = new int[]{
				-1, -1, -1, -1, -1, -1, -1, -1, 19, -1, -1, -1, -1, -1, -1, -1,
				 0,  0,  0,  0,  8,  9, 10, 11, 19, 11, 12, 13, 14,  0,  0,  0,
				 0,  0,  0,  0, 16, 17, 18, 31, 19, 31, 20, 21, 22,  0,  0,  0,
				 0,  0,  0,  0, 24, 25, 26, 19, 19, 19, 28, 29, 30,  0,  0,  0,
				 0,  0,  0,  0, 24, 25, 26, 19, 19, 19, 28, 29, 30,  0,  0,  0,
				 0,  0,  0,  0, 24, 25, 26, 19, 19, 19, 28, 29, 30,  0,  0,  0,
				 0,  0,  0,  0, 24, 25, 34, 35, 35, 35, 34, 29, 30,  0,  0,  0,
				 0,  0,  0,  0, 40, 41, 36, 36, 36, 36, 36, 40, 41,  0,  0,  0,
				 0,  0,  0,  0, 48, 49, 36, 36, 36, 36, 36, 48, 49,  0,  0,  0,
				 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}

	public static class CenterPieceWalls extends CustomTilemap {

		{
			texture = Assets.Environment.HALLS_SP;

			tileW = 16;
			tileH = 9;
		}

		private static final int[] map = new int[]{
				 4,  4,  4,  4,  4,  4,  4,  5,  7,  3,  4,  4,  4,  4,  4,  4,
				 0,  0,  0,  0,  0,  0,  0,  1, 15,  2,  0,  0,  0,  0,  0,  0,
				-1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, 32, 33, -1, -1, -1, -1, -1, 32, 33, -1, -1, -1,
				-1, -1, -1, -1, 40, 41, -1, -1, -1, -1, -1, 40, 41, -1, -1, -1,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(map, tileW);
			return v;
		}
	}
}
