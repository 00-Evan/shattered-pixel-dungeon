/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class SpecialRoom extends Room {
	
	@Override
	public int minWidth() { return 5; }
	public int maxWidth() { return 10; }
	
	@Override
	public int minHeight() {
		return 5;
	}
	public int maxHeight() { return 10; }
	
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
	
	private static final String ENTRANCE = "entrance";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (entrance() != null){
			bundle.put(ENTRANCE, entrance());
		}
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(ENTRANCE)){
			entrance = (Door)bundle.get(ENTRANCE);
		}
	}

	//special rooms which give an equipment reward
	private static final ArrayList<Class<? extends SpecialRoom>> EQUIP_SPECIALS = new ArrayList<>( Arrays.asList(
			WeakFloorRoom.class, CryptRoom.class, PoolRoom.class, ArmoryRoom.class, TrapsRoom.class, StatueRoom.class, VaultRoom.class
	));

	//special rooms which give a consumable reward
	//note that alchemy rooms are spawned separately
	private static final ArrayList<Class<? extends SpecialRoom>> CONSUMABLES_SPECIALS = new ArrayList<>( Arrays.asList(
			RunestoneRoom.class, GardenRoom.class, LibraryRoom.class, StorageRoom.class, TreasuryRoom.class, MagicWellRoom.class
	) );

	public static ArrayList<Class<? extends Room>> runSpecials = new ArrayList<>();
	public static ArrayList<Class<? extends Room>> floorSpecials = new ArrayList<>();
	
	private static int pitNeededDepth = -1;
	
	public static void initForRun() {
		runSpecials = new ArrayList<>();

		ArrayList<Class<?extends Room>> runEquipSpecials = (ArrayList<Class<?extends Room>>)EQUIP_SPECIALS.clone();
		ArrayList<Class<?extends Room>> runConsSpecials = (ArrayList<Class<?extends Room>>)CONSUMABLES_SPECIALS.clone();

		Random.shuffle(runEquipSpecials);
		Random.shuffle(runConsSpecials);

		// TODO currently always an equip special first as there's 1 more of them, adjust as needed if adding more
		/*if (Random.Int(2) == 0){
			runSpecials.add(runConsSpecials.remove(0));
		}*/

		while (!runEquipSpecials.isEmpty() || !runConsSpecials.isEmpty()){
			if (!runEquipSpecials.isEmpty())    runSpecials.add(runEquipSpecials.remove(0));
			if (!runConsSpecials.isEmpty())     runSpecials.add(runConsSpecials.remove(0));
		}

		pitNeededDepth = -1;
	}
	
	public static void initForFloor(){
		floorSpecials = (ArrayList<Class<?extends Room>>) runSpecials.clone();
		
		//laboratory rooms spawn at set intervals every chapter
		if (Dungeon.depth%5 == (Dungeon.seed%3 + 2)){
			floorSpecials.add(0, LaboratoryRoom.class);
		}
	}
	
	private static void useType( Class<?extends Room> type ) {
		floorSpecials.remove( type );
		if (runSpecials.remove( type )) {
			runSpecials.add( type );
		}
	}

	public static void resetPitRoom(int depth){
		if (pitNeededDepth == depth) pitNeededDepth = -1;
	}
	
	public static SpecialRoom createRoom(){
		if (Dungeon.depth == pitNeededDepth){
			pitNeededDepth = -1;
			
			floorSpecials.remove( ArmoryRoom.class );
			floorSpecials.remove( CryptRoom.class );
			floorSpecials.remove( LibraryRoom.class );
			floorSpecials.remove( RunestoneRoom.class );
			floorSpecials.remove( StatueRoom.class );
			floorSpecials.remove( TreasuryRoom.class );
			floorSpecials.remove( VaultRoom.class );
			floorSpecials.remove( WeakFloorRoom.class );
			
			return new PitRoom();
			
		} else if (floorSpecials.contains(LaboratoryRoom.class)) {
		
			useType(LaboratoryRoom.class);
			return new LaboratoryRoom();
		
		} else {
			
			if (Dungeon.bossLevel(Dungeon.depth + 1)){
				floorSpecials.remove(WeakFloorRoom.class);
			}

			//60% chance for front of queue, 30% chance for next, 10% for one after that
			int index = Random.chances(new float[]{6, 3, 1});
			while (index >= floorSpecials.size()) index--;

			Room r = Reflection.newInstance(floorSpecials.get( index ));

			if (r instanceof WeakFloorRoom){
				pitNeededDepth = Dungeon.depth + 1;
			}
			
			useType( r.getClass() );
			return (SpecialRoom)r;
		
		}
	}
	
	private static final String ROOMS	= "special_rooms";
	private static final String PIT	    = "pit_needed";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		runSpecials.clear();
		if (bundle.contains( ROOMS )) {
			for (Class<? extends Room> type : bundle.getClassArray(ROOMS)) {
				runSpecials.add(type);
			}
		} else {
			initForRun();
			ShatteredPixelDungeon.reportException(new Exception("specials array didn't exist!"));
		}
		pitNeededDepth = bundle.getInt(PIT);
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		bundle.put( ROOMS, runSpecials.toArray(new Class[0]) );
		bundle.put( PIT, pitNeededDepth );
	}
}
