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

package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfEnergy extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_ENERGY;
	}

	public String statsInfo() {
		if (isIdentified()){
			String info = Messages.get(this, "stats",
					Messages.decimalFormat("#.##", 100f * (Math.pow(1.15f, soloBuffedBonus()) - 1f)));
			if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero, Energy.class)){
				info += "\n\n" + Messages.get(this, "combined_stats",
						Messages.decimalFormat("#.##", 100f * (Math.pow(1.15f, combinedBuffedBonus(Dungeon.hero, Energy.class)) - 1f)));
			}
			return info;
		} else {
			return Messages.get(this, "typical_stats",
					Messages.decimalFormat("#.##", 15f));
		}
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Energy();
	}
	
	public static float wandChargeMultiplier( Char target ){
		return (float)Math.pow(1.15, getBuffedBonus(target, Energy.class));
	}

	public static float artifactChargeMultiplier( Char target ){
		float bonus = (float)Math.pow(1.15, getBuffedBonus(target, Energy.class));

		if (target instanceof Hero && ((Hero) target).heroClass != HeroClass.ROGUE && ((Hero) target).hasTalent(Talent.LIGHT_CLOAK)){
			bonus *= 1f + (0.2f * ((Hero) target).pointsInTalent(Talent.LIGHT_CLOAK)/3f);
		}

		return bonus;
	}

	public static float armorChargeMultiplier( Char target ){
		return (float)Math.pow(1.15, getBuffedBonus(target, Energy.class));
	}
	
	public class Energy extends RingBuff {
	}
}
