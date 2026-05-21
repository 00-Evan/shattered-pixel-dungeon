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
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultHallwayRoom extends VaultLongRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		if (wide()){
			Painter.fill(level, left+1, top+4, width()-2, 3, Terrain.EMPTY);
			Painter.fill(level, left+6, top+1, 1, height()-2, Terrain.EMPTY);
			Painter.fill(level, right-7, top+1, 1, height()-2, Terrain.EMPTY);
		} else {
			Painter.fill(level, left+4, top+1, 3, height()-2, Terrain.EMPTY);
			Painter.fill(level, left+1, top+6, width()-2, 1, Terrain.EMPTY);
			Painter.fill(level, left+1, bottom-7, width()-2, 1, Terrain.EMPTY);
		}

		Point c = center();
		Item i = ((VaultLevel)level).createEquipment(0);
		if (i != null){
			level.drop(i, level.pointToCell(c));
		}

		Mob enemy = level.createMob();
		if (wide()) {
			enemy.setupStealthGameplayWanderPositions(
					new int[]{level.pointToCell(new Point(left + 2, c.y)),
							level.pointToCell(new Point(right - 2, c.y))}, Random.Int(2));
		} else {
			enemy.setupStealthGameplayWanderPositions(
					new int[]{level.pointToCell(new Point(c.x, top + 2)),
							level.pointToCell(new Point(c.x, bottom - 2))}, Random.Int(2));
		}
		enemy.pos = level.pointToCell(c);
		enemy.state = enemy.WANDERING;
		level.mobs.add(enemy);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			if (wide()) {
				if (door.x == left){
					Painter.drawLine(level, new Point(door.x+1, door.y), new Point(left+1, c.y), Terrain.EMPTY);
				} else if (door.x == right) {
					Painter.drawLine(level, new Point(door.x-1, door.y), new Point(right-1, c.y), Terrain.EMPTY);
				} else if (door.x <= left + 3 || door.x >= right-3) {
					Painter.drawInside(level, this, door, 5, Terrain.EMPTY);
				} else {
					int closestX;
					if (door.x < c.x || (door.x == c.x && Random.Int(2) == 0)) {
						closestX = left + 6;
					} else {
						closestX = right - 7;
					}
					if (door.y == top) {
						Painter.drawLine(level, new Point(door.x, door.y+1), new Point(closestX, door.y+1), Terrain.EMPTY);
					} else {
						Painter.drawLine(level, new Point(door.x, door.y-1), new Point(closestX, door.y-1), Terrain.EMPTY);
					}
				}
			} else {
				if (door.y == top){
					Painter.drawLine(level, new Point(door.x, door.y+1), new Point(c.x, top+1), Terrain.EMPTY);
				} else if (door.y == bottom) {
					Painter.drawLine(level, new Point(door.x, door.y-1), new Point(c.x, bottom-1), Terrain.EMPTY);
				} else if (door.y <= top + 3 || door.y >= bottom-3) {
					Painter.drawInside(level, this, door, 5, Terrain.EMPTY);
				} else {
					int closestY;
					if (door.y < c.y || (door.y == c.y && Random.Int(2) == 0)) {
						closestY = top + 6;
					} else {
						closestY = bottom - 7;
					}
					if (door.x == left) {
						Painter.drawLine(level, new Point(door.x+1, door.y), new Point(door.x+1, closestY), Terrain.EMPTY);
					} else {
						Painter.drawLine(level, new Point(door.x-1, door.y), new Point(door.x-1, closestY), Terrain.EMPTY);
					}
				}
			}
		}
	}

}
