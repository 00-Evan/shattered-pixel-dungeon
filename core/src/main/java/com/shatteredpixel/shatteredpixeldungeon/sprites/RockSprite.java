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
import com.watabou.noosa.TextureFilm;

public class RockSprite extends MobSprite {
	
	public RockSprite() {
		super();
		
		texture( Assets.Sprites.ROCK );
		
		TextureFilm frames = new TextureFilm( texture, 15, 13 );

		idle = new Animation( 20, true );
		idle.frames( frames, 0 );

		run = new Animation( 20, true );
		run.frames( frames, 0 );

		die = new Animation( 15, false );
		die.frames( frames, 0 ,1 ,1 ,1 ,2 ,2 ,2 ,2 );

		
		play( idle );
	}

	
	@Override
	public void die() {
		super.die();
	}


	
	@Override
	public int blood() {
		return 0x423618;
	}
}
