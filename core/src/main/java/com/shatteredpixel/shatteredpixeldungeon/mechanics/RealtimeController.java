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
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.input.RealtimeInput;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
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
 * Optimized for zero-allocation performance.
 */
public class RealtimeController {

	// Use squared distances to avoid expensive Math.sqrt() calls
	private static final float PICKUP_RANGE_SQ = 1.5f * 1.5f;      // 2.25
	private static final float INTERACTION_RANGE_SQ = 1.6f * 1.6f; // 2.56

	/**
	 * Performs realtime interaction for the hero.
	 * Priority order: Stairs → Locked Doors → Containers → Items
	 *
	 * @param hero The hero performing the interaction
	 */
	public static void performInteraction(Hero hero) {
		if (!RealtimeInput.isEnabled() || Dungeon.level == null) return;

		// A) Try level transitions (stairs/portals)
		if (tryLevelTransition(hero)) return;

		// B) Try unlocking adjacent doors
		if (tryUnlockAdjacentDoors(hero)) return;

		// C) Try interacting with containers
		Heap target = scanForTarget(hero);
		if (target != null && tryInteractWithHeap(hero, target)) return;

		// D) Fallback to item pickup
		hero.waitOrPickup = true;
		hero.pickup(null);
	}

	/**
	 * Attempts to activate a level transition if hero is standing on one.
	 * InterlevelScene handles all saving internally.
	 *
	 * @return true if transition was activated
	 */
	private static boolean tryLevelTransition(Hero hero) {
		LevelTransition transition = Dungeon.level.getTransition(hero.pos);
		if (transition == null) return false;
		if (!transition.inside(hero.pos)) return false;
		if (Dungeon.level.locked) return false;
		if (Dungeon.level.plants.containsKey(hero.pos)) return false;
		if (Dungeon.depth >= 26 && transition.type != LevelTransition.Type.REGULAR_ENTRANCE) return false;

		GLog.i("RealtimeController: Activating transition from depth %d to depth %d (type: %s)",
			Dungeon.depth, transition.destDepth, transition.type);
		return Dungeon.level.activateTransition(hero, transition);
	}

	/**
	 * Scans for unlockable doors in adjacent cells and attempts to unlock them.
	 *
	 * @return true if a door was unlocked
	 */
	private static boolean tryUnlockAdjacentDoors(Hero hero) {
		for (int off : PathFinder.NEIGHBOURS8) {
			int cell = hero.pos + off;
			if (!Dungeon.level.insideMap(cell)) continue;

			int tile = Dungeon.level.map[cell];
			if (tile == Terrain.LOCKED_DOOR) {
				if (tryUnlockIronDoor(hero, cell)) return true;
			} else if (tile == Terrain.CRYSTAL_DOOR) {
				if (tryUnlockCrystalDoor(hero, cell)) return true;
			} else if (tile == Terrain.LOCKED_EXIT) {
				if (tryUnlockExit(hero, cell)) return true;
			}
		}
		return false;
	}

	private static boolean tryUnlockIronDoor(Hero hero, int cell) {
		if (Notes.keyCount(new IronKey(Dungeon.depth)) <= 0) {
			GLog.w(Messages.get(hero, "locked_door"));
			return false;
		}

		Notes.remove(new IronKey(Dungeon.depth));
		GameScene.updateKeyDisplay();
		Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
		Level.set(cell, Terrain.DOOR);
		GameScene.updateMap(cell);
		com.shatteredpixel.shatteredpixeldungeon.levels.features.Door.enter(cell);
		return true;
	}

	private static boolean tryUnlockCrystalDoor(Hero hero, int cell) {
		if (Notes.keyCount(new CrystalKey(Dungeon.depth)) <= 0) {
			GLog.w(Messages.get(hero, "locked_door"));
			return false;
		}

		Notes.remove(new CrystalKey(Dungeon.depth));
		GameScene.updateKeyDisplay();
		Level.set(cell, Terrain.EMPTY);
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		CellEmitter.get(cell).start(Speck.factory(Speck.DISCOVER), 0.025f, 20);
		GameScene.updateMap(cell);
		return true;
	}

	private static boolean tryUnlockExit(Hero hero, int cell) {
		if (Notes.keyCount(new SkeletonKey(Dungeon.depth)) <= 0) {
			GLog.w(Messages.get(hero, "locked_door"));
			return false;
		}

		Notes.remove(new SkeletonKey(Dungeon.depth));
		GameScene.updateKeyDisplay();
		Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
		Level.set(cell, Terrain.UNLOCKED_EXIT);
		GameScene.updateMap(cell);
		return true;
	}

	/**
	 * Scans for the closest interactable container within pickup range.
	 * Uses distance squared for zero-allocation performance.
	 *
	 * @return closest container or null
	 */
	private static Heap scanForTarget(Hero hero) {
		Heap best = null;
		float bestDistSq = PICKUP_RANGE_SQ;
		int w = Dungeon.level.width();

		for (Heap h : Dungeon.level.heaps.valueList()) {
			if (h == null) continue;
			if (h.type == Heap.Type.HEAP || h.type == Heap.Type.FOR_SALE) continue;
			if (!Dungeon.level.heroFOV[h.pos]) continue;

			// Calculate distance squared (zero allocation)
			int hx = h.pos % w;
			int hy = h.pos / w;
			float dx = hx - hero.exactX;
			float dy = hy - hero.exactY;
			float distSq = dx * dx + dy * dy;

			if (distSq <= bestDistSq) {
				best = h;
				bestDistSq = distSq;
			}
		}

		return best;
	}

	/**
	 * Attempts to interact with a heap (open container or pickup item).
	 * Uses distance squared check and position snapping for engine compatibility.
	 *
	 * @return true if interaction was successful
	 */
	private static boolean tryInteractWithHeap(Hero hero, Heap heap) {
		// Verify heap is within interaction range (using distance squared)
		int w = Dungeon.level.width();
		int hx = heap.pos % w;
		int hy = heap.pos / w;
		float dx = hx - hero.exactX;
		float dy = hy - hero.exactY;
		float distSq = dx * dx + dy * dy;

		if (distSq > INTERACTION_RANGE_SQ) return false;

		// Snap hero position for engine compatibility
		int savedPos = hero.pos;
		try {
			hero.pos = heap.pos;
			if (hero.sprite != null) {
				hero.sprite.interruptMotion();
				hero.sprite.idle();
			}

			openHeap(hero, heap);
			return true;
		} finally {
			hero.pos = savedPos;
		}
	}

	/**
	 * Opens a heap, handling locked containers with key checks.
	 */
	private static void openHeap(Hero hero, Heap heap) {
		switch (heap.type) {
			case LOCKED_CHEST:
				if (tryUnlockChest(hero, heap, new GoldenKey(Dungeon.depth))) {
					heap.open(hero);
					GLog.i("Manual Unlock Success.");
				} else {
					GLog.w(Messages.get(hero, "locked_chest"));
				}
				break;

			case CRYSTAL_CHEST:
				if (tryUnlockChest(hero, heap, new CrystalKey(Dungeon.depth))) {
					heap.open(hero);
					GLog.i("Manual Unlock Success.");
				} else {
					GLog.w(Messages.get(hero, "locked_chest"));
				}
				break;

			case CHEST:
			case TOMB:
			case SKELETON:
			case REMAINS:
				heap.open(hero);
				GLog.i("Forced container open at %d", heap.pos);
				break;

			default:
				heap.open(hero);
				break;
		}
	}

	/**
	 * Attempts to unlock a chest with a specific key type.
	 *
	 * @return true if key was available and consumed
	 */
	private static boolean tryUnlockChest(Hero hero, Heap heap, Key key) {
		if (Notes.keyCount(key) <= 0) return false;

		Notes.remove(key);
		GameScene.updateKeyDisplay();
		Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
		return true;
	}

}
