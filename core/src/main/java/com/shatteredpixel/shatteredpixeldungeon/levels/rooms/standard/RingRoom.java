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
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class RingRoom extends StandardRoom {
	
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
		
		int minDim = Math.min(width(), height());
		int passageWidth = (int)Math.floor(0.2f*(minDim+3));
		Painter.fill(level, this, passageWidth+1, Terrain.WALL);

		if (minDim >= 10){
			Painter.fill(level, this, passageWidth+2, centerDecoTiles());
			Point center = center();
			int xDir = 0, yDir = 0;

			//prefer to make the door further away if possible
			if (Random.Int(2) == 0) {
				if (center.x < (left+right)/2f){
					xDir = 1;
				} else if (center.x > (left+right)/2f){
					xDir = -1;
				} else {
					xDir = Random.Int(2) == 0 ? 1 : -1;
				}
			} else {
				if (center.y < (top+bottom)/2f){
					yDir = 1;
				} else if (center.y > (top+bottom)/2f){
					yDir = -1;
				} else {
					yDir = Random.Int(2) == 0 ? 1 : -1;
				}
			}

			Painter.set(level, center, Terrain.EMPTY_SP);
			placeCenterDetail(level, level.pointToCell(center));

			center.x += xDir;
			center.y += yDir;
			while (level.map[level.pointToCell(center)] != Terrain.WALL){
				Painter.set(level, center, Terrain.EMPTY_SP);
				center.x += xDir;
				center.y += yDir;
			}
			Painter.set(level, center, Terrain.DOOR);
		}
		
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	protected int centerDecoTiles(){
		return Terrain.REGION_DECO_ALT;
	}

	protected void placeCenterDetail(Level level, int pos){
		level.drop(level.findPrizeItem(), pos);
	}
}
