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

package com.watabou.utils;

import java.util.HashSet;

public class Rect {

	public int left;
	public int top;
	public int right;
	public int bottom;
	
	public Rect() {
		this( 0, 0, 0, 0 );
	}
	
	public Rect( Rect rect ) {
		this( rect.left, rect.top, rect.right, rect.bottom );
	}
	
	public Rect( int left, int top, int right, int bottom ) {
		this.left	= left;
		this.top	= top;
		this.right	= right;
		this.bottom	= bottom;
	}
	
	public int width() {
		return right - left;
	}
	
	public int height() {
		return bottom - top;
	}
	
	public int square() {
		return (right - left) * (bottom - top);
	}
	
	public Rect set( int left, int top, int right, int bottom ) {
		this.left	= left;
		this.top	= top;
		this.right	= right;
		this.bottom	= bottom;
		return this;
	}
	
	public Rect set( Rect rect ) {
		return set( rect.left, rect.top, rect.right, rect.bottom );
	}
	
	public boolean isEmpty() {
		return right <= left || bottom <= top;
	}
	
	public Rect setEmpty() {
		left = right = top = bottom = 0;
		return this;
	}
	
	public Rect intersect( Rect other ) {
		Rect result = new Rect();
		result.left		= Math.max( left, other.left );
		result.right	= Math.min( right, other.right );
		result.top		= Math.max( top, other.top );
		result.bottom	= Math.min( bottom, other.bottom );
		return result;
	}
	
	public Rect union( int x, int y ) {
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
	
	public Rect union( Point p ) {
		return union( p.x, p.y );
	}
	
	public boolean inside( Point p ) {
		return p.x >= left && p.x < right && p.y >= top && p.y < bottom;
	}
	
	public Rect shrink( int d ) {
		return new Rect( left + d, top + d, right - d, bottom - d );
	}
	
	public Rect shrink() {
		return shrink( 1 );
	}

	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<>(square()*2);
		for (int i = left; i <= right; i++)
			for (int j = top; j <= bottom; j++)
				points.add(new Point(i, j));
		return points;
	}
	
}
