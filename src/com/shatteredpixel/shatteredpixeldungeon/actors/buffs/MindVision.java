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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class MindVision extends FlavourBuff {

	public static final float DURATION = 20f;
	
	public int distance = 2;

	{
		type = buffType.POSITIVE;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.MIND_VISION;
	}
	
	@Override
	public String toString() {
		return "Mind vision";
	}

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}

	@Override
	public String desc() {
		return "Somehow you are able to see all creatures on this floor through your mind. It's a weird feeling.\n" +
				"\n" +
				"All characters on this floor are visible to you as long as you have mind vision. " +
				"Seeing a creature through mind vision counts as it being seen or nearby for " +
				"the purposes of many magical effects.\n" +
				"\n" +
				"The mind vision will last for " + dispTurns() + ".";
	}
}
