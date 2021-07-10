/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.levels.traps;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Ooze;
import com.saqfish.spdnet.effects.Splash;
import com.watabou.utils.PathFinder;

public class OozeTrap extends Trap {

	{
		color = GREEN;
		shape = DOTS;
	}

	@Override
	public void activate() {

		for( int i : PathFinder.NEIGHBOURS9) {
			if (!Dungeon.level.solid[pos + i]) {
				Splash.at( pos + i, 0x000000, 5);
				Char ch = Actor.findChar( pos + i );
				if (ch != null && !ch.flying){
					Buff.affect(ch, Ooze.class).set( Ooze.DURATION );
				}
			}
		}
	}
}
