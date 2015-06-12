/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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

import com.watabou.noosa.TextureFilm;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;

public class ImpSprite extends MobSprite {
	
	public ImpSprite() {
		super();
		
		texture( Assets.IMP );
		
		TextureFilm frames = new TextureFilm( texture, 12, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( frames,
			0, 1, 2, 3, 0, 1, 2, 3, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
			0, 1, 2, 3, 0, 1, 2, 3, 0, 1, 3, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4 );
		
		run = new Animation( 20, true );
		run.frames( frames, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 3, 2, 1, 0, 3, 2, 1, 0 );
		
		play( idle );
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		
		if (ch instanceof Imp) {
			alpha( 0.4f );
		}
	}
	
	@Override
	public void onComplete( Animation anim ) {
		if (anim == die) {
			
			emitter().burst( Speck.factory( Speck.WOOL ), 15 );
			killAndErase();
			
		} else {
			super.onComplete( anim );
		}
	}
}
