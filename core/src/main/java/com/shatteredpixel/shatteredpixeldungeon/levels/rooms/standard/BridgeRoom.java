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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

//TODO honestly this might work better as a type of tunnel room
public class BridgeRoom extends StandardRoom {
	
	@Override
	public void paint(Level level) {
		
		Painter.fill( level, this, Terrain.WALL );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
		
		Painter.fill( level, this, 1,
				!Dungeon.bossLevel() && !Dungeon.bossLevel( Dungeon.depth + 1 ) && Random.Int( 3 ) == 0 ?
						Terrain.CHASM :
						Terrain.WATER );
		
		Point door1 = null;
		Point door2 = null;
		for (Point p : connected.values()) {
			if (door1 == null) {
				door1 = p;
			} else {
				door2 = p;
			}
		}
		
		if ((door1.x == left && door2.x == right) ||
				(door1.x == right && door2.x == left)) {
			
			int s = width() / 2;
			
			Painter.drawInside( level, this, door1, s, Terrain.EMPTY_SP );
			Painter.drawInside( level, this, door2, s, Terrain.EMPTY_SP );
			Painter.fill( level, center().x, Math.min( door1.y, door2.y ), 1, Math.abs( door1.y - door2.y ) + 1, Terrain.EMPTY_SP );
			
		} else
		if ((door1.y == top && door2.y == bottom) ||
				(door1.y == bottom && door2.y == top)) {
			
			int s = height() / 2;
			
			Painter.drawInside( level, this, door1, s, Terrain.EMPTY_SP );
			Painter.drawInside( level, this, door2, s, Terrain.EMPTY_SP );
			Painter.fill( level, Math.min( door1.x, door2.x ), center().y, Math.abs( door1.x - door2.x ) + 1, 1, Terrain.EMPTY_SP );
			
		} else
		if (door1.x == door2.x) {
			
			Painter.fill( level, door1.x == left ? left + 1 : right - 1, Math.min( door1.y, door2.y ), 1, Math.abs( door1.y - door2.y ) + 1, Terrain.EMPTY_SP );
			
		} else
		if (door1.y == door2.y) {
			
			Painter.fill( level, Math.min( door1.x, door2.x ), door1.y == top ? top + 1 : bottom - 1, Math.abs( door1.x - door2.x ) + 1, 1, Terrain.EMPTY_SP );
			
		} else
		if (door1.y == top || door1.y == bottom) {
			
			Painter.drawInside( level, this, door1, Math.abs( door1.y - door2.y ), Terrain.EMPTY_SP );
			Painter.drawInside( level, this, door2, Math.abs( door1.x - door2.x ), Terrain.EMPTY_SP );
			
		} else
		if (door1.x == left || door1.x == right) {
			
			Painter.drawInside( level, this, door1, Math.abs( door1.x - door2.x ), Terrain.EMPTY_SP );
			Painter.drawInside( level, this, door2, Math.abs( door1.y - door2.y ), Terrain.EMPTY_SP );
			
		}
	}
}
