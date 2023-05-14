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


import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;

import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BluntTangSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.TangSword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Armorbroken extends Buff {

	{
		type = buffType.NEGATIVE;
	}

	private int brokenlevel;
	private int interval = 1;


	@Override
	public boolean act() {


		if (target.isAlive()) {

			if (brokenlevel <= 0) {
				detach();
			}

		} else {
			detach();
		}

		return true;
	}

	public int level() {
			return brokenlevel + BluntTangSword.addBrokenlevel + TangSword.addBrokenlevel;
	}
	
	public void set( int value, int time ) {
		//decide whether to override, preferring high value + low interval
		if (Math.sqrt(interval)*brokenlevel <= Math.sqrt(time)*value) {
			brokenlevel = value;
			interval = time;
			spend(time - cooldown() - 1);
		}
	}
	
	@Override
	public int icon() {
		return BuffIndicator.VULNERABLE;
	}

	@Override
	public float iconFadePercent() {
		if (target instanceof Hero){
			float max = ((Hero) target).lvl;
			return Math.max(0, (max-brokenlevel)/max);
		}
		return 0;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(brokenlevel);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", brokenlevel, dispTurns(visualcooldown()));
	}
	
	private static final String LEVEL	    = "level";
	private static final String INTERVAL    = "interval";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( INTERVAL, interval );
		bundle.put( LEVEL, brokenlevel );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		interval = bundle.getInt( INTERVAL );
		brokenlevel = bundle.getInt( LEVEL );
	}
}
