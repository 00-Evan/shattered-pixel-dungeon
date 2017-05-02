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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

//A builder with one core loop as its primary element
public class LoopBuilder extends RegularBuilder {
	
	//TODO customizeable equation for angle changes (currently we're just linear)
	
	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {
		
		setupRooms(rooms);
		
		if (entrance == null){
			return null;
		}
		
		entrance.setSize();
		entrance.setPos(0, 0);
		
		float startAngle = Random.Float(0, 360);
		
		ArrayList<Room> loop = new ArrayList<>();
		int roomsOnLoop = (int)(multiConnections.size()*pathLength) + Random.chances(pathLenJitterChances);
		roomsOnLoop = Math.min(roomsOnLoop, multiConnections.size());
		if (exit != null) roomsOnLoop++;
		roomsOnLoop++;
		
		for (int i = 0; i < roomsOnLoop; i++){
			if (i == 0)
				loop.add(entrance);
			else if (exit != null && i == roomsOnLoop/2)
				loop.add(exit);
			else
				loop.add(multiConnections.remove(0));
			
			int tunnels = Random.chances(pathTunnelChances);
			for (int j = 0; j < tunnels; j++){
				loop.add(ConnectionRoom.createRoom());
			}
		}
		
		Room prev = entrance;
		float targetAngle = startAngle;
		float angleChange = 360f / loop.size();
		for (int i = 1; i < loop.size(); i++){
			Room r = loop.get(i);
			targetAngle += angleChange;
			if (placeRoom(rooms, prev, r, targetAngle) != -1) {
				prev = r;
				if (!rooms.contains(prev))
					rooms.add(prev);
			} else {
				//FIXME this is lazy, there are ways to do this without relying on chance
				return null;
			}
		}
		
		//FIXME this is lazy, there are ways to do this without relying on chance
		if (!prev.connect(entrance)){
			return null;
		}
		
		ArrayList<Room> branchable = new ArrayList<>(loop);
		
		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(multiConnections);
		roomsToBranch.addAll(singleConnections);
		createBranches(rooms, branchable, roomsToBranch, branchTunnelChances);
		
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
