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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
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
		ArrayList<StandardRoom> standards = new ArrayList<>();
		ArrayList<SpecialRoom> specials = new ArrayList<>();
		
		for (Room r : rooms){
			if (r instanceof EntranceRoom){
				entrance = r;
			} else if (r instanceof ExitRoom) {
				exit = r;
			} else if (r instanceof ShopRoom){
				shop = r;
			} else if (r instanceof StandardRoom){
				standards.add((StandardRoom)r);
			} else if (r instanceof SpecialRoom){
				specials.add((SpecialRoom)r);
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
			shop.setPos(-shop.width(), -shop.height()/2);
			shop.connect(entrance);
		}
		
		int standardsOnPath = standards.size()/5 + Random.Int(2);
		standardsOnPath = Math.min(standardsOnPath, standards.size());
		
		//standardsOnPath = standards.size();
		
		Room curr = entrance;
		
		for (int i = 0; i < standardsOnPath; i++){
			if (Random.Int(3) == 0){
				TunnelRoom t = new TunnelRoom();
				t.setSize();
				t.setPos( curr.right, -t.height()/2);
				t.connect(curr);
				rooms.add(t);
				branchable.add(t);
				curr = t;
			}
			if (Random.Int(3) == 0){
				TunnelRoom t = new TunnelRoom();
				t.setSize();
				t.setPos( curr.right, -t.height()/2);
				t.connect(curr);
				rooms.add(t);
				branchable.add(t);
				curr = t;
			}
			StandardRoom s = standards.get(i);
			s.setSize();
			s.setPos( curr.right, -s.height()/2);
			s.connect(curr);
			branchable.add(s);
			curr = s;
		}
		
		//place exit
		exit.setSize();
		exit.setPos( curr.right, -exit.height()/2);
		exit.connect(curr);
		branchable.add(exit);
		
		ArrayList<Room> upBrancheable = new ArrayList<>(branchable);
		ArrayList<Room> downBrancheable = new ArrayList<>(branchable);
		
		//place branches
		int i = standardsOnPath;
		while (i < standards.size() + specials.size()){
			boolean up = !upBrancheable.isEmpty() && (Random.Int(2) == 0 || downBrancheable.isEmpty());
			
			if (up){
				curr = Random.element(upBrancheable);
				upBrancheable.remove(curr);
			} else {
				curr = Random.element(downBrancheable);
				downBrancheable.remove(curr);
			}
			
			if (Random.Int(2) == 0){
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
			if (i < standards.size()) {
				r = standards.get(i);
			} else {
				r = specials.get(i - standards.size());
			}
			
			if (!placeBranchRoom(up, curr, r, rooms)){
				continue;
			}
			if (r instanceof StandardRoom && Random.Int(3) == 0) {
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
