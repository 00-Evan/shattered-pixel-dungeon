/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.watabou.input;

import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

//TODO maybe just move to game action?
public class KeyBindings {

	private static LinkedHashMap<Integer, Integer> bindings = new LinkedHashMap<>();
	private static HashMap<Integer, String> names = new HashMap<>();

	public static void addBinding( int keyCode, int keyAction){
		bindings.put(keyCode, keyAction);
	}
	
	public static boolean isBound( int keyCode ){
		return bindings.keySet().contains( keyCode );
	}
	
	public static int getBinding( KeyEvent event ){
		return bindings.get( event.code );
	}

	public static ArrayList<Integer> getBindings( int gameAction ){
		ArrayList<Integer> result = new ArrayList<>();
		for( int i : bindings.keySet()){
			if (bindings.get(i) == gameAction){
				result.add(i);
			}
		}
		return result;
	}

	public static String getKeyName( int keyCode ){
		String result = Input.Keys.toString(keyCode);

		if (result.equals("Plus")){
			return "+";
		} else {
			return result;
		}
	}

	public static void addName( int keyAction, String name ){
		names.put(keyAction, name);
	}

	public static String getName( int keyAction ){
		return names.get( keyAction );
	}

}
