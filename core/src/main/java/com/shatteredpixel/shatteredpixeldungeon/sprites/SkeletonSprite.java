/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class SkeletonSprite extends MobSprite {
	
	public SkeletonSprite() {
		super();
		
		texture( Assets.Sprites.SKELETON );
		
		TextureFilm frames = new TextureFilm( texture, 12, texHeight() );

		int c = texOffset();

		idle = new Animation( 12, true );
		idle.frames( frames, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 0+c, 1+c, 2+c, 3+c );
		
		run = new Animation( 15, true );
		run.frames( frames, 4+c, 5+c, 6+c, 7+c, 8+c, 9+c );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 14+c, 15+c, 16+c );
		
		die = new Animation( 12, false );
		die.frames( frames, 10+c, 11+c, 12+c, 13+c );
		
		play( idle );
	}

	protected int texOffset() {
		return 0;
	}

	protected int texHeight() {
		return 15;
	}
	
	@Override
	public void die() {
		super.die();
		if (Dungeon.level.heroFOV[ch.pos]) {
			emitter().burst( Speck.factory( Speck.BONE ), 6 );
		}
	}
	
	@Override
	public int blood() {
		return 0xFFcccccc;
	}

	public static class Vault extends SkeletonSprite {

		@Override
		protected int texOffset() {
			return 21;
		}

		@Override
		protected int texHeight() {
			return 16; //1px taller due to beard
		}
	}

}
