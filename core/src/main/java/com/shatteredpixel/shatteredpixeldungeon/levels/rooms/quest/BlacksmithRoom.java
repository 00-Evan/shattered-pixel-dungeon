/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.Arrays;

public class BlacksmithRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 8);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 8);
	}

	public void paint(Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		Painter.fill( level, this, 2, Terrain.EMPTY_SP );
		Painter.fill( level, left+2, top+1, width()-4, 1, Terrain.REGION_DECO_ALT);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			Painter.drawInside( level, this, door, 1, Terrain.EMPTY );
		}

		Painter.fill( level, this, 2, Terrain.EMPTY_SP );
		
		Blacksmith npc = new Blacksmith();
		npc.pos = left + 3 + (level.width()*(top+3));
		level.mobs.add( npc );

		Painter.set(level, npc.pos+1, Terrain.CUSTOM_DECO_WTR);
		Painter.set(level, npc.pos+1-level.width(), Terrain.CUSTOM_DECO);
		Painter.set(level, npc.pos-level.width(), Terrain.CUSTOM_DECO);
		Painter.set(level, npc.pos-1-level.width(), Terrain.CUSTOM_DECO);

		int equipPos = right-3 + (bottom-3)*level.width();
		if (height() == 8){
			equipPos += level.width();
		}
		for (int i=0; i < 2; i++) {
			level.drop(
					Generator.random( Random.oneOf(
							Generator.Category.ARMOR,
							Generator.Category.WEAPON,
							Generator.Category.MISSILE
					) ), equipPos );
			Painter.set(level, equipPos, Terrain.PEDESTAL);
			equipPos--;
		}

		boolean entranceOnLeft = Random.Int(2) == 0;
		for (Door d : connected.values()){
			if (d.y <= top+2){
				if (d.x <= left+1){
					entranceOnLeft = false;
				} else if (d.x >= right-1){
					entranceOnLeft = true;
				}
			}
		}

		int entrancePos = (top+1)*level.width() + (entranceOnLeft ? left+1 : right-1);
		level.transitions.add(new LevelTransition(level,
				entrancePos,
				LevelTransition.Type.BRANCH_EXIT,
				Dungeon.depth,
				Dungeon.branch + 1,
				LevelTransition.Type.BRANCH_ENTRANCE));
		Painter.set(level, entrancePos, Terrain.EXIT);

		CustomTilemap vis = new QuestEntrance();
		vis.pos(entrancePos, level);
		level.customTiles.add(vis);

		vis = new SmithyVisuals();
		vis.setRect(left+2, top+2, width()-4, height()-4);
		level.customTiles.add(vis);

		FurnaceOverhang furnace = new FurnaceOverhang();
		Point p = level.cellToPoint(npc.pos);
		furnace.setRect(p.x-1, p.y-2, 1, 1);
		level.customWalls.add(furnace);
	}

	@Override
	public boolean canConnect(Room r) {
		if (r.isExit()){
			//prevents confusion where smith exit and floor exit are very close to each other.
			return false;
		}
		return super.canConnect(r);
	}

	@Override
	public int maxConnections(int direction) {
		if (direction == TOP){
			return 1;
		} else {
			return super.maxConnections(direction);
		}
	}

	//single top connection can only be at corners, no left/right connection at corner
	@Override
	public boolean canConnect(Point p) {
		if (p.y == top && p.x != left+1 && p.x != right-1){
			return false;
		} else if (p.y == top+1){
			return false;
		}
		return super.canConnect(p);
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return false;
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return l.map[l.pointToCell(p)] == Terrain.EMPTY;
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		return false;
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

	public static class QuestEntrance extends CustomTilemap {

		{
			texture = Assets.Environment.CAVES_QUEST;

			tileW = tileH = 1;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map( new int[]{0}, 1 );
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			return Messages.get(this, "desc");
		}

	}

	public static class SmithyVisuals extends CustomTilemap {

		{
			texture = Assets.Environment.CAVES_QUEST;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				//smithy
				if (i == 0)                                 data[i] = 7;
				else if (i == 1)                            data[i] = 16;
				else if (i == 2)                            data[i] = 17;
				else if (i / tileW == 1 && i % tileW == 2)  data[i] = 18;
				//floor and pedestal tiles
				else {
					int cell = tileX + i % tileW;
					cell += (tileY + i / tileW)*Dungeon.level.width();
					if (Dungeon.level.map[cell] == Terrain.EMPTY_SP) {
						if (i >= data.length - tileW) {
							if (i % tileW == 0) data[i] = 12;
							else if (i % tileW == tileW - 1) data[i] = 14;
							else data[i] = 13;
						} else {
							if (i % tileW == 0) data[i] = 8;
							else if (i % tileW == tileW - 1) data[i] = 10;
							else data[i] = -1;
						}
					} else if (Dungeon.level.map[cell] == Terrain.PEDESTAL) {
						if (i >= data.length - tileW)   data[i] = 20;
						else                            data[i] = 21;
					} else {
						data[i] = -1;
					}
				}
			}
			v.map( data, tileW );
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			int cell = (this.tileX+tileX) + (this.tileY+tileY)*Dungeon.level.width();
			if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO || Dungeon.level.map[cell] == Terrain.CUSTOM_DECO_WTR){
				return Messages.get(this, "name");
			}
			return super.name(tileX, tileY);
		}

		@Override
		public String desc(int tileX, int tileY) {
			int cell = (this.tileX+tileX) + (this.tileY+tileY)*Dungeon.level.width();
			if (Dungeon.level.map[cell] == Terrain.CUSTOM_DECO || Dungeon.level.map[cell] == Terrain.CUSTOM_DECO_WTR){
				return Messages.get(this, "desc");
			}
			return super.desc(tileX, tileY);
		}
	}

	public static class FurnaceOverhang extends CustomTilemap{

		{
			texture = Assets.Environment.CAVES_QUEST;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			Arrays.fill(data, 3);
			v.map( data, tileW );
			return v;
		}

	}

}
