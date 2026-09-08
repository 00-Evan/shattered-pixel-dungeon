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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.quest.vault.VaultBossElemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class VaultBossElementalSprite extends MobSprite {

	private Emitter particles;

	{
		perspectiveRaise = 2 / 16f; //It's already huge and floats, so tiny raise
	}

	private VaultBossElemental.ElementalForm form;

	public VaultBossElementalSprite() {
		super();

		texture( Assets.Sprites.VAULT_BOSS_ELEMENTAL );

		updateAnimations();
	}

	public void setForm(VaultBossElemental.ElementalForm form){
		this.form = form;
		updateAnimations();
	}

	@Override
	public void linkVisuals(Char ch) {
		super.linkVisuals(ch);
		if (ch instanceof VaultBossElemental && ((VaultBossElemental) ch).curForm() != null){
			form = ((VaultBossElemental) ch).curForm();
		}
		updateAnimations();
	}

	public void updateForm(){
		if (ch instanceof VaultBossElemental && ((VaultBossElemental) ch).curForm() != null){
			form = ((VaultBossElemental) ch).curForm();
		}
		updateAnimations();
	}

	private void updateAnimations(){

		//pick a random form if we have none
		if (form == null){
			form = VaultBossElemental.ElementalForm.values()[Random.Int(3)];
		}

		int c = 16*form.ordinal();

		TextureFilm frames = new TextureFilm( texture, 31, 30 );

		idle = new Animation( 10, true );
		idle.frames( frames, c+0, c+1, c+2, c+3 );

		run = new Animation( 12, true );
		run.frames( frames, c+0, c+1, c+2, c+3 );

		attack = new Animation( 12, false );
		attack.frames( frames, c+4, c+5, c+6 );

		zap = attack.clone();

		die = new Animation( 15, false );
		die.frames( frames, c+7, c+8, c+9, c+10, c+11, c+12, c+13, c+12 );

		operate = attack.clone();

		play( idle );

		if (particles != null){
			particles.killAndErase();
			particles = createEmitter();
		}

	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	@Override
	public void zap(int cell) {
		zap( cell, null );

		switch (form){
			case FIRE: default:
				MagicMissile.boltFromChar( parent,
						MagicMissile.FIRE,
						this,
						cell,
						new Callback() {
							@Override
							public void call() {
								((VaultBossElemental) ch).onZapComplete();
							}
						} );
				Sample.INSTANCE.play( Assets.Sounds.ZAP );
				break;
			case FROST:
				((VaultBossElemental) ch).onZapComplete();
				Sample.INSTANCE.play( Assets.Sounds.SHATTER );
				break;
			case SHOCK:
				zap( cell, null );

				Ballistica b = new Ballistica(ch.pos, cell, Ballistica.STOP_SOLID);
				cell = b.collisionPos;

				((VaultBossElemental)ch).onZapComplete();

				parent.add( new Lightning(center(), cell, null));
				Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
				break;
		}

	}

	private Emitter createEmitter() {
		Emitter emitter = emitter();
		//centered on the core sprite region
		emitter.pos(this, 8, 8, 16, 16);
		emitter.fillTarget = false;
		switch (form){
			case FIRE: default:
				emitter.pour( FlameParticle.FACTORY, 0.03f );
				break;
			case FROST:
				emitter.pour( MagicMissile.MagicParticle.FACTORY, 0.03f );
				break;
			case SHOCK:
				emitter.pour(  SparkParticle.STATIC, 0.03f );
				break;
		}
		return emitter;
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );

		if (particles == null) {
			particles = createEmitter();
		}
	}

	@Override
	public void update() {
		super.update();

		if (particles != null){
			particles.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();
		if (particles != null){
			particles.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (particles != null){
			particles.killAndErase();
		}
	}

	@Override
	public int blood() {
		switch (form){
			case FIRE: default:
				return 0xFFFFBB33;
			case FROST:
				return 0xFF8EE3FF;
			case SHOCK:
				return 0xFFFFFF85;
		}
	}

}
