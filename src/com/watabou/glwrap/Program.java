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

public class Program {

	private int handle;
	
	public Program() {
		handle = GLES20.glCreateProgram();
	}
	
	public int handle() {
		return handle;
	}
	
	public void attach( Shader shader ) {
		GLES20.glAttachShader( handle, shader.handle() );
	}
	
	public void link() {
		GLES20.glLinkProgram( handle );
		
		int[] status = new int[1];
		GLES20.glGetProgramiv( handle, GLES20.GL_LINK_STATUS, status, 0 );
		if (status[0] == GLES20.GL_FALSE) {
			throw new Error( GLES20.glGetProgramInfoLog( handle ) );
		}
	}
	
	public Attribute attribute( String name ) {
		return new Attribute( GLES20.glGetAttribLocation( handle, name ) );
	}
	
	public Uniform uniform( String name ) {
		return new Uniform( GLES20.glGetUniformLocation( handle, name ) );
	}
	
	public void use() {
		GLES20.glUseProgram( handle );
	}
	
	public void delete() {
		GLES20.glDeleteProgram( handle );
	}
	
	public static Program create( Shader ...shaders ) {
		Program program = new Program();
		for (int i=0; i < shaders.length; i++) {
			program.attach( shaders[i] );
		}
		program.link();
		return program;
	}
}
