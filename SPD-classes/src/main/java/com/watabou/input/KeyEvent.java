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

import com.watabou.utils.Signal;

import java.util.ArrayList;

public class KeyEvent {
	
	public int code;
	public boolean pressed;
	
	public KeyEvent( int code, boolean pressed ) {
		this.code = code;
		this.pressed = pressed;
	}
	
	// **********************
	// *** Static members ***
	// **********************
	
	private static Signal<KeyEvent> keySignal = new Signal<>( true );
	
	public static void addKeyListener( Signal.Listener<KeyEvent> listener ){
		keySignal.add(listener);
	}
	
	public static void removeKeyListener( Signal.Listener<KeyEvent> listener ){
		keySignal.remove(listener);
	}
	
	public static void clearListeners(){
		keySignal.removeAll();
	}
	
	//Accumulated key events
	private static ArrayList<KeyEvent> keyEvents = new ArrayList<>();
	
	public static synchronized void addKeyEvent( KeyEvent event ){
		keyEvents.add( event );
	}
	
	public static synchronized void processKeyEvents(){
		for (KeyEvent k : keyEvents){
			keySignal.dispatch(k);
		}
		keyEvents.clear();
	}
}
