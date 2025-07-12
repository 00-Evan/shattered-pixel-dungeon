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

//places a line of statues along the least used wall
public class StatueLineRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(5, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(5, super.minHeight());
	}

	private static final Integer N = 0;
	private static final Integer E = 1;
	private static final Integer S = 2;
	private static final Integer W = 3;

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		float[] sidePreferences = new float[]{1, 1, 1, 1};

		//try to place the deco objects along a wall without doors
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			if (door.y == top) sidePreferences[N] -= 2;
			if (door.y == top+1) sidePreferences[N] -= 1;

			if (door.y == bottom) sidePreferences[S] -= 2;
			if (door.y == bottom-1) sidePreferences[S] -= 1;

			if (door.x == left) sidePreferences[W] -= 2;
			if (door.x == left+1) sidePreferences[W] -= 1;

			if (door.x == right) sidePreferences[E] -= 2;
			if (door.x == right-1) sidePreferences[E] -= 1;
		}

		//if there are no walls with no doors, prefer fewer doors (corner doors count for half)
		int chosenSide = Random.chances(sidePreferences);
		while (chosenSide == -1) {
			sidePreferences[N]++;
			sidePreferences[E]++;
			sidePreferences[S]++;
			sidePreferences[W]++;
			chosenSide = Random.chances(sidePreferences);
		}

		fillAlongSide(chosenSide, level);
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			Painter.drawInside(level, this, door, 1, Terrain.EMPTY);
		}
	}

	protected int decoTerrain(){
		return Terrain.STATUE;
	}

	private void fillAlongSide(Integer side, Level level){
		switch (side){
			case 0: //N
				Painter.drawLine(level, new Point(left+1, top+1), new Point(right-1, top+1), decoTerrain());
				break;
			case 1: //E
				Painter.drawLine(level, new Point(right-1, top+1), new Point(right-1, bottom-1), decoTerrain());
				break;
			case 2: //S
				Painter.drawLine(level, new Point(left+1, bottom-1), new Point(right-1, bottom-1), decoTerrain());
				break;
			case 3: //W
				Painter.drawLine(level, new Point(left+1, top+1), new Point(left+1, bottom-1), decoTerrain());
				break;
		}
	}

}
