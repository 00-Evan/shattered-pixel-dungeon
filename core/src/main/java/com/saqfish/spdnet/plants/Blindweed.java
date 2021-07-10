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

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.buffs.Blindness;
import com.saqfish.spdnet.actors.buffs.Buff;
import com.saqfish.spdnet.actors.buffs.Cripple;
import com.saqfish.spdnet.actors.buffs.Invisibility;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.hero.HeroSubClass;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;

public class Blindweed extends Plant {
	
	{
		image = 11;
		seedClass = Seed.class;
	}
	
	@Override
	public void activate( Char ch ) {
		
		if (ch != null) {
			if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN){
				Buff.affect(ch, Invisibility.class, Invisibility.DURATION/2f);
			} else {
				Buff.prolong(ch, Blindness.class, Blindness.DURATION);
				Buff.prolong(ch, Cripple.class, Cripple.DURATION);
				if (ch instanceof Mob) {
					if (((Mob) ch).state == ((Mob) ch).HUNTING) ((Mob) ch).state = ((Mob) ch).WANDERING;
					((Mob) ch).beckon(Dungeon.level.randomDestination( ch ));
				}
			}
		}
		
		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.LIGHT ), 4 );
		}
	}
	
	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_BLINDWEED;

			plantClass = Blindweed.class;
		}
	}
}
