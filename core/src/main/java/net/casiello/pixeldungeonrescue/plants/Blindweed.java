/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package net.casiello.pixeldungeonrescue.plants;

import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.actors.Char;
import net.casiello.pixeldungeonrescue.actors.buffs.Blindness;
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.buffs.Cripple;
import net.casiello.pixeldungeonrescue.actors.buffs.Invisibility;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.actors.hero.HeroSubClass;
import net.casiello.pixeldungeonrescue.actors.mobs.Mob;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Blindweed extends Plant {
	
	{
		image = 11;
	}
	
	@Override
	public void activate( Char ch ) {
		
		if (ch != null) {
			if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN){
				Buff.affect(ch, Invisibility.class, 10f);
			} else {
				int len = Random.Int(5, 10);
				Buff.prolong(ch, Blindness.class, len);
				Buff.prolong(ch, Cripple.class, len);
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
