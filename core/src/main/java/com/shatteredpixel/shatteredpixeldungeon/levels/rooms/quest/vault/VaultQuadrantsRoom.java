/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultLaser;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class VaultQuadrantsRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Point c = center();
		Painter.drawInside( level, this, new Point(left, c.y), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(right, c.y), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(c.x, top), 3, Terrain.WALL);
		Painter.drawInside( level, this, new Point(c.x, bottom), 3, Terrain.WALL);

		//TODO 4x laser?
		Painter.set( level, c, Terrain.STATUE);

		VaultLaser laser = new VaultLaser();
		//laser.laserDirs = new int[];

		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return c.x != p.x && c.y != p.y && super.canConnect(p);
	}

}
