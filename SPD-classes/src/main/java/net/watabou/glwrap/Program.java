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

package com.watabou.glwrap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.IntBuffer;

public class Program {

	private int handle;
	
	public Program() {
		handle = Gdx.gl.glCreateProgram();
	}
	
	public int handle() {
		return handle;
	}
	
	public void attach( Shader shader ) {
		Gdx.gl.glAttachShader( handle, shader.handle() );
	}
	
	public void link() {
		Gdx.gl.glLinkProgram( handle );
		
		IntBuffer status = BufferUtils.newIntBuffer(1);
		Gdx.gl.glGetProgramiv( handle, Gdx.gl.GL_LINK_STATUS, status );
		if (status.get() == Gdx.gl.GL_FALSE) {
			throw new Error( Gdx.gl.glGetProgramInfoLog( handle ) );
		}
	}
	
	public Attribute attribute( String name ) {
		return new Attribute( Gdx.gl.glGetAttribLocation( handle, name ) );
	}
	
	public Uniform uniform( String name ) {
		return new Uniform( Gdx.gl.glGetUniformLocation( handle, name ) );
	}
	
	public void use() {
		Gdx.gl.glUseProgram( handle );
	}
	
	public void delete() {
		Gdx.gl.glDeleteProgram( handle );
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
