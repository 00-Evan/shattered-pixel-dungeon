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

	public MimicSprite() {
		super();

		texture( Assets.MIMIC );

		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		hiding = new Animation( 1, true );
		hiding.frames( frames, 0, 0, 0, 0, 0, 0, 1);

		idle = new Animation( 5, true );
		idle.frames( frames, 2, 2, 2, 3, 3 );

		run = new Animation( 10, true );
		run.frames( frames, 2, 3, 4, 5, 5, 4, 3 );

		attack = new Animation( 10, false );
		attack.frames( frames, 2, 6, 7, 8 );

		die = new Animation( 5, false );
		die.frames( frames, 9, 10, 11 );

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
	public synchronized void showSleep() {
		if (curAnim == hiding){
			return;
		}
		super.showSleep();
	}

}
