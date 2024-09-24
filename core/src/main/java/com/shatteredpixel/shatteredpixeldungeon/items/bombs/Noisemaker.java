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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Noisemaker extends Bomb {
	
	{
		image = ItemSpriteSheet.NOISEMAKER;
	}

	@Override
	protected int explosionRange() {
		return 2;
	}

	@Override
	protected Fuse createFuse() {
		return new NoisemakerFuse();
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		//cannot pickup after first trigger
		if (fuse instanceof NoisemakerFuse && ((NoisemakerFuse) fuse).triggered){
			return false;
		}
		return super.doPickUp(hero, pos);
	}

	//does not instantly explode
	public static class NoisemakerFuse extends Fuse {

		private boolean triggered = false;

		private int left;

		@Override
		protected boolean act() {
			if (!triggered){
				//acts like a normal fuse until first trigger
				return super.act();
			} else {

				for (Heap heap : Dungeon.level.heaps.valueList()) {
					if (heap.items.contains(bomb)) {

						//active noisemakers cannot be snuffed out, blow it up!
						if (bomb.fuse != this){
							trigger(heap);

						//check if there is a nearby char, blow up if there is
						} else if (Actor.findChar(heap.pos) != null)  {


							heap.items.remove(bomb);
							if (heap.items.isEmpty()) {
								heap.destroy();
							}

							trigger(heap);

						//otherwise tick down our counter to alert
						} else {

							spend(TICK);
							left--;

							if (left <= 0){
								CellEmitter.center( heap.pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
								Sample.INSTANCE.play( Assets.Sounds.ALERT );

								for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
									mob.beckon( heap.pos );
								}
								left = 6;
							}
						}

						return true;
					}
				}

				//can't find our bomb, something must have removed it, do nothing.
				bomb.fuse = null;
				Actor.remove( this );
				return true;
			}
		}

		@Override
		//first trigger sets the alarm mechanism, second explodes
		protected void trigger(Heap heap) {
			if (!triggered) {
				triggered = true;
			} else {
				super.trigger(heap);
			}
		}

		@Override
		public boolean freeze() {
			if (!triggered) {
				return super.freeze();
			} else {
				//noisemakers cannot have their fuse snuffed once triggered
				return false;
			}
		}

		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			left = bundle.getInt(LEFT);
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 40);
	}
}
