/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

	private static final float SPEED	= 240f;
	
	private Callback callback;
	
	public void reset( int from, int to, Item item, Callback listener ) {
		reset( DungeonTilemap.tileToWorld( from ), DungeonTilemap.tileToWorld( to ), item, listener);
	}

	public void reset( Visual from, Visual to, Item item, Callback listener ) {
		reset(from.center(this), to.center(this), item, listener );
	}

	public void reset( Visual from, int to, Item item, Callback listener ) {
		reset(from.center(this), DungeonTilemap.tileToWorld( to ), item, listener );
	}
	
	public void reset( int from, Visual to, Item item, Callback listener ) {
		reset(DungeonTilemap.tileToWorld( from ), to.center(this), item, listener );
	}

	public void reset( PointF from, PointF to, Item item, Callback listener) {
		revive();
		int image;

		if (item == null)   view(image = 0, null);
		else                view(image = item.image(), item.glowing());

		setup( from,
				to,
				image,
				listener );
	}
	
	private static final int DEFAULT_ANGULAR_SPEED = 720;
	
	private static final HashMap<Integer, Integer> ANGULAR_SPEEDS = new HashMap<>();
	static {
		ANGULAR_SPEEDS.put(ItemSpriteSheet.DART,            0);
		ANGULAR_SPEEDS.put(ItemSpriteSheet.THROWING_KNIFE,  0);
		ANGULAR_SPEEDS.put(ItemSpriteSheet.FISHING_SPEAR,   0);
		ANGULAR_SPEEDS.put(ItemSpriteSheet.JAVELIN,         0);
		ANGULAR_SPEEDS.put(ItemSpriteSheet.TRIDENT,         0);
		
		for( int i = ItemSpriteSheet.TIPPED_DARTS; i < ItemSpriteSheet.TIPPED_DARTS+16; i++){
			ANGULAR_SPEEDS.put(i, 0);
		}
		
		//720 is default
		
		ANGULAR_SPEEDS.put(ItemSpriteSheet.BOOMERANG,       1440);
		ANGULAR_SPEEDS.put(ItemSpriteSheet.BOLAS,           1440);
		
		ANGULAR_SPEEDS.put(ItemSpriteSheet.SHURIKEN,        2160);
	}

	//TODO it might be nice to have a source and destination angle, to improve thrown weapon visuals
	private void setup( PointF from, PointF to, int image, Callback listener ){

		originToCenter();

		this.callback = listener;

		point( from );

		PointF d = PointF.diff( to, from );
		speed.set( d ).normalize().scale( SPEED );

		if ( ANGULAR_SPEEDS.containsKey(image)) angularSpeed = ANGULAR_SPEEDS.get(image);
		else                                    angularSpeed = DEFAULT_ANGULAR_SPEED;
		
		angle = 135 - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);
		
		if (d.x >= 0){
			flipHorizontal = false;
			updateFrame();
			
		} else {
			angularSpeed = -angularSpeed;
			angle += 90;
			flipHorizontal = true;
			updateFrame();
		}
		
		PosTweener tweener = new PosTweener( this, to, d.length() / SPEED );
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
