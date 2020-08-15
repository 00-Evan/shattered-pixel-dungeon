/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

public class Shader {

	public static final int VERTEX		= Gdx.gl.GL_VERTEX_SHADER;
	public static final int FRAGMENT	= Gdx.gl.GL_FRAGMENT_SHADER;
	
	private int handle;
	
	public Shader( int type ) {
		handle = Gdx.gl.glCreateShader( type );
	}
	
	public int handle() {
		return handle;
	}
	
	public void source( String src ) {
		Gdx.gl.glShaderSource( handle, src );
	}
	
	public void compile() {
		Gdx.gl.glCompileShader( handle );
		
		IntBuffer status = BufferUtils.newIntBuffer(1);
		Gdx.gl.glGetShaderiv( handle, Gdx.gl.GL_COMPILE_STATUS, status);
		if (status.get() == Gdx.gl.GL_FALSE) {
			throw new Error( Gdx.gl.glGetShaderInfoLog( handle ) );
		}
	}
	
	public void delete() {
		Gdx.gl.glDeleteShader( handle );
	}
	
	public static Shader createCompiled( int type, String src ) {
		Shader shader = new Shader( type );
		shader.source( src );
		shader.compile();
		return shader;
	}
}
