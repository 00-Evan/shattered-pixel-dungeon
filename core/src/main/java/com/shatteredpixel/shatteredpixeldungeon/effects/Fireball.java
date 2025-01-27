/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class Fireball extends MovieClip {

	private static boolean second = false;

	public Fireball() {
		this(second);
		second = !second;
	}

	public Fireball(boolean second) {

		if (PixelScene.landscape()){
			texture( "effects/fireball-tall.png" );
			TextureFilm frames = new TextureFilm( texture, 61, 61 );
			MovieClip.Animation anim = new MovieClip.Animation( 24, true );
			anim.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 );
			play( anim );
		} else {
			texture( "effects/fireball-short.png" );
			TextureFilm frames = new TextureFilm( texture, 47, 47 );
			MovieClip.Animation anim = new MovieClip.Animation( 24, true );
			anim.frames( frames, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 );
			play( anim );
		}

		//second fireball is flipped and has its animation offset
		if (second){
			flipHorizontal =  true;
			curFrame = 12;
			frame( curAnim.frames[curFrame] );
		}

	}
}
