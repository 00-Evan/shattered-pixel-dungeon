/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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

package com.watabou.noosa.tweeners;

import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;

abstract public class Tweener extends Gizmo {

	public Gizmo target;
	
	public float interval;
	public float elapsed;
	
	public Listener listener;
	
	public Tweener( Gizmo target, float interval ) {
		super();
		
		this.target = target;
		this.interval = interval;
		
		elapsed = 0;
	}
	
	@Override
	public void update() {
		elapsed += Game.elapsed;
		if (elapsed >= interval) {
			updateValues( 1 );
			onComplete();
			kill();
		} else {
			updateValues( elapsed / interval );
		}
	}
	
	protected void onComplete() {
		if (listener != null) {
			listener.onComplete( this );
		}
	}
	
	abstract protected void updateValues( float progress );
	
	public static interface Listener {
		void onComplete( Tweener tweener );
	}
}
