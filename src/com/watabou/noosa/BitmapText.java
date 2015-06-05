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

package com.watabou.noosa;

import java.nio.FloatBuffer;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Matrix;
import com.watabou.glwrap.Quad;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class BitmapText extends Visual {

	protected String text;
	protected Font font;

	protected float[] vertices = new float[16];
	protected FloatBuffer quads;
	
	public int realLength;
	
	protected boolean dirty = true;
	
	public BitmapText() {
		this( "", null );
	}
	
	public BitmapText( Font font ) {
		this( "", font );
	}
	
	public BitmapText( String text, Font font ) {
		super( 0, 0, 0, 0 );
		
		this.text = text;
		this.font = font;
	}
	
	@Override
	public void destroy() {
		text = null;
		font = null;
		vertices = null;
		quads = null;
		super.destroy();
	}
	
	@Override
	protected void updateMatrix() {
		// "origin" field is ignored
		Matrix.setIdentity( matrix );
		Matrix.translate( matrix, x, y );
		Matrix.scale( matrix, scale.x, scale.y );
		Matrix.rotate( matrix, angle );
	}
	
	@Override
	public void draw() {
		
		super.draw();
		
		NoosaScript script = NoosaScript.get();
		
		font.texture.bind();
		
		if (dirty) {
			updateVertices();
		}
		
		script.camera( camera() );
		
		script.uModel.valueM4( matrix );
		script.lighting( 
			rm, gm, bm, am, 
			ra, ga, ba, aa );
		script.drawQuadSet( quads, realLength );
		
	}
	
	protected void updateVertices() {
		
		width = 0;
		height = 0;
		
		if (text == null) {
			text = "";
		}
		
		quads = Quad.createSet( text.length() );
		realLength = 0;
		
		int length = text.length();
		for (int i=0; i < length; i++) {
			RectF rect = font.get( text.charAt( i ) );
	
			if (rect == null) {
				rect=null;
			}
			float w = font.width( rect );
			float h = font.height( rect );
			
			vertices[0] 	= width;
			vertices[1] 	= 0;
			
			vertices[2]		= rect.left;
			vertices[3]		= rect.top;
			
			vertices[4] 	= width + w;
			vertices[5] 	= 0;
			
			vertices[6]		= rect.right;
			vertices[7]		= rect.top;
			
			vertices[8] 	= width + w;
			vertices[9] 	= h;
			
			vertices[10]	= rect.right;
			vertices[11]	= rect.bottom;
			
			vertices[12]	= width;
			vertices[13]	= h;
			
			vertices[14]	= rect.left;
			vertices[15]	= rect.bottom;
			
			quads.put( vertices );
			realLength++;
			
			width += w + font.tracking;
			if (h > height) {
				height = h;
			}
		}
		
		if (length > 0) {
			width -= font.tracking;
		}
		
		dirty = false;
		
	}
	
	public void measure() {
		
		width = 0;
		height = 0;
		
		if (text == null) {
			text = "";
		}
		
		int length = text.length();
		for (int i=0; i < length; i++) {
			RectF rect = font.get( text.charAt( i ) );
	
			float w = font.width( rect );
			float h = font.height( rect );
			
			width += w + font.tracking;
			if (h > height) {
				height = h;
			}
		}
		
		if (length > 0) {
			width -= font.tracking;
		}
	}
	
	public float baseLine() {
		return font.baseLine * scale.y;
	}
	
	public Font font() {
		return font;
	}
	
	public void font( Font value ) {
		font = value;
	}
	
	public String text() {
		return text;
	}
	
	public void text( String str ) {
		text = str;
		dirty = true;
	}
	
	public static class Font extends TextureFilm {
		
		public static final String LATIN_UPPER = 
			" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		public static final String LATIN_FULL = 
			" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u007F";
		
		public SmartTexture texture;
		
		public float tracking = 0;
		public float baseLine;
		
		public boolean autoUppercase = false;
		
		public float lineHeight;
		
		protected Font( SmartTexture tx ) {
			super( tx );
			
			texture = tx;
		}
		
		public Font( SmartTexture tx, int width, String chars ) {
			this( tx, width, tx.height, chars );
		}
		
		public Font( SmartTexture tx, int width, int height, String chars ) {
			super( tx );
			
			texture = tx;
			
			autoUppercase = chars.equals( LATIN_UPPER );
			
			int length = chars.length();
			
			float uw = (float)width / tx.width;
			float vh = (float)height / tx.height;
			
			float left = 0;
			float top = 0;
			float bottom = vh;
			
			for (int i=0; i < length; i++) {
				RectF rect = new RectF( left, top, left += uw, bottom );
				add( chars.charAt( i ), rect );
				if (left >= 1) {
					left = 0;
					top = bottom;
					bottom += vh;
				}
			}
			
			lineHeight = baseLine = height;
		}
		
		protected void splitBy( Bitmap bitmap, int height, int color, String chars ) {
			
			autoUppercase = chars.equals( LATIN_UPPER );
			int length = chars.length();
			
			int width = bitmap.getWidth();
			float vHeight = (float)height / bitmap.getHeight();
			
			int pos;
			
		spaceMeasuring:
			for (pos=0; pos <  width; pos++) {
				for (int j=0; j < height; j++) {
					if (bitmap.getPixel( pos, j ) != color) {
						break spaceMeasuring;
					}
				}
			}
			add( ' ', new RectF( 0, 0, (float)pos / width, vHeight ) );
			
			for (int i=0; i < length; i++) {
				
				char ch = chars.charAt( i );
				if (ch == ' ') {
					continue;
				} else {
					
					boolean found;
					int separator = pos;
					
					do {
						if (++separator >= width) {
							break;
						}
						found = true;
						for (int j=0; j < height; j++) {
							if (bitmap.getPixel( separator, j ) != color) {
								found = false;
								break;
							}
						}
					} while (!found);
					
					add( ch, new RectF( (float)pos / width, 0, (float)separator / width, vHeight ) );
					pos = separator + 1;
				}
			}
			
			lineHeight = baseLine = height( frames.get( chars.charAt( 0 ) ) );
		}
		
		public static Font colorMarked( Bitmap bmp, int color, String chars ) {
			Font font = new Font( TextureCache.get( bmp ) );
			font.splitBy( bmp, bmp.getHeight(), color, chars );
			return font;
		}
		 
		public static Font colorMarked( Bitmap bmp, int height, int color, String chars ) {
			Font font = new Font( TextureCache.get( bmp ) );
			font.splitBy( bmp, height, color, chars );
			return font;
		}
		
		public RectF get( char ch ) {
			return super.get( autoUppercase ? Character.toUpperCase( ch ) : ch );
		}
	}
}
