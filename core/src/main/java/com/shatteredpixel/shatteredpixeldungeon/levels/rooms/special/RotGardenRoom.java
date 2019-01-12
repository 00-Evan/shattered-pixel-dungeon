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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotHeart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RotGardenRoom extends SpecialRoom {
	
	@Override
	public int minWidth() { return 7; }
	
	@Override
	public int minHeight() { return 7; }

	public void paint( Level level ) {

		Door entrance = entrance();
		entrance.set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.GRASS);


		int heartX = Random.IntRange(left+1, right-1);
		int heartY = Random.IntRange(top+1, bottom-1);

		if (entrance.x == left) {
			heartX = right - 1;
		} else if (entrance.x == right) {
			heartX = left + 1;
		} else if (entrance.y == top) {
			heartY = bottom - 1;
		} else if (entrance.y == bottom) {
			heartY = top + 1;
		}

		placePlant(level, heartX + heartY * level.width(), new RotHeart());

		int lashers = ((width()-2)*(height()-2))/8;

		for (int i = 1; i <= lashers; i++){
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (!validPlantPos(level, pos));
			placePlant(level, pos, new RotLasher());
		}
	}

	private static boolean validPlantPos(Level level, int pos){
		if (level.map[pos] != Terrain.GRASS){
			return false;
		}

		for (int i : PathFinder.NEIGHBOURS9){
			if (level.findMob(pos+i) != null){
				return false;
			}
		}

		return true;
	}

	private static void placePlant(Level level, int pos, Mob plant){
		plant.pos = pos;
		level.mobs.add( plant );

		for(int i : PathFinder.NEIGHBOURS8) {
			if (level.map[pos + i] == Terrain.GRASS){
				Painter.set(level, pos + i, Terrain.HIGH_GRASS);
			}
		}
	}
}
