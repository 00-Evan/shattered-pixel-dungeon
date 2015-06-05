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
import com.shatteredpixel.shatteredicepixeldungeon.effects.Splash;

public class LarvaSprite extends MobSprite {
	
	public LarvaSprite() {
		super();
		
		texture( Assets.LARVA );
		
		TextureFilm frames = new TextureFilm( texture, 12, 8 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 4, 4, 4, 4, 4, 5, 5 );
		
		run = new Animation( 12, true );
		run.frames( frames, 0, 1, 2, 3 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 6, 5, 7 );
		
		die = new Animation( 10, false );
		die.frames( frames, 8 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0xbbcc66;
	}
	
	@Override
	public void die() {
		Splash.at( center(), blood(), 10 );
		super.die();
	}
}
