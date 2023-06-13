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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class Rebellion extends MeleeWeapon {

	{
		image = ItemSpriteSheet.REBELLION;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.1f;
		DST = 2;
		//방어구 관통 2

		tier = 4;
	}

	@Override
	public int DstFactor(Char owner) {
		return 2;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +
				lvl*(tier+1);
	} //전투도끼랑 같음

	public void doAttack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
		AttackIndicator.target(enemy);
		if (Dungeon.hero.attack(enemy, dmgMulti, dmgBonus, accMulti)) {
			Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
			if (!enemy.isAlive()) {
				onAbilityKill(Dungeon.hero);
				Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
			}
		}
		Invisibility.dispel();
	}

	public void threeAttack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
		doAttack(enemy,dmgMulti,dmgBonus,accMulti);
		if(enemy.isAlive() && Dungeon.hero.canAttack(enemy)) {
			Dungeon.hero.sprite.attack(enemy.pos, new Callback() {
				@Override
				public void call() {
					doAttack(enemy, dmgMulti, dmgBonus, accMulti);
					if(enemy.isAlive() && Dungeon.hero.canAttack(enemy)) {
						Dungeon.hero.sprite.attack(enemy.pos, new Callback() {
							@Override
							public void call() {
								doAttack(enemy, dmgMulti, dmgBonus, accMulti);
								if(enemy.isAlive()) Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
							}
						});
					}else {
						Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
					}
				}
			});
		}else {
			Dungeon.hero.spendAndNext(Dungeon.hero.attackDelay());
		}
	}



	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		if (target == null){
			return;
		}

		Char enemy = Actor.findChar(target);
		//duelist can lunge out of her FOV, but this wastes the ability instead of cancelling if there is no target
		if (Dungeon.level.heroFOV[target]) {
			if (enemy == null || enemy == hero || hero.isCharmedBy(enemy)) {
				GLog.w(Messages.get(this, "ability_no_target"));
				return;
			}
		}

		if (hero.rooted || Dungeon.level.distance(hero.pos, target) < 1
				|| Dungeon.level.distance(hero.pos, target)-1 > reachFactor(hero)){
			GLog.w(Messages.get(this, "ability_bad_position"));
			return;
		}


		int lungeCell = -1;
		for (int i : PathFinder.NEIGHBOURS8){
			if (Dungeon.level.distance(hero.pos+i, target) <= reachFactor(hero)
					&& Actor.findChar(hero.pos+i) == null
					&& (Dungeon.level.passable[hero.pos+i] || (Dungeon.level.avoid[hero.pos+i] && hero.flying))){
				if (lungeCell == -1 || Dungeon.level.trueDistance(hero.pos + i, target) < Dungeon.level.trueDistance(lungeCell, target)){
					if (!(Dungeon.level.trueDistance(hero.pos , target) == Dungeon.level.trueDistance(lungeCell, target))) {
						lungeCell = hero.pos + i;
					}
				}
			}if (Dungeon.level.distance(hero.pos, target) <= reachFactor(hero)
					&& Actor.findChar(hero.pos+i) != null){
					lungeCell = hero.pos;
			}
		}

		if (lungeCell == -1){
			GLog.w(Messages.get(this, "ability_bad_position"));
			return;
		}

		final int dest = lungeCell;
		hero.busy();
		Sample.INSTANCE.play(Assets.Sounds.MISS);
		hero.sprite.jump(hero.pos, dest, 0, 0.1f, new Callback() {
			@Override
			public void call() {
				if (Dungeon.level.map[hero.pos] == Terrain.OPEN_DOOR) {
					Door.leave( hero.pos );
				}

				hero.pos = dest;
				Dungeon.level.occupyCell(hero);

				if (enemy != null) {
					hero.sprite.attack(enemy.pos, new Callback() {
						@Override
						public void call() {
							if (enemy.isAlive()) {
								beforeAbilityUsed(hero);
								threeAttack(enemy, 0.45f, augment.damageFactor(0), Char.INFINITE_ACCURACY);
								if (!enemy.isAlive()) {
									onAbilityKill(hero);
								}
								afterAbilityUsed(hero);
							} else {
								hero.spendAndNext(hero.attackDelay());

							}
						}
					});
				} else {
					beforeAbilityUsed(hero);
					GLog.w(Messages.get(Rapier.class, "ability_no_target")); //레이피어와 같음
					hero.spendAndNext(hero.speed()); //안보이는데 써도 소모
					afterAbilityUsed(hero);
				}
			}
		});
	}
}
