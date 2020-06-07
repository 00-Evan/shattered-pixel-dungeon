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
import com.badlogic.gdx.graphics.Pixmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Texture {

	public static final int NEAREST	= Gdx.gl.GL_NEAREST;
	public static final int LINEAR	= Gdx.gl.GL_LINEAR;
	
	public static final int REPEAT	= Gdx.gl.GL_REPEAT;
	public static final int MIRROR	= Gdx.gl.GL_MIRRORED_REPEAT;
	public static final int CLAMP	= Gdx.gl.GL_CLAMP_TO_EDGE;
	
	public int id = -1;
	private static int bound_id = 0; //id of the currently bound texture
	
	public boolean premultiplied = false;

	protected void generate(){
		id = Gdx.gl.glGenTexture();
	}
	
	public static void activate( int index ) {
		Gdx.gl.glActiveTexture( Gdx.gl.GL_TEXTURE0 + index );
	}
	
	public void bind() {
		if (id == -1){
			generate();
		}
		if (id != bound_id) {
			Gdx.gl.glBindTexture( Gdx.gl.GL_TEXTURE_2D, id );
			bound_id = id;
		}
	}
	
	public static void clear(){
		bound_id = 0;
	}
	
	public void filter( int minMode, int maxMode ) {
		bind();
		Gdx.gl.glTexParameterf( Gdx.gl.GL_TEXTURE_2D, Gdx.gl.GL_TEXTURE_MIN_FILTER, minMode );
		Gdx.gl.glTexParameterf( Gdx.gl.GL_TEXTURE_2D, Gdx.gl.GL_TEXTURE_MAG_FILTER, maxMode );
	}
	
	public void wrap( int s, int t ) {
		bind();
		Gdx.gl.glTexParameterf( Gdx.gl.GL_TEXTURE_2D, Gdx.gl.GL_TEXTURE_WRAP_S, s );
		Gdx.gl.glTexParameterf( Gdx.gl.GL_TEXTURE_2D, Gdx.gl.GL_TEXTURE_WRAP_T, t );
	}
	
	public void delete() {
		if (bound_id == id) bound_id = 0;
		Gdx.gl.glDeleteTexture( id );
	}
	
	public void bitmap( Pixmap pixmap ) {
		bind();
		
		Gdx.gl.glTexImage2D(
				Gdx.gl.GL_TEXTURE_2D,
				0,
				pixmap.getGLInternalFormat(),
				pixmap.getWidth(),
				pixmap.getHeight(),
				0,
				pixmap.getGLFormat(),
				pixmap.getGLType(),
				pixmap.getPixels()
		);
		
		premultiplied = true;
	}
	
	public void pixels( int w, int h, int[] pixels ) {
	
		bind();
		
		IntBuffer imageBuffer = ByteBuffer.
			allocateDirect( w * h * 4 ).
			order( ByteOrder.nativeOrder() ).
			asIntBuffer();
		imageBuffer.put( pixels );
		imageBuffer.position( 0 );
		
		Gdx.gl.glTexImage2D(
			Gdx.gl.GL_TEXTURE_2D,
			0,
			Gdx.gl.GL_RGBA,
			w,
			h,
			0,
			Gdx.gl.GL_RGBA,
			Gdx.gl.GL_UNSIGNED_BYTE,
			imageBuffer );
	}
	
	public void pixels( int w, int h, byte[] pixels ) {
		
		bind();
		
		ByteBuffer imageBuffer = ByteBuffer.
			allocateDirect( w * h ).
			order( ByteOrder.nativeOrder() );
		imageBuffer.put( pixels );
		imageBuffer.position( 0 );
		
		Gdx.gl.glPixelStorei( Gdx.gl.GL_UNPACK_ALIGNMENT, 1 );

		Gdx.gl.glTexImage2D(
			Gdx.gl.GL_TEXTURE_2D,
			0,
			Gdx.gl.GL_ALPHA,
			w,
			h,
			0,
			Gdx.gl.GL_ALPHA,
			Gdx.gl.GL_UNSIGNED_BYTE,
			imageBuffer );
	}
	
	public static Texture create( Pixmap pix ) {
		Texture tex = new Texture();
		tex.bitmap( pix );
		
		return tex;
	}
	
	public static Texture create( int width, int height, int[] pixels ) {
		Texture tex = new Texture();
		tex.pixels( width, height, pixels );
		
		return tex;
	}
	
	public static Texture create( int width, int height, byte[] pixels ) {
		Texture tex = new Texture();
		tex.pixels( width, height, pixels );
		
		return tex;
	}
}
