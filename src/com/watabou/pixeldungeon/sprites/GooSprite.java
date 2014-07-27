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
package com.watabou.pixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.pixeldungeon.Assets;

public class GooSprite extends MobSprite {
	
	private Animation pump;
	
	public GooSprite() {
		super();
		
		texture( Assets.GOO );
		
		TextureFilm frames = new TextureFilm( texture, 20, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 1 );
		
		pump = new Animation( 20, true );
		pump.frames( frames, 0, 1 );
		
		attack = new Animation( 10, false );
		attack.frames( frames, 5, 0, 6 );
		
		die = new Animation( 10, false );
		die.frames( frames, 2, 3, 4 );
		
		play( idle );
	}
	
	public void pumpUp() {
		play( pump );
	}
	
	@Override
	public int blood() {
		return 0xFF000000;
	}
}
