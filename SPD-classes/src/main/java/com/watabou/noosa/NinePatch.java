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

public class NinePatch extends Visual {
	
	public SmartTexture texture;
	
	protected float[] vertices;
	protected FloatBuffer quads;
	protected Vertexbuffer buffer;
	
	protected RectF outterF;
	protected RectF innerF;
	
	protected int marginLeft;
	protected int marginRight;
	protected int marginTop;
	protected int marginBottom;
	
	protected float nWidth;
	protected float nHeight;

	protected boolean flipHorizontal;
	protected boolean flipVertical;

	protected boolean dirty;
	
	public NinePatch( Object tx, int margin ) {
		this( tx, margin, margin, margin, margin );
	}
	
	public NinePatch( Object tx, int left, int top, int right, int bottom ) {
		this( tx, 0, 0, 0, 0, left, top, right, bottom );
	}
	
	public NinePatch( Object tx, int x, int y, int w, int h, int margin ) {
		this( tx, x, y, w, h, margin, margin, margin, margin );
	}
	
	public NinePatch( Object tx, int x, int y, int w, int h, int left, int top, int right, int bottom ) {
		super( 0, 0, 0, 0 );
		
		texture = TextureCache.get( tx );
		w = w == 0 ? texture.width : w;
		h = h == 0 ? texture.height : h;
		
		nWidth = width = w;
		nHeight = height = h;
		
		vertices = new float[16];
		quads = Quad.createSet( 9 );

		marginLeft	= left;
		marginRight	= right;
		marginTop	= top;
		marginBottom= bottom;
		
		outterF = texture.uvRect( x, y, x + w, y + h );
		innerF = texture.uvRect( x + left, y + top, x + w - right, y + h - bottom );

		updateVertices();
	}
	
	protected void updateVertices() {

		quads.position( 0 );
		
		float right = width - marginRight;
		float bottom = height - marginBottom;

		float outleft   = flipHorizontal ? outterF.right : outterF.left;
		float outright  = flipHorizontal ? outterF.left : outterF.right;
		float outtop    = flipVertical ? outterF.bottom : outterF.top;
		float outbottom = flipVertical ? outterF.top : outterF.bottom;

		float inleft    = flipHorizontal ? innerF.right : innerF.left;
		float inright   = flipHorizontal ? innerF.left : innerF.right;
		float intop     = flipVertical ? innerF.bottom : innerF.top;
		float inbottom  = flipVertical ? innerF.top : innerF.bottom;

		Quad.fill( vertices,
				0, marginLeft, 0, marginTop, outleft, inleft, outtop, intop );
		quads.put( vertices );
		Quad.fill( vertices,
				marginLeft, right, 0, marginTop, inleft, inright, outtop, intop );
		quads.put( vertices );
		Quad.fill( vertices,
				right, width, 0, marginTop, inright, outright, outtop, intop );
		quads.put( vertices );

		Quad.fill( vertices,
				0, marginLeft, marginTop, bottom, outleft, inleft, intop, inbottom );
		quads.put( vertices );
		Quad.fill( vertices,
				marginLeft, right, marginTop, bottom, inleft, inright, intop, inbottom );
		quads.put( vertices );
		Quad.fill( vertices,
				right, width, marginTop, bottom, inright, outright, intop, inbottom );
		quads.put( vertices );

		Quad.fill( vertices,
				0, marginLeft, bottom, height, outleft, inleft, inbottom, outbottom );
		quads.put( vertices );
		Quad.fill( vertices,
				marginLeft, right, bottom, height, inleft, inright, inbottom, outbottom );
		quads.put( vertices );
		Quad.fill( vertices,
				right, width, bottom, height, inright, outright, inbottom, outbottom );
		quads.put( vertices );

		dirty = true;
	}
	
	public int marginLeft() {
		return marginLeft;
	}
	
	public int marginRight() {
		return marginRight;
	}
	
	public int marginTop() {
		return marginTop;
	}
	
	public int marginBottom() {
		return marginBottom;
	}
	
	public int marginHor() {
		return marginLeft + marginRight;
	}
	
	public int marginVer() {
		return marginTop + marginBottom;
	}
	
	public float innerWidth() {
		return width - marginLeft - marginRight;
	}
	
	public float innerHeight() {
		return height - marginTop - marginBottom;
	}
	
	public float innerRight() {
		return width - marginRight;
	}
	
	public float innerBottom() {
		return height - marginBottom;
	}

	public void flipHorizontal(boolean value) {
		flipHorizontal = value;
		updateVertices();
	}

	public void flipVertical(boolean value) {
		flipVertical = value;
		updateVertices();
	}
	
	public void size( float width, float height ) {
		this.width = width;
		this.height = height;
		updateVertices();
	}
	
	@Override
	public void draw() {
		
		super.draw();

		if (dirty){
			if (buffer == null)
				buffer = new Vertexbuffer(quads);
			else
				buffer.updateVertices(quads);
			dirty = false;
		}

		NoosaScript script = NoosaScript.get();
		
		texture.bind();
		
		script.camera( camera() );
		
		script.uModel.valueM4( matrix );
		script.lighting(
			rm, gm, bm, am,
			ra, ga, ba, aa );
		
		script.drawQuadSet( buffer, 9, 0 );
		
	}

	@Override
	public void destroy() {
		super.destroy();
		if (buffer != null)
			buffer.delete();
	}
}
