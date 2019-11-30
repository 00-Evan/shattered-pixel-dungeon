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
import com.watabou.noosa.TextureFilm;

//TODO currently just a reskinned warlock sprite
public class RipperSprite extends MobSprite {

	public RipperSprite() {
		super();

		texture( Assets.RIPPER );

		TextureFilm frames = new TextureFilm( texture, 12, 15 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

		run = new Animation( 15, true );
		run.frames( frames, 0, 2, 3, 4 );

		//TODO shoudl probably have 2 attack animations, like monks
		attack = new Animation( 12, false );
		attack.frames( frames, 0, 5, 6 );

		zap = attack.clone();

		die = new Animation( 15, false );
		die.frames( frames, 0, 7, 8, 8, 9, 10 );

		play( idle );
	}

}
