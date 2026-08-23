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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultSentry;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class VaultCircleRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 2, Terrain.EMPTY );

		Painter.fill( level, this, 4, 1, 4, 1, Terrain.EMPTY );
		Painter.fill( level, this, 1, 4, 1, 4, Terrain.EMPTY );

		Painter.set( level, center(), Terrain.PEDESTAL);

		VaultSentry sentry = new VaultSentry();
		sentry.pos = level.pointToCell(center());

		sentry.scanLength = 4.49f;

		int w = level.width();

		switch (Random.Int(4)){
			case 0:
				sentry.scanWidth = 90f;

				sentry.scanDirs = new int[][]{
						new int[]{sentry.pos-1},
						new int[]{sentry.pos-1-w},
						new int[]{sentry.pos-w},
						new int[]{sentry.pos+1-w},
						new int[]{sentry.pos+1},
						new int[]{sentry.pos+1+w},
						new int[]{sentry.pos+w},
						new int[]{sentry.pos+w-1},
				};
				break;
			case 1: case 2:
				sentry.scanWidth = 45f;
				sentry.scanDirs = new int[][]{
						new int[]{sentry.pos-2, sentry.pos+2},
						new int[]{sentry.pos-2-level.width(), sentry.pos+2+level.width()},
						new int[]{sentry.pos-2-2*level.width(), sentry.pos+2+2*level.width()},
						new int[]{sentry.pos-1-2*level.width(), sentry.pos+1+2*level.width()},
						new int[]{sentry.pos-2*level.width(), sentry.pos+2*level.width()},
						new int[]{sentry.pos+1-2*level.width(), sentry.pos-1+2*level.width()},
						new int[]{sentry.pos+2-2*level.width(), sentry.pos-2+2*level.width()},
						new int[]{sentry.pos+2-level.width(), sentry.pos-2+level.width()},
				};
				break;
			case 3:
				sentry.scanWidth = 22.5f;

				sentry.scanDirs = new int[][]{
						new int[]{sentry.pos-3, sentry.pos-3*w, sentry.pos+3, sentry.pos+3*w},
						new int[]{sentry.pos-3-1*w, sentry.pos+1-3*w, sentry.pos+3+1*w, sentry.pos-1+3*w},
						new int[]{sentry.pos-3-2*w, sentry.pos+2-3*w, sentry.pos+3+2*w, sentry.pos-2+3*w},
						new int[]{sentry.pos-3-3*w, sentry.pos+3-3*w, sentry.pos+3+3*w, sentry.pos-3+3*w},
						new int[]{sentry.pos-2-3*w, sentry.pos+3-2*w, sentry.pos+2+3*w, sentry.pos-3+2*w},
						new int[]{sentry.pos-1-3*w, sentry.pos+3-1*w, sentry.pos+1+3*w, sentry.pos-3+1*w},
				};
				break;
		}

		level.mobs.add(sentry);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			Painter.drawInside(level, this, door, 4, Terrain.EMPTY);
		}
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return false;
	}

}
