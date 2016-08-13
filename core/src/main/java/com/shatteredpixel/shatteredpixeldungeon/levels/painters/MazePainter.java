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
package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.watabou.utils.Random;

public class MazePainter extends Painter {

	public static void paint( Level level, Room room ) {
		fill(level, room, 1, Terrain.EMPTY);

		//true = space, false = wall
		boolean[][] maze = new boolean[room.width() + 1][room.height() + 1];

		for (int x = 0; x < maze.length; x++) {
			for (int y = 0; y < maze[0].length; y++) {
				if (x == 0 || x == maze.length - 1 ||
						y == 0 || y == maze[0].length - 1) {

					//set spaces where there are doors
					for (Room.Door d : room.connected.values()) {
						if (d.x == x + room.left && d.y == y + room.top) {
							maze[x][y] = true;
							break;
						}
					}

				} else {
					maze[x][y] = true;
				}
			}
		}

		int fails = 0;
		while (fails < 10000) {

			//find a random wall point
			int x, y;
			do {
				x = Random.Int(maze.length);
				y = Random.Int(maze[0].length);
			} while (maze[x][y]);

			//decide on how we're going to move
			int[] mov = decideDirection(maze, x, y);
			if (mov == null) {
				fails++;
			} else {

				fails = 0;
				int moves = 0;
				do {
					x += mov[0];
					y += mov[1];
					maze[x][y] = false;
					moves++;
				} while (Random.Int(moves+1) == 0 && checkValidMove(maze, x, y, mov));

			}

		}

		fill(level, room, 1, Terrain.EMPTY);
		for (int x = 0; x < maze.length; x++)
			for (int y = 0; y < maze[0].length; y++) {
				if (!maze[x][y]) {
					fill(level, x + room.left, y + room.top, 1, 1, Terrain.WALL);
				}
			}
	}

	private static int[] decideDirection(boolean[][] maze, int x, int y){

		//attempts to move up
		if (Random.Int(4) == 0 && //1 in 4 chance
				checkValidMove(maze, x, y, new int[]{0, -1})){
			return new int[]{0, -1};
		}

		//attempts to move right
		if (Random.Int(3) == 0 && //1 in 3 chance
				checkValidMove(maze, x, y, new int[]{1, 0})){
			return new int[]{1, 0};
		}

		//attempts to move down
		if (Random.Int(2) == 0 && //1 in 2 chance
				checkValidMove(maze, x, y, new int[]{0, 1})){
			return new int[]{0, 1};
		}

		//attempts to move left
		if (
				checkValidMove(maze, x, y, new int[]{-1, 0})){
			return new int[]{-1, 0};
		}

		return null;
	}

	private static boolean checkValidMove( boolean[][] maze, int x, int y, int[] mov){
		int sideX = 1 - Math.abs(mov[0]);
		int sideY = 1 - Math.abs(mov[1]);

		//checking two tiles forward in the movement, and the tiles to their left/right
		for (int i = 0; i < 2; i ++) {
			x += mov[0];
			y += mov[1];
			//checks if tiles we're examining are valid and open
			if ( x > 0 && x < maze.length-1 && y > 0 && y < maze[0].length-1 &&
					maze[x][y] && maze[x + sideX][y+ sideY] && maze[x - sideX][y - sideY]){
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
}
