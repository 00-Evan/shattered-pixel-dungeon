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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.treasure;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.quest.vault.VaultRoom;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class VaultTreasureRoom extends VaultRoom {

	@Override
	public int maxConnections(int direction) {
		return 1;
	}

	private Door entrance;

	public Door entrance() {
		if (entrance == null){
			if (connected.isEmpty()){
				return null;
			} else {
				entrance = connected.values().iterator().next();
			}
		}
		return entrance;
	}

	@Override
	public boolean canPlaceItem(Point p, Level l) {
		return false;
	}

	@Override
	public boolean canPlaceCharacter(Point p, Level l) {
		return false;
	}

	private static final ArrayList<Class<? extends VaultTreasureRoom>> T1_ROOMS = new ArrayList<>( Arrays.asList(
			VaultFlamePathRoom.class, VaultLaserTreasureRoom.class, VaultCircleScanTreasureRoom.class
	));

	private static final ArrayList<Class<? extends VaultTreasureRoom>> T2_ROOMS = new ArrayList<>( Arrays.asList(
			VaultSingleEnemyTreasureRoom.class, VaultBookcaseTreasureRoom.class, VaultFlamesTreasureRoom.class
	));

	private static final ArrayList<Class<? extends VaultTreasureRoom>> T3_ROOMS = new ArrayList<>( Arrays.asList(
			VaultManyScansRoom.class, VaultMultipleEnemyTreasureRoom.class, VaultHardLaserTreasureRoom.class
	));

	public static ArrayList<Class<? extends VaultTreasureRoom>> treasuresToSpawn = new ArrayList<>();

	//no need to persist this over time like specials, so we just generate once
	public static void generateRoomList() {
		ArrayList<Class<? extends VaultTreasureRoom>> T1s = new ArrayList<>(T1_ROOMS);
		Random.shuffle(T1s);
		ArrayList<Class<? extends VaultTreasureRoom>> T2s = new ArrayList<>(T2_ROOMS);
		Random.shuffle(T2s);
		ArrayList<Class<? extends VaultTreasureRoom>> T3s = new ArrayList<>(T3_ROOMS);
		Random.shuffle(T3s);

		ArrayList<ArrayList<Class<? extends VaultTreasureRoom>>> fullList = new ArrayList<>();
		//always generate in order of T1, T2, T3
		fullList.add(T1s);
		fullList.add(T2s);
		fullList.add(T3s);

		treasuresToSpawn = new ArrayList<>();
		while (!fullList.isEmpty()){
			ArrayList<Class<? extends VaultTreasureRoom>> current = fullList.remove(0);
			treasuresToSpawn.add(current.remove(0));
			if (!current.isEmpty()){
				fullList.add(current);
			}
		}
	}

	public static VaultTreasureRoom nextRoom(){
		return Reflection.newInstance(treasuresToSpawn.remove(0));
	}

}
