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
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Lightning;

public class ShamanSprite extends MobSprite {
	
	private int[] points = new int[2];
	
	public ShamanSprite() {
		super();
		
		texture( Assets.SHAMAN );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( 12, true );
		run.frames( frames, 4, 5, 6, 7 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 2, 3, 0 );
		
		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 8, 9, 10 );
		
		play( idle );
	}
	
	public void zap( int pos ) {
		
		points[0] = ch.pos;
		points[1] = pos;
		parent.add( new Lightning( points, 2, (Shaman)ch ) );
		
		turnTo( ch.pos, pos );
		play( zap );
	}
}
