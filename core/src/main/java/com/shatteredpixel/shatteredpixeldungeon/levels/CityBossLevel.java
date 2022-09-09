/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DwarfKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CityPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ImpShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.HashSet;

public class CityBossLevel extends Level {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}

	private static int WIDTH = 15;
	private static int HEIGHT = 48;

	private static final Rect entry = new Rect(1, 37, 14, 48);
	private static final Rect arena = new Rect(1, 25, 14, 38);
	private static final Rect end = new Rect(0, 0, 15, 22);

	private static final int bottomDoor = 7 + (arena.bottom-1)*15;
	private static final int topDoor = 7 + arena.top*15;

	public static final int throne;
	private static final int[] pedestals = new int[4];

	static {
		Point c = arena.center();
		throne = c.x + (c.y) * WIDTH;
		pedestals[0] = c.x-3 + (c.y-3) * WIDTH;
		pedestals[1] = c.x+3 + (c.y-3) * WIDTH;
		pedestals[2] = c.x+3 + (c.y+3) * WIDTH;
		pedestals[3] = c.x-3 + (c.y+3) * WIDTH;
	}

	private ImpShopRoom impShop;

	@Override
	public void playLevelMusic() {
		if (locked){
			Music.INSTANCE.play(Assets.Music.CITY_BOSS, true);
		//if top door isn't unlocked
		} else if (map[topDoor] == Terrain.LOCKED_DOOR){
			Music.INSTANCE.end();
		} else {
			Music.INSTANCE.playTracks(
					new String[]{Assets.Music.CITY_1, Assets.Music.CITY_2, Assets.Music.CITY_2},
					new float[]{1, 1, 0.5f},
					false);
		}
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_CITY;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_CITY;
	}

	private static final String IMP_SHOP = "imp_shop";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( IMP_SHOP, impShop );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		//pre-1.3.0 saves, modifies exit transition with custom size
		if (bundle.contains("exit")){
			LevelTransition exit = getTransition(LevelTransition.Type.REGULAR_EXIT);
			exit.set(end.left+4, end.top+4, end.left+4+6, end.top+4+4);
			transitions.add(exit);
		}
		impShop = (ImpShopRoom) bundle.get( IMP_SHOP );
		if (map[topDoor] != Terrain.LOCKED_DOOR && Imp.Quest.isCompleted() && !impShop.shopSpawned()){
			spawnShop();
		}
	}

	@Override
	protected boolean build() {

		setSize(WIDTH, HEIGHT);

		//entrance room
		Painter.fill(this, entry, Terrain.WALL);
		Painter.fill(this, entry, 1, Terrain.BOOKSHELF);
		Painter.fill(this, entry, 2, Terrain.EMPTY);

		Painter.fill(this, entry.left+3, entry.top+3, 1, 5, Terrain.BOOKSHELF);
		Painter.fill(this, entry.right-4, entry.top+3, 1, 5, Terrain.BOOKSHELF);

		Point c = entry.center();

		Painter.fill(this, c.x-1, c.y-2, 3, 1, Terrain.STATUE);
		Painter.fill(this, c.x-1, c.y, 3, 1, Terrain.STATUE);
		Painter.fill(this, c.x-1, c.y+2, 3, 1, Terrain.STATUE);
		Painter.fill(this, c.x, entry.top+1, 1, 6, Terrain.EMPTY_SP);

		Painter.set(this, c.x, entry.top, Terrain.DOOR);

		int entrance = c.x + (c.y+2)*width();
		Painter.set(this, entrance, Terrain.ENTRANCE);
		transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

		//DK's throne room
		Painter.fillDiamond(this, arena, 1, Terrain.EMPTY);

		Painter.fill(this, arena, 5, Terrain.EMPTY_SP);
		Painter.fill(this, arena, 6, Terrain.SIGN);

		c = arena.center();
		Painter.set(this, c.x-3, c.y, Terrain.STATUE);
		Painter.set(this, c.x-4, c.y, Terrain.STATUE);
		Painter.set(this, c.x+3, c.y, Terrain.STATUE);
		Painter.set(this, c.x+4, c.y, Terrain.STATUE);

		Painter.set(this, pedestals[0], Terrain.PEDESTAL);
		Painter.set(this, pedestals[1], Terrain.PEDESTAL);
		Painter.set(this, pedestals[2], Terrain.PEDESTAL);
		Painter.set(this, pedestals[3], Terrain.PEDESTAL);

		Painter.set(this, c.x, arena.top, Terrain.LOCKED_DOOR);

		//exit hallway
		Painter.fill(this, end, Terrain.CHASM);
		Painter.fill(this, end.left+4, end.top+5, 7, 18, Terrain.EMPTY);
		Painter.fill(this, end.left+4, end.top+5, 7, 4, Terrain.EXIT);

		int exitCell = end.left+7 + (end.top+8)*width();
		LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
		exit.set(end.left+4, end.top+4, end.left+4+6, end.top+4+4);
		transitions.add(exit);

		impShop = new ImpShopRoom();
		impShop.set(end.left+3, end.top+12, end.left+11, end.top+20);
		Painter.set(this, impShop.center(), Terrain.PEDESTAL);

		Painter.set(this, impShop.left+2, impShop.top, Terrain.STATUE);
		Painter.set(this, impShop.left+6, impShop.top, Terrain.STATUE);

		Painter.fill(this, end.left+5, end.bottom+1, 5, 1, Terrain.EMPTY);
		Painter.fill(this, end.left+6, end.bottom+2, 3, 1, Terrain.EMPTY);

		new CityPainter().paint(this, null);

		//pillars last, no deco on these
		Painter.fill(this, end.left+1, end.top+2, 2, 2, Terrain.WALL);
		Painter.fill(this, end.left+1, end.top+7, 2, 2, Terrain.WALL);
		Painter.fill(this, end.left+1, end.top+12, 2, 2, Terrain.WALL);
		Painter.fill(this, end.left+1, end.top+17, 2, 2, Terrain.WALL);

		Painter.fill(this, end.right-3, end.top+2, 2, 2, Terrain.WALL);
		Painter.fill(this, end.right-3, end.top+7, 2, 2, Terrain.WALL);
		Painter.fill(this, end.right-3, end.top+12, 2, 2, Terrain.WALL);
		Painter.fill(this, end.right-3, end.top+17, 2, 2, Terrain.WALL);

		CustomTilemap customVisuals = new CustomGroundVisuals();
		customVisuals.setRect(0, 0, width(), height());
		customTiles.add(customVisuals);

		customVisuals = new CustomWallVisuals();
		customVisuals.setRect(0, 0, width(), height());
		customWalls.add(customVisuals);

		return true;
	}

	//returns a random pedestal that doesn't already have a summon inbound on it
	public int getSummoningPos(){
		Mob king = getKing();
		HashSet<DwarfKing.Summoning> summons = king.buffs(DwarfKing.Summoning.class);
		ArrayList<Integer> positions = new ArrayList<>();
		for (int pedestal : pedestals) {
			boolean clear = true;
			for (DwarfKing.Summoning s : summons) {
				if (s.getPos() == pedestal) {
					clear = false;
					break;
				}
			}
			if (clear) {
				positions.add(pedestal);
			}
		}
		if (positions.isEmpty()){
			return -1;
		} else {
			return Random.element(positions);
		}
	}

	private Mob getKing(){
		for (Mob m : mobs){
			if (m instanceof DwarfKing) return m;
		}
		return null;
	}

	@Override
	protected void createMobs() {
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
				pos = randomRespawnCell(null);
			} while (pos == entrance());
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8){
			int cell = entrance() + i;
			if (passable[cell]
					&& Actor.findChar(cell) == null
					&& (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])){
				candidates.add(cell);
			}
		}

		if (candidates.isEmpty()){
			return -1;
		} else {
			return Random.element(candidates);
		}
	}

	@Override
	public void occupyCell( Char ch ) {

		super.occupyCell( ch );

		if (map[bottomDoor] != Terrain.LOCKED_DOOR && map[topDoor] == Terrain.LOCKED_DOOR
				&& ch.pos < bottomDoor && ch == Dungeon.hero) {

			seal();

		}
	}

	@Override
	public void seal() {
		super.seal();
		Statistics.qualifiedForBossChallengeBadge = true;

		//moves intelligent allies with the hero, preferring closer pos to entrance door
		int doorPos = pointToCell(new Point(arena.left + arena.width()/2, arena.bottom));
		Mob.holdAllies(this, doorPos);
		Mob.restoreAllies(this, Dungeon.hero.pos, doorPos);

		DwarfKing boss = new DwarfKing();
		boss.state = boss.WANDERING;
		boss.pos = pointToCell(arena.center());
		GameScene.add( boss );
		boss.beckon(Dungeon.hero.pos);

		if (heroFOV[boss.pos]) {
			boss.notice();
			boss.sprite.alpha( 0 );
			boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
		}

		set( bottomDoor, Terrain.LOCKED_DOOR );
		GameScene.updateMap( bottomDoor );
		Dungeon.observe();

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				Music.INSTANCE.play(Assets.Music.CITY_BOSS, true);
			}
		});
	}

	@Override
	public void unseal() {
		super.unseal();

		set( bottomDoor, Terrain.DOOR );
		GameScene.updateMap( bottomDoor );

		set( topDoor, Terrain.DOOR );
		GameScene.updateMap( topDoor );

		if (Imp.Quest.isCompleted()) {
			spawnShop();
		}
		Dungeon.observe();

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				Music.INSTANCE.end();
			}
		});
	}

	private void spawnShop(){
		while (impShop.itemCount() >= 7*(impShop.height()-2)){
			impShop.bottom++;
		}
		impShop.spawnShop(this);
	}

	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}

	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals( ) {
		super.addVisuals();
		CityLevel.addCityVisuals(this, visuals);
		return visuals;
	}

	public static class CustomGroundVisuals extends CustomTilemap {

		{
			texture = Assets.Environment.CITY_BOSS;
			tileW = 15;
			tileH = 48;
		}

		private static final int STAIR_ROWS = 7;

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];

			int[] map = Dungeon.level.map;

			int stairsTop = -1;

			//upper part of the level, mostly demon halls tiles
			for (int i = tileW; i < tileW*22; i++){

				if (map[i] == Terrain.EXIT && stairsTop == -1){
					stairsTop = i;
				}

				//pillars
				if (map[i] == Terrain.WALL && map[i-tileW] == Terrain.CHASM){
					data[i] = 13*8 + 6;
					data[++i] = 13*8 + 7;
				} else if (map[i] == Terrain.WALL && map[i-tileW] == Terrain.WALL){
					data[i] = 14*8 + 6;
					data[++i] = 14*8 + 7;
				} else if (i > tileW && map[i] == Terrain.CHASM && map[i-tileW] == Terrain.WALL) {
					data[i] = 15*8 + 6;
					data[++i] = 15*8 + 7;

				//imp's pedestal
				} else if (map[i] == Terrain.PEDESTAL) {
					data[i] = 12*8 + 5;

				//skull piles
				} else if (map[i] == Terrain.STATUE) {
					data[i] = 15*8 + 5;

				//ground tiles
				} else if (map[i] == Terrain.EMPTY || map[i] == Terrain.EMPTY_DECO
						|| map[i] == Terrain.EMBERS || map[i] == Terrain.GRASS
						|| map[i] == Terrain.HIGH_GRASS || map[i] == Terrain.FURROWED_GRASS){

					//final ground stiching with city tiles
					if (i/tileW == 21){
						data[i] = 11*8 + 0;
						data[++i] = 11*8 + 1;
						data[++i] = 11*8 + 2;
						data[++i] = 11*8 + 3;
						data[++i] = 11*8 + 4;
						data[++i] = 11*8 + 5;
						data[++i] = 11*8 + 6;
					} else {

						//regular ground tiles
						if (map[i - 1] == Terrain.CHASM) {
							data[i] = 12 * 8 + 1;
						} else if (map[i + 1] == Terrain.CHASM) {
							data[i] = 12 * 8 + 3;
						} else if (map[i] == Terrain.EMPTY_DECO) {
							data[i] = 12 * 8 + 4;
						} else {
							data[i] = 12 * 8 + 2;
						}
					}

					//otherwise no tile here
				} else {
					data[i] = -1;
				}
			}

			//custom for stairs
			for (int i = 0; i < STAIR_ROWS; i++){
				for (int j = 0; j < 7; j++){
					data[stairsTop+j] = (i+4)*8 + j;
				}
				stairsTop += tileW;
			}

			//lower part: statues, pedestals, and carpets
			for (int i = tileW*22; i < tileW * tileH; i++){

				//pedestal spawners
				if (map[i] == Terrain.PEDESTAL){
					data[i] = 13*8 + 4;

				//statues that should face left instead of right
				} else if (map[i] == Terrain.STATUE && i%tileW > 7) {
					data[i] = 15 * 8 + 4;

				//carpet tiles
				} else if (map[i] == Terrain.EMPTY_SP) {
					//top row of DK's throne
					if (map[i + 1] == Terrain.EMPTY_SP && map[i + tileW] == Terrain.EMPTY_SP) {
						data[i] = 13 * 8 + 1;
						data[++i] = 13 * 8 + 2;
						data[++i] = 13 * 8 + 3;

						//mid row of DK's throne
					}else if (map[i + 1] == Terrain.SIGN) {
						data[i] = 14 * 8 + 1;
						data[++i] = 14 * 8 + 2;
						data[++i] = 14 * 8 + 3;

						//bottom row of DK's throne
					} else if (map[i+1] == Terrain.EMPTY_SP && map[i-tileW] == Terrain.EMPTY_SP){
						data[i] = 15*8 + 1;
						data[++i] = 15*8 + 2;
						data[++i] = 15*8 + 3;

					//otherwise entrance carpet
					} else if (map[i-tileW] != Terrain.EMPTY_SP){
						data[i] = 13*8 + 0;
					} else if (map[i+tileW] != Terrain.EMPTY_SP){
						data[i] = 15*8 + 0;
					} else {
						data[i] = 14*8 + 0;
					}

					//otherwise no tile here
				} else {
					data[i] = -1;
				}
			}

			v.map( data, tileW );
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			int cell = (this.tileX + tileX) + (this.tileY + tileY)*tileW;

			//demon halls tiles
			if (cell < Dungeon.level.width*22){
				if (Dungeon.level.map[cell] == Terrain.STATUE){
					return Messages.get(HallsLevel.class, "statue_name");
				}

				//DK arena tiles
			} else {
				if (Dungeon.level.map[cell] == Terrain.SIGN){
					return Messages.get(CityBossLevel.class, "throne_name");
				} else if (Dungeon.level.map[cell] == Terrain.PEDESTAL){
					return Messages.get(CityBossLevel.class, "summoning_name");
				}
			}

			return super.name(tileX, tileY);
		}

		@Override
		public String desc(int tileX, int tileY) {
			int cell = (this.tileX + tileX) + (this.tileY + tileY)*tileW;

			//demon halls tiles
			if (cell < Dungeon.level.width*22){
				if (Dungeon.level.map[cell] == Terrain.EXIT){
					return Messages.get(HallsLevel.class, "exit_desc");
				} else if (Dungeon.level.map[cell] == Terrain.STATUE){
					return Messages.get(HallsLevel.class, "statue_desc");
				} else if (Dungeon.level.map[cell] == Terrain.EMPTY_DECO){
					return "";
				}

			//DK arena tiles
			} else {
				if (Dungeon.level.map[cell] == Terrain.SIGN){
					return Messages.get(CityBossLevel.class, "throne_desc");
				} else if (Dungeon.level.map[cell] == Terrain.PEDESTAL){
					return Messages.get(CityBossLevel.class, "summoning_desc");
				}
			}

			return super.desc(tileX, tileY);
		}
	}

	public static class CustomWallVisuals extends CustomTilemap {
		{
			texture = Assets.Environment.CITY_BOSS;
			tileW = 15;
			tileH = 48;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];

			int[] map = Dungeon.level.map;

			int shadowTop = -1;

			//upper part of the level, mostly demon halls tiles
			for (int i = tileW; i < tileW*21; i++) {

				if (map[i] == Terrain.EXIT && shadowTop == -1){
					shadowTop = i - tileW*4;
				}

				//pillars
				if (map[i] == Terrain.CHASM && map[i+tileW] == Terrain.WALL) {
					data[i] = 12*8 + 6;
					data[++i] = 12*8 + 7;
				} else if (map[i] == Terrain.WALL && map[i-tileW] == Terrain.CHASM) {
					data[i] = 13 * 8 + 6;
					data[++i] = 13 * 8 + 7;

				//skull tops
				} else if (map[i+tileW] == Terrain.STATUE) {
					data[i] = 14*8 + 5;

				//otherwise no tile here
				} else {
					data[i] = -1;
				}
			}

			//custom shadow  for stairs
			for (int i = 0; i < 8; i++){
				if (i < 4){
					data[shadowTop] = i*8 + 0;
					data[shadowTop+1] = data[shadowTop+2] = data[shadowTop+3] = data[shadowTop+4] =
							data[shadowTop+5] = data[shadowTop+6] = i*8 + 1;
					data[shadowTop+7] = i*8 + 2;
				} else {
					int j = i - 4;
					data[shadowTop] = j*8 + 3;
					data[shadowTop+1] = data[shadowTop+2] = data[shadowTop+3] = data[shadowTop+4] =
							data[shadowTop+5] = data[shadowTop+6] = j*8 + 4;
					data[shadowTop+7] = j*8 + 5;
				}

				shadowTop += tileW;
			}

			//lower part. Statues and DK's throne
			for (int i = tileW*21; i < tileW * tileH; i++){

				//Statues that need to face left instead of right
				if (map[i] == Terrain.STATUE && i%tileW > 7){
					data[i-tileW] = 14*8 + 4;
				} else if (map[i] == Terrain.SIGN){
					data[i-tileW] = 13*8 + 5;
				}

				//always no tile here (as the above statements are modifying previous tiles)
				data[i] = -1;
			}

			v.map( data, tileW );
			return v;
		}
	}
}
