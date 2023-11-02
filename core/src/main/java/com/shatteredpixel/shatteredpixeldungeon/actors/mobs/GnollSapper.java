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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSapperSprite;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class GnollSapper extends Mob {

	{
		spriteClass = GnollSapperSprite.class;

		HP = HT = 35;
		defenseSkill = 15;

		EXP = 10;
		maxLvl = -2;

		properties.add(Property.MINIBOSS);

		SLEEPING = new Sleeping();
		state = SLEEPING;
		//TODO wandering and hunting too. Partly for abilities, but also for other logic
	}

	private ArrayList<Integer> guardIDs = new ArrayList<>();

	public void linkGuard(GnollGuard g){
		guardIDs.add(g.id());
		g.linkSapper(this);
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		for (Integer g : guardIDs){
			if (Actor.findById(g) instanceof GnollGuard){
				((GnollGuard) Actor.findById(g)).loseSapper();
			}
		}
	}

	private static final String GUARD_IDS = "guard_ids";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		int[] toStore = new int[guardIDs.size()];
		for (int i = 0; i < toStore.length; i++){
			toStore[i] = guardIDs.get(i);
		}
		bundle.put(GUARD_IDS, toStore);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		guardIDs = new ArrayList<>();
		for (int g : bundle.getIntArray(GUARD_IDS)){
			guardIDs.add(g);
		}
	}

	public class Sleeping extends Mob.Sleeping {

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (guardIDs.size() < 2){
				for (Char ch : Actor.chars()){
					if (fieldOfView[ch.pos] && ch instanceof GnollGuard && !guardIDs.contains(ch.id())){
						linkGuard((GnollGuard) ch);
						break;
					}
				}
			}
			return super.act(enemyInFOV, justAlerted);
		}
	}
}
