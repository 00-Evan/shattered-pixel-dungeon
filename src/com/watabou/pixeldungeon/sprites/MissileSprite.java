/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.watabou.pixeldungeon.sprites;

import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.pixeldungeon.DungeonTilemap;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

	private static final float SPEED	= 240f;
	
	private Callback callback;
	
	public MissileSprite() {
		super();
		originToCenter();
	}
	
	public void reset( int from, int to, Item item, Callback listener ) {
		if (item == null) {
			reset( from, to, 0, null, listener );
		} else {
			reset( from, to, item.image(), item.glowing(), listener );
		}
	}
	
	public void reset( int from, int to, int image, Glowing glowing, Callback listener ) {
		revive();
		
		view( image, glowing );
		
		this.callback = listener;

		point( DungeonTilemap.tileToWorld( from ) );
		PointF dest = DungeonTilemap.tileToWorld( to );
		
		PointF d = PointF.diff( dest, point() ); 
		speed.set( d ).normalize().scale( SPEED );
		
		if (image == 31 || image == 108 || image == 109 || image == 110) {
			angularSpeed = 0;
			angle = 135 - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);
		} else {
			angularSpeed = image == 15 || image == 106 ? 1440 : 720;
		}
		
		PosTweener tweener = new PosTweener( this, dest, d.length() / SPEED );
		tweener.listener = this;
		parent.add( tweener );
	}

	@Override
	public void onComplete( Tweener tweener ) {
		kill();
		if (callback != null) {
			callback.call();
		}
	}
}
