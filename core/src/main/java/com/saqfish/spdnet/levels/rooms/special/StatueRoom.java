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

package com.saqfish.spdnet.levels.rooms.special;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.mobs.Statue;
import com.saqfish.spdnet.items.keys.IronKey;
import com.saqfish.spdnet.levels.Level;
import com.saqfish.spdnet.levels.Terrain;
import com.saqfish.spdnet.levels.painters.Painter;
import com.watabou.utils.Point;

public class StatueRoom extends SpecialRoom {

	public void paint( Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		int cx = c.x;
		int cy = c.y;
		
		Door door = entrance();
		
		door.set( Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey( Dungeon.depth ) );
		
		if (door.x == left) {
			
			Painter.fill( level, right - 1, top + 1, 1, height() - 2 , Terrain.STATUE );
			cx = right - 2;
			
		} else if (door.x == right) {
			
			Painter.fill( level, left + 1, top + 1, 1, height() - 2 , Terrain.STATUE );
			cx = left + 2;
			
		} else if (door.y == top) {
			
			Painter.fill( level, left + 1, bottom - 1, width() - 2, 1 , Terrain.STATUE );
			cy = bottom - 2;
			
		} else if (door.y == bottom) {
			
			Painter.fill( level, left + 1, top + 1, width() - 2, 1 , Terrain.STATUE );
			cy = top + 2;
			
		}
		
		Statue statue = Statue.random();
		statue.pos = cx + cy * level.width();
		level.mobs.add( statue );
	}
}
