/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.TrinketCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.GridBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoLineRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.SegmentedRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class VaultLevel extends CityLevel {

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();

		initRooms.add(roomEntrance = new EntranceRoom(){
			@Override
			public int maxConnections(int direction) {
				if (direction == LEFT || direction == TOP) return 0;
				return super.maxConnections(direction);
			}

			@Override
			public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
				return false;
			}
		});

		for (int i = 0; i < 23; i++){
			initRooms.add(new SegmentedRoom(){
				@Override
				public float[] sizeCatProbs() {
					return new float[]{1, 0, 0};
				}
			});
		}

		initRooms.add(new RegionDecoLineRoom(){
			@Override
			public float[] sizeCatProbs() {
				return new float[]{0, 0, 1};
			}

			@Override
			public boolean isExit() {
				return true;
			}

			@Override
			public int maxConnections(int direction) {
				return 1;
			}

			@Override
			public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
				return false;
			}
		});

		return initRooms;
	}

	@Override
	protected Builder builder() {
		return new GridBuilder();
	}

	@Override
	protected int nTraps() {
		return 0;
	}

	@Override
	protected boolean build() {
		for (int i = 0; i < 20; i++){
			Item item = Generator.randomUsingDefaults(Random.oneOf(
					Generator.Category.WEAPON, Generator.Category.WEAPON, Generator.Category.WEAPON,
					Generator.Category.ARMOR,
					Generator.Category.WAND,
					Generator.Category.RING));
			if (item.cursed){
				item.cursed = false;
				if (item instanceof MeleeWeapon && ((MeleeWeapon) item).hasCurseEnchant()){
					((MeleeWeapon) item).enchant(null);
				} else if (item instanceof Armor && ((Armor) item).hasCurseGlyph()){
					((Armor) item).inscribe(null);
				}
			}
			//not true ID, prevents extra info about rings leaking to main game
			item.levelKnown = item.cursedKnown = true;
			addItemToSpawn(item);
		}

		if (!super.build()){
			return false;
		}

		Room finalRoom = room(RegionDecoLineRoom.class);
		for (Point p : finalRoom.getPoints()){
			int cell = pointToCell(p);
			if (map[cell] == Terrain.REGION_DECO){
				set(cell, Terrain.REGION_DECO_ALT, this);
			} else if (map[cell] == Terrain.EMPTY || map[cell] == Terrain.EMPTY_DECO || map[cell] == Terrain.WATER || map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.GRASS){
				set(cell, Terrain.EMPTY_SP, this);
			}
		}

		set(entrance(), Terrain.EMPTY, this);
		transitions.add(new LevelTransition(this,
				entrance(),
				LevelTransition.Type.BRANCH_ENTRANCE,
				Dungeon.depth,
				0,
				LevelTransition.Type.BRANCH_EXIT));

		return true;
	}

	@Override
	public boolean activateTransition(Hero hero, LevelTransition transition) {
		//walking onto transitions does nothing, need to use crystal
		return false;
	}

	@Override
	public Mob createMob() {
		return null;
	}

	@Override
	protected void createMobs() {
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		//copypasta from super.createItems
		for (Item item : itemsToSpawn) {
			int cell = randomDropCell();
			if (item instanceof TrinketCatalyst){
				drop( item, cell ).type = Heap.Type.LOCKED_CHEST;
				int keyCell = randomDropCell();
				drop( new GoldenKey(Dungeon.depth), keyCell ).type = Heap.Type.HEAP;
				if (map[keyCell] == Terrain.HIGH_GRASS || map[keyCell] == Terrain.FURROWED_GRASS) {
					map[keyCell] = Terrain.GRASS;
					losBlocking[keyCell] = false;
				}
			} else {
				drop( item, cell ).type = Heap.Type.HEAP;
			}
			if (map[cell] == Terrain.HIGH_GRASS || map[cell] == Terrain.FURROWED_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		return entrance()-width();
	}
}
