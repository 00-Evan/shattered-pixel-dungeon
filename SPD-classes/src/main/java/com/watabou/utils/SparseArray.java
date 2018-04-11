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

import java.util.ArrayList;
import java.util.List;

public class SparseArray<T> extends android.util.SparseArray<T> {

	public int[] keyArray() {
		int size = size();
		int[] array = new int[size];
		for (int i=0; i < size; i++) {
			array[i] = keyAt( i );
		}
		return array;
	}
	
	public List<T> values() {
		int size = size();
		ArrayList<T> list = new ArrayList<T>( size );
		for (int i=0; i < size; i++) {
			list.add( i, valueAt( i ) );
		}
		return list;
	}
}
