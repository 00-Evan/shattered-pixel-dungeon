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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SpectralWallParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class SkeletonKey extends Artifact {

	{
		image = ItemSpriteSheet.ARTIFACT_KEY;

		levelCap = 10;

		charge = 3+level()/2;
		partialCharge = 0;
		chargeCap = 3+level()/2;

		defaultAction = AC_INSERT;
	}

	public static final String AC_INSERT = "INSERT";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero)
				&& hero.buff(MagicImmune.class) == null
				&& !cursed) {
			actions.add(AC_INSERT);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals(AC_INSERT)){

			curUser = hero;

			if (!isEquipped( hero )) {
				GLog.i( Messages.get(Artifact.class, "need_to_equip") );

			} else if (cursed) {
				GLog.w( Messages.get(this, "cursed") );

			} else {
				GameScene.selectCell(targeter);
			}

		}
	}

	//levels when used, with bonus xp for opening locks that could be opened with keys
	public void gainExp( int xpGain ){
		if (level() == levelCap){
			return;
		}

		exp += xpGain;
		if (exp > 4+level()){
			exp -= 4+level();
			upgrade();
			GLog.p(Messages.get(this, "levelup"));
			Catalog.countUse(SkeletonKey.class);
		}

	}

	public CellSelector.Listener targeter = new CellSelector.Listener(){

		@Override
		public void onSelect(Integer target) {

			if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){

				if (target == curUser.pos){
					GLog.w(Messages.get(SkeletonKey.class, "invalid_target"));
					return;
				}

				if (Dungeon.level.adjacent(target, curUser.pos)) {
					if (Dungeon.level.map[target] == Terrain.LOCKED_EXIT){
						GLog.w(Messages.get(SkeletonKey.class, "wont_open"));
						return;
					}
					if (Dungeon.level.map[target] == Terrain.LOCKED_DOOR){
						if (Dungeon.level.locked){
							GLog.w(Messages.get(SkeletonKey.class, "wont_open"));
							return;
						}
						if (charge < 1){
							GLog.i( Messages.get(SkeletonKey.class, "iron_charges") );
							return;
						}
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						curUser.sprite.operate(target, new Callback() {
							@Override
							public void call() {
								Buff.affect(curUser, KeyReplacementTracker.class).processIronLockOpened();
								Level.set(target, Terrain.DOOR);
								GameScene.updateMap(target);
								charge -= 1;
								gainExp(2 + 1);
								Talent.onArtifactUsed(Dungeon.hero);
								curUser.spendAndNext(Actor.TICK);
								curUser.sprite.idle();
							}
						});
						curUser.busy();
						return;

					} else if (Dungeon.level.map[target] == Terrain.HERO_LKD_DR) {

						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						curUser.sprite.operate(target, new Callback() {
							@Override
							public void call() {
								Level.set(target, Terrain.DOOR);
								GameScene.updateMap(target);
								//no charge cost, no artifact on-use
								curUser.spendAndNext(Actor.TICK);
								curUser.sprite.idle();
							}
						});
						curUser.busy();
						return;
					} else if (Dungeon.level.map[target] == Terrain.CRYSTAL_DOOR) {

						if (charge < 5) {
							GLog.i(Messages.get(SkeletonKey.class, "crystal_charges"));
							return;
						}
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						curUser.sprite.operate(target, new Callback() {
							@Override
							public void call() {
								Buff.affect(curUser, KeyReplacementTracker.class).processCrystalLockOpened();
								Level.set(target, Terrain.EMPTY);
								GameScene.updateMap(target);
								charge -= 5;
								gainExp(2 + 5);
								Talent.onArtifactUsed(Dungeon.hero);
								Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
								CellEmitter.get( target ).start( Speck.factory( Speck.DISCOVER ), 0.025f, 20 );
								curUser.spendAndNext(Actor.TICK);
								curUser.sprite.idle();

								//if there is a distant well landmark above, remove it, as we just opened the door
								Notes.remove(Notes.Landmark.DISTANT_WELL, Dungeon.depth-1);
							}
						});
						curUser.busy();
						return;
					} else if (Dungeon.level.map[target] == Terrain.DOOR || Dungeon.level.map[target] == Terrain.OPEN_DOOR){

						if (charge < 2) {
							GLog.i(Messages.get(SkeletonKey.class, "lock_charges"));
							return;
						}

						//attempt to knock back char
						if (Actor.findChar(target) != null){

							Char toMove = Actor.findChar(target);

							int pushCell = -1;
							//push to the closest open cell that's further than the door
							for (int i : PathFinder.NEIGHBOURS8){
								if (!Dungeon.level.solid[target+i]
										&& Actor.findChar(target+i) == null
										&& (Dungeon.level.openSpace[target+i] || !Char.hasProp(toMove, Char.Property.LARGE))
										&& Dungeon.level.trueDistance(curUser.pos, target+i) > Dungeon.level.trueDistance(curUser.pos, target)
										&& (pushCell == -1 || Dungeon.level.trueDistance(curUser.pos, pushCell) > Dungeon.level.trueDistance(curUser.pos, target + i))){
									pushCell = target + i;
								}
							}

							if (pushCell != -1 && !Char.hasProp(toMove, Char.Property.IMMOVABLE)){
								Ballistica push = new Ballistica(target, pushCell, Ballistica.PROJECTILE);
								WandOfBlastWave.throwChar(toMove, push, 1, false, false, this);
								artifactProc(toMove, visiblyUpgraded(), 2);
							} else {
								GLog.w(Messages.get(SkeletonKey.class, "lock_no_space"));
								return;
							}
						}

						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						curUser.sprite.operate(target, new Callback() {
							@Override
							public void call() {
								Level.set(target, Terrain.HERO_LKD_DR);
								GameScene.updateMap(target);
								charge -= 2;
								gainExp(2);
								Talent.onArtifactUsed(Dungeon.hero);
								curUser.spendAndNext(Actor.TICK);
								curUser.sprite.idle();

								//throw items inside the door in random directions
								if (Dungeon.level.heaps.get(target) != null){
									ArrayList<Integer> candidates = new ArrayList<>();
									for (int n : PathFinder.NEIGHBOURS8){
										if (Dungeon.level.passable[target+n]){
											candidates.add(target+n);
										}
									}
									if (!candidates.isEmpty()){
										Heap heap = Dungeon.level.heaps.get(target);
										while (!heap.isEmpty()) {
											Dungeon.level.drop(heap.pickUp(), Random.element(candidates)).sprite.drop(target);
										}
									}
								}
							}
						});
						curUser.busy();
						return;

					} else if (Dungeon.level.heaps.get(target) != null && Dungeon.level.heaps.get(target).type == Heap.Type.LOCKED_CHEST){
						if (charge < 2) {
							GLog.i(Messages.get(SkeletonKey.class, "gold_charges"));
							return;
						}
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						curUser.sprite.operate(target, new Callback() {
							@Override
							public void call() {
								Buff.affect(curUser, KeyReplacementTracker.class).processGoldLockOpened();
								Dungeon.level.heaps.get(target).open(curUser);
								charge -= 2;
								gainExp(2 + 2);
								Talent.onArtifactUsed(Dungeon.hero);
								curUser.spendAndNext(Actor.TICK);
								curUser.sprite.idle();
							}
						});
						curUser.busy();
						return;

					} else if (Dungeon.level.heaps.get(target) != null && Dungeon.level.heaps.get(target).type == Heap.Type.CRYSTAL_CHEST){
						if (charge < 5) {
							GLog.i(Messages.get(SkeletonKey.class, "crystal_charges"));
							return;
						}
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
						curUser.sprite.operate(target, new Callback() {
							@Override
							public void call() {
								Buff.affect(curUser, KeyReplacementTracker.class).processCrystalLockOpened();
								Dungeon.level.heaps.get(target).open(curUser);
								charge -= 5;
								gainExp(2 + 5);
								Talent.onArtifactUsed(Dungeon.hero);
								curUser.spendAndNext(Actor.TICK);
								curUser.sprite.idle();
							}
						});
						curUser.busy();
						return;

					}
				}

				if (charge < 2){
					GLog.i(Messages.get(SkeletonKey.class, "wall_charges"));
					return;
				}

				int closest = curUser.pos;
				int closestIdx = -1;

				for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
					int ofs = PathFinder.CIRCLE8[i];
					if (Dungeon.level.trueDistance(target, curUser.pos+ofs) < Dungeon.level.trueDistance(target, closest)){
						closest = curUser.pos+ofs;
						closestIdx = i;
					}
				}

				int knockBackDir = PathFinder.CIRCLE8[closestIdx];

				if (Dungeon.level.solid[closest]){
					GLog.w(Messages.get(SkeletonKey.class, "invalid_target"));
					return;
				}

				int finalClosestIdx = closestIdx;
				Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
				curUser.sprite.operate(target, new Callback() {
					@Override
					public void call() {
						placeWall(curUser.pos+PathFinder.CIRCLE8[finalClosestIdx], knockBackDir);
						placeWall(curUser.pos+PathFinder.CIRCLE8[(finalClosestIdx +7)%8], knockBackDir);
						placeWall(curUser.pos+PathFinder.CIRCLE8[(finalClosestIdx +1)%8], knockBackDir);

						//if we're in a diagonal direction
						if (finalClosestIdx % 2 == 0){
							placeWall(curUser.pos+2*PathFinder.CIRCLE8[(finalClosestIdx +7)%8], knockBackDir);
							placeWall(curUser.pos+2*PathFinder.CIRCLE8[(finalClosestIdx +1)%8], knockBackDir);
						}

						charge -= 2;
						gainExp(2);

						Dungeon.observe();
						GameScene.updateFog();
						Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

						Talent.onArtifactUsed(Dungeon.hero);
						curUser.spendAndNext(Actor.TICK);
						curUser.sprite.idle();
					}
				});
				curUser.busy();

			}

		}

		@Override
		public String prompt() {
			return Messages.get(SkeletonKey.class, "prompt");
		}
	};

	@Override
	protected ArtifactBuff passiveBuff() {
		return new keyRecharge();
	}

	@Override
	public void charge(Hero target, float amount) {
		if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null){
			partialCharge += 0.133f*amount;
			while (partialCharge >= 1){
				partialCharge--;
				charge++;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
			}
			updateQuickslot();
		}
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped (Dungeon.hero) ){
			if (cursed){
				desc += "\n\n" + Messages.get(this, "desc_cursed");
			} else {
				desc += "\n\n" + Messages.get(this, "desc_worn");
			}
		}

		return desc;
	}

	public class keyRecharge extends ArtifactBuff {
		@Override
		public boolean act() {
			if (charge < chargeCap
					&& !cursed
					&& target.buff(MagicImmune.class) == null
					&& Regeneration.regenOn()) {
				//120 turns to charge at full, 60 turns to charge at 0/8
				float chargeGain = 1 / (120f - (chargeCap - charge)*7.5f);
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += chargeGain;

				while (partialCharge >= 1) {
					partialCharge --;
					charge ++;

					if (charge == chargeCap){
						partialCharge = 0;
					}
				}
			}

			updateQuickslot();

			spend( TICK );

			return true;
		}
	}

	@Override
	public Item upgrade() {
		chargeCap = 3 + (level()+1)/2;
		return super.upgrade();
	}

	private void placeWall(int pos, int knockbackDIR ){
		Blob wall = Dungeon.level.blobs.get(KeyWall.class);
		if (!Dungeon.level.solid[pos] || (wall != null && wall.cur[pos] > 0)) {
			GameScene.add(Blob.seed(pos, 10, KeyWall.class));

			Char ch = Actor.findChar(pos);
			if (ch != null && ch.alignment == Char.Alignment.ENEMY){
				WandOfBlastWave.throwChar(ch, new Ballistica(pos, pos+knockbackDIR, Ballistica.PROJECTILE), 1, false, false, this);
				artifactProc(ch, visiblyUpgraded(), 2);
			}
		}
	}

	public static class KeyWall extends Blob {

		{
			alwaysVisible = true;
		}

		@Override
		protected void evolve() {

			int cell;
			boolean cellEnded = false;

			Level l = Dungeon.level;
			for (int i = area.left; i < area.right; i++){
				for (int j = area.top; j < area.bottom; j++){
					cell = i + j*l.width();
					off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

					if (cur[cell] > 0 && off[cell] == 0){
						cellEnded = true;
					}

					//caps at 10 turns
					off[cell] = Math.min(off[cell], 9);

					volume += off[cell];

					l.losBlocking[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.LOS_BLOCKING) != 0;
					l.solid[cell] = off[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
					l.passable[cell] = off[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.PASSABLE) != 0;
					l.avoid[cell] = off[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.AVOID) != 0;
					l.updateOpenSpace(cell);
				}
			}

			if (cellEnded){
				Dungeon.observe();
			}
		}

		@Override
		public void seed(Level level, int cell, int amount) {
			super.seed(level, cell, amount);
			level.losBlocking[cell] = cur[cell] > 0 || (Terrain.flags[level.map[cell]] & Terrain.LOS_BLOCKING) != 0;
			level.solid[cell] = cur[cell] > 0 || (Terrain.flags[level.map[cell]] & Terrain.SOLID) != 0;
			level.passable[cell] = cur[cell] == 0 && (Terrain.flags[level.map[cell]] & Terrain.PASSABLE) != 0;
			level.avoid[cell] = cur[cell] == 0 && (Terrain.flags[level.map[cell]] & Terrain.AVOID) != 0;
			level.updateOpenSpace(cell);
		}

		@Override
		public void clear(int cell) {
			super.clear(cell);
			if (cur == null) return;
			Level l = Dungeon.level;
			l.losBlocking[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.LOS_BLOCKING) != 0;
			l.solid[cell] = cur[cell] > 0 || (Terrain.flags[l.map[cell]] & Terrain.SOLID) != 0;
			l.passable[cell] = cur[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.PASSABLE) != 0;
			l.avoid[cell] = cur[cell] == 0 && (Terrain.flags[l.map[cell]] & Terrain.AVOID) != 0;
			l.updateOpenSpace(cell);
		}

		@Override
		public void fullyClear() {
			super.fullyClear();
			Dungeon.level.buildFlagMaps();
		}

		@Override
		public void onBuildFlagMaps(Level l) {
			if (volume > 0){
				for (int i=0; i < l.length(); i++) {
					l.losBlocking[i] = l.losBlocking[i] || cur[i] > 0;
					l.solid[i] = l.solid[i] || cur[i] > 0;
					l.passable[i] = l.passable[i] && cur[i] == 0;
					l.avoid[i] = l.avoid[i] && cur[i] == 0;
					//openSpace will be updated as part of building flap maps
				}
			}
		}

		@Override
		public void use(BlobEmitter emitter) {
			super.use( emitter );
			emitter.pour(SpectralWallParticle.FACTORY, 0.02f );
		}

		@Override
		public String tileDesc() {
			return Messages.get(this, "desc");
		}

	}

	public static class KeyReplacementTracker extends Buff {

		public int[] ironKeysNeeded, goldenKeysNeeded, crystalKeysNeeded;

		{
			revivePersists = true;
			ironKeysNeeded = new int[26];
			Arrays.fill(ironKeysNeeded, -1);
			goldenKeysNeeded = new int[26];
			Arrays.fill(goldenKeysNeeded, -1);
			crystalKeysNeeded = new int[26];
			Arrays.fill(crystalKeysNeeded, -1);
		}

		public void setupKeysForDepth(){
			ironKeysNeeded[Dungeon.depth] = 0;
			goldenKeysNeeded[Dungeon.depth] = 0;
			crystalKeysNeeded[Dungeon.depth] = 0;

			for (Heap h : Dungeon.level.heaps.valueList()){
				if (h.type == Heap.Type.LOCKED_CHEST){
					goldenKeysNeeded[Dungeon.depth]++;
				} else if (h.type == Heap.Type.CRYSTAL_CHEST){
					crystalKeysNeeded[Dungeon.depth]++;
				}
			}

			for (int i = 0; i < Dungeon.level.length(); i++){
				if (Dungeon.level.map[i] == Terrain.LOCKED_DOOR){
					ironKeysNeeded[Dungeon.depth]++;
				} else if (Dungeon.level.map[i] == Terrain.CRYSTAL_DOOR){
					crystalKeysNeeded[Dungeon.depth]++;
				}
			}
		}

		//used if a level was reset, e.g. via unblessed ankh vs. boss
		public void clearDepth(){
			ironKeysNeeded[Dungeon.depth] = -1;
			goldenKeysNeeded[Dungeon.depth] = -1;
			crystalKeysNeeded[Dungeon.depth] = -1;
		}

		public void processIronLockOpened(){
			if (ironKeysNeeded[Dungeon.depth] == -1){
				setupKeysForDepth();
			}
			ironKeysNeeded[Dungeon.depth] -= 1;
			processExcessKeys();
		}

		public void processGoldLockOpened(){
			if (goldenKeysNeeded[Dungeon.depth] == -1){
				setupKeysForDepth();
			}
			goldenKeysNeeded[Dungeon.depth] -= 1;
			processExcessKeys();
		}

		public void processCrystalLockOpened(){
			if (crystalKeysNeeded[Dungeon.depth] == -1){
				setupKeysForDepth();
			}
			crystalKeysNeeded[Dungeon.depth] -= 1;
			processExcessKeys();
		}

		public void processExcessKeys(){
			int keysNeeded = ironKeysNeeded[Dungeon.depth];
			boolean removed = false;
			if (keysNeeded >= 0) {
				while (Notes.keyCount(new IronKey(Dungeon.depth)) > keysNeeded) {
					Notes.remove(new IronKey(Dungeon.depth));
					removed = true;
				}
			}
			keysNeeded = goldenKeysNeeded[Dungeon.depth];
			if (keysNeeded >= 0) {
				while (Notes.keyCount(new GoldenKey(Dungeon.depth)) > keysNeeded) {
					Notes.remove(new GoldenKey(Dungeon.depth));
					removed = true;
				}
			}
			keysNeeded = crystalKeysNeeded[Dungeon.depth];
			if (keysNeeded >= 0) {
				while (Notes.keyCount(new CrystalKey(Dungeon.depth)) > keysNeeded) {
					Notes.remove(new CrystalKey(Dungeon.depth));
					removed = true;
				}
			}
			if (removed){
				GameScene.updateKeyDisplay();
				GLog.i(Messages.get(SkeletonKey.class, "discard"));
			}
		}

		public static String IRON_NEEDED = "iron_needed";
		public static String GOLDEN_NEEDED = "golden_needed";
		public static String CRYSTAL_NEEDED = "crystal_needed";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(IRON_NEEDED, ironKeysNeeded);
			bundle.put(GOLDEN_NEEDED, goldenKeysNeeded);
			bundle.put(CRYSTAL_NEEDED, crystalKeysNeeded);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			ironKeysNeeded = bundle.getIntArray(IRON_NEEDED);
			goldenKeysNeeded = bundle.getIntArray(GOLDEN_NEEDED);
			crystalKeysNeeded = bundle.getIntArray(CRYSTAL_NEEDED);
		}

	}
}
