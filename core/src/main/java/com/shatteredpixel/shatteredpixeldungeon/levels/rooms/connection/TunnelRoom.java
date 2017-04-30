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
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

//tunnels along the rooms center, with straight lines
public class TunnelRoom extends ConnectionRoom {
	
	public void paint(Level level) {
		
		int floor = level.tunnelTile();
		
		Point c = center();
		
		if (width() > height() || (width() == height() && Random.Int( 2 ) == 0)) {
			
			int from = right - 1;
			int to = left + 1;
			
			for (Door door : connected.values()) {
				
				int step = door.y < c.y ? +1 : -1;
				
				if (door.x == left) {
					
					from = left + 1;
					for (int i=door.y; i != c.y; i += step) {
						Painter.set( level, from, i, floor );
					}
					
				} else if (door.x == right) {
					
					to = right - 1;
					for (int i=door.y; i != c.y; i += step) {
						Painter.set( level, to, i, floor );
					}
					
				} else {
					if (door.x < from) {
						from = door.x;
					}
					if (door.x > to) {
						to = door.x;
					}
					
					for (int i=door.y+step; i != c.y; i += step) {
						Painter.set( level, door.x, i, floor );
					}
				}
			}
			
			for (int i=from; i <= to; i++) {
				Painter.set( level, i, c.y, floor );
			}
			
		} else {
			
			int from = bottom - 1;
			int to = top + 1;
			
			for (Door door : connected.values()) {
				
				int step = door.x < c.x ? +1 : -1;
				
				if (door.y == top) {
					
					from = top + 1;
					for (int i=door.x; i != c.x; i += step) {
						Painter.set( level, i, from, floor );
					}
					
				} else if (door.y == bottom) {
					
					to = bottom - 1;
					for (int i=door.x; i != c.x; i += step) {
						Painter.set( level, i, to, floor );
					}
					
				} else {
					if (door.y < from) {
						from = door.y;
					}
					if (door.y > to) {
						to = door.y;
					}
					
					for (int i=door.x+step; i != c.x; i += step) {
						Painter.set( level, i, door.y, floor );
					}
				}
			}
			
			for (int i=from; i <= to; i++) {
				Painter.set( level, c.x, i, floor );
			}
		}
		
		for (Door door : connected.values()) {
			door.set( Door.Type.TUNNEL );
		}
	}
}
