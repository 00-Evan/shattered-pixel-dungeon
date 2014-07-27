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
package com.watabou.pixeldungeon.items.rings;

public class RingOfMending extends Ring {
	
	{
		name = "Ring of Mending";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Rejuvenation();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"This ring increases the body's regenerative properties, allowing " +
			"one to recover lost health at an accelerated rate. Degraded rings will " +
			"decrease or even halt one's natural regeneration." :
			super.desc();
	}
	
	public class Rejuvenation extends RingBuff {
	}
}
