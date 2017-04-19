/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.watabou.utils.Bundle;

//shops probably shouldn't extend special room, because of cases like this.
public class ImpShopRoom extends ShopRoom {

	private boolean impSpawned = false;

	//force a certain size here to guarantee enough room for 48 items, and the same center space
	@Override
	public int minWidth() {
		return 9;
	}
	public int minHeight() {
		return 9;
	}
	public int maxWidth() { return 9; }
	public int maxHeight() { return 9; }

	@Override
	public int maxConnections(int direction) {
		return 2;
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		Painter.fill( level, this, 3, Terrain.WATER);

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		if (Imp.Quest.isCompleted()){
			impSpawned = true;
			placeItems(level);
			placeShopkeeper(level);
		} else {
			impSpawned = false;
		}

	}

	@Override
	protected void placeShopkeeper(Level level) {

		int pos = level.pointToCell(center());

		Mob shopkeeper = new ImpShopkeeper();
		shopkeeper.pos = pos;
		level.mobs.add( shopkeeper );

	}

	//fix for connections not being bundled normally
	@Override
	public Door entrance() {
		return connected.isEmpty() ? new Door(left, top+2) : super.entrance();
	}

	private void spawnShop(Level level){
		impSpawned = true;
		super.paint(level);
	}

	private static final String IMP = "imp_spawned";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(IMP, impSpawned);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		impSpawned = bundle.getBoolean(IMP);
	}

	@Override
	public void onLevelLoad(Level level) {
		super.onLevelLoad(level);

		if (Imp.Quest.isCompleted() && !impSpawned){
			impSpawned = true;
			placeItems(level);
			placeShopkeeper(level);
		}
	}
}
