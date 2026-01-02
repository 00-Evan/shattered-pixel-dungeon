/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;

//a builder with static room sizes aligned to a grid
//TODO extend regular builder?
public class GridBuilder extends Builder {

	public static int ROOM_SIZE = 9;

	//each adjacency is processed twice, so this gives a ~50% chance to connect two adjacent rooms
	protected float extraConnectionChance = 0.30f;

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

		ArrayList<Room> toPlace = new ArrayList<>(rooms);
		toPlace.remove(entrance);

		//move the exit to the back
		if (exit != null) {
			toPlace.remove(exit);
			toPlace.add(exit);
		}

		ArrayList<Room> placed = new ArrayList<>();
		placed.add(entrance);

		//use a sparse array to track room positions, with a mapping of x + 1000*y = cell
		// and an index offset of 100,100 (x=y=100) to ensure we aren't dealing with negative indexes
		//this effectively puts a limit of -99 < x < 999 and -99 < y < inf. on level sizes in rooms
		SparseArray<Room> gridCells = new SparseArray<>();
		gridCells.put(100_100, entrance);
		for (Room r : toPlace){
			int cellWidth = 1;
			int cellHeight = 1;
			//TODO this works on rigid multiples atm, would be nicer to buffer rooms that don't quite work
			if (!r.forceSize(ROOM_SIZE, ROOM_SIZE)){
				if (!r.forceSize(2*ROOM_SIZE-1, 2*ROOM_SIZE-1)) {
					throw new RuntimeException("rigid room sizes for now!");
				}
				cellWidth = cellHeight = 2;
			}
			do {
				r.neigbours.clear();
				int[] keys = gridCells.keyArray();
				int nIdx = keys[Random.Int(keys.length)];
				Room n =  gridCells.get(nIdx, null);
				int rIdx = nIdx;
				//currently always pulls down and to the right
				switch (Random.Int(10)){
					case 0: case 4: case 5: case 6:
						rIdx += 1;
						break;
					case 1: case 7: case 8: case 9:
						rIdx += 1000;
						break;
					case 2:
						rIdx -= 1;
						break;
					case 3:
						rIdx -= 1000;
						break;
				}
				//-100 to cancel offsets
				int x = (rIdx % 1000) - 100;
				int y = (rIdx / 1000) - 100;
				r.setPos(x*(ROOM_SIZE-1), y*(ROOM_SIZE-1));
				boolean valid = true;
				for (int i = 0; i < cellWidth; i++){
					for (int j = 0; j < cellHeight; j++){
						if (gridCells.containsKey(rIdx + i + j*1000)){
							valid = false;
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

}
