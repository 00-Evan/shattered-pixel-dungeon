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
package com.shatteredpixel.shatteredpixeldungeon.effects;

import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class MagicMissile extends Emitter {

	private static final float SPEED	= 200f;
	
	private Callback callback;
	
	private float sx;
	private float sy;
	private float time;
	
	public void reset( int from, int to, Callback callback ) {
		reset( from, to, SPEED, callback );
	}

	public void reset( int from, int to, float velocity, Callback callback ) {
		this.callback = callback;
		
		revive();
		
		PointF pf = DungeonTilemap.tileCenterToWorld( from );
		PointF pt = DungeonTilemap.tileCenterToWorld( to );
		
		x = pf.x;
		y = pf.y;
		width = 0;
		height = 0;
		
		PointF d = PointF.diff( pt, pf );
		PointF speed = new PointF( d ).normalize().scale( velocity );
		sx = speed.x;
		sy = speed.y;
		time = d.length() / velocity;
	}
	
	public void size( float size ) {
		x -= size / 2;
		y -= size / 2;
		width = height = size;
	}
	
	public static void blueLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.pour( MagicParticle.FACTORY, 0.01f );
	}
	
	public static void fire( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 10 );
		missile.pour( FlameParticle.FACTORY, 0.03f );
	}
	
	public static void earth( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 2 );
		missile.pour( EarthParticle.FACTORY, 0.01f );
	}
	
	public static void purpleLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 2 );
		missile.pour( PurpleParticle.MISSILE, 0.01f );
	}
	
	public static void whiteLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( WhiteParticle.FACTORY, 0.01f );
	}
	
	public static void wool( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 3 );
		missile.pour( WoolParticle.FACTORY, 0.01f );
	}
	
	public static void poison( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 3 );
		missile.pour( PoisonParticle.MISSILE, 0.01f );
	}
	
	public static void foliage( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 10 );
		missile.pour( LeafParticle.GENERAL, 0.03f );
	}
	
	public static void slowness( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.pour( SlowParticle.FACTORY, 0.01f );
	}
	
	public static void force( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 0 );
		missile.pour( ForceParticle.FACTORY, 0.01f );
	}
	
	public static void coldLight( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( ColdParticle.FACTORY, 0.01f );
	}
	
	public static void shadow( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( ShadowParticle.MISSILE, 0.01f );
	}

	public static void rainbow( Group group, int from, int to, Callback callback ) {
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		missile.reset( from, to, callback );
		missile.size( 4 );
		missile.pour( RainbowParticle.BURST, 0.01f );
	}
	
	@Override
	public void update() {
		super.update();
		if (on) {
			float d = Game.elapsed;
			x += sx * d;
			y += sy * d;
			if ((time -= d) <= 0) {
				on = false;
				if (callback != null ) callback.call();
			}
		}
	}
	
	public static class MagicParticle extends PixelParticle {
		
		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((MagicParticle)emitter.recycle( MagicParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			};
		};
		
		public MagicParticle() {
			super();
			
			color( 0x88CCFF );
			lifespan = 0.5f;
			
			speed.set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
		}
		
		@Override
		public void update() {
			super.update();
			// alpha: 1 -> 0; size: 1 -> 4
			size( 4 - (am = left / lifespan) * 3 );
		}
	}
	
	public static class EarthParticle extends PixelParticle.Shrinking {
		
		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((EarthParticle)emitter.recycle( EarthParticle.class )).reset( x, y );
			}
		};
		
		public EarthParticle() {
			super();
			
			lifespan = 0.5f;
			
			color( ColorMath.random( 0x555555, 0x777766 ) );
			
			acc.set( 0, +40 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			size = 4;
			
			speed.set( Random.Float( -10, +10 ), Random.Float( -10, +10 ) );
		}
	}
	
	public static class WhiteParticle extends PixelParticle {
		
		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((WhiteParticle)emitter.recycle( WhiteParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			};
		};
		
		public WhiteParticle() {
			super();
			
			lifespan = 0.4f;
			
			am = 0.5f;
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
		}
		
		@Override
		public void update() {
			super.update();
			// size: 3 -> 0
			size( (left / lifespan) * 3 );
		}
	}

	public static class SlowParticle extends PixelParticle {
		
		private Emitter emitter;
		
		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((SlowParticle)emitter.recycle( SlowParticle.class )).reset( x, y, emitter );
			}
			@Override
			public boolean lightMode() {
				return true;
			};
		};
		
		public SlowParticle() {
			super();
			
			lifespan = 0.6f;
			
			color( 0x664422 );
			size( 2 );
		}
		
		public void reset( float x, float y, Emitter emitter ) {
			revive();
			
			this.x = x;
			this.y = y;
			this.emitter = emitter;
			
			left = lifespan;
			
			acc.set( 0 );
			speed.set( Random.Float( -20, +20 ), Random.Float( -20, +20 ) );
		}
		
		@Override
		public void update() {
			super.update();
			
			am = left / lifespan;
			acc.set( (emitter.x - x) * 10, (emitter.y - y) * 10 );
		}
	}

	public static class ForceParticle extends PixelParticle.Shrinking {

		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((ForceParticle)emitter.recycle( ForceParticle.class )).reset( index, x, y );
			}
		};

		public void reset( int index, float x, float y ) {
			super.reset( x, y, 0xFFFFFF, 8, 0.5f );

			speed.polar( PointF.PI2 / 8 * index, 12 );
			this.x -= speed.x * lifespan;
			this.y -= speed.y * lifespan;
		}

		@Override
		public void update() {
			super.update();

			am = (1 - left / lifespan) / 2;
		}
	}
	
	public static class ColdParticle extends PixelParticle.Shrinking {
		
		public static final Emitter.Factory FACTORY = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((ColdParticle)emitter.recycle( ColdParticle.class )).reset( x, y );
			}
			@Override
			public boolean lightMode() {
				return true;
			};
		};
		
		public ColdParticle() {
			super();
			
			lifespan = 0.6f;
			
			color( 0x2244FF );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			size = 8;
		}
		
		@Override
		public void update() {
			super.update();
			
			am = 1 - left / lifespan;
		}
	}
}
