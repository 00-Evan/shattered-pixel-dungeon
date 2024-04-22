/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

public class RitualRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 9);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 9);
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{2, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Point c = center();

		Painter.set(level, c.x-2, c.y-1, Terrain.STATUE);
		Painter.set(level, c.x-1, c.y-2, Terrain.STATUE);
		Painter.set(level, c.x+2, c.y-1, Terrain.STATUE);
		Painter.set(level, c.x+1, c.y-2, Terrain.STATUE);
		Painter.set(level, c.x-2, c.y+1, Terrain.STATUE);
		Painter.set(level, c.x-1, c.y+2, Terrain.STATUE);
		Painter.set(level, c.x+2, c.y+1, Terrain.STATUE);
		Painter.set(level, c.x+1, c.y+2, Terrain.STATUE);
		Painter.fill(level, c.x-1, c.y-1, 3, 3, Terrain.EMBERS);
		Painter.set(level, c, Terrain.PEDESTAL);

		if (width() >= 11 && height() >= 11){
			Painter.set(level, left+2, top+2, Terrain.STATUE);
			Painter.set(level, right-2, top+2, Terrain.STATUE);
			Painter.set(level, left+2, bottom-2, Terrain.STATUE);
			Painter.set(level, right-2, bottom-2, Terrain.STATUE);
			if (width() >= 13 && height() >= 13){
				Painter.set(level, left+3, top+3, Terrain.STATUE);
				Painter.set(level, right-3, top+3, Terrain.STATUE);
				Painter.set(level, left+3, bottom-3, Terrain.STATUE);
				Painter.set(level, right-3, bottom-3, Terrain.STATUE);
			}
		}

		placeloot(level, c);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	protected void placeloot(Level level, Point p){
		Item prize = Random.Int(2) == 0 ? level.findPrizeItem() : null;

		if (prize == null){
			prize = Generator.random( Random.oneOf(Generator.Category.POTION, Generator.Category.SCROLL));
		}

		level.drop(prize, level.pointToCell(p));
	}
}
