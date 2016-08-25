/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon;

import android.opengl.GLES20;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Image;
import com.watabou.utils.Rect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class FogOfWar extends Image {

	private static final int VISIBLE	= 0x00000000;
	private static final int VISITED	= 0xcc000000;
	private static final int MAPPED		= 0xcc442211;
	private static final int INVISIBLE	= 0xFF000000;
	
	private int pWidth;
	private int pHeight;
	
	private int width2;
	private int height2;

	public Rect updated;
	
	public FogOfWar( int mapWidth, int mapHeight ) {
		
		super();
		
		pWidth = mapWidth + 1;
		pHeight = mapHeight + 1;
		
		width2 = 1;
		while (width2 < pWidth) {
			width2 <<= 1;
		}
		
		height2 = 1;
		while (height2 < pHeight) {
			height2 <<= 1;
		}
		
		float size = DungeonTilemap.SIZE;
		width = width2 * size;
		height = height2 * size;
		
		texture( new FogTexture(width2, height2) );
		
		scale.set(
			DungeonTilemap.SIZE,
			DungeonTilemap.SIZE );
		
		x = y = -size / 2;

		updated = new Rect(0, 0, pWidth, pHeight);
	}
	
	public void updateVisibility( boolean[] visible, boolean[] visited, boolean[] mapped ) {

		if (updated.isEmpty())
			return;

		FogTexture fog = (FogTexture)texture;

		for (int i=updated.top; i < updated.bottom; i++) {
			int cell = (pWidth - 1) * i + updated.left;
			fog.pixels.position((width2) * i + updated.left);
			for (int j=updated.left; j < updated.right; j++) {
				if (cell < pWidth || cell >= Dungeon.level.length()) {
					fog.pixels.put(INVISIBLE);
				} else
				if (visible[cell] && visible[cell - (pWidth - 1)] &&
					visible[cell - 1] && visible[cell - (pWidth - 1) - 1]) {
					fog.pixels.put(VISIBLE);
				} else
				if (visited[cell] && visited[cell - (pWidth - 1)] &&
					visited[cell - 1] && visited[cell - (pWidth - 1) - 1]) {
					fog.pixels.put(VISITED);
				}
				else
				if (mapped[cell] && mapped[cell - (pWidth - 1)] &&
					mapped[cell - 1] && mapped[cell - (pWidth - 1) - 1]) {
					fog.pixels.put(MAPPED);
				} else {
					fog.pixels.put(INVISIBLE);
				}
				cell++;
			}
		}

		if (updated.width() == pWidth && updated.height() == pHeight)
			fog.update();
		else
			fog.update(updated.top, updated.bottom);
		updated.setEmpty();

	}

	//provides a native intbuffer implementation because android.graphics.bitmap is too slow
	//TODO perhaps should spin this off into something like FastEditTexture in SPD-classes
	private class FogTexture extends SmartTexture {

		private IntBuffer pixels;
		
		public FogTexture(int w, int h) {
			super();
			width = w;
			height = h;
			pixels = ByteBuffer.
					allocateDirect( w * h * 4 ).
					order( ByteOrder.nativeOrder() ).
					asIntBuffer();

			TextureCache.add( FogOfWar.class, this );
		}

		@Override
		protected void generate() {
			int[] ids = new int[1];
			GLES20.glGenTextures( 1, ids, 0 );
			id = ids[0];
		}

		@Override
		public void reload() {
			generate();
			update();
		}

		public void update(){
			bind();
			filter( Texture.LINEAR, Texture.LINEAR );
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

		@Override
		public void delete() {
			super.delete();
		}
	}

	@Override
	public void draw() {

		if (!updated.isEmpty()){
			updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
		}

		super.draw();
	}
}
