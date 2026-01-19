/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GooWarn;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GooSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ArcaneBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.ARCANE_BOMB;
	}

	@Override
	public boolean explodesDestructively() {
		return false;
	}

	@Override
	protected int explosionRange() {
		return 2;
	}

	@Override
	protected Fuse createFuse() {
		return new ArcaneBombFuse();
	}

	@Override
	public void explode(int cell) {
		super.explode(cell);
		
		ArrayList<Char> affected = new ArrayList<>();
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), explosionRange() );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
				Char ch = Actor.findChar(i);
				if (ch != null){
					affected.add(ch);
				}
			}
		}
		
		for (Char ch : affected){
			//pierces armor, and damage in 5x5 instead of 3x3
			int damage = Math.round(Random.NormalIntRange( 4 + Dungeon.scalingDepth(), 12 + 3*Dungeon.scalingDepth() ));
			ch.damage(damage, this);
			if (ch == Dungeon.hero && !ch.isAlive()){
				Badges.validateDeathFromFriendlyMagic();
				Dungeon.fail(this);
			}
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}

	//normal fuse logic, but with particle effects
	public static class ArcaneBombFuse extends Fuse {

		private ArrayList<Emitter> gooWarnEmitters = new ArrayList<>();

		//particle effect addition is delayed using an actor to ensure things like gamescene is set up when loading
		@Override
		public Fuse ignite(Bomb bomb) {
			super.ignite(bomb);
			Actor.add(new Actor() {
				{ actPriority = VFX_PRIO; }
				@Override
				protected boolean act() {
					int bombPos = -1;
					for (Heap heap : Dungeon.level.heaps.valueList()) {
						if (heap.items.contains(bomb)) {
							bombPos = heap.pos;
						}
					}
					if (bombPos != -1) {
						PathFinder.buildDistanceMap(bombPos, BArray.not(Dungeon.level.solid, null), bomb.explosionRange());
						for (int i = 0; i < PathFinder.distance.length; i++) {
							if (PathFinder.distance[i] < Integer.MAX_VALUE) {
								Emitter e = CellEmitter.get(i);
								if (e != null) {
									e.pour(GooSprite.GooParticle.FACTORY, 0.03f);
									gooWarnEmitters.add(e);
								}
							}
						}
					}
					Actor.remove(this);
					return true;
				}
			});
			return this;
		}

		@Override
		public void snuff() {
			super.snuff();
			for (Emitter e : gooWarnEmitters) {
				e.on = false;
			}
			gooWarnEmitters.clear();
		}
	}
}
