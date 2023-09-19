/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

public abstract class CrystalSpireSprite extends MobSprite {

	{
		perspectiveRaise = 7 / 16f; //7 pixels

		shadowWidth     = 1f;
		shadowHeight    = 1f;
		shadowOffset    = 1f;
	}

	public CrystalSpireSprite(){
		texture( Assets.Sprites.CRYSTAL_SPIRE );

		TextureFilm frames = new TextureFilm( texture, 30, 45 );

		int c = texOffset();

		idle = new Animation(1, true);
		idle.frames( frames, 0+c );

		run = idle.clone();
		attack = idle.clone();
		zap = idle.clone();

		die = new Animation(1, false);
		die.frames( frames, 4+c );

		play(idle);
	}

	public void updateIdle(){
		float hpPercent = 1f;
		if (ch != null){
			hpPercent = ch.HP/(float)ch.HT;
		}

		TextureFilm frames = new TextureFilm( texture, 30, 45 );

		if (hpPercent > 0.8f){
			idle.frames( frames, 0+texOffset() );
		} else if (hpPercent > 0.5f){
			idle.frames( frames, 1+texOffset() );
		} else if (hpPercent > 0.25f){
			idle.frames( frames, 2+texOffset() );
		} else {
			idle.frames( frames, 3+texOffset() );
		}
		play(idle, true);
		run = idle.clone();
		attack = idle.clone();
		zap = idle.clone();
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		updateIdle();
	}

	@Override
	public void die() {
		super.die();
		Splash.around(this, blood(), 100);
	}

	protected abstract int texOffset();

	public static class Blue extends CrystalSpireSprite {
		@Override
		protected int texOffset() {
			return 0;
		}
		@Override
		public int blood() {
			return 0xFF8EE3FF;
		}
	}

	public static class Green extends CrystalSpireSprite {
		@Override
		protected int texOffset() {
			return 5;
		}
		@Override
		public int blood() {
			return 0xFF85FFC8;
		}
	}

	public static class Red extends CrystalSpireSprite {
		@Override
		protected int texOffset() {
			return 10;
		}
		@Override
		public int blood() {
			return 0xFFFFBB33;
		}
	}

}
