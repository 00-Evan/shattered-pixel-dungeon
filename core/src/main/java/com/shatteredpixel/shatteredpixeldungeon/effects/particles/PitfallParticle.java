/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Random;

public class PitfallParticle extends PixelParticle.Shrinking {

	public static final Emitter.Factory FACTORY4 = new Emitter.Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((PitfallParticle)emitter.recycle( PitfallParticle.class )).reset( x,  y, 4 );
		}
	};

	public static final Emitter.Factory FACTORY8 = new Emitter.Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((PitfallParticle)emitter.recycle( PitfallParticle.class )).reset( x,  y, 8 );
		}
	};

	public PitfallParticle(){
		super();

		color( 0x000000 );
		angle = Random.Float( -30, 30 );

	}

	public void reset( float x, float y, int size ) {
		revive();

		this.x = x;
		this.y = y;

		left = lifespan = 1f;

		this.size = size;
	}

}
