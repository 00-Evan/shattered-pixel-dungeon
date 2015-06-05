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
package com.shatteredpixel.shatteredicepixeldungeon.items.potions;

import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;

public class PotionOfExperience extends Potion {

	{
		name = "Potion of Experience";
		initials = "Ex";

        bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		hero.earnExp( hero.maxExp() - hero.exp );
	}
	
	@Override
	public String desc() {
		return
			"The storied experiences of multitudes of battles reduced to liquid form, " +
			"this draught will instantly raise your experience level.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 80 * quantity : super.price();
	}
}
