/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalWisp;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.TorchHalo;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PointF;

public abstract class CrystalWispSprite extends MobSprite {

	private TorchHalo light;

	public CrystalWispSprite() {
		super();

		int c = texOffset();

		texture( Assets.Sprites.CRYSTAL_WISP );

		TextureFilm frames = new TextureFilm( texture, 12, 14 );

		idle = new Animation( 1, true );
		idle.frames( frames, c+0 );

		run = new Animation( 12, true );
		run.frames( frames, c+0, c+0, c+0, c+1 );

		attack = new Animation( 16, false );
		attack.frames( frames, c+2, c+3, c+4, c+5 );

		zap = attack.clone();

		die = new Animation( 15, false );
		die.frames( frames, c+6, c+7, c+8, c+9, c+10, c+11, c+12, c+11 );

		play( idle );
	}

	public void zap( int cell ) {

		super.zap( cell );

		parent.add(new AlphaTweener(light, 1f, 0.2f) {
			@Override
			public void onComplete() {
				light.alpha(0.3f);
				((CrystalWisp)ch).onZapComplete();
				Beam ray = new Beam.LightRay(center(), DungeonTilemap.raisedTileCenterToWorld(cell));
				ray.hardlight(blood() & 0x00FFFFFF);
				parent.add( ray );
			}
		});

	}

	@Override
	public synchronized void attack(int cell) {
		super.attack(cell);
		parent.add(new AlphaTweener(light, 1f, 0.2f) {
			@Override
			public void onComplete() {
				light.alpha(0.3f);
			}
		});
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		light = new TorchHalo( this );
		light.hardlight(blood() & 0x00FFFFFF);
		light.alpha(0.3f);
		light.radius(10);

		GameScene.effect(light);
	}

	@Override
	public void die() {
		super.die();
		if (light != null){
			light.putOut();
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (light != null){
			light.killAndErase();
		}
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	private float baseY = Float.NaN;

	@Override
	public void place(int cell) {
		super.place(cell);
		baseY = y;
	}

	@Override
	public PointF point(PointF p) {
		super.point(p);
		baseY = y;
		return p;
	}

	@Override
	public void move(int from, int to) {
		super.move(from, to);
	}

	@Override
	public void update() {
		super.update();

		if (!paused && curAnim != die){
			if (Float.isNaN(baseY)) baseY = y;
			y = baseY + Math.abs((float)Math.sin(Game.timeTotal));
			shadowOffset = 0.25f - 0.8f*Math.abs((float)Math.sin(Game.timeTotal));
		}

		if (light != null){
			light.visible = visible;
			light.point(center());

		}
	}

	@Override
	public void turnTo(int from, int to) {
		//do nothing
	}

	protected abstract int texOffset();

	public static class Blue extends CrystalWispSprite {
		@Override
		protected int texOffset() {
			return 0;
		}
		@Override
		public int blood() {
			return 0xFF66B3FF;
		}
	}

	public static class Green extends CrystalWispSprite {
		@Override
		protected int texOffset() {
			return 13;
		}
		@Override
		public int blood() {
			return 0xFF2EE62E;
		}
	}

	public static class Red extends CrystalWispSprite {
		@Override
		protected int texOffset() {
			return 26;
		}
		@Override
		public int blood() {
			return 0xFFFF7F00;
		}
	}

}
