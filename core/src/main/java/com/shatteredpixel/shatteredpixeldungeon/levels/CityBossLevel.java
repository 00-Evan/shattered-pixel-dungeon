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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.noosa.Group;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class CityBossLevel extends Level {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	
	private static final int TOP			= 2;
	private static final int HALL_WIDTH		= 7;
	private static final int HALL_HEIGHT	= 15;
	private static final int CHAMBER_HEIGHT	= 4;

	private static final int WIDTH = 32;
	
	private static final int LEFT	= (WIDTH - HALL_WIDTH) / 2;
	private static final int CENTER	= LEFT + HALL_WIDTH / 2;
	
	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
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
		
		Painter.fill( this, LEFT, TOP, HALL_WIDTH, HALL_HEIGHT, Terrain.EMPTY );
		Painter.fill( this, CENTER, TOP, 1, HALL_HEIGHT, Terrain.EMPTY_SP );
		
		int y = TOP + 1;
		while (y < TOP + HALL_HEIGHT) {
			map[y * width() + CENTER - 2] = Terrain.STATUE_SP;
			map[y * width() + CENTER + 2] = Terrain.STATUE_SP;
			y += 2;
		}
		
		int left = pedestal( true );
		int right = pedestal( false );
		map[left] = map[right] = Terrain.PEDESTAL;
		for (int i=left+1; i < right; i++) {
			map[i] = Terrain.EMPTY_SP;
		}
		
		exit = (TOP - 1) * width() + CENTER;
		map[exit] = Terrain.LOCKED_EXIT;
		
		arenaDoor = (TOP + HALL_HEIGHT) * width() + CENTER;
		map[arenaDoor] = Terrain.DOOR;
		
		Painter.fill( this, LEFT, TOP + HALL_HEIGHT + 1, HALL_WIDTH, CHAMBER_HEIGHT, Terrain.EMPTY );
		Painter.fill( this, LEFT, TOP + HALL_HEIGHT + 1, HALL_WIDTH, 1, Terrain.BOOKSHELF);
		map[arenaDoor + width()] = Terrain.EMPTY;
		Painter.fill( this, LEFT, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.BOOKSHELF );
		Painter.fill( this, LEFT + HALL_WIDTH - 1, TOP + HALL_HEIGHT + 1, 1, CHAMBER_HEIGHT, Terrain.BOOKSHELF );
		
		entrance = (TOP + HALL_HEIGHT + 3 + Random.Int( CHAMBER_HEIGHT - 2 )) * width() + LEFT + (/*1 +*/ Random.Int( HALL_WIDTH-2 ));
		map[entrance] = Terrain.ENTRANCE;
		
		return true;
	}
	
	@Override
	protected void decorate() {

		for (int i=0; i < length() - width(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			} else if (map[i] == Terrain.WALL
					&& DungeonTileSheet.floorTile(map[i + width()])
					&& Random.Int( 21 - Dungeon.depth ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		int sign = arenaDoor + 2*width() + 1;
		map[sign] = Terrain.SIGN;
	}
	
	public int pedestal( boolean left ) {
		if (left) {
			return (TOP + HALL_HEIGHT / 2) * width() + CENTER - 2;
		} else {
			return (TOP + HALL_HEIGHT / 2) * width() + CENTER + 2;
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
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos =
					Random.IntRange( LEFT + 1, LEFT + HALL_WIDTH - 2 ) +
					Random.IntRange( TOP + HALL_HEIGHT + 1, TOP + HALL_HEIGHT  + CHAMBER_HEIGHT ) * width();
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell() {
		int cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]){
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if (!enteredArena && outsideEntraceRoom( cell ) && hero == Dungeon.hero) {
			
			enteredArena = true;
			seal();
			
			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.WANDERING;
			int count = 0;
			do {
				boss.pos = Random.Int( length() );
			} while (
				!passable[boss.pos] ||
				!outsideEntraceRoom( boss.pos ) ||
				(Dungeon.visible[boss.pos] && count++ < 20));
			GameScene.add( boss );

			if (Dungeon.visible[boss.pos]) {
				boss.notice();
				boss.sprite.alpha( 0 );
				boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
			}

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
	
	private boolean outsideEntraceRoom( int cell ) {
		return cell / width() < arenaDoor / width();
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
}
