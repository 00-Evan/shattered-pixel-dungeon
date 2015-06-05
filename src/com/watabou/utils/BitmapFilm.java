/*
 * Copyright (C) 2012-2015 Oleg Dolya
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

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class BitmapFilm {

	public Bitmap bitmap;
	
	protected HashMap<Object,Rect> frames = new HashMap<Object, Rect>();
	
	public BitmapFilm( Bitmap bitmap ) {
		this.bitmap = bitmap;
		add( null, new Rect( 0, 0, bitmap.getWidth(), bitmap.getHeight() ) );
	}
	
	public BitmapFilm( Bitmap bitmap, int width ) {
		this( bitmap, width, bitmap.getHeight() );
	}
	
	public BitmapFilm( Bitmap bitmap, int width, int height ) {
		this.bitmap = bitmap;
		int cols = bitmap.getWidth() / width;
		int rows = bitmap.getHeight() / height;
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				Rect rect = new Rect( j * width, i * height, (j+1) * width, (i+1) * height );
				add( i * cols + j, rect );
			}
		}
	}
	
	public void add( Object id, Rect rect ) {
		frames.put( id, rect );
	}
	
	public Rect get( Object id ) {
		return frames.get( id );
	}
}
