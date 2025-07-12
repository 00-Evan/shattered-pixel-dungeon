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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StatueLineRoom;
import com.watabou.utils.Point;

public class StatueLineExitRoom extends StatueLineRoom {

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int exit;
		do {
			exit = level.pointToCell(random(3));
		} while (level.findMob(exit) != null);

		Painter.set(level, exit, Terrain.EXIT);
		level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
	}


}
