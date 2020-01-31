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

//TODO currently just a recolored monk sprite
public class GhoulSprite extends MobSprite {

	private Animation crumple;
	
	public GhoulSprite() {
		super();
		
		texture( Assets.GHOUL );
		
		TextureFilm frames = new TextureFilm( texture, 15, 14 );
		
		idle = new Animation( 6, true );
		idle.frames( frames, 1, 0, 1, 2 );
		
		run = new Animation( 15, true );
		run.frames( frames, 11, 12, 13, 14, 15, 16 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 3, 4, 3, 4 );

		crumple = new Animation( 15, false);
		crumple.frames( frames, 1, 7, 8 );

		die = new Animation( 15, false );
		die.frames( frames, 7, 8, 8, 9, 10 );
		
		play( idle );
	}

	public void crumple(){
		if (emo != null){
			emo.killAndErase();
			emo = null;
		}
		play(crumple);
	}
	
}
