/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

public class AdrenalineSurge extends Buff {

	public static float DURATION = 200f;
	
	{
		type = buffType.POSITIVE;
	}
	
	private int boost;
	private float interval;
	
	public void reset(int boost, float interval){
		this.boost = boost;
		this.interval = interval;
		spend(interval - cooldown());
	}
	
	public int boost(){
		return boost;
	}
	
	@Override
	public boolean act() {
		boost --;
		if (boost > 0){
			spend( interval );
		} else {
			detach();
		}
		return true;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.UPGRADE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 0.5f, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)visualcooldown());
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", boost, dispTurns(visualcooldown()));
	}
	
	private static final String BOOST	    = "boost";
	private static final String INTERVAL	    = "interval";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( BOOST, boost );
		bundle.put( INTERVAL, interval );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		boost = bundle.getInt( BOOST );
		interval = bundle.getFloat(INTERVAL);
	}
}
