/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;

public class SkullsRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{9, 3, 1};
	}

	@Override
	public void paint(Level level) {

		int minDim = Math.min(width(), height());

		Painter.fill( level, this, Terrain.WALL );

		if (minDim >= 9) {
			Painter.fillEllipse(level, this, 2, Terrain.EMPTY);
		} else {
			Painter.fill(level, this, 2, Terrain.EMPTY);
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			if (door.x == left || door.x == right){
				Painter.drawInside(level, this, door, (width() - 3) / 2, Terrain.EMPTY);
			} else {
				Painter.drawInside(level, this, door, (height() - 3) / 2, Terrain.EMPTY);
			}
		}

		boolean oddWidth = width() % 2 == 1;
		boolean oddHeight = height() % 2 == 1;

		if (minDim >= 12){

			Painter.fillEllipse(level, this, 5, Terrain.STATUE);
			Painter.fillEllipse(level, this, 6, Terrain.WALL);

		} else {

			Painter.fill(level,
					left + width()/2 + (oddWidth ? 0 : -1),
					top + height()/2 + (oddHeight ? 0 : -1),
					oddWidth ? 1 : 2,
					oddHeight ? 1 : 2,
					Terrain.STATUE);

		}

	}
}
