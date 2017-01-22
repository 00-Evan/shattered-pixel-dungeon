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

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PoisonParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Visual;
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

	//missile types
	public static final int MAGIC_MISSILE   = 0;
	public static final int FROST           = 1;
	public static final int FIRE            = 2;
	public static final int POISON          = 3;
	public static final int FOLIAGE         = 4;
	public static final int FORCE           = 5;
	public static final int BEACON          = 6;
	public static final int SHADOW          = 7;
	public static final int RAINBOW         = 8;

	public static final int FIRE_CONE       = 100;
	public static final int FOLIAGE_CONE    = 101;
	
	public void reset( int type, int from, int to, Callback callback ) {
		reset( type,
				DungeonTilemap.raisedTileCenterToWorld( from ),
				DungeonTilemap.raisedTileCenterToWorld( to ),
				callback );
	}

	public void reset( int type, Visual from, Visual to, Callback callback ) {
		reset( type,
				from.center(),
				to.center(),
				callback);
	}

	public void reset( int type, Visual from, int to, Callback callback ) {
		reset( type,
				from.center(),
				DungeonTilemap.raisedTileCenterToWorld( to ),
				callback);
	}

	public void reset( int type, PointF from, PointF to, Callback callback ) {
		this.callback = callback;
		
		revive();
		
		x = from.x;
		y = from.y;
		width = 0;
		height = 0;
		
		PointF d = PointF.diff( to, from );
		PointF speed = new PointF( d ).normalize().scale( SPEED );
		sx = speed.x;
		sy = speed.y;
		time = d.length() / SPEED;

		switch(type){
			case MAGIC_MISSILE: default:
				size( 4 );
				pour( WhiteParticle.FACTORY, 0.01f );
				break;
			case FROST:
				pour( MagicParticle.FACTORY, 0.01f );
				break;
			case FIRE:
				size( 4 );
				pour( FlameParticle.FACTORY, 0.01f );
				break;
			case POISON:
				size( 3 );
				pour( PoisonParticle.MISSILE, 0.01f );
				break;
			case FOLIAGE:
				size( 4 );
				pour( LeafParticle.GENERAL, 0.01f );
				break;
			case FORCE:
				pour( SlowParticle.FACTORY, 0.01f );
				break;
			case BEACON:
				pour( ForceParticle.FACTORY, 0.01f );
				break;
			case SHADOW:
				size( 4 );
				pour( ShadowParticle.MISSILE, 0.01f );
				break;
			case RAINBOW:
				size( 4 );
				pour( RainbowParticle.BURST, 0.01f );
				break;

			case FIRE_CONE:
				size( 10 );
				pour( FlameParticle.FACTORY, 0.03f );
				break;
			case FOLIAGE_CONE:
				size( 10 );
				pour( LeafParticle.GENERAL, 0.03f );
				break;
		}
	}
	
	public void size( float size ) {
		x -= size / 2;
		y -= size / 2;
		width = height = size;
	}

	//convenience method for the common case of a bolt going from a character to a tile or enemy
	public static void boltFromChar(Group group, int type, Visual sprite, int to, Callback callback){
		MagicMissile missile = ((MagicMissile)group.recycle( MagicMissile.class ));
		if (Actor.findChar(to) != null){
			missile.reset(type, sprite, Actor.findChar(to).sprite, callback);
		} else {
			missile.reset(type, sprite, to, callback);
		}
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

		public static final Emitter.Factory ATTRACTING = new Factory() {
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				((MagicParticle)emitter.recycle( MagicParticle.class )).resetAttract( x, y );
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

		public void resetAttract( float x, float y) {
			revive();

			//size = 8;
			left = lifespan;

			speed.polar( Random.Float( PointF.PI2 ), Random.Float( 16, 32 ) );
			this.x = x - speed.x * lifespan;
			this.y = y - speed.y * lifespan;
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
