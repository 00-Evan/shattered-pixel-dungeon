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

public class RingOfTenacity extends Ring {

	{
		name = "Ring of Tenacity";
	}

	@Override
	protected RingBuff buff( ) {
		return new Tenacity();
	}

	@Override
	public String desc() {
		return isKnown() ?
				"When worn, this ring will allow the wearer to resist normally mortal strikes. " +
				"The more injured the user is, the more resistant they will be to damage. " +
				"A degraded ring will instead make it easier for enemies to execute the wearer." :
				super.desc();
	}

	public class Tenacity extends RingBuff {
	}
}

