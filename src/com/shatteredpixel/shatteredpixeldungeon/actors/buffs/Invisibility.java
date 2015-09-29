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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Invisibility extends FlavourBuff {

	public static final float DURATION	= 20f;

	{
		type = buffType.POSITIVE;
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {
			target.invisible++;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		if (target.invisible > 0)
			target.invisible--;
		super.detach();
	}
	
	@Override
	public int icon() {
		return BuffIndicator.INVISIBLE;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.INVISIBLE );
		else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
	}

	@Override
	public String toString() {
		return "Invisible";
	}

	@Override
	public String desc() {
		return "You are completely blended into the surrounding terrain, making you impossible to see.\n" +
				"\n" +
				"While you are invisible enemies are unable to attack or follow you. " +
				"Physical attacks and magical effects (such as scrolls and wands) will immediately cancel invisibility.\n" +
				"\n" +
				"This invisibility will last for " + dispTurns() + ".";
	}

	public static void dispel() {
		Invisibility buff = Dungeon.hero.buff( Invisibility.class );
		if (buff != null) {
			buff.detach();
		}
		CloakOfShadows.cloakStealth cloakBuff = Dungeon.hero.buff( CloakOfShadows.cloakStealth.class );
		if (cloakBuff != null) {
			cloakBuff.dispel();
		}
		//this isn't a form of invisibilty, but it is meant to dispel at the same time as it.
		TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff( TimekeepersHourglass.timeFreeze.class );
		if (timeFreeze != null) {
			timeFreeze.detach();
		}
	}
}
