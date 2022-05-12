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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class CrystalChoiceRoom extends SpecialRoom {

	@Override
	public int minWidth() { return 7; }
	public int minHeight() { return 7; }

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );

		Door entrance = entrance();

		Room entry = new EmptyRoom();

		Room room1 = new EmptyRoom();
		Room room2 = new EmptyRoom();

		if (entrance.x == left){
			entry.set(left+1, top+1, left+2, bottom-1);

			room1.set(entry.right+2, top+1, right-1, center().y-1);
			room2.set(entry.right+2, room1.bottom+2, right-1, bottom-1);

			Painter.set(level, new Point(entry.right+1, (room1.top + room1.bottom + 1)/2), Terrain.CRYSTAL_DOOR);
			Painter.set(level, new Point(entry.right+1, (room2.top + room2.bottom)/2), Terrain.CRYSTAL_DOOR);

		} else if (entrance.y == top){
			entry.set(left+1, top+1, right-1, top+2);

			room1.set(left+1, entry.bottom+2, center().x-1, bottom-1);
			room2.set(room1.right+2, entry.bottom+2, right-1, bottom-1);

			Painter.set(level, new Point((room1.left + room1.right + 1)/2, entry.bottom+1), Terrain.CRYSTAL_DOOR);
			Painter.set(level, new Point((room2.left + room2.right)/2, entry.bottom+1), Terrain.CRYSTAL_DOOR);

		} else if (entrance.x == right){
			entry.set(right-2, top+1, right-1, bottom-1);
			Painter.drawLine(level, new Point(right-1, top+1), new Point(right-1, bottom-1), Terrain.EMPTY);

			room1.set(left+1, top+1, entry.left-2, center().y-1);
			room2.set(left+1, room1.bottom+2, entry.left-2, bottom-1);

			Painter.set(level, new Point(entry.left-1, (room1.top + room1.bottom + 1)/2), Terrain.CRYSTAL_DOOR);
			Painter.set(level, new Point(entry.left-1, (room2.top + room2.bottom)/2), Terrain.CRYSTAL_DOOR);

		} else if (entrance.y == bottom){
			entry.set(left+1, bottom-2, right-1, bottom-1);

			room1.set(left+1, top+1, center().x-1, entry.top-2);
			room2.set(room1.right+2, top+1, right-1, entry.top-2);

			Painter.set(level, new Point((room1.left + room1.right + 1)/2, entry.top-1), Terrain.CRYSTAL_DOOR);
			Painter.set(level, new Point((room2.left + room2.right)/2, entry.top-1), Terrain.CRYSTAL_DOOR);

		}

		Painter.fill(level, entry, Terrain.EMPTY);
		Painter.fill(level, room1, Terrain.EMPTY_SP);
		Painter.fill(level, room2, Terrain.EMPTY_SP);

		if (Random.Int(2) == 0){
			Room tmp = room1;
			room1 = room2;
			room2 = tmp;
		}

		int n = Random.NormalIntRange(3, 4);
		for (int i = 0; i < n; i++){
			Item reward = Generator.random(Random.oneOf(
					Generator.Category.POTION,
					Generator.Category.SCROLL
			));
			int pos;
			do {
				if (room1.square() >= 16){
					pos = level.pointToCell(room1.random(1));
				} else {
					pos = level.pointToCell(room1.random(0));
				}
			} while (level.heaps.get(pos) != null);
			level.drop(reward, pos);
		}

		Item hidden = Generator.random(Random.oneOf(
				Generator.Category.WAND,
				Generator.Category.RING,
				Generator.Category.ARTIFACT
		));
		Heap chest = level.drop(hidden, level.pointToCell(room2.center()));
		chest.type = Heap.Type.CHEST;
		//opening the chest is optional, so it doesn't count for exploration bonus
		chest.autoExplored = true;

		level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );

		entrance().set( Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey( Dungeon.depth ) );

	}
}
