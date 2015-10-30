/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GuardSprite extends MobSprite {

	public GuardSprite() {
		super();

		texture( Assets.GUARD );

		TextureFilm frames = new TextureFilm( texture, 12, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

		run = new MovieClip.Animation( 15, true );
		run.frames( frames, 2, 3, 4, 5, 6, 7 );

		attack = new MovieClip.Animation( 12, false );
		attack.frames( frames, 8, 9, 10 );

		die = new MovieClip.Animation( 8, false );
		die.frames( frames, 11, 12, 13, 14 );

		play( idle );
	}

	@Override
	public void play( Animation anim ) {
		if (anim == die) {
			emitter().burst( ShadowParticle.UP, 4 );
		}
		super.play( anim );
	}
}