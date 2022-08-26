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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SacrificialParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SacrificeRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SacrificialFire extends Blob {

	BlobEmitter curEmitter;

	{
		//acts after mobs, so they can get marked as they move
		actPriority = MOB_PRIO-1;
	}

	//Can spawn extra mobs to make sacrificing less tedious
	// The limit is to prevent farming
	private int bonusSpawns = 3;

	private Item prize;

	@Override
	protected void evolve() {
		int cell;
		for (int i=area.top-1; i <= area.bottom; i++) {
			for (int j = area.left-1; j <= area.right; j++) {
				cell = j + i* Dungeon.level.width();
				if (Dungeon.level.insideMap(cell)) {
					off[cell] = cur[cell];
					volume += off[cell];

					if (off[cell] > 0){
						for (int k : PathFinder.NEIGHBOURS9){
							Char ch = Actor.findChar( cell+k );
							if (ch != null){
								if (Dungeon.level.heroFOV[cell+k] && ch.buff( Marked.class ) == null) {
									CellEmitter.get(cell+k).burst( SacrificialParticle.FACTORY, 5 );
								}
								Buff.prolong( ch, Marked.class, Marked.DURATION );
							}
						}

						if (off[cell] > 0 && Dungeon.level.heroFOV[cell]) {

							Notes.add( Notes.Landmark.SACRIFICIAL_FIRE);

							if (Dungeon.level.mobCount() == 0
									&& bonusSpawns > 0) {
								if (Dungeon.level.spawnMob(4)) {
									bonusSpawns--;
								}
							}
						}
					}
				}
			}
		}

		//a bit brittle, assumes only one tile of sacrificial fire can exist per floor
		int max = 6 + Dungeon.depth * 4;
		curEmitter.pour( SacrificialParticle.FACTORY, 0.01f + ((volume / (float)max) * 0.09f) );
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		curEmitter = emitter;

		//a bit brittle, assumes only one tile of sacrificial fire can exist per floor
		int max = 6 + Dungeon.depth * 4;
		curEmitter.pour( SacrificialParticle.FACTORY, 0.01f + ((volume / (float)max) * 0.09f) );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	private static final String BONUS_SPAWNS = "bonus_spawns";
	private static final String PRIZE = "prize";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(BONUS_SPAWNS, bonusSpawns);
		bundle.put(PRIZE, prize);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		bonusSpawns = bundle.getInt(BONUS_SPAWNS);
		if (bundle.contains(PRIZE)) prize = (Item) bundle.get(PRIZE);
	}

	public void setPrize( Item prize ){
		this.prize = prize;
	}

	public void sacrifice( Char ch ) {

		int firePos = -1;
		for (int i : PathFinder.NEIGHBOURS9){
			if (volume > 0 && cur[ch.pos+i] > 0){
				firePos = ch.pos+i;
				break;
			}
		}

		if (firePos != -1) {

			int exp = 0;
			if (ch instanceof Mob) {
				//same rates as used in wand of corruption, except for swarms
				if (ch instanceof Statue || ch instanceof Mimic){
					exp = 1 + Dungeon.depth;
				} else if (ch instanceof Piranha || ch instanceof Bee) {
					exp = 1 + Dungeon.depth/2;
				} else if (ch instanceof Wraith) {
					exp = 1 + Dungeon.depth/3;
				} else if (ch instanceof Swarm && ((Swarm) ch).EXP == 0){
					//give 1 exp for child swarms, instead of 0
					exp = 1;
				} else {
					exp = ((Mob)ch).EXP;
				}
				exp *= Random.IntRange( 2, 3 );
			} else if (ch instanceof Hero) {
				exp = 1_000_000; //always enough to activate the reward, if you can somehow get it
				Badges.validateDeathFromSacrifice();
			}

			if (exp > 0) {

				int volumeLeft = cur[firePos] - exp;
				if (volumeLeft > 0) {
					cur[firePos] -= exp;
					volume -= exp;
					bonusSpawns++;
					CellEmitter.get(firePos).burst( SacrificialParticle.FACTORY, 20 );
					Sample.INSTANCE.play(Assets.Sounds.BURNING );
					GLog.w( Messages.get(SacrificialFire.class, "worthy"));
				} else {
					clear(firePos);
					Notes.remove(Notes.Landmark.SACRIFICIAL_FIRE);

					for (int i : PathFinder.NEIGHBOURS9){
						CellEmitter.get(firePos+i).burst( SacrificialParticle.FACTORY, 20 );
					}
					Sample.INSTANCE.play(Assets.Sounds.BURNING );
					Sample.INSTANCE.play(Assets.Sounds.BURNING );
					Sample.INSTANCE.play(Assets.Sounds.BURNING );
					GLog.w( Messages.get(SacrificialFire.class, "reward"));
					if (prize != null) {
						Dungeon.level.drop(prize, firePos).sprite.drop();
					} else {
						Dungeon.level.drop(SacrificeRoom.prize(Dungeon.level), firePos).sprite.drop();
					}
				}
			} else {

				GLog.w( Messages.get(SacrificialFire.class, "unworthy"));

			}
		}
	}

	public static class Marked extends FlavourBuff {

		public static final float DURATION	= 2f;

		@Override
		public void detach() {
			if (!target.isAlive()) {
				SacrificialFire fire = (SacrificialFire) Dungeon.level.blobs.get(SacrificialFire.class);
				if (fire != null) {
					fire.sacrifice(target);
				}
			}
			super.detach();
		}
	}

}
