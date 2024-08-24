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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class Rapier extends MeleeWeapon {

	{
		image = ItemSpriteSheet.RAPIER;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.3f;

		tier = 1;

		bones = false;
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //8 base, down from 10
				lvl*(tier+1);   //scaling unchanged
	}

	@Override
	public int defenseFactor( Char owner ) {
		return 1;	//1 extra defence
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//+(5+1.5*lvl) damage, roughly +111% base damage, +100% scaling
		int dmgBoost =  augment.damageFactor(5 + Math.round(1.5f*buffedLvl()));
		lungeAbility(hero, target, 1, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		int dmgBoost = levelKnown ? 5 + Math.round(1.5f*buffedLvl()) : 5;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
		}
	}

	public String upgradeAbilityStat(int level){
		int dmgBoost = 5 + Math.round(1.5f*level);
		return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
	}

	public static void lungeAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep){
		if (target == null){
			return;
		}

		Char enemy = Actor.findChar(target);
		//duelist can lunge out of her FOV, but this wastes the ability instead of cancelling if there is no target
		if (Dungeon.level.heroFOV[target]) {
			if (enemy == null || enemy == hero || hero.isCharmedBy(enemy)) {
				GLog.w(Messages.get(wep, "ability_no_target"));
				return;
			}
		}

		if (hero.rooted || Dungeon.level.distance(hero.pos, target) < 2
				|| Dungeon.level.distance(hero.pos, target)-1 > wep.reachFactor(hero)){
			GLog.w(Messages.get(wep, "ability_target_range"));
			if (hero.rooted) PixelScene.shake( 1, 1f );
			return;
		}

		int lungeCell = -1;
		for (int i : PathFinder.NEIGHBOURS8){
			if (Dungeon.level.distance(hero.pos+i, target) <= wep.reachFactor(hero)
					&& Actor.findChar(hero.pos+i) == null
					&& (Dungeon.level.passable[hero.pos+i] || (Dungeon.level.avoid[hero.pos+i] && hero.flying))){
				if (lungeCell == -1 || Dungeon.level.trueDistance(hero.pos + i, target) < Dungeon.level.trueDistance(lungeCell, target)){
					lungeCell = hero.pos + i;
				}
			}
		}

		if (lungeCell == -1){
			GLog.w(Messages.get(wep, "ability_target_range"));
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
				Dungeon.observe();

				hero.belongings.abilityWeapon = wep; //set this early to we can check canAttack
				if (enemy != null && hero.canAttack(enemy)) {
					hero.sprite.attack(enemy.pos, new Callback() {
						@Override
						public void call() {

							wep.beforeAbilityUsed(hero, enemy);
							AttackIndicator.target(enemy);
							if (hero.attack(enemy, dmgMulti, dmgBoost, Char.INFINITE_ACCURACY)) {
								Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
								if (!enemy.isAlive()) {
									wep.onAbilityKill(hero, enemy);
								}
							}
							Invisibility.dispel();
							hero.spendAndNext(hero.attackDelay());
							wep.afterAbilityUsed(hero);
						}
					});
				} else {
					//spends charge but otherwise does not count as an ability use
					Charger charger = Buff.affect(hero, Charger.class);
					charger.partialCharge -= 1;
					while (charger.partialCharge < 0 && charger.charges > 0) {
						charger.charges--;
						charger.partialCharge++;
					}
					updateQuickslot();
					GLog.w(Messages.get(Rapier.class, "ability_no_target"));
					hero.spendAndNext(1/hero.speed());
				}
			}
		});
	}
}
