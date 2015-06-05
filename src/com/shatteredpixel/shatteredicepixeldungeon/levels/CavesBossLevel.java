/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.levels;

import com.watabou.noosa.Camera;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Bones;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredicepixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class CavesBossLevel extends Level {
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		
		viewDistance = 6;
	}
	
	private static final int ROOM_LEFT		= WIDTH / 2 - 2;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 2;
	private static final int ROOM_TOP		= HEIGHT / 2 - 2;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;
	
	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	private static final String DOOR	= "door";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "droppped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DOOR, arenaDoor );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		arenaDoor = bundle.getInt( DOOR );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}
	
	@Override
	protected boolean build() {
		
		int topMost = Integer.MAX_VALUE;
		
		for (int i=0; i < 8; i++) {
			int left, right, top, bottom;
			if (Random.Int( 2 ) == 0) {
				left = Random.Int( 1, ROOM_LEFT - 3 );
				right = ROOM_RIGHT + 3;
			} else {
				left = ROOM_LEFT - 3;
				right = Random.Int( ROOM_RIGHT + 3, WIDTH - 1 );
			}
			if (Random.Int( 2 ) == 0) {
				top = Random.Int( 2, ROOM_TOP - 3 );
				bottom = ROOM_BOTTOM + 3;
			} else {
				top = ROOM_LEFT - 3;
				bottom = Random.Int( ROOM_TOP + 3, HEIGHT - 1 );
			}
			
			Painter.fill( this, left, top, right - left + 1, bottom - top + 1, Terrain.EMPTY );
			
			if (top < topMost) {
				topMost = top;
				exit = Random.Int( left, right ) + (top - 1) * WIDTH;
			}
		}
		
		map[exit] = Terrain.LOCKED_EXIT;
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 6 ) == 0) {
				map[i] = Terrain.INACTIVE_TRAP;
			}
		}
		
		Painter.fill( this, ROOM_LEFT - 1, ROOM_TOP - 1, 
			ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP + 1, 
			ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP, Terrain.EMPTY );
		
		Painter.fill( this, ROOM_LEFT, ROOM_TOP, 
			ROOM_RIGHT - ROOM_LEFT + 1, 1, Terrain.TOXIC_TRAP );
		
		arenaDoor = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + (ROOM_BOTTOM + 1) * WIDTH;
		map[arenaDoor] = Terrain.DOOR;
		
		entrance = Random.Int( ROOM_LEFT + 1, ROOM_RIGHT - 1 ) + 
			Random.Int( ROOM_TOP + 1, ROOM_BOTTOM - 1 ) * WIDTH;
		map[entrance] = Terrain.ENTRANCE;
		
		boolean[] patch = Patch.generate( 0.45f, 6 );
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}
		
		return true;
	}
	
	@Override
	protected void decorate() {	
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i+1] == Terrain.WALL) {
					n++;
				}
				if (map[i-1] == Terrain.WALL) {
					n++;
				}
				if (map[i+WIDTH] == Terrain.WALL) {
					n++;
				}
				if (map[i-WIDTH] == Terrain.WALL) {
					n++;
				}
				if (Random.Int( 8 ) <= n) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		int sign;
		do {
			sign = Random.Int( ROOM_LEFT, ROOM_RIGHT ) + Random.Int( ROOM_TOP, ROOM_BOTTOM ) * WIDTH;
		} while (sign == entrance);
		map[sign] = Terrain.SIGN;
	}
	
	@Override
	protected void createMobs() {	
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * WIDTH;
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell() {
		return -1;
	}
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if (!enteredArena && outsideEntraceRoom( cell ) && hero == Dungeon.hero) {
			
			enteredArena = true;
            locked = true;
			
			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.HUNTING;
			do {
				boss.pos = Random.Int( LENGTH );
			} while (
				!passable[boss.pos] ||
				!outsideEntraceRoom( boss.pos ) ||
				Dungeon.visible[boss.pos]);
			GameScene.add( boss );
			
			set( arenaDoor, Terrain.WALL );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();
			
			CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
		}
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			
			keyDropped = true;
            locked = false;
			
			CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			
			set( arenaDoor, Terrain.EMPTY_DECO );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();
		}
		
		return super.drop( item, cell );
	}
	
	private boolean outsideEntraceRoom( int cell ) {
		int cx = cell % WIDTH;
		int cy = cell / WIDTH;
		return cx < ROOM_LEFT-1 || cx > ROOM_RIGHT+1 || cy < ROOM_TOP-1 || cy > ROOM_BOTTOM+1;
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.GRASS:
			return "Fluorescent moss";
		case Terrain.HIGH_GRASS:
			return "Fluorescent mushrooms";
		case Terrain.WATER:
			return "Freezing cold water.";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
		case Terrain.ENTRANCE:
			return "The ladder leads up to the upper depth.";
		case Terrain.EXIT:
			return "The ladder leads down to the lower depth.";
		case Terrain.HIGH_GRASS:
			return "Huge mushrooms block the view.";
		case Terrain.WALL_DECO:
			return "A vein of some ore is visible on the wall. Gold?";
		default:
			return super.tileDesc( tile );
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		CavesLevel.addVisuals( this, scene );
	}
}
