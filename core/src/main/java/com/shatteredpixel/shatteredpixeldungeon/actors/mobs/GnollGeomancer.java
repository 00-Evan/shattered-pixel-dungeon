/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGeomancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollGeomancer extends Mob {

	{
		//TODO
		HP = HT = 100;
		spriteClass = GnollGeomancerSprite.class;

		EXP = 20;

		//acts after other mobs?
		actPriority = MOB_PRIO-1;

		SLEEPING = new Sleeping();
		state = SLEEPING;

		properties.add(Property.BOSS);
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return super.isInvulnerable(effect) || (buff(RockArmor.class) != null && effect != Pickaxe.class);
	}

	@Override
	public boolean heroShouldInteract() {
		return super.heroShouldInteract() || buff(RockArmor.class) != null;
	}

	@Override
	protected boolean getCloser(int target) {
		return false;
	}

	@Override
	protected boolean getFurther(int target) {
		return false;
	}

	int hits = 0;
	int phase = 0;

	@Override
	public boolean interact(Char c) {
		if (c != Dungeon.hero || buff(RockArmor.class) == null) {
			return super.interact(c);
		} else {
			final Pickaxe p = Dungeon.hero.belongings.getItem(Pickaxe.class);

			if (p == null){
				return true;
			}

			Dungeon.hero.sprite.attack(pos, new Callback() {
				@Override
				public void call() {
					//does its own special damage calculation that's only influenced by pickaxe level and augment
					//we pretend the geomancer is the owner here so that properties like hero str or or other equipment do not factor in
					int dmg = p.damageRoll(GnollGeomancer.this);

					boolean wasSleeping = state == SLEEPING;

					//ensure we don't do enough damage to break the barrier at the start
					if (wasSleeping) dmg = Math.min(dmg, 15);

					dmg = Math.min(dmg, buff(RockArmor.class).shielding());

					damage(dmg, p);
					sprite.bloodBurstA(Dungeon.hero.sprite.center(), dmg);
					sprite.flash();

					hits++;
					if (hits == 1){
						GLog.n( Messages.get(GnollGeomancer.this, "warning"));
					} if (hits == 3){
						GLog.n( Messages.get(GnollGeomancer.this, "alert"));
						wasSleeping = false;
						spend(TICK);
						sprite.idle();

						//this is a start, but need a lot more fight logic
						int target;
						do {
							target = Random.Int(Dungeon.level.length());
						} while (!Dungeon.level.insideMap(target) || Dungeon.level.distance(pos, target) != 10);
						carveRock(target);
					}

					if (wasSleeping) {
						state = SLEEPING;
						alerted = false;
					}

					if (buff(RockArmor.class) == null){
						sprite.idle();
					}

					Sample.INSTANCE.play(Assets.Sounds.MINE, 1f, Random.Float(0.85f, 1.15f));
					Invisibility.dispel(Dungeon.hero);
					Dungeon.hero.spendAndNext(p.delayFactor(GnollGeomancer.this));
				}
			});

			return false;
		}
	}

	@Override
	public void damage(int dmg, Object src) {
		int hpBracket = HT / 3;

		int beforeHitHP = HP;
		super.damage(dmg, src);
		dmg = beforeHitHP - HP;

		//geomancer cannot be hit through multiple brackets at a time
		if ((beforeHitHP/hpBracket - HP/hpBracket) >= 2){
			HP = hpBracket * ((beforeHitHP/hpBracket)-1) + 1;
		}

		if (beforeHitHP / hpBracket != HP / hpBracket) {
			//this is a start, but need a lot more fight logic
			int target;
			do {
				target = Random.Int(Dungeon.level.length());
			} while (!Dungeon.level.insideMap(target) || Dungeon.level.distance(pos, target) != 10);
			carveRock(target);
			Buff.affect(this, RockArmor.class).setShield(50);
		}
	}

	private void carveRock(int target){
		Ballistica path = new Ballistica(pos, target, Ballistica.STOP_TARGET);

		ArrayList<Integer> cells = new ArrayList<>(path.subPath(0, path.dist));
		cells.addAll(spreadDiamondAOE(cells));
		cells.addAll(spreadDiamondAOE(cells));
		cells.addAll(spreadDiamondAOE(cells));

		ArrayList<Integer> exteriorCells = spreadDiamondAOE(cells);

		for (int i : cells){
			if (Dungeon.level.map[i] == Terrain.WALL_DECO){
				Dungeon.level.drop(new DarkGold(), i).sprite.drop();
			}
			if (Dungeon.level.solid[i]){
				//TODO boulders?
				Dungeon.level.map[i] = Terrain.EMPTY_DECO;
			}
			CellEmitter.get( i - Dungeon.level.width() ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
		}
		for (int i : exteriorCells){
			if (!Dungeon.level.solid[i]
					&& Dungeon.level.map[i] != Terrain.EMPTY_SP
					&& Dungeon.level.traps.get(i) == null
					&& Dungeon.level.plants.get(i) == null
					&& Actor.findChar(i) == null){
				Dungeon.level.map[i] = Terrain.MINE_BOULDER;
			}
		}
		//we potentially update a lot of cells, so might as well just reset properties instead of incrementally updating
		Dungeon.level.buildFlagMaps();
		Dungeon.level.cleanWalls();
		GameScene.updateMap();
		GameScene.updateFog();
		Dungeon.observe();

		PixelScene.shake(3, 0.7f);
		Sample.INSTANCE.play(Assets.Sounds.ROCKS);

		int oldpos = pos;
		pos = target;
		Actor.add(new Pushing(this, oldpos, pos));
	}

	private ArrayList<Integer> spreadDiamondAOE(ArrayList<Integer> currentCells){
		ArrayList<Integer> spreadCells = new ArrayList<>();
		for (int i : currentCells){
			for (int j : PathFinder.NEIGHBOURS4){
				if (Dungeon.level.insideMap(i+j) && !spreadCells.contains(i+j) && !currentCells.contains(i+j)){
					spreadCells.add(i+j);
				}
			}
		}
		return spreadCells;
	}

	@Override
	public String description() {
		if (state == SLEEPING){
			return Messages.get(this, "desc_sleeping");
		} else {
			return super.description();
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		Blacksmith.Quest.beatBoss();
	}

	private class Sleeping extends Mob.Sleeping {

		@Override
		protected void awaken(boolean enemyInFOV) {
			//do nothing, has special awakening rules
		}
	}

	public static class RockArmor extends ShieldBuff { }
}
