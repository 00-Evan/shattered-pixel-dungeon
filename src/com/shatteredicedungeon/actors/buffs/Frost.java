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
package com.shatteredicedungeon.actors.buffs;

import com.shatteredicedungeon.Dungeon;
import com.shatteredicedungeon.actors.Char;
import com.shatteredicedungeon.actors.hero.Hero;
import com.shatteredicedungeon.actors.mobs.Thief;
import com.shatteredicedungeon.items.Item;
import com.shatteredicedungeon.items.food.FrozenCarpaccio;
import com.shatteredicedungeon.items.food.MysteryMeat;
import com.shatteredicedungeon.items.potions.Potion;
import com.shatteredicedungeon.items.rings.RingOfElements.Resistance;
import com.shatteredicedungeon.levels.Level;
import com.shatteredicedungeon.sprites.CharSprite;
import com.shatteredicedungeon.ui.BuffIndicator;
import com.shatteredicedungeon.utils.GLog;

public class Frost extends FlavourBuff {

	private static final String TXT_FREEZES = "%s freezes!";

	private static final float DURATION	= 5f;

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			
			target.paralysed = true;
			Buff.detach( target, Burning.class );
			Buff.detach( target, Chill.class );

			if (target instanceof Hero) {

				Hero hero = (Hero)target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Potion) {

					item = item.detach( hero.belongings.backpack );
					GLog.w(TXT_FREEZES, item.toString());
					((Potion) item).shatter(hero.pos);

				} else if (item instanceof MysteryMeat) {

					item = item.detach( hero.belongings.backpack );
					FrozenCarpaccio carpaccio = new FrozenCarpaccio();
					if (!carpaccio.collect( hero.belongings.backpack )) {
						Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
					}
					GLog.w(TXT_FREEZES, item.toString());

				}
			} else if (target instanceof Thief && ((Thief)target).item instanceof Potion) {

				((Potion) ((Thief)target).item).shatter( target.pos );
				((Thief) target).item = null;

			}


			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		Paralysis.unfreeze( target );
		if (Level.water[target.pos]){
			Buff.prolong(target, Chill.class, 4f);
		}
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.FROZEN);
		else target.sprite.remove(CharSprite.State.FROZEN);
	}

	@Override
	public String toString() {
		return "Frozen";
	}

	@Override
	public String desc() {
		return "Not to be confused with freezing solid, this more benign freezing simply encases the target in ice.\n" +
				"\n" +
				"Freezing acts similarly to paralysis, making it impossible for the target to act. " +
				"Unlike paralysis, freezing is immediately cancelled if the target takes damage, as the ice will shatter.\n" +
				"\n" +
				"The freeze will last for " + dispTurns() + ", or until the target takes damage.\n";
	}

	public static float duration( Char ch ) {
		Resistance r = ch.buff( Resistance.class );
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}
}
