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
import com.watabou.utils.Point;
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
	
	protected Rect findFreeSpace(Point start, ArrayList<Room> rooms, int maxSize){
		Rect space = new Rect(start.x-maxSize, start.y-maxSize, start.x+maxSize, start.y+maxSize);
		
		ArrayList<Room> colliding = new ArrayList<>(rooms);
		do{
			
			//remove any rooms we aren't currently overlapping
			Iterator<Room> it = colliding.iterator();
			while (it.hasNext()){
				Room room = it.next();
				//if not colliding
				if ( Math.max(space.left, room.left) >= Math.min(space.right, room.right)
					|| Math.max(space.top, room.top) >= Math.min(space.bottom, room.bottom) ){
					it.remove();
				}
			}
			
			//iterate through all rooms we are overlapping, and find which one would take
			//the largest area reduction to resolve the overlapping
			Room biggestCollision = null;
			int wDiff, hDiff, biggestDiff = 0;
			boolean widthCollision = false;
			for (Room room : colliding){
				wDiff = Integer.MAX_VALUE;
				if (room.left >= start.x){
					wDiff = (space.right - room.left) * (space.height() + 1);
				} else if (room.right <= start.x){
					wDiff = (room.right - space.left) * (space.height() + 1);
				}
				
				hDiff = Integer.MAX_VALUE;
				if (room.top >= start.y){
					hDiff = (space.bottom - room.top) * (space.width() + 1);
				} else if (room.bottom <= start.y){
					hDiff = (room.bottom - space.top) * (space.width() + 1);
				}
				
				//our start is inside this room, return an empty rect
				if (hDiff == Integer.MAX_VALUE && wDiff == Integer.MAX_VALUE){
					space.set(0, 0, 0, 0);
					return space;
					
				} else {
					if (wDiff < hDiff || (wDiff == hDiff && Random.Int(2) == 0)){
						if (wDiff >= biggestDiff){
							biggestDiff = wDiff;
							biggestCollision = room;
							widthCollision = true;
						}
						
					} else {
						if (hDiff >= biggestDiff){
							biggestDiff = hDiff;
							biggestCollision = room;
							widthCollision = false;
						}
					}
					
				}
				
			}
			
			//reduce the available space in order to not overlap with the biggest collision we found
			if (biggestCollision != null){
				if (widthCollision){
					if (biggestCollision.left >= start.x && biggestCollision.left < space.right) space.right = biggestCollision.left;
					if (biggestCollision.right <= start.x && biggestCollision.right > space.left) space.left = biggestCollision.right;
				} else {
					if (biggestCollision.top >= start.y && biggestCollision.top < space.bottom) space.bottom = biggestCollision.top;
					if (biggestCollision.bottom <= start.y && biggestCollision.bottom > space.top) space.top = biggestCollision.bottom;
				}
				colliding.remove(biggestCollision);
			} else {
				colliding.clear();
			}
			
			//loop until we are no longer colliding with any rooms
		} while (!colliding.isEmpty());
		
		return space;
	}
}
