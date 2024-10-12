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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class HolyTome extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_TOME;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = Math.min(level()+3, 10);

		defaultAction = AC_CAST;

		unique = true;
		bones = false;
	}

	public static final String AC_CAST = "CAST";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero )
				&& !cursed
				&& hero.buff(MagicImmune.class) == null
				&& charge > 0) {
			actions.add(AC_CAST);
		}
		return actions;
	}

	//levelling:
	//starts with 3 charges at +0, reaches 10 charges at +7. +8,9,10 slightly increases charge speed
	//levels up based on use, probably with a very similar target level system to the Cloak

	//how does the UI for actually using it work though?

	@Override
	protected ArtifactBuff passiveBuff() {
		return new tomeRecharge();
	}

	public class tomeRecharge extends ArtifactBuff{

		public void gainCharge(float levelPortion) {
			//TODO
		}

	}

}
