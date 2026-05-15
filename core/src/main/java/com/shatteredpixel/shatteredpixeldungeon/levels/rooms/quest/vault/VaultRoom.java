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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class VaultRoom extends StandardRoom {

	//always 'large', with a static size of 9x9 internally

	@Override
	public float[] sizeCatProbs() {
		return new float[]{0, 1, 0};
	}

	@Override
	public int minHeight() { return 11; }
	public int maxHeight() { return 11; }

	@Override
	public int minWidth() { return 11; }
	public int maxWidth() { return 11; }

	// no merging by default, maybe allow this in some specific cases?
	@Override
	public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
		return false;
	}

	@Override
	public int sizeFactor() {
		return 1;
	}

	@Override
	public boolean canConnect(Room r) {
		//two rooms of the same kind can't be adjacent
		return r.getClass() != getClass() && super.canConnect(r);
	}

	private static ArrayList<Class<?extends VaultRoom>> rooms = new ArrayList<>();
	static {
		rooms.add(VaultRingRoom.class);
		rooms.add(VaultCircleRoom.class);
		rooms.add(VaultCrossRoom.class);
		rooms.add(VaultQuadrantsRoom.class);
		rooms.add(VaultRingsRoom.class);
		rooms.add(VaultEnemyCenterRoom.class);

		rooms.add(VaultLongRoom.class);
		rooms.add(VaultAlternatingFireRoom.class);
		rooms.add(VaultLasersRoom.class);
		rooms.add(VaultSimpleEnemyTreasureRoom.class);
	}

	private static float[] chances = new float[0];

	public static void setupChances(){
		chances = new float[]{3,3,3,3,3,3, 2,2,2,2};
	}

	public static VaultRoom createRoom(){
		int idx = Random.chances(chances);
		if (idx == -1){
			setupChances();
			idx = Random.chances(chances);
		}
		chances[idx]--;
		return Reflection.newInstance(rooms.get(idx));
	}

}
