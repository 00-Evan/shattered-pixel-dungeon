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

package com.saqfish.spdnet.levels.traps;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.actors.Actor;
import com.saqfish.spdnet.actors.Char;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.actors.mobs.Mob;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.Speck;
import com.saqfish.spdnet.items.Heap;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.scrolls.ScrollOfTeleportation;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.utils.BArray;
import com.saqfish.spdnet.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WarpingTrap extends Trap {

	{
		color = TEAL;
		shape = STARS;
	}

	@Override
	public void activate() {
		CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		
		Char ch = Actor.findChar(pos);
		if (ch != null && !ch.flying) {
			if (ch instanceof Hero) {
				ScrollOfTeleportation.teleportHero((Hero) ch);
				BArray.setFalse(Dungeon.level.visited);
				BArray.setFalse(Dungeon.level.mapped);
				GameScene.updateFog();
				Dungeon.observe();
				
			} else {
				int count = 20;
				int pos;
				do {
					pos = Dungeon.level.randomRespawnCell( ch );
					if (count-- <= 0) {
						break;
					}
				} while (pos == -1 || Dungeon.level.secret[pos]);
				
				if (pos == -1 || Dungeon.bossLevel()) {
					
					GLog.w(Messages.get(ScrollOfTeleportation.class, "no_tele"));
					
				} else {
					
					ch.pos = pos;
					if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING) {
						((Mob) ch).state = ((Mob) ch).WANDERING;
					}
					ch.sprite.place(ch.pos);
					ch.sprite.visible = Dungeon.level.heroFOV[pos];
					
				}
			}
		}
		
		Heap heap = Dungeon.level.heaps.get(pos);
		
		if (heap != null){
			int cell = Dungeon.level.randomRespawnCell( null );
			
			Item item = heap.pickUp();
			
			if (cell != -1) {
				Dungeon.level.drop( item, cell );
			}
		}

	}
}
