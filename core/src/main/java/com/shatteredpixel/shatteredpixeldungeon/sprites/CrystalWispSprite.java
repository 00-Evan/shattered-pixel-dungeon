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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalWisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;

public abstract class CrystalWispSprite extends MobSprite {

	public CrystalWispSprite() {
		super();

		int c = texOffset();

		texture( Assets.Sprites.CRYSTAL_WISP );

		TextureFilm frames = new TextureFilm( texture, 12, 14 );

		idle = new Animation( 4, true );
		idle.frames( frames, c+0, c+1, c+0, c+2 );

		run = new Animation( 12, true );
		run.frames( frames, c+0, c+1, c+0, c+3 );

		attack = new Animation( 15, false );
		attack.frames( frames, c+4, c+5, c+6 );

		zap = attack.clone();

		die = new Animation( 15, false );
		die.frames( frames, c+7, c+8, c+9, c+10, c+11, c+12, c+13, c+12 );

		play( idle );
	}

	public void zap( int cell ) {

		super.zap( cell );

		((CrystalWisp)ch).onZapComplete();
		parent.add( new Beam.LightRay(center(), DungeonTilemap.raisedTileCenterToWorld(cell)));

	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	protected abstract int texOffset();

	public static class Blue extends CrystalWispSprite {
		@Override
		protected int texOffset() {
			return 0;
		}
		@Override
		public int blood() {
			return 0xFF8EE3FF;
		}
	}

	public static class Green extends CrystalWispSprite {
		@Override
		protected int texOffset() {
			return 14;
		}
		@Override
		public int blood() {
			return 0xFF85FFC8;
		}
	}

	public static class Red extends CrystalWispSprite {
		@Override
		protected int texOffset() {
			return 28;
		}
		@Override
		public int blood() {
			return 0xFFFFBB33;
		}
	}

}
