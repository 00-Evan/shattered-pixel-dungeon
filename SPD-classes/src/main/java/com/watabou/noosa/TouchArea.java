/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

import com.watabou.input.Touchscreen;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.utils.Signal;

public class TouchArea extends Visual implements Signal.Listener<Touchscreen.Touch> {
	
	// Its target can be toucharea itself
	public Visual target;
	
	protected Touchscreen.Touch touch = null;

	//if true, this TouchArea will always block input, even when it is inactive
	public boolean blockWhenInactive = false;
	
	public TouchArea( Visual target ) {
		super( 0, 0, 0, 0 );
		this.target = target;
		
		Touchscreen.event.add( this );
	}
	
	public TouchArea( float x, float y, float width, float height ) {
		super( x, y, width, height );
		this.target = this;
		
		visible = false;
		
		Touchscreen.event.add( this );
	}

	@Override
	public void onSignal( Touch touch ) {

		boolean hit = touch != null && target.overlapsScreenPoint( (int)touch.current.x, (int)touch.current.y );
		
		if (!isActive()) {
			if (hit && blockWhenInactive) Touchscreen.event.cancel();
			return;
		}
		
		if (hit) {

			if (touch.down || touch == this.touch) Touchscreen.event.cancel();

			if (touch.down) {
				
				if (this.touch == null) {
					this.touch = touch;
				}
				onTouchDown( touch );
				
			} else {
				
				onTouchUp( touch );
				
				if (this.touch == touch) {
					this.touch = null;
					onClick( touch );
				}

			}
			
		} else {
			
			if (touch == null && this.touch != null) {
				onDrag( this.touch );
			}
			
			else if (this.touch != null && !touch.down) {
				onTouchUp( touch );
				this.touch = null;
			}
			
		}
	}
	
	protected void onTouchDown( Touch touch ) {
	}
	
	protected void onTouchUp( Touch touch ) {
	}
	
	protected void onClick( Touch touch ) {
	}
	
	protected void onDrag( Touch touch ) {
	}
	
	public void reset() {
		touch = null;
	}
	
	@Override
	public void destroy() {
		Touchscreen.event.remove( this );
		super.destroy();
	}
}
