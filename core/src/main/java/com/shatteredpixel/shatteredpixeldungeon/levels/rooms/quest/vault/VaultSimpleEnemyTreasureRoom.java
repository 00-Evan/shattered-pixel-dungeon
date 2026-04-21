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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultDM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultRat;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class VaultSimpleEnemyTreasureRoom extends StandardRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

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

		MeleeWeapon treasure = Generator.randomWeapon(true);
		level.drop(treasure, treasurePos).type = Heap.Type.CHEST;
		if (treasure.cursed){
			treasure.cursed = false;
			if (((MeleeWeapon) treasure).hasCurseEnchant()){
				((MeleeWeapon) treasure).enchant(null);
			}
		}
		//not true ID
		treasure.levelKnown = treasure.cursedKnown = true;

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		Mob enemy;
		if (treasure.tier <= 3){
			enemy = Reflection.newInstance(Random.oneOf(VaultLevel.T1Mobs));
		} else if (treasure.tier == 4){
			enemy = Reflection.newInstance(Random.oneOf(VaultLevel.T2Mobs));
		} else {
			enemy = Reflection.newInstance(Random.oneOf(VaultLevel.T3Mobs));
		}

		enemy.pos = enemyPos;
		level.mobs.add(enemy);

	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return false;
	}

}
