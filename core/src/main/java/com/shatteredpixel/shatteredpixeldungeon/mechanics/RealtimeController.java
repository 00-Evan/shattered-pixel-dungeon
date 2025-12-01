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

package com.shatteredpixel.shatteredpixeldungeon.mechanics;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.input.RealtimeInput;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

/**
 * Stateless controller for realtime interaction logic.
 * Handles spacebar interaction: doors/stairs first, then containers/items.
 */
public class RealtimeController {

	private static final float PICKUP_RANGE = 1.5f;

	/**
	 * Performs realtime interaction for the hero.
	 * Handles level transitions, locked doors, containers, and item pickup.
	 *
	 * @param hero The hero performing the interaction
	 */
	public static void performInteraction(Hero hero) {
		if (!RealtimeInput.isEnabled() || Dungeon.level == null) return;

		// A) Stairs/Level transitions: if standing on a transition and the level isn't locked, activate it
		LevelTransition transition = Dungeon.level.getTransition(hero.pos);
		if (transition != null
				&& transition.inside(hero.pos)
				&& !Dungeon.level.locked
				&& !Dungeon.level.plants.containsKey(hero.pos)
				&& (Dungeon.depth < 26 || transition.type == LevelTransition.Type.REGULAR_ENTRANCE)) {
			// Trigger transition immediately; bypass turn system
			if (Dungeon.level.activateTransition(hero, transition)) {
				return;
			}
		}

		// B) Locked doors adjacent: direct manual unlock (no operate flow)
		for (int off : PathFinder.NEIGHBOURS8) {
			int cell = hero.pos + off;
			if (!Dungeon.level.insideMap(cell)) continue;
			int tile = Dungeon.level.map[cell];

			if (tile == Terrain.LOCKED_DOOR) {
				if (Notes.keyCount(new IronKey(Dungeon.depth)) > 0) {
					Notes.remove(new IronKey(Dungeon.depth));
					GameScene.updateKeyDisplay();
					Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
					Level.set(cell, Terrain.DOOR);
					GameScene.updateMap(cell);
					// Optionally auto-open immediately
					com.shatteredpixel.shatteredpixeldungeon.levels.features.Door.enter(cell);
					return;
				} else {
					GLog.w(Messages.get(hero, "locked_door"));
					// don't early-return; still allow other interactions this frame
				}
			} else if (tile == Terrain.CRYSTAL_DOOR) {
				if (Notes.keyCount(new CrystalKey(Dungeon.depth)) > 0) {
					Notes.remove(new CrystalKey(Dungeon.depth));
					GameScene.updateKeyDisplay();
					Level.set(cell, Terrain.EMPTY);
					Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
					CellEmitter.get(cell).start(Speck.factory(Speck.DISCOVER), 0.025f, 20);
					GameScene.updateMap(cell);
					return;
				} else {
					GLog.w(Messages.get(hero, "locked_door"));
				}
			} else if (tile == Terrain.LOCKED_EXIT) {
				if (Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {
					Notes.remove(new SkeletonKey(Dungeon.depth));
					GameScene.updateKeyDisplay();
					Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
					Level.set(cell, Terrain.UNLOCKED_EXIT);
					GameScene.updateMap(cell);
					return;
				} else {
					GLog.w(Messages.get(hero, "locked_door"));
				}
			}
		}

		// 1) Prioritize interactive containers within range (1.5 tiles)

		Heap bestContainer = null;
		float bestDist = Float.MAX_VALUE;
		int w = Dungeon.level.width();
		for (Heap h : Dungeon.level.heaps.valueList()){
			if (h == null) continue;
			if (h.type == Heap.Type.HEAP || h.type == Heap.Type.FOR_SALE) continue; // only containers
			if (!Dungeon.level.heroFOV[h.pos]) continue;
			int hx = h.pos % w;
			int hy = h.pos / w;
			float dx = hx - hero.exactX;
			float dy = hy - hero.exactY;
			float dist = (float)Math.sqrt(dx*dx + dy*dy);
			if (dist <= PICKUP_RANGE && dist < bestDist){
				bestContainer = h;
				bestDist = dist;
			}
		}

		if (bestContainer != null){
			// Expanded radius tolerance for interaction
			float tol = 1.6f;
			int hx = bestContainer.pos % w;
			int hy = bestContainer.pos / w;
			float dx = hx - hero.exactX;
			float dy = hy - hero.exactY;
			float dist = (float)Math.sqrt(dx*dx + dy*dy);
			if (dist <= tol){
				int savedPos = hero.pos;
				try {
					// FORCE ON-TOP SNAP: engine requires hero.pos == heap.pos to open
					hero.pos = bestContainer.pos;
					// stop any motion/animations that could block immediate open
					if (hero.sprite != null) {
						hero.sprite.interruptMotion();
						hero.sprite.idle();
					}
					// Direct hotwire by heap type (manual key logic for locked/crystal)
					switch (bestContainer.type) {
						case LOCKED_CHEST: {
							boolean hasKey = Notes.keyCount(new GoldenKey(Dungeon.depth)) > 0;
							if (hasKey) {
								Notes.remove(new GoldenKey(Dungeon.depth));
								GameScene.updateKeyDisplay();
								Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
								bestContainer.open(hero);
								GLog.i("Manual Unlock Success.");
							} else {
								GLog.w(Messages.get(hero, "locked_chest"));
							}
							break;
						}
						case CRYSTAL_CHEST: {
							boolean hasKey = Notes.keyCount(new CrystalKey(Dungeon.depth)) > 0;
							if (hasKey) {
								Notes.remove(new CrystalKey(Dungeon.depth));
								GameScene.updateKeyDisplay();
								Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
								bestContainer.open(hero);
								GLog.i("Manual Unlock Success.");
							} else {
								GLog.w(Messages.get(hero, "locked_chest"));
							}
							break;
						}
						case CHEST:
						case TOMB:
						case SKELETON:
						case REMAINS:
							bestContainer.open(hero);
							GLog.i("Forced container open at %d", bestContainer.pos);
							break;
						default:
							bestContainer.open(hero);
							break;
					}


				} finally {
					hero.pos = savedPos;
				}
				return; // consume action
			}
		}


		// 2) Fallback to item pickup using our realtime pickup logic
		hero.waitOrPickup = true;
		hero.pickup(null);

		// 3) Optional: else could attempt an attack here if desired
	}
}
