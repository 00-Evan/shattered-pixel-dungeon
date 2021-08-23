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

package com.shatteredpixel.shatteredpixeldungeon.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class EarthParticle extends PixelParticle {
	
	public static final Emitter.Factory FACTORY = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((EarthParticle)emitter.recycle( EarthParticle.class )).reset( x,  y );
		}
	};

	public static final Emitter.Factory FALLING = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((EarthParticle)emitter.recycle( EarthParticle.class )).resetFalling( x,  y );
		}
	};

	public EarthParticle() {
		super();
		
		color( ColorMath.random( 0x444444, 0x777766 ) );
		angle = Random.Float( -30, 30 );
	}
	
	public void reset( float x, float y ) {
		revive();
		
		this.x = x;
		this.y = y;

		left = lifespan = 0.5f;
		size = 16;
	}

	public void resetFalling( float x, float y ) {
		reset(x, y);

		left = lifespan = 1f;
		size = 8;

		acc.y = 30;
		speed.y = -5;
		angularSpeed = Random.Float(-90, 90);
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = left / lifespan;
		size( (p < 0.5f ? p : 1 - p) * size );
	}
}