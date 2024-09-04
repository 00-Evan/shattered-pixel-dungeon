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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DimensionalSundial extends Trinket {

	{
		image = ItemSpriteSheet.SUNDIAL;
	}

	@Override
	protected int upgradeEnergyCost() {
		//6 -> 8(14) -> 10(24) -> 12(36)
		return 6+2*level();
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this,
					"stats_desc",
					(int)(100*(1f - enemySpawnMultiplierDaytime(buffedLvl()))),
					(int)(100*(enemySpawnMultiplierNighttime(buffedLvl())-1f)));
		} else {
			return Messages.get(this, "typical_stats_desc",
					(int)(100*(1f - enemySpawnMultiplierDaytime(0))),
					(int)(100*(enemySpawnMultiplierNighttime(0)-1f)));
		}
	}

	public static boolean sundialWarned = false;

	public static float spawnMultiplierAtCurrentTime(){
		if (trinketLevel(DimensionalSundial.class) != -1) {
			Calendar cal = GregorianCalendar.getInstance();
			if (cal.get(Calendar.HOUR_OF_DAY) >= 20 || cal.get(Calendar.HOUR_OF_DAY) <= 7) {
				if (!sundialWarned){
					GLog.w(Messages.get(DimensionalSundial.class, "warning"));
					sundialWarned = true;
				}
				return enemySpawnMultiplierNighttime();
			} else {
				return enemySpawnMultiplierDaytime();
			}
		} else {
			return 1f;
		}
	}

	public static float enemySpawnMultiplierDaytime(){
		return enemySpawnMultiplierDaytime(trinketLevel(DimensionalSundial.class));
	}

	public static float enemySpawnMultiplierDaytime( int level ){
		if (level == -1){
			return 1f;
		} else {
			return 0.95f - 0.05f*level;
		}
	}

	public static float enemySpawnMultiplierNighttime(){
		return enemySpawnMultiplierNighttime(trinketLevel(DimensionalSundial.class));
	}

	public static float enemySpawnMultiplierNighttime( int level ){
		if (level == -1){
			return 1f;
		} else {
			return 1.25f + 0.25f*level;
		}
	}
}
