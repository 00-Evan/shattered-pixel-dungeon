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
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

//FIXME some copypasta from segmented room with changed constants in here, might want to externalize
public class SegmentedLibraryRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 3, 1};
	}

	@Override
	public void paint( Level level ) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.BOOKSHELF );
		Painter.fill( level, this, 2 , Terrain.EMPTY_SP );

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			//set door areas to be empty to help with create walls logic
			Painter.drawInside(level, this, door, 2, Terrain.EMPTY_SP);
		}

		createWalls( level, new Rect(left+2, top+2, right-2, bottom-2));
	}

	private void createWalls( Level level, Rect area ){
		if (Math.max(area.width()+1, area.height()+1) < 4
				|| Math.min(area.width()+1, area.height()+1) < 3){
			return;
		}

		int tries = 10;

		//splitting top/bottom
		if (area.width() > area.height() || (area.width() == area.height() && Random.Int(2) == 0)){

			do{
				int splitX = Random.IntRange(area.left+2, area.right-2);

				if (level.map[splitX + level.width()*(area.top-1)] == Terrain.BOOKSHELF
						&& level.map[splitX + level.width()*(area.bottom+1)] == Terrain.BOOKSHELF){
					tries = 0;

					Painter.drawLine(level, new Point(splitX, area.top), new Point(splitX, area.bottom), Terrain.BOOKSHELF);

					int spaceTop = Random.IntRange(area.top, area.bottom-1);
					Painter.set(level, splitX, spaceTop, Terrain.EMPTY_SP);
					//Painter.set(level, splitX, spaceTop+1, Terrain.EMPTY);

					createWalls(level, new Rect(area.left, area.top, splitX-1, area.bottom));
					createWalls(level, new Rect(splitX+1, area.top, area.right, area.bottom));
				}

			} while (--tries > 0);

			//splitting left/right
		} else {

			do{
				int splitY = Random.IntRange(area.top+2, area.bottom-2);

				if (level.map[area.left-1 + level.width()*splitY] == Terrain.BOOKSHELF
						&& level.map[area.right+1 + level.width()*splitY] == Terrain.BOOKSHELF){
					tries = 0;

					Painter.drawLine(level, new Point(area.left, splitY), new Point(area.right, splitY), Terrain.BOOKSHELF);

					int spaceLeft = Random.IntRange(area.left, area.right-1);
					Painter.set(level, spaceLeft, splitY, Terrain.EMPTY_SP);
					//Painter.set(level, spaceLeft+1, splitY, Terrain.EMPTY);

					createWalls(level, new Rect(area.left, area.top, area.right, splitY-1));
					createWalls(level, new Rect(area.left, splitY+1, area.right, area.bottom));
				}

			} while (--tries > 0);

		}
	}

}
