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

//A simple builder which utilizes a line as its core feature.
public class LineBuilder extends Builder {

	private float pathVariance = 45f;

	public LineBuilder setPathVariance( float var ){
		pathVariance = var;
		return this;
	}

	//path length is a percentage of total path rooms
	private float pathLength = 0.2f;
	//The chance weights for extra rooms to be added to the path
	private float[] pathLenJitterChances = new float[]{1, 1};

	public LineBuilder setPathLength( float len, float[] jitter ){
		pathLength = len;
		pathLenJitterChances = jitter;
		return this;
	}

	private float[] pathTunnelChances = new float[]{1, 2, 1};
	private float[] branchTunnelChances = new float[]{2, 1, 1};

	public LineBuilder setTunnelLength( float[] path, float[] branch){
		pathTunnelChances = path;
		branchTunnelChances = branch;
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
		
		int roomsOnPath = Math.round(multiConnections.size()*pathLength + Random.chances(pathLenJitterChances));
		roomsOnPath = Math.min(roomsOnPath, multiConnections.size());
		
		Room curr = entrance;

		for (int i = 0; i <= roomsOnPath; i++){
			if (i == roomsOnPath && exit == null)
				continue;

			int tunnels = Random.chances(pathTunnelChances);
			for (int j = 0; j < tunnels; j++){
				TunnelRoom t = new TunnelRoom();
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
		
		//place branches
		int i = roomsOnPath;
		float angle;
		int tries;
		while (i < multiConnections.size() + singleConnections.size()){

			curr = Random.element(branchable);

			int tunnels = Random.chances(branchTunnelChances);
			for (int j = 0; j < tunnels; j++){
				TunnelRoom t = new TunnelRoom();
				tries = 10;

				do {
					angle = placeRoom(rooms, curr, t, Random.Float(360f));
					tries--;
				} while (angle == -1 && tries >= 0);

				if (angle == -1)
					continue;
				else
					rooms.add(t);

				curr = t;
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

			if (angle == -1)
				continue;

			if (r.maxConnections(Room.ALL) > 1 && Random.Int(2) == 0)
				branchable.add(r);

			i++;
		}
		
		return rooms;
	
	}

}
