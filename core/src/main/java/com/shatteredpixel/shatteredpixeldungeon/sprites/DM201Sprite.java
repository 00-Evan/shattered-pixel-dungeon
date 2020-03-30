/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM201;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

//TODO currently just DM-200s with treads chopped off
public class DM201Sprite extends MobSprite {

	public DM201Sprite () {
		super();

		texture( Assets.DM300 );

		TextureFilm frames = new TextureFilm( texture, 22, 16 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1 );

		run = idle.clone();

		attack = new Animation( 15, false );
		attack.frames( frames, 4, 5, 6, 0 );

		zap = attack.clone();

		die = new Animation( 20, false );
		die.frames( frames, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 8 );

		play( idle );
		scale.set( 0.8f );
	}

	@Override
	public void resetColor() {
		super.resetColor();
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				MagicMissile.CORROSION,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						Sample.INSTANCE.play( Assets.SND_PUFF );
						((DM201)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );
	}

}
