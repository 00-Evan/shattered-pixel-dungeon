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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class LotusSprite extends MobSprite {

	private ArrayList<Emitter> grassVfx;

	public LotusSprite(){
		super();

		perspectiveRaise = 0f;

		texture( Assets.Sprites.LOTUS );

		TextureFilm frames = new TextureFilm( texture, 19, 16 );

		idle = new MovieClip.Animation( 1, true );
		idle.frames( frames, 0 );

		run = new MovieClip.Animation( 1, true );
		run.frames( frames, 0 );

		attack = new MovieClip.Animation( 1, false );
		attack.frames( frames, 0 );

		die = new MovieClip.Animation( 1, false );
		die.frames( frames, 0 );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );

		renderShadow = false;

		if (grassVfx == null && ch instanceof WandOfRegrowth.Lotus){
			WandOfRegrowth.Lotus l = (WandOfRegrowth.Lotus) ch;
			grassVfx = new ArrayList<>();
			for (int i = 0; i < Dungeon.level.length(); i++){
				if (!Dungeon.level.solid[i] && l.inRange(i)) {
					Emitter e = CellEmitter.get(i);
					e.pour(LeafParticle.LEVEL_SPECIFIC, 0.5f);
					grassVfx.add(e);
				}
			}
		}
	}

	@Override
	public void place(int cell) {
		if (parent != null) parent.sendToBack(this);
		super.place(cell);
	}

	@Override
	public void turnTo(int from, int to) {
		//do nothing
	}

	@Override
	public void update() {
		visible = true;
		super.update();
	}

	@Override
	public void die() {
		super.die();

		if (grassVfx != null){
			for (Emitter e : grassVfx){
				e.on = false;
			}
			grassVfx = null;
		}
	}
}
