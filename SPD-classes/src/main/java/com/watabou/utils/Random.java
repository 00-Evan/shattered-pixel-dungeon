/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.watabou.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Random {

	private static java.util.Random rand = new java.util.Random();

	public static void seed( ){
		rand = new java.util.Random();
	}

	public static void seed( long seed ){
		rand.setSeed(seed);
	}

	//returns a uniformly distributed float in the range [0, 1)
	public static float Float() {
		return rand.nextFloat();
	}

	//returns a uniformly distributed float in the range [0, max)
	public static float Float( float max ) {
		return Float() * max;
	}

	//returns a uniformly distributed float in the range [min, max)
	public static float Float( float min, float max ) {
		return min + Float(max - min);
	}

	//returns a uniformly distributed int in the range [0, max)
	public static int Int( int max ) {
		return max > 0 ? rand.nextInt(max) : 0;
	}

	//returns a uniformly distributed int in the range [min, max)
	public static int Int( int min, int max ) {
		return min + Int(max - min);
	}

	//returns a uniformly distributed int in the range [min, max]
	public static int IntRange( int min, int max ) {
		return min + Int(max - min + 1);
	}

	//returns a triangularly distributed int in the range [min, max]
	public static int NormalIntRange( int min, int max ) {
		return min + (int)((Float() + Float()) * (max - min + 1) / 2f);
	}

	//returns a uniformly distributed long in the range [-2^63, 2^63)
	public static long Long() {
		return rand.nextLong();
	}

	//returns a uniformly distributed long in the range [0, max)
	public static long Long( long max ) {
		long result = Long();
		if (result < 0) result += Long.MAX_VALUE;
		return result % max;
	}

	//returns an index from chances, the probability of each index is the weight values in changes
	public static int chances( float[] chances ) {
		
		int length = chances.length;
		
		float sum = 0;
		for (int i=0; i < length; i++) {
			sum += chances[i];
		}
		
		float value = Float( sum );
		sum = 0;
		for (int i=0; i < length; i++) {
			sum += chances[i];
			if (value < sum) {
				return i;
			}
		}
		
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	//returns a key element from chances, the probability of each key is the weight value it maps to
	public static <K> K chances( HashMap<K,Float> chances ) {
		
		int size = chances.size();

		Object[] values = chances.keySet().toArray();
		float[] probs = new float[size];
		float sum = 0;
		for (int i=0; i < size; i++) {
			probs[i] = chances.get( values[i] );
			sum += probs[i];
		}
		
		if (sum <= 0) {
			return null;
		}
		
		float value = Float( sum );
		
		sum = probs[0];
		for (int i=0; i < size; i++) {
			if (value < sum) {
				return (K)values[i];
			}
			sum += probs[i + 1];
		}
		
		return null;
	}
	
	public static int index( Collection<?> collection ) {
		return Int(collection.size());
	}

	@SafeVarargs
	public static<T> T oneOf(T... array ) {
		return array[Int(array.length)];
	}
	
	public static<T> T element( T[] array ) {
		return element( array, array.length );
	}
	
	public static<T> T element( T[] array, int max ) {
		return array[Int(max)];
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T element( Collection<? extends T> collection ) {
		int size = collection.size();
		return size > 0 ?
			(T)collection.toArray()[Int( size )] :
			null;
	}

	public static<T> void shuffle( List<?extends T> list){
		Collections.shuffle(list, rand);
	}
	
	public static<T> void shuffle( T[] array ) {
		for (int i=0; i < array.length - 1; i++) {
			int j = Int( i, array.length );
			if (j != i) {
				T t = array[i];
				array[i] = array[j];
				array[j] = t;
			}
		}
	}
	
	public static<U,V> void shuffle( U[] u, V[]v ) {
		for (int i=0; i < u.length - 1; i++) {
			int j = Int( i, u.length );
			if (j != i) {
				U ut = u[i];
				u[i] = u[j];
				u[j] = ut;
				
				V vt = v[i];
				v[i] = v[j];
				v[j] = vt;
			}
		}
	}
}
