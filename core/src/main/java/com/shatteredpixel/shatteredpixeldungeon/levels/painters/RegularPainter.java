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
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class RegularPainter extends Painter {
	
	private float waterFill = 0f;
	private int waterSmoothness;
	
	private float grassFill = 0f;
	private int grassSmoothness;
	
	private int nTraps = 0;
	private Class<? extends Trap>[] trapClasses;
	private float[] trapChances;
	
	public void setWater(float fill, int smoothness){
		waterFill = fill;
		waterSmoothness = smoothness;
	}
	
	public void setGrass(float fill, int smoothness){
		grassFill = fill;
		grassSmoothness = smoothness;
	}
	
	public void setTraps(int num, Class<?>[] classes, float[] chances){
		nTraps = num;
		trapClasses = (Class<? extends Trap>[]) classes;
		trapChances = chances;
	}
	
	@Override
	public boolean paint(Level level, ArrayList<Room> rooms) {
		int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;
		
		for (Room r : rooms){
			if (r.left < leftMost) leftMost = r.left;
			if (r.top < topMost) topMost = r.top;
		}
		
		leftMost--;
		topMost--;
		
		int width = 0, height = 0;
		
		for (Room r : rooms){
			r.shift( -leftMost, -topMost);
			if (r.right > width) width = r.right;
			if (r.bottom > height) height = r.bottom;
		}
		
		width++;
		height++;
		
		level.setSize(width+1, height+1);
		
		for (Room r : rooms) {
			placeDoors( r );
			r.paint( level );
		}
		
		for (Room r : rooms) {
			paintDoors( level, r );
		}
		
		if (waterFill > 0f) {
			paintWater( level );
		}
		
		if (grassFill > 0f){
			paintGrass( level );
		}
		
		if (nTraps > 0){
			paintTraps( level );
		}
		
		return true;
	}
	
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
				door = new Room.Door(Random.element(doorSpots));
				
				r.connected.put( n, door );
				n.connected.put( r, door );
			}
		}
	}
	
	protected void paintDoors( Level l, Room r ) {
		for (Room n : r.connected.keySet()) {
			
			if (joinRooms( l, r, n )) {
				continue;
			}
			
			Room.Door d = r.connected.get( n );
			int door = d.x + d.y * l.width();
			
			switch (d.type) {
				case EMPTY:
					l.map[door] = Terrain.EMPTY;
					break;
				case TUNNEL:
					l.map[door] =  l.tunnelTile();
					break;
				case REGULAR:
					if (Dungeon.depth <= 1) {
						l.map[door] = Terrain.DOOR;
					} else {
						boolean secret = (Dungeon.depth < 6 ? Random.Int( 12 - Dungeon.depth ) : Random.Int( 6 )) == 0;
						l.map[door] = secret ? Terrain.SECRET_DOOR : Terrain.DOOR;
					}
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
	
	protected boolean joinRooms( Level l, Room r, Room n ) {
		
		if (!(r instanceof StandardRoom && n instanceof StandardRoom)) {
			return false;
		}
		
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
	
	protected void paintWater( Level l ){
		boolean[] lake =
				Patch.generate( l.width(), l.height(), waterFill, waterSmoothness, true );
		for (int i=0; i < l.length(); i++) {
			if (l.map[i] == Terrain.EMPTY && lake[i]) {
				l.map[i] = Terrain.WATER;
			}
		}
	}
	
	protected void paintGrass( Level l ) {
		boolean[] grass =
				Patch.generate( l.width(), l.height(), grassFill, grassSmoothness, true );
		
		//adds some chaos to grass distribution, note that this does decrease the fill rate slightly
		//TODO: analyize statistical changes on fill rate
		for (int i=l.width()+1; i < l.length()-l.width()-1; i++) {
			if (l.map[i] == Terrain.EMPTY && grass[i]) {
				int count = 1;
				for (int n : PathFinder.NEIGHBOURS8) {
					if (grass[i + n]) {
						count++;
					}
				}
				l.map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;
			}
		}
	}
	
	protected void paintTraps( Level l ) {
		ArrayList<Integer> validCells = new ArrayList<>();
		
		for (int i = 0; i < l.length(); i ++) {
			if (l.map[i] == Terrain.EMPTY){
				validCells.add(i);
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
