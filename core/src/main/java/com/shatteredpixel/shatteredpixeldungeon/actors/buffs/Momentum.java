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

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Momentum extends Buff {
	
	{
		type = buffType.POSITIVE;
	}
	
	private int stacks = 0;
	private int turnsSinceMove = 0;
	
	@Override
	public boolean act() {
		turnsSinceMove++;
		if (turnsSinceMove > 0){
			stacks = Math.max(0, stacks - turnsSinceMove);
			if (stacks == 0) detach();
		}
		spend(TICK);
		return true;
	}
	
	public void gainStack(){
		stacks = Math.min(stacks+1, 10);
		turnsSinceMove = -1;
	}
	
	public int stacks(){
		return stacks;
	}
	
	public float speedMultiplier(){
		//1.33x speed at max stacks
		return 1f + (stacks/30f);
	}
	
	public int evasionBonus( int excessArmorStr ){
		//8 evasion, +2 evasion per excess str, at max stacks
		return Math.round((0.8f + 0.2f*excessArmorStr) * stacks);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.MOMENTUM;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.invert();
	}

	@Override
	public float iconFadePercent() {
		return (10-stacks)/10f;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", stacks*10);
	}
	
	private static final String STACKS =        "stacks";
	private static final String TURNS_SINCE =   "turnsSinceMove";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STACKS, stacks);
		bundle.put(TURNS_SINCE, turnsSinceMove);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		stacks = bundle.getInt(STACKS);
		turnsSinceMove = bundle.getInt(TURNS_SINCE);
	}
}
