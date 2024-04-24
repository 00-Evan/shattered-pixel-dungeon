/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class StandardBridgeRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(5, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(5, super.minHeight());
	}

	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		int cell = l.pointToCell(pointInside(p, 1));
		return l.map[cell] != spaceTile();
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return super.canPlaceItem(p, l) && (spaceRect == null || !spaceRect.inside(p));
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return super.canPlaceItem(p, l) && (spaceRect == null || !spaceRect.inside(p));
	}

	//keep these so that subclasses can use them in their methods
	protected Rect spaceRect;
	protected Rect bridgeRect;

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );

		//prefer to place the bridge space to segment the most doors, or the most space in the room
		int doorsXY = 0;
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
			if (door.x == left || door.x == right){
				doorsXY++;
			} else {
				doorsXY--;
			}
		}
		doorsXY += (width() - height())/2;

		if (doorsXY > 0 || (doorsXY == 0 && Random.Int(2) == 0)){

			ArrayList<Point> spacePoints = new ArrayList<>();
			for (Door door : connected.values()) {
				if (door.y == top || door.y == bottom){
					spacePoints.add(door);
				}
			}

			//add fake doors for very left/right
			spacePoints.add(new Point(left+1, 0));
			spacePoints.add(new Point(right-1, 0));

			Collections.sort(spacePoints, new Comparator<Point>() {
				@Override
				public int compare(Point d1, Point d2) {
					return d1.x - d2.x;
				}
			});

			int spaceStart = -1;
			int spaceEnd = -1;
			for (int i = 0; i < spacePoints.size()-1; i++){
				if (spaceEnd - spaceStart < spacePoints.get(i+1).x - spacePoints.get(i).x){
					spaceStart = spacePoints.get(i).x;
					spaceEnd = spacePoints.get(i+1).x;
				}
			}

			while (spaceEnd - spaceStart > maxBridgeWidth(width())+1){
				if (Random.Int(2) == 0){
					spaceStart++;
				} else {
					spaceEnd--;
				}
			}

			spaceRect = new Rect(spaceStart+1, top+1, spaceEnd, bottom);

			int bridgeY = Random.NormalIntRange(spaceRect.top+1, spaceRect.bottom-2);
			bridgeRect = new Rect(spaceRect.left, bridgeY, spaceRect.right, bridgeY+1);

		} else {

			ArrayList<Point> spacePoints = new ArrayList<>();
			for (Door door : connected.values()) {
				if (door.x == left || door.x == right){
					spacePoints.add(door);
				}
			}

			//add fake doors for very top/bottom
			spacePoints.add(new Point(0, top+1));
			spacePoints.add(new Point(0, bottom-1));

			Collections.sort(spacePoints, new Comparator<Point>() {
				@Override
				public int compare(Point d1, Point d2) {
					return d1.y - d2.y;
				}
			});

			int spaceStart = -1;
			int spaceEnd = -1;
			for (int i = 0; i < spacePoints.size()-1; i++){
				if (spaceEnd - spaceStart < spacePoints.get(i+1).y - spacePoints.get(i).y){
					spaceStart = spacePoints.get(i).y;
					spaceEnd = spacePoints.get(i+1).y;
				}
			}

			while (spaceEnd - spaceStart > maxBridgeWidth(height())+1){
				if (Random.Int(2) == 0){
					spaceStart++;
				} else {
					spaceEnd--;
				}
			}

			spaceRect = new Rect(left+1, spaceStart+1, right, spaceEnd);

			int bridgeX = Random.NormalIntRange(spaceRect.left+1, spaceRect.right-2);
			bridgeRect = new Rect(bridgeX, spaceRect.top, bridgeX+1, spaceRect.bottom);

		}

		Painter.fill(level, spaceRect, spaceTile());
		Painter.fill(level, bridgeRect, Terrain.EMPTY_SP);

	}

	protected abstract int maxBridgeWidth( int roomDimension );

	protected abstract int spaceTile();
}
