/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

//TODO, limit connections on corners
public class StudyRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 7);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 7);
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		Painter.fill( level, this, 1 , Terrain.BOOKSHELF );
		Painter.fill( level, this, 2 , Terrain.EMPTY_SP );
		
		for (Point door : connected.values()) {
			if (door.x == left) {
				Painter.set( level, door.x + 1, door.y, Terrain.EMPTY_SP );
				Painter.set( level, door.x + 2, door.y, Terrain.EMPTY_SP );
			} else if (door.x == right) {
				Painter.set( level, door.x - 1, door.y, Terrain.EMPTY_SP );
				Painter.set( level, door.x - 2, door.y, Terrain.EMPTY_SP );
			} else if (door.y == top) {
				Painter.set( level, door.x, door.y + 1, Terrain.EMPTY_SP );
				Painter.set( level, door.x, door.y + 2, Terrain.EMPTY_SP );
			} else if (door.y == bottom) {
				Painter.set( level, door.x , door.y - 1, Terrain.EMPTY_SP );
				Painter.set( level, door.x , door.y - 2, Terrain.EMPTY_SP );
			}
		}
		Point center = center();
		Painter.set( level, center, Terrain.PEDESTAL );
		if (Random.Int(2) != 0){
			Item prize = level.findPrizeItem();
			if (prize != null) {
				level.drop(prize, (center.x + center.y * level.width()));
				return;
			}
		}
		
		level.drop(Generator.random( Random.oneOf(
				Generator.Category.POTION,
				Generator.Category.SCROLL)), (center.x + center.y * level.width()));
	}
}
