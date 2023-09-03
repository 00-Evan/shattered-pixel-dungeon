/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CaveRoom;

public class MineSmallRoom extends CaveRoom {

	@Override
	public int minWidth() { return Math.max(6, super.minWidth()); }

	@Override
	public int minHeight() { return Math.max(6, super.minHeight()); }

	@Override
	public float[] sizeCatProbs() {
		return new float[]{1, 0, 0};
	}

	@Override
	protected float fill() {
		return 0.40f;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		//TODO per-quest details here

	}
}
