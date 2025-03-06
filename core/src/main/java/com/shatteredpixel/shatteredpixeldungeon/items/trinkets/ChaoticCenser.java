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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
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
				spend(Random.NormalIntRange(1, 5));
				return true;
			} else if (left > avgTurns*1.2f){
				left = Random.IntRange((int) (avgTurns*0.833f), (int) (avgTurns*1.2f));
			}

			if (left <= 0) {

				Char enemy = null;

				if (TargetHealthIndicator.instance != null && TargetHealthIndicator.instance.isVisible()
						&& TargetHealthIndicator.instance.target() != null
						&& TargetHealthIndicator.instance.target().alignment == Char.Alignment.ENEMY
						&& TargetHealthIndicator.instance.target().isAlive()) {

					if (produceGas(TargetHealthIndicator.instance.target())){
						Sample.INSTANCE.play(Assets.Sounds.GAS, 0.5f);
						Dungeon.hero.interrupt();
						left += Random.IntRange((int) (avgTurns * 0.9f), (int) (avgTurns * 1.1f));
					}
				}

			}

			//buff ticks an average of every 3 turns
			int delay = Random.NormalIntRange(1, 3);
			spend(delay);
			left = (int)Math.max(left-delay, -avgTurns/3f);

			return true;
		}

		private static String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			if (bundle.contains(LEFT)){
				left = bundle.getInt(LEFT);
			}
		}
	}

	private static boolean produceGas( Char target ){
		int level = trinketLevel(ChaoticCenser.class);

		if (level < 0 || level > 3){
			return false;
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

		HashMap<Integer, Float> candidateCells = new HashMap<>();
		PathFinder.buildDistanceMap(Dungeon.hero.pos, BArray.not(Dungeon.level.solid, null), 6);

		//spawn gas in a random visible cell 2-6 tiles away
		for (int i = 0; i < Dungeon.level.length(); i++){
			if (Dungeon.level.heroFOV[i] && PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (PathFinder.distance[i] >= 2 && PathFinder.distance[i] <= 6) {
					candidateCells.put(i, 0f);
				}
			}
		}

		//strongly prefer cells closer to target
		int targetpos = target.pos;
		if (Dungeon.level.trueDistance(target.pos, Dungeon.hero.pos) >= 4){
			//if target is a distance from the hero, aim in front of them instead
			for (int i : PathFinder.NEIGHBOURS8){
				while (!Dungeon.level.solid[targetpos+i]
						&& Dungeon.level.trueDistance(target.pos+i, Dungeon.hero.pos) < Dungeon.level.trueDistance(targetpos, Dungeon.hero.pos)){
					targetpos = target.pos+i;
				}
			}
		}
		float closest = 100;
		for (int cell : candidateCells.keySet()){
			float dist = Dungeon.level.distance(cell, targetpos);
			if (dist < closest){
				closest = dist;
			}
		}
		for (int cell : candidateCells.keySet()){
			float dist = Dungeon.level.distance(cell, targetpos);
			if (dist - closest == 0) {
				candidateCells.put(cell, 8f);
			} else if (dist - closest <= 1) {
				candidateCells.put(cell, 1f);
			} else {
				candidateCells.put(cell, 0f);
			}
		}

		if (!candidateCells.isEmpty()) {
			Integer targetCell = Random.chances(candidateCells);
			if (targetCell != null) {
				Buff.affect(Dungeon.hero, GasSpewer.class, Dungeon.hero.cooldown()).set(targetCell, gasToSpawn, (int)gasQuantity);
				GLog.w(Messages.get(ChaoticCenser.class, "spew", Messages.titleCase(Messages.get(gasToSpawn, "name")) ));
				if (target.sprite != null && target.sprite.parent != null) {
					target.sprite.parent.addToBack(new TargetedCell(targetCell, 0xFF0000));
				}
				return true;
			}
		}

		return false;

	}

	public static class GasSpewer extends FlavourBuff {

		private int targetCell;

		private int depth;
		private int branch;

		private Class<?extends Blob> gasType;
		private int gasQuantity;

		public void set( int targetCell, Class<?extends Blob> gasType, int gasQuantity){
			this.targetCell = targetCell;

			depth = Dungeon.depth;
			branch = Dungeon.branch;

			this.gasType = gasType;
			this.gasQuantity = gasQuantity;
		}

		@Override
		public boolean act() {

			if (depth == Dungeon.depth && branch == Dungeon.branch){
				GameScene.add(Blob.seed(targetCell, gasQuantity, gasType));

				//corrosion starts at the same level as potion of corrosive gas
				if (gasType == CorrosiveGas.class){
					((CorrosiveGas)Dungeon.level.blobs.get(CorrosiveGas.class)).setStrength( 2 + Dungeon.scalingDepth()/5, ChaoticCenser.class);
				}

				MagicMissile.boltFromChar(Dungeon.hero.sprite.parent, MISSILE_VFX.get(gasType), Dungeon.hero.sprite, targetCell, null);
				Sample.INSTANCE.play(Assets.Sounds.GAS);
			}

			detach();
			return true;
		}

		private static final String CELL = "cell";
		private static final String DEPTH = "depth";
		private static final String BRANCH = "branch";
		private static final String GAS_TYPE = "gas_type";
		private static final String GAS_QUANTITY = "gas_quantity";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(CELL, targetCell);
			bundle.put(DEPTH, depth);
			bundle.put(BRANCH, branch);
			bundle.put(GAS_TYPE, gasType);
			bundle.put(GAS_QUANTITY, gasQuantity);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			targetCell = bundle.getInt(CELL);
			depth = bundle.getInt(DEPTH);
			branch = bundle.getInt(BRANCH);
			gasType = bundle.getClass(GAS_TYPE);
			gasQuantity = bundle.getInt(GAS_QUANTITY);
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
		COMMON_GASSES.put(ToxicGas.class, 300f);
		COMMON_GASSES.put(ConfusionGas.class, 300f);
		COMMON_GASSES.put(Regrowth.class, 200f);
	}

	private static final HashMap<Class<? extends Blob>, Float> UNCOMMON_GASSES = new HashMap<>();
	static {
		UNCOMMON_GASSES.put(StormCloud.class, 300f);
		UNCOMMON_GASSES.put(SmokeScreen.class, 300f);
		UNCOMMON_GASSES.put(StenchGas.class, 200f);
	}

	private static final HashMap<Class<? extends Blob>, Float> RARE_GASSES = new HashMap<>();
	static {
		RARE_GASSES.put(Inferno.class, 300f);
		RARE_GASSES.put(Blizzard.class, 300f);
		RARE_GASSES.put(CorrosiveGas.class, 200f);
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
