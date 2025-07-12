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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoBridgeRoom;
import com.watabou.utils.PathFinder;

public class RegionDecoBridgeEntranceRoom extends RegionDecoBridgeRoom {

	@Override
	public int minWidth() {
		return Math.max(8, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(8, super.minHeight());
	}

	@Override
	public boolean isEntrance() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int entrance;
		boolean valid;
		do {
			valid = true;
			entrance = level.pointToCell(random(2));

			if (spaceRect.inside(level.cellToPoint(entrance))){
				valid = false;
			} else {
				for (int i : PathFinder.NEIGHBOURS8){
					if (level.map[entrance+i] == Terrain.REGION_DECO_ALT){
						valid = false;
					}
				}
			}

		} while (!valid);

		Painter.set( level, entrance, Terrain.ENTRANCE );
		level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));
	}

}
