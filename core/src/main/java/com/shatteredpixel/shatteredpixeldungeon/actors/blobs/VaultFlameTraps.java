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

package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.Arrays;

//contains both blob logic and logic for seeding itself
public class VaultFlameTraps extends Blob {

	public int[] afterTriggerCooldowns;
	public int[] curCooldowns;
	public int[] triggersAfterCooldown;

	//always gives a warning, via the blob

	@Override
	public boolean act() {
		super.act();

		if (afterTriggerCooldowns != null) {
			for (int i = 0; i < afterTriggerCooldowns.length; i++) {
				if (afterTriggerCooldowns[i] > -1) {
					if (curCooldowns[i] <= 0) {
						curCooldowns[i] = afterTriggerCooldowns[i];
					}
					curCooldowns[i]--;
					if (curCooldowns[i] <= 0) {
						seed(Dungeon.level, i, triggersAfterCooldown[i]);
					}
				}
			}
		}

		return true;
	}

	//flame traps will collectively play a SFX at most every 80 ms
	private static long SFXLastPlayed = 0;

	@Override
	protected void evolve() {
		int cell;

		boolean playSfx = false;
		for (int i = area.left; i < area.right; i++) {
			for (int j = area.top; j < area.bottom; j++) {
				cell = i + j* Dungeon.level.width();
				if (cur[cell] > 0) {

					Char ch = Actor.findChar( cell );
					if (ch != null && !ch.isImmune(getClass())) {
						if (ch == Dungeon.hero) {
							Sample.INSTANCE.play(Assets.Sounds.BURNING);
							SFXLastPlayed = ShatteredPixelDungeon.realTime;
							if (Imp.Quest.hazardFreebies > 0){
								Imp.Quest.hazardFreebies--;
							} else {
								Statistics.questScores[3] -= 100;
							}
						}

						if (!ch.isImmune(Fire.class)) {
							Buff.affect( ch, Burning.class ).reignite( ch, 4 );
						}

						Heap heap = Dungeon.level.heaps.get( cell );
						if (heap != null) {
							heap.burn();
						}

						Plant plant = Dungeon.level.plants.get( cell );
						if (plant != null){
							plant.wither();
						}

					}

					if (Dungeon.level.heroFOV[cell]) {
						CellEmitter.get(cell).start(ElmoParticle.FACTORY, 0.02f, 10);
						playSfx = true;
					}

					off[cell] = cur[cell] - 1;
					volume += off[cell];
				} else {
					off[cell] = 0;
				}
			}
		}

		if (playSfx && SFXLastPlayed +80 < ShatteredPixelDungeon.realTime) {
			Sample.INSTANCE.play(Assets.Sounds.BURNING, 0.5f);
			SFXLastPlayed = ShatteredPixelDungeon.realTime;
		}
	}

	public void seed(Level level, int cell, int amount ) {
		super.seed(level, cell, amount);
		if (afterTriggerCooldowns == null) {
			afterTriggerCooldowns = new int[level.length()];
			Arrays.fill(afterTriggerCooldowns, -1);
		}
		if (curCooldowns == null){
			curCooldowns = new int[level.length()];
		}
		if (triggersAfterCooldown == null){
			triggersAfterCooldown = new int[level.length()];
		}
	}

	private static final String AFTER_TRIGGER_CDS	= "after_trigger_cds";
	private static final String CUR_COOLDOWNS	    = "cur_cooldowns";
	private static final String TRIGGERS	        = "triggers";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (afterTriggerCooldowns != null) {
			bundle.put(AFTER_TRIGGER_CDS, afterTriggerCooldowns);
			bundle.put(CUR_COOLDOWNS, curCooldowns);
			bundle.put(TRIGGERS, triggersAfterCooldown);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(AFTER_TRIGGER_CDS)){
			afterTriggerCooldowns = bundle.getIntArray(AFTER_TRIGGER_CDS);
			curCooldowns = bundle.getIntArray(CUR_COOLDOWNS);
			triggersAfterCooldown = bundle.getIntArray(TRIGGERS);
		}
	}

	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.bound.set(0.4f, 0.4f, 0.6f, 0.6f);
		emitter.pour( ElmoParticle.FACTORY, 0.3f );
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

}
