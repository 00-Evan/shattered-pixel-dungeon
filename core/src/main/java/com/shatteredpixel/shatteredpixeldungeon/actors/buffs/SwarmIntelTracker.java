/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2026 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class SwarmIntelTracker extends Buff {

	private int alertRange = 0;
	private float leftAtZero;

	@Override
	public boolean act() {

		alertRange = 0;
		float lowestCooldown = 1;
		for (Mob m : Dungeon.level.mobs){
			if (target.fieldOfView != null
					&& target.fieldOfView.length == Dungeon.level.length()
					&& target.fieldOfView[m.pos]) {
				alertRange = Math.max(alertRange, m.swarmAlertRange());
			}
			if (m.cooldown() < lowestCooldown){
				lowestCooldown = m.cooldown();
			}
		}

		if (alertRange > 0){
			leftAtZero = 5f;
		} else {
			leftAtZero -= target.cooldown();
		}

		//always acts right after the next mob, or 1 turn at most
		spend(lowestCooldown);
		return true;
	}

	@Override
	public int icon() {
		if (alertRange == 0 && leftAtZero <= 0){
			return BuffIndicator.NONE;
		} else {
			return BuffIndicator.TARGETED;
		}
	}

	@Override
	public float iconFadePercent() {
		return (12-alertRange)/12f;
	}

	@Override
	public void tintIcon(Image icon) {
		if (alertRange == 0){
			icon.brightness(0.5f);
		} else {
			icon.resetColor();
		}
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(alertRange);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", alertRange);
	}

	public static final String ALERT_RANGE = "alert_range";
	public static final String LEFT = "left";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ALERT_RANGE, alertRange);
		bundle.put(LEFT, leftAtZero);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		alertRange = bundle.getInt(ALERT_RANGE);
		leftAtZero = bundle.getInt(LEFT);
	}
}
