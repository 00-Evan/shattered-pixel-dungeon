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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;

public class Halo extends Image {
	
	private static final Object CACHE_KEY = Halo.class;
	
	protected static final int RADIUS	= 64;
	
	protected float radius = RADIUS;
	protected float brightness = 1;

	public Halo() {
		super();
		
		if (!TextureCache.contains( CACHE_KEY )) {
			Bitmap bmp = Bitmap.createBitmap( RADIUS * 2, RADIUS * 2, Bitmap.Config.ARGB_8888 );
			Canvas canvas = new Canvas( bmp );
			Paint paint = new Paint();
			paint.setColor( 0x0AFFFFFF );
			for (int i = 0; i < 50; i++) {
				canvas.drawCircle(RADIUS, RADIUS, RADIUS * (i+1)/50f, paint);
			}
			TextureCache.add( CACHE_KEY, new SmartTexture( bmp ) );
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
		PixelScene.align(this);
		return this;
	}
	
	public void radius( float value ) {
		scale.set(  (this.radius = value) / RADIUS );
	}
}
