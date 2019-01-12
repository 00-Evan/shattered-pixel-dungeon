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

public class Flare extends Visual {
	
	private float duration = 0;
	private float lifespan;
	
	private boolean lightMode = true;
	
	private SmartTexture texture;
	
	private FloatBuffer vertices;
	private ShortBuffer indices;
	
	private int nRays;
	
	public Flare( int nRays, float radius ) {
		
		super( 0, 0, 0, 0 );

		int gradient[] = {0xFFFFFFFF, 0x00FFFFFF};
		texture = TextureCache.createGradient( gradient );
		
		this.nRays = nRays;
		
		angle = 45;
		angularSpeed = 180;
		
		vertices = ByteBuffer.
			allocateDirect( (nRays * 2 + 1) * 4 * (Float.SIZE / 8) ).
			order( ByteOrder.nativeOrder() ).
			asFloatBuffer();
		
		indices = ByteBuffer.
			allocateDirect( nRays * 3 * Short.SIZE / 8 ).
			order( ByteOrder.nativeOrder() ).
			asShortBuffer();
		
		float v[] = new float[4];
		
		v[0] = 0;
		v[1] = 0;
		v[2] = 0.25f;
		v[3] = 0;
		vertices.put( v );
		
		v[2] = 0.75f;
		v[3] = 0;
		
		for (int i=0; i < nRays; i++) {
			
			float a = i * 3.1415926f * 2 / nRays;
			v[0] = (float)Math.cos( a ) * radius;
			v[1] = (float)Math.sin( a ) * radius;
			vertices.put( v );
			
			a += 3.1415926f * 2 / nRays / 2;
			v[0] = (float)Math.cos( a ) * radius;
			v[1] = (float)Math.sin( a ) * radius;
			vertices.put( v );
			
			indices.put( (short)0 );
			indices.put( (short)(1 + i * 2) );
			indices.put( (short)(2 + i * 2) );
		}
		
		indices.position( 0 );
	}
	
	public Flare color( int color, boolean lightMode ) {
		this.lightMode = lightMode;
		hardlight( color );
		
		return this;
	}
	
	public Flare show( Visual visual, float duration ) {
		point( visual.center() );
		visual.parent.addToBack( this );
		
		lifespan = this.duration = duration;
		if (lifespan > 0) scale.set( 0 );
		
		return this;
	}

	public Flare show( Group parent, PointF pos, float duration ) {
		point( pos );
		parent.add( this );

		lifespan = this.duration = duration;
		if (lifespan > 0) scale.set( 0 );

		return this;
	}

	@Override
	public void update() {
		super.update();
		
		if (duration > 0) {
			if ((lifespan -= Game.elapsed) > 0) {
				
				float p = 1 - lifespan / duration;	// 0 -> 1
				p =  p < 0.25f ? p * 4 : (1 - p) * 1.333f;
				scale.set( p );
				alpha( p );
				
			} else {
				killAndErase();
			}
		}
	}
	
	@Override
	public void draw() {
		
		super.draw();
		
		if (lightMode) {
			Blending.setLightMode();
			drawRays();
			Blending.setNormalMode();
		} else {
			drawRays();
		}
	}
	
	private void drawRays() {
		
		NoosaScript script = NoosaScript.get();
		
		texture.bind();
		
		script.uModel.valueM4( matrix );
		script.lighting(
			rm, gm, bm, am,
			ra, ga, ba, aa );
		
		script.camera( camera );
		script.drawElements( vertices, indices, nRays * 3 );
	}
}
