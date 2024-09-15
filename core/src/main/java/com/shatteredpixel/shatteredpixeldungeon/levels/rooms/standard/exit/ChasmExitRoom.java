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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ChasmRoom;
import com.watabou.utils.PathFinder;

public class ChasmExitRoom extends ChasmRoom {

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{2, 1, 0};
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int exit;
		int tries = 30;
		boolean valid;
		do {
			exit = level.pointToCell(random(2));

			//need extra logic here as these rooms can spawn small and cramped in very rare cases
			if (tries-- > 0){
				valid = level.map[exit] != Terrain.CHASM && level.findMob(exit) == null;
			} else {
				valid = false;
				for (int i : PathFinder.NEIGHBOURS4){
					if (level.map[exit+i] != Terrain.CHASM){
						valid = true;
					}
				}
				valid = valid && level.findMob(exit) == null;
			}
		} while (!valid);
		Painter.set( level, exit, Terrain.EXIT );

		for (int i : PathFinder.NEIGHBOURS8){
			Painter.set( level, exit+i, Terrain.EMPTY );
		}

		level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
	}

}
