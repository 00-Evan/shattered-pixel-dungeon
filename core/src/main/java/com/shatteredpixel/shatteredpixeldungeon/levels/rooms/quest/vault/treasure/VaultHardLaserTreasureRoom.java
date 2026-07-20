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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultLaser;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class VaultHardLaserTreasureRoom extends VaultTreasureRoom {

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);

		Door entrance = entrance();
		entrance.set(Door.Type.REGULAR);

		Rect itemPlace;

		if (entrance.x == left || entrance.x == right) {
			int areaTop = (int) GameMath.gate(top + 1, entrance.y - 2, bottom - 5);
			Painter.fill(level, left + 1, areaTop, 9, 5, Terrain.EMPTY_SP);
			if (entrance.x == left) {
				itemPlace = new Rect(right - 1, areaTop, right - 1, areaTop + 4);
				for (int x = right - 2; x > left + 1; x--) {
					VaultLaser sentry = new VaultLaser();
					sentry.pos = x + level.width() * areaTop;
					sentry.laserDirs = new int[]{sentry.pos + level.width()};
					sentry.curCooldown = right - x;
					sentry.afterShotCooldown = 2;
					sentry.shotsAfterCooldown = 6;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);

					//second row is only visual, to avoid double damage
					sentry = new VaultLaser();
					sentry.pos = x + level.width() * (areaTop + 4);
					sentry.laserDirs = new int[]{sentry.pos - level.width()};
					sentry.curCooldown = Integer.MAX_VALUE;
					sentry.afterShotCooldown = Integer.MAX_VALUE;
					sentry.shotsAfterCooldown = 0;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);
				}
			} else {
				itemPlace = new Rect(left + 1, areaTop, left + 1, areaTop + 4);
				for (int x = left + 2; x < right - 1; x++) {
					VaultLaser sentry = new VaultLaser();
					sentry.pos = x + level.width() * areaTop;
					sentry.laserDirs = new int[]{sentry.pos + level.width()};
					sentry.curCooldown = x - left;
					sentry.afterShotCooldown = 2;
					sentry.shotsAfterCooldown = 6;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);

					//second row is only visual, to avoid double damage
					sentry = new VaultLaser();
					sentry.pos = x + level.width() * (areaTop + 4);
					sentry.laserDirs = new int[]{sentry.pos - level.width()};
					sentry.curCooldown = Integer.MAX_VALUE;
					sentry.afterShotCooldown = Integer.MAX_VALUE;
					sentry.shotsAfterCooldown = 0;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);
				}
			}
		} else {
			int areaLeft = (int) GameMath.gate(left + 1, entrance.x - 2, right - 5);
			Painter.fill(level, areaLeft, top + 1, 5, 9, Terrain.EMPTY_SP);
			if (entrance.y == top) {
				itemPlace = new Rect(areaLeft, bottom - 1, areaLeft + 4, bottom - 1);
				for (int y = bottom - 2; y > top + 1; y--) {
					VaultLaser sentry = new VaultLaser();
					sentry.pos = areaLeft + level.width() * (y);
					sentry.laserDirs = new int[]{sentry.pos + 1};
					sentry.curCooldown = bottom - y;
					sentry.afterShotCooldown = 2;
					sentry.shotsAfterCooldown = 6;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);

					//second row is only visual, to avoid double damage
					sentry = new VaultLaser();
					sentry.pos = areaLeft + 4 + level.width() * (y);
					sentry.laserDirs = new int[]{sentry.pos - 1};
					sentry.curCooldown = Integer.MAX_VALUE;
					sentry.afterShotCooldown = Integer.MAX_VALUE;
					sentry.shotsAfterCooldown = 0;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);
				}
			} else {
				itemPlace = new Rect(areaLeft, top + 1, areaLeft + 4, top + 1);
				for (int y = top + 2; y < bottom - 1; y++) {
					VaultLaser sentry = new VaultLaser();
					sentry.pos = areaLeft + level.width() * (y);
					sentry.laserDirs = new int[]{sentry.pos + 1};
					sentry.curCooldown = y - top;
					sentry.afterShotCooldown = 2;
					sentry.shotsAfterCooldown = 6;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);

					//second row is only visual, to avoid double damage
					sentry = new VaultLaser();
					sentry.pos = areaLeft + 4 + level.width() * (y);
					sentry.laserDirs = new int[]{sentry.pos - 1};
					sentry.curCooldown = Integer.MAX_VALUE;
					sentry.afterShotCooldown = Integer.MAX_VALUE;
					sentry.shotsAfterCooldown = 0;
					sentry.giveWarning = false;
					Painter.set(level, sentry.pos, Terrain.PEDESTAL);
					level.mobs.add(sentry);
				}
			}
		}

		Painter.fill(level, itemPlace.left, itemPlace.top, itemPlace.width() + 1, itemPlace.height() + 1, Terrain.EMPTY_SP);
		int treasurePos = level.pointToCell(Random.element(itemPlace.getPoints()));
		Item treasureItem = ((VaultLevel) level).createEquipment(3);
		level.drop(treasureItem, treasurePos).type = Heap.Type.CHEST;

		treasureItem = ((VaultLevel) level).createConsumabe(3);
		if (treasureItem != null) {
			do {
				treasurePos = level.pointToCell(Random.element(itemPlace.getPoints()));
			} while (level.heaps.get(treasurePos) != null);
			level.drop(treasureItem, treasurePos);
		}

		level.addItemToSpawn(new StoneOfBlink());
	}
}
