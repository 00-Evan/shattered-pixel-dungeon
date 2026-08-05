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
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultSimpleEnemyTreasureRoom extends VaultRoom {

	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		int enemyPos = 0;
		int treasurePos = 0;
		switch (Random.Int(4)){
			case 0:
				Painter.fill(level, left+2, top+2, 6, 6, Terrain.WALL );
				Painter.fill(level, left+3, top+3, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+4, top+7, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+7, top+4, 1, 2, Terrain.EMPTY_SP );
				enemyPos = level.pointToCell(new Point(left+4, top+4));
				treasurePos = level.pointToCell(new Point(left+3, top+3));
				break;
			case 1:
				Painter.fill(level, left+3, top+2, 6, 6, Terrain.WALL );
				Painter.fill(level, left+4, top+3, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+5, top+7, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+3, top+4, 1, 2, Terrain.EMPTY_SP );
				enemyPos = level.pointToCell(new Point(right-4, top+4));
				treasurePos = level.pointToCell(new Point(right-3, top+3));
				break;
			case 2:
				Painter.fill(level, left+3, top+3, 6, 6, Terrain.WALL );
				Painter.fill(level, left+4, top+4, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+5, top+3, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+3, top+5, 1, 2, Terrain.EMPTY_SP );
				enemyPos = level.pointToCell(new Point(right-4, bottom-4));
				treasurePos = level.pointToCell(new Point(right-3, bottom-3));
				break;
			case 3:
				Painter.fill(level, left+2, top+3, 6, 6, Terrain.WALL );
				Painter.fill(level, left+3, top+4, 4, 4, Terrain.EMPTY_SP );
				Painter.fill(level, left+4, top+3, 2, 1, Terrain.EMPTY_SP );
				Painter.fill(level, left+7, top+5, 1, 2, Terrain.EMPTY_SP );
				enemyPos = level.pointToCell(new Point(left+4, bottom-4));
				treasurePos = level.pointToCell(new Point(left+3, bottom-3));
				break;
		}

		//no T1 mobs, only T2+
		Mob enemy;
		ArrayList<Class<?extends Mob>> toReturn = new ArrayList<>();
		boolean valid = true;
		do {
			enemy = level.createMob();
			valid = true;
			for (Class<?extends Mob> cls : VaultLevel.T1Mobs){
				if (cls.equals(enemy.getClass())){
					valid = false;
					toReturn.add(enemy.getClass());
				}
			}
		} while (!valid);
		for (Class<?extends Mob> cls : toReturn){
			((VaultLevel) level).returnMob(cls);
		}

		int tier = 2;
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
		level.drop(treasure, treasurePos).type = Heap.Type.CHEST;

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		enemy.pos = enemyPos;
		level.mobs.add(enemy);

	}

	@Override
	//no random items in the center
	public boolean canPlaceItem(Point p, Level l) {
		Point c = center();
		if (Math.abs(c.x - p.x) <= 2) return false;
		if (Math.abs(c.y - p.y) <= 2) return false;
		return super.canPlaceItem(p, l);
	}

}
