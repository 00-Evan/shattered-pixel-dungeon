/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Maze;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

public class MazeConnectionRoom extends ConnectionRoom {
	
	@Override
	public void paint(Level level) {
		super.paint(level);
		
		Painter.fill(level, this, 1, Terrain.EMPTY);
		
		//true = space, false = wall
		boolean[][] maze = Maze.generate(this);
		
		Painter.fill(level, this, 1, Terrain.EMPTY);
		for (int x = 0; x < maze.length; x++)
			for (int y = 0; y < maze[0].length; y++) {
				if (maze[x][y] == Maze.FILLED) {
					Painter.fill(level, x + left, y + top, 1, 1, Terrain.WALL);
				}
			}
		
		for (Door door : connected.values()) {
			door.set( Door.Type.TUNNEL );
		}
	}
}
