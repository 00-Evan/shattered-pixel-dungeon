/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Necromancer;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class NecromancerSprite extends MobSprite {
	
	private Animation charging;
	private Emitter summoningBones;
	
	public NecromancerSprite(){
		super();
		
		texture( Assets.Sprites.NECRO );
		TextureFilm film = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		
		run = new Animation( 8, true );
		run.frames( film, 0, 0, 0, 2, 3, 4 );
		
		zap = new Animation( 10, false );
		zap.frames( film, 5, 6, 7, 8 );
		
		charging = new Animation( 5, true );
		charging.frames( film, 7, 8 );
		
		die = new Animation( 10, false );
		die.frames( film, 9, 10, 11, 12 );
		
		attack = zap.clone();
		
		idle();
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Necromancer && ((Necromancer) ch).summoning){
			zap(((Necromancer) ch).summoningPos);
		}
	}

	@Override
	public void update() {
		super.update();
		if (summoningBones != null && ((Necromancer) ch).summoningPos != -1){
			summoningBones.visible = Dungeon.level.heroFOV[((Necromancer) ch).summoningPos];
		}
	}

	@Override
	public void die() {
		super.die();
		if (summoningBones != null){
			summoningBones.on = false;
		}
	}

	@Override
	public void kill() {
		super.kill();
		if (summoningBones != null){
			summoningBones.killAndErase();
		}
	}

	public void cancelSummoning(){
		if (summoningBones != null){
			summoningBones.on = false;
		}
	}

	public void finishSummoning(){
		if (summoningBones.visible) {
			Sample.INSTANCE.play(Assets.Sounds.BONES);
			summoningBones.burst(Speck.factory(Speck.RATTLE), 5);
		} else {
			summoningBones.on = false;
		}
		idle();
	}

	public void charge(){
		play(charging);
	}

	@Override
	public void zap(int cell) {
		super.zap(cell);
		if (ch instanceof Necromancer && ((Necromancer) ch).summoning){
			if (summoningBones != null){
				summoningBones.on = false;
			}
			summoningBones = CellEmitter.get(((Necromancer) ch).summoningPos);
			summoningBones.pour(Speck.factory(Speck.RATTLE), 0.2f);
			summoningBones.visible = Dungeon.level.heroFOV[((Necromancer) ch).summoningPos];
			if (visible || summoningBones.visible ) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP, 1f, 0.8f );
		}
	}

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);
		if (anim == zap){
			if (ch instanceof Necromancer){
				if (((Necromancer) ch).summoning){
					charge();
				} else {
					((Necromancer)ch).onZapComplete();
					idle();
				}
			} else {
				idle();
			}
		}
	}
}
