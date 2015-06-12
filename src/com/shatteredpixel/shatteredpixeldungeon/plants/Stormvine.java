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
package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Stormvine extends Plant {

	private static final String TXT_DESC =
			"Gravity affects the Stormvine plant strangely, allowing its whispy blue tendrils " +
			"to 'hang' on the air. Anything caught in the vine is affected by this, and becomes disoriented.";

	{
		image = 9;
		plantName = "Stormvine";
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

		if (ch != null) {
			Buff.affect(ch, Vertigo.class, Vertigo.duration( ch ) );
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = "Stormvine";

			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_STORMVINE;

			plantClass = Stormvine.class;
			alchemyClass = PotionOfLevitation.class;
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
