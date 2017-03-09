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

package com.shatteredpixel.shatteredpixeldungeon.levels.rooms;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

public class Room extends Rect implements Graph.Node, Bundlable {
	
	public ArrayList<Room> neigbours = new ArrayList<Room>();
	public LinkedHashMap<Room, Door> connected = new LinkedHashMap<Room, Door>();
	
	public int distance;
	public int price = 1;
	
	public Room(){
		super();
	}
	
	public Room(Rect other){
		super(other);
	}
	
	public Room( int left, int top, int right, int bottom ) {
		super( left, top, right, bottom );
	}
	
	//TODO convert these types into full subclasses of room
	public static enum Type {
		NULL( null ),
		STANDARD	( StandardRoom.class ),
		ENTRANCE	( EntranceRoom.class ),
		EXIT		( ExitRoom.class ),
		TUNNEL		( TunnelRoom.class ),
		PASSAGE		( PassageRoom.class ),
		SHOP		( ShopRoom.class ),
		BLACKSMITH	( BlacksmithRoom.class ),
		TREASURY	( TreasuryRoom.class ),
		ARMORY		( ArmoryRoom.class ),
		LIBRARY		( LibraryRoom.class ),
		LABORATORY	( LaboratoryRoom.class ),
		VAULT		( VaultRoom.class ),
		TRAPS		( TrapsRoom.class ),
		STORAGE		( StorageRoom.class ),
		MAGIC_WELL	( MagicWellRoom.class ),
		GARDEN		( GardenRoom.class ),
		CRYPT		( CryptRoom.class ),
		STATUE		( StatueRoom.class ),
		POOL		( PoolRoom.class ),
		RAT_KING	( RatKingRoom.class ),
		WEAK_FLOOR	( WeakFloorRoom.class ),
		PIT			( PitRoom.class ),

		//prison quests
		MASS_GRAVE  ( MassGraveRoom.class ),
		ROT_GARDEN  ( RotGardenRoom.class ),
		RITUAL_SITE ( RitualSiteRoom.class );
		
		private Method paint;
		
		Type( Class<? extends Room> painter ) {
			if (painter == null)
				paint = null;
			else
				try {
					paint = painter.getMethod( "paint", Level.class, Room.class );
				} catch (Exception e) {
					ShatteredPixelDungeon.reportException(e);
					paint = null;
				}
		}
		
		public void paint( Level level, Room room ) {
			try {
				paint.invoke( null, level, room );
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
	};

	private static final ArrayList<Type> ALL_SPEC = new ArrayList<Type>( Arrays.asList(
		Type.WEAK_FLOOR, Type.MAGIC_WELL, Type.CRYPT, Type.POOL, Type.GARDEN, Type.LIBRARY, Type.ARMORY,
		Type.TREASURY, Type.TRAPS, Type.STORAGE, Type.STATUE, Type.LABORATORY, Type.VAULT
	) );
	
	public static ArrayList<Type> SPECIALS = new ArrayList<Type>( Arrays.asList(
		Type.WEAK_FLOOR, Type.MAGIC_WELL, Type.CRYPT, Type.POOL, Type.GARDEN, Type.LIBRARY, Type.ARMORY,
		Type.TREASURY, Type.TRAPS, Type.STORAGE, Type.STATUE, Type.LABORATORY, Type.VAULT
	) );
	
	public Type type = Type.NULL;
	
	public Point random() {
		return random( 0 );
	}
	
	public Point random( int m ) {
		return new Point( Random.Int( left + 1 + m, right - m ),
				Random.Int( top + 1 + m, bottom - m ));
	}
	
	public void addNeigbour( Room other ) {
		
		Rect i = intersect( other );
		if ((i.width() == 0 && i.height() >= 3) ||
			(i.height() == 0 && i.width() >= 3)) {
			neigbours.add( other );
			other.neigbours.add( this );
		}
		
	}
	
	public void connect( Room room ) {
		if (!connected.containsKey( room )) {
			connected.put( room, null );
			room.connected.put( this, null );
		}
	}
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
	
	public boolean inside( Point p ) {
		return p.x > left && p.y > top && p.x < right && p.y < bottom;
	}
	
	public Point center() {
		return new Point(
			(left + right) / 2 + (((right - left) & 1) == 1 ? Random.Int( 2 ) : 0),
			(top + bottom) / 2 + (((bottom - top) & 1) == 1 ? Random.Int( 2 ) : 0) );
	}
	
	// **** Graph.Node interface ****

	@Override
	public int distance() {
		return distance;
	}

	@Override
	public void distance( int value ) {
		distance = value;
	}
	
	@Override
	public int price() {
		return price;
	}

	@Override
	public void price( int value ) {
		price = value;
	}

	@Override
	public Collection<Room> edges() {
		return neigbours;
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( "left", left );
		bundle.put( "top", top );
		bundle.put( "right", right );
		bundle.put( "bottom", bottom );
		bundle.put( "type", type.toString() );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		left = bundle.getInt( "left" );
		top = bundle.getInt( "top" );
		right = bundle.getInt( "right" );
		bottom = bundle.getInt( "bottom" );
		type = Type.valueOf( bundle.getString( "type" ) );
	}
	
	public static void shuffleTypes() {
		SPECIALS = (ArrayList<Type>)ALL_SPEC.clone();
		int size = SPECIALS.size();
		for (int i=0; i < size - 1; i++) {
			int j = Random.Int( i, size );
			if (j != i) {
				Type t = SPECIALS.get( i );
				SPECIALS.set( i, SPECIALS.get( j ) );
				SPECIALS.set( j, t );
			}
		}
	}
	
	public static void useType( Type type ) {
		if (SPECIALS.remove( type )) {
			SPECIALS.add( type );
		}
	}
	
	private static final String ROOMS	= "rooms";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( ROOMS )) {
			SPECIALS.clear();
			for (String type : bundle.getStringArray( ROOMS )) {
				SPECIALS.add( Type.valueOf( type ));
			}
		} else {
			shuffleTypes();
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		String[] array = new String[SPECIALS.size()];
		for (int i=0; i < array.length; i++) {
			array[i] = SPECIALS.get( i ).toString();
		}
		bundle.put( ROOMS, array );
	}
	
	public static class Door extends Point {
		
		public static enum Type {
			EMPTY, TUNNEL, REGULAR, UNLOCKED, HIDDEN, BARRICADE, LOCKED
		}
		public Type type = Type.EMPTY;
		
		public Door( int x, int y ) {
			super( x, y );
		}
		
		public void set( Type type ) {
			if (type.compareTo( this.type ) > 0) {
				this.type = type;
			}
		}
	}
}