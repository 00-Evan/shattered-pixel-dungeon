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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements.Resistance;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Paralysis extends FlavourBuff {

	private static final float DURATION	= 10f;
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			target.paralysed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		unfreeze(target);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.PARALYSIS;
	}
	
	@Override
	public String toString() {
		return "Paralysed";
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	public static void unfreeze( Char ch ) {
		if (ch.buff( Paralysis.class ) == null &&
				ch.buff( Frost.class ) == null) {

			ch.paralysed = false;
		}
	}
}
