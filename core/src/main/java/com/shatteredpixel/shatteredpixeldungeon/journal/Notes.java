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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Notes {

	public enum Landmark {
		WELL_OF_HEALTH,
		WELL_OF_AWARENESS,
		WELL_OF_TRANSMUTATION,
		ALCHEMY,
		GARDEN,
		STATUE,
		
		GHOST,
		WANDMAKER,
		TROLL,
		IMP;

		public String desc() {
			return Messages.get(this, name());
		}
	};
	
	public static class Record implements Comparable<Record>, Bundlable {
		
		//compatibility with pre-0.6.1 saves
		private static final String FEATURE	= "feature";
		
		private static final String LANDMARK	= "landmark";
		
		private static final String DEPTH	= "depth";
		
		public Landmark landmark;
		
		public int depth;
		
		public Record() {
		}
		
		public Record(Landmark landmark, int depth ) {
			this.landmark = landmark;
			this.depth = depth;
		}

		@Override
		public int compareTo( Record another ) {
			return another.depth - depth;
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			if (bundle.contains(FEATURE)) {
				landmark = Landmark.valueOf(bundle.getString(FEATURE));
			} else {
				landmark = Landmark.valueOf(bundle.getString(LANDMARK));
			}
			depth = bundle.getInt( DEPTH );
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			bundle.put( LANDMARK, landmark.toString() );
			bundle.put( DEPTH, depth );
		}
	}
	
	public static ArrayList<Record> records;
	
	public static void reset() {
		records = new ArrayList<>();
	}
	
	private static final String JOURNAL	= "journal";
	
	public static void storeInBundle( Bundle bundle ) {
		bundle.put( JOURNAL, records );
	}
	
	public static void restoreFromBundle( Bundle bundle ) {
		records = new ArrayList<>();
		for (Bundlable rec : bundle.getCollection( JOURNAL ) ) {
			records.add( (Record) rec );
		}
	}
	
	public static void add( Landmark landmark ) {
		int size = records.size();
		for (int i=0; i < size; i++) {
			Record rec = records.get( i );
			if (rec.landmark == landmark && rec.depth == Dungeon.depth) {
				return;
			}
		}
		
		records.add( new Record(landmark, Dungeon.depth ) );
	}
	
	public static void remove( Landmark landmark ) {
		int size = records.size();
		for (int i=0; i < size; i++) {
			Record rec = records.get( i );
			if (rec.landmark == landmark && rec.depth == Dungeon.depth) {
				records.remove( i );
				return;
			}
		}
	}
}
