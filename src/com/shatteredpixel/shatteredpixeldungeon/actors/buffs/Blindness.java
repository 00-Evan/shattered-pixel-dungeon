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
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Blindness extends FlavourBuff {

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
	
	@Override
	public int icon() {
		return BuffIndicator.BLINDNESS;
	}
	
	@Override
	public String toString() {
		return "Blinded";
	}

	@Override
	public String desc() {
		return "Blinding turns the surrounding world into a dark haze.\n" +
				"\n" +
				"While blinded, a character can't see more than one tile infront of themselves, rendering ranged " +
				"attacks useless and making it very easy to lose track of distant enemies. Additionally, a blinded " +
				"hero is unable to read scrolls or books.\n" +
				"\n" +
				"The blindness will last for " + dispTurns() + ".";
	}
}
