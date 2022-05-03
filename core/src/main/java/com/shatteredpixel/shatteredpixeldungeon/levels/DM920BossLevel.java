/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM920BossLevel extends Level {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

		viewDistance = Math.min(6, viewDistance);
	}

	private static final int WIDTH = 43;
	private static final int HEIGHT = 43;

	private static final int ROOM_LEFT		= WIDTH / 2 - 3;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 1;
	private static final int ROOM_TOP		= HEIGHT / 2 - 2;
	private static final int ROOM_BOTTOM	= HEIGHT / 2 + 2;

	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_DIED;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
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
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}

	private static final int W = Terrain.WALL;
	public static final int U = Terrain.DOOR;
	public static final int J = Terrain.LOCKED_DOOR;
	private static final int G = Terrain.PEDESTAL;

	private static final int T = Terrain.WATER;
	private static final int O = Terrain.INACTIVE_TRAP;
	private static final int L = Terrain.EMPTY;
	private static final int E = Terrain.EMPTY_SP;
	private static final int X = Terrain.WATER;
	private static final int S = Terrain.ENTRANCE;

	private static final int[] MAP = {
			W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
			W,E,E,T,T,E,E,W,E,T,T,T,E,E,T,E,E,E,T,E,E,E,E,E,T,T,T,T,E,T,E,E,E,E,E,W,E,E,T,T,E,E,W,
			W,E,T,E,E,T,E,W,E,T,E,E,T,E,T,E,E,E,T,E,E,E,E,T,E,E,E,E,E,T,E,E,E,E,E,W,E,T,E,E,T,E,W,
			W,T,E,T,T,E,T,W,E,T,E,T,T,E,T,E,E,E,T,E,E,E,E,T,T,T,T,T,E,T,T,T,T,E,E,W,T,E,T,T,E,T,W,
			W,T,E,T,T,E,T,W,E,T,T,E,E,E,T,E,E,E,T,E,E,G,E,E,E,E,E,T,E,T,E,E,E,T,E,W,T,E,T,T,E,T,W,
			W,E,T,E,E,T,E,W,E,T,E,T,E,E,T,E,E,E,T,E,E,E,E,T,E,E,E,T,E,T,E,E,E,T,E,W,E,T,E,E,T,E,W,
			W,E,E,T,T,E,E,W,E,T,E,E,T,E,E,T,T,T,E,E,E,E,E,E,T,T,T,E,E,T,E,E,E,T,E,W,E,E,T,T,E,E,W,
			W,W,W,U,U,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,J,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,U,U,W,W,W,
			W,E,E,E,E,E,E,W,O,O,O,O,O,O,O,O,O,O,O,O,O,E,O,O,O,O,O,O,O,O,O,O,O,O,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,L,L,L,L,E,L,L,L,L,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,W,W,W,W,W,W,W,O,L,L,L,L,L,L,L,L,L,L,L,L,E,L,L,L,L,L,L,L,L,L,L,L,L,O,W,W,W,W,W,W,W,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,L,L,L,L,E,L,L,L,L,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,E,E,E,E,E,E,E,E,E,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,E,L,L,L,L,E,L,L,L,L,E,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,E,L,L,L,L,L,E,L,L,L,L,L,E,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,E,E,L,L,L,E,E,E,E,E,L,L,L,E,E,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,E,L,L,E,L,E,T,T,E,T,T,E,L,E,L,L,E,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,L,L,E,T,T,T,E,T,T,T,E,L,L,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,L,E,T,E,T,E,E,E,T,E,T,E,L,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,E,T,T,T,E,T,E,T,E,T,T,T,E,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,E,T,T,E,T,E,E,E,T,E,T,T,E,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,G,E,E,J,E,E,E,E,E,E,E,E,E,E,E,E,E,T,E,E,E,E,E,E,E,E,E,E,E,E,E,J,E,E,G,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,E,T,T,E,T,E,E,E,T,E,T,T,E,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,E,T,T,T,E,T,E,T,E,T,T,T,E,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,L,E,T,E,T,E,E,E,T,E,T,E,L,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,E,L,L,L,L,E,T,T,T,E,T,T,T,E,L,L,L,L,E,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,E,L,L,E,L,E,T,T,E,T,T,E,L,E,L,L,E,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,E,E,L,L,L,E,E,E,E,E,L,L,L,E,E,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,E,L,L,L,L,L,E,L,L,L,L,L,E,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,E,L,L,L,L,E,L,L,L,L,E,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,E,E,E,E,E,E,E,E,E,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,L,L,L,L,E,L,L,L,L,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,L,L,L,L,E,L,L,L,L,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,L,L,L,L,L,L,L,L,L,L,L,L,E,L,L,L,L,L,L,L,L,L,L,L,L,O,W,E,E,E,E,E,E,W,
			W,E,E,E,E,E,E,W,O,O,O,O,O,O,O,O,O,O,O,O,O,E,O,O,O,O,O,O,O,O,O,O,O,O,O,W,E,E,E,E,E,E,W,
			W,W,W,U,U,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,S,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,U,U,W,W,W,
			W,E,E,T,T,E,E,W,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,W,E,E,T,T,E,E,W,
			W,E,T,E,E,T,E,U,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,U,E,T,E,E,T,E,W,
			W,T,E,T,T,E,T,W,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,W,T,E,T,T,E,T,W,
			W,T,E,T,T,E,T,U,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,W,T,E,T,T,E,T,W,
			W,E,T,E,E,T,E,W,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,U,E,T,E,E,T,E,W,
			W,E,E,T,T,E,E,W,E,E,E,E,E,E,E,E,E,E,E,E,W,W,W,E,E,E,E,E,E,E,E,E,E,E,E,W,E,E,T,T,E,E,W,
			W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,
	};

	protected boolean build() {
		setSize(WIDTH, HEIGHT);
		map = MAP.clone();

		buildFlagMaps();
		cleanWalls();

		for(int i=4*WIDTH;i<length-4*WIDTH;++i){
			if(!solid[i]) {
				if (Random.Int(100) < 6) {
				}
			}
		}

		this.exit = 0;
		this.entrance = (this.width * 35) + 21;
		//map[exit] = (this.width * 35) + 21;;

		return true;
	}

	@Override
	protected void createMobs() {
		//DM920 Boss= new DM920();
		//Boss.pos =  (this.width * 21 + 21);
		//mobs.add(Boss);
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = Random.IntRange( ROOM_LEFT, ROOM_RIGHT ) + Random.IntRange( ROOM_TOP + 1, ROOM_BOTTOM ) * width();
			} while (pos == entrance);
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public void occupyCell( Char ch ) {
		super.occupyCell(ch);

		if (map[entrance] == Terrain.ENTRANCE && map[exit] != Terrain.EXIT
				&& ch == Dungeon.hero && Dungeon.level.distance(ch.pos, entrance) >= 0) {
			seal();
		}
	}

	@Override
	public void seal() {
		super.seal();

		set( entrance, Terrain.WALL );
		GameScene.updateMap( entrance );
		CellEmitter.get( entrance ).start( FlameParticle.FACTORY, 0.1f, 10 );
		Music.INSTANCE.play( Assets.BGM_0, true );
		Dungeon.observe();
	}

	@Override
	public void unseal() {
		super.unseal();
		set( entrance, Terrain.EXIT );
		GameScene.updateMap( entrance );
		CellEmitter.get( entrance ).start( FlameParticle.FACTORY, 0.1f, 10 );
		Dungeon.observe();

		Dungeon.observe();
	}

	@Override
	public Heap drop( Item item, int cell ) {

		if (!keyDropped && item instanceof SkeletonKey) {

			keyDropped = true;
			unseal();

			CellEmitter.get( arenaDoor ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );

			set( entrance, Terrain.ENTRANCE );
			GameScene.updateMap( entrance );
			Dungeon.observe();
		}

		return super.drop( item, cell );
	}

	private boolean outsideEntraceRoom( int cell ) {
		int cx = cell % width();
		int cy = cell / width();
		return cx < ROOM_LEFT-1 || cx > ROOM_RIGHT+1 || cy < ROOM_TOP-1 || cy > ROOM_BOTTOM+1;
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
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(HallsLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(HallsLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
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
}
