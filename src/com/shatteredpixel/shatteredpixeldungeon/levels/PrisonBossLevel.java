/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.MazePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SpearTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.CustomTileVisual;
import com.shatteredpixel.shatteredpixeldungeon.ui.HealthIndicator;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PrisonBossLevel extends Level {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	private enum State{
		START,
		FIGHT_START,
		MAZE,
		FIGHT_ARENA,
		WON
	}
	
	private State state;
	private Tengu tengu;

	//keep track of that need to be removed as the level is changed. We dump 'em back into the level at the end.
	private ArrayList<Item> storedItems = new ArrayList<>();
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}
	
	private static final String STATE	        = "state";
	private static final String TENGU	        = "tengu";
	private static final String STORED_ITEMS    = "storeditems";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( STATE, state );
		bundle.put( TENGU, tengu );
		bundle.put( STORED_ITEMS, storedItems);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		state = bundle.getEnum( STATE, State.class );

		//in some states tengu won't be in the world, in others he will be.
		if (state == State.START || state == State.MAZE) {
			tengu = (Tengu)bundle.get( TENGU );
		} else {
			for (Mob mob : mobs){
				if (mob instanceof Tengu) {
					tengu = (Tengu) mob;
					break;
				}
			}
		}

		for (Bundlable item : bundle.getCollection(STORED_ITEMS)){
			storedItems.add( (Item)item );
		}
	}
	
	@Override
	protected boolean build() {
		
		map = MAP_START.clone();
		decorate();

		buildFlagMaps();
		cleanWalls();

		state = State.START;
		entrance = 5+2*32;
		exit = 0;

		resetTraps();

		return true;
	}

	@Override
	protected void decorate() {
		//do nothing, all decorations are hard-coded.
	}

	@Override
	protected void createMobs() {
		tengu = new Tengu(); //We want to keep track of tengu independently of other mobs, he's not always in the level.
	}
	
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomRespawnCell() ).type = Heap.Type.REMAINS;
		}
		drop(new IronKey(10), randomPrisonCell());
	}

	private int randomPrisonCell(){
		int pos = 1+8*32; //initial position at top-left room

		//randomly assign a room.
		pos += Random.Int(4)*(4*32); //one of the 4 rows
		pos += Random.Int(2)*6; // one of the 2 columns

		//and then a certain tile in that room.
		pos += Random.Int(3) + Random.Int(3)*32;

		return pos;
	}

	@Override
	public void press( int cell, Char ch ) {

		super.press(cell, ch);

		if (ch == Dungeon.hero){
			//hero enters tengu's chamber
			if (state == State.START
					&& ((Room)new Room().set(2, 25, 8, 32)).inside(cell)){
				progress();
			}

			//hero finishes the maze
			else if (state == State.MAZE
					&& ((Room)new Room().set(4, 1, 7, 4)).inside(cell)){
				progress();
			}
		}
	}

	@Override
	public int randomRespawnCell() {
		return 5+2*32 + NEIGHBOURS8[Random.Int(8)]; //random cell adjacent to the entrance.
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(PrisonLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	private void resetTraps(){
		for (Trap trap : traps.values()){
			trap.sprite.kill();
		}
		traps.clear();

		for (int i = 0; i < Level.LENGTH; i++){
			if (map[i] == Terrain.INACTIVE_TRAP) {
				Trap t = new SpearTrap().reveal();
				t.active = false;
				setTrap(t, i);
				map[i] = Terrain.INACTIVE_TRAP;
			}
		}
	}

	private void changeMap(int[] map){
		this.map = map.clone();
		GameScene.resetMap();
		buildFlagMaps();
		cleanWalls();

		exit = entrance = 0;
		for (int i = 0; i < LENGTH; i ++)
			if (map[i] == Terrain.ENTRANCE)
				entrance = i;
			else if (map[i] == Terrain.EXIT)
				exit = i;

		visited = mapped = new boolean[LENGTH];
		for (Blob blob: blobs.values()){
			blob.fullyClear();
		}
		addVisuals(); //this also resets existing visuals
		resetTraps();

		Dungeon.observe();
	}

	private void clearEntities(Room safeArea){
		for (Heap heap : heaps.values()){
			if (safeArea == null || !safeArea.inside(heap.pos)){
				for (Item item : heap.items)
					storedItems.add(item);
				heap.destroy();
			}
		}
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])){
			if (mob != tengu && (safeArea == null || !safeArea.inside(mob.pos))){
				mob.destroy();
			}
		}
		for (Plant plant : plants.values()){
			if (safeArea == null || !safeArea.inside(plant.pos)){
				plants.remove(plant.pos);
				plant.sprite.kill();
			}
		}
	}

	public void progress(){
		switch (state){
			//moving to the beginning of the fight
			case START:
				seal();
				set(5 + 25 * 32, Terrain.LOCKED_DOOR);
				GameScene.updateMap(5 + 25 * 32);

				tengu.state = tengu.HUNTING;
				tengu.pos = 5 + 28*32; //in the middle of the fight room
				GameScene.add( tengu );
				tengu.notice();

				state = State.FIGHT_START;
				break;

			//halfway through, move to the maze
			case FIGHT_START:

				changeMap(MAP_MAZE);
				clearEntities((Room) new Room().set(0, 5, 8, 32)); //clear all but the entrance

				Actor.remove(tengu);
				mobs.remove(tengu);
				HealthIndicator.instance.target(null);
				tengu.sprite.kill();

				Room maze = new Room();
				maze.set(10, 1, 31, 29);
				maze.connected.put(null, new Room.Door(10, 2));
				maze.connected.put(maze, new Room.Door(20, 29));
				MazePainter.paint(this, maze);
				GameScene.resetMap();
				buildFlagMaps();
				cleanWalls();

				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.SND_BLAST);

				state = State.MAZE;
				break;

			//maze beaten, moving to the arena
			case MAZE:
				Dungeon.hero.interrupt();
				Dungeon.hero.pos += 9+3*32;
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);

				changeMap(MAP_ARENA);
				clearEntities(null);

				tengu.state = tengu.HUNTING;
				do {
					tengu.pos = Random.Int(LENGTH);
				} while (solid[tengu.pos] || distance(tengu.pos, Dungeon.hero.pos) < 8);
				GameScene.add(tengu);
				tengu.notice();

				state = State.FIGHT_ARENA;
				break;

			//arena ended, fight over.
			case FIGHT_ARENA:
				unseal();

				CustomTileVisual vis = new exitVisual();
				vis.pos(7, 7);
				customTiles.add(vis);
				((GameScene)ShatteredPixelDungeon.scene()).addCustomTile(vis);

				Dungeon.hero.interrupt();
				Dungeon.hero.pos = 5+27*32;
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);

				tengu.pos = 5+28*32;
				tengu.sprite.place(5 + 28 * 32);

				changeMap(MAP_END);
				clearEntities(null);

				tengu.die(Dungeon.hero);

				for (Item item : storedItems)
					drop(item, randomPrisonCell());

				state = State.WON;
				break;
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		PrisonLevel.addPrisonVisuals(this, visuals);
		return visuals;
	}

	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int _ = Terrain.EMPTY; //for readability
	private static final int S = Terrain.SIGN;

	private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	private static final int[] MAP_START =
			{       W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, E, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, S, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, M, W, L, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_MAZE =
			{       W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, _, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					_, _, _, D, _, _, _, D, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, W, _, _, _, W, W, M, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, W, W, M, W, W, W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, W, W, D, W, W, W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, W, W, _, W, W, W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, M, W, W, _, W, W, M, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, D, _, D, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, M, W, W, _, W, W, M, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, D, _, D, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, M, W, W, _, W, W, M, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, D, _, D, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, M, W, W, _, W, W, M, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, D, _, D, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, M, W, W, _, W, W, M, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, W, W, _, W, W, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, M, W, D, W, M, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, T, T, T, T, T, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, T, T, T, T, T, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, T, T, T, T, T, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W,
					W, W, W, T, T, T, T, T, W, _, W, W, W, W, W, W, W, W, W, W, _, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_ARENA =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W,
					W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W, W,
					W, _, _, _, _, _, W, _, _, _, _, _, W, W, M, W, W, _, _, _, _, _, W, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, W, _, _, _, _, W, W, _, _, _, W, W, _, _, _, _, W, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, M, W, _, _, _, _, _, D, _, _, _, D, _, _, _, _, _, W, M, _, _, _, _, W, W, W, W,
					W, _, _, W, W, W, W, _, _, _, _, W, W, _, _, _, W, W, _, _, _, _, W, W, W, W, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, _, _, _, W, W, M, W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, _, W, _, _, _, _, _, _, _, W, _, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, W, W, _, _, _, _, _, _, _, W, W, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, _, W, _, _, _, W, W, W, W,
					W, _, _, W, W, D, W, W, _, _, _, _, W, _, _, _, W, _, _, _, _, W, W, D, W, W, _, _, W, W, W, W,
					W, _, W, W, _, _, _, W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, _, _, _, W, W, _, W, W, W, W,
					W, _, W, M, _, _, _, M, W, _, _, _, _, _, M, _, _, _, _, _, W, M, _, _, _, M, W, _, W, W, W, W,
					W, _, W, W, _, _, _, W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, _, _, _, W, W, _, W, W, W, W,
					W, _, _, W, W, D, W, W, _, _, _, _, W, _, _, _, W, _, _, _, _, W, W, D, W, W, _, _, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, _, W, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, W, W, _, _, _, _, _, _, _, W, W, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, _, W, _, _, _, _, _, _, _, W, _, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, _, _, _, _, _, _, W, W, M, W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W,
					W, _, _, W, W, W, W, _, _, _, _, W, W, _, _, _, W, W, _, _, _, _, W, W, W, W, _, _, W, W, W, W,
					W, _, _, _, _, M, W, _, _, _, _, _, D, _, _, _, D, _, _, _, _, _, W, M, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, W, _, _, _, _, W, W, _, _, _, W, W, _, _, _, _, W, _, _, _, _, _, W, W, W, W,
					W, _, _, _, _, _, W, _, _, _, _, _, W, W, M, W, W, _, _, _, _, _, W, _, _, _, _, _, W, W, W, W,
					W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, W, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W, W,
					W, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_END =
			{       W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, E, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, S, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, _, W, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, _, _, _, _, _, _, _, _, _, _, _, _, _, X, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, W, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, _, W, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, W, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, D, _, D, _, _, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, _, _, _, W, _, W, _, _, _, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, _, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, _, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, M, W, D, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, P, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};


	public static class exitVisual extends CustomTileVisual{

		{
			name = "prison exit";

			tx = Assets.PRISON_EXIT;
			txX = txY = 0;
			tileW = tileH = 16;
		}

		@Override
		public String desc() {
			return super.desc();
		}
	}
}
