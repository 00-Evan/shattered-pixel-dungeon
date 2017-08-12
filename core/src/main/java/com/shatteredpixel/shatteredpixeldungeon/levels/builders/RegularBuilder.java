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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashSet;

//Introduces the concept of a major path, and branches
// with tunnels padding rooms placed in them
public abstract class RegularBuilder extends Builder {
	
	// *** Parameter values for level building logic ***
	// note that implementations do not have to use al of these variables
	
	protected float pathVariance = 45f;
	
	public RegularBuilder setPathVariance( float var ){
		pathVariance = var;
		return this;
	}
	
	//path length is the percentage of pathable rooms that are on
	protected float pathLength = 0.5f;
	//The chance weights for extra rooms to be added to the path
	protected float[] pathLenJitterChances = new float[]{0, 1, 0};
	
	public RegularBuilder setPathLength( float len, float[] jitter ){
		pathLength = len;
		pathLenJitterChances = jitter;
		return this;
	}
	
	protected float[] pathTunnelChances = new float[]{1, 3, 1};
	protected float[] branchTunnelChances = new float[]{2, 2, 1};
	
	public RegularBuilder setTunnelLength( float[] path, float[] branch){
		pathTunnelChances = path;
		branchTunnelChances = branch;
		return this;
	}
	
	protected float extraConnectionChance = 0.2f;
	
	public RegularBuilder setExtraConnectionChance( float chance ){
		extraConnectionChance = chance;
		return this;
	}
	
	// *** Room Setup ***
	
	protected Room entrance = null;
	protected Room exit = null;
	protected Room shop = null;
	
	protected ArrayList<Room> multiConnections = new ArrayList<>();
	protected ArrayList<Room> singleConnections = new ArrayList<>();
	
	protected void setupRooms(ArrayList<Room> rooms){
		for(Room r : rooms){
			r.setEmpty();
		}
		
		entrance = exit = shop = null;
		singleConnections.clear();
		multiConnections.clear();
		for (Room r : rooms){
			if (r instanceof EntranceRoom){
				entrance = r;
			} else if (r instanceof ExitRoom) {
				exit = r;
			} else if (r instanceof ShopRoom && r.maxConnections(Room.ALL) == 1){
				shop = r;
			} else if (r.maxConnections(Room.ALL) > 1){
				multiConnections.add(r);
			} else if (r.maxConnections(Room.ALL) == 1){
				singleConnections.add(r);
			}
		}

		//this weights larger rooms to be much more likely to appear in the main loop, by placing them earlier in the multiconnections list
		weightRooms(multiConnections);
		Random.shuffle(multiConnections);
		multiConnections = new ArrayList<>(new LinkedHashSet<>(multiConnections));
	}
	
	// *** Branch Placement ***
	
	protected void weightRooms(ArrayList<Room> rooms){
		for (Room r : rooms.toArray(new Room[0])){
			if (r instanceof StandardRoom){
				for (int i = 1; i < ((StandardRoom) r).sizeCat.connectionWeight(); i++)
					rooms.add(r);
			}
		}
	}
	
	//places the rooms in roomsToBranch into branches from rooms in branchable.
	//note that the three arrays should be separate, they may contain the same rooms however
	protected void createBranches(ArrayList<Room> rooms, ArrayList<Room> branchable,
	                                     ArrayList<Room> roomsToBranch, float[] connChances){
		
		int i = 0;
		float angle;
		int tries;
		Room curr;
		ArrayList<Room> connectingRoomsThisBranch = new ArrayList<>();
		
		float[] connectionChances = connChances.clone();
		while (i < roomsToBranch.size()){
			
			connectingRoomsThisBranch.clear();
			curr = Random.element(branchable);
			
			int connectingRooms = Random.chances(connectionChances);
			if (connectingRooms == -1){
				connectionChances = connChances.clone();
				connectingRooms = Random.chances(connectionChances);
			}
			connectionChances[connectingRooms]--;
			
			for (int j = 0; j < connectingRooms; j++){
				ConnectionRoom t = ConnectionRoom.createRoom();
				tries = 3;
				
				do {
					angle = placeRoom(rooms, curr, t, randomBranchAngle(curr));
					tries--;
				} while (angle == -1 && tries > 0);
				
				if (angle == -1) {
					for (Room r : connectingRoomsThisBranch){
						r.clearConnections();
						rooms.remove(r);
					}
					connectingRoomsThisBranch.clear();
					break;
				} else {
					connectingRoomsThisBranch.add(t);
					rooms.add(t);
				}
				
				curr = t;
			}
			
			if (connectingRoomsThisBranch.size() != connectingRooms){
				continue;
			}
			
			Room r = roomsToBranch.get(i);
			
			tries = 10;
			
			do {
				angle = placeRoom(rooms, curr, r, randomBranchAngle(curr));
				tries--;
			} while (angle == -1 && tries > 0);
			
			if (angle == -1){
				for (Room t : connectingRoomsThisBranch){
					t.clearConnections();
					rooms.remove(t);
				}
				connectingRoomsThisBranch.clear();
				continue;
			}
			
			for (int j = 0; j <connectingRoomsThisBranch.size(); j++){
				if (Random.Int(3) <= 1) branchable.add(connectingRoomsThisBranch.get(j));
			}
			if (r.maxConnections(Room.ALL) > 1 && Random.Int(3) == 0) {
				if (r instanceof StandardRoom){
					for (int j = 0; j < ((StandardRoom) r).sizeCat.connectionWeight(); j++){
						branchable.add(r);
					}
				} else {
					branchable.add(r);
				}
			}
			
			i++;
		}
	}
	
	protected float randomBranchAngle( Room r ){
		return Random.Float(360f);
	}
	
}
