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
package com.shatteredpixel.shatteredicepixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;

public class AlbinoSprite extends MobSprite {

	public AlbinoSprite() {
		super();
		
		texture( Assets.RAT );
		
		TextureFilm frames = new TextureFilm( texture, 16, 15 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 16, 16, 16, 17 );
		
		run = new Animation( 10, true );
		run.frames( frames, 22, 23, 24, 25, 26 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 18, 19, 20, 21 );
		
		die = new Animation( 10, false );
		die.frames( frames, 27, 28, 29, 30 );
		
		play( idle );
	}
}
