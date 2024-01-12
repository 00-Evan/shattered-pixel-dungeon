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
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

//A builder that creates only branches, very simple and very random
public class BranchesBuilder extends RegularBuilder {
	
	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms) {
		
		setupRooms( rooms );
		
		if (entrance == null){
			return null;
		}
		
		ArrayList<Room> branchable = new ArrayList<>();
		
		entrance.setSize();
		entrance.setPos(0, 0);
		branchable.add(entrance);
		
		if (shop != null){
			placeRoom(branchable, entrance, shop, Random.Float(360f));
		}

		//we place up to 2 or 3 main path rooms first so that levelgen has a starting point for branches
		int mainBranchRooms = Math.max(Random.Int(2, 3), mainPathRooms.size());
		float[] pathTunnels = pathTunnelChances.clone();
		for (int i = 1; i < mainBranchRooms; i++){
			Room prev = entrance;
			Room r = mainPathRooms.get(0);

			int tunnels = Random.chances(pathTunnels);
			if (tunnels == -1){
				pathTunnels = pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}
			pathTunnels[tunnels]--;

			for (int j = 0; j < tunnels; j++){
				ConnectionRoom t = ConnectionRoom.createRoom();
				float result;
				do {
					result = placeRoom(rooms, prev, t, Random.Float(360f));
				} while (result == -1);
				branchable.add(t);
				rooms.add(t);
				prev = t;
			}

			float result;
			do {
				result = placeRoom(rooms, prev, r, Random.Float(360f));
			} while (result == -1);
			branchable.add(r);
			mainPathRooms.remove(r);
		}

		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(mainPathRooms);
		roomsToBranch.addAll(multiConnections);
		if (exit != null) roomsToBranch.add(exit);
		roomsToBranch.addAll(singleConnections);
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

	@Override
	protected float randomBranchAngle( Room r ) {
		PointF entranceCenter = new PointF(entrance.center());
		//generate four angles randomly and return the one which points closer to the entrance
		float toCenter = angleBetweenPoints( new PointF((r.left + r.right)/2f, (r.top + r.bottom)/2f), entranceCenter);
		if (toCenter < 0) toCenter += 360f;

		float currAngle = Random.Float(360f);
		for( int i = 0; i < 4; i ++){
			float newAngle = Random.Float(360f);
			if (Math.abs(toCenter - newAngle) < Math.abs(toCenter - currAngle)){
				currAngle = newAngle;
			}
		}
		return currAngle;
	}
}
