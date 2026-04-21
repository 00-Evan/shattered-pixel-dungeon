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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultDM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultElemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultGhoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultGolem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultShaman;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultRingRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill(level, this, 4, Terrain.WALL);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Mob enemy = level.createMob();
		do {
			enemy.pos = level.pointToCell(random(1));
		} while (level.solid[enemy.pos]);

		int[] wanderPositions;
		if (Random.Int(2) == 0) {
			wanderPositions = new int[]{
					level.pointToCell(new Point(left+2, top+2)),
					level.pointToCell(new Point(right-2, top+2)),
					level.pointToCell(new Point(right-2, bottom-2)),
					level.pointToCell(new Point(left+2, bottom-2))
			};
		} else {
			wanderPositions = new int[]{
					level.pointToCell(new Point(left+2, bottom-2)),
					level.pointToCell(new Point(right-2, bottom-2)),
					level.pointToCell(new Point(right-2, top+2)),
					level.pointToCell(new Point(left+2, top+2))
			};
		}
		enemy.setupStealthGameplayWanderPositions(wanderPositions, Random.Int(4));
		enemy.state = enemy.WANDERING;
		level.mobs.add(enemy);
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}
}
