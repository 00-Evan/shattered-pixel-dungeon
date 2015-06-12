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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;

public class EyeSprite extends MobSprite {
	
	private int attackPos;
	
	public EyeSprite() {
		super();
		
		texture( Assets.EYE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 18 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 1, 2 );
		
		run = new Animation( 12, true );
		run.frames( frames, 5, 6 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 4, 3 );
		
		die = new Animation( 8, false );
		die.frames( frames, 7, 8, 9 );
		
		play( idle );
	}
	
	@Override
	public void attack( int pos ) {
		attackPos = pos;
		super.attack( pos );
	}
	
	@Override
	public void onComplete( Animation anim ) {
		super.onComplete( anim );
		
		if (anim == attack) {
			if (Dungeon.visible[ch.pos] || Dungeon.visible[attackPos]) {
				parent.add( new Beam.DeathRay( center(), DungeonTilemap.tileCenterToWorld( attackPos ) ) );
			}
		}
	}
}
