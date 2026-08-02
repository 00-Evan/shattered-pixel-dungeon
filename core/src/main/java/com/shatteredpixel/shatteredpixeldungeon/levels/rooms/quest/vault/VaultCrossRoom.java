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

public class VaultCrossRoom extends VaultRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Painter.fill( level, this, 4, 1, 4, 1, Terrain.EMPTY );
		Painter.fill( level, this, 1, 4, 1, 4, Terrain.EMPTY );

		Painter.set( level, center(), Terrain.PEDESTAL);

		VaultSentry sentry = new VaultSentry();
		sentry.pos = level.pointToCell(center());

		sentry.scanLength = 4;
		sentry.scanWidth = 90;

		sentry.afterScanCooldown = 3;

		sentry.scanDirs = new int[][]{
				new int[]{sentry.pos-1},
				new int[]{sentry.pos-level.width()},
				new int[]{sentry.pos+1},
				new int[]{sentry.pos+level.width()},
		};

		level.mobs.add(sentry);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return (Math.abs(c.x - p.x) <= 1 || Math.abs(c.y - p.y) <= 1) && super.canConnect(p);
	}
}
