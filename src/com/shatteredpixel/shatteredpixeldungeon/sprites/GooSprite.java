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
package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class GooSprite extends MobSprite {
	
	private Animation pump;
	private Animation pumpAttack;

	public GooSprite() {
		super();
		
		texture( Assets.GOO );
		
		TextureFilm frames = new TextureFilm( texture, 20, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 2, 1, 0, 0, 1 );
		
		run = new Animation( 15, true );
		run.frames( frames, 3, 2, 1, 2 );
		
		pump = new Animation( 20, true );
		pump.frames( frames, 4, 3, 2, 1, 0 );

		pumpAttack = new Animation ( 20, false );
		pumpAttack.frames( frames, 4, 3, 2, 1, 0, 7);

		attack = new Animation( 10, false );
		attack.frames( frames, 8, 9, 10 );
		
		die = new Animation( 10, false );
		die.frames( frames, 5, 6, 7 );
		
		play( idle );
	}
	
	public void pumpUp() {
		play( pump );
	}

	public void pumpAttack() { play( pumpAttack ); }

	@Override
	public void play( Animation anim, boolean force ) {
		super.play( anim, force );
	}

	@Override
	public int blood() {
		return 0xFF000000;
	}

	public static class GooParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((GooParticle)emitter.recycle( GooParticle.class )).reset( x, y );
			}
		};

		public GooParticle() {
			super();

			color( 0x000000 );
			lifespan = 0.3f;

			acc.set( 0, +50 );
		}

		public void reset( float x, float y ) {
			revive();

			this.x = x;
			this.y = y;

			left = lifespan;

			size = 4;
			speed.polar( -Random.Float( PointF.PI ), Random.Float( 32, 48 ) );
		}

		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.5f ? (1 - p) * 2f : 1;
		}
	}

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete(anim);

		if (anim == pumpAttack) {

			idle();
			ch.onAttackComplete();
		}
	}
}
