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

public class VaultEnemyCenterRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		Painter.fill( level, this, 2 , Terrain.WALL );
		Painter.fill( level, this, 3 , Terrain.EMPTY );
		Painter.drawLine( level, new Point(left+1, top+3), new Point(right-1, top+3), Terrain.EMPTY);
		Painter.drawLine( level, new Point(left+1, bottom-3), new Point(right-1, bottom-3), Terrain.EMPTY);
		Painter.drawLine( level, new Point(left+3, top+1), new Point(left+3, bottom-1), Terrain.EMPTY);
		Painter.drawLine( level, new Point(right-3, top+1), new Point(right-3, bottom-1), Terrain.EMPTY);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Mob enemy = level.createMob();

		int[] wanderPositions;
		Point c = center();
		if (Random.Int(2) == 0) {
			wanderPositions = new int[]{
					level.pointToCell(new Point(c.x-1, c.y-1)),
					level.pointToCell(new Point(c.x+1, c.y-1)),
					level.pointToCell(new Point(c.x+1, c.y+1)),
					level.pointToCell(new Point(c.x-1, c.y+1))
			};
		} else {
			wanderPositions = new int[]{
					level.pointToCell(new Point(c.x-1, c.y-1)),
					level.pointToCell(new Point(c.x-1, c.y+1)),
					level.pointToCell(new Point(c.x+1, c.y+1)),
					level.pointToCell(new Point(c.x+1, c.y-1))
			};
		}
		int idx = Random.Int(4);
		enemy.pos = wanderPositions[idx];
		enemy.setupStealthGameplayWanderPositions(wanderPositions, idx);
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
		level.drop(treasure, level.pointToCell(c)).type = Heap.Type.CHEST;

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
