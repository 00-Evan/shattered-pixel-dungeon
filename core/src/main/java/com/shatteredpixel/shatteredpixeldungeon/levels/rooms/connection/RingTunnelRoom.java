/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class RingTunnelRoom extends TunnelRoom {

	@Override
	public int minWidth() {
		return Math.max(5, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(5, super.minHeight());
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int floor = level.tunnelTile();

		Rect ring = getConnectionSpace();

		Painter.fill( level, ring.left, ring.top, 3, 3,  floor);
		Painter.fill( level, ring.left+1, ring.top+1, 1, 1,  Terrain.WALL);
	}

	//caches the value so multiple calls will always return the same.
	private Rect connSpace;

	@Override
	protected Rect getConnectionSpace() {
		if (connSpace == null) {
			Point c = getDoorCenter();

			c.x = (int) GameMath.gate(left + 2, c.x, right - 2);
			c.y = (int) GameMath.gate(top + 2, c.y, bottom - 2);


			connSpace = new Rect(c.x-1, c.y-1, c.x+1, c.y+1);
		}

		return connSpace;
	}
}
