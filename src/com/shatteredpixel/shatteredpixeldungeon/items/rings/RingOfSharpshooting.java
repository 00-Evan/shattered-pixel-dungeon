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

public class RingOfSharpshooting extends Ring {

	{
		name = "Ring of Sharpshooting";
	}

	@Override
	protected RingBuff buff( ) {
		return new Aim();
	}

	@Override
	public String desc() {
		return isKnown() ?
				"This ring enhances the wearer's precision and aim, which will " +
				"make all projectile weapons more accurate and durable. " +
				"A degraded ring will have the opposite effect.":
				super.desc();
	}

	public class Aim extends RingBuff {
	}
}
