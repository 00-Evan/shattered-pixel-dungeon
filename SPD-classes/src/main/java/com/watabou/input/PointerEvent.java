/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Cursor;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class PointerEvent {

	public enum Type {
		DOWN,
		UP,
		HOVER
	}

	//buttons
	public static final int NONE = -1;
	public static final int LEFT = Input.Buttons.LEFT;
	public static final int RIGHT = Input.Buttons.RIGHT;
	public static final int MIDDLE = Input.Buttons.MIDDLE;
	public static final int BACK = Input.Buttons.BACK; //currently unused
	public static final int FORWARD = Input.Buttons.FORWARD;//currently unused

	public PointF start;
	public PointF current;
	public int id;
	public Type type;
	public int button;
	public boolean handled; //for hover events, to ensure hover always ends even with overlapping elements

	public PointerEvent( int x, int y, int id, Type type){
		this(x, y, id, type, NONE);
	}

	public PointerEvent( int x, int y, int id, Type type, int button){
		if (Cursor.isCursorCaptured()){
			x = Game.width/2;
			y = Game.width/2;
		}
		start = current = new PointF(x, y);
		this.id = id;
		this.type = type;
		handled = false;
		this.button = button;
	}
	
	public void update( PointerEvent other ){
		this.current = other.current;
	}
	
	public void update( int x, int y ){
		current.set( x, y );
	}
	
	public PointerEvent up() {
		if (type == Type.DOWN) type = Type.UP;
		return this;
	}

	public PointerEvent handle(){
		handled = true;
		return this;
	}
	
	// **********************
	// *** Static members ***
	// **********************
	
	private static Signal<PointerEvent> pointerSignal = new Signal<>( true );
	
	public static void addPointerListener( Signal.Listener<PointerEvent> listener ){
		pointerSignal.add(listener);
	}
	
	public static void removePointerListener( Signal.Listener<PointerEvent> listener ){
		pointerSignal.remove(listener);
	}
	
	public static void clearListeners(){
		pointerSignal.removeAll();
	}
	
	// Accumulated pointer events
	private static ArrayList<PointerEvent> pointerEvents = new ArrayList<>();
	private static HashMap<Integer, PointerEvent> activePointers = new HashMap<>();

	private static PointF lastHoverPos = new PointF();

	public static PointF currentHoverPos(){
		if (lastHoverPos.x == 0 && lastHoverPos.y == 0){
			lastHoverPos.x = Game.width/2;
			lastHoverPos.y = Game.height/2;
		}
		return lastHoverPos.clone();
	}
	
	public static synchronized void addPointerEvent( PointerEvent event ){
		pointerEvents.add( event );
	}

	public static synchronized void addIfExisting( PointerEvent event ){
		if (activePointers.containsKey(event.id)) {
			pointerEvents.add(event);
		}
	}

	public static boolean clearKeyboardThisPress = true;
	
	public static synchronized void processPointerEvents(){
		//handle any hover events separately first as we may need to add drag events
		boolean hovered = false;
		for (PointerEvent p : pointerEvents){
			if (p.type == Type.HOVER){
				lastHoverPos.set(p.current);
				pointerSignal.dispatch(p);
				hovered = true;
			}
		}

		//add drag events for any emulated presses
		if (hovered){
			for (int i = 10+LEFT; i <= 10+FORWARD; i++){
				if (activePointers.containsKey(i)){
					Game.inputHandler.emulateDrag(i-10);
				}
			}
		}

		for (PointerEvent p : pointerEvents){
			if (p.type == Type.HOVER){
				continue;
			}
			clearKeyboardThisPress = true;
			if (activePointers.containsKey(p.id)){
				PointerEvent existing = activePointers.get(p.id);
				existing.current = p.current;
				if (existing.type == p.type){
					pointerSignal.dispatch( null );
				} else if (p.type == Type.DOWN) {
					pointerSignal.dispatch( existing );
				} else {
					activePointers.remove(existing.id);
					pointerSignal.dispatch(existing.up());
				}
			} else {
				if (p.type == Type.DOWN) {
					activePointers.put(p.id, p);
				}
				pointerSignal.dispatch(p);
			}
			if (clearKeyboardThisPress){
				//most press events should clear the keyboard
				Game.platform.setOnscreenKeyboardVisible(false);
			}
		}
		pointerEvents.clear();
	}

	public static synchronized void clearPointerEvents(){
		pointerEvents.clear();
		for (PointerEvent p : activePointers.values()){
			p.current = p.start = new PointF(-1, -1);
			pointerSignal.dispatch(p.up());
		}
		activePointers.clear();
	}
}
