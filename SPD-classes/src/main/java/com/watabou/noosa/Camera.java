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

import com.watabou.glwrap.Matrix;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Camera extends Gizmo {

	private static ArrayList<Camera> all = new ArrayList<Camera>();
	
	protected static float invW2;
	protected static float invH2;
	
	public static Camera main;

	public boolean fullScreen;

	public float zoom;
	
	public int x;
	public int y;
	public int width;
	public int height;
	
	int screenWidth;
	int screenHeight;
	
	public float[] matrix;
	
	public PointF scroll;
	public Visual target;
	
	private float shakeMagX		= 10f;
	private float shakeMagY		= 10f;
	private float shakeTime		= 0f;
	private float shakeDuration	= 1f;
	
	protected float shakeX;
	protected float shakeY;
	
	public static Camera reset() {
		return reset( createFullscreen( 1 ) );
	}
	
	public static synchronized Camera reset( Camera newCamera ) {
		
		invW2 = 2f / Game.width;
		invH2 = 2f / Game.height;
		
		int length = all.size();
		for (int i=0; i < length; i++) {
			all.get( i ).destroy();
		}
		all.clear();
		
		return main = add( newCamera );
	}
	
	public static synchronized Camera add( Camera camera ) {
		all.add( camera );
		return camera;
	}
	
	public static synchronized Camera remove( Camera camera ) {
		all.remove( camera );
		return camera;
	}
	
	public static synchronized void updateAll() {
		int length = all.size();
		for (int i=0; i < length; i++) {
			Camera c = all.get( i );
			if (c.exists && c.active) {
				c.update();
			}
		}
	}
	
	public static Camera createFullscreen( float zoom ) {
		int w = (int)Math.ceil( Game.width / zoom );
		int h = (int)Math.ceil( Game.height / zoom );
		Camera c = new Camera(
				(int)(Game.width - w * zoom) / 2,
				(int)(Game.height - h * zoom) / 2,
				w, h, zoom );
		c.fullScreen = true;
		return c;
	}
	
	public Camera( int x, int y, int width, int height, float zoom ) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.zoom = zoom;
		
		screenWidth = (int)(width * zoom);
		screenHeight = (int)(height * zoom);
		
		scroll = new PointF();
		
		matrix = new float[16];
		Matrix.setIdentity( matrix );
	}
	
	@Override
	public void destroy() {
		target = null;
	}
	
	public void zoom( float value ) {
		zoom( value,
			scroll.x + width / 2,
			scroll.y + height / 2 );
	}
	
	public void zoom( float value, float fx, float fy ) {
		
		zoom = value;
		width = (int)(screenWidth / zoom);
		height = (int)(screenHeight / zoom);
		
		focusOn( fx, fy );
	}
	
	public void resize( int width, int height ) {
		this.width = width;
		this.height = height;
		screenWidth = (int)(width * zoom);
		screenHeight = (int)(height * zoom);
	}
	
	@Override
	public void update() {
		super.update();
		
		if (target != null) {
			focusOn( target.x + target.width / 2, target.y + target.height / 2 );
		}
		
		if ((shakeTime -= Game.elapsed) > 0) {
			float damping = shakeTime / shakeDuration;
			shakeX = Random.Float( -shakeMagX, +shakeMagX ) * damping;
			shakeY = Random.Float( -shakeMagY, +shakeMagY ) * damping;
		} else {
			shakeX = 0;
			shakeY = 0;
		}
		
		updateMatrix();
	}
	
	public PointF center() {
		return new PointF( width / 2, height / 2 );
	}
	
	public boolean hitTest( float x, float y ) {
		return x >= this.x && y >= this.y && x < this.x + screenWidth && y < this.y + screenHeight;
	}
	
	public void focusOn( float x, float y ) {
		scroll.set( x - width / 2, y - height / 2 );
	}
	
	public void focusOn( PointF point ) {
		focusOn( point.x, point.y );
	}
	
	public void focusOn( Visual visual ) {
		focusOn( visual.center() );
	}
	
	public PointF screenToCamera( int x, int y ) {
		return new PointF(
			(x - this.x) / zoom + scroll.x,
			(y - this.y) / zoom + scroll.y );
	}
	
	public Point cameraToScreen( float x, float y ) {
		return new Point(
			(int)((x - scroll.x) * zoom + this.x),
			(int)((y - scroll.y) * zoom + this.y));
	}
	
	public float screenWidth() {
		return width * zoom;
	}
	
	public float screenHeight() {
		return height * zoom;
	}
	
	protected void updateMatrix() {

	/*	Matrix.setIdentity( matrix );
		Matrix.translate( matrix, -1, +1 );
		Matrix.scale( matrix, 2f / G.width, -2f / G.height );
		Matrix.translate( matrix, x, y );
		Matrix.scale( matrix, zoom, zoom );
		Matrix.translate( matrix, scroll.x, scroll.y );*/
		
		matrix[0] = +zoom * invW2;
		matrix[5] = -zoom * invH2;
		
		matrix[12] = -1 + x * invW2 - (scroll.x + shakeX) * matrix[0];
		matrix[13] = +1 - y * invH2 - (scroll.y + shakeY) * matrix[5];
		
	}
	
	public void shake( float magnitude, float duration ) {
		shakeMagX = shakeMagY = magnitude;
		shakeTime = shakeDuration = duration;
	}
}
