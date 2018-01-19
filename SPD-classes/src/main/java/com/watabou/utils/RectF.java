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

package com.watabou.utils;

public class RectF {
	
	public float left;
	public float top;
	public float right;
	public float bottom;
	
	public RectF() {
		this( 0, 0, 0, 0 );
	}
	
	public RectF( RectF rect ) {
		this( rect.left, rect.top, rect.right, rect.bottom );
	}
	
	public RectF( Rect rect ) {
		this( rect.left, rect.top, rect.right, rect.bottom );
	}
	
	public RectF( float left, float top, float right, float bottom ) {
		this.left	= left;
		this.top	= top;
		this.right	= right;
		this.bottom	= bottom;
	}
	
	public float width() {
		return right - left;
	}
	
	public float height() {
		return bottom - top;
	}
	
	public float square() {
		return width() * height();
	}
	
	public RectF set( float left, float top, float right, float bottom ) {
		this.left	= left;
		this.top	= top;
		this.right	= right;
		this.bottom	= bottom;
		return this;
	}
	
	public RectF set( Rect rect ) {
		return set( rect.left, rect.top, rect.right, rect.bottom );
	}
	
	public RectF setPos( float x, float y ) {
		return set( x, y, x + (right - left), y + (bottom - top));
	}
	
	public RectF shift( float x, float y ) {
		return set( left+x, top+y, right+x, bottom+y );
	}
	
	public RectF resize( float w, float h ){
		return set( left, top, left+w, top+h);
	}
	
	public boolean isEmpty() {
		return right <= left || bottom <= top;
	}
	
	public RectF setEmpty() {
		left = right = top = bottom = 0;
		return this;
	}
	
	public RectF intersect( RectF other ) {
		RectF result = new RectF();
		result.left		= Math.max( left, other.left );
		result.right	= Math.min( right, other.right );
		result.top		= Math.max( top, other.top );
		result.bottom	= Math.min( bottom, other.bottom );
		return result;
	}
	
	public RectF union( RectF other ){
		RectF result = new RectF();
		result.left		= Math.min( left, other.left );
		result.right	= Math.max( right, other.right );
		result.top		= Math.min( top, other.top );
		result.bottom	= Math.max( bottom, other.bottom );
		return result;
	}
	
	public RectF union( float x, float y ) {
		if (isEmpty()) {
			return set( x, y, x + 1, y + 1 );
		} else {
			if (x < left) {
				left = x;
			} else if (x >= right) {
				right = x + 1;
			}
			if (y < top) {
				top = y;
			} else if (y >= bottom) {
				bottom = y + 1;
			}
			return this;
		}
	}
	
	public RectF union( Point p ) {
		return union( p.x, p.y );
	}
	
	public boolean inside( Point p ) {
		return p.x >= left && p.x < right && p.y >= top && p.y < bottom;
	}
	
	public RectF shrink( float d ) {
		return new RectF( left + d, top + d, right - d, bottom - d );
	}
	
	public RectF shrink() {
		return shrink( 1 );
	}
	
}
