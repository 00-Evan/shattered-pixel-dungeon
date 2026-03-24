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
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class VaultSkeletonSprite extends SkeletonSprite {

	public VaultSkeletonSprite(){
		super();

		texture( Assets.Sprites.VAULT_SKELETON );

		TextureFilm frames = new TextureFilm( texture, 12, 16 );

		idle = new MovieClip.Animation( 12, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3 );

		run = new MovieClip.Animation( 15, true );
		run.frames( frames, 4, 5, 6, 7, 8, 9 );

		attack = new MovieClip.Animation( 15, false );
		attack.frames( frames, 14, 15, 16 );

		die = new MovieClip.Animation( 12, false );
		die.frames( frames, 10, 11, 12, 13 );

		play( idle );
	}

}
