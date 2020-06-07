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

package com.watabou.noosa;

import com.watabou.input.PointerEvent;
import com.watabou.utils.Signal;

public class PointerArea extends Visual implements Signal.Listener<PointerEvent> {
	
	// Its target can be pointerarea itself
	public Visual target;
	
	protected PointerEvent curEvent = null;
	
	//if true, this PointerArea will always block input, even when it is inactive
	public boolean blockWhenInactive = false;
	
	public PointerArea( Visual target ) {
		super( 0, 0, 0, 0 );
		this.target = target;
		
		PointerEvent.addPointerListener( this );
	}
	
	public PointerArea( float x, float y, float width, float height ) {
		super( x, y, width, height );
		this.target = this;
		
		visible = false;
		
		PointerEvent.addPointerListener( this );
	}
	
	@Override
	public boolean onSignal( PointerEvent event ) {
		
		boolean hit = event != null && target.overlapsScreenPoint( (int)event.current.x, (int)event.current.y );
		
		if (!isActive()) {
			return (hit && blockWhenInactive);
		}
		
		if (hit) {
			
			boolean returnValue = (event.down || event == curEvent);
			
			if (event.down) {
				
				if (curEvent == null) {
					curEvent = event;
				}
				onPointerDown( event );
				
			} else {
				
				onPointerUp( event );
				
				if (curEvent == event) {
					curEvent = null;
					onClick( event );
				}
				
			}
			
			return returnValue;
			
		} else {
			
			if (event == null && curEvent != null) {
				onDrag(curEvent);
			}
			
			else if (curEvent != null && !event.down) {
				onPointerUp( event );
				curEvent = null;
			}
			
			return false;
			
		}
	}
	
	protected void onPointerDown( PointerEvent event ) { }
	
	protected void onPointerUp( PointerEvent event) { }
	
	protected void onClick( PointerEvent event ) { }
	
	protected void onDrag( PointerEvent event ) { }
	
	public void reset() {
		curEvent = null;
	}
	
	@Override
	public void destroy() {
		PointerEvent.removePointerListener( this );
		super.destroy();
	}
}
