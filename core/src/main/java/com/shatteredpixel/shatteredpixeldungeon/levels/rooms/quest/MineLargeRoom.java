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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalGuardian;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FungalSentry;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGuard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollSapper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CaveRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GnollRockfallTrap;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MineLargeRoom extends CaveRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public int minHeight() {
		return 11;
	}

	@Override
	public int minWidth() {
		return 11;
	}

	@Override
	protected float fill() {
		return 0.55f;
	}

	@Override
	public int mobSpawnWeight() {
		//These rooms always spawn their own enemies
		// so don't amp up regular enemy spawns too
		return 1;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Blacksmith.Quest.Type() == Blacksmith.Quest.CRYSTAL){
			Painter.fillEllipse(level, this, 3, Terrain.MINE_CRYSTAL);
			Painter.fillEllipse(level, this, 4, Terrain.EMPTY);

			Point p = random(5);
			ArrayList<Integer> internalcells = new ArrayList<>();
			findInternalCells(level, level.pointToCell(p), internalcells);

			//we want to ensure that every internal cell has no way out, even diagonally
			for (int i : internalcells){
				for (int j : PathFinder.CIRCLE8){
					if (!internalcells.contains(i+j) && level.map[i+j] != Terrain.MINE_CRYSTAL){
						level.map[i] = Terrain.MINE_CRYSTAL;
						break;
					}
				}
			}

			for (int i = 0; i < width()*height()/4; i ++){
				Point r = random(1);
				if (level.map[level.pointToCell(r)] != Terrain.WALL) {
					Painter.set(level, r, Terrain.MINE_CRYSTAL);
				}
			}

			CrystalGuardian m = new CrystalGuardian();
			m.pos = level.pointToCell(p);
			level.mobs.add(m);
			Painter.set(level, p, Terrain.EMPTY);

		} else if (Blacksmith.Quest.Type() == Blacksmith.Quest.GNOLL){
			Painter.fillEllipse(level, this, 3, Terrain.EMPTY);

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

			int sapperPos = level.pointToCell(random(5));
			GnollSapper s = new GnollSapper();
			s.pos = sapperPos;
			s.spawnPos = s.pos;
			level.mobs.add(s);

			int guardPos;
			do {
				guardPos = sapperPos+PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (level.map[guardPos] != Terrain.EMPTY);
			GnollGuard g = new GnollGuard();
			g.pos = guardPos;
			level.mobs.add(g);
			s.linkPartner(g);

			int barricades = Random.Int(2) == 0 ? 2 : 1;
			for (int i = 0; i < barricades; i ++){
				int barricadePos = sapperPos+PathFinder.NEIGHBOURS8[Random.Int(8)];
				if (level.map[barricadePos] == Terrain.EMPTY && barricadePos != guardPos){
					Painter.set(level, barricadePos, Terrain.BARRICADE);
				} else {
					i--;
				}
			}

			int traps = square() > 150 ? 3 : 2;
			for (int i = 0; i < traps; i ++){
				Point r;
				do {
					r = random(2);
				} while (level.map[level.pointToCell(r)] != Terrain.EMPTY
						|| level.pointToCell(r) == sapperPos
						|| level.pointToCell(r) == guardPos);
				Painter.set(level, r, Terrain.TRAP);
				level.setTrap(new GnollRockfallTrap().reveal(), level.pointToCell(r));
			}

			for (Point p : getPoints()){
				int cell = level.pointToCell(p);
				if (level.map[cell] == Terrain.EMPTY
						&& cell != sapperPos
						&& cell != guardPos){
					float dist = 1000;
					for (Door d : doors){
						dist = Math.min(dist, Point.distance(p, d));
					}
					dist = GameMath.gate(1f, dist-0.5f, 4f);
					float val = Random.Float((float) Math.pow(dist, 2));
					if (val <= 0.75f || dist <= 1) {
						Painter.set(level, cell, Terrain.MINE_BOULDER);
					} else if (val <= 5f && dist <= 3){
						Painter.set(level, cell, Terrain.EMPTY_DECO);
					}
				}
			}
		} else if (Blacksmith.Quest.Type() == Blacksmith.Quest.FUNGI){

			for (Point p : getPoints()){
				int cell = level.pointToCell(p);
				if (level.map[cell] == Terrain.EMPTY){
					level.map[cell] = Terrain.HIGH_GRASS;
				}
			}

			Painter.fillEllipse(level, this, 3, Terrain.GRASS);

			for (int i = 0; i < width() * height() / 6; i++) {
				Point r = random(1);
				if (level.map[level.pointToCell(r)] != Terrain.WALL) {
					Painter.set(level, r, Terrain.HIGH_GRASS);
				}
			}

			Point p = center();
			FungalSentry m = new FungalSentry();
			m.pos = level.pointToCell(p);
			level.mobs.add(m);
			Painter.set(level, p, Terrain.GRASS);

			//no high grass directly above the sentry
			p.y--;
			Painter.set(level, p, Terrain.GRASS);

		} else {
			Painter.fillEllipse(level, this, 3, Terrain.EMPTY);
		}

	}

	private void findInternalCells(Level level, int cell, ArrayList<Integer> internalCells){
		for (int i : PathFinder.NEIGHBOURS4){
			if (!internalCells.contains(cell+i) && level.map[cell+i] != Terrain.MINE_CRYSTAL){
				internalCells.add(cell+i);
				findInternalCells(level, cell+i, internalCells);
			}
		}
	}

}
