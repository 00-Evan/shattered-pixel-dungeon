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

package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.Visual;
import com.watabou.utils.PointF;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class CircleArc extends Visual {
	
	private float duration = 0;
	private float lifespan;
	
	//1f is an entire 360 degree sweep
	private float sweep;
	private boolean dirty;
	
	private boolean lightMode = true;
	
	private SmartTexture texture;
	
	private FloatBuffer vertices;
	private ShortBuffer indices;
	
	private int nTris;
	private float rad;
	
	//more triangles means a more precise visual
	public CircleArc( int triangles, float radius ) {
		
		super( 0, 0, 0, 0 );

		texture = TextureCache.createSolid( 0xFFFFFFFF );
		
		this.nTris = triangles;
		this.rad = radius;
		
		vertices = ByteBuffer.
				allocateDirect( (nTris * 2 + 1) * 4 * (Float.SIZE / 8) ).
				order( ByteOrder.nativeOrder() ).
				asFloatBuffer();
		
		indices = ByteBuffer.
				allocateDirect( nTris * 3 * Short.SIZE / 8 ).
				order( ByteOrder.nativeOrder() ).
				asShortBuffer();
		
		sweep = 1f;
		updateTriangles();
	}
	
	public CircleArc color( int color, boolean lightMode ) {
		this.lightMode = lightMode;
		hardlight( color );
		
		return this;
	}
	
	public CircleArc show( Visual visual, float duration ) {
		point( visual.center() );
		visual.parent.addToBack( this );
		
		lifespan = this.duration = duration;
		
		return this;
	}
	
	public CircleArc show(Group parent, PointF pos, float duration ) {
		point( pos );
		parent.add( this );
		
		lifespan = this.duration = duration;
		
		return this;
	}
	
	public void setSweep( float sweep ){
		this.sweep = sweep;
		dirty = true;
	}
	
	private void updateTriangles(){
		
		dirty = false;
		float v[] = new float[4];
		
		indices.position( 0 );
		vertices.position( 0 );
		
		v[0] = 0;
		v[1] = 0;
		v[2] = 0.25f;
		v[3] = 0;
		vertices.put( v );
		
		v[2] = 0.75f;
		v[3] = 0;
		
		//starting position is very top by default, use angle to adjust this.
		double start = 2 * (Math.PI - Math.PI*sweep) - Math.PI/2.0;
		
		for (int i = 0; i < nTris; i++) {
			
			double a = start + i * Math.PI * 2 / nTris * sweep;
			v[0] = (float)Math.cos( a ) * rad;
			v[1] = (float)Math.sin( a ) * rad;
			vertices.put( v );
			
			a += 3.1415926f * 2 / nTris * sweep;
			v[0] = (float)Math.cos( a ) * rad;
			v[1] = (float)Math.sin( a ) * rad;
			vertices.put( v );
			
			indices.put( (short)0 );
			indices.put( (short)(1 + i * 2) );
			indices.put( (short)(2 + i * 2) );
		}
		
		indices.position( 0 );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (duration > 0) {
			if ((lifespan -= Game.elapsed) > 0) {
				sweep = lifespan/duration;
				dirty = true;
			
			} else {
				killAndErase();
			}
		}
	}
	
	@Override
	public void draw() {
		
		super.draw();
		
		if (dirty) {
			updateTriangles();
		}
		
		if (lightMode) Blending.setLightMode();
		
		NoosaScript script = NoosaScript.get();
		
		texture.bind();
		
		script.uModel.valueM4( matrix );
		script.lighting(
				rm, gm, bm, am,
				ra, ga, ba, aa );
		
		script.camera( camera );
		script.drawElements( vertices, indices, nTris * 3 );
		
		if (lightMode) Blending.setNormalMode();
	}
}
