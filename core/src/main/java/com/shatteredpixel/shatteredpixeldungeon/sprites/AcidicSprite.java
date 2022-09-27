/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

public class AcidicSprite extends ScorpioSprite {
	
	public AcidicSprite() {
		super();
		
		texture( Assets.Sprites.SCORPIO );
		
		TextureFilm frames = new TextureFilm( texture, 17, 17 );
		
		idle = new Animation( 12, true );
		idle.frames( frames, 15, 15, 15, 15, 15, 15, 15, 15, 16, 17, 16, 17, 16, 17 );
		
		run = new Animation( 4, true );
		run.frames( frames, 20, 21 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 15, 18, 19 );
		
		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 15, 22, 23, 24, 25 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xFF66FF22;
	}
}
