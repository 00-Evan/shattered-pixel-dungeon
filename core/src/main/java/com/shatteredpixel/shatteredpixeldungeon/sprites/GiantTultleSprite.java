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
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class GiantTultleSprite extends MobSprite {

	private MovieClip.Animation  defensing;

	public GiantTultleSprite() {
		super();
		tint( 0.0f, 1.0f, 0.0f ,0.6f);//绿色

		texture( Assets.Sprites.CRAB );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		idle = new Animation( 5, true );
		idle.frames( frames, 0, 1, 0, 2 );

		run = new Animation( 15, true );
		run.frames( frames, 3, 4, 5, 6 );

		attack = new Animation( 12, false );
		attack.frames( frames, 7, 8, 9 );

		defensing = new Animation( 12, false );
		defensing.frames( frames, 10, 11, 12, 13 );

		die = new Animation( 12, false );
		die.frames( frames, 10, 11, 12, 13 );

		play( idle );
	}

	//@Override
	//public int blood() {return 0xFFFFEA80;}	//血液黄色

	public void defense( int pos ){
		turnTo(ch.pos, pos);
		play( defensing );
		if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
	}


	@Override
	public void resetColor(){
		super.resetColor( );
		tint( 0.2f, 1.0f, 0.2f ,0.6f );
	}


}
