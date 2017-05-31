/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

public class PillarsRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 7);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 7);
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
		
		int minDim = Math.min(width(), height());
		
		//2 pillars
		if (minDim == 7 || Random.Int(2) == 0){
			
			int pillarInset = minDim >= 11 ? 2 : 1;
			int pillarSize = ((minDim-3)/2) - pillarInset;
			
			int pillarX, pillarY;
			if (Random.Int(2) == 0) {
				pillarX = Random.IntRange(left + 1 + pillarInset, right - pillarSize - pillarInset);
				pillarY = top + 1 + pillarInset;
			} else {
				pillarX = left + 1 + pillarInset;
				pillarY = Random.IntRange(top + 1 + pillarInset, bottom - pillarSize - pillarInset);
			}
			
			//first pillar
			Painter.fill(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);
			
			//invert for second pillar
			pillarX = right - (pillarX - left + pillarSize - 1);
			pillarY = bottom - (pillarY - top + pillarSize - 1);
			Painter.fill(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);
		
		//4 pillars
		} else {
			
			int pillarInset = minDim >= 12 ? 2 : 1;
			int pillarSize = (minDim - 6)/(pillarInset + 1);
			
			float xSpaces = width() - 2*pillarInset - pillarSize - 2;
			float ySpaces = height() - 2*pillarInset - pillarSize - 2;
			float minSpaces = Math.min(xSpaces, ySpaces);
			
			float percentSkew = Math.round(Random.Float() * minSpaces) / minSpaces;
			
			//top-left, skews right
			Painter.fill(level, left + 1 + pillarInset + Math.round(percentSkew*xSpaces), top + 1 + pillarInset, pillarSize, pillarSize, Terrain.WALL);
			
			//top-right, skews down
			Painter.fill(level, right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew*ySpaces), pillarSize, pillarSize, Terrain.WALL);
			
			//bottom-right, skews left
			Painter.fill(level, right - pillarSize - pillarInset - Math.round(percentSkew*xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize, Terrain.WALL);
			
			//bottom-left, skews up
			Painter.fill(level, left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew*ySpaces), pillarSize, pillarSize, Terrain.WALL);
			
		}
	}
}
