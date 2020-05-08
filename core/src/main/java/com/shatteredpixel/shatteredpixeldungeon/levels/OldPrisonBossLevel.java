/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldTengu;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.MazeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//Exists to support pre-0.7.5 saves
public class OldPrisonBossLevel extends Level {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	public enum State {
		START,
		FIGHT_START,
		MAZE,
		FIGHT_ARENA,
		WON
	}
	
	private static final int ARENA_CENTER = 5+28*32;
	private static final int ARENA_DOOR = 5+25*32;
	
	private State state;
	private OldTengu tengu;
	
	public State state(){
		return state;
	}

	//keep track of that need to be removed as the level is changed. We dump 'em back into the level at the end.
	private ArrayList<Item> storedItems = new ArrayList<>();
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_PRISON;
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
			tengu = (OldTengu)bundle.get( TENGU );
		} else {
			for (Mob mob : mobs){
				if (mob instanceof OldTengu) {
					tengu = (OldTengu) mob;
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
		
		setSize(32, 32);
		
		map = MAP_START.clone();

		buildFlagMaps();
		cleanWalls();

		state = State.START;
		entrance = 5+2*32;
		exit = 0;

		resetTraps();

		return true;
	}
	
	@Override
	protected void createMobs() {
		tengu = new OldTengu(); //We want to keep track of tengu independently of other mobs, he's not always in the level.
	}
	
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomRespawnCell( null ) ).setHauntedIfCursed().type = Heap.Type.REMAINS;
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
	
	private int randomTenguArenaCell(){
		int pos = ARENA_CENTER - 2 - (2*32);//initial position at top-left of room
		
		pos += Random.Int(5)*32;
		pos += Random.Int(5);
		
		//cannot choose the center
		if (pos == ARENA_CENTER)    return randomTenguArenaCell();
		else                        return pos;
	}

	@Override
	public void occupyCell( Char ch ) {
		
		super.occupyCell( ch );

		if (ch == Dungeon.hero){
			//hero enters tengu's chamber
			if (state == State.START
					&& (new EmptyRoom().set(2, 25, 8, 32)).inside(cellToPoint(ch.pos))){
				progress();
			}

			//hero finishes the maze
			else if (state == State.MAZE
					&& (new EmptyRoom().set(4, 0, 7, 4)).inside(cellToPoint(ch.pos))){
				progress();
			}
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int pos = 5+2*32; //random cell adjacent to the entrance.
		int cell;
		do {
			cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
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
		traps.clear();

		for (int i = 0; i < length(); i++){
			if (map[i] == Terrain.INACTIVE_TRAP) {
				Trap t = new GrippingTrap().reveal();
				t.active = false;
				setTrap(t, i);
				map[i] = Terrain.INACTIVE_TRAP;
			}
		}
	}

	private void changeMap(int[] map){
		this.map = map.clone();
		buildFlagMaps();
		cleanWalls();

		exit = entrance = 0;
		for (int i = 0; i < length(); i ++)
			if (map[i] == Terrain.ENTRANCE)
				entrance = i;
			else if (map[i] == Terrain.EXIT)
				exit = i;

		BArray.setFalse(visited);
		BArray.setFalse(mapped);
		
		for (Blob blob: blobs.values()){
			blob.fullyClear();
		}
		addVisuals(); //this also resets existing visuals
		resetTraps();


		GameScene.resetMap();
		Dungeon.observe();
	}

	private void clearEntities(Room safeArea){
		for (Heap heap : heaps.valueList()){
			if (safeArea == null || !safeArea.inside(cellToPoint(heap.pos))){
				storedItems.addAll(heap.items);
				heap.destroy();
			}
		}
		
		for (HeavyBoomerang.CircleBack b : Dungeon.hero.buffs(HeavyBoomerang.CircleBack.class)){
			if (safeArea == null || !safeArea.inside(cellToPoint(b.returnPos()))){
				storedItems.add(b.cancel());
			}
		}
		
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
			if (mob != tengu && (safeArea == null || !safeArea.inside(cellToPoint(mob.pos)))){
				mob.destroy();
				if (mob.sprite != null)
					mob.sprite.killAndErase();
			}
		}
		for (Plant plant : plants.valueList()){
			if (safeArea == null || !safeArea.inside(cellToPoint(plant.pos))){
				plants.remove(plant.pos);
			}
		}
	}

	public void progress(){
		switch (state){
			//moving to the beginning of the fight
			case START:
				
				//if something is occupying Tengu's space, wait and do nothing.
				if (Actor.findChar(ARENA_CENTER) != null){
					return;
				}
				
				seal();
				set(ARENA_DOOR, Terrain.LOCKED_DOOR);
				GameScene.updateMap(ARENA_DOOR);

				for (Mob m : mobs){
					//bring the first ally with you
					if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
						m.pos = ARENA_DOOR; //they should immediately walk out of the door
						m.sprite.place(m.pos);
						break;
					}
				}
				
				tengu.state = tengu.HUNTING;
				tengu.pos = ARENA_CENTER; //in the middle of the fight room
				GameScene.add( tengu );
				tengu.notice();

				state = State.FIGHT_START;
				break;

			//halfway through, move to the maze
			case FIGHT_START:

				changeMap(MAP_MAZE);
				clearEntities((Room) new EmptyRoom().set(0, 5, 8, 32)); //clear the entrance

				Actor.remove(tengu);
				mobs.remove(tengu);
				TargetHealthIndicator.instance.target(null);
				tengu.sprite.kill();

				Room maze = new MazeRoom();
				maze.set(10, 1, 31, 29);
				maze.connected.put(null, new Room.Door(10, 2));
				maze.connected.put(maze, new Room.Door(20, 29));
				maze.paint(this);
				buildFlagMaps();
				cleanWalls();
				GameScene.resetMap();

				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);

				state = State.MAZE;
				break;

			//maze beaten, moving to the arena
			case MAZE:
				Dungeon.hero.interrupt();
				Dungeon.hero.pos += 9+3*32;
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);

				changeMap(MAP_ARENA);
				clearEntities( (Room) new EmptyRoom().set(0, 0, 10, 4)); //clear all but the area right around the teleport spot
				
				//if any allies are left over, move them along the same way as the hero
				for (Mob m : mobs){
					if (m.alignment == Char.Alignment.ALLY) {
						m.pos += 9 + 3 * 32;
						m.sprite().place(m.pos);
					}
				}

				tengu.state = tengu.HUNTING;
				do {
					tengu.pos = Random.Int(length());
				} while (solid[tengu.pos] || distance(tengu.pos, Dungeon.hero.pos) < 8);
				GameScene.add(tengu);
				tengu.notice();
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);

				state = State.FIGHT_ARENA;
				break;

			//arena ended, fight over.
			case FIGHT_ARENA:
				unseal();

				CustomTilemap vis = new exitVisual();
				vis.pos(11, 8);
				customTiles.add(vis);
				((GameScene)ShatteredPixelDungeon.scene()).addCustomTile(vis);

				vis = new exitVisualWalls();
				vis.pos(11, 8);
				customWalls.add(vis);
				((GameScene)ShatteredPixelDungeon.scene()).addCustomWall(vis);

				Dungeon.hero.interrupt();
				Dungeon.hero.pos = 5+27*32;
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);

				tengu.pos = ARENA_CENTER;
				tengu.sprite.place(ARENA_CENTER);
				
				//remove all mobs, but preserve allies
				ArrayList<Mob> allies = new ArrayList<>();
				for(Mob m : mobs.toArray(new Mob[0])){
					if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
						allies.add(m);
						mobs.remove(m);
					}
				}
				
				changeMap(MAP_END);
				
				for (Mob m : allies){
					do{
						m.pos = randomTenguArenaCell();
					} while (findMob(m.pos) != null);
					if (m.sprite != null) m.sprite.place(m.pos);
					mobs.add(m);
				}

				tengu.die(Dungeon.hero);
				
				clearEntities((Room) new EmptyRoom().set(2, 25, 8, 31)); //arena is safe

				for (Item item : storedItems)
					drop(item, randomTenguArenaCell());
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);
				
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
	private static final int e = Terrain.EMPTY;

	private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	//TODO if I ever need to store more static maps I should externalize them instead of hard-coding
	//Especially as I means I won't be limited to legal identifiers
	private static final int[] MAP_START =
			{       W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, M, W, L, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_MAZE =
			{       W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, e, D, e, e, e, D, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, W, e, e, e, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, W, W, e, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, W, W, D, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, M, W, W, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, W, W, e, W, W, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, W, W, e, W, W, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, M, W, D, W, M, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, T, T, T, T, T, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, T, T, T, T, T, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, T, T, T, T, T, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W,
					W, W, W, T, T, T, T, T, W, e, W, W, W, W, W, W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_ARENA =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
					W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, W, W, e, e, e, e, e, D, e, e, e, D, e, e, e, e, e, W, W, e, e, e, e, W, W, W, W,
					W, e, e, W, W, W, M, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, M, W, W, W, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, W, W, W, W,
					W, e, e, W, W, D, W, W, e, e, e, e, W, e, e, e, W, e, e, e, e, W, W, D, W, W, e, e, W, W, W, W,
					W, e, W, W, e, e, e, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
					W, e, W, W, e, e, e, W, W, e, e, e, e, e, M, e, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
					W, e, W, W, e, e, e, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, e, e, e, W, W, e, W, W, W, W,
					W, e, e, W, W, D, W, W, e, e, e, e, W, e, e, e, W, e, e, e, e, W, W, D, W, W, e, e, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, e, W, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, W, W, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, W, W, M, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W,
					W, e, e, W, W, W, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, W, W, W, e, e, W, W, W, W,
					W, e, e, e, e, M, W, e, e, e, e, e, D, e, e, e, D, e, e, e, e, e, W, M, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, W, W, e, e, e, W, W, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
					W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W,
					W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W,
					W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

	private static final int[] MAP_END =
			{       W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, E, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, D, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, M, e, W, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, e, e, e, e, e, e, e, e, e, e, e, e, e, X, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, M, W, W, e, W, W, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, W, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, D, e, D, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, e, W, e, e, e, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, e, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, M, W, D, W, M, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, P, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, T, T, T, T, T, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};


	public static class exitVisual extends CustomTilemap {
		
		{
			texture = Assets.Environment.PRISON_EXIT_OLD;
			
			tileW = 12;
			tileH = 14;
		}
		
		final int TEX_WIDTH = 256;
		
		private static short[] render = new short[]{
				0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
				0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
				0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
				1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		};
		
		@Override
		public Tilemap create() {
			
			Tilemap v = super.create();
			int[] data = mapSimpleImage(0, 0, TEX_WIDTH);
			for (int i = 0; i < data.length; i++){
				if (render[i] == 0) data[i] = -1;
			}
			
			v.map(data, tileW);
			return v;
		}

	}

	public static class exitVisualWalls extends CustomTilemap {
		
		{
			texture = Assets.Environment.PRISON_EXIT_OLD;
			
			tileW = 12;
			tileH = 14;
		}
		
		final int TEX_WIDTH = 256;
		
		private static short[] render = new short[]{
				0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
				0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0,
				0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		};

		@Override
		public Tilemap create() {
			
			Tilemap v = super.create();
			
			int[] data = mapSimpleImage(4, 0, TEX_WIDTH);
			for (int i = 0; i < data.length; i++){
				if (render[i] == 0) data[i] = -1;
			}
			
			v.map(data, tileW);
			return v;
		}

	}
}
