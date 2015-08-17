/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Room.Type;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Scene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.List;

public class PrisonBossLevel extends RegularLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}
	
	private Room anteroom;
	private int arenaDoor;
	
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}
	
	private static final String ARENA	= "arena";
	private static final String DOOR	= "door";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "droppped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARENA, roomExit );
		bundle.put( DOOR, arenaDoor );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		roomExit = (Room)bundle.get( ARENA );
		arenaDoor = bundle.getInt( DOOR );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}
	
	@Override
	protected boolean build() {
		
		initRooms();
	
		int distance;
		int retry = 0;

		do {
			
			if (retry++ > 10) {
				return false;
			}
			
			int innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return false;
				}
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4);
			
			innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return false;
				}
				roomExit = Random.element( rooms );
			} while (
				roomExit == roomEntrance ||
				roomExit.width() < 7 ||
				roomExit.height() < 7 ||
				roomExit.top == 0);
	
			Graph.buildDistanceMap( rooms, roomExit );
			distance = Graph.buildPath( rooms, roomEntrance, roomExit ).size();
			
		} while (distance < 3);
		
		roomEntrance.type = Type.ENTRANCE;
		roomExit.type = Type.BOSS_EXIT;
		
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );
		Graph.setPrice( path, roomEntrance.distance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		anteroom = path.get( path.size() - 2 );
		anteroom.type = Type.STANDARD;

		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
		}
		
		for (Room r : rooms) {
			if (r.type == Type.NULL && r.connected.size() > 0) {
				r.type = Type.PASSAGE;
			}
		}
		
		paint();
		
		Room r = (Room)roomExit.connected.keySet().toArray()[0];
		if (roomExit.connected.get( r ).y == roomExit.top) {
			return false;
		}
		
		paintWater();
		paintGrass();
		
		placeTraps();
		
		return true;
	}
		
	protected boolean[] water() {
		return Patch.generate( 0.45f, 5 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( 0.30f, 4 );
	}
	
	protected void paintDoors( Room r ) {
		
		for (Room n : r.connected.keySet()) {
			
			if (r.type == Type.NULL) {
				continue;
			}
			
			Point door = r.connected.get( n );
			
			if (r.type == Room.Type.PASSAGE && n.type == Room.Type.PASSAGE) {
				
				Painter.set( this, door, Terrain.EMPTY );
				
			} else {
				
				Painter.set( this, door, Terrain.DOOR );
				
			}
			
		}
	}
	
	@Override
	protected void placeTraps() {
		
		int nTraps = nTraps();

		for (int i=0; i < nTraps; i++) {
			
			int trapPos = Random.Int( LENGTH );
			
			if (map[trapPos] == Terrain.EMPTY) {
				map[trapPos] = Terrain.TRAP;
				setTrap( new PoisonTrap().reveal(), trapPos );
			}
		}
	}
	
	@Override
	protected void decorate() {
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) {
				
				float c = 0.15f;
				if (map[i + 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i + 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				
				if (Random.Float() < c) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 4 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL &&
				map[i - WIDTH] == Terrain.WALL &&
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 2 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		placeSign();
		
		Point door = roomExit.entrance();
		arenaDoor = door.x + door.y * WIDTH;
		Painter.set( this, arenaDoor, Terrain.LOCKED_DOOR );
		
		Painter.fill( this,
			roomExit.left + 2,
			roomExit.top + 2,
			roomExit.width() - 3,
			roomExit.height() - 3,
			Terrain.INACTIVE_TRAP );

		for (int cell : roomExit.getCells()){
			if (map[cell] == Terrain.INACTIVE_TRAP){
				Trap t = new PoisonTrap().reveal();
				t.active = false;
				setTrap(t, cell);
			}
		}
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {

		int keyPos = anteroom.random();
		while (!passable[keyPos]) {
			keyPos = anteroom.random();
		}
		drop( new IronKey( Dungeon.depth ), keyPos ).type = Heap.Type.CHEST;
		
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = roomEntrance.random();
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}

	@Override
	public void press( int cell, Char ch ) {
		
		super.press( cell, ch );
		
		if (ch == Dungeon.hero && !enteredArena && roomExit.inside( cell )) {
			
			enteredArena = true;
			seal();
		
			int pos;
			do {
				pos = roomExit.random();
			} while (pos == cell || Actor.findChar( pos ) != null);
			
			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.HUNTING;
			boss.pos = pos;
			GameScene.add( boss );
			boss.notice();
			
			mobPress( boss );
			
			set( arenaDoor, Terrain.LOCKED_DOOR );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();

		}
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			
			keyDropped = true;
			unseal();
			
			set( arenaDoor, Terrain.DOOR );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();
		}
		
		return super.drop( item, cell );
	}
	
	@Override
	public int randomRespawnCell() {
		return roomEntrance.random();
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Dark cold water.";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "There are old blood stains on the floor.";
		default:
			return super.tileDesc( tile );
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		PrisonLevel.addVisuals( this, scene );
	}
}
