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
import com.watabou.utils.Random;

import java.util.ArrayList;

//A simple builder which utilizes a line as its core feature.
public class LineBuilder extends Builder {

	private float pathVariance = 45f;

	public LineBuilder setPathVariance( float var ){
		pathVariance = var;
		return this;
	}

	//path length is the percentage of pathable rooms that are on the path
	private float pathLength = 0.1f;
	//The chance weights for extra rooms to be added to the path
	private float[] pathLenJitterChances = new float[]{0, 2, 1};

	public LineBuilder setPathLength( float len, float[] jitter ){
		pathLength = len;
		pathLenJitterChances = jitter;
		return this;
	}

	private float[] pathTunnelChances = new float[]{2, 3, 1};
	private float[] branchTunnelChances = new float[]{3, 2, 1};

	public LineBuilder setTunnelLength( float[] path, float[] branch){
		pathTunnelChances = path;
		branchTunnelChances = branch;
		return this;
	}
	
	private float extraConnectionChance = 0.1f;
	
	public LineBuilder setExtraConnectionChance( float chance ){
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
			} else if (r instanceof ShopRoom && r.maxConnections(Room.ALL) == 1){
				shop = r;
			} else if (r.maxConnections(Room.ALL) > 1){
				multiConnections.add(r);
			} else if (r.maxConnections(Room.ALL) == 1){
				singleConnections.add(r);
			}
		}
		
		if (entrance == null){
			return null;
		}

		float direction = Random.Float(0, 360);
		ArrayList<Room> branchable = new ArrayList<>();
		
		entrance.setSize();
		entrance.setPos(0, 0);
		branchable.add(entrance);

		if (shop != null){
			placeRoom(rooms, entrance, shop, direction + 180f);
		}
		
		int roomsOnPath = (int)(multiConnections.size()*pathLength) + Random.chances(pathLenJitterChances);
		roomsOnPath = Math.min(roomsOnPath, multiConnections.size());
		
		Room curr = entrance;

		for (int i = 0; i <= roomsOnPath; i++){
			if (i == roomsOnPath && exit == null)
				continue;

			int tunnels = Random.chances(pathTunnelChances);
			for (int j = 0; j < tunnels; j++){
				ConnectionRoom t = ConnectionRoom.createRoom();
				placeRoom(rooms, curr, t, direction + Random.Float(-pathVariance, pathVariance));
				branchable.add(t);
				rooms.add(t);
				curr = t;
			}

			Room r = (i == roomsOnPath ? exit : multiConnections.get(i));
			placeRoom(rooms, curr, r, direction + Random.Float(-pathVariance, pathVariance));
			branchable.add(r);
			curr = r;
		}
		
		ArrayList<Room> roomsToBranch = new ArrayList<>();
		for (int i = roomsOnPath; i < multiConnections.size(); i++){
			roomsToBranch.add(multiConnections.get(i));
		}
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
