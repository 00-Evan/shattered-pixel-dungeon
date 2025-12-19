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

		ArrayList<Room> placed = new ArrayList<>();
		placed.add(entrance);

		//use a sparse array to track room positions, with a mapping of x + 1000*y = cell
		//this assumes that levels won't more than 1000 rooms wide
		SparseArray<Room> gridCells = new SparseArray<>();
		gridCells.put(0, entrance);

		for (Room r : toPlace){
			if (!r.forceSize(ROOM_SIZE, ROOM_SIZE)){
				throw new RuntimeException("rigid room sizes for now!");
			}
			do {
				Room n = Random.element(placed);
				int nIdx = gridCells.findKey(n, true, Integer.MIN_VALUE);
				int rIdx = nIdx;
				switch (Random.Int(4)){
					case 0:
						rIdx += 1;
						break;
					case 1:
						rIdx += 1000;
						break;
					case 2:
						rIdx -= 1;
						break;
					case 3:
						rIdx -= 1000;
						break;
				}
				//TODO negatives
				int x = rIdx%1000;
				int y = rIdx/1000;
				r.setPos(x*(ROOM_SIZE-1), y*(ROOM_SIZE-1));
				//TODO want to manually limit size probably
				if (x >= 0 && y >= 0 && !gridCells.containsKey(rIdx)){
					if (r.connect(n)) {
						placed.add(r);
						gridCells.put(rIdx, r);
					}
				}
			} while (!placed.contains(r));
		}

		//need a buffer room?
		//contains an internal room, fills with

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
