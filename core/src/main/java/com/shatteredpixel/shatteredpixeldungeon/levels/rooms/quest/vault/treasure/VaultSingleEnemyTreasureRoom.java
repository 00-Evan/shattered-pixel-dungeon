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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class VaultSingleEnemyTreasureRoom extends VaultTreasureRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Painter.fillEllipse( level, this, 3, Terrain.EMPTY );

		Painter.drawInside(level, this, entrance(), 3, Terrain.EMPTY);

		Mob enemy = Reflection.newInstance(Random.oneOf(VaultLevel.T2Mobs));
		enemy.pos = level.pointToCell(center());
		level.mobs.add(enemy);

		int treasurePos;
		if (entrance().x == left){
			treasurePos = enemy.pos+2;
			enemy.pos += 1;
		} else if (entrance().y == top){
			treasurePos = enemy.pos+2*level.width();
			enemy.pos += level.width();
		} else if (entrance().x == right){
			treasurePos = enemy.pos-2;
			enemy.pos -= 1;
		} else {
			treasurePos = enemy.pos-2*level.width();
			enemy.pos -= level.width();
		}

		Item treasureItem = ((VaultLevel)level).createEquipment(2);
		level.drop(treasureItem, treasurePos).type = Heap.Type.CHEST;

		int i;
		do {
			i = PathFinder.NEIGHBOURS4[Random.Int(PathFinder.NEIGHBOURS4.length)];
		} while (level.map[treasurePos+i] == Terrain.WALL || treasurePos+i == enemy.pos);

		treasureItem = ((VaultLevel)level).createConsumabe(2);
		level.drop(treasureItem, treasurePos+i);

		entrance().set(Door.Type.REGULAR);

	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false; //no grass to obstruct vision
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return (Math.abs(c.x - p.x) <= 2 || Math.abs(c.y - p.y) <= 2) && super.canConnect(p);
	}
}
