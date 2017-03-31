/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FireTrap;
import com.watabou.utils.Random;

public class BurnedRoom extends StandardRoom {
	
	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		for (int i=top + 1; i < bottom; i++) {
			for (int j=left + 1; j < right; j++) {
				int cell = i * level.width() + j;
				int t = Terrain.EMBERS;
				switch (Random.Int( 5 )) {
					case 0:
						t = Terrain.EMPTY;
						break;
					case 1:
						t = Terrain.TRAP;
						level.setTrap(new FireTrap().reveal(), cell);
						break;
					case 2:
						t = Terrain.SECRET_TRAP;
						level.setTrap(new FireTrap().hide(), cell);
						break;
					case 3:
						t = Terrain.INACTIVE_TRAP;
						FireTrap trap = new FireTrap();
						trap.reveal().active = false;
						level.setTrap(trap, cell);
						break;
				}
				level.map[cell] = t;
			}
		}
	}
}
