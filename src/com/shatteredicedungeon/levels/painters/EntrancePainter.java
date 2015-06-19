/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredicedungeon.levels.painters;

import com.shatteredicedungeon.Dungeon;
import com.shatteredicedungeon.levels.Level;
import com.shatteredicedungeon.levels.Room;
import com.shatteredicedungeon.levels.Terrain;

public class EntrancePainter extends Painter {

	public static void paint(Level level, Room room) {

		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY);

		for (Room.Door door : room.connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}
		//
		// level.entrance = room.random( 1 );
		// set( level, level.entrance, Terrain.ENTRANCE );
		//
		if (Dungeon.depth <= 5) {
			level.entrance = room.top * Level.WIDTH + Level.WIDTH
					+ (room.right - 1);
			set(level, level.entrance, Terrain.ENTRANCE);
		} else {
			level.entrance = room.random(1);
			set(level, level.entrance, Terrain.ENTRANCE);
		}
	}

}
