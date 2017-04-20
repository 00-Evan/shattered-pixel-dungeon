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
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;

public class SewerBossEntranceRoom extends EntranceRoom {
	
	@Override
	public int minWidth() {
		return 9;
	}
	
	@Override
	public int maxWidth() {
		return 9;
	}
	
	@Override
	public int minHeight() {
		return 6;
	}
	
	@Override
	public int maxHeight() {
		return 10;
	}
	
	//TODO perhaps I just want to deny all top-side connections
	@Override
	public boolean canConnect(Point p) {
		//refuses connections on the center 3 tiles on the top side, and the top tile along left/right
		return super.canConnect(p)
				&& !(p.y == top && p.x >= (left + (width()/2 - 1)) && p.x <= (left + (width()/2 + 1)))
				&& p.y != top+1;
	}
	
	public void paint(Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		Painter.fill( level, left+1, top+1, width()-2, 1, Terrain.WALL_DECO);
		Painter.fill( level, left+1, top+2, width()-2, 1, Terrain.WATER);
		
		Painter.set( level, left+width()/2, top+1, Terrain.LOCKED_EXIT);
		level.exit = level.pointToCell(new Point(left+width()/2, top+1));
		
		do {
			level.entrance = level.pointToCell(random(3));
		} while (level.findMob(level.entrance) != null);
		Painter.set( level, level.entrance, Terrain.ENTRANCE );
		
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
			
			if (door.y == top){
				Painter.set( level, door.x, door.y+1, Terrain.WATER);
			}
		}
		
	}
	
}
