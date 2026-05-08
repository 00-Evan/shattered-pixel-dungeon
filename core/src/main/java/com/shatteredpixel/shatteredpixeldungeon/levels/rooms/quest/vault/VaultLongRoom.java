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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultLongRoom extends VaultRoom {

	private boolean wide = Random.Int(2) == 0;

	@Override
	public int minWidth() {
		return wide ? 21 : 11;
	}

	@Override
	public int maxWidth() {
		return minWidth();
	}

	@Override
	public int minHeight() {
		return wide ? 11: 21;
	}

	@Override
	public int maxHeight() {
		return minHeight();
	}

	@Override
	public int sizeFactor() {
		return 2;
	}


	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill(level, this, 4, Terrain.WALL);

		if (wide){
			Painter.fill(level, this, 8, 4, 8, 4, Terrain.EMPTY);
		} else {
			Painter.fill(level, this, 4, 8, 4, 8, Terrain.EMPTY);
		}

		Point c = center();
		Item i = level.findPrizeItem();
		if (i != null){
			level.drop(i, level.pointToCell(c));
		}

		Mob enemy = level.createMob();
		enemy.pos = randomWander(level);
		int[] wanderPositions = new int[]{
				randomWander(level), randomWander(level), randomWander(level),
				randomWander(level), randomWander(level), randomWander(level),
				randomWander(level), randomWander(level), randomWander(level),
				randomWander(level), randomWander(level), randomWander(level),
				randomWander(level), randomWander(level), randomWander(level),
				randomWander(level), randomWander(level), randomWander(level),
		};
		enemy.setupStealthGameplayWanderPositions(wanderPositions, 0);
		enemy.state = enemy.WANDERING;
		level.mobs.add(enemy);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	private int randomWander(Level level){
		int pos;
		do {
			pos = level.pointToCell(random(1));
		} while (level.map[pos] == Terrain.WALL);
		return pos;
	}

}
