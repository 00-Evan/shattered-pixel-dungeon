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

package com.watabou.input;

import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//FIXME at lot of the logic here, in WndKeyBindings, and SPDAction is fairly messy
// should see about doing some refactoring to clean this up
public class KeyBindings {

	private static LinkedHashMap<Integer, GameAction> bindings = new LinkedHashMap<>();

	public static LinkedHashMap<Integer, GameAction> getAllBindings(){
		return new LinkedHashMap<>(bindings);
	}

	public static void setAllBindings(LinkedHashMap<Integer, GameAction> newBindings){
		bindings = new LinkedHashMap<>(newBindings);
	}

	//these are special keybinding that are not user-configurable
	private static LinkedHashMap<Integer, GameAction> hardBindings = new LinkedHashMap<>();

	public static void addHardBinding(int keyCode, GameAction action){
		hardBindings.put(keyCode, action);
	}

	public static boolean acceptUnbound = false;

	public static boolean isKeyBound(int keyCode){
		if (keyCode <= 0 || keyCode > 255){
			return false;
		}
		return acceptUnbound || bindings.containsKey( keyCode ) || hardBindings.containsKey( keyCode );
	}
	
	public static GameAction getActionForKey(KeyEvent event){
		if (bindings.containsKey( event.code )){
			return bindings.get( event.code );
		} else if (hardBindings.containsKey( event.code )) {
			return hardBindings.get( event.code );
		}
		return GameAction.NONE;
	}

	public static ArrayList<Integer> getBoundKeysForAction(GameAction action){
		ArrayList<Integer> result = new ArrayList<>();
		for( int i : bindings.keySet()){
			if (bindings.get(i) == action){
				result.add(i);
			}
		}
		return result;
	}

	public static String getKeyName( int keyCode ){
		if (keyCode == Input.Keys.UNKNOWN){
			return "None";
		} else if (keyCode == Input.Keys.PLUS){
			return "+";
		} else if (keyCode == Input.Keys.BACKSPACE) {
			return "Backspace";
		} else if (keyCode == Input.Keys.FORWARD_DEL) {
			return "Delete";
		} else {
			return Input.Keys.toString(keyCode);
		}
	}

}
