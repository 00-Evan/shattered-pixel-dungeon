/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.watabou.utils.Random;

public class StatuesRoom extends StandardRoom {

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
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		int rows = (width() + 1)/6;
		int cols = (height() + 1)/6;

		int w = (width() - 4 - (rows-1))/rows;
		int h = (height() - 4 - (cols-1))/cols;

		int Wspacing = rows % 2 == width() % 2 ? 2 : 1;
		int Hspacing = cols % 2 == height() % 2 ? 2 : 1;

		for (int x = 0; x < rows; x++){
			for (int y = 0; y < cols; y++){
				int left = this.left + 2 + (x * (w + Wspacing));
				int top = this.top + 2 + (y * (h + Hspacing));

				Painter.fill(level, left, top, w, h, Terrain.CUSTOM_DECO_EMPTY);

				Painter.set(level, left, top, Terrain.STATUE);
				Painter.set(level, left + w-1, top, Terrain.STATUE);
				Painter.set(level, left, top + h-1, Terrain.STATUE);
				Painter.set(level, left + w-1, top + h-1, Terrain.STATUE);

				Carpet carpet = new Carpet();
				carpet.setRect(left, top, w, h);
				carpet.overrideTile(0, 0, Carpet.CITY_STATUE_TL);
				carpet.overrideTile(carpet.tileW-1, 0, Carpet.CITY_STATUE_TR);
				carpet.overrideTile(0, carpet.tileH-1, Carpet.CITY_STATUE_BL);
				carpet.overrideTile(carpet.tileW-1, carpet.tileH-1, Carpet.CITY_STATUE_BR);
				level.customTiles.add(carpet);

				//place a flaming pedestal or entrance/exit in the center
				if (w >= 5 && h >= 5 || ((isEntrance() || isExit()) && rows == 1 && cols == 1)){
					int cx = left + w/2;
					if (w % 2 == 0 && Random.Int(2) == 0){
						cx--;
					}
					int cy = top + h/2;
					if (h % 2 == 0 && Random.Int(2) == 0){
						cy--;
					}
					if (isEntrance() && rows == 1 && cols == 1){
						Painter.set(level, cx, cy, Terrain.ENTRANCE);
						carpet.overrideTile(level, cx, cy, Carpet.CITY_ENTRANCE);
					} else if (isExit() && rows == 1 && cols == 1){
						Painter.set(level, cx, cy, Terrain.EXIT);
						carpet.overrideTile(level, cx, cy, Carpet.SKIP);
					} else {
						Painter.set(level, cx, cy, Terrain.REGION_DECO);
						carpet.overrideTile(level, cx, cy, Carpet.CITY_PEDESTAL);
					}
				}
			}
		}

	}
}
