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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.watabou.noosa.TextureFilm;

public class MimicSprite extends MobSprite {

	private Animation hiding;

	{
		//adjust shadow slightly to account for 1 empty bottom pixel (used for border while hiding)
		perspectiveRaise    = 5 / 16f; //5 pixels
		shadowWidth         = 1f;
		shadowOffset        = -0.4f;
	}

	protected int texOffset(){
		return 0;
	}

	public MimicSprite() {
		super();

		int c = texOffset();

		texture( Assets.Sprites.MIMIC );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		hiding = new Animation( 1, true );
		hiding.frames( frames, 0+c, 0+c, 0+c, 0+c, 0+c, 1+c);

		idle = new Animation( 5, true );
		idle.frames( frames, 2+c, 2+c, 2+c, 3+c, 3+c );

		run = new Animation( 10, true );
		run.frames( frames, 2+c, 3+c, 4+c, 5+c, 5+c, 4+c, 3+c );

		attack = new Animation( 10, false );
		attack.frames( frames, 2+c, 6+c, 7+c, 8+c );

		die = new Animation( 5, false );
		die.frames( frames, 9+c, 10+c, 11+c );

		play( idle );
	}
	
	@Override
	public void linkVisuals(Char ch) {
		super.linkVisuals(ch);
		if (ch.alignment == Char.Alignment.NEUTRAL) {
			hideMimic();
		}
	}

	public void hideMimic(){
		play(hiding);
		hideSleep();
	}

	@Override
	public void showSleep() {
		if (curAnim == hiding){
			return;
		}
		super.showSleep();
	}

	public static class Golden extends MimicSprite{
		@Override
		protected int texOffset() {
			return 16;
		}
	}

	public static class Crystal extends MimicSprite{
		@Override
		protected int texOffset() {
			return 32;
		}
	}

}
