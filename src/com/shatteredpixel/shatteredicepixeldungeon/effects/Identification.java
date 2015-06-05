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
package com.shatteredpixel.shatteredicepixeldungeon.effects;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;

import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Identification extends Group {

	private static int[] DOTS = {
		-1, -3,
		 0, -3,
		+1, -3,
		-1, -2,
		+1, -2,
		+1, -1,
		 0,  0,
		+1,  0,
		 0, +1,
		 0, +3
	};
	
	public Identification( PointF p ) {
		
		for (int i=0; i < DOTS.length; i += 2) {
			add( new Speck( p.x, p.y, DOTS[i], DOTS[i+1] ) );
			add( new Speck( p.x, p.y, DOTS[i], DOTS[i+1] ) );
		}
	}
	
	@Override
	public void update() {
		super.update();
		if (countLiving() == 0) {
			killAndErase();
		}
	}
	
	@Override
	public void draw() {
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
		super.draw();
		GLES20.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
	}
	
	public static class Speck extends PixelParticle {

		private static final int COLOR = 0x4488CC;
		private static final int SIZE = 3;
		
		public Speck( float x0, float y0, int mx, int my ) {
			
			super();
			color( COLOR );
			
			float x1 = x0 + mx * SIZE;
			float y1 = y0 + my * SIZE;
			
			PointF p = new PointF().polar( Random.Float( 2 * PointF.PI ), 8 );
			x0 += p.x;
			y0 += p.y;
			
			float dx = x1 - x0;
			float dy = y1 - y0;
			
			x = x0;
			y = y0;
			speed.set( dx, dy );
			acc.set( -dx / 4, -dy / 4 );
			
			left = lifespan = 2f;
		}
		
		@Override
		public void update() {
			super.update();
			
			am = 1 - Math.abs( left / lifespan - 0.5f ) * 2;
			am *= am;
			size( am * SIZE );
		}
	}
}
