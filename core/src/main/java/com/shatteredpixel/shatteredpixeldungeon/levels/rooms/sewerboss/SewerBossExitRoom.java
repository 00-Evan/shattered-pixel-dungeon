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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;

public class SewerBossExitRoom extends ExitRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 8);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 8);
	}
	
	public void paint(Level level) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
		
		Point c = center();
		
		Painter.fill( level, c.x-1, c.y-1, 3, 2, Terrain.WALL );
		Painter.fill( level, c.x-1, c.y+1, 3, 1, Terrain.EMPTY_SP );
		
		level.exit = level.pointToCell(c);
		Painter.set( level, level.exit, Terrain.LOCKED_EXIT );
		
		CustomTilemap vis = new SewerExit();
		vis.pos(c.x-1, c.y);
		level.customTiles.add(vis);
		
		vis = new SewerExitOverhang();
		vis.pos(c.x-1, c.y-2);
		level.customWalls.add(vis);
		
	}
	
	public static class SewerExit extends CustomTilemap {
		
		{
			texture = Assets.SEWER_BOSS;
			
			tileW = 3;
			tileH = 3;
		}
		
		private static final int[] layout = new int[]{
				21, -1, 22,
				23, 23, 23,
				24, 24, 24
		};
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(layout, 3);
			return v;
		}
		
		@Override
		public Image image(int tileX, int tileY) {
			if ((tileX == 1 && tileY == 0) || tileY == 2){
				return null;
			}
			return super.image(tileX, tileY);
		}
	}
	
	public static class SewerExitOverhang extends CustomTilemap {
		
		{
			texture = Assets.SEWER_BOSS;
			
			tileW = 3;
			tileH = 2;
		}
		
		private static final int[] layout = new int[]{
				16, 17, 18,
				19, -1, 20
		};
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(layout, 3);
			return v;
		}
		
		@Override
		public Image image(int tileX, int tileY) {
			return null;
		}
	}
}
