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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSapperSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

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
		return Random.NormalIntRange( 1, 6 );
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
			GnollGeomancer.doRockThrowAttack(this, throwingRockFromPos, throwingRockToPos);

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
				if (Dungeon.level.distance(spawnPos, target) > 3){
					//don't chase something more than a few tiles out of spawning position
					target = pos;
				}
				return super.act(enemyInFOV, justAlerted);
			} else {
				enemySeen = true;

				if (Actor.findById(guardID) instanceof GnollGuard
						&& Dungeon.level.distance(pos, enemy.pos) <= 3){
					((GnollGuard) Actor.findById(guardID)).target = enemy.pos;
					((GnollGuard) Actor.findById(guardID)).aggro(enemy);
				}

				if (abilityCooldown-- <= 0){
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
					Ballistica aim = GnollGeomancer.prepRockThrowAttack(enemy, GnollSapper.this);
					if (aim != null && (targetNextToBarricade || Random.Int(2) == 0)) {

						throwingRockFromPos = aim.sourcePos;
						throwingRockToPos = aim.collisionPos;

						Ballistica warnPath = new Ballistica(aim.sourcePos, aim.collisionPos, Ballistica.STOP_SOLID);
						for (int i : warnPath.subPath(0, warnPath.dist)){
							sprite.parent.add(new TargetedCell(i, 0xFF0000));
						}

						Dungeon.hero.interrupt();
						abilityCooldown = Random.NormalIntRange(4, 6);
						spend(GameMath.gate(TICK, (int)Math.ceil(enemy.cooldown()), 3*TICK));
						return true;
					} else if (GnollGeomancer.prepRockFallAttack(enemy, GnollSapper.this, 2, true)) {
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
