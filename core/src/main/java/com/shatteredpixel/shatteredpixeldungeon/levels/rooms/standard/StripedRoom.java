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
import com.watabou.utils.Random;

public class StripedRoom extends StandardRoom {
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{2, 1, 0};
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		if (sizeCat == SizeCategory.NORMAL) {
			Painter.fill(level, this, 1, Terrain.EMPTY_SP);
			if (width() > height() || (width() == height() && Random.Int(2) == 0)) {
				for (int i = left + 2; i < right; i += 2) {
					Painter.fill(level, i, top + 1, 1, height() - 2, Terrain.HIGH_GRASS);
				}
			} else {
				for (int i = top + 2; i < bottom; i += 2) {
					Painter.fill(level, left + 1, i, width() - 2, 1, Terrain.HIGH_GRASS);
				}
			}
			
		} else if (sizeCat == SizeCategory.LARGE){
			int layers = (Math.min(width(), height())-1)/2;
			for (int i = 1; i <= layers; i++){
				Painter.fill(level, this, i, (i % 2 == 1) ? Terrain.EMPTY_SP : Terrain.HIGH_GRASS);
			}
		}
	}
}
