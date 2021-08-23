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

package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Painter {
	
	//If painters require additional parameters, they should
	// request them in their constructor or other methods
	
	//Painters take a level and its collection of rooms, and paint all the specific tile values
	public abstract boolean paint(Level level, ArrayList<Room> rooms);
	
	// Static methods

	public static void set( Level level, int cell, int value ) {
		level.map[cell] = value;
	}
	
	public static void set( Level level, int x, int y, int value ) {
		set( level, x + y * level.width(), value );
	}
	
	public static void set( Level level, Point p, int value ) {
		set( level, p.x, p.y, value );
	}
	
	public static void fill( Level level, int x, int y, int w, int h, int value ) {
		
		int width = level.width();
		
		int pos = y * width + x;
		for (int i=y; i < y + h; i++, pos += width) {
			Arrays.fill( level.map, pos, pos + w, value );
		}
	}
	
	public static void fill( Level level, Rect rect, int value ) {
		fill( level, rect.left, rect.top, rect.width(), rect.height(), value );
	}
	
	public static void fill( Level level, Rect rect, int m, int value ) {
		fill( level, rect.left + m, rect.top + m, rect.width() - m*2, rect.height() - m*2, value );
	}
	
	public static void fill( Level level, Rect rect, int l, int t, int r, int b, int value ) {
		fill( level, rect.left + l, rect.top + t, rect.width() - (l + r), rect.height() - (t + b), value );
	}
	
	public static void drawLine( Level level, Point from, Point to, int value){
		float x = from.x;
		float y = from.y;
		float dx = to.x - from.x;
		float dy = to.y - from.y;
		
		boolean movingbyX = Math.abs(dx) >= Math.abs(dy);
		//normalize
		if (movingbyX){
			dy /= Math.abs(dx);
			dx /= Math.abs(dx);
		} else {
			dx /= Math.abs(dy);
			dy /= Math.abs(dy);
		}
		
		set(level, Math.round(x), Math.round(y), value);
		while((movingbyX && to.x != x) || (!movingbyX && to.y != y)){
			x += dx;
			y += dy;
			set(level, Math.round(x), Math.round(y), value);
		}
	}

	public static void fillEllipse(Level level, Rect rect, int value ) {
		fillEllipse( level, rect.left, rect.top, rect.width(), rect.height(), value );
	}

	public static void fillEllipse(Level level, Rect rect, int m, int value ) {
		fillEllipse( level, rect.left + m, rect.top + m, rect.width() - m*2, rect.height() - m*2, value );
	}
	
	public static void fillEllipse(Level level, int x, int y, int w, int h, int value){

		//radii
		double radH = h/2f;
		double radW = w/2f;

		//fills each row of the ellipse from top to bottom
		for( int i = 0; i < h; i++){

			//y coordinate of the row for determining ellipsis width
			//always want to test the middle of a tile, hence the 0.5 shift
			double rowY = -radH + 0.5 + i;

			//equation is derived from ellipsis formula: y^2/radH^2 + x^2/radW^2 = 1
			//solves for x and then doubles to get the width
			double rowW = 2.0 * Math.sqrt((radW * radW) * (1.0 - (rowY*rowY) / (radH * radH)));

			//need to round to nearest even or odd number, depending on width
			if ( w % 2 == 0 ){
				rowW = Math.round(rowW / 2.0)*2.0;

			} else {
				rowW = Math.floor(rowW / 2.0)*2.0;
				rowW++;
			}

			int cell = x + (w - (int)rowW)/2 + ((y + i) * level.width());
			Arrays.fill( level.map, cell, cell + (int)rowW, value );

		}

	}

	public static void fillDiamond(Level level, Rect rect, int value ) {
		fillDiamond( level, rect.left, rect.top, rect.width(), rect.height(), value );
	}

	public static void fillDiamond(Level level, Rect rect, int m, int value ) {
		fillDiamond( level, rect.left + m, rect.top + m, rect.width() - m*2, rect.height() - m*2, value );
	}

	public static void fillDiamond(Level level, int x, int y, int w, int h, int value){

		//we want the end width to be w, and the width will grow by a total of (h-2 - h%2)
		int diamondWidth = w - (h-2 - h%2);
		//but starting width cannot be smaller than 2 on even width, 3 on odd width.
		diamondWidth = Math.max(diamondWidth, w%2 == 0 ? 2 : 3);

		for (int i = 0; i <= h; i++){
			Painter.fill( level, x + (w - diamondWidth)/2, y+i, diamondWidth, h-2*i, value);
			diamondWidth += 2;
			if (diamondWidth > w) break;
		}

	}
	
	public static Point drawInside( Level level, Room room, Point from, int n, int value ) {
		
		Point step = new Point();
		if (from.x == room.left) {
			step.set( +1, 0 );
		} else if (from.x == room.right) {
			step.set( -1, 0 );
		} else if (from.y == room.top) {
			step.set( 0, +1 );
		} else if (from.y == room.bottom) {
			step.set( 0, -1 );
		}
		
		Point p = new Point( from ).offset( step );
		for (int i=0; i < n; i++) {
			if (value != -1) {
				set( level, p, value );
			}
			p.offset( step );
		}
		
		return p;
	}
}
