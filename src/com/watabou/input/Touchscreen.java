/*
 * Copyright (C) 2012-2015 Oleg Dolya
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

import java.util.ArrayList;
import java.util.HashMap;

import com.watabou.utils.PointF;
import com.watabou.utils.Signal;

import android.view.MotionEvent;

public class Touchscreen {
	
	public static Signal<Touch> event = new Signal<Touch>( true );
	
	public static HashMap<Integer,Touch> pointers = new HashMap<Integer, Touch>();
	
	public static float x;
	public static float y;
	public static boolean touched;

	public static void processTouchEvents( ArrayList<MotionEvent> events ) {
		
		int size = events.size();
		for (int i=0; i < size; i++) {
			
			MotionEvent e = events.get( i );
			Touch touch;
			
			switch (e.getAction() & MotionEvent.ACTION_MASK) {
			
			case MotionEvent.ACTION_DOWN:
				touched = true;
				touch = new Touch( e, 0 );
				pointers.put( e.getPointerId( 0 ), touch );
				event.dispatch( touch );
				break;
				
			case MotionEvent.ACTION_POINTER_DOWN:
				int index = e.getActionIndex();
				touch = new Touch( e, index );
				pointers.put( e.getPointerId( index ), touch );
				event.dispatch( touch );
				break;
				
			case MotionEvent.ACTION_MOVE:
				int count = e.getPointerCount();
				for (int j=0; j < count; j++) {		
					pointers.get( e.getPointerId( j ) ).update( e, j );
				}
				event.dispatch( null );
				break;
				
			case MotionEvent.ACTION_POINTER_UP:
				event.dispatch( pointers.remove( e.getPointerId( e.getActionIndex() ) ).up() );
				break;
				
			case MotionEvent.ACTION_UP:
				touched = false;
				event.dispatch( pointers.remove( e.getPointerId( 0 ) ).up() );
				break;
				
			}
			
			e.recycle();
		}
	}
	
	public static class Touch {
		
		public PointF start;
		public PointF current;
		public boolean down;
		
		public Touch( MotionEvent e, int index ) {
			
			float x = e.getX( index );
			float y = e.getY( index );
			
			start = new PointF( x, y );
			current = new PointF( x, y );
			
			down = true;
		}
		
		public void update( MotionEvent e, int index ) {
			current.set( e.getX( index ), e.getY( index ) );
		}
		
		public Touch up() {
			down = false;
			return this;
		}
	}

}
