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

import android.content.SharedPreferences;
import android.os.Build;

import com.watabou.noosa.Game;

public class GameSettings {
	
	private static SharedPreferences prefs;
	
	private static SharedPreferences get() {
		if (prefs == null) {
			prefs = Game.instance.getPreferences( Game.MODE_PRIVATE );
		}
		return prefs;
	}
	
	public static boolean contains( String key ){
		return get().contains( key );
	}
	
	public static int getInt( String key, int defValue ) {
		return getInt(key, defValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static int getInt( String key, int defValue, int min, int max ) {
		try {
			int i = get().getInt( key, defValue );
			if (i < min || i > max){
				int val = (int)GameMath.gate(min, i, max);
				put(key, val);
				return val;
			} else {
				return i;
			}
		} catch (ClassCastException e) {
			//ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}
	
	public static boolean getBoolean( String key, boolean defValue ) {
		try {
			return get().getBoolean(key, defValue);
		} catch (ClassCastException e) {
			//ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}
	
	public static String getString( String key, String defValue ) {
		return getString(key, defValue, Integer.MAX_VALUE);
	}
	
	public static String getString( String key, String defValue, int maxLength ) {
		try {
			String s = get().getString( key, defValue );
			if (s != null && s.length() > maxLength) {
				put(key, defValue);
				return defValue;
			} else {
				return s;
			}
		} catch (ClassCastException e) {
			//ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}
	
	//android 2.3+ supports apply, which is asyncronous, much nicer
	
	public static void put( String key, int value ) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			get().edit().putInt(key, value).apply();
		} else {
			get().edit().putInt(key, value).commit();
		}
	}
	
	public static void put( String key, boolean value ) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			get().edit().putBoolean(key, value).apply();
		} else {
			get().edit().putBoolean(key, value).commit();
		}
	}
	
	public static void put( String key, String value ) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			get().edit().putString(key, value).apply();
		} else {
			get().edit().putString(key, value).commit();
		}
	}
	
}
