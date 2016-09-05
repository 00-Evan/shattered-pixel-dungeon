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
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.utils.Rect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class FogOfWar extends Image {

	private static final int VISIBLE[]	= new int[]{0xAA000000, 0x55000000, //-2 and -1 brightness
													0x00000000, //0 brightness
													0x00000000, 0x00000000}; //1 and 2 brightness

	private static final int VISITED[]	= new int[]{0xEE000000, 0xDD000000,
													0xCC000000,
													0x99000000, 0x66000000};

	private static final int MAPPED[]   = new int[]{0xEE442211, 0xDD442211,
													0xCC442211,
													0x99442211, 0x66442211};

	private static final int INVISIBLE[]= new int[]{0xFF000000, 0xFF000000,
													0xFF000000,
													0xFF000000, 0xFF000000};
	
	private int pWidth;
	private int pHeight;
	
	private int width2;
	private int height2;

	private volatile Rect updated;
	private Rect updating;
	
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

	public synchronized void updateFog(){
		updated.set( 0, 0, pWidth, pWidth );
	}

	public synchronized void updateFogArea(int x, int y, int w, int h){
		updated.union(x, y);
		updated.union(x + w, y + h);
	}

	public synchronized void moveToUpdating(){
		updating = new Rect(updated);
		updated.setEmpty();
	}
	
	private void updateTexture( boolean[] visible, boolean[] visited, boolean[] mapped ) {

		moveToUpdating();

		FogTexture fog = (FogTexture)texture;

		int brightness = ShatteredPixelDungeon.brightness() + 2;

		for (int i=updating.top; i < updating.bottom; i++) {
			int cell = (pWidth - 1) * i + updating.left;
			fog.pixels.position((width2) * i + updating.left);
			for (int j=updating.left; j < updating.right; j++) {
				if (cell < pWidth || cell >= Dungeon.level.length() || j == 0 || j == pWidth-1) {
					fog.pixels.put(INVISIBLE[brightness]);
				} else
				if (visible[cell] && visible[cell - (pWidth - 1)] &&
					visible[cell - 1] && visible[cell - (pWidth - 1) - 1]) {
					fog.pixels.put(VISIBLE[brightness]);
				} else
				if (visited[cell] && visited[cell - (pWidth - 1)] &&
					visited[cell - 1] && visited[cell - (pWidth - 1) - 1]) {
					fog.pixels.put(VISITED[brightness]);
				}
				else
				if (mapped[cell] && mapped[cell - (pWidth - 1)] &&
					mapped[cell - 1] && mapped[cell - (pWidth - 1) - 1]) {
					fog.pixels.put(MAPPED[brightness]);
				} else {
					fog.pixels.put(INVISIBLE[brightness]);
				}
				cell++;
			}
		}

		if (updating.width() == pWidth && updating.height() == pHeight)
			fog.update();
		else
			fog.update(updating.top, updating.bottom);

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
	protected NoosaScript script() {
		return NoosaScriptNoLighting.get();
	}

	@Override
	public void draw() {

		if (!updated.isEmpty()){
			updateTexture(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
			updating.setEmpty();
		}

		super.draw();
	}
}
