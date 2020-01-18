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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM300;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class DM300Sprite extends MobSprite {

	private Animation slam;

	private Emitter superchargeSparks;
	
	public DM300Sprite() {
		super();
		
		texture( Assets.DM300 );
		
		TextureFilm frames = new TextureFilm( texture, 22, 20 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 2, 3 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 4, 5, 6 );

		slam = attack.clone();

		zap = attack.clone();

		die = new Animation( 20, false );
		die.frames( frames, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 8 );
		
		play( idle );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				MagicMissile.TOXIC_VENT,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((NewDM300)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.SND_PUFF );
	}

	public void slam( int cell ){
		turnTo( ch.pos , cell );
		play( slam );
		Sample.INSTANCE.play( Assets.SND_ROCKS );
		Camera.main.shake( 3, 0.7f );
	}
	
	@Override
	public void onComplete( Animation anim ) {

		if (anim == zap || anim == slam){
			idle();
		}

		if (anim == slam){
			((NewDM300)ch).onSlamComplete();
		}

		super.onComplete( anim );
		
		if (anim == die) {
			Sample.INSTANCE.play(Assets.SND_BLAST);
			emitter().burst( BlastParticle.FACTORY, 25 );
		}
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		superchargeSparks = emitter();
		superchargeSparks.autoKill = false;
		superchargeSparks.pour(SparkParticle.STATIC, 0.05f);
		superchargeSparks.on = false;

		if (ch instanceof NewDM300 && ((NewDM300) ch).isSupercharged()){
			tint(1, 0, 0, 0.33f);
			superchargeSparks.on = true;
		}
	}

	@Override
	public void update() {
		super.update();

		if (ch instanceof NewDM300){
			superchargeSparks.on = ((NewDM300) ch).isSupercharged();
		}

		if (superchargeSparks != null){
			superchargeSparks.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();
		if (superchargeSparks != null){
			superchargeSparks.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (superchargeSparks != null){
			superchargeSparks.killAndErase();
		}
	}

	@Override
	public void resetColor() {
		super.resetColor();
		if (ch instanceof NewDM300 && ((NewDM300) ch).isSupercharged()){
			tint(1, 0, 0, 0.33f);
		}
	}

	@Override
	public int blood() {
		return 0xFFFFFF88;
	}
}
