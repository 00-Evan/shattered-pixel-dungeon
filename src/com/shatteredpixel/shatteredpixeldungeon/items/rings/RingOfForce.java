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
package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;

public class RingOfForce extends Ring {

	{
		name = "Ring of Force";
	}

	@Override
	protected RingBuff buff( ) {
		return new Force();
	}

	@Override
	public String desc() {
		if (isKnown()){
			String desc = "This ring enhances the force of the wearer's blows. " +
					"This extra power is largely wasted when wielding weapons, " +
					"but an unarmed attack will be made much stronger. " +
					"A degraded ring will instead weaken the wearer's blows.\n\n" +
					"When unarmed, at your current strength, ";
			int str = Dungeon.hero.STR() - 8;
			desc += levelKnown ?
					"average damage with this ring is " + (str/2+level + (int)(str*0.5f*level) + str*2)/2 + " points per hit.":
					"typical average damage with this ring is" + (str/2+1 + (int)(str*0.5f) + str*2)/2 + " points per hit.";
			desc += " Wearing a second ring of force would enhance this.";
			return desc;
		} else
			return super.desc();
	}

	public class Force extends RingBuff {
	}
}

