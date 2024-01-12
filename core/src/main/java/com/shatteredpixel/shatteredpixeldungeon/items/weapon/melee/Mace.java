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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Daze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Mace extends MeleeWeapon {

	{
		image = ItemSpriteSheet.MACE;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 1f;

		tier = 3;
		ACC = 1.28f; //28% boost to accuracy
	}

	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //16 base, down from 20
				lvl*(tier+1);   //scaling unchanged
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected int baseChargeUse(Hero hero, Char target){
		if (target == null || (target instanceof Mob && ((Mob) target).surprisedBy(hero))) {
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Mace.heavyBlowAbility(hero, target, 1.40f, this);
	}

	public static void heavyBlowAbility(Hero hero, Integer target, float dmgMulti, MeleeWeapon wep){
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
			GLog.w(Messages.get(wep, "ability_bad_position"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		//need to separately check charges here, as non-surprise attacks cost 2
		if (enemy instanceof Mob && !((Mob) enemy).surprisedBy(hero)){
			Charger charger = Buff.affect(hero, Charger.class);
			if (Dungeon.hero.belongings.weapon == wep) {
				if (charger.charges + charger.partialCharge < wep.abilityChargeUse(hero, enemy)){
					GLog.w(Messages.get(wep, "ability_no_charge"));
					return;
				}
			} else {
				if (charger.secondCharges + charger.secondPartialCharge < wep.abilityChargeUse(hero, enemy)){
					GLog.w(Messages.get(wep, "ability_no_charge"));
					return;
				}
			}
		}

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				wep.beforeAbilityUsed(hero, enemy);
				AttackIndicator.target(enemy);
				if (hero.attack(enemy, dmgMulti, 0, Char.INFINITE_ACCURACY)) {
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					if (enemy.isAlive()){
						Buff.affect(enemy, Daze.class, Daze.DURATION);
					} else {
						wep.onAbilityKill(hero, enemy);
					}
				}
				Invisibility.dispel();
				hero.spendAndNext(hero.attackDelay());
				wep.afterAbilityUsed(hero);
			}
		});
	}

}
