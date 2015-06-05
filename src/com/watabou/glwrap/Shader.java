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

import android.opengl.GLES20;

public class Shader {

	public static final int VERTEX		= GLES20.GL_VERTEX_SHADER;
	public static final int FRAGMENT	= GLES20.GL_FRAGMENT_SHADER;
	
	private int handle;
	
	public Shader( int type ) {
		handle = GLES20.glCreateShader( type );
	}
	
	public int handle() {
		return handle;
	}
	
	public void source( String src ) {
		GLES20.glShaderSource( handle, src );
	}
	
	public void compile() {
		GLES20.glCompileShader( handle );

		int[] status = new int[1];
		GLES20.glGetShaderiv( handle, GLES20.GL_COMPILE_STATUS, status, 0 );
		if (status[0] == GLES20.GL_FALSE) {
			throw new Error( GLES20.glGetShaderInfoLog( handle ) );
		}
	}
	
	public void delete() {
		GLES20.glDeleteShader( handle );
	}
	
	public static Shader createCompiled( int type, String src ) {
		Shader shader = new Shader( type );
		shader.source( src );
		shader.compile();
		return shader;
	}
}
