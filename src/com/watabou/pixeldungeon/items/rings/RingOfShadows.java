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

public class RingOfShadows extends Ring {

	{
		name = "Ring of Shadows";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Shadows();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"Enemies will be less likely to notice you if you wear this ring. Degraded rings " +
			"of shadows will alert enemies who might otherwise not have noticed your presence." :
			super.desc();
	}
	
	public class Shadows extends RingBuff {
	}
}
