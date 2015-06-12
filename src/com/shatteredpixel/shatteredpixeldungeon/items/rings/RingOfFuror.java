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

public class RingOfFuror extends Ring {

	{
		name = "Ring of Furor";
	}

	@Override
	protected RingBuff buff( ) {
		return new Furor();
	}

	@Override
	public String desc() {
		return isKnown() ?
				"This ring grants the wearer an inner fury, allowing them to attack more rapidly. " +
				"This fury works best in large bursts, so slow weapons benefit far more than fast ones. " +
				"A degraded ring will instead slow the wearer's speed of attack." :
				super.desc();
	}

	public class Furor extends RingBuff {
	}
}
