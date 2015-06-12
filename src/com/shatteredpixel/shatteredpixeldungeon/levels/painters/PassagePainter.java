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
package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import java.util.ArrayList;
import java.util.Collections;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room;
import com.watabou.utils.Point;

public class PassagePainter extends Painter {

	private static int pasWidth;
	private static int pasHeight;
	
	public static void paint( Level level, Room room ) {
		
		pasWidth = room.width() - 2;
		pasHeight = room.height() - 2;
		
		int floor = level.tunnelTile();
		
		ArrayList<Integer> joints = new ArrayList<Integer>();
		for (Point door : room.connected.values()) {
			joints.add( xy2p( room, door ) );
		}
		Collections.sort( joints );
		
		int nJoints = joints.size();
		int perimeter = pasWidth * 2 + pasHeight * 2;
		
		int start = 0;
		int maxD = joints.get( 0 ) + perimeter - joints.get( nJoints - 1 );
		for (int i=1; i < nJoints; i++) {
			int d = joints.get( i ) - joints.get( i - 1 );
			if (d > maxD) {
				maxD = d;
				start = i;
			}
		}
		
		int end = (start + nJoints - 1) % nJoints;
		
		int p = joints.get( start );
		do {
			set( level, p2xy( room, p ), floor );
			p = (p + 1) % perimeter;
		} while (p != joints.get( end ));
		
		set( level, p2xy( room, p ), floor );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.TUNNEL );
		}
	}
	
	private static int xy2p( Room room, Point xy ) {
		if (xy.y == room.top) {
			
			return (xy.x - room.left - 1);
			
		} else if (xy.x == room.right) {
			
			return (xy.y - room.top - 1) + pasWidth;
			
		} else if (xy.y == room.bottom) {
			
			return (room.right - xy.x - 1) + pasWidth + pasHeight;
			
		} else {
			
			if (xy.y == room.top + 1) {
				return 0;
			} else {
				return (room.bottom - xy.y - 1) + pasWidth * 2 + pasHeight;
			}
			
		}
	}
	
	private static Point p2xy( Room room, int p ) {
		if (p < pasWidth) {
			
			return new Point( room.left + 1 + p, room.top + 1);
			
		} else if (p < pasWidth + pasHeight) {
			
			return new Point( room.right - 1, room.top + 1 + (p - pasWidth) );
			
		} else if (p < pasWidth * 2 + pasHeight) {
			
			return new Point( room.right - 1 - (p - (pasWidth + pasHeight)), room.bottom - 1 );
			
		} else {
			
			return new Point( room.left + 1, room.bottom - 1 - (p - (pasWidth * 2 + pasHeight)) );
			
		}
	}
}
