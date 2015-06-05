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

public class StatueSprite extends MobSprite {
	
	public StatueSprite() {
		super();
		
		texture( Assets.STATUE );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 1, 1 );
		
		run = new Animation( 15, true );
		run.frames( frames, 2, 3, 4, 5, 6, 7 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 8, 9, 10 );
		
		die = new Animation( 5, false );
		die.frames( frames, 11, 12, 13, 14, 15, 15 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xFFcdcdb7;
	}
}
