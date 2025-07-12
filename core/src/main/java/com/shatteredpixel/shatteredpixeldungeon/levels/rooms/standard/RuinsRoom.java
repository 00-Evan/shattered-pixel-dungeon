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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class RuinsRoom extends PatchRoom {
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{4, 2, 1};
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return true;
	}

	@Override
	protected float fill() {
		//fill scales from ~30% at 4x4, to ~60% at 18x18
		// normal   ~30% to ~40%
		// large    ~40% to ~50%
		// giant    ~50% to ~60%
		int scale = Math.min(width()*height(), 18*18);
		return 0.30f + scale/1024f;
	}

	@Override
	protected int clustering() {
		return 0;
	}

	@Override
	protected boolean ensurePath() {
		return connected.size() > 0;
	}

	@Override
	protected boolean cleanEdges() {
		return true;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		setupPatch(level);
		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				if (patch[xyToPatchCoords(j, i)]) {

					//isolated bits of wall are turned into rubble
					boolean wall;
					if (i > top+1 && i < bottom-1 && j >left+1 && j<right-1){
						int adjacent = 0;
						if (patch[xyToPatchCoords(j-1, i)]) adjacent++;
						if (patch[xyToPatchCoords(j+1, i)]) adjacent++;
						if (patch[xyToPatchCoords(j, i-1)]) adjacent++;
						if (patch[xyToPatchCoords(j, i+1)]) adjacent++;
						wall = Random.Int(2) < adjacent;
					} else {
						wall = true;
					}

					int cell = i * level.width() + j;
					level.map[cell] = wall ? Terrain.WALL : Terrain.REGION_DECO;
				}
			}
		}
	}
}
