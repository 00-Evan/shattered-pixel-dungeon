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

package com.elementalpixel.elementalpixeldungeon.levels.rooms.special;


import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.actors.mobs.Skeleton;
import com.elementalpixel.elementalpixeldungeon.items.Generator;
import com.elementalpixel.elementalpixeldungeon.items.Gold;
import com.elementalpixel.elementalpixeldungeon.items.Heap;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.elementalpixel.elementalpixeldungeon.items.quest.CorpseDust;
import com.elementalpixel.elementalpixeldungeon.levels.Level;
import com.elementalpixel.elementalpixeldungeon.levels.Terrain;
import com.elementalpixel.elementalpixeldungeon.levels.painters.Painter;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MassGraveRoom extends SpecialRoom {
	
	@Override
	public int minWidth() { return 7; }
	
	@Override
	public int minHeight() { return 7; }
	
	public void paint(Level level){

		Door entrance = entrance();
		entrance.set(Door.Type.BARRICADE);
		level.addItemToSpawn(new PotionOfLiquidFlame());

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY_SP);

		Bones b = new Bones();

		b.setRect(left+1, top, width()-2, height()-1);
		level.customTiles.add(b);

		//50% 1 skeleton, 50% 2 skeletons
		for (int i = 0; i <= Random.Int(2); i++){
			Skeleton skele = new Skeleton();

			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.findMob(pos) != null);
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
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null);
			Heap h = level.drop(item, pos);
			h.setHauntedIfCursed();
			h.type = Heap.Type.SKELETON;
		}
	}

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
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			return Messages.get(this, "desc");
		}
	}
}
