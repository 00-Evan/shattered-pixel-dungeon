/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;

public class Regeneration extends Buff {
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT && !((Hero)target).isStarving()) {
				LockedFloor lock = target.buff(LockedFloor.class);
				if (lock == null || lock.regenOn()) {
					target.HP += 1;
					if (target.HP == target.HT) {
						((Hero) target).resting = false;
					}
				}
			}

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff( ChaliceOfBlood.chaliceRegen.class);

			if (regenBuff != null)
				if (regenBuff.isCursed())
					spend( REGENERATION_DELAY * 1.5f );
				else
					spend( REGENERATION_DELAY - regenBuff.level()*0.9f );
			else
				spend( REGENERATION_DELAY );
			
		} else {
			
			diactivate();
			
		}
		
		return true;
	}
}
