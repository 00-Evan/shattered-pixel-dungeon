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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class PylonSprite extends MobSprite {

	private Animation activeIdle;

	public PylonSprite() {
		super();

		perspectiveRaise = 5/16f; //1 pixel less
		renderShadow = false;

		texture( Assets.Sprites.PYLON );

		TextureFilm frames = new TextureFilm( texture, 10, 20 );

		idle = new Animation( 1, false );
		idle.frames( frames, 0 );

		activeIdle = new Animation( 1, false );
		activeIdle.frames( frames, 1 );

		run = idle.clone();

		attack = idle.clone();

		die = new Animation( 1, false );
		die.frames( frames, 2 );

		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Pylon && ch.alignment == Char.Alignment.ENEMY){
			activate();
		}
		renderShadow = false;
	}

	@Override
	public void place(int cell) {
		if (parent != null) parent.bringToFront(this);
		super.place(cell);
	}

	public void activate(){
		idle = activeIdle.clone();
		idle();
	}

	@Override
	public void play(Animation anim) {
		if (anim == die){
			turnTo(ch.pos, ch.pos+1); //always face right to merge with custom tiles
			emitter().burst(BlastParticle.FACTORY, 20);
			Sample.INSTANCE.play(Assets.Sounds.BLAST);
		}
		super.play(anim);
	}

	@Override
	public void onComplete(Animation anim) {
		if (anim == attack){
			flash();
		}
		super.onComplete(anim);
	}
}
