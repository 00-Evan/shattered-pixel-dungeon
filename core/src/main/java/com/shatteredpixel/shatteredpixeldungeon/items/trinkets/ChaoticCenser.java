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

package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blizzard;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Inferno;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Regrowth;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StormCloud;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashMap;

public class ChaoticCenser extends Trinket {

	{
		image = ItemSpriteSheet.CHAOTIC_CENSER;
	}

	@Override
	protected int upgradeEnergyCost() {
		//6 -> 8(14) -> 10(24) -> 12(36)
		return 6+2*level();
	}

	@Override
	public String statsDesc() {
		if (isIdentified()){
			return Messages.get(this, "stats_desc", averageTurnsUntilGas(buffedLvl()));
		} else {
			return Messages.get(this, "stats_desc", averageTurnsUntilGas(0));
		}
	}

	public static int averageTurnsUntilGas(){
		return averageTurnsUntilGas(trinketLevel(ChaoticCenser.class));
	}

	public static int averageTurnsUntilGas(int level){
		if (level <= -1){
			return -1;
		} else {
			return 300 / (level + 1);
		}
	}

	public static class CenserGasTracker extends Buff {

		private int left = Integer.MAX_VALUE;
		private int safeAreaDelay = 100;

		@Override
		public boolean act() {

			int avgTurns = averageTurnsUntilGas();

			if (avgTurns == -1){
				spend(Random.NormalIntRange(2, 8));
				return true;
			} else if (left > avgTurns*1.1f){
				left = Random.IntRange((int) (avgTurns*0.9f), (int) (avgTurns*1.1f));
			}

			if (left < avgTurns/5){

				//censer will try to delay spawning gas in certain safe areas, atm just inside shops
				if (safeAreaDelay >= 0) {
					for (Char ch : Actor.chars()) {
						if (ch instanceof Shopkeeper
								&& Dungeon.level.distance(target.pos, ch.pos) <= 5
								&& new Ballistica(target.pos, ch.pos, Ballistica.STOP_SOLID).collisionPos == ch.pos) {
							int delay = Random.NormalIntRange(2, 8);
							spend(delay);
							safeAreaDelay -= delay;
							return true;
						}
					}
				}

				//scales quadratically from ~4% at avgTurns/5, to 36% at 0, to 100% at -avgTurns/5
				float triggerChance = (float) Math.pow( 1f - (left + avgTurns/5f)/(avgTurns/2f), 2);

				//trigger chance is linearly increased by 5% for each open adjacent cell after 2
				int openCells = 0;
				for (int i : PathFinder.NEIGHBOURS8){
					if (!Dungeon.level.solid[target.pos+i]){
						openCells++;
					}
				}
				if (openCells > 2){
					triggerChance += (openCells-2)*0.05f;
				}

				//if no target is present, quadratically scale back trigger chance again, strongly
				if (TargetHealthIndicator.instance.target() != null){
					triggerChance = (float)Math.pow(triggerChance, 3);
				}

				if (Random.Float() < triggerChance){
					produceGas();
					Sample.INSTANCE.play(Assets.Sounds.GAS);
					Dungeon.hero.interrupt();
					left += Random.IntRange((int) (avgTurns*0.9f), (int) (avgTurns*1.1f));
				}
			}

			//buff ticks an average of every 5 turns
			int delay = Random.NormalIntRange(2, 8);
			spend(delay);
			safeAreaDelay = Math.min(safeAreaDelay+delay, 100);
			left -= delay;

			return true;
		}

		private static String LEFT = "left";
		private static String SAFE_AREA_DELAY = "safe_area_delay";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(LEFT, left);
			bundle.put(SAFE_AREA_DELAY, safeAreaDelay);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			if (bundle.contains(LEFT)){
				left = bundle.getInt(LEFT);
				safeAreaDelay = bundle.getInt(SAFE_AREA_DELAY);
			}
		}
	}

	private static void produceGas(){
		int level = trinketLevel(ChaoticCenser.class);

		if (level < 0 || level > 3){
			return;
		}

		Class<?extends Blob> gasToSpawn;
		float gasQuantity;
		switch (Random.chances(GAS_CAT_CHANCES[level])){
			case 0: default:
				do {
					gasToSpawn = Random.element(COMMON_GASSES.keySet());
				} while (!Regeneration.regenOn() && gasToSpawn == Regrowth.class);
				gasQuantity = COMMON_GASSES.get(gasToSpawn);
				break;
			case 1:
				gasToSpawn = Random.element(UNCOMMON_GASSES.keySet());
				gasQuantity = UNCOMMON_GASSES.get(gasToSpawn);
				break;
			case 2:
				gasToSpawn = Random.element(RARE_GASSES.keySet());
				gasQuantity = RARE_GASSES.get(gasToSpawn);
				break;
		}

		Char target = null;
		if (TargetHealthIndicator.instance != null){
			target  = TargetHealthIndicator.instance.target();
		}

		HashMap<Integer, Float> candidateCells = new HashMap<>();
		PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.not(Dungeon.level.solid, null), 3);

		//spawn gas in a random cell 1-3 tiles away, likelihood is 2>3>1
		for (int i = 0; i < Dungeon.level.length(); i++){
			switch (PathFinder.distance[i]){
				case 0: default: break; //do nothing
				case 1: candidateCells.put(i, 1f); break;
				case 2: candidateCells.put(i, 3f); break;
				case 3: candidateCells.put(i, 2f); break;
			}
		}

		//unless we have a target, then strongly prefer cells closer to target
		if (target != null){
			float furthest = 0;
			for (int cell : candidateCells.keySet()){
				float dist = Dungeon.level.trueDistance(cell, target.pos);
				if (dist > furthest){
					furthest = dist;
				}
			}
			for (int cell : candidateCells.keySet()){
				float dist = Dungeon.level.trueDistance(cell, target.pos);
				candidateCells.put(cell, furthest - dist);
			}
		}

		if (!candidateCells.isEmpty()) {
			int targetCell = Random.chances(candidateCells);
			GameScene.add(Blob.seed(targetCell, (int) gasQuantity, gasToSpawn));
			MagicMissile.boltFromChar(Dungeon.hero.sprite.parent, MISSILE_VFX.get(gasToSpawn), Dungeon.hero.sprite, targetCell, null);
		}

	}

	private static final float[][] GAS_CAT_CHANCES = new float[4][3];
	static {
		GAS_CAT_CHANCES[0] = new float[]{70, 25, 5};
		GAS_CAT_CHANCES[1] = new float[]{60, 30, 10};
		GAS_CAT_CHANCES[2] = new float[]{50, 35, 15};
		GAS_CAT_CHANCES[3] = new float[]{40, 40, 20};
	}

	private static final HashMap<Class<? extends Blob>, Float> COMMON_GASSES = new HashMap<>();
	static {
		COMMON_GASSES.put(ToxicGas.class, 500f);
		COMMON_GASSES.put(ConfusionGas.class, 500f);
		COMMON_GASSES.put(Regrowth.class, 250f);
	}

	private static final HashMap<Class<? extends Blob>, Float> UNCOMMON_GASSES = new HashMap<>();
	static {
		UNCOMMON_GASSES.put(StormCloud.class, 500f);
		UNCOMMON_GASSES.put(SmokeScreen.class, 500f);
		UNCOMMON_GASSES.put(StenchGas.class, 250f);
	}

	private static final HashMap<Class<? extends Blob>, Float> RARE_GASSES = new HashMap<>();
	static {
		RARE_GASSES.put(Inferno.class, 500f);
		RARE_GASSES.put(Blizzard.class, 500f);
		RARE_GASSES.put(CorrosiveGas.class, 250f);
	}

	private static final HashMap<Class<? extends Blob>, Integer> MISSILE_VFX = new HashMap<>();
	static {
		MISSILE_VFX.put(ToxicGas.class, MagicMissile.SPECK + Speck.TOXIC);
		MISSILE_VFX.put(ConfusionGas.class, MagicMissile.SPECK + Speck.CONFUSION);
		MISSILE_VFX.put(Regrowth.class, MagicMissile.FOLIAGE);
		MISSILE_VFX.put(StormCloud.class, MagicMissile.SPECK + Speck.STORM);
		MISSILE_VFX.put(SmokeScreen.class, MagicMissile.SPECK + Speck.SMOKE);
		MISSILE_VFX.put(StenchGas.class, MagicMissile.SPECK + Speck.STENCH);
		MISSILE_VFX.put(Inferno.class, MagicMissile.SPECK + Speck.INFERNO);
		MISSILE_VFX.put(Blizzard.class, MagicMissile.SPECK + Speck.BLIZZARD);
		MISSILE_VFX.put(CorrosiveGas.class, MagicMissile.SPECK + Speck.CORROSION);
	}

}
