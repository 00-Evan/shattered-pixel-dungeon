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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Blindweed extends Plant {

	private static final String TXT_DESC = 
		"Upon being touched a Blindweed perishes in a bright flash of light. " +
        "The flash is strong enough to disorient for several seconds.";
	
	{
		image = 3;
		plantName = "Blindweed";
	}
	
	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);
		
		if (ch != null) {
            int len = Random.Int( 5, 10 );
			Buff.prolong( ch, Blindness.class, len );
            Buff.prolong( ch, Cripple.class, len );
			if (ch instanceof Mob) {
				if (((Mob)ch).state == ((Mob)ch).HUNTING) ((Mob)ch).state = ((Mob)ch).WANDERING;
				((Mob)ch).beckon( Dungeon.level.randomDestination() );
			}
		}
		
		if (Dungeon.visible[pos]) {
			CellEmitter.get( pos ).burst( Speck.factory( Speck.LIGHT ), 4 );
		}
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Blindweed";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_BLINDWEED;
			
			plantClass = Blindweed.class;
			alchemyClass = PotionOfInvisibility.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
