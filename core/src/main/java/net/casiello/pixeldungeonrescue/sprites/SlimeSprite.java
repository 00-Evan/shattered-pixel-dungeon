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

package net.casiello.pixeldungeonrescue.sprites;

import net.casiello.pixeldungeonrescue.Assets;
import com.watabou.noosa.TextureFilm;

public abstract class SlimeSprite extends MobSprite {

	protected abstract int texOffset();

	public SlimeSprite() {
		super();

		int c = texOffset();

		texture( Assets.SLIME );

		TextureFilm frames = new TextureFilm( texture, 14, 12 );
		
		idle = new Animation( 3, true );
		idle.frames( frames, c+0, c+1, c+1, c+0 );
		
		run = new Animation( 10, true );
		run.frames( frames, c+0, c+2, c+3, c+3, c+2, c+0 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, c+2, c+3, c+4, c+3, c+2 );
		
		die = new Animation( 10, false );
		die.frames( frames, c+5, c+6, c+7, c+8, c+7, c+6, c+5 );

		play(idle);
	}

	public static class Lime extends SlimeSprite {
		@Override
		protected int texOffset() {
			return 0;
		}
	}
	public static class Lemon extends SlimeSprite {
		@Override
		protected int texOffset() {
			return 2 * 9;
		}
	}
	public static class Strawberry extends SlimeSprite {
		@Override
		protected int texOffset() {
			return 3 * 9;
		}
	}
	public static class Grape extends SlimeSprite {
		@Override
		protected int texOffset() {
			return 4 * 9;
		}
	}
	public static class Orange extends SlimeSprite {
		@Override
		protected int texOffset() {
			return 5 * 9;
		}
	}
}
