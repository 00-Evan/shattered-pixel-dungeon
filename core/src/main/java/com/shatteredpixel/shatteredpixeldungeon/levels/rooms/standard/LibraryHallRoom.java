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

public class LibraryHallRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public float[] sizeCatProbs(){
		//TODO this room could probably have a giant variant?
		// possible with three lines of bookcases instead of 2, and/or will holes in the middle
		return new float[]{2, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		//we decide which way to lay out the room based on a few factors:
		float topBottomBooks = 0;
		float leftRightBooks = 0;

		//primarily we decide based on whether it is taller or wider
		if (width() > height()){
			topBottomBooks += (width() - height());
		} else if (height() > width()){
			leftRightBooks += (height() - width());
		}

		//but if one dimension is odd and the other's even, favor the odd one
		if (width()%2 == 0 && height()%2 != 0){
			topBottomBooks += 2;
		} else if (width()%2 != 0 && height()%2 == 0){
			leftRightBooks += 2;
		}

		//lastly, if all else is equal, prefer to leave the sides with the most doors open
		for (Door door : connected.values()) {
			if (door.x == left || door.x == right){
				topBottomBooks += 0.1f;
			} else {
				leftRightBooks += 0.1f;
			}
		}

		boolean layingOutLeftToRight = topBottomBooks > leftRightBooks
				|| (topBottomBooks == leftRightBooks && Random.Int(2) == 0);

		int majorDim = layingOutLeftToRight ? height() : width();
		int minorDim = layingOutLeftToRight ? width() : height();

		//if room is sufficiently small/big, place row of bookcases along edges
		if (majorDim >= 11 || majorDim < 9){
			if (layingOutLeftToRight) {
				Painter.drawLine(level, new Point(left + 1, top + 1), new Point(right - 1, top + 1), Terrain.BOOKSHELF);
				Painter.drawLine(level, new Point(left + 1, bottom - 1), new Point(right - 1, bottom - 1), Terrain.BOOKSHELF);
			} else {
				Painter.drawLine(level, new Point(left+1, top+1), new Point(left+1, bottom-1), Terrain.BOOKSHELF);
				Painter.drawLine(level, new Point(right-1, top+1), new Point(right-1, bottom-1), Terrain.BOOKSHELF);
			}

		}

		Point center = center();

		//if room is sufficiently big, place rows inset as well
		int lengthInset = 2;
		if (majorDim >= 9){
			if (minorDim >= 13){
				lengthInset++;
			}

			if (layingOutLeftToRight) {
				Painter.drawLine(level, new Point(left + lengthInset, center.y - 2), new Point(right - lengthInset, center.y - 2), Terrain.BOOKSHELF);
				Painter.drawLine(level, new Point(left + lengthInset, center.y + 2), new Point(right - lengthInset, center.y + 2), Terrain.BOOKSHELF);
			} else {
				Painter.drawLine(level, new Point(center.x-2, top+lengthInset), new Point(center.x-2, bottom-lengthInset), Terrain.BOOKSHELF);
				Painter.drawLine(level, new Point(center.x+2, top+lengthInset), new Point(center.x+2, bottom-lengthInset), Terrain.BOOKSHELF);
			}
		}

		if (minorDim % 2 == 1 && minorDim < 9){
			Painter.set(level, center, Terrain.REGION_DECO);
		} else {
			int pedestalInset = 2;
			if (minorDim >= 10){
				pedestalInset++;
				if (minorDim >= 13){
					pedestalInset++;
				}
			}
			if (layingOutLeftToRight) {
				Painter.set(level, left+pedestalInset, center.y, Terrain.REGION_DECO);
				Painter.set(level, right-pedestalInset, center.y, Terrain.REGION_DECO);
			} else {
				Painter.set(level, center.x, top+pedestalInset, Terrain.REGION_DECO);
				Painter.set(level, center.x, bottom-pedestalInset, Terrain.REGION_DECO);
			}
		}

		//ensure doors can still be accessed
		for (Door door : connected.values()) {
			Painter.drawInside(level, this, door, 1, Terrain.EMPTY);
			door.set( Door.Type.REGULAR );
		}

	}

}
