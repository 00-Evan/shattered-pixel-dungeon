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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldDM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class NewCavesBossLevel extends Level {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_CAVES;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_CAVES;
	}

	private static int WIDTH = 33;
	private static int HEIGHT = 42;

	public static Rect mainArena = new Rect(5, 14, 28, 37);
	public static Rect gate = new Rect(14, 13, 19, 14);
	public static int[] pylonPositions = new int[]{ 4 + 13*WIDTH, 28 + 13*WIDTH, 4 + 37*WIDTH, 28 + 37*WIDTH };

	private ArenaVisuals customArenaVisuals;

	@Override
	protected boolean build() {

		setSize(WIDTH, HEIGHT);

		//These signs are visually overridden with custom tile visuals
		Painter.fill(this, gate, Terrain.SIGN);

		//set up main boss arena
		Painter.fillEllipse(this, mainArena, Terrain.EMPTY);

		boolean[] patch = Patch.generate( width, height-14, 0.15f, 2, true );
		for (int i= 14*width(); i < length(); i++) {
			if (map[i] == Terrain.EMPTY) {
				if (patch[i - 14*width()]){
					map[i] = Terrain.WATER;
				} else if (Random.Int(8) == 0){
					map[i] = Terrain.INACTIVE_TRAP;
				}
			}
		}

		buildEntrance();
		buildCorners();

		new CavesPainter().paint(this, null);

		//setup exit area above main boss arena
		Painter.fill(this, 0, 3, width(), 4, Terrain.CHASM);
		Painter.fill(this, 6, 7, 21, 1, Terrain.CHASM);
		Painter.fill(this, 10, 8, 13, 1, Terrain.CHASM);
		Painter.fill(this, 12, 9, 9, 1, Terrain.CHASM);
		Painter.fill(this, 13, 10, 7, 1, Terrain.CHASM);
		Painter.fill(this, 14, 3, 5, 10, Terrain.EMPTY);

		//fill in special floor, statues, and exits
		Painter.fill(this, 15, 2, 3, 3, Terrain.EMPTY_SP);
		Painter.fill(this, 15, 5, 3, 1, Terrain.STATUE);
		Painter.fill(this, 15, 7, 3, 1, Terrain.STATUE);
		Painter.fill(this, 15, 9, 3, 1, Terrain.STATUE);
		Painter.fill(this, 16, 5, 1, 6, Terrain.EMPTY_SP);
		Painter.fill(this, 15, 0, 3, 3, Terrain.EXIT);

		exit = 16 + 2*width();

		CustomTilemap customVisuals = new CityEntrance();
		customVisuals.setRect(0, 0, width(), 11);
		customTiles.add(customVisuals);

		customVisuals = new EntranceOverhang();
		customVisuals.setRect(0, 0, width(), 11);
		customWalls.add(customVisuals);

		customVisuals = customArenaVisuals = new ArenaVisuals();
		customVisuals.setRect(0, 12, width(), 27);
		customTiles.add(customVisuals);

		return true;

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		for (CustomTilemap c : customTiles){
			if (c instanceof ArenaVisuals){
				customArenaVisuals = (ArenaVisuals) c;
			}
		}

		//pre-0.8.1 saves that may not have had pylons added
		int gatePos = pointToCell(new Point(gate.left, gate.top));
		if (!locked && solid[gatePos]){

			for (int i : pylonPositions) {
				if (findMob(i) == null) {
					Pylon pylon = new Pylon();
					pylon.pos = i;
					mobs.add(pylon);
				}
			}

		}
	}

	@Override
	protected void createMobs() {
		for (int i : pylonPositions) {
			Pylon pylon = new Pylon();
			pylon.pos = i;
			mobs.add(pylon);
		}
	}

	@Override
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = randomRespawnCell(null);
			} while (pos == entrance);
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		//this check is mainly here for DM-300, to prevent an infinite loop
		if (Char.hasProp(ch, Char.Property.LARGE) && map[entrance] != Terrain.ENTRANCE){
			return -1;
		}
		int cell;
		do {
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public boolean setCellToWater(boolean includeTraps, int cell) {
		for (int i : pylonPositions){
			if (Dungeon.level.distance(cell, i) <= 1){
				return false;
			}
		}

		return super.setCellToWater(includeTraps, cell);
	}

	@Override
	public void occupyCell(Char ch) {
		super.occupyCell(ch);

		//seal the level when the hero moves near to a pylon, the level isn't already sealed, and the gate hasn't been destroyed
		int gatePos = pointToCell(new Point(gate.left, gate.top));
		if (ch == Dungeon.hero && !locked && solid[gatePos]){
			for (int pos : pylonPositions){
				if (Dungeon.level.distance(ch.pos, pos) <= 3){
					seal();
					break;
				}
			}
		}
	}

	@Override
	public void seal() {
		super.seal();

		set( entrance, Terrain.WALL );

		Heap heap = Dungeon.level.heaps.get( entrance );
		if (heap != null) {
			int n;
			do {
				n = entrance + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable[n]);
			Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( entrance );
		}

		Char ch = Actor.findChar( entrance );
		if (ch != null) {
			int n;
			do {
				n = entrance + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable[n]);
			ch.pos = n;
			ch.sprite.place(n);
		}

		GameScene.updateMap( entrance );
		Dungeon.observe();

		CellEmitter.get( entrance ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
		Camera.main.shake( 3, 0.7f );
		Sample.INSTANCE.play( Assets.Sounds.ROCKS );

		NewDM300 boss = new NewDM300();
		boss.state = boss.WANDERING;
		do {
			boss.pos = pointToCell(Random.element(mainArena.getPoints()));
		} while (!openSpace[boss.pos] || map[boss.pos] == Terrain.EMPTY_SP);
		GameScene.add( boss );

	}

	@Override
	public void unseal() {
		super.unseal();

		blobs.get(PylonEnergy.class).fullyClear();

		set( entrance, Terrain.ENTRANCE );
		int i = 14 + 13*width();
		for (int j = 0; j < 5; j++){
			set( i+j, Terrain.EMPTY );
			if (Dungeon.level.heroFOV[i+j]){
				CellEmitter.get(i+j).burst(BlastParticle.FACTORY, 10);
			}
		}
		GameScene.updateMap();

		customArenaVisuals.updateState();

		Dungeon.observe();

	}

	public void activatePylon(){
		ArrayList<Pylon> pylons = new ArrayList<>();
		for (Mob m : mobs){
			if (m instanceof Pylon && m.alignment == Char.Alignment.NEUTRAL){
				pylons.add((Pylon) m);
			}
		}

		if (pylons.size() == 1){
			pylons.get(0).activate();
		} else if (!pylons.isEmpty()) {
			Pylon closest = null;
			for (Pylon p : pylons){
				if (closest == null || trueDistance(p.pos, Dungeon.hero.pos) < trueDistance(closest.pos, Dungeon.hero.pos)){
					closest = p;
				}
			}
			pylons.remove(closest);
			Random.element(pylons).activate();
		}

		for( int i = (mainArena.top-1)*width; i <length; i++){
			if (map[i] == Terrain.INACTIVE_TRAP || map[i] == Terrain.WATER || map[i] == Terrain.SIGN){
				GameScene.add(Blob.seed(i, 1, PylonEnergy.class));
			}
		}

	}

	public void eliminatePylon(){
		customArenaVisuals.updateState();
		int pylonsRemaining = 0;
		for (Mob m : mobs){
			if (m instanceof NewDM300){
				((NewDM300) m).loseSupercharge();
				PylonEnergy.energySourceSprite = m.sprite;
			} else if (m instanceof Pylon){
				pylonsRemaining++;
			}
		}
		if (pylonsRemaining > 2) {
			blobs.get(PylonEnergy.class).fullyClear();
		}
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.GRASS:
				return Messages.get(CavesLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_name");
			case Terrain.WATER:
				return Messages.get(CavesLevel.class, "water_name");
			case Terrain.STATUE:
				//city statues are used
				return Messages.get(CityLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return super.tileDesc( tile ) + "\n\n" + Messages.get(NewCavesBossLevel.class, "water_desc");
			case Terrain.ENTRANCE:
				return Messages.get(CavesLevel.class, "entrance_desc");
			case Terrain.EXIT:
				//city exit is used
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_desc");
			case Terrain.WALL_DECO:
				return Messages.get(CavesLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CavesLevel.class, "bookshelf_desc");
			//city statues are used
			case Terrain.STATUE:
				return Messages.get(CityLevel.class, "statue_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		CavesLevel.addCavesVisuals(this, visuals);
		return visuals;
	}

	/**
	 * semi-randomized setup for entrance and corners
	 */

	private static final short n = -1; //used when a tile shouldn't be changed
	private static final short W = Terrain.WALL;
	private static final short e = Terrain.EMPTY;
	private static final short s = Terrain.EMPTY_SP;

	private static short[] entrance1 = {
			n, n, n, n, n, n, n, n,
			n, n, n, n, n, n, n, n,
			n, n, n, n, W, e, W, W,
			n, n, n, W, W, e, W, W,
			n, n, W, W, e, e, e, e,
			n, n, e, e, e, W, W, e,
			n, n, W, W, e, W, e, e,
			n, n, W, W, e, e, e, e
	};

	private static short[] entrance2 = {
			n, n, n, n, n, n, n, n,
			n, n, n, n, n, n, n, n,
			n, n, n, n, n, e, e, e,
			n, n, n, W, e, W, W, e,
			n, n, n, e, e, e, e, e,
			n, n, e, W, e, W, W, e,
			n, n, e, W, e, W, e, e,
			n, n, e, e, e, e, e, e
	};

	private static short[] entrance3 = {
			n, n, n, n, n, n, n, n,
			n, n, n, n, n, n, n, n,
			n, n, n, n, n, n, n, n,
			n, n, n, W, W, e, W, W,
			n, n, n, W, W, e, W, W,
			n, n, n, e, e, e, e, e,
			n, n, n, W, W, e, W, e,
			n, n, n, W, W, e, e, e
	};

	private static short[] entrance4 = {
			n, n, n, n, n, n, n, n,
			n, n, n, n, n, n, n, e,
			n, n, n, n, n, n, W, e,
			n, n, n, n, n, W, W, e,
			n, n, n, n, W, W, W, e,
			n, n, n, W, W, W, W, e,
			n, n, W, W, W, W, e, e,
			n, e, e, e, e, e, e, e
	};

	private static short[][] entranceVariants = {
			entrance1,
			entrance2,
			entrance3,
			entrance4
	};

	private void buildEntrance(){
		entrance = 16 + 25*width();

		//entrance area
		int NW = entrance - 7 - 7*width();
		int NE = entrance + 7 - 7*width();
		int SE = entrance + 7 + 7*width();
		int SW = entrance - 7 + 7*width();

		short[] entranceTiles = Random.oneOf(entranceVariants);
		for (int i = 0; i < entranceTiles.length; i++){
			if (i % 8 == 0 && i != 0){
				NW += (width() - 8);
				NE += (width() + 8);
				SE -= (width() - 8);
				SW -= (width() + 8);
			}

			if (entranceTiles[i] != n) map[NW] = map[NE] = map[SE] = map[SW] = entranceTiles[i];
			NW++; NE--; SW++; SE--;
		}

		Painter.set(this, entrance, Terrain.ENTRANCE);
	}

	private static short[] corner1 = {
			W, W, W, W, W, W, W, W, W, W,
			W, s, s, s, e, e, e, W, W, W,
			W, s, s, s, W, W, e, e, W, W,
			W, s, s, s, W, W, W, e, e, W,
			W, e, W, W, W, W, W, W, e, n,
			W, e, W, W, W, W, W, n, n, n,
			W, e, e, W, W, W, n, n, n, n,
			W, W, e, e, W, n, n, n, n, n,
			W, W, W, e, e, n, n, n, n, n,
			W, W, W, W, n, n, n, n, n, n,
	};

	private static short[] corner2 = {
			W, W, W, W, W, W, W, W, W, W,
			W, s, s, s, W, W, W, W, W, W,
			W, s, s, s, e, e, e, e, e, W,
			W, s, s, s, W, W, W, W, e, e,
			W, W, e, W, W, W, W, W, W, e,
			W, W, e, W, W, W, W, n, n, n,
			W, W, e, W, W, W, n, n, n, n,
			W, W, e, W, W, n, n, n, n, n,
			W, W, e, e, W, n, n, n, n, n,
			W, W, W, e, e, n, n, n, n, n,
	};

	private static short[] corner3 = {
			W, W, W, W, W, W, W, W, W, W,
			W, s, s, s, W, W, W, W, W, W,
			W, s, s, s, e, e, e, e, W, W,
			W, s, s, s, W, W, W, e, W, W,
			W, W, e, W, W, W, W, e, W, n,
			W, W, e, W, W, W, W, e, e, n,
			W, W, e, W, W, W, n, n, n, n,
			W, W, e, e, e, e, n, n, n, n,
			W, W, W, W, W, e, n, n, n, n,
			W, W, W, W, n, n, n, n, n, n,
	};

	private static short[] corner4 = {
			W, W, W, W, W, W, W, W, W, W,
			W, s, s, s, W, W, W, W, W, W,
			W, s, s, s, e, e, e, W, W, W,
			W, s, s, s, W, W, e, W, W, W,
			W, W, e, W, W, W, e, W, W, n,
			W, W, e, W, W, W, e, e, n, n,
			W, W, e, e, e, e, e, n, n, n,
			W, W, W, W, W, e, n, n, n, n,
			W, W, W, W, W, n, n, n, n, n,
			W, W, W, W, n, n, n, n, n, n,
	};

	private static short[][] cornerVariants = {
			corner1,
			corner2,
			corner3,
			corner4
	};

	private void buildCorners(){
		int NW = 2 + 11*width();
		int NE = 30 + 11*width();
		int SE = 30 + 39*width();
		int SW = 2 + 39*width();

		short[] cornerTiles = Random.oneOf(cornerVariants);
		for(int i = 0; i < cornerTiles.length; i++){
			if (i % 10 == 0 && i != 0){
				NW += (width() - 10);
				NE += (width() + 10);
				SE -= (width() - 10);
				SW -= (width() + 10);
			}

			if (cornerTiles[i] != n) map[NW] = map[NE] = map[SE] = map[SW] = cornerTiles[i];
			NW++; NE--; SW++; SE--;
		}
	}

	/**
	 * Visual Effects
	 */

	public static class CityEntrance extends CustomTilemap{

		{
			texture = Assets.Environment.CAVES_BOSS;
		}

		private static short[] entryWay = new short[]{
				-1,  7,  7,  7, -1,
				-1,  1,  2,  3, -1,
				 8,  1,  2,  3, 12,
				16,  9, 10, 11, 20,
				16, 16, 18, 20, 20,
				16, 17, 18, 19, 20,
				16, 16, 18, 20, 20,
				16, 17, 18, 19, 20,
				16, 16, 18, 20, 20,
				16, 17, 18, 19, 20,
				24, 25, 26, 27, 28
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int entryPos = 0;
			for (int i = 0; i < data.length; i++){

				//override the entryway
				if (i % tileW == tileW/2 - 2){
					data[i++] = entryWay[entryPos++];
					data[i++] = entryWay[entryPos++];
					data[i++] = entryWay[entryPos++];
					data[i++] = entryWay[entryPos++];
					data[i] = entryWay[entryPos++];

				//otherwise check if we are on row 2 or 3, in which case we need to override walls
				} else {
					if (i / tileW == 2) data[i] = 13;
					else if (i / tileW == 3) data[i] = 21;
					else data[i] = -1;
				}
			}
			v.map( data, tileW );
			return v;
		}

	}

	public static class EntranceOverhang extends CustomTilemap{

		{
			texture = Assets.Environment.CAVES_BOSS;
		}

		private static short[] entryWay = new short[]{
				 0,  7,  7,  7,  4,
				 0, 15, 15, 15,  4,
				-1, 23, 23, 23, -1,
				-1, -1, -1, -1, -1,
				-1,  6, -1, 14, -1,
				-1, -1, -1, -1, -1,
				-1,  6, -1, 14, -1,
				-1, -1, -1, -1, -1,
				-1,  6, -1, 14, -1,
				-1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			int entryPos = 0;
			for (int i = 0; i < data.length; i++){

				//copy over this row of the entryway
				if (i % tileW == tileW/2 - 2){
					data[i++] = entryWay[entryPos++];
					data[i++] = entryWay[entryPos++];
					data[i++] = entryWay[entryPos++];
					data[i++] = entryWay[entryPos++];
					data[i] = entryWay[entryPos++];
				} else {
					data[i] = -1;
				}
			}
			v.map( data, tileW );
			return v;
		}

	}

	public static class ArenaVisuals extends CustomTilemap {

		{
			texture = Assets.Environment.CAVES_BOSS;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			updateState( );

			return v;
		}

		public void updateState( ){
			if (vis != null){
				int[] data = new int[tileW*tileH];
				int j = Dungeon.level.width() * tileY;
				for (int i = 0; i < data.length; i++){

					if (Dungeon.level.map[j] == Terrain.EMPTY_SP) {
						for (int k : pylonPositions) {
							if (k == j) {
								if (Dungeon.level.locked
										&& !(Actor.findChar(k) instanceof Pylon)) {
									data[i] = 38;
								} else {
									data[i] = -1;
								}
							} else if (Dungeon.level.adjacent(k, j)) {
								int w = Dungeon.level.width;
								data[i] = 54 + (j % w + 8 * (j / w)) - (k % w + 8 * (k / w));
							}
						}
					} else if (Dungeon.level.map[j] == Terrain.INACTIVE_TRAP){
						data[i] = 37;
					} else if (gate.inside(Dungeon.level.cellToPoint(j))){
						int idx = Dungeon.level.solid[j] ? 40 : 32;
						data[i++] = idx++;
						data[i++] = idx++;
						data[i++] = idx++;
						data[i++] = idx++;
						data[i] = idx;
						j += 4;
					} else {
						data[i] = -1;
					}

					j++;
				}
				vis.map(data, tileW);
			}
		}

		@Override
		public String name(int tileX, int tileY) {
			int i = tileX + tileW*(tileY + this.tileY);
			if (Dungeon.level.map[i] == Terrain.INACTIVE_TRAP){
				return Messages.get(NewCavesBossLevel.class, "wires_name");
			} else if (gate.inside(Dungeon.level.cellToPoint(i))){
				return Messages.get(NewCavesBossLevel.class, "gate_name");
			}

			return super.name(tileX, tileY);
		}

		@Override
		public String desc(int tileX, int tileY) {
			int i = tileX + tileW*(tileY + this.tileY);
			if (Dungeon.level.map[i] == Terrain.INACTIVE_TRAP){
				return Messages.get(NewCavesBossLevel.class, "wires_desc");
			} else if (gate.inside(Dungeon.level.cellToPoint(i))){
				if (Dungeon.level.solid[i]){
					return Messages.get(NewCavesBossLevel.class, "gate_desc");
				} else {
					return Messages.get(NewCavesBossLevel.class, "gate_desc_broken");
				}
			}
			return super.desc(tileX, tileY);
		}

		@Override
		public Image image(int tileX, int tileY) {
			int i = tileX + tileW*(tileY + this.tileY);
			for (int k : pylonPositions){
				if (Dungeon.level.distance(i, k) <= 1){
					return null;
				}
			}

			return super.image(tileX, tileY);

		}
	}

	public static class PylonEnergy extends Blob {

		@Override
		protected void evolve() {
			for (int cell = 0; cell < Dungeon.level.length(); cell++) {
				if (Dungeon.level.insideMap(cell)) {
					off[cell] = cur[cell];

					//instantly spreads to water cells
					if (off[cell] == 0 && Dungeon.level.water[cell]){
						off[cell]++;
					}

					volume += off[cell];

					if (off[cell] > 0){

						Char ch = Actor.findChar(cell);
						if (ch != null && !(ch instanceof NewDM300)) {
							Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
							ch.damage( Random.NormalIntRange(6, 12), Electricity.class);
							ch.sprite.flash();

							if (ch == Dungeon.hero && !ch.isAlive()) {
								Dungeon.fail(NewDM300.class);
								GLog.n( Messages.get(Electricity.class, "ondeath") );
							}
						}
					}
				}
			}
		}

		@Override
		public void fullyClear() {
			super.fullyClear();
			energySourceSprite = null;
		}

		private static CharSprite energySourceSprite = null;

		private static Emitter.Factory DIRECTED_SPARKS = new Emitter.Factory() {
			@Override
			public void emit(Emitter emitter, int index, float x, float y) {
				if (energySourceSprite == null){
					for (Char c : Actor.chars()){
						if (c instanceof Pylon && c.alignment != Char.Alignment.NEUTRAL){
							energySourceSprite = c.sprite;
							break;
						} else if (c instanceof OldDM300){
							energySourceSprite = c.sprite;
						}
					}
					if (energySourceSprite == null){
						return;
					}
				}

				SparkParticle s = ((SparkParticle) emitter.recycle(SparkParticle.class));
				s.resetStatic(x, y);
				s.speed.set((energySourceSprite.x + energySourceSprite.width/2f) - x,
						(energySourceSprite.y + energySourceSprite.height/2f) - y);
				s.speed.normalize().scale(DungeonTilemap.SIZE*2f);

				//offset the particles slightly so they don't go too far outside of the cell
				s.x -= s.speed.x/8f;
				s.y -= s.speed.y/8f;
			}

			@Override
			public boolean lightMode() {
				return true;
			}
		};

		@Override
		public String tileDesc() {
			return Messages.get(NewCavesBossLevel.class, "energy_desc");
		}

		@Override
		public void use( BlobEmitter emitter ) {
			super.use( emitter );
			energySourceSprite = null;
			emitter.pour(DIRECTED_SPARKS, 0.125f);
		}

	}
}
