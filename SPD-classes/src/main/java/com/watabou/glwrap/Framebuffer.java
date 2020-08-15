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

public class Framebuffer {

	public static final int COLOR	= Gdx.gl.GL_COLOR_ATTACHMENT0;
	public static final int DEPTH	= Gdx.gl.GL_DEPTH_ATTACHMENT;
	public static final int STENCIL	= Gdx.gl.GL_STENCIL_ATTACHMENT;
	
	public static final Framebuffer	system	= new Framebuffer( 0 );
	
	private int id;
	
	public Framebuffer() {
		id = Gdx.gl.glGenBuffer();
	}
	
	private Framebuffer( int n ) {
		
	}
	
	public void bind() {
		Gdx.gl.glBindFramebuffer( Gdx.gl.GL_FRAMEBUFFER, id );
	}
	
	public void delete() {
		Gdx.gl.glDeleteBuffer(id);
	}
	
	public void attach( int point, Texture tex ) {
		bind();
		Gdx.gl.glFramebufferTexture2D( Gdx.gl.GL_FRAMEBUFFER, point, Gdx.gl.GL_TEXTURE_2D, tex.id, 0 );
	}
	
	public void attach( int point, Renderbuffer buffer ) {
		bind();
		Gdx.gl.glFramebufferRenderbuffer( Gdx.gl.GL_RENDERBUFFER, point, Gdx.gl.GL_TEXTURE_2D, buffer.id() );
	}
	
	public boolean status() {
		bind();
		return Gdx.gl.glCheckFramebufferStatus( Gdx.gl.GL_FRAMEBUFFER ) == Gdx.gl.GL_FRAMEBUFFER_COMPLETE;
	}
}
