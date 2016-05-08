/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Random;

public class LeafParticle extends PixelParticle.Shrinking {
	
	public static int color1;
	public static int color2;
	
	
	public static final Emitter.Factory GENERAL = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			LeafParticle p = ((LeafParticle)emitter.recycle( LeafParticle.class ));
			p.color( ColorMath.random( 0x004400, 0x88CC44 ) );
			p.reset( x, y );
		}
	};
	
	public static final Emitter.Factory LEVEL_SPECIFIC = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			LeafParticle p = ((LeafParticle)emitter.recycle( LeafParticle.class ));
			p.color( ColorMath.random( Dungeon.level.color1, Dungeon.level.color2 ) );
			p.reset( x, y );
		}
	};
	
	public LeafParticle() {
		super();
		
		lifespan = 1.2f;
		acc.set( 0, 25 );
	}
	
	public void reset( float x, float y ) {
		revive();
		
		this.x = x;
		this.y = y;
		
		speed.set( Random.Float( -8, +8 ), -20 );
		
		left = lifespan;
		size = Random.Float( 2, 3 );
	}
}