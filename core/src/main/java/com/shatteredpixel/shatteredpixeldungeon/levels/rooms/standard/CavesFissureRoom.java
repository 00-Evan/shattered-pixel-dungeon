/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CavesFissureRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{9, 3, 1};
	}

	@Override
	public boolean canMerge(Level l, Point p, int mergeTerrain) {
		if (mergeTerrain == Terrain.CHASM) {
			return true;
		} else {
			int cell = l.pointToCell(pointInside(p, 1));
			return l.map[cell] != Terrain.CHASM;
		}
	}

	@Override
	public void paint(Level level) {

		boolean pathable = true;
		PathFinder.setMapSize(width()-2, height()-2);

		do {
			Painter.fill(level, this, Terrain.WALL);
			Painter.fill(level, this, 1, Terrain.EMPTY);

			for (Door door : connected.values()) {
				door.set(Door.Type.REGULAR);
			}

			PointF center = new PointF(center());
			center.x += 0.5f;
			center.y += 0.5f;

			//find the angle of each door from our center point
			ArrayList<Float> doorAngles = new ArrayList<>();
			for (Door d : connected.values()) {
				PointF doorCenter = new PointF(d.x + 0.5f, d.y + 0.5f);
				float doorAngle = angleBetweenPoints(center, doorCenter);
				if (doorAngle < 0) doorAngle += 360f;
				doorAngles.add(doorAngle);
			}

			//generate angles for 2-4 fissure lines, they can't be too close to doors or eachother
			ArrayList<Float> lineAngles = new ArrayList<>();
			int numLines = 1 + sizeCat.roomValue;
			for (int i = 0; i < numLines; i++) {
				int tries = 100;
				boolean valid;
				do {
					valid = true;
					float lineAngle = Random.Float(0, 360);
					for (float doorAngle : doorAngles) {
						float angleDiff = Math.abs(lineAngle - doorAngle);
						if (angleDiff > 180f) angleDiff = 360f - angleDiff;
						if (angleDiff <= (sizeCat == SizeCategory.NORMAL ? 30f : 15f)) {
							valid = false;
							break;
						}
					}

					for (float existingLineAngle : lineAngles) {
						float angleDiff = Math.abs(lineAngle - existingLineAngle);
						if (angleDiff > 180f) angleDiff = 360f - angleDiff;
						if (angleDiff <= (numLines == 2 ? 120f : 60f)) {
							valid = false;
							break;
						}
					}

					if (valid) {
						lineAngles.add(lineAngle);
					}

				} while (!valid && --tries > 0);
			}

			//just become an empty room if we can't make at least 2 lines
			if (lineAngles.size() < 2) {
				PathFinder.setMapSize(level.width(), level.height());
				return;
			}

			//fill in each fissure
			for (float lineAngle : lineAngles) {
				float dX = (float) Math.cos(lineAngle / A - Math.PI / 2.0);
				float dY = (float) Math.sin(lineAngle / A - Math.PI / 2.0);

				boolean horizontal;
				if (Math.abs(dX) >= Math.abs(dY)) {
					horizontal = true;
					dY /= Math.abs(dX);
					dX /= Math.abs(dX);
				} else {
					horizontal = false;
					dX /= Math.abs(dY);
					dY /= Math.abs(dY);
				}

				PointF curr = new PointF(center);
				int cell = (int) curr.x + ((int) curr.y) * level.width();
				Painter.set(level, cell, Terrain.CHASM);
				do {
					if (!horizontal) {
						if (level.map[cell - 1] == Terrain.EMPTY
								&& ((curr.x % 1 <= 0.5f) || sizeCat == SizeCategory.GIANT)) {
							Painter.set(level, cell - 1, Terrain.CHASM);
						}
						if (level.map[cell] == Terrain.EMPTY)
							Painter.set(level, cell, Terrain.CHASM);
						if (level.map[cell + 1] == Terrain.EMPTY
								&& ((curr.x % 1 > 0.5f) || sizeCat == SizeCategory.GIANT)) {
							Painter.set(level, cell + 1, Terrain.CHASM);
						}
					} else {
						if (level.map[cell - level.width()] == Terrain.EMPTY
								&& ((curr.y % 1 <= 0.5f) || sizeCat == SizeCategory.GIANT)) {
							Painter.set(level, cell - level.width(), Terrain.CHASM);
						}
						if (level.map[cell] == Terrain.EMPTY)
							Painter.set(level, cell, Terrain.CHASM);
						if (level.map[cell + level.width()] == Terrain.EMPTY
								&& ((curr.y % 1 > 0.5f) || sizeCat == SizeCategory.GIANT)) {
							Painter.set(level, cell + level.width(), Terrain.CHASM);
						}
					}

					curr.x += dX;
					curr.y += dY;
					cell = (int) curr.x + ((int) curr.y) * level.width();
				} while (level.map[cell] == Terrain.EMPTY || level.map[cell] == Terrain.CHASM);

			}

			//add chasm to the center in larger rooms
			if (lineAngles.size() >= 3) {
				if (sizeCat == SizeCategory.GIANT) {
					Painter.fill(level, (int) center.x - 2, (int) center.y - 2, 5, 5, Terrain.CHASM);
				} else {
					Painter.fill(level, (int) center.x - 1, (int) center.y - 1, 3, 3, Terrain.CHASM);
				}
			}

			//draw bridges for the chasms
			if (lineAngles.size() == 2) {
				buildBridge(level, Random.element(lineAngles), center, 1);
			} else {
				for (float angle : lineAngles) {
					buildBridge(level, angle, center, sizeCat.roomValue);
				}
			}

			int doorPoint = 0;
			for (Door door : connected.values()) {
				Painter.drawInside(level, this, door, 1, Terrain.EMPTY);
				if (door.x == left)         doorPoint = xyToRoomCoords(door.x+1, door.y);
				else if (door.x == right)   doorPoint = xyToRoomCoords(door.x-1, door.y);
				else if (door.y == top)     doorPoint = xyToRoomCoords(door.x, door.y+1);
				else if (door.y == bottom)  doorPoint = xyToRoomCoords(door.x, door.y-1);
			}

			//ensures that there is always a path to any non-chasm tile
			//TODO some copypasta from PatchRoom here, maybe standardize this as a static function in Room?
			pathable = true;
			boolean[] passable = new boolean[PathFinder.distance.length];

			for (Point p : shrink().getPoints()){
				int i = xyToRoomCoords(p.x, p.y);
				passable[i] = level.map[level.pointToCell(p)] != Terrain.CHASM;
			}

			PathFinder.buildDistanceMap(doorPoint, passable);

			for (Point p : shrink().getPoints()){
				int i = xyToRoomCoords(p.x, p.y);
				if (passable[i] && PathFinder.distance[i] == Integer.MAX_VALUE){
					pathable = false;
					break;
				}
			}

		} while (!pathable);

		PathFinder.setMapSize(level.width(), level.height());

	}

	private void buildBridge( Level level, float fisssureAngle, PointF center, int centerMargin){
		float dX = (float)Math.cos(fisssureAngle/A - Math.PI/2.0);
		float dY = (float)Math.sin(fisssureAngle/A - Math.PI/2.0);

		int edgemargin = 2;

		//horizontal bridge
		if (Math.abs(dY) >= Math.abs(dX)){
			int Y;
			if (dY > 0) Y = Random.IntRange((int)center.y+centerMargin, bottom-edgemargin);
			else        Y = Random.IntRange(top+edgemargin, (int)center.y-centerMargin);

			boolean foundChasm = false;
			if (dX <= 0) {
				for (int X = left + 1; X <= right - 1; X++) {
					int cell = X + Y * level.width();
					if (level.map[cell] == Terrain.CHASM) {
						foundChasm = true;
						Painter.set(level, cell, Terrain.EMPTY_SP);
					} else if (foundChasm) {
						break;
					}
				}
			} else {
				for (int X = right-1; X >= left+1; X--) {
					int cell = X + Y * level.width();
					if (level.map[cell] == Terrain.CHASM) {
						foundChasm = true;
						Painter.set(level, cell, Terrain.EMPTY_SP);
					} else if (foundChasm) {
						break;
					}
				}
			}

		//vertical bridge
		} else {
			int X;
			if (dX > 0) X = Random.IntRange((int)center.x+centerMargin, right-edgemargin);
			else        X = Random.IntRange(left+edgemargin, (int)center.x-centerMargin);

			boolean foundChasm = false;
			if (dY <= 0) {
				for (int Y = top + 1; Y <= bottom - 1; Y++) {
					int cell = X + Y * level.width();
					if (level.map[cell] == Terrain.CHASM) {
						foundChasm = true;
						Painter.set(level, cell, Terrain.EMPTY_SP);
					} else if (foundChasm){
						break;
					}
				}
			} else {
				for (int Y = bottom-1; Y >= top + 1; Y--) {
					int cell = X + Y * level.width();
					if (level.map[cell] == Terrain.CHASM) {
						foundChasm = true;
						Painter.set(level, cell, Terrain.EMPTY_SP);
					} else if (foundChasm){
						break;
					}
				}
			}

		}
	}

	private static final double A = 180 / Math.PI;

	protected static float angleBetweenPoints( PointF from, PointF to ){
		double m = (to.y - from.y)/(to.x - from.x);

		float angle = (float)(A*(Math.atan(m) + Math.PI/2.0));
		if (from.x > to.x) angle -= 180f;
		return angle;
	}

	protected int xyToRoomCoords(int x, int y){
		return (x-left-1) + ((y-top-1) * (width()-2));
	}

}
