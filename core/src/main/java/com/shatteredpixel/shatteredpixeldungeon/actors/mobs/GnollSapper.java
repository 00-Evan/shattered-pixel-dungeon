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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSapperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollSapper extends Mob {

	{
		//always acts after guards, makes it easier to kite them into attacks
		actPriority = Actor.MOB_PRIO-1;

		spriteClass = GnollSapperSprite.class;

		HP = HT = 50;
		defenseSkill = 15;

		EXP = 10;
		maxLvl = -2;

		properties.add(Property.MINIBOSS);

		HUNTING = new Hunting();
		WANDERING = new Wandering();
		state = SLEEPING;
	}

	public int spawnPos;
	private int guardID = -1;

	private int abilityCooldown = Random.NormalIntRange(4, 6);

	private int throwingRockFromPos = -1;
	private int throwingRockToPos = -1;

	public void linkGuard(GnollGuard g){
		guardID = g.id();
		g.linkSapper(this);
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
		if (guardID != -1 && Actor.findById(guardID) instanceof GnollGuard){
			((GnollGuard) Actor.findById(guardID)).loseSapper();
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 3, 6 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 18;
	}

	@Override
	public void damage(int dmg, Object src) {
		super.damage(dmg, src);
		abilityCooldown -= dmg/10f;
	}

	@Override
	public int drRoll() {
		return super.drRoll() + Random.NormalIntRange(0, 6);
	}

	@Override
	protected boolean act() {
		if (throwingRockFromPos != -1){
			Level.set(throwingRockFromPos, Terrain.EMPTY);
			GameScene.updateMap(throwingRockFromPos);
			sprite.attack(throwingRockToPos, new Callback() {
				@Override
				public void call() {
					//do nothing
				}
			});

			Ballistica rockPath = new Ballistica(throwingRockFromPos, throwingRockToPos, Ballistica.MAGIC_BOLT);

			Sample.INSTANCE.play(Assets.Sounds.MISS);
			((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
					reset( throwingRockFromPos, rockPath.collisionPos, new Boulder(), new Callback() {
						@Override
						public void call() {
							//TODO can probably have better particles
							Splash.at(rockPath.collisionPos, ColorMath.random( 0x444444, 0x777766 ), 15);
							Sample.INSTANCE.play(Assets.Sounds.ROCKS);

							Char ch = Actor.findChar(rockPath.collisionPos);
							if (ch == Dungeon.hero){
								PixelScene.shake( 3, 0.7f );
							} else {
								PixelScene.shake(0.5f, 0.5f);
							}

							if (ch != null){
								ch.damage(Random.NormalIntRange(5, 10), this);

								if (ch.isAlive()){
									Buff.prolong( ch, Paralysis.class, ch instanceof GnollGuard ? 10 : 3 );
								} else if (!ch.isAlive() && ch == Dungeon.hero) {
									Badges.validateDeathFromEnemyMagic();
									Dungeon.fail( GnollSapper.this );
									//TODO GLog.n( Messages.get(this, "bolt_kill") );
								}

								if (rockPath.path.size() > rockPath.dist+1) {
									Ballistica trajectory = new Ballistica(ch.pos, rockPath.path.get(rockPath.dist + 1), Ballistica.MAGIC_BOLT);
									WandOfBlastWave.throwChar(ch, trajectory, 1, false, false, GnollSapper.this);
								}
							} else {
								Dungeon.level.pressCell(rockPath.collisionPos);
							}

							next();
						}
					} );

			throwingRockFromPos = -1;
			throwingRockToPos = -1;

			spend(TICK);
			return false;
		} else {
			return super.act();
		}

	}

	public class Hunting extends Mob.Hunting {
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (!enemyInFOV) {
				return super.act(enemyInFOV, justAlerted);
			} else {
				enemySeen = true;

				if (abilityCooldown-- <= 0){
					boolean targetNextToBarricade = false;
					for (int i : PathFinder.NEIGHBOURS8){
						if (Dungeon.level.map[enemy.pos+i] == Terrain.BARRICADE){
							targetNextToBarricade = true;
							break;
						}
					}

					// 50/50 to either throw a rock or do rockfall
					// unless target is next to a barricade, then always try to throw
					// unless nothing to throw, then always rockfall
					if ((targetNextToBarricade || Random.Int(2) == 0) && prepRockAttack(enemy)) {
						Dungeon.hero.interrupt();
						abilityCooldown = Random.NormalIntRange(4, 6);
						spend(GameMath.gate(TICK, (int)Math.ceil(enemy.cooldown()), 3*TICK));
						return true;
					} else if (prepRockFallAttack(enemy)) {
						Dungeon.hero.interrupt();
						spend(GameMath.gate(TICK, (int)Math.ceil(enemy.cooldown()), 3*TICK));
						abilityCooldown = Random.NormalIntRange(4, 6);
						return true;
					}
				}

				//does not approach an enemy it can see, but does melee if in range
				if (canAttack(enemy)){
					return super.act(enemyInFOV, justAlerted);
				} else {
					spend(TICK);
					return true;
				}
			}
		}
	}

	private boolean prepRockAttack( Char target ){
		ArrayList<Integer> candidateRocks = new ArrayList<>();

		for (int i = 0; i < Dungeon.level.length(); i++){
			if (fieldOfView[i] && Dungeon.level.map[i] == Terrain.MINE_BOULDER){
				if (new Ballistica(i, target.pos, Ballistica.PROJECTILE).collisionPos == target.pos){
					candidateRocks.add(i);
				}
			}
		}

		if (candidateRocks.isEmpty()){
			return false;
		} else {

			//throw closest rock to enemy
			throwingRockFromPos = candidateRocks.get(0);
			for (int i : candidateRocks){
				if (Dungeon.level.trueDistance(i, target.pos) < Dungeon.level.trueDistance(throwingRockFromPos, target.pos)){
					throwingRockFromPos = i;
				}
			}
			throwingRockToPos = enemy.pos;

			Ballistica warnPath = new Ballistica(throwingRockFromPos, throwingRockToPos, Ballistica.STOP_SOLID);
			for (int i : warnPath.subPath(0, warnPath.dist)){
				sprite.parent.add(new TargetedCell(i, 0xFF0000));
			}

		}

		return true;
	}

	//similar overall logic as DM-300's rock fall attack, but 5x5 and can't hit barricades
	private boolean prepRockFallAttack( Char target ){

		final int rockCenter = target.pos;

		int safeCell;
		do {
			safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
		} while (safeCell == pos
				|| (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
				|| (Dungeon.level.traps.containsKey(safeCell) && Random.Int(2) == 0));

		ArrayList<Integer> rockCells = new ArrayList<>();

		int start = rockCenter - Dungeon.level.width() * 2 - 2;
		int pos;
		for (int y = 0; y < 5; y++) {
			pos = start + Dungeon.level.width() * y;
			for (int x = 0; x < 5; x++) {
				if (!Dungeon.level.insideMap(pos)) {
					pos++;
					continue;
				}
				if (Dungeon.level instanceof MiningLevel){
					boolean barricade = false;
					for (int j : PathFinder.NEIGHBOURS9){
						if (Dungeon.level.map[pos+j] == Terrain.BARRICADE){
							barricade = true;
						}
					}
					if (barricade){
						pos++;
						continue;
					}
				}
				//add rock cell to pos, if it is not solid, and isn't the safecell
				if (!Dungeon.level.solid[pos] && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
					rockCells.add(pos);
				}
				pos++;
			}
		}
		for (int i : rockCells){
			sprite.parent.add(new TargetedCell(i, 0xFF0000));
		}
		//don't want to overly punish players with slow move or attack speed
		Buff.append(this, SapperRockFall.class, GameMath.gate(TICK, (int)Math.ceil(target.cooldown()), 3*TICK)).setRockPositions(rockCells);

		sprite.attack(target.pos, new Callback() {
			@Override
			public void call() {
				//do nothing
			}
		});

		return true;
	}

	public static class SapperRockFall extends DelayedRockFall {

		@Override
		public void affectChar(Char ch) {
			Buff.prolong(ch, Paralysis.class, ch instanceof GnollGuard ? 10 : 3);
		}

		@Override
		public void affectCell(int cell) {
			if (Dungeon.level.traps.get(cell) != null){
				Dungeon.level.pressCell(cell);
			}
			if (Random.Int(3) == 0) {
				Level.set(cell, Terrain.MINE_BOULDER);
				GameScene.updateMap(cell);
			}
		}

	}

	public class Boulder extends Item {
		{
			image = ItemSpriteSheet.GEO_BOULDER;
		}
	}

	public class Wandering extends Mob.Wandering {
		@Override
		protected int randomDestination() {
			return spawnPos;
		}
	}

	private static final String SPAWN_POS = "spawn_pos";
	private static final String GUARD_ID = "guard_id";

	private static final String ABILITY_COOLDOWN = "ability_cooldown";
	private static final String ROCK_FROM_POS = "rock_from_pos";
	private static final String ROCK_TO_POS = "rock_to_pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(GUARD_ID, guardID);
		bundle.put(SPAWN_POS, spawnPos);
		bundle.put(ABILITY_COOLDOWN, abilityCooldown);
		bundle.put(ROCK_FROM_POS, throwingRockFromPos);
		bundle.put(ROCK_TO_POS, throwingRockToPos);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		guardID = bundle.getInt( GUARD_ID );
		spawnPos = bundle.getInt(SPAWN_POS);
		abilityCooldown = bundle.getInt(ABILITY_COOLDOWN);
		throwingRockFromPos = bundle.getInt(ROCK_FROM_POS);
		throwingRockToPos = bundle.getInt(ROCK_TO_POS);
	}
}
