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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EmptyRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.RegionDecoLineRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.SegmentedRoom;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;

public class VaultLevel extends Level { //for now

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}

	@Override
	public void playLevelMusic() {
		Music.INSTANCE.playTracks(CityLevel.CITY_TRACK_LIST, CityLevel.CITY_TRACK_CHANCES, false);
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_CITY;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_CITY;
	}

	@Override
	protected boolean build() {
		setSize(34, 34);

		ArrayList<Room> rooms = new ArrayList<>();

		Room finalRoom = null;
		Room entryRoom = null;

		for (int x = 0; x < 4; x++){
			for (int y = 0; y < 4; y++){

				if (x == 3 && y <= 1){
					if (y == 1) {
						continue;
					} else {
						Room r = new RegionDecoLineRoom();
						r.set(1+8*x, 1+8*y, 9+8*x, 17);
						rooms.add(r);
						finalRoom = r;
						continue;
					}
				}

				if (x == 0 && y == 3){
					Room r = new EmptyRoom();
					r.set(1+8*x, 1+8*y, 9+8*x, 9+8*y);
					rooms.add(r);
					entryRoom = r;
				} else {
					Room r = new SegmentedRoom();
					r.set(1+8*x, 1+8*y, 9+8*x, 9+8*y);
					rooms.add(r);
				}
			}
		}

		//builder.findneighbnours
		Room[] ra = rooms.toArray( new Room[0] );
		for (int i=0; i < ra.length-1; i++) {
			for (int j=i+1; j < ra.length; j++) {
				ra[i].addNeigbour( ra[j] );
			}
		}

		for (Room n : rooms){
			for (Room p : n.neigbours){
				if (p.height() > 10){
					continue;
				}
				if (n.height() > 10){
					if (n.canConnect(p)){
						if (n.bottom == p.top){
							n.connect(p);
						}
					}
				} else if (n.canConnect(p)) {
					n.connect(p);
				}
			}
		}

		//Painter.placedoors
		for (Room r : rooms){
			for (Room n : r.connected.keySet()) {
				Room.Door door = r.connected.get( n );
				if (door == null) {

					Rect i = r.intersect( n );
					ArrayList<Point> doorSpots = new ArrayList<>();
					for (Point p : i.getPoints()){
						if (r.canConnect(p) && n.canConnect(p))
							doorSpots.add(p);
					}
					if (doorSpots.isEmpty()){
						ShatteredPixelDungeon.reportException(
								new RuntimeException("Could not place a door! " +
										"r=" + r.getClass().getSimpleName() +
										" n=" + n.getClass().getSimpleName()));
						continue;
					}
					door = new Room.Door(Random.element(doorSpots));

					r.connected.put( n, door );
					n.connected.put( r, door );
				}
			}
		}

		for (Room n : rooms){
			n.paint(this);
			if (n instanceof RegionDecoLineRoom){
				Painter.fill(this, n, 1, Terrain.EMPTY_SP);
				Painter.fill(this, n.left+1, n.top+1, 7, 1, Terrain.REGION_DECO_ALT);
				Painter.fill(this, n.left+1, n.top+1, 1, 14, Terrain.REGION_DECO_ALT);
				Painter.fill(this, n.right-1, n.top+1, 1, 14, Terrain.REGION_DECO_ALT);
			}
			for (Point door : n.connected.values()){
				Painter.set(this, door, Terrain.DOOR);
			}
		}

		entrance = pointToCell(entryRoom.random());
		transitions.add(new LevelTransition(this,
				entrance,
				LevelTransition.Type.BRANCH_ENTRANCE,
				Dungeon.depth,
				0,
				LevelTransition.Type.BRANCH_EXIT));

		rooms.remove(entryRoom);
		rooms.remove(finalRoom);

		for (Room n : rooms){
			if (Random.Int(5) != 0){
				Item item = Generator.randomUsingDefaults(Random.oneOf(
						Generator.Category.WEAPON, Generator.Category.WEAPON,
						Generator.Category.ARMOR,
						Generator.Category.WAND,
						Generator.Category.RING));
				int pos;
				do {
					pos = pointToCell(n.random());
				} while (map[pos] != Terrain.EMPTY);
				if (item.cursed){
					item.cursed = false;
					if (item instanceof MeleeWeapon && ((MeleeWeapon) item).hasCurseEnchant()){
						((MeleeWeapon) item).enchant(null);
					} else if (item instanceof Armor && ((Armor) item).hasCurseGlyph()){
						((Armor) item).inscribe(null);
					}
				}
				item.identify();
				drop(item, pos);
			}
		}

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
		Random.pushGenerator(Random.Long());
		ArrayList<Item> bonesItems = Bones.get();
		if (bonesItems != null) {
			for (Item i : bonesItems) {
				drop(i, entrance()-width()).setHauntedIfCursed().type = Heap.Type.REMAINS;
			}
		}
		Random.popGenerator();
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		return entrance()-width();
	}
}
