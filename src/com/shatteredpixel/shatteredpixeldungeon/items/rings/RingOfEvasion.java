/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class RingOfEvasion extends Ring {
	
	@Override
	protected RingBuff buff( ) {
		return new Evasion();
	}

	//yup, the only ring in the game with logic inside of its class
	public class Evasion extends RingBuff {
		public int effectiveLevel;

		@Override
		public boolean attachTo( Char target ) {

			effectiveLevel = Math.min(0, RingOfEvasion.this.level());
			return super.attachTo(target);
		}

		@Override
		public boolean act() {

			boolean seen = false;

			for (Mob enemy : Dungeon.level.mobs.toArray(new Mob[0])){
				if (enemy.focusingHero()) {
					seen = true;
					break;
				}
			}

			if (RingOfEvasion.this.level() < 1){
				effectiveLevel = RingOfEvasion.this.level();
			} else if (seen) {
				effectiveLevel = Math.max(effectiveLevel - 1, 0);
			} else {
				effectiveLevel = Math.min(effectiveLevel + 1, RingOfEvasion.this.level());
			}

			return super.act();
		}

		@Override
		public int level() {
			return effectiveLevel;
		}
	}
}
