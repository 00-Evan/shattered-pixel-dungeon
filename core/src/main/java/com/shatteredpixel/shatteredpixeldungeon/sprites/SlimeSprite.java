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

public class SlimeSprite extends MobSprite {
	
	public SlimeSprite() {
		super();
		
		texture( Assets.Sprites.SLIME );
		
		TextureFilm frames = new TextureFilm( texture, 14, 12 );
		
		idle = new Animation( 3, true );
		idle.frames( frames, 0, 1, 1, 0 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 2, 3, 3, 2, 0 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 2, 3, 4, 6, 5 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 5, 6, 7 );
		
		play(idle);
	}
	
}
