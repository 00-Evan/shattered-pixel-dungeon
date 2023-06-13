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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.TOXIC_STEALTH;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Plague;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class PlagueGas extends Blob implements Hero.Doom {

	@Override
	protected void evolve() {
		super.evolve();

		int damage = 1 + Dungeon.scalingDepth()/5;

		Char ch;
		int cell;
		Hero hero = Dungeon.hero;

		for (int i = area.left; i < area.right; i++){
			for (int j = area.top; j < area.bottom; j++){
				cell = i + j*Dungeon.level.width();
				if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
					if (!ch.isImmune(this.getClass())) {
						ch.damage(damage, this);

						if (ch.buff(Plague.class) == null) {
							Talent.PlagueCounter counter = Buff.count(ch, Talent.PlagueCounter.class, 1);
							if (ch != hero && ch.HP > 0
									&& Random.Float() >= (Math.pow(0.9f, counter.count()) - 0.15f)) {
								//역병포자의 역병 옮기는 계산 15% 시작
								Plague.affect(ch, Plague.class).set(Plague.DURATION);
							}
						}
					}
				}
			}
		}

		if (hero.isAlive() && cur[hero.pos] > 0 && hero.hasTalent(TOXIC_STEALTH)) {
			Barrier s = Buff.affect( hero, Barrier.class );
			if (s != null && s.shielding() < hero.pointsInTalent(TOXIC_STEALTH)){
				s.incShield(1);
			}
		}

	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( Speck.factory( Speck.STENCH), 0.4f );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get( this, "desc");
	}

	@Override
	public void onDeath() {
		//면역
		return;
	}
}
