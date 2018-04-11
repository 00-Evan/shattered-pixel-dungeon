/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.watabou.gltextures;

import android.opengl.GLES20;

import com.watabou.glwrap.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

//provides a native intbuffer implementation because android.graphics.bitmap is too slow
public class BufferTexture extends SmartTexture {
	
	public IntBuffer pixels;
	
	public BufferTexture(int w, int h) {
		super();
		width = w;
		height = h;
		pixels = ByteBuffer.
				allocateDirect( w * h * 4 ).
				order( ByteOrder.nativeOrder() ).
				asIntBuffer();
	}
	
	@Override
	protected void generate() {
		int[] ids = new int[1];
		GLES20.glGenTextures( 1, ids, 0 );
		id = ids[0];
	}
	
	@Override
	public void reload() {
		super.reload();
		update();
	}
	
	public void update(){
		bind();
		filter( Texture.LINEAR, Texture.LINEAR );
		wrap( Texture.CLAMP, Texture.CLAMP);
		pixels.position(0);
		GLES20.glTexImage2D(
				GLES20.GL_TEXTURE_2D,
				0,
				GLES20.GL_RGBA,
				width,
				height,
				0,
				GLES20.GL_RGBA,
				GLES20.GL_UNSIGNED_BYTE,
				pixels );
	}
	
	//allows partially updating the texture
	public void update(int top, int bottom){
		bind();
		filter( Texture.LINEAR, Texture.LINEAR );
		wrap( Texture.CLAMP, Texture.CLAMP);
		pixels.position(top*width);
		GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D,
				0,
				0,
				top,
				width,
				bottom - top,
				GLES20.GL_RGBA,
				GLES20.GL_UNSIGNED_BYTE,
				pixels);
	}
}
