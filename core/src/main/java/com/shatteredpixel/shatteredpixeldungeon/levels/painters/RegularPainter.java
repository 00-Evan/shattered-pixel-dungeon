/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.noosa.Game;
import com.watabou.utils.Graph;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class RegularPainter extends Painter {
	
	private float waterFill = 0f;
	private int waterSmoothness;
	
	public RegularPainter setWater(float fill, int smoothness){
		waterFill = fill;
		waterSmoothness = smoothness;
		return this;
	}
	
	private float grassFill = 0f;
	private int grassSmoothness;
	
	public RegularPainter setGrass(float fill, int smoothness){
		grassFill = fill;
		grassSmoothness = smoothness;
		return this;
	}
	
	private int nTraps = 0;
	private Class<? extends Trap>[] trapClasses;
	private float[] trapChances;
	
	public RegularPainter setTraps(int num, Class<?>[] classes, float[] chances){
		nTraps = num;
		trapClasses = (Class<? extends Trap>[]) classes;
		trapChances = chances;
		return this;
	}
	
	@Override
	public boolean paint(Level level, ArrayList<Room> rooms) {
		
		//painter can be used without rooms
		if (rooms != null) {
			
			int padding = level.feeling == Level.Feeling.CHASM ? 2 : 1;
			
			int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;
			
			for (Room r : rooms) {
				if (r.left < leftMost) leftMost = r.left;
				if (r.top < topMost) topMost = r.top;
			}
			
			leftMost -= padding;
			topMost -= padding;
			
			int rightMost = 0, bottomMost = 0;
			
			for (Room r : rooms) {
				r.shift(-leftMost, -topMost);
				if (r.right > rightMost) rightMost = r.right;
				if (r.bottom > bottomMost) bottomMost = r.bottom;
			}
			
			rightMost += padding;
			bottomMost += padding;
			
			//add 1 to account for 0 values
			level.setSize(rightMost + 1, bottomMost + 1);
		} else {
			//check if the level's size was already initialized by something else
			if (level.length() == 0) return false;
			
			//easier than checking for null everywhere
			rooms = new ArrayList<>();
		}
		
		Random.shuffle(rooms);
		
		for (Room r : rooms.toArray(new Room[0])) {
			if (r.connected.isEmpty()){
				Game.reportException( new RuntimeException("Painting a room with no connections! Room:" + r.getClass().getSimpleName() + " Seed:" + Dungeon.seed + " Depth:" + Dungeon.depth));
				if (r instanceof SpecialRoom) return false;
			}
			placeDoors( r );
			r.paint( level );
		}
		
		paintDoors( level, rooms );
		
		if (waterFill > 0f) {
			paintWater( level, rooms );
		}
		
		if (grassFill > 0f){
			paintGrass( level, rooms );
		}
		
		if (nTraps > 0){
			paintTraps( level, rooms );
		}
		
		decorate( level, rooms );
		
		return true;
	}
	
	protected abstract void decorate(Level level, ArrayList<Room> rooms);
	
	private void placeDoors( Room r ) {
		for (Room n : r.connected.keySet()) {
			Room.Door door = r.connected.get( n );
			if (door == null) {
				
				Rect i = r.intersect( n );
				ArrayList<Point> doorSpots = new ArrayList<>();
				for (Point p : i.getPoints()){
					if (r.canConnect(p) && n.canConnect(p))
						doorSpots.add(p);
				}
				if (doorSpots.isEmpty()){
					ShatteredPixelDungeon.reportException(
							new RuntimeException("Could not place a door! " +
									"r=" + r.getClass().getSimpleName() +
									" n=" + n.getClass().getSimpleName()));
					continue;
				}
				door = new Room.Door(Random.element(doorSpots));
				
				r.connected.put( n, door );
				n.connected.put( r, door );
			}
		}
	}
	
	protected void paintDoors( Level l, ArrayList<Room> rooms ) {

		float hiddenDoorChance = 0;
		if (Dungeon.depth > 1){
			//chance for a hidden door scales from 2/20 on floor 2 to 20/20 on floor 20
			hiddenDoorChance = Math.min(1f, Dungeon.depth / 20f);
		}
		if (l.feeling == Level.Feeling.SECRETS){
			//pull the value of extra secret doors toward 50% on secrets level feel
			hiddenDoorChance = (0.5f + hiddenDoorChance)/2f;
		}

		HashMap<Room, Room> roomMerges = new HashMap<>();

		for (Room r : rooms) {
			for (Room n : r.connected.keySet()) {

				//normal sized rooms can be merged at most once. Large and Giant rooms can be merged many times
				if (roomMerges.get(r) == n || roomMerges.get(n) == r){
					continue;
				} else if (!roomMerges.containsKey(r) && !roomMerges.containsKey(n) &&
						mergeRooms(l, r, n, r.connected.get(n), Terrain.EMPTY)) {
					if (((StandardRoom) r).sizeCat == StandardRoom.SizeCategory.NORMAL) roomMerges.put(r, n);
					if (((StandardRoom) n).sizeCat == StandardRoom.SizeCategory.NORMAL) roomMerges.put(n, r);
					continue;
				}
				
				Room.Door d = r.connected.get(n);
				int door = d.x + d.y * l.width();
				
				if (d.type == Room.Door.Type.REGULAR){
					if (Random.Float() < hiddenDoorChance) {
						d.type = Room.Door.Type.HIDDEN;
						//all standard rooms must have an unbroken path to all other standard rooms
						if (l.feeling != Level.Feeling.SECRETS){
							Graph.buildDistanceMap(rooms, r);
							if (n.distance == Integer.MAX_VALUE){
								d.type = Room.Door.Type.UNLOCKED;
							}
						//on a secrets level, rooms just have to not be totally isolated
						} else {
							int roomsInGraph = 0;
							Graph.buildDistanceMap(rooms, r);
							for (Room rDest : rooms){
								if (rDest.distance != Integer.MAX_VALUE
										&& !(rDest instanceof ConnectionRoom)){
									roomsInGraph++;
								}
							}
							if (roomsInGraph < 2){
								d.type = Room.Door.Type.UNLOCKED;
							} else {
								roomsInGraph = 0;
								Graph.buildDistanceMap(rooms, n);
								for (Room nDest : rooms){
									if (nDest.distance != Integer.MAX_VALUE
											&& !(nDest instanceof ConnectionRoom)){
										roomsInGraph++;
									}
								}
								if (roomsInGraph < 2){
									d.type = Room.Door.Type.UNLOCKED;
								}
							}
						}
						Graph.buildDistanceMap(rooms, r);
						//don't hide if it would make this room only accessible by hidden doors
						//unless we're on a secrets depth
						if (l.feeling != Level.Feeling.SECRETS && n.distance == Integer.MAX_VALUE){
							d.type = Room.Door.Type.UNLOCKED;
						}
					} else {
						d.type = Room.Door.Type.UNLOCKED;
					}

					//entrance doors on floor 1 are hidden during tutorial
					//entrance doors on floor 2 are hidden if the player hasn't picked up 2nd guidebook page
					if (r instanceof EntranceRoom || n instanceof EntranceRoom){
						if ((Dungeon.depth == 1 && SPDSettings.intro())
							|| (Dungeon.depth == 2 && !Document.ADVENTURERS_GUIDE.isPageFound(Document.GUIDE_SEARCHING))) {
							d.type = Room.Door.Type.HIDDEN;
						}
					}
				}
				
				switch (d.type) {
					case EMPTY:
						l.map[door] = Terrain.EMPTY;
						break;
					case TUNNEL:
						l.map[door] = l.tunnelTile();
						break;
					case WATER:
						l.map[door] = Terrain.WATER;
						break;
					case UNLOCKED:
						l.map[door] = Terrain.DOOR;
						break;
					case HIDDEN:
						l.map[door] = Terrain.SECRET_DOOR;
						break;
					case BARRICADE:
						l.map[door] = Terrain.BARRICADE;
						break;
					case LOCKED:
						l.map[door] = Terrain.LOCKED_DOOR;
						break;
					case CRYSTAL:
						l.map[door] = Terrain.CRYSTAL_DOOR;
						break;
				}
			}
		}
	}

	protected boolean mergeRooms( Level l, Room r, Room n, Point start, int mergeTerrain){

		Rect intersect = r.intersect( n );
		if (intersect.left == intersect.right) {

			Rect merge = new Rect();
			merge.left = merge.right = intersect.left;
			merge.top = merge.bottom = start != null ? start.y : intersect.center().y;

			Point p = new Point(merge.left, merge.top);
			while(merge.top > intersect.top && n.canMerge(l, p, mergeTerrain) && r.canMerge(l, p, mergeTerrain)) {
				merge.top--;
				p.y--;
			}
			p.y = merge.bottom;
			while(merge.bottom < intersect.bottom && n.canMerge(l, p, mergeTerrain) && r.canMerge(l, p, mergeTerrain)) {
				merge.bottom++;
				p.y++;
			}

			if (merge.height() >= 3) {
				r.merge(l, n, new Rect(merge.left, merge.top + 1, merge.left+1, merge.bottom), mergeTerrain);
				return true;
			} else {
				return false;
			}

		} else if (intersect.top == intersect.bottom) {

			Rect merge = new Rect();
			merge.left = merge.right = start != null ? start.x : intersect.center().x;
			merge.top = merge.bottom = intersect.top;

			Point p = new Point(merge.left, merge.top);
			while(merge.left > intersect.left && n.canMerge(l, p, mergeTerrain) && r.canMerge(l, p, mergeTerrain)) {
				merge.left--;
				p.x--;
			}
			p.x = merge.right;
			while(merge.right < intersect.right && n.canMerge(l, p, mergeTerrain) && r.canMerge(l, p, mergeTerrain)) {
				merge.right++;
				p.x++;
			}

			if (merge.width() >= 3) {
				r.merge(l, n, new Rect(merge.left + 1, merge.top, merge.right, merge.top+1), mergeTerrain);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}
	
	protected void paintWater( Level l, ArrayList<Room> rooms ){
		boolean[] lake = Patch.generate( l.width(), l.height(), waterFill, waterSmoothness, true );
		
		if (!rooms.isEmpty()){
			for (Room r : rooms){
				for (Point p : r.waterPlaceablePoints()){
					int i = l.pointToCell(p);
					if (lake[i] && l.map[i] == Terrain.EMPTY){
						l.map[i] = Terrain.WATER;
					}
				}
			}
		} else {
			for (int i = 0; i < l.length(); i ++) {
				if (lake[i] && l.map[i] == Terrain.EMPTY){
					l.map[i] = Terrain.WATER;
				}
			}
		}
		
	}
	
	protected void paintGrass( Level l, ArrayList<Room> rooms ) {
		boolean[] grass = Patch.generate( l.width(), l.height(), grassFill, grassSmoothness, true );
		
		ArrayList<Integer> grassCells = new ArrayList<>();
		
		if (!rooms.isEmpty()){
			for (Room r : rooms){
				for (Point p : r.grassPlaceablePoints()){
					int i = l.pointToCell(p);
					if (grass[i] && l.map[i] == Terrain.EMPTY){
						grassCells.add(i);
					}
				}
			}
		} else {
			for (int i = 0; i < l.length(); i ++) {
				if (grass[i] && l.map[i] == Terrain.EMPTY){
					grassCells.add(i);
				}
			}
		}
		
		//Adds chaos to grass height distribution. Ratio of high grass depends on fill and smoothing
		//Full range is 8.3% to 75%, but most commonly (20% fill with 3 smoothing) is around 60%
		//low smoothing, or very low fill, will begin to push the ratio down, normally to 50-30%
		for (int i : grassCells) {
			if (l.heaps.get(i) != null || l.findMob(i) != null) {
				l.map[i] = Terrain.GRASS;
				continue;
			}
			
			int count = 1;
			for (int n : PathFinder.NEIGHBOURS8) {
				if (grass[i + n]) {
					count++;
				}
			}
			l.map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
		}
	}
	
	protected void paintTraps( Level l, ArrayList<Room> rooms ) {
		ArrayList<Integer> validCells = new ArrayList<>();
		
		if (!rooms.isEmpty()){
			for (Room r : rooms){
				for (Point p : r.trapPlaceablePoints()){
					int i = l.pointToCell(p);
					if (l.map[i] == Terrain.EMPTY){
						validCells.add(i);
					}
				}
			}
		} else {
			for (int i = 0; i < l.length(); i ++) {
				if (l.map[i] == Terrain.EMPTY){
					validCells.add(i);
				}
			}
		}
		
		//no more than one trap every 5 valid tiles.
		nTraps = Math.min(nTraps, validCells.size()/5);

		//for traps that want to avoid being in hallways
		ArrayList<Integer> validNonHallways = new ArrayList<>();

		//temporarily use the passable array for the next step
		for (int i = 0; i < l.length(); i++){
			l.passable[i] = (Terrain.flags[l.map[i]] & Terrain.PASSABLE) != 0;
		}

		for (int i : validCells){
			if ((l.passable[i+PathFinder.CIRCLE4[0]] || l.passable[i+PathFinder.CIRCLE4[2]])
					&& (l.passable[i+PathFinder.CIRCLE4[1]] || l.passable[i+PathFinder.CIRCLE4[3]])){
				validNonHallways.add(i);
			}
		}

		//no more than one trap every 5 valid tiles.
		nTraps = Math.min(nTraps, validCells.size()/5);

		//5x traps on traps level feeling, but the extra traps are all visible
		for (int i = 0; i < (l.feeling == Level.Feeling.TRAPS ? 5*nTraps : nTraps); i++) {

			Trap trap = Reflection.newInstance(trapClasses[Random.chances( trapChances )]);

			Integer trapPos;
			if (trap.avoidsHallways && !validNonHallways.isEmpty()){
				trapPos = Random.element(validNonHallways);
			} else {
				trapPos = Random.element(validCells);
			}
			//removes the integer object, not at the index
			validCells.remove(trapPos);
			validNonHallways.remove(trapPos);

			if (i < nTraps) trap.hide();
			else            trap.reveal();

			l.setTrap( trap, trapPos );
			//some traps will not be hidden
			l.map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
		}
	}
	
}
