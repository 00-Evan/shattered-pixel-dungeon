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

package com.watabou.glwrap;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Attribute {

	private int location;
	
	public Attribute( int location ) {
		this.location = location;
	}
	
	public int location() {
		return location;
	}
	
	public void enable() {
		GLES20.glEnableVertexAttribArray( location );
	}
	
	public void disable() {
		GLES20.glDisableVertexAttribArray( location );
	}
	
	public void vertexPointer( int size, int stride, FloatBuffer ptr ) {
		GLES20.glVertexAttribPointer( location, size, GLES20.GL_FLOAT, false, stride * Float.SIZE / 8, ptr );
	}
}
