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

//TODO placeholder graphics atm
public class NecromancerSprite extends MobSprite {
	
	private Animation charging;
	
	public NecromancerSprite(){
		super();
		
		texture( Assets.NECRO );
		TextureFilm film = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( film, 0, 2, 3, 4, 0 );
		
		zap = new Animation( 5, false );
		zap.frames( film, 5, 1 );
		
		charging = new Animation( 5, true );
		charging.frames( film, 1, 5 );
		
		die = new Animation( 10, false );
		die.frames( film, 6, 7, 8, 9 );
		
		attack = zap.clone();
		
		idle();
	}
	
	public void charge( int pos ){
		turnTo(ch.pos, pos);
		play(charging);
	}
	
	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);
		if (anim == zap){
			idle();
		}
	}
}
