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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.TunnelRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

//A builder with one core loop as its primary element
public class LoopBuilder extends Builder {
	
	//TODO customizeable equation for angle changes (currently we're just linear)
	
	//path length is the percentage of pathable rooms that are on the loop
	private float pathLength = 0.1f;
	//The chance weights for extra rooms to be added to the path
	private float[] pathLenJitterChances = new float[]{0, 2, 1};
	
	public LoopBuilder setPathLength( float len, float[] jitter ){
		pathLength = len;
		pathLenJitterChances = jitter;
		return this;
	}
	
	private float[] pathTunnelChances = new float[]{2, 3, 1};
	private float[] branchTunnelChances = new float[]{3, 2, 1};
	
	public LoopBuilder setTunnelLength( float[] path, float[] branch){
		pathTunnelChances = path;
		branchTunnelChances = branch;
		return this;
	}
	
	private float extraConnectionChance = 0.1f;
	
	public LoopBuilder setExtraConnectionChance( float chance ){
		extraConnectionChance = chance;
		return this;
	}
	
	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {
		
		Room entrance = null;
		Room exit = null;
		Room shop = null;
		
		ArrayList<Room> multiConnections = new ArrayList<>();
		ArrayList<Room> singleConnections = new ArrayList<>();
		
		for (Room r : rooms){
			if (r instanceof EntranceRoom){
				entrance = r;
			} else if (r instanceof ExitRoom) {
				exit = r;
			} else if (r.maxConnections(Room.ALL) > 1){
				multiConnections.add(r);
			} else if (r.maxConnections(Room.ALL) == 1){
				singleConnections.add(r);
			}
		}
		
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
				loop.add(new TunnelRoom());
			}
		}
		
		Room prev = entrance;
		float targetAngle = startAngle;
		float angleChange = 360f / loop.size();
		for (int i = 0; i < loop.size(); i++){
			Room r = loop.get(i);
			targetAngle += angleChange;
			if (placeRoom(rooms, prev, r, targetAngle) != -1) {
				prev = r;
				if (!rooms.contains(prev))
					rooms.add(prev);
			}
		}
		
		//FIXME this is lazy, there are ways to do this without relying on chance
		if (!prev.connect(entrance)){
			return null;
		}
		
		ArrayList<Room> branchable = new ArrayList<>();
		for (Room r : loop){
			if (r instanceof EmptyRoom) branchable.add(r);
		}
		
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
