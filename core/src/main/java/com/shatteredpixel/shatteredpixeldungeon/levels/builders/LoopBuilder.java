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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tunnel.TunnelRoom;
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
			float placeAngle;
			if ((placeAngle = placeRoom(rooms, prev, r, targetAngle)) != -1) {
				//targetAngle += (targetAngle - placeAngle);
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
			if (r instanceof StandardRoom) branchable.add(r);
		}
		
		int i = 0;
		
		Room curr;
		float angle;
		int tries;
		ArrayList<Room> tunnelsThisBranch = new ArrayList<>();
		//TODO this is almost identical to logic in linebuilder, can probably generalize
		while (i < multiConnections.size() + singleConnections.size()){
			
			tunnelsThisBranch.clear();
			curr = Random.element(branchable);
			
			int tunnels = Random.chances(branchTunnelChances);
			for (int j = 0; j < tunnels; j++){
				TunnelRoom t = new TunnelRoom();
				tries = 10;
				
				do {
					angle = placeRoom(rooms, curr, t, Random.Float(360f));
					tries--;
				} while (angle == -1 && tries >= 0);
				
				if (angle == -1) {
					for (Room r : tunnelsThisBranch){
						r.clearConnections();
						rooms.remove(r);
					}
					tunnelsThisBranch.clear();
					break;
				} else {
					tunnelsThisBranch.add(t);
					rooms.add(t);
				}
				
				curr = t;
			}
			
			if (tunnelsThisBranch.size() != tunnels){
				continue;
			}
			
			Room r;
			if (i < multiConnections.size()) {
				r = multiConnections.get(i);
			} else {
				r = singleConnections.get(i - multiConnections.size());
			}
			
			tries = 10;
			
			do {
				angle = placeRoom(rooms, curr, r, Random.Float(360f));
				tries--;
			} while (angle == -1 && tries >= 0);
			
			if (angle == -1){
				for (Room t : tunnelsThisBranch){
					t.clearConnections();
					rooms.remove(t);
				}
				tunnelsThisBranch.clear();
				continue;
			}
			
			
			if (r.maxConnections(Room.ALL) > 1 && Random.Int(2) == 0)
				branchable.add(r);
			
			i++;
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
