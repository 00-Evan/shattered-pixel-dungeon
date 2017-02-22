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

package com.shatteredpixel.shatteredpixeldungeon;

import android.content.SharedPreferences;
import com.watabou.noosa.Game;
import com.watabou.utils.GameMath;

enum Preferences {

	INSTANCE;
	
	public static final String KEY_LANDSCAPE	= "landscape";
	public static final String KEY_IMMERSIVE	= "immersive";
	public static final String KEY_POWER_SAVER 	= "power_saver";
	public static final String KEY_SCALE		= "scale";
	public static final String KEY_MUSIC		= "music";
	public static final String KEY_MUSIC_VOL    = "music_vol";
	public static final String KEY_SOUND_FX		= "soundfx";
	public static final String KEY_SFX_VOL      = "sfx_vol";
	public static final String KEY_ZOOM			= "zoom";
	public static final String KEY_LAST_CLASS	= "last_class";
	public static final String KEY_CHALLENGES	= "challenges";
	public static final String KEY_QUICKSLOTS	= "quickslots";
	public static final String KEY_FLIPTOOLBAR	= "flipped_ui";
	public static final String KEY_FLIPTAGS 	= "flip_tags";
	public static final String KEY_BARMODE		= "toolbar_mode";
	public static final String KEY_LANG         = "language";
	public static final String KEY_CLASSICFONT	= "classic_font";
	public static final String KEY_INTRO		= "intro";
	public static final String KEY_BRIGHTNESS	= "brightness";
	public static final String KEY_GRID 	    = "visual_grid";
	public static final String KEY_VERSION      = "version";
	
	private SharedPreferences prefs;
	
	private SharedPreferences get() {
		if (prefs == null) {
			prefs = Game.instance.getPreferences( Game.MODE_PRIVATE );
		}
		return prefs;
	}

	boolean contains( String key ){
		return get().contains( key );
	}

	int getInt( String key, int defValue ) {
		return getInt(key, defValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	int getInt( String key, int defValue, int min, int max ) {
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
			ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}
	
	boolean getBoolean( String key, boolean defValue ) {
		try {
			return get().getBoolean(key, defValue);
		} catch (ClassCastException e) {
			ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}

	String getString( String key, String defValue ) {
		return getString(key, defValue, Integer.MAX_VALUE);
	}
	
	String getString( String key, String defValue, int maxLength ) {
		try {
			String s = get().getString( key, defValue );
			if (s != null && s.length() > maxLength) {
				put(key, defValue);
				return defValue;
			} else {
				return s;
			}
		} catch (ClassCastException e) {
			ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}
	
	void put( String key, int value ) {
		get().edit().putInt( key, value ).commit();
	}
	
	void put( String key, boolean value ) {
		get().edit().putBoolean( key, value ).commit();
	}
	
	void put( String key, String value ) {
		get().edit().putString( key, value ).commit();
	}
}
