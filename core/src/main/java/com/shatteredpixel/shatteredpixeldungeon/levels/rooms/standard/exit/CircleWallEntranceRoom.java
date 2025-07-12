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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CircleWallRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class CircleWallEntranceRoom extends CircleWallRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 11);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 11);
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public boolean isEntrance() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		Point p = center();

		int cell = level.pointToCell(p);
		for (int i : PathFinder.NEIGHBOURS8){
			if (level.map[cell+2*i] == Terrain.WALL){
				Painter.set(level, cell+i, Terrain.EMPTY);
			}
		}
		Painter.set(level, p, Terrain.ENTRANCE);
		level.transitions.add(new LevelTransition(level, level.pointToCell(p), LevelTransition.Type.REGULAR_ENTRANCE));

		int xDir = 0, yDir = 0;
		if (Random.Int(2) == 0) {
			xDir = Random.Int(2) == 0 ? 1 : -1;
		} else {
			yDir = Random.Int(2) == 0 ? 1 : -1;
		}

		p.x += 2*xDir;
		p.y += 2*yDir;

		while (level.map[level.pointToCell(p)] == Terrain.WALL){
			Painter.set(level, p, Terrain.EMPTY);
			p.x += xDir;
			p.y += yDir;
		}
	}
}
