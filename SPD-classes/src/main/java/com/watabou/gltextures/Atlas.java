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

package com.watabou.gltextures;

import com.watabou.utils.RectF;

import java.util.HashMap;

public class Atlas {

	public SmartTexture tx;
	
	protected HashMap<Object,RectF> namedFrames;
	
	protected float uvLeft;
	protected float uvTop;
	protected float uvWidth;
	protected float uvHeight;
	protected int cols;
	
	public Atlas( SmartTexture tx ) {
		
		this.tx = tx;
		tx.atlas = this;
		
		namedFrames = new HashMap<>();
	}
	
	public void add( Object key, int left, int top, int right, int bottom ) {
		add( key, uvRect( tx, left, top, right, bottom ) );
	}
	
	public void add( Object key, RectF rect ) {
		namedFrames.put( key, rect );
	}
	
	public void grid( int width ) {
		grid( width, tx.height );
	}
	
	public void grid( int width, int height ) {
		grid( 0, 0, width, height, tx.width / width );
	}
	
	public void grid( int left, int top, int width, int height, int cols ) {
		uvLeft	= (float)left	/ tx.width;
		uvTop	= (float)top	/ tx.height;
		uvWidth	= (float)width	/ tx.width;
		uvHeight= (float)height	/ tx.height;
		this.cols = cols;
	}
	
	public RectF get( int index ) {
		float x = index % cols;
		float y = index / cols;
		float l = uvLeft	+ x * uvWidth;
		float t = uvTop	+ y * uvHeight;
		return new RectF( l, t, l + uvWidth, t + uvHeight );
	}
	
	public RectF get( Object key ) {
		return namedFrames.get( key );
	}
	
	public float width( RectF rect ) {
		return rect.width() * tx.width;
	}
	
	public float height( RectF rect ) {
		return rect.height() * tx.height;
	}
	
	public static RectF uvRect( SmartTexture tx, int left, int top, int right, int bottom ) {
		return new RectF(
			(float)left		/ tx.width,
			(float)top		/ tx.height,
			(float)right	/ tx.width,
			(float)bottom	/ tx.height );
	}
}
