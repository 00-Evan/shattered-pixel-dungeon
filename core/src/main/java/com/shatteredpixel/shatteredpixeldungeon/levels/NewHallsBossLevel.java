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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class NewHallsBossLevel extends Level {

	{
		color1 = 0x801500;
		color2 = 0xa68521;

		viewDistance = Math.min(4, viewDistance);
	}

	private static final int WIDTH = 32;
	private static final int HEIGHT = 32;

	private static final int ROOM_LEFT		= WIDTH / 2 - 4;
	private static final int ROOM_RIGHT		= WIDTH / 2 + 4;
	private static final int ROOM_TOP		= 8;
	private static final int ROOM_BOTTOM	= ROOM_TOP + 8;

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_HALLS;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}

	@Override
	protected boolean build() {

		setSize(WIDTH, HEIGHT);

		for (int i = 0; i < 5; i++) {

			int top;
			int bottom;

			if (i == 0 || i == 4){
				top = Random.IntRange(ROOM_TOP-1, ROOM_TOP+3);
				bottom = Random.IntRange(ROOM_BOTTOM+2, ROOM_BOTTOM+6);
			} else if (i == 1 || i == 3){
				top = Random.IntRange(ROOM_TOP-5, ROOM_TOP-1);
				bottom = Random.IntRange(ROOM_BOTTOM+6, ROOM_BOTTOM+10);
			} else {
				top = Random.IntRange(ROOM_TOP-6, ROOM_TOP-3);
				bottom = Random.IntRange(ROOM_BOTTOM+8, ROOM_BOTTOM+12);
			}

			Painter.fill(this, 4 + i * 5, top, 5, bottom - top + 1, Terrain.EMPTY);

			if (i == 2) {
				entrance = (6 + i * 5) + (bottom - 1) * width();
			}

		}

		boolean[] patch = Patch.generate(width, height, 0.20f, 0, true);
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.STATUE;
			}
		}

		map[entrance] = Terrain.ENTRANCE;

		Painter.fill(this, ROOM_LEFT-1, ROOM_TOP-1, 11, 11, Terrain.EMPTY );

		patch = Patch.generate(width, height, 0.30f, 3, true);
		for (int i = 0; i < length(); i++) {
			if ((map[i] == Terrain.EMPTY || map[i] == Terrain.STATUE) && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}

		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(4) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			}
		}

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, 9, 9, Terrain.EMPTY_SP );

		Painter.fill(this, ROOM_LEFT, ROOM_TOP, 9, 2, Terrain.WALL_DECO );
		Painter.fill(this, ROOM_LEFT, ROOM_BOTTOM-1, 2, 2, Terrain.WALL_DECO );
		Painter.fill(this, ROOM_RIGHT-1, ROOM_BOTTOM-1, 2, 2, Terrain.WALL_DECO );

		Painter.fill(this, ROOM_LEFT+3, ROOM_TOP+2, 3, 4, Terrain.EMPTY );

		exit = width/2 + ((ROOM_TOP+1) * width);

		CustomTilemap vis = new CenterPieceVisuals();
		vis.pos(ROOM_LEFT, ROOM_TOP+1);
		customTiles.add(vis);

		vis = new CenterPieceWalls();
		vis.pos(ROOM_LEFT, ROOM_TOP);
		customWalls.add(vis);

		//basic version of building flag maps for the pathfinder test
		for (int i = 0; i < length; i++){
			passable[i]	= ( Terrain.flags[map[i]] & Terrain.PASSABLE) != 0;
		}

		//ensures a path to the exit exists
		return (PathFinder.getStep(entrance, exit, passable) != -1);
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
				pos = randomRespawnCell(null);
			} while (pos == entrance);
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int pos = entrance;
		int cell;
		do {
			cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (!passable[cell]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
				|| Actor.findChar(cell) != null);
		return cell;
	}

	@Override
	public void occupyCell( Char ch ) {
		super.occupyCell( ch );

		if (map[entrance] == Terrain.ENTRANCE && map[exit] != Terrain.EXIT
				&& ch == Dungeon.hero && Dungeon.level.distance(ch.pos, entrance) >= 2) {
			seal();
		}
	}

	@Override
	public void seal() {
		super.seal();
		set( entrance, Terrain.EMPTY_SP );
		GameScene.updateMap( entrance );
		CellEmitter.get( entrance ).start( FlameParticle.FACTORY, 0.1f, 10 );

		Dungeon.observe();

		YogDzewa boss = new YogDzewa();
		boss.pos = exit + width*3;
		GameScene.add( boss );
	}

	@Override
	public void unseal() {
		set( entrance, Terrain.ENTRANCE );
		GameScene.updateMap( entrance );

		set( exit, Terrain.EXIT );
		GameScene.updateMap( exit );

		CellEmitter.get(exit-1).burst(ShadowParticle.UP, 25);
		CellEmitter.get(exit).burst(ShadowParticle.UP, 100);
		CellEmitter.get(exit+1).burst(ShadowParticle.UP, 25);
		for( CustomTilemap t : customTiles){
			if (t instanceof CenterPieceVisuals){
				((CenterPieceVisuals) t).updateState();
			}
		}
		for( CustomTilemap t : customWalls){
			if (t instanceof CenterPieceWalls){
				((CenterPieceWalls) t).updateState();
			}
		}

		Dungeon.observe();
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

	public static class CenterPieceVisuals extends CustomTilemap {

		{
			texture = Assets.Environment.HALLS_SP;

			tileW = 9;
			tileH = 8;
		}

		private static final int[] map = new int[]{
				 8,  9, 10, 11, 11, 11, 12, 13, 14,
				16, 17, 18, 27, 19, 27, 20, 21, 22,
				24, 25, 26, 19, 19, 19, 28, 29, 30,
				24, 25, 26, 19, 19, 19, 28, 29, 30,
				24, 25, 26, 19, 19, 19, 28, 29, 30,
				24, 25, 34, 35, 35, 35, 34, 29, 30,
				40, 41, 36, 36, 36, 36, 36, 40, 41,
				48, 49, 36, 36, 36, 36, 36, 48, 49
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			updateState();
			return v;
		}

		private void updateState(){
			if (vis != null){
				int[] data = map.clone();
				if (Dungeon.level.map[Dungeon.level.exit] == Terrain.EXIT) {
					data[4] = 19;
					data[12] = data[14] = 31;
				}
				vis.map(data, tileW);
			}
		}
	}

	public static class CenterPieceWalls extends CustomTilemap {

		{
			texture = Assets.Environment.HALLS_SP;

			tileW = 9;
			tileH = 9;
		}

		private static final int[] map = new int[]{
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1,
				32, 33, -1, -1, -1, -1, -1, 32, 33,
				40, 41, -1, -1, -1, -1, -1, 40, 41,
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			updateState();
			return v;
		}

		private void updateState(){
			if (vis != null){
				int[] data = map.clone();
				if (Dungeon.level.map[Dungeon.level.exit] == Terrain.EXIT) {
					data[3] = 1;
					data[4] = 0;
					data[5] = 2;
					data[13] = 23;
				}
				vis.map(data, tileW);
			}
		}

	}
}
