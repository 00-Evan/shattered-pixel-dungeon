/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.sprites;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.mobs.YogFist;
import com.saqfish.spdnet.effects.Beam;
import com.saqfish.spdnet.effects.MagicMissile;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.effects.particles.CorrosionParticle;
import com.saqfish.spdnet.effects.particles.FlameParticle;
import com.saqfish.spdnet.effects.particles.LeafParticle;
import com.saqfish.spdnet.effects.particles.ShadowParticle;
import com.saqfish.spdnet.effects.particles.SparkParticle;
import com.saqfish.spdnet.tiles.DungeonTilemap;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public abstract class FistSprite extends MobSprite {

	private static final float SLAM_TIME	= 0.33f;

	protected int boltType;

	protected abstract int texOffset();

	private Emitter particles;
	protected abstract Emitter createEmitter();

	public FistSprite() {
		super();

		int c = texOffset();

		texture( Assets.Sprites.FISTS );

		TextureFilm frames = new TextureFilm( texture, 24, 17 );

		idle = new Animation( 2, true );
		idle.frames( frames, c+0, c+0, c+1 );

		run = new Animation( 3, true );
		run.frames( frames, c+0, c+1 );

		attack = new Animation( Math.round(1 / SLAM_TIME), false );
		attack.frames( frames, c+0 );

		zap = new Animation( 8, false );
		zap.frames( frames, c+0, c+5, c+6 );

		die = new Animation( 10, false );
		die.frames( frames, c+0, c+2, c+3, c+4 );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );

		if (particles == null) {
			particles = createEmitter();
		}
	}

	@Override
	public void update() {
		super.update();

		if (particles != null){
			particles.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();
		if (particles != null){
			particles.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (particles != null){
			particles.killAndErase();
		}
	}

	@Override
	public void attack( int cell ) {
		super.attack( cell );

		jump(ch.pos, ch.pos, null, 9, SLAM_TIME );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				boltType,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((YogFist)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		if (anim == attack) {
			Camera.main.shake( 4, 0.2f );
		} else if (anim == zap) {
			idle();
		}
	}

	public static class Burning extends FistSprite {

		{
			boltType = MagicMissile.FIRE;
		}

		@Override
		protected int texOffset() {
			return 0;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( FlameParticle.FACTORY, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFFFFDD34;
		}

	}

	public static class Soiled extends FistSprite {

		{
			boltType = MagicMissile.FOLIAGE;
		}

		@Override
		protected int texOffset() {
			return 10;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( LeafParticle.GENERAL, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFF7F5424;
		}

	}

	public static class Rotting extends FistSprite {

		{
			boltType = MagicMissile.TOXIC_VENT;
		}

		@Override
		protected int texOffset() {
			return 20;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour(Speck.factory(Speck.TOXIC), 0.25f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFFB8BBA1;
		}

	}

	public static class Rusted extends FistSprite {

		{
			boltType = MagicMissile.CORROSION;
		}

		@Override
		protected int texOffset() {
			return 30;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour(CorrosionParticle.MISSILE, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFF7F7F7F;
		}

	}

	public static class Bright extends FistSprite {

		{
			boltType = MagicMissile.RAINBOW;
		}

		@Override
		protected int texOffset() {
			return 40;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour(SparkParticle.STATIC, 0.06f );
			return emitter;
		}

		@Override
		public void zap( int cell ) {
			turnTo( ch.pos , cell );
			play( zap );

			((YogFist)ch).onZapComplete();
			parent.add( new Beam.LightRay(center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
		}
		@Override
		public int blood() {
			return 0xFFFFFFFF;
		}

	}

	public static class Dark extends FistSprite {

		{
			boltType = MagicMissile.SHADOW;
		}

		@Override
		protected int texOffset() {
			return 50;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour(ShadowParticle.MISSILE, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFF4A2F53;
		}

	}

}
