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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tunnel.TunnelRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

//A simple builder which puts most rooms in a straight line with a few branches
public class LineBuilder extends Builder {
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
			} else if (r instanceof ShopRoom){
				shop = r;
			} else if (r.maxConnections(Room.ALL) > 1){
				multiConnections.add(r);
			} else if (r.maxConnections(Room.ALL) == 1){
				singleConnections.add(r);
			}
		}
		
		if (entrance == null || exit == null){
			return null;
		}
		
		ArrayList<Room> branchable = new ArrayList<>();
		
		entrance.setSize();
		entrance.setPos(0, -entrance.width()/2);
		branchable.add(entrance);
		
		if (shop != null){
			shop.setSize();
			shop.setPos(-shop.width()+1, -shop.height()/2);
			shop.connect(entrance);
		}
		
		int roomsOnPath = multiConnections.size()/5 + Random.Int(2);
		roomsOnPath = Math.min(roomsOnPath, multiConnections.size());
		
		Room curr = entrance;
		
		for (int i = 0; i <= roomsOnPath; i++){
			if (Random.Int(2) == 0){
				TunnelRoom t = new TunnelRoom();
				t.setSize();
				t.setPos( curr.right, -t.height()/2);
				t.connect(curr);
				rooms.add(t);
				branchable.add(t);
				curr = t;
			}
			if (Random.Int(2) == 0){
				TunnelRoom t = new TunnelRoom();
				t.setSize();
				t.setPos( curr.right, -t.height()/2);
				t.connect(curr);
				rooms.add(t);
				branchable.add(t);
				curr = t;
			}
			Room r = (i == roomsOnPath ? exit : multiConnections.get(i));
			r.setSize();
			r.setPos( curr.right, -r.height()/2);
			r.connect(curr);
			branchable.add(r);
			curr = r;
		}
		
		
		ArrayList<Room> upBrancheable = new ArrayList<>(branchable);
		ArrayList<Room> downBrancheable = new ArrayList<>(branchable);
		
		//place branches
		int i = roomsOnPath;
		while (i < multiConnections.size() + singleConnections.size()){
			if (upBrancheable.isEmpty() && downBrancheable.isEmpty())
				return null;
			
			boolean up = downBrancheable.isEmpty()
					|| (Random.Int(2) == 0 && !upBrancheable.isEmpty());
			
			if (up){
				curr = Random.element(upBrancheable);
				upBrancheable.remove(curr);
			} else {
				curr = Random.element(downBrancheable);
				downBrancheable.remove(curr);
			}
			
			if (Random.Int(3) == 0){
				TunnelRoom t = new TunnelRoom();
				if (placeBranchRoom(up, curr, t, rooms)){
					rooms.add(t);
				} else {
					continue;
				}
				curr = t;
			}
			if (Random.Int(3) == 0){
				TunnelRoom t = new TunnelRoom();
				if (placeBranchRoom(up, curr, t, rooms)){
					rooms.add(t);
				} else {
					continue;
				}
				curr = t;
			}
			Room r;
			if (i < multiConnections.size()) {
				r = multiConnections.get(i);
			} else {
				r = singleConnections.get(i - multiConnections.size());
			}
			
			if (!placeBranchRoom(up, curr, r, rooms)){
				continue;
			}
			if (r.canConnect(up ? Room.TOP : Room.BOTTOM) && Random.Int(3) == 0) {
				if (up) upBrancheable.add(r);
				else downBrancheable.add(r);
			}
			i++;
		}
		
		return rooms;
	
	}
	
	private boolean placeBranchRoom(boolean up, Room curr, Room next, ArrayList<Room> collision){
		Rect space = findFreeSpace(
				new Point( curr.left + curr.width()/2, up ? curr.top : curr.bottom),
				collision,
				Math.max(next.maxWidth(), next.maxHeight()));
		
		if (next.setSizeWithLimit(space.width()+1,space.height()+1 )){
			next.setPos( curr.left + (curr.width()-next.width())/2, up ? curr.top - next.height()+1 : curr.bottom);
			
			if (next.right > space.right){
				next.shift( space.right - next.right, 0);
			} else if (next.left < space.left){
				next.shift( space.left - next.left, 0);
			}
			
			return next.connect(curr);
			
		} else {
			return false;
		}
	}
}
