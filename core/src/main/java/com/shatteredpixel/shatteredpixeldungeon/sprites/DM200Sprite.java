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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

//TODO currently just uses DM-300's sprite scaled to 80%
public class DM200Sprite extends MobSprite {

	public DM200Sprite () {
		super();

		texture( Assets.DM300 );

		TextureFilm frames = new TextureFilm( texture, 22, 20 );

		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1 );

		run = new Animation( 10, true );
		run.frames( frames, 2, 3 );

		attack = new Animation( 15, false );
		attack.frames( frames, 4, 5, 6, 0 );

		zap = attack.clone();

		die = new Animation( 20, false );
		die.frames( frames, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 8 );

		play( idle );
		scale.set( 0.8f );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				MagicMissile.TOXIC_VENT,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((DM200)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.SND_PUFF );
	}

}
