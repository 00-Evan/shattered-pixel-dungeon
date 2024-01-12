/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

//A simple builder which utilizes a line as its core feature.
public class LineBuilder extends RegularBuilder {

	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {
	
		setupRooms(rooms);
		
		if (entrance == null){
			return null;
		}

		float direction = Random.Float(0, 360);
		ArrayList<Room> branchable = new ArrayList<>();
		
		entrance.setSize();
		entrance.setPos(0, 0);

		mainPathRooms.add(0, entrance);
		if (exit != null) mainPathRooms.add(exit);

		if (shop != null){
			placeRoom(rooms, entrance, shop, direction + 180f);
		}
		
		Room prev = entrance;

		float[] pathTunnels = pathTunnelChances.clone();
		for (int i = 1; i < mainPathRooms.size(); i++){
			Room r = mainPathRooms.get(i);

			int tunnels = Random.chances(pathTunnels);
			if (tunnels == -1){
				pathTunnels = pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}
			pathTunnels[tunnels]--;

			for (int j = 0; j < tunnels; j++){
				ConnectionRoom t = ConnectionRoom.createRoom();
				placeRoom(rooms, prev, t, direction + Random.Float(-pathVariance, pathVariance));
				branchable.add(t);
				rooms.add(t);
				prev = t;
			}

			placeRoom(rooms, prev, r, direction + Random.Float(-pathVariance, pathVariance));
			branchable.add(r);
			prev = r;
		}
		
		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(multiConnections);
		roomsToBranch.addAll(singleConnections);
		weightRooms(branchable);
		if (!createBranches(rooms, branchable, roomsToBranch, branchTunnelChances)){
			return null;
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
