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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class AwakenBuff extends FlavourBuff {
	//추적자의 수면버프 지속을 제한하기 위한 장치

	public static final float DURATION = 3f;
	//영추자의 수면은 3턴 이후 해제됨

	{
		type = buffType.NEUTRAL;

	}

	public boolean attachTo(Char target ) {
		if (target instanceof Mob && super.attachTo(target)) {
			if (cooldown() == 0) {
				spend(DURATION);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean act(){
		if (target instanceof Mob && ((Mob) target).state == ((Mob) target).SLEEPING && super.attachTo(target)) {
			((Mob) target).state = ((Mob) target).WANDERING;
		}
		detach();
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(visualcooldown()));
	}
}
