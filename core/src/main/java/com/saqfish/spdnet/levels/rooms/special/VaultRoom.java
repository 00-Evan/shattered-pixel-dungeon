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

package com.saqfish.spdnet.levels.rooms.special;

import com.saqfish.spdnet.Challenges;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.mobs.CrystalMimic;
import com.saqfish.spdnet.actors.mobs.Mimic;
import com.saqfish.spdnet.items.Generator;
import com.saqfish.spdnet.items.Heap;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.keys.CrystalKey;
import com.saqfish.spdnet.items.keys.IronKey;
import com.saqfish.spdnet.levels.Level;
import com.saqfish.spdnet.levels.Terrain;
import com.saqfish.spdnet.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class VaultRoom extends SpecialRoom {

	//fixed size to improve presentation and provide space for crystal mimics
	@Override
	public int minHeight() { return 7; }
	public int maxHeight() { return 7; }
	public int minWidth() { return 7; }
	public int maxWidth() { return 7; }

	public void paint( Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		Painter.fill( level, this, 2, Terrain.EMPTY );
		
		int c = level.pointToCell(center());
		Random.shuffle(prizeClasses);
		
		Item i1, i2;
		i1 = prize();
		i2 = prize();

		int i1Pos, i2Pos;
		int doorPos = level.pointToCell(entrance());
		do {
			int neighbourIdx = Random.Int(PathFinder.CIRCLE8.length);
			i1Pos = c + PathFinder.CIRCLE8[neighbourIdx];
			i2Pos = c + PathFinder.CIRCLE8[(neighbourIdx+4)%8];
		} while (level.adjacent(i1Pos, doorPos) || level.adjacent(i2Pos, doorPos));

		level.drop( i1, i1Pos ).type = Heap.Type.CRYSTAL_CHEST;
		if (Random.Int(10) == 0){
			level.mobs.add(Mimic.spawnAt(i2Pos, i2, CrystalMimic.class));
		} else {
			level.drop(i2, i2Pos).type = Heap.Type.CRYSTAL_CHEST;
		}
		Painter.set(level, i1Pos, Terrain.PEDESTAL);
		Painter.set(level, i2Pos, Terrain.PEDESTAL);

		level.addItemToSpawn( new CrystalKey( Dungeon.depth ) );
		
		entrance().set( Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey( Dungeon.depth ) );
	}
	
	private Item prize() {
		Generator.Category cat = prizeClasses.remove(0);
		prizeClasses.add(cat);
		Item prize;
		do {
			prize = Generator.random(cat);
		} while (prize == null || Challenges.isItemBlocked(prize));
		return prize;
	}
	
	private ArrayList<Generator.Category> prizeClasses = new ArrayList<>(
			Arrays.asList(Generator.Category.WAND,
					Generator.Category.RING,
					Generator.Category.ARTIFACT));
}
