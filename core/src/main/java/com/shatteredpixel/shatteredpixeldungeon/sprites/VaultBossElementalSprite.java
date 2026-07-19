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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultBossElemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public abstract class VaultBossElementalSprite extends ElementalSprite {

	public VaultBossElementalSprite(){
		super();
		scale.set(2f);

		operate = attack.clone();

	}

	public static class Fire extends VaultBossElementalSprite {

		@Override
		protected int texOffset() {
			return 0;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( FlameParticle.FACTORY, 0.06f );
			return emitter;
		}

		public void zap( int cell ) {
			zap( cell, null );

			MagicMissile.boltFromChar( parent,
					MagicMissile.FIRE_CONE,
					this,
					cell,
					new Callback() {
						@Override
						public void call() {
							((VaultBossElemental) ch).onZapComplete();
						}
					} );
			Sample.INSTANCE.play( Assets.Sounds.ZAP );
		}

		@Override
		public int blood() {
			return 0xFFFFBB33;
		}
	}

	public static class Frost extends VaultBossElementalSprite {

		@Override
		protected int texOffset() {
			return 28;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( MagicMissile.MagicParticle.FACTORY, 0.06f );
			return emitter;
		}

		public void zap( int cell ) {
			zap( cell, null );

			((VaultBossElemental)ch).onZapComplete();
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
		}

		@Override
		public int blood() {
			return 0xFF8EE3FF;
		}
	}

	public static class Shock extends VaultBossElementalSprite {

		//different bolt, so overrides zap
		@Override
		public void zap( int cell ) {
			zap( cell, null );

			Ballistica b = new Ballistica(ch.pos, cell, Ballistica.STOP_SOLID);
			cell = b.collisionPos;

			((VaultBossElemental)ch).onZapComplete();

			parent.add( new Lightning(center(), cell, null));
			Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
		}

		@Override
		protected int texOffset() {
			return 42;
		}

		@Override
		protected Emitter createEmitter() {
			Emitter emitter = emitter();
			emitter.pour( SparkParticle.STATIC, 0.06f );
			return emitter;
		}

		@Override
		public int blood() {
			return 0xFFFFFF85;
		}
	}

}
