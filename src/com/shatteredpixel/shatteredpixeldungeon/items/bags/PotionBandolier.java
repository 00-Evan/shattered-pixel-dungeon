/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class PotionBandolier extends Bag {

	{
		name = "potion bandolier";
		image = ItemSpriteSheet.BANDOLIER;

		size = 12;
	}

	@Override
	public boolean grab( Item item ) {
		return item instanceof Potion;
	}

	@Override
	public int price() {
		return 50;
	}

	@Override
	public String info() {
		return
			"This thick bandolier fits around your chest like a sash, it has many small vials to hold your potions.\n\n" +
			"The vials are made of tempered glass, and should be quite resistant to the cold.";
	}
}
