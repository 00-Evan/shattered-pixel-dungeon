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

import java.util.HashMap;

public class KeyBindings {
	
	private static HashMap<Integer, KeyAction> bindings;
	
	static {
		bindings = new HashMap<>();
		
		bindings.put( Input.Keys.BACK, KeyAction.BACK );
		bindings.put( Input.Keys.MENU, KeyAction.MENU );
		
		bindings.put( Input.Keys.H, KeyAction.HERO_INFO );
		bindings.put( Input.Keys.J, KeyAction.JOURNAL );
		
		bindings.put( Input.Keys.NUMPAD_5, KeyAction.WAIT );
		bindings.put( Input.Keys.SPACE,    KeyAction.WAIT );
		bindings.put( Input.Keys.S,        KeyAction.SEARCH );
		
		bindings.put( Input.Keys.I,  KeyAction.INVENTORY );
		bindings.put( Input.Keys.Q,  KeyAction.QUICKSLOT_1 );
		bindings.put( Input.Keys.W,  KeyAction.QUICKSLOT_2 );
		bindings.put( Input.Keys.E,  KeyAction.QUICKSLOT_3 );
		bindings.put( Input.Keys.R,  KeyAction.QUICKSLOT_4 );
		
		bindings.put( Input.Keys.A,     KeyAction.TAG_ATTACK );
		bindings.put( Input.Keys.TAB,   KeyAction.TAG_DANGER );
		bindings.put( Input.Keys.D,     KeyAction.TAG_ACTION );
		bindings.put( Input.Keys.ENTER, KeyAction.TAG_LOOT );
		bindings.put( Input.Keys.T,     KeyAction.TAG_RESUME );
		
		bindings.put( Input.Keys.PLUS,   KeyAction.ZOOM_IN );
		bindings.put( Input.Keys.EQUALS, KeyAction.ZOOM_IN );
		bindings.put( Input.Keys.MINUS,  KeyAction.ZOOM_OUT );
		bindings.put( Input.Keys.SLASH,  KeyAction.ZOOM_DEFAULT );
		
		bindings.put( Input.Keys.UP,    KeyAction.N );
		bindings.put( Input.Keys.RIGHT, KeyAction.E );
		bindings.put( Input.Keys.DOWN,  KeyAction.S );
		bindings.put( Input.Keys.LEFT,  KeyAction.W );
		bindings.put( Input.Keys.NUMPAD_8,  KeyAction.N );
		bindings.put( Input.Keys.NUMPAD_9,  KeyAction.NE );
		bindings.put( Input.Keys.NUMPAD_6,  KeyAction.E );
		bindings.put( Input.Keys.NUMPAD_3,  KeyAction.SE );
		bindings.put( Input.Keys.NUMPAD_2,  KeyAction.S );
		bindings.put( Input.Keys.NUMPAD_1,  KeyAction.SW );
		bindings.put( Input.Keys.NUMPAD_4,  KeyAction.W );
		bindings.put( Input.Keys.NUMPAD_7,  KeyAction.NW );
	}
	
	public static boolean isBound( int keyCode ){
		return bindings.keySet().contains( keyCode );
	}
	
	public static KeyAction getBinding( KeyEvent event ){
		return bindings.get( event.code );
	}
	
}
