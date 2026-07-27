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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultGhoul;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultMultipleEnemyTreasureRoom extends VaultTreasureRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );

		Point c = center();

		int treasurePos;

		if (entrance.x == left || entrance.x == right){
			int areaTop = (int) GameMath.gate(top+1, entrance.y-2, bottom-5);
			Painter.fill(level, left+1, areaTop+1, 9, 3, Terrain.EMPTY_SP);
			Painter.fill(level, left+4, areaTop, 3, 5, Terrain.EMPTY_SP);

			VaultGhoul ghoul = new VaultGhoul();
			ghoul.maxLvl = 0; //no token
			ghoul.pos = c.x + areaTop*level.width();
			level.mobs.add(ghoul);

			ghoul = new VaultGhoul();
			ghoul.maxLvl = 0; //no token
			ghoul.pos = c.x + (areaTop+4)*level.width();
			level.mobs.add(ghoul);

			ghoul = new VaultGhoul();
			if (entrance.x == left){
				ghoul.pos = c.x + 1 + (areaTop+2)*level.width();
				treasurePos = ghoul.pos + 2;
			} else {
				ghoul.pos = c.x - 1 + (areaTop+2)*level.width();
				treasurePos = ghoul.pos - 2;
			}
			level.mobs.add(ghoul);

		} else {
			int areaLeft = (int)GameMath.gate(left+1, entrance.x-2, right-5);
			Painter.fill(level, areaLeft+1, top+1, 3, 9, Terrain.EMPTY_SP);
			Painter.fill(level, areaLeft, top+4, 5, 3, Terrain.EMPTY_SP);

			VaultGhoul ghoul = new VaultGhoul();
			ghoul.maxLvl = 0; //no token
			ghoul.pos = areaLeft + c.y*level.width();
			level.mobs.add(ghoul);

			ghoul = new VaultGhoul();
			ghoul.maxLvl = 0; //no token
			ghoul.pos = areaLeft+4 + c.y*level.width();
			level.mobs.add(ghoul);

			ghoul = new VaultGhoul();
			if (entrance.y == top){
				ghoul.pos = areaLeft+2 + (c.y+1)*level.width();
				treasurePos = ghoul.pos + 2*level.width();
			} else {
				ghoul.pos = areaLeft+2 + (c.y-1)*level.width();
				treasurePos = ghoul.pos - 2*level.width();
			}
			level.mobs.add(ghoul);
		}

		Painter.set(level, treasurePos, Terrain.PEDESTAL);

		Item treasureItem = ((VaultLevel)level).createEquipment(3);
		level.drop(treasureItem, treasurePos).type = Heap.Type.CHEST;

		int i;
		do {
			i = PathFinder.NEIGHBOURS8[Random.Int(PathFinder.NEIGHBOURS8.length)];
		} while (level.map[treasurePos+i] == Terrain.WALL);

		treasureItem = ((VaultLevel)level).createConsumabe(3);
		level.drop(treasureItem, treasurePos+i);

	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return (Math.abs(c.x - p.x) <= 3 || Math.abs(c.y - p.y) <= 3) && super.canConnect(p);
	}

}
