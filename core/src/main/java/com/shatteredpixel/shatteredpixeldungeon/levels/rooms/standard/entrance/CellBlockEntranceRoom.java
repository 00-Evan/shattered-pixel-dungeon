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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CellBlockRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

public class CellBlockEntranceRoom extends CellBlockRoom {

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

		while (true){
			Point p = random(3);

			if (level.map[level.pointToCell(p)] == Terrain.EMPTY_SP){
				boolean valid = true;
				for (int i : PathFinder.NEIGHBOURS8){
					if (level.map[level.pointToCell(p)+i] == Terrain.DOOR){
						valid = false;
					}
				}

				if (valid){
					int entrance = level.pointToCell(p);
					Painter.set( level, entrance, Terrain.ENTRANCE_SP );

					level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
					return;
				}
			}
		}

	}

}
