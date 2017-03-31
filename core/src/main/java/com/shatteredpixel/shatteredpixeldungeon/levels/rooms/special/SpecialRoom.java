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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class SpecialRoom extends Room {
	
	@Override
	public int minWidth() { return 5; }
	public int maxWidth() { return 10; }
	
	@Override
	public int minHeight() {
		return 5;
	}
	public int maxHeight() { return 10; }
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
	
	private static final ArrayList<Class<? extends SpecialRoom>> ALL_SPEC = new ArrayList<>( Arrays.asList(
			WeakFloorRoom.class, MagicWellRoom.class, CryptRoom.class, PoolRoom.class, GardenRoom.class, LibraryRoom.class, ArmoryRoom.class,
			TreasuryRoom.class, TrapsRoom.class, StorageRoom.class, StatueRoom.class, LaboratoryRoom.class, VaultRoom.class
	) );
	
	public static ArrayList<Class<? extends Room>> SPECIALS = new ArrayList<>();
	
	public static void shuffleTypes() {
		SPECIALS = (ArrayList<Class<?extends Room>>)ALL_SPEC.clone();
		int size = SPECIALS.size();
		for (int i=0; i < size - 1; i++) {
			int j = Random.Int( i, size );
			if (j != i) {
				Class<?extends Room> c = SPECIALS.get( i );
				SPECIALS.set( i, SPECIALS.get( j ) );
				SPECIALS.set( j, c );
			}
		}
	}
	
	public static void useType( Class<?extends Room> type ) {
		if (SPECIALS.remove( type )) {
			SPECIALS.add( type );
		}
	}
	
	private static final String ROOMS	= "special_rooms";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( ROOMS )) {
			SPECIALS.clear();
			for (Class<?extends Room> type : bundle.getClassArray( ROOMS )) {
				SPECIALS.add( type );
			}
		} else {
			shuffleTypes();
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		bundle.put( ROOMS, SPECIALS.toArray(new Class[0]) );
	}
}
