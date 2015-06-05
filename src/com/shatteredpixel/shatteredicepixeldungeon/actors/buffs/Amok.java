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
package com.shatteredpixel.shatteredicepixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredicepixeldungeon.ui.BuffIndicator;

public class Amok extends FlavourBuff {
	
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
}
