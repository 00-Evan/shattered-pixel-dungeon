/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.utils;

public class BArray {

	private static boolean[] falseArray;

	//This is MUCH faster than making a new boolean[] or using Arrays.fill;
	public static void setFalse( boolean[] toBeFalse ){
		if (falseArray == null || falseArray.length < toBeFalse.length)
			falseArray = new boolean[toBeFalse.length];

		System.arraycopy(falseArray, 0, toBeFalse, 0,  toBeFalse.length);
	}

	public static boolean[] and( boolean[] a, boolean[] b, boolean[] result ) {
		
		int length = a.length;
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=0; i < length; i++) {
			result[i] = a[i] && b[i];
		}
		
		return result;
	}

	public static boolean[] or( boolean[] a, boolean[] b, boolean[] result ){
		return or( a, b, 0, a.length, result);
	}
	
	public static boolean[] or( boolean[] a, boolean[] b, int offset, int length, boolean[] result ) {
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=offset; i < offset+length; i++) {
			result[i] = a[i] || b[i];
		}
		
		return result;
	}
	
	public static boolean[] not( boolean[] a, boolean[] result ) {
		
		int length = a.length;
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=0; i < length; i++) {
			result[i] = !a[i];
		}
		
		return result;
	}
	
	public static boolean[] is( int[] a, boolean[] result, int v1 ) {
		
		int length = a.length;
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=0; i < length; i++) {
			result[i] = a[i] == v1;
		}
		
		return result;
	}
	
	public static boolean[] isOneOf( int[] a, boolean[] result, int... v ) {
		
		int length = a.length;
		int nv = v.length;
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=0; i < length; i++) {
			result[i] = false;
			for (int j=0; j < nv; j++) {
				if (a[i] == v[j]) {
					result[i] = true;
					break;
				}
			}
		}
		
		return result;
	}
	
	public static boolean[] isNot( int[] a, boolean[] result, int v1 ) {
		
		int length = a.length;
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=0; i < length; i++) {
			result[i] = a[i] != v1;
		}
		
		return result;
	}
	
	public static boolean[] isNotOneOf( int[] a, boolean[] result, int... v ) {
		
		int length = a.length;
		int nv = v.length;
		
		if (result == null) {
			result = new boolean[length];
		}
		
		for (int i=0; i < length; i++) {
			result[i] = true;
			for (int j=0; j < nv; j++) {
				if (a[i] == v[j]) {
					result[i] = false;
					break;
				}
			}
		}
		
		return result;
	}
}
