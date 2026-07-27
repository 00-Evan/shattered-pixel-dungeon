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

import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DwarfToken;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class VaultBookcaseTreasureRoom extends VaultTreasureRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );

		int firstItem;
		int secondItem;

		if (entrance.x == left || entrance.x == right){
			int bookTop = (int)GameMath.gate(top+1, entrance.y-2, bottom-5);
			Painter.fill(level, left+1, bookTop, 9, 5, Terrain.BOOKSHELF);
			Painter.fill(level, left+2, bookTop+1, 3, 3, Terrain.EMPTY_SP);
			Painter.fill(level, left+6, bookTop+1, 3, 3, Terrain.EMPTY_SP);
			if (entrance.x == left){
				firstItem = (left + 3) + level.width()*(bookTop+2);
				secondItem = (right - 3) + level.width()*(bookTop+2);
			} else {
				firstItem = (right - 3) + level.width()*(bookTop+2);
				secondItem = (left + 3) + level.width()*(bookTop+2);
			}
		} else {
			int bookLeft = (int)GameMath.gate(left+1, entrance.x-2, right-5);
			Painter.fill(level, bookLeft, top+1, 5, 9, Terrain.BOOKSHELF);
			Painter.fill(level, bookLeft+1, top+2, 3, 3, Terrain.EMPTY_SP);
			Painter.fill(level, bookLeft+1, top+6, 3, 3, Terrain.EMPTY_SP);
			if (entrance.y == top){
				firstItem = (bookLeft + 2) + level.width()*(top+3);
				secondItem = (bookLeft + 2) + level.width()*(bottom-3);
			} else {
				firstItem = (bookLeft + 2) + level.width()*(bottom-3);
				secondItem = (bookLeft + 2) + level.width()*(top+3);
			}
		}

		Painter.set(level, firstItem, Terrain.PEDESTAL);
		Painter.set(level, secondItem, Terrain.PEDESTAL);

		Item treasureItem = level.findPrizeItem();
		if (treasureItem != null){
			level.drop(treasureItem, firstItem);
		}

		treasureItem = ((VaultLevel)level).createEquipment(2);
		level.drop(treasureItem,secondItem).type = Heap.Type.CHEST;

		treasureItem = ((VaultLevel)level).findT3SolveItem();
		if (treasureItem == null){
			treasureItem = ((VaultLevel) level).createConsumabe(2);
		}
		level.drop(treasureItem, secondItem + PathFinder.NEIGHBOURS8[Random.Int(PathFinder.NEIGHBOURS8.length)]);

		treasureItem = new DwarfToken();
		level.drop(treasureItem, secondItem + PathFinder.NEIGHBOURS8[Random.Int(PathFinder.NEIGHBOURS8.length)]);

		level.addItemToSpawn(new PotionOfLiquidFlame());

		Painter.drawInside(level, this, entrance, 2, Terrain.EMPTY_SP);

	}

}
