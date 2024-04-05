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

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CavesPainter extends RegularPainter {
	
	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {
		
		int w = level.width();
		int l = level.length();
		int[] map = level.map;

		for (Room r : rooms) {
			for (Room n : r.neigbours) {
				if (!r.connected.containsKey( n )) {
					mergeRooms(level, r, n, null, Terrain.CHASM);
				}
			}
		}

		for (Room room : rooms) {
			if (!(room instanceof StandardRoom)) {
				continue;
			}
			
			if (room.width() <= 4 || room.height() <= 4) {
				continue;
			}
			
			int s = room.square();

			//for each corner, we have a chance to fill based on room size
			//but not if filling that corner blocks a connection or places a visible trap next to a wall
			if (Random.Int( s ) > 8) {
				int corner = (room.left + 1) + (room.top + 1) * w;
				if (map[corner-1] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner-1))
						&& map[corner-w] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner-w))
						&& map[corner+1] != Terrain.TRAP && map[corner+w] != Terrain.TRAP) {
					map[corner] = Terrain.WALL;
					level.traps.remove(corner);
				}
			}
			
			if (Random.Int( s ) > 8) {
				int corner = (room.right - 1) + (room.top + 1) * w;
				if (map[corner+1] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner+1))
						&& map[corner-w] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner-w))
						&& map[corner-1] != Terrain.TRAP && map[corner+w] != Terrain.TRAP) {
					map[corner] = Terrain.WALL;
					level.traps.remove(corner);
				}
			}
			
			if (Random.Int( s ) > 8) {
				int corner = (room.left + 1) + (room.bottom - 1) * w;
				if (map[corner-1] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner-1))
						&& map[corner+w] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner+w))
						&& map[corner+1] != Terrain.TRAP && map[corner-w] != Terrain.TRAP) {
					map[corner] = Terrain.WALL;
					level.traps.remove(corner);
				}
			}
			
			if (Random.Int( s ) > 8) {
				int corner = (room.right - 1) + (room.bottom - 1) * w;
				if (map[corner+1] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner+1))
						&& map[corner+w] == Terrain.WALL && !room.connected.containsValue(level.cellToPoint(corner+w))
						&& map[corner-1] != Terrain.TRAP && map[corner-w] != Terrain.TRAP) {
					map[corner] = Terrain.WALL;
					level.traps.remove(corner);
				}
			}

		}

		for (int i=w + 1; i < l - w; i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i+1] == Terrain.WALL) {
					n++;
				}
				if (map[i-1] == Terrain.WALL) {
					n++;
				}
				if (map[i+w] == Terrain.WALL) {
					n++;
				}
				if (map[i-w] == Terrain.WALL) {
					n++;
				}
				if (Random.Int( 6 ) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		generateGold(level, rooms);

	}

	protected void generateGold(Level level, ArrayList<Room> rooms){
		int w = level.width();
		int l = level.length();
		int[] map = level.map;

		for (int i=0; i < l - w; i++) {
			if (map[i] == Terrain.WALL &&
					DungeonTileSheet.floorTile(map[i + w])
					&& Random.Int( 4 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
	}
}
