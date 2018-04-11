/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

import java.util.ArrayList;

//tunnels along the room's perimeter
public class PerimeterRoom extends ConnectionRoom {

	public void paint( Level level ) {
		
		int floor = level.tunnelTile();
		
		ArrayList<Point> pointsToFill = new ArrayList<>();
		for (Point door : connected.values()) {
			Point p = new Point(door);
			if (p.y == top){
				p.y++;
			} else if (p.y == bottom) {
				p.y--;
			} else if (p.x == left){
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
					int dist = distanceBetweenPoints(f, t);
					if (dist < shortestDistance){
						from = f;
						to = t;
						shortestDistance = dist;
					}
				}
			}
			fillBetweenPoints(level, from, to, floor);
			pointsFilled.add(to);
			pointsToFill.remove(to);
		}
		
		for (Door door : connected.values()) {
			door.set( Door.Type.TUNNEL );
		}
	}
	
	private int spaceBetween(int a, int b){
		return Math.abs(a - b)-1;
	}
	
	//gets the path distance between two points
	private int distanceBetweenPoints(Point a, Point b){
		//on the same side
		if (a.y == b.y || a.x == b.x){
			return Math.max(spaceBetween(a.x, b.x), spaceBetween(a.y, b.y));
		}
		
		//otherwise...
		//subtract 1 at the end to account for overlap
		return
				Math.min(spaceBetween(left, a.x) + spaceBetween(left, b.x),
				spaceBetween(right, a.x) + spaceBetween(right, b.x))
				+
				Math.min(spaceBetween(top, a.y) + spaceBetween(top, b.y),
				spaceBetween(bottom, a.y) + spaceBetween(bottom, b.y))
				-
				1;
	}
	
	private Point[] corners;
	
	//picks the smallest path to fill between two points
	private void fillBetweenPoints(Level level, Point from, Point to, int floor){
		
		//doors are along the same side
		if (from.y == to.y || from.x == to.x){
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
			corners[0] = new Point(left+1, top+1);
			corners[1] = new Point(right-1, top+1);
			corners[2] = new Point(right-1, bottom-1);
			corners[3] = new Point(left+1, bottom-1);
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
		if (from.y == top+1 || from.y == bottom-1){
			//connect along the left, or right side
			if (spaceBetween(left, from.x) + spaceBetween(left, to.x) <=
				spaceBetween(right, from.x) + spaceBetween(right, to.x)){
				side = new Point(left+1, top + height()/2);
			} else {
				side = new Point(right-1, top + height()/2);
			}
		
		} else {
			//connect along the top, or bottom side
			if (spaceBetween(top, from.y) + spaceBetween(top, to.y) <=
				spaceBetween(bottom, from.y) + spaceBetween(bottom, to.y)){
				side = new Point(left + width()/2, top+1);
			} else {
				side = new Point(left + width()/2, bottom-1);
			}
		}
		//treat this as two connections with adjacent sides
		fillBetweenPoints(level, from, side, floor);
		fillBetweenPoints(level, side, to, floor);
	}
}
