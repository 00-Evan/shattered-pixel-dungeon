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

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.utils.Graph;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

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
		
		for (Room r : rooms) {
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
		for (Room r : rooms) {
			for (Room n : r.connected.keySet()) {
				
				if (joinRooms(l, r, n)) {
					continue;
				}
				
				Room.Door d = r.connected.get(n);
				int door = d.x + d.y * l.width();
				
				if (d.type == Room.Door.Type.REGULAR){
					//chance for a hidden door scales from 3/21 on floor 2 to 3/3 on floor 20
					if (Dungeon.depth > 1 &&
							(Dungeon.depth >= 20 || Random.Int(23 - Dungeon.depth) < Dungeon.depth)) {
						d.type = Room.Door.Type.HIDDEN;
						Graph.buildDistanceMap(rooms, r);
						//don't hide if it would make this room only accessible by hidden doors
						if (n.distance == Integer.MAX_VALUE){
							d.type = Room.Door.Type.UNLOCKED;
						}
					} else {
						d.type = Room.Door.Type.UNLOCKED;
					}
				}
				
				switch (d.type) {
					case EMPTY:
						l.map[door] = Terrain.EMPTY;
						break;
					case TUNNEL:
						l.map[door] = l.tunnelTile();
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
				}
			}
		}
	}
	
	protected boolean joinRooms( Level l, Room r, Room n ) {
		
		if (!(r instanceof EmptyRoom && n instanceof EmptyRoom)) {
			return false;
		}
		
		//TODO decide on good probabilities and dimension restrictions
		Rect w = r.intersect( n );
		if (w.left == w.right) {
			
			if (w.bottom - w.top < 3) {
				return false;
			}
			
			if (w.height()+1 == Math.max( r.height(), n.height() )) {
				return false;
			}
			
			if (r.width() + n.width() > 10) {
				return false;
			}
			
			w.top += 1;
			w.bottom -= 0;
			
			w.right++;
			
			Painter.fill( l, w.left, w.top, 1, w.height(), Terrain.EMPTY );
			
		} else {
			
			if (w.right - w.left < 3) {
				return false;
			}
			
			if (w.width()+1 == Math.max( r.width(), n.width() )) {
				return false;
			}
			
			if (r.height() + n.height() > 10) {
				return false;
			}
			
			w.left += 1;
			w.right -= 0;
			
			w.bottom++;
			
			Painter.fill( l, w.left, w.top, w.width(), 1, Terrain.EMPTY );
		}
		
		return true;
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
		
		for (int i = 0; i < nTraps; i++) {
			
			Integer trapPos = Random.element(validCells);
			validCells.remove(trapPos); //removes the integer object, not at the index
			
			try {
				Trap trap = trapClasses[Random.chances( trapChances )].newInstance().hide();
				l.setTrap( trap, trapPos );
				//some traps will not be hidden
				l.map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}
