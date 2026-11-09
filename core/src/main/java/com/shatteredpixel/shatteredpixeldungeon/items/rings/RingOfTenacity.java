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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfTenacity extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_TENACITY;
		buffClass = Tenacity.class;
	}

	//damage taken is reduced by up to 15% per level
	private static final float MAX_DMG_FACTOR = 0.85f;

	public static float maxDamageMultiplier( int bonus ){
		return (float)Math.pow(MAX_DMG_FACTOR, bonus);
	}

	public static float maxDamageMultiplier( Char target ){
		return maxDamageMultiplier(getBuffedBonus( target, Tenacity.class));
	}

	public static float effectiveDamageMultiplier( int bonus, float HPPercent ){
		return (float)Math.pow(MAX_DMG_FACTOR, bonus*HPPercent);
	}

	public static float effectiveDamageMultiplier( Char target ){
		float missingHPPercent = ((float)(target.HT - target.HP)/target.HT);
		return effectiveDamageMultiplier(getBuffedBonus( target, Tenacity.class), missingHPPercent);
	}

	@Override
	protected RingBuff buff( ) {
		return new Tenacity();
	}

	public class Tenacity extends RingBuff {}

	@Override
	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats",
					Messages.decimalFormat("#.##", 100f * (1f - maxDamageMultiplier(soloBuffedBonus()))));

			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
				info += "\n\n" + Messages.get(this, "combined_stats",
						Messages.decimalFormat("#.##", 100f * (1f - maxDamageMultiplier(combinedBuffedBonus(Dungeon.hero)))));
			}

			return info;
		} else {
			return Messages.get(this, "typical_stats", Messages.decimalFormat("#.##", 100*(1f - maxDamageMultiplier(1))));
		}
	}

	@Override
	public String upgradeStat1(int level){
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Messages.decimalFormat("#.##", 100f * (1f - maxDamageMultiplier(level+1))) + "%";
	}
}

