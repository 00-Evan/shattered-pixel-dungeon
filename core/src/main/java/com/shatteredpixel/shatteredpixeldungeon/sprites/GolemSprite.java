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

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class GolemSprite extends MobSprite {

	private Emitter teleParticles;
	
	public GolemSprite() {
		super();
		
		texture( Assets.Sprites.GOLEM );
		
		TextureFilm frames = new TextureFilm( texture, 17, 19 );
		
		idle = new Animation( 4, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 12, true );
		run.frames( frames, 2, 3, 4, 5 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 6, 7, 8 );

		zap = attack.clone();
		
		die = new Animation( 15, false );
		die.frames( frames, 9, 10, 11, 12, 13 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		teleParticles = emitter();
		teleParticles.autoKill = false;
		teleParticles.pour(ElmoParticle.FACTORY, 0.05f);
		teleParticles.on = false;
	}

	@Override
	public void update() {
		super.update();
		if (teleParticles != null){
			teleParticles.pos( this );
			teleParticles.visible = visible;
		}
	}

	@Override
	public void kill() {
		super.kill();

		if (teleParticles != null) {
			teleParticles.on = false;
		}
	}

	public void teleParticles(boolean value){
		if (teleParticles != null) teleParticles.on = value;
	}

	@Override
	public synchronized void play(Animation anim, boolean force) {
		if (teleParticles != null) teleParticles.on = false;
		super.play(anim, force);
	}

	@Override
	public int blood() {
		return 0xFF80706c;
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				MagicMissile.ELMO,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((Golem)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == die) {
			emitter().burst( ElmoParticle.FACTORY, 4 );
		}
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}
}
