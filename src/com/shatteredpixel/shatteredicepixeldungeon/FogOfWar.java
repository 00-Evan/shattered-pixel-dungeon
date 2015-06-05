/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon;

import java.util.Arrays;

import android.graphics.Bitmap;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.Image;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;

public class FogOfWar extends Image {

	private static final int VISIBLE	= 0x00000000;
	private static final int VISITED	= 0xcc111111;
	private static final int MAPPED		= 0xcc442211;
	private static final int INVISIBLE	= 0xFF000000;
	
	private int[] pixels;
	
	private int pWidth;
	private int pHeight;
	
	private int width2;
	private int height2;
	
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
		
		texture( new FogTexture() );
		
		scale.set( 
			DungeonTilemap.SIZE, 
			DungeonTilemap.SIZE );
		
		x = y = -size / 2;
	}
	
	public void updateVisibility( boolean[] visible, boolean[] visited, boolean[] mapped ) {
		
		if (pixels == null) {
			pixels = new int[width2 * height2];
			Arrays.fill( pixels, INVISIBLE );
		}
		
		for (int i=1; i < pHeight - 1; i++) {
			int pos = (pWidth - 1) * i;
			for (int j=1; j < pWidth - 1; j++) {
				pos++;
				int c = INVISIBLE;
				if (visible[pos] && visible[pos - (pWidth - 1)] && 
					visible[pos - 1] && visible[pos - (pWidth - 1) - 1]) {
					c = VISIBLE;
				} else 
				if (visited[pos] && visited[pos - (pWidth - 1)] && 
					visited[pos - 1] && visited[pos - (pWidth - 1) - 1]) {
					c = VISITED;
				}
				else 
				if (mapped[pos] && mapped[pos - (pWidth - 1)] && 
					mapped[pos - 1] && mapped[pos - (pWidth - 1) - 1]) {
					c = MAPPED;
				}
				pixels[i * width2 + j] = c;
			}
		}
		
		texture.pixels( width2, height2, pixels );
	}
	
	private class FogTexture extends SmartTexture {
		
		public FogTexture() {
			super( Bitmap.createBitmap( width2, height2, Bitmap.Config.ARGB_8888 ) );
			filter( Texture.LINEAR, Texture.LINEAR );
			TextureCache.add( FogOfWar.class, this );
		}
		
		@Override
		public void reload() {
			super.reload();
			GameScene.afterObserve();
		}
	}
}
