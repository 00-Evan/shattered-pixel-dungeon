/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

public class RitualRoom extends PatchRoom {

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
		return new float[]{6, 3, 1};
	}

	@Override
	protected float fill() {
		//fill scales from ~30% at 4x4, to ~60% at 18x18
		// normal   ~30% to ~40%
		// large    ~40% to ~50%
		// giant    ~50% to ~60%
		// however,  the inner 7x7 is overridden, so overall fill is much lower
		int scale = Math.min(width()*height(), 18*18);
		return 0.30f + scale/1024f;
	}

	@Override
	protected int clustering() {
		return 0;
	}

	@Override
	protected boolean ensurePath() {
		return connected.size() > 0;
	}

	@Override
	protected boolean cleanEdges() {
		return true;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Point c = center();

		setupPatch(level);
		fillPatch(level, Terrain.REGION_DECO);

		Painter.fill(level, c.x - 3, c.y - 3, 7, 7, Terrain.EMPTY);

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
