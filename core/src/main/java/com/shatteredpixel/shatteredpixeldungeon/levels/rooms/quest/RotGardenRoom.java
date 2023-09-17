/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotHeart;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RotLasher;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class RotGardenRoom extends SpecialRoom {
	
	@Override
	public int minWidth() { return 10; }
	
	@Override
	public int minHeight() { return 10; }

	public void paint( Level level ) {

		Door entrance = entrance();
		entrance.set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));

		//define basic terrain, mostly high grass with some chaotically placed wall tiles
		Painter.fill(level, this, Terrain.WALL);
		Painter.set(level, entrance, Terrain.LOCKED_DOOR);
		Painter.fill(level, this, 1, Terrain.HIGH_GRASS);
		for (int i = 0; i < 12; i ++){
			Painter.set(level, random(1), Terrain.WALL);
		}
		for (int i = 0; i < 8; i ++){
			Painter.set(level, random(2), Terrain.WALL);
		}
		for (int i = 0; i < 4; i ++){
			Painter.set(level, random(3), Terrain.WALL);
		}
		Painter.drawInside(level, this, entrance, 3, Terrain.HIGH_GRASS);

		boolean[] passable = new boolean[level.length()];
		for (int i = 0; i < passable.length; i++){
			passable[i] = level.map[i] != Terrain.WALL;
		}

		//place the heart in a slightly random location sufficiently far from the entrance
		int entryPos = level.pointToCell(entrance());
		PathFinder.buildDistanceMap(entryPos, passable);
		ArrayList<Integer> candidates = new ArrayList<>();
		for (Point p : getPoints()){
			int i = level.pointToCell(p);
			if (PathFinder.distance[i] != Integer.MAX_VALUE){
				if (PathFinder.distance[i] >= 3){
					candidates.add(i);
				}
			} else {
				//fill in grass tiles that are enclosed
				if (level.map[i] == Terrain.HIGH_GRASS){
					level.map[i] = Terrain.WALL;
				}
			}
		}
		Random.shuffle(candidates);
		int closestPos = 3;
		while (candidates.size() > 5){
			for (Integer i : candidates.toArray(new Integer[0])){
				if (candidates.size() > 5 && PathFinder.distance[i] == closestPos){
					candidates.remove(i);
				}
			}
			closestPos++;
		}
		int heartPos = Random.element(candidates);
		placePlant(level, heartPos, new RotHeart());

		//place up to 6 lashers in such a way that there is always a safe path to the heart
		boolean[] newPassable = Arrays.copyOf(passable, passable.length);
		int maxLashers = 6;
		for (int i = 1; i <= maxLashers; i++){
			int pos;
			int tries = 50;
			do {
				pos = level.pointToCell(random());
				tries--;
			} while (tries > 0 && !validPlantPos(passable, newPassable, level, pos, heartPos, entryPos));
			if (tries <= 0){
				break;
			}
			placePlant(level, pos, new RotLasher());
		}

		//If the only open cells next to the heart are a diagonal, open one additional adjacent cell
		//This is important so that the heart can spread gas
		boolean openCardinal = false;
		for (int i = 1; i < PathFinder.CIRCLE8.length; i+=2){
			if (level.map[heartPos + PathFinder.CIRCLE8[i]] != Terrain.WALL) openCardinal = true;
		}
		if (!openCardinal){
			for (int i = 0; i < PathFinder.CIRCLE8.length; i+=2){
				if (level.map[heartPos + PathFinder.CIRCLE8[i]] != Terrain.WALL){
					Painter.set(level, heartPos + PathFinder.CIRCLE8[i+1], Terrain.HIGH_GRASS);
				}
			}
		}

		//if almost every open cell next to the heart has a lasher threatening it, clear one lasher
		int safeHeartcells = 0;
		HashSet<Mob> adjacentLashers = new HashSet<>();
		for (int i : PathFinder.NEIGHBOURS8){
			if (level.map[heartPos+i] == Terrain.WALL) {
				continue;
			}
			boolean foundLasher = false;
			for (int j : PathFinder.NEIGHBOURS8){
				if (heartPos+i+j != heartPos
						&& level.map[heartPos+i+j] != Terrain.WALL
						&& level.findMob(heartPos+i+j) != null){
					foundLasher = true;
					adjacentLashers.add(level.findMob(heartPos+i+j));
				}
			}
			if (!foundLasher){
				safeHeartcells++;
			}
		}

		if (safeHeartcells < 2 && !adjacentLashers.isEmpty()){
			Char toRemove = Random.element(adjacentLashers);
			level.mobs.remove(toRemove);
			Painter.set(level, toRemove.pos, Terrain.HIGH_GRASS);
		}

	}

	private static boolean validPlantPos(boolean[] passable, boolean[] newPassable, Level level, int pos, int heartPos, int entryPos){
		if (level.map[pos] != Terrain.HIGH_GRASS){
			return false;
		}

		for (int i : PathFinder.NEIGHBOURS9){
			if (level.findMob(pos+i) != null){
				return false;
			}
		}

		newPassable[pos] = false;
		for (int i : PathFinder.NEIGHBOURS4){
			newPassable[pos+i] = false;
		}

		PathFinder.buildDistanceMap(heartPos, newPassable);

		if (PathFinder.distance[entryPos] == Integer.MAX_VALUE){
			System.arraycopy(passable, 0, newPassable, 0, passable.length);
			return false;
		} else {
			System.arraycopy(newPassable, 0, passable, 0, passable.length);
			return true;
		}
	}

	private static void placePlant(Level level, int pos, Mob plant){
		plant.pos = pos;
		level.mobs.add( plant );

		Painter.set(level, pos, Terrain.GRASS);
	}
}
