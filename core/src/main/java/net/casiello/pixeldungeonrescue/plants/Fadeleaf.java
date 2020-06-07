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
import net.casiello.pixeldungeonrescue.actors.buffs.Buff;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.actors.hero.HeroSubClass;
import net.casiello.pixeldungeonrescue.actors.mobs.Mob;
import net.casiello.pixeldungeonrescue.effects.CellEmitter;
import net.casiello.pixeldungeonrescue.effects.Speck;
import net.casiello.pixeldungeonrescue.items.artifacts.TimekeepersHourglass;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTeleportation;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.InterlevelScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.GLog;
import com.watabou.noosa.Game;

public class Fadeleaf extends Plant {
	
	{
		image = 10;
	}
	
	@Override
	public void activate( final Char ch ) {
		
		if (ch instanceof Hero) {
			
			((Hero)ch).curAction = null;
			
			if (((Hero) ch).subClass == HeroSubClass.WARDEN){
				
				if (Dungeon.bossLevel()) {
					GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
					return;
					
				}
				
				Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null) buff.detach();
				buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
				if (buff != null) buff.detach();
				
				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1));
				InterlevelScene.returnPos = -2;
				Game.switchScene( InterlevelScene.class );
				
			} else {
				ScrollOfTeleportation.teleportHero((Hero) ch);
			}
			
		} else if (ch instanceof Mob && !ch.properties().contains(Char.Property.IMMOVABLE)) {

			if (!Dungeon.bossLevel()) {

				int count = 10;
				int newPos;
				do {
					newPos = Dungeon.level.randomRespawnCell(ch);
					if (count-- <= 0) {
						break;
					}
				} while (newPos == -1);

				if (newPos != -1) {

					ch.pos = newPos;
					if (((Mob) ch).state == ((Mob) ch).HUNTING)
						((Mob) ch).state = ((Mob) ch).WANDERING;
					ch.sprite.place(ch.pos);
					ch.sprite.visible = Dungeon.level.heroFOV[ch.pos];

				}
			}

		}
		
		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.get( pos ).start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		}
	}
	
	public static class Seed extends Plant.Seed {
		{
			image = ItemSpriteSheet.SEED_FADELEAF;

			plantClass = Fadeleaf.class;
		}
	}
}
