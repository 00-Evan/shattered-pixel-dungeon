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

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HighOrderKnightArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PapalKnightArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PastorClothe;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class HolyLight extends Light {
	
	{
		type = buffType.POSITIVE;
	}
	@Override
	public boolean act() {
		if (target.isAlive()) {
			if (	   !( hero.belongings.armor instanceof PastorClothe)
					&& !( hero.belongings.armor instanceof PapalKnightArmor)
					&& !( hero.belongings.armor instanceof HighOrderKnightArmor)
					&& !( hero.belongings.armor instanceof ClassArmor)		) {
				detach();
			}
		}
		return true;
	}

}
