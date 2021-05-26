/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.elementalpixel.elementalpixeldungeon.items.bags;


import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.potions.Potion;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;

public class PotionBandolier extends Bag {

	{
		image = ItemSpriteSheet.BANDOLIER;
	}

	@Override
	public boolean canHold( Item item ) {
		if (item instanceof Potion){
			return super.canHold(item);
		} else {
			return false;
		}
	}

	public int capacity(){
		return 19;
	}

	@Override
	public int value() {
		return 40;
	}

}
