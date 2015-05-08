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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Amok extends FlavourBuff {

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.AMOK;
	}

    @Override
    public void detach() {
        super.detach();
        if (target instanceof Mob)
            ((Mob)target).aggro( null );
    }

    @Override
	public String toString() {
		return "Amok";
	}

	@Override
	public String desc() {
		return "Amok causes a state of great rage and confusion in its target.\n" +
				"\n" +
				"When a creature is amoked, they will attack whatever is near them, whether they be friend or foe.\n" +
				"\n" +
				"The amok will last for " + dispTurns() + ".";
	}
}
