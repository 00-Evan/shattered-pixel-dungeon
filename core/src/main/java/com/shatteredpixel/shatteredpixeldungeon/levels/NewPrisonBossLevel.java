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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewTengu;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TenguDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class NewPrisonBossLevel extends Level {
	
	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
		
		//the player should be able to see all of Tengu's arena
		viewDistance = 12;
	}
	
	public enum State {
		START,
		FIGHT_START,
		TRAP_MAZES, //pre-0.8.1 saves
		FIGHT_PAUSE,
		FIGHT_ARENA,
		WON
	}
	
	private State state;
	private NewTengu tengu;
	
	public State state(){
		return state;
	}
	
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
	private static final String TRIGGERED       = "triggered";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( STATE, state );
		bundle.put( TENGU, tengu );
		bundle.put( STORED_ITEMS, storedItems);
		bundle.put(TRIGGERED, triggered );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		state = bundle.getEnum( STATE, State.class );
		
		//in some states tengu won't be in the world, in others he will be.
		if (state == State.START || state == State.TRAP_MAZES || state == State.FIGHT_PAUSE) {
			tengu = (NewTengu)bundle.get( TENGU );
		} else {
			for (Mob mob : mobs){
				if (mob instanceof NewTengu) {
					tengu = (NewTengu) mob;
					break;
				}
			}
		}
		
		for (Bundlable item : bundle.getCollection(STORED_ITEMS)){
			storedItems.add( (Item)item );
		}
		
		triggered = bundle.getBooleanArray(TRIGGERED);
		
	}
	
	@Override
	protected boolean build() {
		setSize(32, 32);
		
		state = State.START;
		setMapStart();
		
		return true;
	}
	
	private static final int ENTRANCE_POS = 10 + 4*32;
	private static final Rect entranceRoom = new Rect(8, 2, 13, 8);
	private static final Rect startHallway = new Rect(9, 7, 12, 24);
	private static final Rect[] startCells = new Rect[]{ new Rect(5, 9, 10, 16), new Rect(11, 9, 16, 16),
	                                         new Rect(5, 15, 10, 22), new Rect(11, 15, 16, 22)};
	private static final Rect tenguCell = new Rect(6, 23, 15, 32);
	private static final Point tenguCellCenter = new Point(10, 27);
	private static final Point tenguCellDoor = new Point(10, 23);
	private static final Point[] startTorches = new Point[]{ new Point(10, 2),
	                                       new Point(7, 9), new Point(13, 9),
	                                       new Point(7, 15), new Point(13, 15),
	                                       new Point(8, 23), new Point(12, 23)};
	
	private void setMapStart(){
		entrance = ENTRANCE_POS;
		exit = 0;
		
		Painter.fill(this, 0, 0, 32, 32, Terrain.WALL);
		
		//Start
		Painter.fill(this, entranceRoom, Terrain.WALL);
		Painter.fill(this, entranceRoom, 1, Terrain.EMPTY);
		Painter.set(this, entrance, Terrain.ENTRANCE);
		
		Painter.fill(this, startHallway, Terrain.WALL);
		Painter.fill(this, startHallway, 1, Terrain.EMPTY);
		
		Painter.set(this, startHallway.left+1, startHallway.top, Terrain.DOOR);
		
		for (Rect r : startCells){
			Painter.fill(this, r, Terrain.WALL);
			Painter.fill(this, r, 1, Terrain.EMPTY);
		}
		
		Painter.set(this, startHallway.left, startHallway.top+5, Terrain.DOOR);
		Painter.set(this, startHallway.right-1, startHallway.top+5, Terrain.DOOR);
		Painter.set(this, startHallway.left, startHallway.top+11, Terrain.DOOR);
		Painter.set(this, startHallway.right-1, startHallway.top+11, Terrain.DOOR);
		
		Painter.fill(this, tenguCell, Terrain.WALL);
		Painter.fill(this, tenguCell, 1, Terrain.EMPTY);
		
		Painter.set(this, tenguCell.left+4, tenguCell.top, Terrain.LOCKED_DOOR);
		
		for (Point p : startTorches){
			Painter.set(this, p, Terrain.WALL_DECO);
		}
	}

	private void setMapPause(){
		setMapStart();

		exit = entrance = 0;

		Painter.set(this, tenguCell.left+4, tenguCell.top, Terrain.DOOR);

		Painter.fill(this, startCells[1].left, startCells[1].top+3, 1, 7, Terrain.EMPTY);
		Painter.fill(this, startCells[1].left+2, startCells[1].top+2, 3, 10, Terrain.EMPTY);

		Painter.fill(this, entranceRoom, Terrain.WALL);
		Painter.set(this, startHallway.left+1, startHallway.top, Terrain.EMPTY);
		Painter.set(this, startHallway.left+1, startHallway.top+1, Terrain.DOOR);

	}
	
	private static final Rect arena = new Rect(3, 1, 18, 16);
	
	private void setMapArena(){
		exit = entrance = 0;
		
		Painter.fill(this, 0, 0, 32, 32, Terrain.WALL);
		
		Painter.fill(this, arena, Terrain.WALL);
		Painter.fillEllipse(this, arena, 1, Terrain.EMPTY);
	
	}
	
	private static int W = Terrain.WALL;
	private static int D = Terrain.WALL_DECO;
	private static int e = Terrain.EMPTY;
	private static int E = Terrain.EXIT;
	private static int C = Terrain.CHASM;
	
	private static final Point endStart = new Point( startHallway.left+2, startHallway.top+2);
	private static final Point levelExit = new Point( endStart.x+12, endStart.y+6);
	private static final int[] endMap = new int[]{
			W, W, D, W, W, W, W, W, W, W, W, W, W, W,
			W, e, e, e, W, W, W, W, W, W, W, W, W, W,
			W, e, e, e, e, e, e, e, e, W, W, W, W, W,
			e, e, e, e, e, e, e, e, e, e, e, e, W, W,
			e, e, e, e, e, e, e, e, e, e, e, e, e, W,
			e, e, e, C, C, C, C, C, C, C, C, e, e, W,
			e, W, C, C, C, C, C, C, C, C, C, E, E, W,
			e, e, e, C, C, C, C, C, C, C, C, E, E, W,
			e, e, e, e, e, C, C, C, C, C, C, E, E, W,
			e, e, e, e, e, e, e, W, W, W, C, C, C, W,
			W, e, e, e, e, e, W, W, W, W, C, C, C, W,
			W, e, e, e, e, W, W, W, W, W, W, C, C, W,
			W, W, W, W, W, W, W, W, W, W, W, C, C, W,
			W, W, W, W, W, W, W, W, W, W, W, C, C, W,
			W, D, W, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			e, e, e, W, W, W, W, W, W, W, W, C, C, W,
			W, W, W, W, W, W, W, W, W, W, W, C, C, W
	};
	
	private void setMapEnd(){
		
		Painter.fill(this, 0, 0, 32, 32, Terrain.WALL);
		
		setMapStart();
		
		for (Heap h : heaps.valueList()){
			if (h.peek() instanceof IronKey){
				h.destroy();
			}
		}
		
		CustomTilemap vis = new exitVisual();
		vis.pos(11, 10);
		customTiles.add(vis);
		GameScene.add(vis, false);
		
		vis = new exitVisualWalls();
		vis.pos(11, 10);
		customWalls.add(vis);
		GameScene.add(vis, true);
		
		Painter.set(this, tenguCell.left+4, tenguCell.top, Terrain.DOOR);
		
		int cell = pointToCell(endStart);
		int i = 0;
		while (cell < length()){
			System.arraycopy(endMap, i, map, cell, 14);
			i += 14;
			cell += width();
		}
		
		exit = pointToCell(levelExit);
	}
	
	//keep track of removed items as the level is changed. Dump them back into the level at the end.
	private ArrayList<Item> storedItems = new ArrayList<>();
	
	private void clearEntities(Rect safeArea){
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
	
	private void cleanMapState(){
		buildFlagMaps();
		cleanWalls();
		
		BArray.setFalse(visited);
		BArray.setFalse(mapped);
		
		for (Blob blob: blobs.values()){
			blob.fullyClear();
		}
		addVisuals(); //this also resets existing visuals
		traps.clear();
		
		GameScene.resetMap();
		Dungeon.observe();
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		PrisonLevel.addPrisonVisuals(this, visuals);
		return visuals;
	}
	
	public void progress(){
		switch (state){
			case START:
				
				int tenguPos = pointToCell(tenguCellCenter);
				
				//if something is occupying Tengu's space, try to put him in an adjacent cell
				if (Actor.findChar(tenguPos) != null){
					ArrayList<Integer> candidates = new ArrayList<>();
					for (int i : PathFinder.NEIGHBOURS8){
						if (Actor.findChar(tenguPos + i) == null){
							candidates.add(tenguPos + i);
						}
					}
					
					if (!candidates.isEmpty()){
						tenguPos = Random.element(candidates);
					//if there are no adjacent cells, wait and do nothing
					} else {
						return;
					}
				}
				
				seal();
				set(pointToCell(tenguCellDoor), Terrain.LOCKED_DOOR);
				GameScene.updateMap(pointToCell(tenguCellDoor));
				
				for (Mob m : mobs){
					//bring the first ally with you
					if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
						m.pos = pointToCell(tenguCellDoor); //they should immediately walk out of the door
						m.sprite.place(m.pos);
						break;
					}
				}
				
				tengu.state = tengu.HUNTING;
				tengu.pos = tenguPos;
				GameScene.add( tengu );
				tengu.notice();
				
				state = State.FIGHT_START;
				break;
				
			case FIGHT_START:
				
				clearEntities( tenguCell ); //clear anything not in tengu's cell
				
				setMapPause();
				cleanMapState();

				Doom d = tengu.buff(Doom.class);
				Actor.remove(tengu);
				mobs.remove(tengu);
				TargetHealthIndicator.instance.target(null);
				tengu.sprite.kill();
				if (d != null) tengu.add(d);
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);
				
				state = State.FIGHT_PAUSE;
				break;

			case TRAP_MAZES: //for pre-0.8.1 saves
			case FIGHT_PAUSE:
				
				Dungeon.hero.interrupt();
				
				clearEntities( arena ); //clear anything not in the arena
				
				setMapArena();
				cleanMapState();
				
				tengu.state = tengu.HUNTING;
				tengu.pos = (arena.left + arena.width()/2) + width()*(arena.top+2);
				GameScene.add(tengu);
				tengu.notice();
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);
				
				state = State.FIGHT_ARENA;
				break;
				
			case FIGHT_ARENA:
				
				unseal();
				
				Dungeon.hero.interrupt();
				Dungeon.hero.pos = tenguCell.left+4 + (tenguCell.top+2)*width();
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);
				Camera.main.snapTo(Dungeon.hero.sprite.center());
				
				tengu.pos = pointToCell(tenguCellCenter);
				tengu.sprite.place(tengu.pos);
				
				//remove all mobs, but preserve allies
				ArrayList<Mob> allies = new ArrayList<>();
				for(Mob m : mobs.toArray(new Mob[0])){
					if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
						allies.add(m);
						mobs.remove(m);
					}
				}
				
				setMapEnd();
				
				for (Mob m : allies){
					do{
						m.pos = randomTenguCellPos();
					} while (findMob(m.pos) != null);
					if (m.sprite != null) m.sprite.place(m.pos);
					mobs.add(m);
				}
				
				tengu.die(Dungeon.hero);
				
				clearEntities(tenguCell);
				cleanMapState();
				
				for (Item item : storedItems) {
					if (!(item instanceof NewTengu.BombAbility.BombItem)
						&& !(item instanceof NewTengu.ShockerAbility.ShockerItem)) {
						drop(item, randomTenguCellPos());
					}
				}
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.Sounds.BLAST);
				
				state = State.WON;
				break;
		}
	}
	
	private boolean[] triggered = new boolean[]{false, false, false, false};
	
	@Override
	public void occupyCell(Char ch) {
		super.occupyCell(ch);
		
		if (ch == Dungeon.hero){
			switch (state){
				case START:
					if (cellToPoint(ch.pos).y > tenguCell.top){
						progress();
					}
					break;
				case TRAP_MAZES: //pre-0.8.1
				case FIGHT_PAUSE:
					
					if (cellToPoint(ch.pos).y <= startHallway.top+1){
						progress();
					}
					break;
			}
		}
	}
	
	@Override
	protected void createMobs() {
		tengu = new NewTengu(); //We want to keep track of tengu independently of other mobs, he's not always in the level.
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
		drop(new IronKey(10), randomPrisonCellPos());
	}
	
	private int randomPrisonCellPos(){
		Rect room = startCells[Random.Int(startCells.length)];
		
		return Random.IntRange(room.left+1, room.right-2)
				+ width()*Random.IntRange(room.top+1, room.bottom-2);
	}
	
	public int randomTenguCellPos(){
		return Random.IntRange(tenguCell.left+1, tenguCell.right-2)
				+ width()*Random.IntRange(tenguCell.top+1, tenguCell.bottom-2);
	}
	
	public void cleanTenguCell(){
		
		traps.clear();
		Painter.fill(this, tenguCell, 1, Terrain.EMPTY);
		buildFlagMaps();
		
	}
	
	public void placeTrapsInTenguCell(float fill){

		for (CustomTilemap vis : customTiles){
			if (vis instanceof FadingTraps){
				((FadingTraps) vis).remove();
			}
		}
		
		Point tenguPoint = cellToPoint(tengu.pos);
		Point heroPoint = cellToPoint(Dungeon.hero.pos);
		
		PathFinder.setMapSize(7, 7);
		
		int tenguPos = tenguPoint.x-(tenguCell.left+1) + (tenguPoint.y-(tenguCell.top+1))*7;
		int heroPos = heroPoint.x-(tenguCell.left+1) + (heroPoint.y-(tenguCell.top+1))*7;
		
		boolean[] trapsPatch;
		
		do {
			trapsPatch = Patch.generate(7, 7, fill, 0, false);
			
			PathFinder.buildDistanceMap(tenguPos, BArray.not(trapsPatch, null));
			//note that the effective range of fill is 40%-90%
			//so distance to tengu starts at 3-6 tiles and scales up to 7-8 as fill increases
		} while (((PathFinder.distance[heroPos] < Math.ceil(7*fill))
				|| (PathFinder.distance[heroPos] > Math.ceil(4 + 4*fill))));

		PathFinder.setMapSize(width(), height());
		
		for (int i = 0; i < trapsPatch.length; i++){
			if (trapsPatch[i]) {
				int x = i % 7;
				int y = i / 7;
				int cell = x+tenguCell.left+1 + (y+tenguCell.top+1)*width();
				if (Blob.volumeAt(cell, StormCloud.class) == 0
						&& Blob.volumeAt(cell, Regrowth.class) <= 9
						&& Actor.findChar(cell) == null) {
					Level.set(cell, Terrain.SECRET_TRAP);
					setTrap(new TenguDartTrap().hide(), cell);
					CellEmitter.get(cell).burst(Speck.factory(Speck.LIGHT), 2);
				}
			}
		}
		
		GameScene.updateMap();
		
		FadingTraps t = new FadingTraps();
		t.fadeDelay = 2f;
		t.setCoveringArea(tenguCell);
		GameScene.add(t, false);
		customTiles.add(t);
	}
	
	@Override
	public int randomRespawnCell( Char ch ) {
		int pos = ENTRANCE_POS; //random cell adjacent to the entrance.
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
	
	//TODO consider making this external to the prison boss level
	public static class FadingTraps extends CustomTilemap {
		
		{
			texture = Assets.Environment.TERRAIN_FEATURES;
		}
		
		Rect area;
		
		private float fadeDuration = 1f;
		private float initialAlpha = .4f;
		private float fadeDelay = 1f;
		
		public void setCoveringArea(Rect area){
			tileX = area.left;
			tileY = area.top;
			tileH = area.bottom - area.top;
			tileW = area.right - area.left;
			
			this.area = area;
		}
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int cell;
			Trap t;
			int i = 0;
			for (int y = tileY; y < tileY + tileH; y++){
				cell = tileX + y*Dungeon.level.width();
				for (int x = tileX; x < tileX + tileW; x++){
					t = Dungeon.level.traps.get(cell);
					if (t != null){
						data[i] = t.color + t.shape*16;
					} else {
						data[i] = -1;
					}
					cell++;
					i++;
				}
			}
			
			v.map( data, tileW );
			setFade();
			return v;
		}
		
		@Override
		public String name(int tileX, int tileY) {
			int cell = (this.tileX+tileX) + Dungeon.level.width()*(this.tileY+tileY);
			if (Dungeon.level.traps.get(cell) != null){
				return Messages.titleCase(Dungeon.level.traps.get(cell).name);
			}
			return super.name(tileX, tileY);
		}
		
		@Override
		public String desc(int tileX, int tileY) {
			int cell = (this.tileX+tileX) + Dungeon.level.width()*(this.tileY+tileY);
			if (Dungeon.level.traps.get(cell) != null){
				return Dungeon.level.traps.get(cell).desc();
			}
			return super.desc(tileX, tileY);
		}
		
		private void setFade( ){
			if (vis == null){
				return;
			}
			
			vis.alpha( initialAlpha );
			Actor.addDelayed(new Actor() {
				
				{
					actPriority = HERO_PRIO+1;
				}
				
				@Override
				protected boolean act() {
					Actor.remove(this);
					
					if (vis != null && vis.parent != null) {
						Dungeon.level.customTiles.remove(FadingTraps.this);
						vis.parent.add(new AlphaTweener(vis, 0f, fadeDuration) {
							@Override
							protected void onComplete() {
								super.onComplete();
								vis.killAndErase();
								killAndErase();
							}
						});
					}
					
					return true;
				}
			}, fadeDelay);
		}

		private void remove(){
			if (vis != null){
				vis.killAndErase();
			}
			Dungeon.level.customTiles.remove(this);
		}
		
	}
	
	public static class exitVisual extends CustomTilemap {
		
		{
			texture = Assets.Environment.PRISON_EXIT_NEW;
			
			tileW = 14;
			tileH = 11;
		}
		
		final int TEX_WIDTH = 256;
		
		private static byte[] render = new byte[]{
				0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0,
				1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0,
				1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
				1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
				1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0,
				1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0,
				0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0,
				0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0
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
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			tileX = 11;
			tileY = 10;
			tileW = 14;
			tileH = 11;
		}
	}
	
	public static class exitVisualWalls extends CustomTilemap {
		
		{
			texture = Assets.Environment.PRISON_EXIT_NEW;
			
			tileW = 14;
			tileH = 22;
		}
		
		final int TEX_WIDTH = 256;
		
		private static byte[] render = new byte[]{
				0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
				1, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1,
				0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1,
				0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1
		};
		
		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = mapSimpleImage(0, 10, TEX_WIDTH);
			for (int i = 0; i < data.length; i++){
				if (render[i] == 0) data[i] = -1;
			}
			v.map(data, tileW);
			return v;
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			tileX = 11;
			tileY = 10;
			tileW = 14;
			tileH = 22;
		}
		
	}
	
}
