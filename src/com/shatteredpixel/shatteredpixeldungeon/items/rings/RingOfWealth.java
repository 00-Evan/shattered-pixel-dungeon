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
package com.shatteredpixel.shatteredpixeldungeon.items.rings;

public class RingOfWealth extends Ring {

	{
		name = "Ring of Wealth";
	}

	@Override
	protected RingBuff buff( ) {
		return new Wealth();
	}

	@Override
	public String desc() {
		return isKnown() ?
				"It's not clear what this ring does exactly, good luck may influence " +
				"the life of an adventurer in many subtle ways. " +
				"Naturally a degraded ring would give bad luck." :
				super.desc();
	}

	public class Wealth extends RingBuff {
	}
}
