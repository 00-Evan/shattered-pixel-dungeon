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

package com.shatteredpixel.shatteredpixeldungeon.levels.builders;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Builder {
	
	//If builders require additional parameters, they should
	// request them in their constructor or other methods
	
	//builders take a list of rooms and returns them as a connected map
	//returns null on failure
	public abstract ArrayList<Room> build(ArrayList<Room> rooms);
	
	protected static void findNeighbours(ArrayList<Room> rooms){
		Room[] ra = rooms.toArray( new Room[0] );
		for (int i=0; i < ra.length-1; i++) {
			for (int j=i+1; j < ra.length; j++) {
				ra[i].addNeigbour( ra[j] );
			}
		}
	}

	//returns a rectangle representing the maximum amount of free space from a specific start point
	protected static Rect findFreeSpace(Point start, ArrayList<Room> collision, int maxSize){
		Rect space = new Rect(start.x-maxSize, start.y-maxSize, start.x+maxSize, start.y+maxSize);

		//shallow copy
		ArrayList<Room> colliding = new ArrayList<>(collision);
		do{
			
			//remove empty rooms and any rooms we aren't currently overlapping
			Iterator<Room> it = colliding.iterator();
			while (it.hasNext()){
				Room room = it.next();
				//if not colliding
				if ( room.isEmpty()
						|| Math.max(space.left, room.left) >= Math.min(space.right, room.right)
						|| Math.max(space.top, room.top) >= Math.min(space.bottom, room.bottom) ){
					it.remove();
				}
			}
			
			//iterate through all rooms we are overlapping, and find the closest one
			Room closestRoom = null;
			int closestDiff = Integer.MAX_VALUE;
			boolean inside = true;
			int curDiff = 0;
			for (Room curRoom : colliding){
				
				if (start.x <= curRoom.left){
					inside = false;
					curDiff += curRoom.left - start.x;
				} else if (start.x >= curRoom.right){
					inside = false;
					curDiff += start.x - curRoom.right;
				}
			
				if (start.y <= curRoom.top){
					inside = false;
					curDiff += curRoom.top - start.y;
				} else if (start.y >= curRoom.bottom){
					inside = false;
					curDiff += start.y - curRoom.bottom;
				}
				
				if (inside){
					space.set(start.x, start.y, start.x, start.y);
					return space;
				}
				
				if (curDiff < closestDiff){
					closestDiff = curDiff;
					closestRoom = curRoom;
				}
			
			}
			
			int wDiff, hDiff;
			if (closestRoom != null){
				
				wDiff = Integer.MAX_VALUE;
				if (closestRoom.left >= start.x){
					wDiff = (space.right - closestRoom.left) * (space.height() + 1);
				} else if (closestRoom.right <= start.x){
					wDiff = (closestRoom.right - space.left) * (space.height() + 1);
				}
				
				hDiff = Integer.MAX_VALUE;
				if (closestRoom.top >= start.y){
					hDiff = (space.bottom - closestRoom.top) * (space.width() + 1);
				} else if (closestRoom.bottom <= start.y){
					hDiff = (closestRoom.bottom - space.top) * (space.width() + 1);
				}
				
				//reduce by as little as possible to resolve the collision
				if (wDiff < hDiff || wDiff == hDiff && Random.Int(2) == 0){
					if (closestRoom.left >= start.x && closestRoom.left < space.right) space.right = closestRoom.left;
					if (closestRoom.right <= start.x && closestRoom.right > space.left) space.left = closestRoom.right;
				} else {
					if (closestRoom.top >= start.y && closestRoom.top < space.bottom) space.bottom = closestRoom.top;
					if (closestRoom.bottom <= start.y && closestRoom.bottom > space.top) space.top = closestRoom.bottom;
				}
				colliding.remove(closestRoom);
			} else {
				colliding.clear();
			}
			
			//loop until we are no longer colliding with any rooms
		} while (!colliding.isEmpty());
		
		return space;
	}

	private static final double A = 180 / Math.PI;
	
	//returns the angle in degrees made by the centerpoints of 2 rooms, with 0 being straight up.
	protected static float angleBetweenRooms( Room from, Room to){
		PointF fromCenter = new PointF((from.left + from.right)/2f, (from.top + from.bottom)/2f);
		PointF toCenter = new PointF((to.left + to.right)/2f, (to.top + to.bottom)/2f);
		return angleBetweenPoints(fromCenter, toCenter);
	}
	
	protected static float angleBetweenPoints( PointF from, PointF to ){
		double m = (to.y - from.y)/(to.x - from.x);
		
		float angle = (float)(A*(Math.atan(m) + Math.PI/2.0));
		if (from.x > to.x) angle -= 180f;
		return angle;
	}

	//Attempts to place a room such that the angle between the center of the previous room
	// and it matches the given angle ([0-360), where 0 is straight up) as closely as possible.
	//Note that getting an exactly correct angle is harder the closer that angle is to diagonal.
	//Returns the exact angle between the centerpoints of the two rooms, or -1 if placement fails.
	protected static float placeRoom( ArrayList<Room> collision, Room prev, Room next, float angle){

		//wrap angle around to always be [0-360)
		angle %= 360f;
		if (angle < 0){
			angle += 360f;
		}

		PointF prevCenter = new PointF((prev.left + prev.right)/2f, (prev.top + prev.bottom)/2f);

		// calculating using y = mx+b, straight line formula
		double m = Math.tan(angle/A + Math.PI/2.0);
		double b = prevCenter.y -m*prevCenter.x;

		//using the line equation, we find the point along the prev room where the line exists
		Point start;
		int direction;
		if (Math.abs(m) >= 1){
			if (angle < 90 || angle > 270){
				direction = Room.TOP;
				start = new Point( (int)Math.round((prev.top - b)/m), prev.top);
			} else {
				direction = Room.BOTTOM;
				start = new Point( (int)Math.round((prev.bottom - b)/m), prev.bottom);
			}
		} else {
			if (angle < 180){
				direction = Room.RIGHT;
				start = new Point(prev.right, (int) Math.round(m * prev.right + b));
			} else {
				direction = Room.LEFT;
				start = new Point(prev.left, (int) Math.round(m * prev.left + b));
			}
		}

		//cap it to a valid connection point for most rooms
		if (direction == Room.TOP || direction == Room.BOTTOM) {
			start.x = (int) GameMath.gate(prev.left + 1, start.x, prev.right - 1);
		} else {
			start.y = (int) GameMath.gate(prev.top + 1, start.y, prev.bottom - 1);
		}

		//space checking
		Rect space = findFreeSpace(start, collision, Math.max(next.maxWidth(), next.maxHeight()));
		if (!next.setSizeWithLimit(space.width()+1, space.height()+1)){
			return -1;
		}

		//find the ideal center for this new room using the line equation and known dimensions
		PointF targetCenter = new PointF();
		if (direction == Room.TOP) {
			targetCenter.y = prev.top - (next.height() - 1) / 2f;
			targetCenter.x = (float) ((targetCenter.y - b) / m);
			next.setPos(Math.round(targetCenter.x - (next.width() - 1) / 2f), prev.top - (next.height() - 1));

		} else if (direction == Room.BOTTOM) {
			targetCenter.y = prev.bottom + (next.height() - 1) / 2f;
			targetCenter.x = (float) ((targetCenter.y - b) / m);
			next.setPos(Math.round(targetCenter.x - (next.width() - 1) / 2f), prev.bottom);

		} else if (direction == Room.RIGHT) {
			targetCenter.x = prev.right + (next.width()-1)/2f;
			targetCenter.y = (float)(m*targetCenter.x + b);
			next.setPos( prev.right, Math.round(targetCenter.y - (next.height()-1)/2f));

		} else if (direction == Room.LEFT) {
			targetCenter.x = prev.left - (next.width()-1)/2f;
			targetCenter.y = (float)(m*targetCenter.x + b);
			next.setPos( prev.left - (next.width() - 1), Math.round(targetCenter.y - (next.height()-1)/2f));

		}

		//perform connection bounds and target checking, move the room if necessary
		if (direction == Room.TOP || direction == Room.BOTTOM){
			if (next.right < prev.left+2)	    next.shift(prev.left+2-next.right, 0);
			else if (next.left > prev.right-2)  next.shift(prev.right-2-next.left, 0);

			if (next.right > space.right)       next.shift( space.right - next.right, 0);
			else if (next.left < space.left)    next.shift( space.left - next.left, 0);
		} else {
			if (next.bottom < prev.top+2)	    next.shift(0, prev.top+2-next.bottom);
			else if (next.top > prev.bottom-2)  next.shift(0, prev.bottom-2-next.top);

			if (next.bottom > space.bottom)     next.shift( 0, space.bottom - next.bottom);
			else if (next.top < space.top)      next.shift( 0, space.top - next.top);
		}

		//attempt to connect, return the result angle if successful.
		if (next.connect(prev)){
			return angleBetweenRooms(prev, next);
		} else {
			return -1;
		}
	}
}
