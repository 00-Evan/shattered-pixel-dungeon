/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;

//this is largely a copy-paste from timekeeper's hourglass with the artifact-specific code removed
public class TimeStasis extends FlavourBuff {

	{
		type = Buff.buffType.POSITIVE;
		actPriority = BUFF_PRIO-3; //acts after all other buffs, so they are prevented
	}

	@Override
	public boolean attachTo(Char target) {

		if (super.attachTo(target)) {

			target.invisible++;
			target.paralysed++;
			target.next();

			if (Dungeon.hero != null) {
				Dungeon.observe();
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void spend(float time) {
		super.spend(time);

		//don't punish the player for going into stasis frequently
		Hunger hunger = Buff.affect(target, Hunger.class);
		if (hunger != null && !hunger.isStarving()) {
			hunger.affectHunger(cooldown(), true);
		}
	}

	@Override
	public void detach() {
		if (target.invisible > 0) target.invisible--;
		if (target.paralysed > 0) target.paralysed--;
		super.detach();
		Dungeon.observe();
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.PARALYSED );
		else {
			if (target.paralysed == 0) target.sprite.remove( CharSprite.State.PARALYSED );
			if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
		}
	}

}
