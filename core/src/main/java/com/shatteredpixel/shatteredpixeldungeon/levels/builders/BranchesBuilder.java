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
		
		ArrayList<Room> roomsToBranch = new ArrayList<>();
		roomsToBranch.addAll(multiConnections);
		if (exit != null) roomsToBranch.add(exit);
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
