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

import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;

public class Halo extends Image {
	
	private static final Object CACHE_KEY = Halo.class;
	
	protected static final int RADIUS	= 128;
	
	protected float radius = RADIUS;
	protected float brightness = 1;

	public Halo() {
		super();
		
		if (!TextureCache.contains( CACHE_KEY )) {
			Pixmap pixmap = new Pixmap(2*RADIUS+1, 2*RADIUS+1, Pixmap.Format.RGBA8888);
			pixmap.setColor( 0xFFFFFF08 );
			for (int i = 0; i < RADIUS; i+=2) {
				pixmap.fillCircle(RADIUS, RADIUS, (RADIUS - i));
			}
			TextureCache.add( CACHE_KEY, new SmartTexture( pixmap ) );
		}
		
		texture( CACHE_KEY );
	}
	
	public Halo( float radius, int color, float brightness ) {
		
		this();
		
		hardlight( color );
		alpha( this.brightness = brightness );
		radius( radius );
	}
	
	public Halo point( float x, float y ) {
		this.x = x - (width()/2f);
		this.y = y - (height()/2f);
		return this;
	}
	
	public void radius( float value ) {
		scale.set(  (this.radius = value) / RADIUS );
	}
}
