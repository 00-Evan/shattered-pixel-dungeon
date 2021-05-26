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

package com.elementalpixel.elementalpixeldungeon.items.stones;

import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.actors.Actor;
import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.FlavourBuff;
import com.elementalpixel.elementalpixeldungeon.actors.mobs.Mob;
import com.elementalpixel.elementalpixeldungeon.effects.CellEmitter;
import com.elementalpixel.elementalpixeldungeon.effects.Speck;
import com.elementalpixel.elementalpixeldungeon.items.Heap;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class StoneOfAggression extends Runestone {
	
	{
		image = ItemSpriteSheet.STONE_AGGRESSION;
	}
	
	@Override
	protected void activate(int cell) {
		
		Char ch = Actor.findChar( cell );
		
		if (ch != null) {
			if (ch.alignment == Char.Alignment.ENEMY) {
				Buff.prolong(ch, Aggression.class, Aggression.DURATION / 5f);
			} else {
				Buff.prolong(ch, Aggression.class, Aggression.DURATION);
			}
			CellEmitter.center(cell).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
			Sample.INSTANCE.play( Assets.Sounds.READ );
		} else {
			//Item.onThrow
			Heap heap = Dungeon.level.drop( this, cell );
			if (!heap.isEmpty()) {
				heap.sprite.drop( cell );
			}
		}
		
	}
	
	public static class Aggression extends FlavourBuff {
		
		public static final float DURATION = 20f;
		
		{
			type = buffType.NEGATIVE;
			announced = true;
		}
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle(bundle);
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
		}
		
		@Override
		public void detach() {
			//if our target is an enemy, reset the aggro of any enemies targeting it
			if (target.isAlive()) {
				if (target.alignment == Char.Alignment.ENEMY) {
					for (Mob m : Dungeon.level.mobs) {
						if (m.alignment == Char.Alignment.ENEMY && m.isTargeting(target)) {
							m.aggro(null);
						}
					}
				}
			}
			super.detach();
			
		}
		
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
		
	}
	
}
