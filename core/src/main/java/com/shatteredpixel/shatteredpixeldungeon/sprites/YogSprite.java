/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.watabou.noosa.TextureFilm;

public class YogSprite extends MobSprite {
	
	public YogSprite() {
		super();

		perspectiveRaise = 0.2f;

		texture( Assets.YOG );
		
		TextureFilm frames = new TextureFilm( texture, 20, 19 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 2, 2, 1, 0, 3, 4, 4, 3, 0, 5, 6, 6, 5 );
		
		run = new Animation( 12, true );
		run.frames( frames, 0 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 7, 8, 9 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		renderShadow = false;
	}

	@Override
	public void die() {
		super.die();
		
		Splash.at( center(), blood(), 12 );
	}
}
