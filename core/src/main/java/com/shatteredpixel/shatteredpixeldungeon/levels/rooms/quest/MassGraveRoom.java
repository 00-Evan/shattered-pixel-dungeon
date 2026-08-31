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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class MassGraveRoom extends SpecialRoom {

	@Override
	public int minWidth() { return 11; }
	public int maxWidth() { return 11; }
	public int minHeight() { return 10; }
	public int maxHeight() { return 10; }

	public void paint(Level level){

		Door entrance = entrance();
		entrance.set(Door.Type.BARRICADE);
		level.addItemToSpawn(new PotionOfLiquidFlame());

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.CUSTOM_DECO_EMPTY);

		Painter.fill(level, left+1, top+1, 3, 1, Terrain.WALL);
		Painter.fill(level, left+1, top+2, 2, 1, Terrain.WALL);
		Painter.fill(level, right-3, top+1, 3, 1, Terrain.WALL);
		Painter.fill(level, right-2, top+2, 2, 1, Terrain.WALL);
		Painter.set(level, left+5, top, Terrain.WALL_DECO);

		Painter.set(level, left+3, top+2, Terrain.STATUE);
		Painter.set(level, right-3, top+2, Terrain.STATUE);

		MassGraveDeco b = new MassGraveDeco();
		b.setRect(left+1, top, width()-2, height()-1);
		level.customTiles.add(b);

		StatueRaised statue = new StatueRaised();
		statue.setRect(left+3, top+2, 1, 1);
		level.customTerrain.add(statue);

		statue = new StatueRaised();
		statue.setRect(right-3, top+2, 1, 1);
		level.customTerrain.add(statue);

		//50% 1 skeleton, 50% 2 skeletons
		for (int i = 0; i <= Random.Int(2); i++){
			Skeleton skele = new Skeleton();

			int pos;
			Point p;
			do {
				p = random(1);
				//pull in range for top two rows
				if (p.y <= top+2) p.x = Random.IntRange(left+4, right-4);
				pos = level.pointToCell(p);
			} while (p.y > top+3 || level.findMob(pos) != null);
			skele.pos = pos;
			level.mobs.add( skele );
		}

		ArrayList<Item> items = new ArrayList<>();
		//100% corpse dust, 2x100% 1 coin, 2x30% coins, 1x60% random item, 1x30% armor
		items.add(new CorpseDust());
		items.add(new Gold(1));
		items.add(new Gold(1));
		if (Random.Float() <= 0.3f) items.add(new Gold());
		if (Random.Float() <= 0.3f) items.add(new Gold());
		if (Random.Float() <= 0.6f) items.add(Generator.random());
		if (Random.Float() <= 0.3f) items.add(Generator.randomArmor());

		for (Item item : items){
			int pos;
			Point p;
			do {
				p = random(1);
				//pull in range for top two rows
				if (p.y <= top+2) p.x = Random.IntRange(left+4, right-4);
				pos = level.pointToCell(p);
			} while (p.y > top+5 || level.heaps.get(pos) != null);
			Heap h = level.drop(item, pos);
			h.setHauntedIfCursed();
			h.type = Heap.Type.SKELETON;
		}
	}

	@Override
	public boolean canConnect(int direction) {
		return super.canConnect(direction) && direction == BOTTOM;
	}

	@Override
	public boolean canConnect(Point p) {
		return Math.abs(p.x- center().x) <= 2;
	}

	@Override
	public boolean canConnect(Room r) {
		if (r.isEntrance()){
			return false;
		}

		//must have at least 3 rooms between it and the entrance room
		for (Room r1 : r.connected.keySet()) {
			if (r1.isEntrance()){
				return false;
			}
			for (Room r2 : r1.connected.keySet()) {
				if (r2.isEntrance()){
					return false;
				}
				for (Room r3 : r2.connected.keySet()) {
					if (r3.isEntrance()){
						return false;
					}
				}
			}
		}

		return super.canConnect(r);
	}

	// for pre-v4.0 saves, which still use the old room layout
	public static class Bones extends CustomTilemap {

		private static final int WALL_OVERLAP   = 3;
		private static final int FLOOR          = 7;

		{
			texture = Assets.Environment.PRISON_QUEST;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i < tileW)  data[i] = WALL_OVERLAP;
				else            data[i] = FLOOR;
			}
			v.map( data, tileW );
			return v;
		}

		@Override
		public Image image(int tileX, int tileY) {
			if (tileY == 0) return null;
			else            return super.image(tileX, tileY);
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(MassGraveDeco.class, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			return Messages.get(MassGraveDeco.class, "desc");
		}
	}

	public static class MassGraveDeco extends CustomTilemap {

		{
			texture = Assets.Environment.PRISON_QUEST;

			tileW = 9;
			tileH = 9;
		}

		private static byte[] render = new byte[]{
				0, 0, 0, 1, 1, 1, 0, 0, 0,
				0, 0, 1, 1, 1, 1, 1, 0, 0,
				1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 0, 0, 0, 1, 1, 1,
				1, 1, 0, 0, 0, 0, 0, 1, 1
		};

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = mapSimpleImage( 5, 0, 256);
			for (int i = 0; i < data.length; i++){
				if (render[i] == 0) data[i] = -1;
			}
			v.map(data, tileW);
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			if (render[tileX + tileY*tileH] == 1) {
				return Messages.get(this, "name");
			} else {
				return super.name(tileX, tileY);
			}
		}

		@Override
		public String desc(int tileX, int tileY) {
			if (render[tileX + tileY*tileH] == 1) {
				return Messages.get(this, "desc");
			} else {
				return super.desc(tileX, tileY);
			}
		}

	}

	public static class StatueRaised extends CustomTilemap {

		{
			texture = Assets.Environment.PRISON_QUEST;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			Arrays.fill(data,4); //constant for statues in tilesheet
			v.map(data, tileW);
			return v;
		}
	}
}
