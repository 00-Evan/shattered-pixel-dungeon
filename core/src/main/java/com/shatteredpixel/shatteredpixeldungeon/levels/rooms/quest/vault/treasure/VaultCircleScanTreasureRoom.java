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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.VaultSentry;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.VaultLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.Collections;

public class VaultCircleScanTreasureRoom extends VaultTreasureRoom {

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );

		Painter.fill( level, this, 2, Terrain.EMPTY );

		Painter.fill( level, this, 4, 1, 4, 1, Terrain.EMPTY );
		Painter.fill( level, this, 1, 4, 1, 4, Terrain.EMPTY );

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );
		Painter.drawInside(level, this, entrance, 3, Terrain.EMPTY);

		Point c = center();

		boolean clockwise = true;

		//place sentry in center
		Painter.set(level, c, Terrain.PEDESTAL);

		Rect treasure = null;
		if (entrance.x == left){
			Painter.set(level, c.x+2, c.y, Terrain.WALL);
			Painter.set(level, c.x+1, c.y, Terrain.STATUE);
			Painter.set(level, c.x-1, c.y, Terrain.STATUE);
			treasure = new Rect(left+1, c.y-1, c.x-2, c.y+1);
			Painter.fill(level, treasure.left, treasure.top, treasure.width()+1, treasure.height()+1, Terrain.WALL);
			treasure.right--;
			if (entrance.y < c.y){
				clockwise = false;
				treasure.top++;
			} else {
				clockwise = true;
				treasure.bottom--;
			}
		} else if (entrance.y == top){
			Painter.set(level, c.x, c.y+2, Terrain.WALL);
			Painter.set(level, c.x, c.y+1, Terrain.STATUE);
			Painter.set(level, c.x, c.y-1, Terrain.STATUE);
			treasure = new Rect(c.x-1, top+1, c.x+1, top+3);
			Painter.fill(level, treasure.left, treasure.top, treasure.width()+1, treasure.height()+1, Terrain.WALL);
			treasure.bottom--;
			if (entrance.x < c.x){
				clockwise = true;
				treasure.left++;
			} else {
				clockwise = false;
				treasure.right--;
			}
		} else if (entrance.x == right){
			Painter.set(level, c.x-2, c.y, Terrain.WALL);
			Painter.set(level, c.x-1, c.y, Terrain.STATUE);
			Painter.set(level, c.x+1, c.y, Terrain.STATUE);
			treasure = new Rect(right-3, c.y-1, right-1, c.y+1);
			Painter.fill(level, treasure.left, treasure.top, treasure.width()+1, treasure.height()+1, Terrain.WALL);
			treasure.left++;
			if (entrance.y < c.y){
				clockwise = true;
				treasure.top++;
			} else {
				clockwise = false;
				treasure.bottom--;
			}
		} else if (entrance.y == bottom){
			Painter.set(level, c.x, c.y-2, Terrain.WALL);
			Painter.set(level, c.x, c.y-1, Terrain.STATUE);
			Painter.set(level, c.x, c.y+1, Terrain.STATUE);
			treasure = new Rect(c.x-1, bottom-3, c.x+1, bottom-1);
			Painter.fill(level, treasure.left, treasure.top, treasure.width()+1, treasure.height()+1, Terrain.WALL);
			treasure.top++;
			if (entrance.x < c.x){
				clockwise = false;
				treasure.left++;
			} else {
				clockwise = true;
				treasure.right--;
			}
		}
		Painter.fill(level, treasure.left, treasure.top, treasure.width()+1, treasure.height()+1, Terrain.EMPTY_SP);

		VaultSentry sentry = new VaultSentry();
		sentry.pos = level.pointToCell(c);
		sentry.scanLength = 4.49f;
		int w = level.width();
		sentry.scanWidth = 45f;
		sentry.scanDirs = new int[][]{
				new int[]{sentry.pos-2},
				new int[]{sentry.pos-2-w},
				new int[]{sentry.pos-2-2*w},
				new int[]{sentry.pos-1-2*w},
				new int[]{sentry.pos-2*w},
				new int[]{sentry.pos+1-2*w},
				new int[]{sentry.pos+2-2*w},
				new int[]{sentry.pos+2-w},
				new int[]{sentry.pos+2},
				new int[]{sentry.pos+2+w},
				new int[]{sentry.pos+2+2*w},
				new int[]{sentry.pos+1+2*w},
				new int[]{sentry.pos+2*w},
				new int[]{sentry.pos-1+2*w},
				new int[]{sentry.pos-2+2*w},
				new int[]{sentry.pos-2+w},
		};

		if (!clockwise){
			int[][] reverse = new int[sentry.scanDirs.length][];
			for (int i = 0; i < sentry.scanDirs.length; i++){
				reverse[i] = sentry.scanDirs[sentry.scanDirs.length-i-1];
			}
			sentry.scanDirs = reverse;
		}

		level.mobs.add(sentry);

		int treasurePos = level.pointToCell(Random.element(treasure.getPoints()));
		Item treasureItem = ((VaultLevel)level).createEquipment(1);
		level.drop(treasureItem, treasurePos).type = Heap.Type.CHEST;

		treasureItem = ((VaultLevel) level).findT2SolveItem();
		if (treasureItem == null){
			treasureItem = ((VaultLevel)level).createConsumabe(1);
		}
		do {
			treasurePos = level.pointToCell(Random.element(treasure.getPoints()));
		} while (level.heaps.get(treasurePos) != null);
		level.drop(treasureItem, treasurePos);
	}

	@Override
	public boolean canPlaceGrass(Point p) {
		return false; //no grass to obstruct vision
	}

	@Override
	public boolean canConnect(Point p) {
		Point c = center();
		return (Math.abs(c.x - p.x) > 1 && Math.abs(c.y - p.y) > 1) && super.canConnect(p);
	}
}
