/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.items.food;

import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;

public class OverpricedRation extends Food {

	{
		name = "overpriced food ration";
		image = ItemSpriteSheet.OVERPRICED;
		energy = Hunger.STARVING - Hunger.HUNGRY;
		message = "That food tasted ok.";
        hornValue = 1;
	}
	
	@Override
	public String info() {
		return "It looks exactly like a standard ration of food but smaller.";
	}
	
	@Override
	public int price() {
		return 20 * quantity;
	}
}
