/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class CellBlockRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 3, 1};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		Painter.fill( level, this, 3, Terrain.WALL );

		Rect internal = new EmptyRoom();
		internal.set(left+3, top+3, right-3, bottom-3);

		int rows = (internal.width() - 1)/3;
		int cols = (internal.height() - 1)/3;

		if (internal.height() == 11) cols--;
		if (internal.width() == 11) rows--;

		int w = (internal.width() - 2 - (rows-1))/rows;
		int h = (internal.height() - 2 - (cols-1))/cols;

		int Wspacing = (rows * w + (rows+1)) == internal.width() ? 1 : 2;
		int Hspacing = (cols * h + (cols+1)) == internal.height() ? 1 : 2;

		Boolean topBottomDoors = rows > cols || (rows == cols && Random.Int(2) == 0);

		if (rows == 1 || cols == 1){
			topBottomDoors = !topBottomDoors;
		}

		if (rows == 1 && cols == 1){
			topBottomDoors = null;
		}

		for (int x = 0; x < rows; x++){
			for (int y = 0; y < cols; y++){
				//no center room
				if (rows == 3 && cols == 3 && x == 1 && y == 1) continue;

				int left = internal.left + 1 + (x * (w + Wspacing));
				int top = internal.top + 1 + (y * (h + Hspacing));

				Painter.fill(level, left, top, w, h, Terrain.EMPTY_SP);

				if (topBottomDoors == null) {
					switch (Random.Int(4)){
						case 0: Painter.set(level, internal.left, internal.top + internal.height()/2, Terrain.DOOR); break;
						case 1: Painter.set(level, internal.left + internal.width()/2, internal.top, Terrain.DOOR); break;
						case 2: Painter.set(level, internal.right, internal.top + internal.height()/2, Terrain.DOOR);break;
						case 3: Painter.set(level, internal.left + internal.width()/2, internal.bottom, Terrain.DOOR); break;
					}
				} else if (topBottomDoors){
					if (y == 0){
						Painter.set(level, left + w/2, top-1, Terrain.DOOR);
					} else if (y == cols-1){
						Painter.set(level, left + w/2-1, top+h, Terrain.DOOR);
					} else if (x == 0) {
						Painter.set(level, left-1, top+h/2-1, Terrain.DOOR);
					} else if (x == rows-1){
						Painter.set(level, left+w, top+h/2, Terrain.DOOR);
					}
				} else if (!topBottomDoors) {
					if (x == 0){
						Painter.set(level, left-1, top+h/2-1, Terrain.DOOR);
					} else if (x == rows-1){
						Painter.set(level, left+w, top+h/2, Terrain.DOOR);
					} else if (y == 0) {
						Painter.set(level, left + w/2, top-1, Terrain.DOOR);
					} else if (y == cols-1){
						Painter.set(level, left + w/2-1, top+h, Terrain.DOOR);
					}
				}
			}
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}
}
