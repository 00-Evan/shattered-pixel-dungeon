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
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Bless extends FlavourBuff {

	{
		type = buffType.POSITIVE;
	}

	@Override
	public int icon() {
		return BuffIndicator.BLESS;
	}

	@Override
	public String toString() {
		return "Blessed";
	}

	@Override
	public String desc() {
		return "A great burst of focus, some say it is inspired by the gods.\n" +
				"\n" +
				"Blessing significantly increases accuracy and evasion, making the blessed much more effective in combat.\n" +
				"\n" +
				"This blessing will last for  " + dispTurns() + ".";
	}
}
