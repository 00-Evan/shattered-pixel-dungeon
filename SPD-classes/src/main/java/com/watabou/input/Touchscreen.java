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

import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import java.util.ArrayList;
import java.util.HashMap;

//TODO integrate into a central input handler class
public class Touchscreen {
	
	public static Signal<Touch> event = new Signal<>( true );
	
	public static HashMap<Integer,Touch> pointers = new HashMap<>();
	
	public static void processTouchEvents( ArrayList<Touch> events ) {
		for (Touch t : events){
			if (pointers.containsKey(t.id)){
				Touch existing = pointers.get(t.id);
				existing.current = t.current;
				if (existing.down == t.down){
					event.dispatch( null );
				} else if (t.down) {
					event.dispatch( existing );
				} else {
					pointers.remove(existing.id);
					event.dispatch(existing.up());
				}
			} else {
				if (t.down) {
					pointers.put(t.id, t);
				}
				event.dispatch(t);
			}
		}
	}
	
	public static class Touch {
		
		public PointF start;
		public PointF current;
		public int id;
		public boolean down;
		
		public Touch( int x, int y, int id, boolean down){
			start = current = new PointF(x, y);
			this.id = id;
			this.down = down;
		}
		
		public void update( Touch other ){
			this.current = other.current;
		}
		
		public void update( int x, int y ){
			current.set( x, y );
		}
		
		public Touch up() {
			down = false;
			return this;
		}
	}

}
