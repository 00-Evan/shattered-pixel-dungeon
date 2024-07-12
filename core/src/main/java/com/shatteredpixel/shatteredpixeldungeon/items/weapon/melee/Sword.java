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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Sword extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.SWORD;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;

		tier = 3;
	}

	@Override
	protected int baseChargeUse(Hero hero, Char target){
		if (hero.buff(Sword.CleaveTracker.class) != null){
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//+(5+lvl) damage, roughly +45% base dmg, +40% scaling
		int dmgBoost = augment.damageFactor(5 + buffedLvl());
		Sword.cleaveAbility(hero, target, 1, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		int dmgBoost = levelKnown ? 5 + buffedLvl() : 5;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
		}
	}

	public String upgradeAbilityStat(int level){
		int dmgBoost = 5 + level;
		return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
	}

	public static void cleaveAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep){
		if (target == null) {
			return;
		}

		Char enemy = Actor.findChar(target);
		if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
			GLog.w(Messages.get(wep, "ability_no_target"));
			return;
		}

		hero.belongings.abilityWeapon = wep;
		if (!hero.canAttack(enemy)){
			GLog.w(Messages.get(wep, "ability_target_range"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				wep.beforeAbilityUsed(hero, enemy);
				AttackIndicator.target(enemy);
				if (hero.attack(enemy, dmgMulti, dmgBoost, Char.INFINITE_ACCURACY)){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				}

				Invisibility.dispel();

				if (!enemy.isAlive()){
					hero.next();
					wep.onAbilityKill(hero, enemy);
					if (hero.buff(CleaveTracker.class) != null) {
						hero.buff(CleaveTracker.class).detach();
					} else {
						Buff.prolong(hero, CleaveTracker.class, 4f); //1 less as attack was instant
					}
				} else {
					hero.spendAndNext(hero.attackDelay());
					if (hero.buff(CleaveTracker.class) != null) {
						hero.buff(CleaveTracker.class).detach();
					}
				}
				wep.afterAbilityUsed(hero);
			}
		});
	}

	public static class CleaveTracker extends FlavourBuff {

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.DUEL_CLEAVE;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (5 - visualcooldown()) / 5);
		}
	}

}
