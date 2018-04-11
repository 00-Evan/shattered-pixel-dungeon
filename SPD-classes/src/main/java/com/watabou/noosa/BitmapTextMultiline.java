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

package com.watabou.noosa;

import com.watabou.glwrap.Quad;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class BitmapTextMultiline extends BitmapText {

	public int maxWidth = Integer.MAX_VALUE;
	
	protected static final Pattern PARAGRAPH	= Pattern.compile( "\n" );
	protected static final Pattern WORD			= Pattern.compile( "\\s+" );
	
	protected float spaceSize;

	public int nLines = 0;

	public boolean[] mask;
	
	public BitmapTextMultiline( Font font ) {
		this( "", font );
	}
	
	public BitmapTextMultiline( String text, Font font ) {
		super( text, font );
		spaceSize = font.width( font.get( ' ' ) );
	}
	
	@Override
	protected void updateVertices() {
		
		if (text == null) {
			text = "";
		}
		
		quads = Quad.createSet( text.length() );
		realLength = 0;
		
		// This object controls lines breaking
		SymbolWriter writer = new SymbolWriter();
		
		// Word size
		PointF metrics = new PointF();
		
		String paragraphs[] = PARAGRAPH.split( text );
		
		// Current character (used in masking)
		int pos = 0;
		
		for (int i=0; i < paragraphs.length; i++) {

			String[] words = WORD.split( paragraphs[i] );
			
			for (int j=0; j < words.length; j++) {
				
				String word = words[j];
				if (word.length() == 0) {
					// This case is possible when there are
					// several spaces coming along
					continue;
				}
				
				
				getWordMetrics( word, metrics );
				writer.addSymbol( metrics.x, metrics.y );
				
				int length = word.length();
				float shift = 0;	// Position in pixels relative to the beginning of the word

				for (int k=0; k < length; k++) {
					RectF rect = font.get( word.charAt( k ) );
			
					float w = font.width( rect );
					float h = font.height( rect );
					
					if (mask == null || mask[pos]) {
						vertices[0] 	= writer.x + shift;
						vertices[1] 	= writer.y;
						
						vertices[2]		= rect.left;
						vertices[3]		= rect.top;
						
						vertices[4] 	= writer.x + shift + w;
						vertices[5] 	= writer.y;
						
						vertices[6]		= rect.right;
						vertices[7]		= rect.top;
						
						vertices[8] 	= writer.x + shift + w;
						vertices[9] 	= writer.y + h;
						
						vertices[10]	= rect.right;
						vertices[11]	= rect.bottom;
						
						vertices[12]	= writer.x + shift;
						vertices[13]	= writer.y + h;
						
						vertices[14]	= rect.left;
						vertices[15]	= rect.bottom;
						
						quads.put( vertices );
						realLength++;
					}
					
					shift += w + font.tracking;
					
					pos++;
				}
				
				writer.addSpace( spaceSize );
			}
			
			writer.newLine( 0, font.lineHeight );
		}

		nLines = writer.nLines();

		dirty = false;
	}
	
	private void getWordMetrics( String word, PointF metrics ) {
		
		float w = 0;
		float h = 0;
		
		int length = word.length();
		for (int i=0; i < length; i++) {

			RectF rect = font.get( word.charAt( i ) );
			w += font.width( rect ) + (w > 0 ? font.tracking : 0);
			h = Math.max( h, font.height( rect ) );
		}
		
		metrics.set( w, h );
	}
	
	@Override
	public void measure() {
		
		SymbolWriter writer = new SymbolWriter();
		
		PointF metrics = new PointF();
		
		String paragraphs[] = PARAGRAPH.split( text );
		
		for (int i=0; i < paragraphs.length; i++) {
			
			String[] words = WORD.split( paragraphs[i] );
			
			for (int j=0; j < words.length; j++) {

				if (j > 0) {
					writer.addSpace( spaceSize );
				}
				String word = words[j];
				if (word.length() == 0) {
					continue;
				}

				getWordMetrics( word, metrics );
				writer.addSymbol( metrics.x, metrics.y );
			}
			
			writer.newLine( 0, font.lineHeight );
		}
		
		width = writer.width;
		height = writer.height;

		nLines = writer.nLines();
	}

	@Override
	public float baseLine() {
		return (height - font.lineHeight + font.baseLine) * scale.y;
	}

	private class SymbolWriter {
		
		public float width = 0;
		public float height = 0;

		public int nLines = 0;

		public float lineWidth = 0;
		public float lineHeight = 0;

		public float x = 0;
		public float y = 0;
		
		public void addSymbol( float w, float h ) {
			if (lineWidth > 0 && lineWidth + font.tracking + w > maxWidth / scale.x) {
				newLine( w, h );
			} else {
				
				x = lineWidth;
				
				lineWidth += (lineWidth > 0 ? font.tracking : 0) + w;
				if (h > lineHeight) {
					lineHeight = h;
				}
			}
		}
		
		public void addSpace( float w ) {
			if (lineWidth > 0 && lineWidth + font.tracking + w > maxWidth / scale.x) {
				newLine( 0, 0 );
			} else {
				
				x = lineWidth;
				lineWidth += (lineWidth > 0 ? font.tracking : 0) + w;
			}
		}
		
		public void newLine( float w, float h ) {
			
			height += lineHeight;
			if (width < lineWidth) {
				width = lineWidth;
			}
			
			lineWidth = w;
			lineHeight = h;
			
			x = 0;
			y = height;

			nLines++;
		}

		public int nLines() {
			return x == 0 ? nLines : nLines+1;
		}
	}
	
	public class LineSplitter {
		
		private ArrayList<BitmapText> lines;
		
		private StringBuilder curLine;
		private float curLineWidth;
		
		private PointF metrics = new PointF();
		
		private void newLine( String str, float width ) {
			BitmapText txt = new BitmapText( curLine.toString(), font );
			txt.scale.set( scale.x );
			lines.add( txt );
			
			curLine = new StringBuilder( str );
			curLineWidth = width;
		}
		
		private void append( String str, float width ) {
			curLineWidth += (curLineWidth > 0 ? font.tracking : 0) + width;
			curLine.append( str );
		}
		
		public ArrayList<BitmapText> split() {
			
			lines = new ArrayList<>();

			curLine = new StringBuilder();
			curLineWidth = 0;
			
			String paragraphs[] = PARAGRAPH.split( text );
			
			for (int i=0; i < paragraphs.length; i++) {
				
				String[] words = WORD.split( paragraphs[i] );
				
				for (int j=0; j < words.length; j++) {
					
					String word = words[j];
					if (word.length() == 0) {
						continue;
					}
					
					getWordMetrics( word, metrics );

					if (curLineWidth > 0 && curLineWidth + font.tracking + metrics.x > maxWidth / scale.x) {
						newLine( word, metrics.x );
					} else {
						append( word,  metrics.x );
					}
					
					if (curLineWidth > 0 && curLineWidth + font.tracking + spaceSize > maxWidth / scale.x) {
						newLine( "", 0 );
					} else {
						append( " ", spaceSize );
					}
				}
				
				newLine( "", 0 );
			}
			
			return lines;
		}
	}
}