/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GnollGeomancer;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.ColorMath;

public class GnollGeomancerSprite extends MobSprite {

	private Animation statue;

	public GnollGeomancerSprite() {
		super();

		texture(Assets.Sprites.GNOLL_GEOMANCER);

		TextureFilm frames = new TextureFilm( texture, 12, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 1, 1, 1, 2, 1, 1, 2, 2 );

		run = new Animation( 12, true );
		run.frames( frames, 5, 6, 7, 8 );

		attack = new Animation( 12, false );
		attack.frames( frames, 3, 4, 1 );

		zap = attack.clone();

		die = new Animation( 12, false );
		die.frames( frames, 9, 10, 11 );

		statue = new Animation(1, true);
		statue.frames( frames, 0 );

		play(idle);

		scale.set(1.25f);
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	@Override
	public void idle() {
		super.idle();
		if (ch != null && ch.buff(GnollGeomancer.RockArmor.class) != null){
			play( statue );
		}
	}

	@Override
	public int blood() {
		return curAnim == statue ? ColorMath.random( 0x444444, 0x777766 ) : super.blood();
	}
}
