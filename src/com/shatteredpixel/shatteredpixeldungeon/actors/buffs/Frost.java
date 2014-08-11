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
package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements.Resistance;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Frost extends FlavourBuff {

	private static final float DURATION	= 5f;
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			
			target.paralysed = true;
			Burning.detach( target, Burning.class );
			if (target instanceof Hero) {
				Hero hero = (Hero)target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof MysteryMeat) {
					item = item.detach( hero.belongings.backpack );
					FrozenCarpaccio carpaccio = new FrozenCarpaccio(); 
					if (!carpaccio.collect( hero.belongings.backpack )) {
						Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		target.paralysed = false;
		super.detach();
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}
	
	@Override
	public String toString() {
		return "Frozen";
	}
	
	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}
}
