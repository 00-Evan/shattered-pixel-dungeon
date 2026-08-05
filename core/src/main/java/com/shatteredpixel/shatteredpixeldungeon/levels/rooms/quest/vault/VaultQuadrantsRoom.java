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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultQuadrantsRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		Painter.drawInside( level, this, new Point(left, c.y), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(right, c.y), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(c.x, top), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(c.x, bottom), 3, Terrain.WALL);

		Painter.set( level, c, Terrain.STATUE);

		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		ArrayList<Point> spawnPositions = new ArrayList<>();
		spawnPositions.add(new Point(left + 2, top + 2));
		spawnPositions.add(new Point(right - 2, top + 2));
		spawnPositions.add(new Point(right - 2, bottom - 2));
		spawnPositions.add(new Point(left + 2, bottom - 2));

		for (Point p : spawnPositions.toArray(new Point[0])){
			for (Room.Door door : connected.values()) {
				if (Point.distance(p, door) <= 3){
					spawnPositions.remove(p);
				}
			}
		}

		if (!spawnPositions.isEmpty()) {
			Mob enemy = level.createMob();
			Point enemyCorner = Random.element(spawnPositions);
			enemy.pos = level.pointToCell(enemyCorner);
			enemy.state = enemy.WANDERING;
			level.mobs.add(enemy);

			int tier = 1;
			for (Class<?extends Mob> cls : VaultLevel.T1Mobs){
				if (cls.equals(enemy.getClass())){
					tier = 1;
				}
			}
			for (Class<?extends Mob> cls : VaultLevel.T2Mobs){
				if (cls.equals(enemy.getClass())){
					tier = 2;
				}
			}
			for (Class<?extends Mob> cls : VaultLevel.T3Mobs){
				if (cls.equals(enemy.getClass())){
					tier = 3;
				}
			}
			//special case for elementals
			if (enemy instanceof Elemental){
				tier = 3;
			}

			Item treasure = ((VaultLevel)level).createEquipment(tier);
			int treasurePos = enemy.pos;
			if (enemyCorner.x < c.x){
				treasurePos--;
			} else {
				treasurePos++;
			}
			if (enemyCorner.y < c.y){
				treasurePos -= level.width();
			} else {
				treasurePos += level.width();
			}
			level.drop(treasure, treasurePos).type = Heap.Type.CHEST;
		}

	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return c.x != p.x && c.y != p.y && super.canConnect(p);
	}

}
