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
import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Vertexbuffer;
import com.watabou.utils.RectF;

import java.nio.FloatBuffer;

public class Image extends Visual {

	public SmartTexture texture;
	protected RectF frame;
	
	public boolean flipHorizontal;
	public boolean flipVertical;
	
	protected float[] vertices;
	protected FloatBuffer verticesBuffer;
	protected Vertexbuffer buffer;
	
	protected boolean dirty;
	
	public Image() {
		super( 0, 0, 0, 0 );
		
		vertices = new float[16];
		verticesBuffer = Quad.create();
	}
	
	public Image( Image src ) {
		this();
		copy( src );
	}
	
	public Image( Object tx ) {
		this();
		texture( tx );
	}
	
	public Image( Object tx, int left, int top, int width, int height ) {
		this( tx );
		frame( texture.uvRect( left,  top,  left + width, top + height ) );
	}
	
	public void texture( Object tx ) {
		texture = tx instanceof SmartTexture ? (SmartTexture)tx : TextureCache.get( tx );
		frame( new RectF( 0, 0, 1, 1 ) );
	}
	
	public void frame( RectF frame ) {
		this.frame = frame;
		
		width = frame.width() * texture.width;
		height = frame.height() * texture.height;
		
		updateFrame();
		updateVertices();
	}
	
	public void frame( int left, int top, int width, int height ) {
		frame( texture.uvRect( left, top, left + width, top + height ) );
	}
	
	public RectF frame() {
		return new RectF( frame );
	}

	public void copy( Image other ) {
		texture = other.texture;
		frame = new RectF( other.frame );
		
		width = other.width;
		height = other.height;

		scale = other.scale;
		
		updateFrame();
		updateVertices();
	}
	
	protected void updateFrame() {
		
		if (flipHorizontal) {
			vertices[2]		= frame.right;
			vertices[6]		= frame.left;
			vertices[10]	= frame.left;
			vertices[14]	= frame.right;
		} else {
			vertices[2]		= frame.left;
			vertices[6]		= frame.right;
			vertices[10]	= frame.right;
			vertices[14]	= frame.left;
		}
		
		if (flipVertical) {
			vertices[3]		= frame.bottom;
			vertices[7]		= frame.bottom;
			vertices[11]	= frame.top;
			vertices[15]	= frame.top;
		} else {
			vertices[3]		= frame.top;
			vertices[7]		= frame.top;
			vertices[11]	= frame.bottom;
			vertices[15]	= frame.bottom;
		}
		
		dirty = true;
	}
	
	protected void updateVertices() {
		
		vertices[0] 	= 0;
		vertices[1] 	= 0;
		
		vertices[4] 	= width;
		vertices[5] 	= 0;
		
		vertices[8] 	= width;
		vertices[9] 	= height;
		
		vertices[12]	= 0;
		vertices[13]	= height;
		
		dirty = true;
	}
	
	@Override
	public void draw() {

		if (texture == null || (!dirty && buffer == null))
			return;
		
		super.draw();

		if (dirty) {
			verticesBuffer.position( 0 );
			verticesBuffer.put( vertices );
			if (buffer == null)
				buffer = new Vertexbuffer( verticesBuffer );
			else
				buffer.updateVertices( verticesBuffer );
			dirty = false;
		}

		NoosaScript script = script();
		
		texture.bind();
		
		script.camera( camera() );
		
		script.uModel.valueM4( matrix );
		script.lighting(
			rm, gm, bm, am,
			ra, ga, ba, aa );

		script.drawQuad( buffer );
		
	}

	protected NoosaScript script(){
		return NoosaScript.get();
	}

	@Override
	public void destroy() {
		super.destroy();
		if (buffer != null)
			buffer.delete();
	}
}
