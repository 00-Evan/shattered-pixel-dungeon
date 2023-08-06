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

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTileSheet;
import com.watabou.utils.Graph;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class MiningLevelPainter extends CavesPainter {

	//TODO currently this does an acceptable job of generating veins/clusters of gold
	// and an excellent job of ensuring that the total gold amount is consistent
	// but it doesn't ensure anything about gold distribution. Gold can often be overly clumped in certain areas

	//TODO gold on left/right/below walls is also super hard to see atm

	private int goldToAdd = 0;

	public RegularPainter setGold(int amount){
		goldToAdd = amount;
		return this;
	}

	@Override
	protected void generateGold(Level level, ArrayList<Room> rooms) {
		//we start by counting all the gold purposefully made by rooms
		for (int i = 0; i < level.length(); i++){
			if (level.map[i] == Terrain.WALL_DECO) {
				goldToAdd--;
			}
		}

		int[] map = level.map;

		do {
			//use the patch system to generate a random smattering of gold with some grouping
			boolean[] gold = Patch.generate( level.width(), level.height(), 0.05f, 2, true );
			ArrayList<Integer> goldPosCandidates = new ArrayList<>();

			//find all potential gold cells that are exposed
			for (int i = 0; i < level.length(); i++){
				if (level.insideMap(i) && goldToAdd > 0 && map[i] == Terrain.WALL && gold[i]){

					for (int j : PathFinder.NEIGHBOURS4){
						if (level.insideMap(i+j) && DungeonTileSheet.floorTile(map[i+j])){
							goldPosCandidates.add(i);
							break;
						}
					}
				}
			}

			Random.shuffle(goldPosCandidates);

			//fill in only the exposed potential gold cells...
			for (int i : goldPosCandidates){
				if (map[i] == Terrain.WALL) {
					map[i] = Terrain.WALL_DECO;
					goldToAdd--;
				}

				//...with some preference to fill in neighbours
				for (int k : PathFinder.NEIGHBOURS4){
					if (!level.insideMap(i+k)) continue;
					if (goldToAdd > 0 && goldPosCandidates.contains(i+k) && map[i+k] == Terrain.WALL){
						map[i+k] = Terrain.WALL_DECO;
						goldToAdd--;
					}
				};

				if (goldToAdd <= 0) break;

			}

			//if we have more gold to add, also look into filling in some non-exposed potential gold cells
			// that are adjacent to exposed cells
			if (goldToAdd > 0){
				for (int i : goldPosCandidates){
					for (int k : PathFinder.NEIGHBOURS4){
						if (!level.insideMap(i+k)) continue;
						if (goldToAdd > 0 && gold[i+k] && map[i+k] == Terrain.WALL){
							map[i+k] = Terrain.WALL_DECO;
							goldToAdd--;
						}
					};
					if (goldToAdd <= 0) break;
				}
			}

		//if we don't have enough gold yet, loop
		} while (goldToAdd > 0);

	}

	/*
	//spreads gold over an entire cluster, currently unused
	private void spreadGold(int i, boolean[] gold){
		for (int k : PathFinder.NEIGHBOURS4){
			if (!insideMap(i+k)) continue;
			if (goldToAdd > 0 && gold[i+k] && map[i+k] == Terrain.WALL){
				map[i+k] = Terrain.BARRICADE;
				goldToAdd--;
				spreadGold(i+k, gold);
			}
		}
	}
	*/

	@Override
	protected void paintDoors(Level l, ArrayList<Room> rooms) {
		HashMap<Room, Room> roomMerges = new HashMap<>();

		float hiddenDoorChance = 0.90f;

		//hidden doors become wall tiles
		//(maybe sometimes become gold ore?)
		//everything else becomes empty
		for (Room r : rooms) {
			for (Room n : r.connected.keySet()) {

				Room.Door d = r.connected.get(n);
				int door = d.x + d.y * l.width();

				if (d.type == Room.Door.Type.HIDDEN){
					l.map[door] = Terrain.WALL;
				} else {
					//some of these are randomly hidden, using the same rules as regular levels
					if (Random.Float() < hiddenDoorChance) {
						d.type = Room.Door.Type.HIDDEN;
						Graph.buildDistanceMap(rooms, r);
						if (n.distance == Integer.MAX_VALUE){
							l.map[door] = Terrain.EMPTY;
							d.type = Room.Door.Type.EMPTY;
						} else {
							l.map[door] = Terrain.WALL;
						}
					} else {
						l.map[door] = Terrain.EMPTY;
						d.type = Room.Door.Type.EMPTY;
					}

				}

				//if the door is empty, always merge the rooms
				if (l.map[door] == Terrain.EMPTY){
					if (roomMerges.get(r) == n || roomMerges.get(n) == r){
						continue;
					} else if (mergeRooms(l, r, n, r.connected.get(n), Terrain.EMPTY)) {
						roomMerges.put(r, n);
						roomMerges.put(n, r);
					}
				}

			}
		}
	}

	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {
		super.decorate(level, rooms);

		//no chasms allowed, replace with ground!
		for (int i = 0; i < level.length(); i++){
			if (level.map[i] == Terrain.CHASM){
				level.map[i] = Terrain.EMPTY;
			}
		}
	}
}
