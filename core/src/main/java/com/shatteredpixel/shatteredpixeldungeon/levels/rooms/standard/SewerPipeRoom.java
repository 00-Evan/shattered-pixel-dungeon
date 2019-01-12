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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class SewerPipeRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{4, 2, 1};
	}

	@Override
	public boolean canConnect(Point p) {
		//refuses connections next to corners
		return super.canConnect(p) && ((p.x > left+1 && p.x < right-1) || (p.y > top+1 && p.y < bottom-1));
	}

	//FIXME this class is a total mess, lots of copy-pasta from tunnel and perimeter rooms here
	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );

		Rect c = getConnectionSpace();

		if (connected.size() <= 2) {
			for (Door door : connected.values()) {
				
				Point start;
				Point mid;
				Point end;
				
				start = new Point(door);
				if (start.x == left) start.x += 2;
				else if (start.y == top) start.y += 2;
				else if (start.x == right) start.x -= 2;
				else if (start.y == bottom) start.y -= 2;
				
				int rightShift;
				int downShift;
				
				if (start.x < c.left) rightShift = c.left - start.x;
				else if (start.x > c.right) rightShift = c.right - start.x;
				else rightShift = 0;
				
				if (start.y < c.top) downShift = c.top - start.y;
				else if (start.y > c.bottom) downShift = c.bottom - start.y;
				else downShift = 0;
				
				//always goes inward first
				if (door.x == left || door.x == right) {
					mid = new Point(start.x + rightShift, start.y);
					end = new Point(mid.x, mid.y + downShift);
					
				} else {
					mid = new Point(start.x, start.y + downShift);
					end = new Point(mid.x + rightShift, mid.y);
					
				}
				
				Painter.drawLine(level, start, mid, Terrain.WATER);
				Painter.drawLine(level, mid, end, Terrain.WATER);
				
			}
		} else {
			ArrayList<Point> pointsToFill = new ArrayList<>();
			for (Point door : connected.values()) {
				Point p = new Point(door);
				if (p.y == top){
					p.y+=2;
				} else if (p.y == bottom) {
					p.y-=2;
				} else if (p.x == left){
					p.x+=2;
				} else {
					p.x-=2;
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
				fillBetweenPoints(level, from, to, Terrain.WATER);
				pointsFilled.add(to);
				pointsToFill.remove(to);
			}
		}

		for(Point p : getPoints()){
			int cell = level.pointToCell(p);
			if (level.map[cell] == Terrain.WATER){
				for (int i : PathFinder.NEIGHBOURS8){
					if (level.map[cell + i] == Terrain.WALL){
						Painter.set(level, cell + i, Terrain.EMPTY);
					}
				}
			}
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	//returns the space which all doors must connect to (usually 1 cell, but can be more)
	//Note that, like rooms, this space is inclusive to its right and bottom sides
	protected Rect getConnectionSpace(){
		Point c = connected.size() <= 1 ? center() : getDoorCenter();

		return new Rect(c.x, c.y, c.x, c.y);
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	//returns a point equidistant from all doors this room has
	protected final Point getDoorCenter(){
		PointF doorCenter = new PointF(0, 0);

		for (Door door : connected.values()) {
			doorCenter.x += door.x;
			doorCenter.y += door.y;
		}

		Point c = new Point((int)doorCenter.x / connected.size(), (int)doorCenter.y / connected.size());
		if (Random.Float() < doorCenter.x % 1) c.x++;
		if (Random.Float() < doorCenter.y % 1) c.y++;
		c.x = (int) GameMath.gate(left+2, c.x, right-2);
		c.y = (int)GameMath.gate(top+2, c.y, bottom-2);

		return c;
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
			corners[0] = new Point(left+2, top+2);
			corners[1] = new Point(right-2, top+2);
			corners[2] = new Point(right-2, bottom-2);
			corners[3] = new Point(left+2, bottom-2);
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
		if (from.y == top+2 || from.y == bottom-2){
			//connect along the left, or right side
			if (spaceBetween(left, from.x) + spaceBetween(left, to.x) <=
					spaceBetween(right, from.x) + spaceBetween(right, to.x)){
				side = new Point(left+2, top + height()/2);
			} else {
				side = new Point(right-2, top + height()/2);
			}
			
		} else {
			//connect along the top, or bottom side
			if (spaceBetween(top, from.y) + spaceBetween(top, to.y) <=
					spaceBetween(bottom, from.y) + spaceBetween(bottom, to.y)){
				side = new Point(left + width()/2, top+2);
			} else {
				side = new Point(left + width()/2, bottom-2);
			}
		}
		//treat this as two connections with adjacent sides
		fillBetweenPoints(level, from, side, floor);
		fillBetweenPoints(level, side, to, floor);
	}

}
