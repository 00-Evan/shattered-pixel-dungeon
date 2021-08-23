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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;

import java.util.ArrayList;

//tunnels along the room's perimeter
public class PerimeterRoom extends ConnectionRoom {

	public void paint( Level level ) {
		
		int floor = level.tunnelTile();
		
		fillPerimiterPaths(level, this, floor);
		
		for (Door door : connected.values()) {
			door.set( Door.Type.TUNNEL );
		}
	}
	
	public static void fillPerimiterPaths( Level l, Room r, int floor ){
		
		corners = null;
		
		ArrayList<Point> pointsToFill = new ArrayList<>();
		for (Point door : r.connected.values()) {
			Point p = new Point(door);
			if (p.y == r.top){
				p.y++;
			} else if (p.y == r.bottom) {
				p.y--;
			} else if (p.x == r.left){
				p.x++;
			} else {
				p.x--;
			}
			pointsToFill.add( p );
		}
		
		ArrayList<Point> pointsFilled = new ArrayList<>();
		pointsFilled.add(pointsToFill.remove(0));
		
		Point from = null, to = null;
		int shortestDistance;
		while(!pointsToFill.isEmpty()){
			shortestDistance = Integer.MAX_VALUE;
			for (Point f : pointsFilled){
				for (Point t : pointsToFill){
					int dist = distanceBetweenPoints(r, f, t);
					if (dist < shortestDistance){
						from = f;
						to = t;
						shortestDistance = dist;
					}
				}
			}
			fillBetweenPoints(l, r, from, to, floor);
			pointsFilled.add(to);
			pointsToFill.remove(to);
		}
		
	}
	
	private static int spaceBetween(int a, int b){
		return Math.abs(a - b)-1;
	}
	
	//gets the path distance between two points
	private static int distanceBetweenPoints(Room r, Point a, Point b){
		//on the same side
		if (((a.x == r.left || a.x == r.right) && a.y == b.y)
				|| ((a.y == r.top || a.y == r.bottom) && a.x == b.x)){
			return Math.max(spaceBetween(a.x, b.x), spaceBetween(a.y, b.y));
		}
		
		//otherwise...
		//subtract 1 at the end to account for overlap
		return
				Math.min(spaceBetween(r.left, a.x) + spaceBetween(r.left, b.x),
				spaceBetween(r.right, a.x) + spaceBetween(r.right, b.x))
				+
				Math.min(spaceBetween(r.top, a.y) + spaceBetween(r.top, b.y),
				spaceBetween(r.bottom, a.y) + spaceBetween(r.bottom, b.y))
				-
				1;
	}
	
	private static Point[] corners;
	
	//picks the smallest path to fill between two points
	private static void fillBetweenPoints(Level level, Room r, Point from, Point to, int floor){
		
		//doors are along the same side
		if (((from.x == r.left+1 || from.x == r.right-1) && from.x == to.x)
				|| ((from.y == r.top+1 || from.y == r.bottom-1) && from.y == to.y)){
			Painter.fill(level,
					Math.min(from.x, to.x),
					Math.min(from.y, to.y),
					spaceBetween(from.x, to.x) + 2,
					spaceBetween(from.y, to.y) + 2,
					floor);
			return;
		}
		
		//set up corners
		if (corners == null){
			corners = new Point[4];
			corners[0] = new Point(r.left+1, r.top+1);
			corners[1] = new Point(r.right-1, r.top+1);
			corners[2] = new Point(r.right-1, r.bottom-1);
			corners[3] = new Point(r.left+1, r.bottom-1);
		}
		
		//doors on adjacent sides
		for (Point c : corners){
			if ((c.x == from.x || c.y == from.y) && (c.x == to.x || c.y == to.y)){
				Painter.drawLine(level, from, c, floor);
				Painter.drawLine(level, c, to, floor);
				return;
			}
		}
		
		//doors on opposite sides
		Point side;
		if (from.y == r.top+1 || from.y == r.bottom-1){
			//connect along the left, or right side
			if (spaceBetween(r.left, from.x) + spaceBetween(r.left, to.x) <=
				spaceBetween(r.right, from.x) + spaceBetween(r.right, to.x)){
				side = new Point(r.left+1, r.top + r.height()/2);
			} else {
				side = new Point(r.right-1, r.top + r.height()/2);
			}
		
		} else {
			//connect along the top, or bottom side
			if (spaceBetween(r.top, from.y) + spaceBetween(r.top, to.y) <=
				spaceBetween(r.bottom, from.y) + spaceBetween(r.bottom, to.y)){
				side = new Point(r.left + r.width()/2, r.top+1);
			} else {
				side = new Point(r.left + r.width()/2, r.bottom-1);
			}
		}
		//treat this as two connections with adjacent sides
		fillBetweenPoints(level, r, from, side, floor);
		fillBetweenPoints(level, r, side, to, floor);
	}
}
