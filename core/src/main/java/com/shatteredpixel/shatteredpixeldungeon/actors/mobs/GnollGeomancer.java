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

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGeomancerSprite;

public class GnollGeomancer extends Mob {

	{
		//TODO
		HP = HT = 300;
		spriteClass = GnollGeomancerSprite.class;

		EXP = 20;

		//acts after other mobs?
		actPriority = MOB_PRIO-1;

		SLEEPING = new Sleeping();
		state = SLEEPING;

		properties.add(Property.BOSS);
	}

	@Override
	public String description() {
		if (state == SLEEPING){
			return Messages.get(this, "desc_sleeping");
		} else {
			return super.description();
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Blacksmith.Quest.beatBoss();
	}

	private class Sleeping extends Mob.Sleeping {

		@Override
		protected void awaken(boolean enemyInFOV) {
			//TODO . do nothing for now
		}
	}
}
