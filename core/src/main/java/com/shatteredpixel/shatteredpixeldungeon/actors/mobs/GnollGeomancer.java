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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DarkGold;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollGeomancerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollGeomancer extends Mob {

	{
		//TODO
		HP = HT = 150;
		spriteClass = GnollGeomancerSprite.class;

		EXP = 20;

		//acts after other mobs, just like sappers
		actPriority = MOB_PRIO-1;

		SLEEPING = new Sleeping();
		HUNTING = new Hunting();
		state = SLEEPING;

		//can see the hero from a distance
		viewDistance = 12;

		properties.add(Property.BOSS);
	}

	private int abilityCooldown = Random.NormalIntRange(3, 5);

	//TODO do we want to allow for multple rock throws at once here?
	private int throwingRockFromPos = -1;
	private int throwingRockToPos = -1;

	@Override
	protected boolean act() {
		if (throwingRockFromPos != -1){
			GnollGeomancer.doRockThrowAttack(this, throwingRockFromPos, throwingRockToPos);

			throwingRockFromPos = -1;
			throwingRockToPos = -1;

			spend(TICK);
			return false;
		} else {
			return super.act();
		}

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

					//ensure we don't do enough damage to break the armor at the start
					if (wasSleeping) dmg = Math.min(dmg, 15);

					dmg = Math.min(dmg, buff(RockArmor.class).shielding());

					damage(dmg, p);
					sprite.bloodBurstA(Dungeon.hero.sprite.center(), dmg);
					sprite.flash();

					hits++;
					if (hits == 1){
						GLog.w( Messages.get(GnollGeomancer.this, "warning"));
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

						state = HUNTING;
						enemy = Dungeon.hero;
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

		//geomancer cannot be hit through multiple brackets at a time
		if ((beforeHitHP/hpBracket - HP/hpBracket) >= 2){
			HP = hpBracket * ((beforeHitHP/hpBracket)-1) + 1;
		}

		//taking damage from full HP does not trigger a jump
		if (beforeHitHP != HT && beforeHitHP / hpBracket != HP / hpBracket) {
			//this is a start, but need a lot more fight logic
			int target;
			do {
				target = Random.Int(Dungeon.level.length());
			} while (!Dungeon.level.insideMap(target) || Dungeon.level.distance(pos, target) != 10);
			carveRock(target);
			Buff.affect(this, RockArmor.class).setShield(30);
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

	private class Hunting extends Mob.Hunting {

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (!enemyInFOV){
				spend(TICK);
				return true;
			} else {
				enemySeen = true;
				//sprite.showStatus(CharSprite.DEFAULT, "seen");

				//TODO cooldown
				if (abilityCooldown-- <= 0){
					//do we care?
					boolean targetNextToBarricade = false;
					for (int i : PathFinder.NEIGHBOURS8){
						if (Dungeon.level.map[enemy.pos+i] == Terrain.BARRICADE
								|| Dungeon.level.map[enemy.pos+i] == Terrain.ENTRANCE){
							targetNextToBarricade = true;
							break;
						}
					}

					// 50/50 to either throw a rock or do rockfall
					// unless target is next to a barricade, then always try to throw
					// unless nothing to throw, then always rockfall
					Ballistica aim = GnollGeomancer.prepRockThrowAttack(enemy, GnollGeomancer.this);
					if (aim != null && (targetNextToBarricade || Random.Int(2) == 0)) {

						throwingRockFromPos = aim.sourcePos;
						throwingRockToPos = aim.collisionPos;

						Ballistica warnPath = new Ballistica(aim.sourcePos, aim.collisionPos, Ballistica.STOP_SOLID);
						for (int i : warnPath.subPath(0, warnPath.dist)){
							sprite.parent.add(new TargetedCell(i, 0xFF0000));
						}

						Dungeon.hero.interrupt();
						abilityCooldown = Random.NormalIntRange(3, 5);
						spend(GameMath.gate(TICK, (int)Math.ceil(enemy.cooldown()), 3*TICK));
						return true;
					} else if (GnollGeomancer.prepRockFallAttack(enemy, GnollGeomancer.this, 3, true)) {
						Dungeon.hero.interrupt();
						spend(GameMath.gate(TICK, (int)Math.ceil(enemy.cooldown()), 3*TICK));
						abilityCooldown = Random.NormalIntRange(3, 5);
						return true;
					}
				}

				spend(TICK);
				return true;
			}
		}

	}

	//*** These methods are public static as their logic is also accessed by gnoll sappers ***

	public static Ballistica prepRockThrowAttack( Char target, Char source ){
		ArrayList<Integer> candidateRocks = new ArrayList<>();

		for (int i = 0; i < Dungeon.level.length(); i++){
			if (source.fieldOfView[i] && Dungeon.level.map[i] == Terrain.MINE_BOULDER){
				if (new Ballistica(i, target.pos, Ballistica.PROJECTILE).collisionPos == target.pos){
					candidateRocks.add(i);
				}
			}
		}

		if (candidateRocks.isEmpty()){
			return null;
		} else {

			//throw closest rock to enemy
			int throwingFromPos = candidateRocks.get(0);
			for (int i : candidateRocks){
				if (Dungeon.level.trueDistance(i, target.pos) < Dungeon.level.trueDistance(throwingFromPos, target.pos)){
					throwingFromPos = i;
				}
			}
			int throwingToPos = target.pos;

			return new Ballistica(throwingFromPos, throwingToPos, Ballistica.PROJECTILE);

		}
	}

	public static void doRockThrowAttack( Char source, int from, int to ){

		Level.set(from, Terrain.EMPTY);
		GameScene.updateMap(from);
		source.sprite.attack(from, new Callback() {
			@Override
			public void call() {
				//do nothing
			}
		});

		Ballistica rockPath = new Ballistica(from, to, Ballistica.MAGIC_BOLT);

		Sample.INSTANCE.play(Assets.Sounds.MISS);
		((MissileSprite)source.sprite.parent.recycle( MissileSprite.class )).
				reset( from, rockPath.collisionPos, new GnollGeomancer.Boulder(), new Callback() {
					@Override
					public void call() {
						Splash.at(rockPath.collisionPos, ColorMath.random( 0x444444, 0x777766 ), 15);
						Sample.INSTANCE.play(Assets.Sounds.ROCKS);

						Char ch = Actor.findChar(rockPath.collisionPos);
						if (ch == Dungeon.hero){
							PixelScene.shake( 3, 0.7f );
						} else {
							PixelScene.shake(0.5f, 0.5f);
						}

						if (ch != null && !(ch instanceof GnollGeomancer)){
							ch.damage(Random.NormalIntRange(5, 10), this);

							if (ch.isAlive()){
								Buff.prolong( ch, Paralysis.class, ch instanceof GnollGuard ? 10 : 3 );
							} else if (!ch.isAlive() && ch == Dungeon.hero) {
								Badges.validateDeathFromEnemyMagic();
								Dungeon.fail( source.getClass() );
								GLog.n( Messages.get( GnollGeomancer.class, "rock_kill") );
							}

							if (rockPath.path.size() > rockPath.dist+1) {
								Ballistica trajectory = new Ballistica(ch.pos, rockPath.path.get(rockPath.dist + 1), Ballistica.MAGIC_BOLT);
								WandOfBlastWave.throwChar(ch, trajectory, 1, false, false, source);
							}
						} else if (ch == null) {
							Dungeon.level.pressCell(rockPath.collisionPos);
						}

						source.next();
					}
				} );
	}

	public static class Boulder extends Item {
		{
			image = ItemSpriteSheet.GEO_BOULDER;
		}
	}

	//similar overall logic as DM-300's rock fall attack, but with more parameters
	public static boolean prepRockFallAttack( Char target, Char source, int range, boolean avoidBarricades ){
		final int rockCenter = target.pos;

		int safeCell;
		do {
			safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (safeCell == source.pos
				|| (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
				|| (Dungeon.level.traps.containsKey(safeCell) && Random.Int(2) == 0));

		ArrayList<Integer> rockCells = new ArrayList<>();

		int start = rockCenter - Dungeon.level.width() * range - range;
		int pos;
		for (int y = 0; y < 1+2*range; y++) {
			pos = start + Dungeon.level.width() * y;
			for (int x = 0; x < 1+2*range; x++) {
				if (!Dungeon.level.insideMap(pos)) {
					pos++;
					continue;
				}
				if (avoidBarricades){
					boolean barricade = false;
					for (int j : PathFinder.NEIGHBOURS9){
						if (Dungeon.level.map[pos+j] == Terrain.BARRICADE
								|| Dungeon.level.map[pos+j] == Terrain.ENTRANCE){
							barricade = true;
						}
					}
					if (barricade){
						pos++;
						continue;
					}
				}
				//add rock cell to pos, if it is not solid, isn't the safecell, and isn't where geomancer is standing
				if (!Dungeon.level.solid[pos]
						&& pos != safeCell
						&& !(Actor.findChar(pos) instanceof GnollGeomancer)
						&& Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
					rockCells.add(pos);
				}
				pos++;
			}
		}
		for (int i : rockCells){
			source.sprite.parent.add(new TargetedCell(i, 0xFF0000));
		}
		//don't want to overly punish players with slow move or attack speed
		Buff.append(source, GnollRockFall.class, GameMath.gate(TICK, (int)Math.ceil(target.cooldown()), 3*TICK)).setRockPositions(rockCells);

		source.sprite.attack(target.pos, new Callback() {
			@Override
			public void call() {
				//do nothing
			}
		});

		return true;
	}

	public static class GnollRockFall extends DelayedRockFall{

		@Override
		public void affectChar(Char ch) {
			Buff.prolong(ch, Paralysis.class, ch instanceof GnollGuard ? 10 : 3);
		}

		@Override
		public void affectCell(int cell) {
			if (Random.Int(3) == 0) {
				Level.set(cell, Terrain.MINE_BOULDER);
				GameScene.updateMap(cell);
			}
		}

	}

	public static class RockArmor extends ShieldBuff { }

	public static final String HITS = "hits";

	private static final String ABILITY_COOLDOWN = "ability_cooldown";
	private static final String ROCK_FROM_POS = "rock_from_pos";
	private static final String ROCK_TO_POS = "rock_to_pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(HITS, hits);
		bundle.put(ABILITY_COOLDOWN, abilityCooldown);
		bundle.put(ROCK_FROM_POS, throwingRockFromPos);
		bundle.put(ROCK_TO_POS, throwingRockToPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		hits = bundle.getInt(HITS);
		abilityCooldown = bundle.getInt(ABILITY_COOLDOWN);
		throwingRockFromPos = bundle.getInt(ROCK_FROM_POS);
		throwingRockToPos = bundle.getInt(ROCK_TO_POS);
	}
}
