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

package com.saqfish.spdnet.levels.rooms.secret;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.items.Generator;
import com.saqfish.spdnet.items.Heap;
import com.saqfish.spdnet.items.keys.GoldenKey;
import com.saqfish.spdnet.items.potions.PotionOfLevitation;
import com.saqfish.spdnet.levels.Level;
import com.saqfish.spdnet.levels.Terrain;
import com.saqfish.spdnet.levels.painters.Painter;
import com.watabou.utils.Point;

public class SecretChestChasmRoom extends SecretRoom {
	
	//width and height are controlled here so that this room always requires 2 levitation potions
	
	@Override
	public int minWidth() {
		return 8;
	}
	
	@Override
	public int maxWidth() {
		return 9;
	}
	
	@Override
	public int minHeight() {
		return 8;
	}
	
	@Override
	public int maxHeight() {
		return 9;
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.CHASM);
		
		int chests = 0;
		
		Point p = new Point(left+3, top+3);
		Painter.set(level, p, Terrain.EMPTY_SP);
		level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;
		if (level.heaps.get(level.pointToCell(p)) != null) chests++;
		
		p.x = right-3;
		Painter.set(level, p, Terrain.EMPTY_SP);
		level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;
		if (level.heaps.get(level.pointToCell(p)) != null) chests++;
		
		p.y = bottom-3;
		Painter.set(level, p, Terrain.EMPTY_SP);
		level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;
		if (level.heaps.get(level.pointToCell(p)) != null) chests++;
		
		p.x = left+3;
		Painter.set(level, p, Terrain.EMPTY_SP);
		level.drop(Generator.random(), level.pointToCell(p)).type = Heap.Type.LOCKED_CHEST;
		if (level.heaps.get(level.pointToCell(p)) != null) chests++;
		
		p = new Point(left+1, top+1);
		Painter.set(level, p, Terrain.EMPTY_SP);
		if (chests > 0) {
			level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));
			chests--;
		}
		
		p.x = right-1;
		Painter.set(level, p, Terrain.EMPTY_SP);
		if (chests > 0) {
			level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));
			chests--;
		}
		
		p.y = bottom-1;
		Painter.set(level, p, Terrain.EMPTY_SP);
		if (chests > 0) {
			level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));
			chests--;
		}
		
		p.x = left+1;
		Painter.set(level, p, Terrain.EMPTY_SP);
		if (chests > 0) {
			level.drop(new GoldenKey(Dungeon.depth), level.pointToCell(p));
			chests--;
		}
		
		level.addItemToSpawn(new PotionOfLevitation());
		
		entrance().set(Door.Type.HIDDEN);
	}
}
