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

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Barkskin extends Buff {

	private int level = 0;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			spend( TICK );
			if (--level <= 0) {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}
	
	public int level() {
		return level;
	}
	
	public void level( int value ) {
		if (level < value) {
			level = value;
		}
	}
	
	@Override
	public int icon() {
		return BuffIndicator.BARKSKIN;
	}
	
	@Override
	public String toString() {
		return "Barkskin";
	}

	@Override
	public String desc() {
		return "Your skin is hardened, it feels rough and solid like bark.\n" +
				"\n" +
				"The hardened skin increases your effective armor, allowing you to better defend against physical attack. " +
				"The armor bonus will decrease by one point each turn until it expires.\n" +
				"\n" +
				"Your armor is currently increased by " + level +".";
	}
}
