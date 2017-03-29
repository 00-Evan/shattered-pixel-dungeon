package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class SpecialRoom extends Room {
	
	@Override
	public int minDimension() {
		return 5;
	}
	
	@Override
	public int maxDimension() {
		return 10;
	}
	
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
