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
import com.watabou.utils.Signal;

import java.util.ArrayList;

//TODO probably want to merge this into a central input processor class
public class Keys {
	
	public static final int BACK		= Input.Keys.BACK;
	public static final int MENU		= Input.Keys.MENU;

	public static Signal<Key> event = new Signal<>( true );
	
	public static void processKeyEvents( ArrayList<Key> events ){
		for (Key k : events){
			event.dispatch(k);
		}
	}
	
	public static class Key {
		
		public int code;
		public boolean pressed;
		
		public Key( int code, boolean pressed ) {
			this.code = code;
			this.pressed = pressed;
		}
	}
}
