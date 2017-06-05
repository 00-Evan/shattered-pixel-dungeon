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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yog;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Group;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class HallsBossLevel extends Level {
	
	{
		color1 = 0x801500;
		color2 = 0xa68521;
		
		viewDistance = 4;
	}

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;

	private static final int ROOM_LEFT		= WIDTH / 2 - 1;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
	private static final int ROOM_TOP		= HEIGHT / 2 - 1;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 1;
	
	private int stairs = -1;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}
	
	private static final String STAIRS	= "stairs";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "droppped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STAIRS, stairs );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stairs = bundle.getInt( STAIRS );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}
	
	@Override
	protected boolean build() {
		
		setSize(32, 32);
		
		for (int i = 0; i < 5; i++) {
			
			int top = Random.IntRange(2, ROOM_TOP - 1);
			int bottom = Random.IntRange(ROOM_BOTTOM + 1, 22);
			Painter.fill(this, 2 + i * 4, top, 4, bottom - top + 1, Terrain.EMPTY);
			
			if (i == 2) {
				exit = (i * 4 + 3) + (top - 1) * width();
			}
			
			for (int j = 0; j < 4; j++) {
				if (Random.Int(2) == 0) {
					int y = Random.IntRange(top + 1, bottom - 1);
					map[i * 4 + j + y * width()] = Terrain.WALL_DECO;
				}
			}
		}
		
		map[exit] = Terrain.LOCKED_EXIT;
		
		Painter.fill(this, ROOM_LEFT - 1, ROOM_TOP - 1,
				ROOM_RIGHT - ROOM_LEFT + 3, ROOM_BOTTOM - ROOM_TOP + 3, Terrain.WALL);
		Painter.fill(this, ROOM_LEFT, ROOM_TOP,
				ROOM_RIGHT - ROOM_LEFT + 1, ROOM_BOTTOM - ROOM_TOP + 1, Terrain.EMPTY);
		
		entrance = Random.Int(ROOM_LEFT + 1, ROOM_RIGHT - 1) +
				Random.Int(ROOM_TOP + 1, ROOM_BOTTOM - 1) * width();
		map[entrance] = Terrain.ENTRANCE;
		
		boolean[] patch = Patch.generate(width, height, 0.30f, 6, true);
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}
		
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(10) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			}
		}
		
		return true;
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
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * width();
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell() {
		int pos = entrance == -1 ? stairs : entrance;
		int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]){
			cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if (!enteredArena && hero == Dungeon.hero && cell != entrance) {
			
			enteredArena = true;
			seal();
			
			for (int i=ROOM_LEFT-1; i <= ROOM_RIGHT + 1; i++) {
				doMagic( (ROOM_TOP - 1) * width() + i );
				doMagic( (ROOM_BOTTOM + 1) * width() + i );
			}
			for (int i=ROOM_TOP; i < ROOM_BOTTOM + 1; i++) {
				doMagic( i * width() + ROOM_LEFT - 1 );
				doMagic( i * width() + ROOM_RIGHT + 1 );
			}
			doMagic( entrance );
			GameScene.updateMap();

			Dungeon.observe();
			
			Yog boss = new Yog();
			do {
				boss.pos = Random.Int( length() );
			} while (
				!passable[boss.pos] ||
				Dungeon.visible[boss.pos]);
			GameScene.add( boss );
			boss.spawnFists();
			
			stairs = entrance;
			entrance = -1;
		}
	}
	
	private void doMagic( int cell ) {
		set( cell, Terrain.EMPTY_SP );
		CellEmitter.get( cell ).start( FlameParticle.FACTORY, 0.1f, 3 );
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			keyDropped = true;
			unseal();
			
			entrance = stairs;
			set( entrance, Terrain.ENTRANCE );
			GameScene.updateMap( entrance );
		}
		
		return super.drop( item, cell );
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals( this, visuals );
		return visuals;
	}
}
