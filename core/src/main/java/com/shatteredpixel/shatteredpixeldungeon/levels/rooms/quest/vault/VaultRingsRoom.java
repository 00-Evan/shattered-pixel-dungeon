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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultRingsRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill(level, left+2, top+2, 3, 3, Terrain.WALL);
		Painter.fill(level, right-4, top+2, 3, 3, Terrain.WALL);
		Painter.fill(level, left+2, bottom-4, 3, 3, Terrain.WALL);
		Painter.fill(level, right-4, bottom-4, 3, 3, Terrain.WALL);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Mob enemy;
		ArrayList<Class<?extends Mob>> toReturn = new ArrayList<>();
		do {
			enemy = level.createMob();
			if (Char.hasProp(enemy, Char.Property.LARGE)){
				toReturn.add(enemy.getClass());
			}
		} while (Char.hasProp(enemy, Char.Property.LARGE));
		do {
			enemy.pos = level.pointToCell(random(1));
		} while (level.map[enemy.pos] == Terrain.WALL);
		enemy.state = enemy.WANDERING;
		level.mobs.add(enemy);

		int[] wanderPositions = new int[]{
				level.pointToCell(new Point(left+1, top+1)),
				level.pointToCell(new Point(left+1, top+5)),
				level.pointToCell(new Point(left+1, top+9)),
				level.pointToCell(new Point(left+5, top+1)),
				level.pointToCell(new Point(left+5, top+5)),
				level.pointToCell(new Point(left+5, top+9)),
				level.pointToCell(new Point(left+9, top+1)),
				level.pointToCell(new Point(left+9, top+5)),
				level.pointToCell(new Point(left+9, top+9))
		};
		Random.shuffle(wanderPositions);
		enemy.setupStealthGameplayWanderPositions(wanderPositions, 0);

		for (Class<?extends Mob> cls : toReturn){
			((VaultLevel) level).returnMob(cls);
		}

	}

}
