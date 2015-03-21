/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfMight extends PotionOfStrength {

	{
		name = "Potion of Might";
		initials = "Mi";

        bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		
		hero.STR++;
		hero.HT += 5;
		hero.HP += 5;
		hero.sprite.showStatus( CharSprite.POSITIVE, "+1 str, +5 ht" );
		GLog.p( "Newfound strength surges through your body." );

        Badges.validateStrengthAttained();
	}
	
	@Override
	public String desc() {
		return
			"This powerful liquid will course through your muscles, permanently " +
			"increasing your strength by one point and health by five points.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 200 * quantity : super.price();
	}
}
