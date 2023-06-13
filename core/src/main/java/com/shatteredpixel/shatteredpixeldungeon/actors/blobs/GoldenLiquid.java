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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Golden;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.GoldenParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GoldenLiquid extends Blob {

	int count = 0;

	@Override
	protected void evolve() {

		int cell;

		boolean observe = false;

		for (int i = area.left - 1; i <= area.right; i++) {
			for (int j = area.top - 1; j <= area.bottom; j++) {
				cell = i + j * Dungeon.level.width();
				if (cur[cell] > 0) {

					golden(cell);

					Heap h = Dungeon.level.heaps.get( cell );
					if (h != null){
						Item item = h.peek();
						if (item instanceof MissileWeapon && !(item instanceof Dart)){
							MissileWeapon m = ((MissileWeapon) item);

							m.repair(Random.Int(1,1 + Dungeon.scalingDepth() / 5) );

						}
					}

					off[cell] = cur[cell]  - 1;
					volume += off[cell];

				} else {
					off[cell] = 0;

				}
			}

			if (observe) {
				Dungeon.observe();
			}
		}
	}

	public static void golden(int pos) {
		Char ch = Actor.findChar(pos);
		if (ch != null && !ch.isImmune(Golden.class)) {
			Buff.affect(ch, Golden.class).set(6);
		}

		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.golden();
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.pour(GoldenParticle.UP, 0.03f);
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}

	private static final String COUNT = "count";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt(COUNT);
	}
}
