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

package com.watabou.noosa;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.utils.RectF;

import java.util.HashMap;

public class TextureFilm {
	
	private static final RectF FULL = new RectF( 0, 0, 1, 1 );
	
	private int texWidth;
	private int texHeight;

	private SmartTexture texture;
	
	protected HashMap<Object,RectF> frames = new HashMap<>();
	
	public TextureFilm( Object tx ) {
		
		texture = TextureCache.get( tx );
		
		texWidth = texture.width;
		texHeight = texture.height;
		
		add( null, FULL );
	}
	
	public TextureFilm( SmartTexture texture, int width ) {
		this( texture, width, texture.height );
	}
	
	public TextureFilm( Object tx, int width, int height ) {
		
		texture = TextureCache.get( tx );
		
		texWidth = texture.width;
		texHeight = texture.height;
		
		float uw = (float)width / texWidth;
		float vh = (float)height / texHeight;
		int cols = texWidth / width;
		int rows = texHeight / height;
		
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				RectF rect = new RectF( j * uw, i * vh, (j+1) * uw, (i+1) * vh );
				add( i * cols + j, rect );
			}
		}
	}
	
	public TextureFilm( TextureFilm atlas, Object key, int width, int height ) {

		texture = atlas.texture;
	
		texWidth = atlas.texWidth;
		texHeight = atlas.texHeight;
		
		RectF patch = atlas.get( key );
		
		float uw = (float)width / texWidth;
		float vh = (float)height / texHeight;
		int cols = (int)(width( patch ) / width);
		int rows = (int)(height( patch ) / height);
		
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				RectF rect = new RectF( j * uw, i * vh, (j+1) * uw, (i+1) * vh );
				rect.shift( patch.left, patch.top );
				add( i * cols + j, rect );
			}
		}
	}
	
	public void add( Object id, RectF rect ) {
		frames.put( id, rect );
	}

	public void add( Object id, int left, int top, int right, int bottom){
		frames.put( id, texture.uvRect(left, top, right, bottom));
	}
	
	public RectF get( Object id ) {
		return frames.get( id );
	}

	public float width( Object id ){
		return width( get( id ) );
	}
	
	public float width( RectF frame ) {
		return frame.width() * texWidth;
	}

	public float height( Object id ){
		return height( get( id ) );
	}
	
	public float height( RectF frame ) {
		return frame.height() * texHeight;
	}
}