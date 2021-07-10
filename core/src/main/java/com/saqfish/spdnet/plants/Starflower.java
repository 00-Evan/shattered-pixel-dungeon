/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.plants;

import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Bless;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Recharging;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.hero.HeroSubClass;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;

public class Starflower extends Plant {

	{
		image = 9;
		seedClass = Seed.class;
	}

	@Override
	public void activate( Char ch ) {

		if (ch != null) {
			Buff.prolong(ch, Bless.class, Bless.DURATION);
			if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN){
				Buff.prolong(ch, Recharging.class, Recharging.DURATION);
			}
		}

	}

	public static class Seed extends Plant.Seed{

		{
			image = ItemSpriteSheet.SEED_STARFLOWER;

			plantClass = Starflower.class;
		}
		
		@Override
		public int value() {
			return 30 * quantity;
		}
	}
}
