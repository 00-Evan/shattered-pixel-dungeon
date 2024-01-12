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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CaveRoom;
import com.watabou.utils.GameMath;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MineSmallRoom extends CaveRoom {

	@Override
	public int minWidth() { return Math.max(6, super.minWidth()); }

	@Override
	public int minHeight() { return Math.max(6, super.minHeight()); }

	@Override
	public float[] sizeCatProbs() {
		return new float[]{1, 0, 0};
	}

	@Override
	protected float fill() {
		return 0.40f;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Blacksmith.Quest.Type() == Blacksmith.Quest.CRYSTAL){
			for (int i = 0; i < width()*height()/3; i ++){
				Point r = random(1);
				if (level.map[level.pointToCell(r)] != Terrain.WALL) {
					Painter.set(level, r, Terrain.MINE_CRYSTAL);
				}
			}
		} else if (Blacksmith.Quest.Type() == Blacksmith.Quest.GNOLL) {

			//connections to non-secret rooms have a 9/10 chance to become empty, otherwise wall
			for (Room n : connected.keySet()){
				if (!(n instanceof SecretRoom) && connected.get(n).type == Door.Type.REGULAR){
					if (Random.Int(10) == 0){
						connected.get(n).set(Door.Type.EMPTY);
					} else {
						connected.get(n).set(Door.Type.WALL);
					}
					connected.get(n).lockTypeChanges(true);
				}
			}

			ArrayList<Door> doors = new ArrayList<>();
			for (Door d : connected.values()){
				if (d.type == Door.Type.WALL){
					doors.add(d);
				}
			}

			for (Point p : getPoints()){
				int cell = level.pointToCell(p);
				if (level.map[cell] == Terrain.EMPTY){
					float dist = 1000;
					for (Door d : doors){
						dist = Math.min(dist, Point.distance(p, d));
					}
					dist = GameMath.gate(1f, dist, 5f);
					float val = Random.Float((float) Math.pow(dist, 2));
					if (val <= 0.75f || dist <= 1) {
						Painter.set(level, cell, Terrain.MINE_BOULDER);
					} else if (val <= 5f && dist <= 2){
						Painter.set(level, cell, Terrain.EMPTY_DECO);
					}
				}
			}

		}

	}
}
