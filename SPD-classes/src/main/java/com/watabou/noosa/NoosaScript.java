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

import android.opengl.GLES20;
import android.os.Build;

import com.watabou.glscripts.Script;
import com.watabou.glwrap.Attribute;
import com.watabou.glwrap.FroyoGLES20Fix;
import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Uniform;
import com.watabou.glwrap.Vertexbuffer;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class NoosaScript extends Script {
	
	public Uniform uCamera;
	public Uniform uModel;
	public Uniform uTex;
	public Uniform uColorM;
	public Uniform uColorA;
	public Attribute aXY;
	public Attribute aUV;
	
	private Camera lastCamera;
	
	public NoosaScript() {

		super();
		compile( shader() );
		
		uCamera	= uniform( "uCamera" );
		uModel	= uniform( "uModel" );
		uTex	= uniform( "uTex" );
		uColorM	= uniform( "uColorM" );
		uColorA	= uniform( "uColorA" );
		aXY		= attribute( "aXYZW" );
		aUV		= attribute( "aUV" );

		Quad.setupIndices();
		Quad.bindIndices();
		
	}
	
	@Override
	public void use() {
		
		super.use();
		
		aXY.enable();
		aUV.enable();
		
	}

	public void drawElements( FloatBuffer vertices, ShortBuffer indices, int size ) {
		
		vertices.position( 0 );
		aXY.vertexPointer( 2, 4, vertices );
		
		vertices.position( 2 );
		aUV.vertexPointer( 2, 4, vertices );

		Quad.releaseIndices();
		GLES20.glDrawElements( GLES20.GL_TRIANGLES, size, GLES20.GL_UNSIGNED_SHORT, indices );
		Quad.bindIndices();
	}

	public void drawQuad( FloatBuffer vertices ) {
		
		vertices.position( 0 );
		aXY.vertexPointer( 2, 4, vertices );
		
		vertices.position( 2 );
		aUV.vertexPointer( 2, 4, vertices );

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			GLES20.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE, GLES20.GL_UNSIGNED_SHORT, 0 );
		} else {
			FroyoGLES20Fix.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE, GLES20.GL_UNSIGNED_SHORT, 0 );
		}
		
	}

	public void drawQuad( Vertexbuffer buffer ) {

		buffer.updateGLData();

		buffer.bind();

		aXY.vertexBuffer( 2, 4, 0 );
		aUV.vertexBuffer( 2, 4, 2 );

		buffer.release();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			GLES20.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE, GLES20.GL_UNSIGNED_SHORT, 0 );
		} else {
			FroyoGLES20Fix.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE, GLES20.GL_UNSIGNED_SHORT, 0 );
		}
	}
	
	public void drawQuadSet( FloatBuffer vertices, int size ) {
		
		if (size == 0) {
			return;
		}
		
		vertices.position( 0 );
		aXY.vertexPointer( 2, 4, vertices );
		
		vertices.position( 2 );
		aUV.vertexPointer( 2, 4, vertices );

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			GLES20.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE * size, GLES20.GL_UNSIGNED_SHORT, 0 );
		} else {
			FroyoGLES20Fix.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE * size, GLES20.GL_UNSIGNED_SHORT, 0 );
		}
		
	}

	public void drawQuadSet( Vertexbuffer buffer, int length, int offset ){

		if (length == 0) {
			return;
		}

		buffer.updateGLData();

		buffer.bind();

		aXY.vertexBuffer( 2, 4, 0 );
		aUV.vertexBuffer( 2, 4, 2 );

		buffer.release();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			GLES20.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE * length, GLES20.GL_UNSIGNED_SHORT, Quad.SIZE * Short.SIZE/8 * offset );
		} else {
			FroyoGLES20Fix.glDrawElements( GLES20.GL_TRIANGLES, Quad.SIZE * length, GLES20.GL_UNSIGNED_SHORT, Quad.SIZE * Short.SIZE/8 * offset );
		}
	}
	
	public void lighting( float rm, float gm, float bm, float am, float ra, float ga, float ba, float aa ) {
		uColorM.value4f( rm, gm, bm, am );
		uColorA.value4f( ra, ga, ba, aa );
	}
	
	public void resetCamera() {
		lastCamera = null;
	}
	
	public void camera( Camera camera ) {
		if (camera == null) {
			camera = Camera.main;
		}
		if (camera != lastCamera && camera.matrix != null) {
			lastCamera = camera;
			uCamera.valueM4( camera.matrix );

			if (!camera.fullScreen) {
				GLES20.glEnable( GLES20.GL_SCISSOR_TEST );
				GLES20.glScissor(
						camera.x,
						Game.height - camera.screenHeight - camera.y,
						camera.screenWidth,
						camera.screenHeight);
			} else {
				GLES20.glDisable( GLES20.GL_SCISSOR_TEST );
			}
		}
	}
	
	public static NoosaScript get() {
		return Script.use( NoosaScript.class );
	}
	
	
	protected String shader() {
		return SHADER;
	}
	
	private static final String SHADER =
		
		"uniform mat4 uCamera;" +
		"uniform mat4 uModel;" +
		"attribute vec4 aXYZW;" +
		"attribute vec2 aUV;" +
		"varying vec2 vUV;" +
		"void main() {" +
		"  gl_Position = uCamera * uModel * aXYZW;" +
		"  vUV = aUV;" +
		"}" +
		
		"//\n" +

		"varying mediump vec2 vUV;" +
		"uniform lowp sampler2D uTex;" +
		"uniform lowp vec4 uColorM;" +
		"uniform lowp vec4 uColorA;" +
		"void main() {" +
		"  gl_FragColor = texture2D( uTex, vUV ) * uColorM + uColorA;" +
		"}";
}
