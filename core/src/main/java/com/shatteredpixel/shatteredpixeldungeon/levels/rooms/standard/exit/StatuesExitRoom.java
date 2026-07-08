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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StatuesRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.custom.Carpet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class StatuesExitRoom extends StatuesRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{3, 1, 0};
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int exit = -1;
		if (width() >= 11 || height() >= 11){
			Point c = center();
			exit = level.pointToCell(c);
			for (int i : PathFinder.NEIGHBOURS8){
				if (level.map[exit + i] != Terrain.STATUE ){
					Painter.set(level, exit + i, Terrain.EMPTY_DECO);
				}
			}
			Carpet carpet = new Carpet();
			Rect carpetRect = new Rect(c.x-1, c.y-1, c.x+1, c.y+1);
			if (width()%2 == 0){
				if (c.x < (left+right)/2f){
					carpetRect.right++;
				} else {
					carpetRect.left--;
				}
			}
			if (height()%2 == 0){
				if (c.y < (top+bottom)/2f){
					carpetRect.bottom++;
				} else {
					carpetRect.top--;
				}
			}
			carpet.setRect(carpetRect.left, carpetRect.top, carpetRect.width()+1, carpetRect.height()+1);
			carpet.overrideTile(exit, level, Carpet.SKIP);
			level.customTiles.add(0, carpet); //so other carpets are on top of it
		} else {
			//already have an exit placed in this case
			for ( Point p : getPoints()){
				if (level.map[level.pointToCell(p)] == Terrain.EXIT){
					exit = level.pointToCell(p);
					break;
				}
			}
		}

		Painter.set( level, exit, Terrain.EXIT );
		level.transitions.add(new LevelTransition(level, exit, LevelTransition.Type.REGULAR_EXIT));

	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return super.canPlaceCharacter(p, l) && l.pointToCell(p) != l.exit();
	}

}
