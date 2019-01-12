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
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class FissureRoom extends StandardRoom {
	
	
	@Override
	public float[] sizeCatProbs() {
		return new float[]{6, 3, 1};
	}
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		if (square() <= 25){
			//just fill in one tile if the room is tiny
			Point p = center();
			Painter.set( level, p.x, p.y, Terrain.CHASM);
			
		} else {
			int smallestDim = Math.min(width(), height());
			int floorW = (int)Math.sqrt(smallestDim);
			//chance for a tile at the edge of the floor to remain a floor tile
			float edgeFloorChance = (float)Math.sqrt(smallestDim) % 1;
			//the wider the floor the more edge chances tend toward 50%
			edgeFloorChance = (edgeFloorChance + (floorW-1)*0.5f) / (float)floorW;
			
			for (int i=top + 2; i <= bottom - 2; i++) {
				for (int j=left + 2; j <= right - 2; j++) {
					int v = Math.min( i - top, bottom - i );
					int h = Math.min( j - left, right - j );
					if (Math.min( v, h ) > floorW
							|| (Math.min( v, h ) == floorW && Random.Float() > edgeFloorChance)) {
						Painter.set( level, j, i, Terrain.CHASM );
					}
				}
			}
		}
	}
	
}
