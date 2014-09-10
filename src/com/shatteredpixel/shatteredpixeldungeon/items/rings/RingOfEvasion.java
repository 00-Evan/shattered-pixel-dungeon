/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class RingOfEvasion extends Ring {

	{
		name = "Ring of Evasion";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Evasion();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"This ring increases the wearer's ability to focus and anticipate the movements of an enemy. " +
            "The longer the wearer stands still, the more focused they will become. " +
            "A cursed ring will instead make dodging harder." :
			super.desc();
	}

    //yup, the only ring in the game with logic inside of its class
	public class Evasion extends RingBuff {
        public int effectiveLevel;
        private int pos;

        @Override
        public boolean attachTo( Char target ) {

            pos = target.pos;
            effectiveLevel = Math.min(0, level);
            return super.attachTo(target);
        }

        @Override
        public boolean act() {

            if (level >= 0) {
                if (pos == target.pos && effectiveLevel < level) {
                    effectiveLevel++;
                } else if (pos != target.pos) {
                    effectiveLevel = 0;
                    pos = target.pos;
                }
            } else if (level < 0) {
                if (pos == target.pos && effectiveLevel < 0) {
                    effectiveLevel++;
                } else if (pos != target.pos) {
                    effectiveLevel = level;
                    pos = target.pos;
                }
            }

            return super.act();
        }
	}
}
