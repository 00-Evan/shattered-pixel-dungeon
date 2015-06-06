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
package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;

public class Icecap extends Plant {

	private static final String TXT_DESC = 
		"Upon being touched, an Icecap lets out a puff of freezing pollen. " +
        "The freezing effect is much stronger if the environment is wet.";
	
	{
		image = 1;
		plantName = "Icecap";
	}
	
	@Override
	public void activate() {
		
		PathFinder.buildDistanceMap( pos, BArray.not( Level.losBlocking, null ), 1 );
		
		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		
		for (int i=0; i < Level.LENGTH; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Freezing.affect( i, fire );
			}
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Icecap";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_ICECAP;
			
			plantClass = Icecap.class;
			alchemyClass = PotionOfFrost.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
