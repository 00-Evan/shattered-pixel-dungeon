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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultLaser;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultLasersRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 2, Terrain.EMPTY);

		for (Room.Door door : connected.values()) {
			Painter.drawInside(level, this, door, 2, Terrain.EMPTY);
			door.set(Room.Door.Type.REGULAR);
		}

		for (int x = left+2; x <= right-2; x++){
			if (level.map[x + (top+1)*level.width()] == Terrain.WALL
					&& level.map[x + (bottom-1)*level.width()] == Terrain.WALL){
				VaultLaser laser = new VaultLaser();
				if (Random.Int(2) == 0){
					int cell = x + level.width()*(top+1);
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell+level.width()};
					laser.pos = cell;
				} else {
					int cell = x + level.width()*(bottom-1);
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell-level.width()};
					laser.pos = cell;
				}
				laser.afterShotCooldown = Random.IntRange(3, 7);
				laser.curCooldown = Random.IntRange(1, laser.afterShotCooldown);
				level.mobs.add(laser);
			}
		}

		for (int y = top+2; y <= bottom-2; y++){
			if (level.map[left+1 + (y)*level.width()] == Terrain.WALL
					&& level.map[right-1 + (y)*level.width()] == Terrain.WALL){
				VaultLaser laser = new VaultLaser();
				if (Random.Int(2) == 0){
					int cell = left+1 + level.width()*y;
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell+1};
					laser.pos = cell;
				} else {
					int cell = right-1 + level.width()*y;
					Painter.set(level, cell, Terrain.PEDESTAL);
					laser.laserDirs = new int[]{cell-1};
					laser.pos = cell;
				}
				laser.afterShotCooldown = Random.IntRange(3, 7);
				laser.curCooldown = Random.IntRange(1, laser.afterShotCooldown);
				level.mobs.add(laser);
			}
		}

	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return false;
	}

}
