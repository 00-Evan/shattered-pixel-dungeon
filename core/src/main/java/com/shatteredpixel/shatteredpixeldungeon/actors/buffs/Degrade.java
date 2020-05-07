/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Degrade extends FlavourBuff {

	public static final float DURATION = 30f;
	
	{
		type = buffType.NEGATIVE;
		announced = true;
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)){
			Item.updateQuickslot();
			return true;
		}
		return false;
	}

	@Override
	public void detach() {
		super.detach();
		Item.updateQuickslot();
	}

	//called in Item.buffedLevel()
	public static int reduceLevel( int level ){
		if (level <= 0){
			//zero or negative levels are unaffected
			return level;
		} else {
			//Otherwise returns the rounded result of sqrt(2*(lvl-1)) + 1
			// This means that levels 1/2/3/4/5/6/7/8/9/10/11/12/...
			// Are now instead:       1/2/3/3/4/4/4/5/5/ 5/ 5/ 6/...
			// Basically every level starting with 3 sticks around for 1 level longer than the last
			return (int)Math.round(Math.sqrt(2*(level-1)) + 1);
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.DEGRADE;
	}

	@Override
	public float iconFadePercent() {
		return (DURATION - visualcooldown())/DURATION;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String heroMessage() {
		return Messages.get(this, "heromsg");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
	
}
