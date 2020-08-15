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

package com.watabou.utils;

import com.badlogic.gdx.utils.IntMap;

import java.util.Arrays;
import java.util.List;

public class SparseArray<T> extends IntMap<T> {
	
	@Override
	public synchronized T put(int key, T value) {
		return super.put(key, value);
	}
	
	@Override
	public synchronized T get(int key, T defaultValue) {
		return super.get(key, defaultValue);
	}
	
	@Override
	public synchronized T remove(int key) {
		return super.remove(key);
	}
	
	public synchronized int[] keyArray() {
		return keys().toArray().toArray();
	}
	
	public synchronized List<T> valueList() {
		return Arrays.asList(values().toArray().toArray());
	}
}
