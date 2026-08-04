/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

//a builder with static room sizes aligned to a grid
//placement of rooms is largely random, but with size limits to ensure compactness
public class GridBuilder extends Builder {

	//TODO this shouldn't be static, could be a parameter
	public static int ROOM_SIZE = 11;

	//each adjacency is processed twice, so this gives a ~80% chance to connect two adjacent rooms
	protected float extraConnectionChance = 0.55f;

	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {
		for(Room r : rooms){
			r.setEmpty();
		}

		Room entrance = null;
		Room exit = null;

		for (Room r : rooms){
			if (r.isEntrance()){
				entrance = r;
			} else if (r.isExit()){
				exit = r;
			}
		}

		if (!entrance.forceSize(ROOM_SIZE, ROOM_SIZE)){
			throw new RuntimeException("rigid room sizes for now!");
		}
		entrance.setPos(0, 0);

		ArrayList<Room> toPlace = new ArrayList<>();

		//we try to interleave things so we get though a few normal rooms for every special room
		ArrayList<Room> multis = new ArrayList<>();
		ArrayList<Room> singles = new ArrayList<>();
		for (Room r : rooms){
			if (r.maxConnections(Room.ALL) == 1){
				singles.add(r);
			} else {
				multis.add(r);
			}
		}

		//we set a boundary based on the total number of rooms
		// (atm this assumes all rooms are 1x1, but very large rooms are allowed to
		// 'poke' out of the boundary and also are handled by the extra multiplier)
		int maxWidth = 0, maxHeight = 0;
		while ((maxWidth * maxHeight) < rooms.size()*1.25f){
			if (maxWidth < maxHeight || (maxWidth == maxHeight && Random.Int(2) == 0)){
				maxWidth++;
			} else {
				maxHeight++;
			}
		}

		ArrayList<Room> placed = new ArrayList<>();

		if (!multis.isEmpty()) toPlace.add(multis.remove(0));
		if (!multis.isEmpty()) toPlace.add(multis.remove(0));
		while (!multis.isEmpty() || !singles.isEmpty()){
			if (!multis.isEmpty()) toPlace.add(multis.remove(0));
			if (!multis.isEmpty()) toPlace.add(multis.remove(0));
			if (!multis.isEmpty()) toPlace.add(multis.remove(0));
			if (!singles.isEmpty()) toPlace.add(singles.remove(0));
		}

		toPlace.remove(entrance);
		int entryX;
		int entryY;
		//always place entrance along the perimeter
		//yes this intentionally makes corners a bit more likely)
		switch (Random.Int(4)){
			case 0: default:
				entryX = 0;
				entryY = Random.Int(0, maxHeight);
				break;
			case 1:
				entryX = Random.Int(0, maxWidth);
				entryY = 0;
				break;
			case 2:
				entryX = maxWidth-1;
				entryY = Random.Int(0, maxHeight);
				break;
			case 3:
				entryX = Random.Int(0, maxWidth);
				entryY = maxHeight-1;
				break;
		}
		entrance.setPos(entryX*(ROOM_SIZE-1), entryY*(ROOM_SIZE-1));
		placed.add(entrance);

		//move the exit to the back
		if (exit != null) {
			toPlace.remove(exit);
			toPlace.add(exit);
		}

		PointF aimCenter = new PointF(maxWidth/2f, maxHeight/2f);

		//use a sparse array to track room positions, with a mapping of x + 1000*y = cell
		// and an index offset of 100,100 (x=y=100) to ensure we aren't dealing with negative indexes
		//this effectively puts a limit of -99 < x < 999 and -99 < y < inf. on level sizes in rooms
		SparseArray<Room> gridCells = new SparseArray<>();
		gridCells.put(getIdx(entryX, entryY), entrance);
		int roomPlacementFailures = 0;
		while (!toPlace.isEmpty()) {
			Room r = toPlace.remove(0);
			int cellWidth = 1;
			int cellHeight = 1;
			//TODO this works on rigid multiples atm, would be nicer to buffer rooms that don't work on that
			if (!r.forceSize(ROOM_SIZE, ROOM_SIZE)){
				//TODO tries larger width first, perhaps randomize that?
				if (!r.forceSize(2*ROOM_SIZE-1, 2*ROOM_SIZE-1)) {
					if (!r.forceSize(ROOM_SIZE, 2*ROOM_SIZE-1)) {
						if (!r.forceSize(2*ROOM_SIZE-1, ROOM_SIZE)) {
							throw new RuntimeException("rigid room sizes for now!");
						} else {
							cellWidth = 2; cellHeight = 1;
						}
					} else {
						cellWidth = 1; cellHeight = 2;
					}
				} else {
					cellWidth = cellHeight = 2;
				}
			}
			int tries = 0;
			do {
				r.neigbours.clear();
				tries++;
				if (tries > 100) {
					//can't place for now, put it back into the list
					toPlace.add(Math.min(2, toPlace.size()), r);

					//if we're consistently failing to place a room, then restart
					roomPlacementFailures++;
					if (roomPlacementFailures > 100){
						return null;
					}
					break;
				}

				Room n;
				int nIdx;
				//first two placed rooms always connect to the entrance.
				if (placed.size() < 3){
					n = entrance;
					nIdx = getIdx(entryX, entryY);
				} else {
					int[] keys = gridCells.keyArray();
					nIdx = keys[Random.Int(keys.length)];
					n = gridCells.get(nIdx, null);
				}
				int rIdx = nIdx;
				float xDiff = aimCenter.x - ((rIdx % 1000) - 100);
				float yDiff = aimCenter.y - ((rIdx / 1000) - 100);
				float dist = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
				//farther out from the center = more likely to pull to center
				// 67-100% chance to pull to center at 0-4 tiles of distance
				//also always go toward center on first room placement
				if (Random.Float(12) < 8+dist || (placed.size() == 1)){
					if (Math.abs(xDiff) >= Math.abs(yDiff)){
						if (xDiff > 0){
							//move right
							rIdx += 1;
						} else {
							//move left
							rIdx -= 1;
						}
					} else {
						if (yDiff > 0){
							//move down
							rIdx += 1000;
						} else {
							//move up
							rIdx -= 1000;
						}
					}
				} else {
					switch (Random.Int(4)) {
						case 0:
							rIdx += 1;
							break;
						case 1:
							rIdx -= 1000;
							break;
						case 2:
							rIdx -= 1;
							break;
						case 3:
							rIdx += 1000;
							break;
					}
				}
				//-100 to cancel offsets
				int x = (rIdx % 1000) - 100;
				int y = (rIdx / 1000) - 100;
				boolean valid;

				//invalid if starting cell is outside max area
				if (x < 0 || x >= maxHeight || y < 0 || y >= maxHeight) {
					valid = false;

				//invalid if starting cell is inside another room
				} else if (gridCells.containsKey(rIdx)) {
					valid = false;

				//more complex check for larger rooms, need to check all cells they occupy
				} else if (cellWidth > 1 || cellHeight > 1) {

					Rect space = findFreeGridSpace(new Point(x, y), gridCells, cellWidth, cellHeight);

					//1x2 or 2x1 rooms have to fit entirely, larger ones are allowed to stick out a bit
					if (cellWidth * cellHeight <= 2) {
						space.left = Math.max(0, space.left);
						space.top = Math.max(0, space.top);
						space.right = Math.min(maxWidth - 1, space.right);
						space.bottom = Math.min(maxHeight - 1, space.bottom);
					}

					//add 1 to width/height as it's inclusive
					int excessWidth = (space.width() + 1) - cellWidth;
					int excessHeight = (space.height() + 1) - cellHeight;
					valid = excessWidth >= 0 && excessHeight >= 0;
					if (valid) {
						//randomly place the room within available space.
						// We could do more with this probably, e.g. preferred DIR
						x = space.left + Random.Int(excessWidth+1);
						y = space.top + Random.Int(excessHeight+1);
						rIdx = getIdx(x, y);
					}
				} else {
					valid = true;
				}

				//two of the same kind of room cannot share a side
				//the more regular layouts of grid rooms makes this look very repetative
				if (valid){
					r.setPos(x*(ROOM_SIZE-1), y*(ROOM_SIZE-1));
					for (Room other : placed){
						if (other.getClass() == r.getClass()){
							Rect i = r.intersect(other);
							if (i.width() > 0 || i.height() > 0){
								valid = false;
							}
						}
					}
				}

				if (valid){
					if (r.connect(n)) {
						placed.add(r);
						for (int i = 0; i < cellWidth; i++){
							for (int j = 0; j < cellHeight; j++){
								gridCells.put(rIdx + i + j*1000, r);
							}
						}
					}
				}
			} while (!placed.contains(r));
		}

		findNeighbours(rooms);

		for (Room r : rooms){
			for (Room n : r.neigbours){
				if (!n.connected.containsKey(r)
						&& Random.Float() < extraConnectionChance){
					r.connect(n);
				}
			}
		}

		return rooms;
	}

	//same as Builder.findFreeSpace, but using grid coordination and collision
	//assumes the starting cell is open
	public Rect findFreeGridSpace(Point start, SparseArray<Room> collision, int maxWidth, int maxHeight){
		Rect space = new Rect(start.x, start.y, start.x, start.y);

		//expand one at a time in each direction, so as to prioritize a more square shape
		boolean expanded = true;
		while (expanded){
			expanded = false;
			//left
			if (space.left > start.x-(maxWidth-1)) {
				boolean valid = true;
				for (int y = space.top; y <= space.bottom; y++) {
					if (collision.containsKey(getIdx(space.left-1, y))) {
						valid = false;
						break;
					}
				}
				if (valid){
					space.left--;
					expanded = true;
				}
			}
			//top
			if (space.top > start.y-(maxHeight-1)){
				boolean valid = true;
				for (int x = space.left; x <= space.right; x++) {
					if (collision.containsKey(getIdx(x, space.top-1))) {
						valid = false;
						break;
					}
				}
				if (valid){
					space.top--;
					expanded = true;
				}
			}
			//right
			if (space.right < start.x+(maxWidth-1)) {
				boolean valid = true;
				for (int y = space.top; y <= space.bottom; y++) {
					if (collision.containsKey(getIdx(space.right+1, y))) {
						valid = false;
						break;
					}
				}
				if (valid){
					space.right++;
					expanded = true;
				}
			}
			//bottom
			if (space.bottom < start.y+(maxHeight-1)){
				boolean valid = true;
				for (int x = space.left; x <= space.right; x++) {
					if (collision.containsKey(getIdx(x, space.bottom+1))) {
						valid = false;
						break;
					}
				}
				if (valid){
					space.bottom++;
					expanded = true;
				}
			}
		}

		return space;
	}

	private int getIdx(Point p){
		return p.x + 1000*p.y + 100;
	}

	private int getIdx(int x, int y){
		return (x+100) + 1000*(y+100);
	}

}
