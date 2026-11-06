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

public class RingOfSharpshooting extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_SHARPSHOOT;
		buffClass = Aim.class;
	}

	//1 extra thrown weapon lvl of damage per level. Scales linearly!
	private static final float LVL_FACTOR = 1f;

	public static int levelDamageBonus( int bonus ){
		return (int) (LVL_FACTOR*bonus);
	}

	public static int levelDamageBonus( Char target ){
		return levelDamageBonus(getBuffedBonus(target, Aim.class));
	}

	//25% extra thrown weapon durability per level
	private static final float DUR_FACTOR = 1.25f;

	public static float durabilityMultiplier( int bonus ){
		return (float)(Math.pow(DUR_FACTOR, bonus));
	}

	public static float durabilityMultiplier( Char target ){
		return durabilityMultiplier(getBonus(target, Aim.class));
	}

	@Override
	protected RingBuff buff( ) {
		return new Aim();
	}

	public class Aim extends RingBuff {}

	@Override
	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats",
					levelDamageBonus(soloBonus()),
					Messages.decimalFormat("#.##", 100f * (durabilityMultiplier(soloBuffedBonus()) - 1f)));

			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
				info += "\n\n" + Messages.get(this, "combined_stats",
						levelDamageBonus(getBonus(Dungeon.hero, RingOfMight.Might.class)),
						Messages.decimalFormat("#.##", 100f * (durabilityMultiplier(combinedBuffedBonus(Dungeon.hero)) - 1f)));
			}
			return info;
		} else {
			return Messages.get(this, "typical_stats",
					levelDamageBonus(1),
					Messages.decimalFormat("#.##", 100*(durabilityMultiplier(1)-1f)));
		}
	}

	@Override
	public String upgradeStat1(int level) {
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Integer.toString(levelDamageBonus(level+1));
	}

	@Override
	public String upgradeStat2(int level) {
		if (cursed && cursedKnown) level = Math.min(-1, level-3);
		return Messages.decimalFormat("#.##", 100f * (durabilityMultiplier(level+1)-1f)) + "%";
	}
}
