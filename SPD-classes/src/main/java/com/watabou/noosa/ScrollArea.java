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

package com.watabou.noosa;

import com.watabou.input.ScrollEvent;
import com.watabou.utils.Signal;

//pointer area with additional support for detecting scrolling events
public class ScrollArea extends PointerArea {
	
	public ScrollArea( Visual target ){
		super( target );
		ScrollEvent.addScrollListener( scrollListener );
	}
	
	public ScrollArea(float x, float y, float width, float height ) {
		super( x, y, width, height );
		ScrollEvent.addScrollListener( scrollListener );
	}
	
	private Signal.Listener<ScrollEvent> scrollListener = new Signal.Listener<ScrollEvent>() {
		@Override
		public boolean onSignal(ScrollEvent event) {

			boolean hit = event != null && target.overlapsScreenPoint( (int)event.pos.x, (int)event.pos.y );

			if (!isActive()) {
				return (hit && blockLevel == ALWAYS_BLOCK);
			}

			if (hit){
				onScroll( event );
				return true;
			}
			return false;
		}
	};
	
	protected void onScroll( ScrollEvent event ){ }
	
	@Override
	public void destroy() {
		super.destroy();
		ScrollEvent.removeScrollListener( scrollListener );
	}
}
