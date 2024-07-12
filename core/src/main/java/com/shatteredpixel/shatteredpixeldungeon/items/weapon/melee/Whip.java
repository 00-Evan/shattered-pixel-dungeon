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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class Whip extends MeleeWeapon {

	{
		image = ItemSpriteSheet.WHIP;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;

		tier = 3;
		RCH = 3;    //lots of extra reach
	}

	@Override
	public int max(int lvl) {
		return  5*(tier) +      //15 base, down from 20
				lvl*(tier);     //+3 per level, down from +4
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {

		ArrayList<Char> targets = new ArrayList<>();
		Char closest = null;

		hero.belongings.abilityWeapon = this;
		for (Char ch : Actor.chars()){
			if (ch.alignment == Char.Alignment.ENEMY
					&& !hero.isCharmedBy(ch)
					&& Dungeon.level.heroFOV[ch.pos]
					&& hero.canAttack(ch)){
				targets.add(ch);
				if (closest == null || Dungeon.level.trueDistance(hero.pos, closest.pos) > Dungeon.level.trueDistance(hero.pos, ch.pos)){
					closest = ch;
				}
			}
		}
		hero.belongings.abilityWeapon = null;

		if (targets.isEmpty()) {
			GLog.w(Messages.get(this, "ability_no_target"));
			return;
		}

		throwSound();
		Char finalClosest = closest;
		hero.sprite.attack(hero.pos, new Callback() {
			@Override
			public void call() {
				beforeAbilityUsed(hero, finalClosest);
				for (Char ch : targets) {
					//ability does no extra damage
					hero.attack(ch, 1, 0, Char.INFINITE_ACCURACY);
					if (!ch.isAlive()){
						onAbilityKill(hero, ch);
					}
				}
				Invisibility.dispel();
				hero.spendAndNext(hero.attackDelay());
				afterAbilityUsed(hero);
			}
		});
	}

	@Override
	public String abilityInfo() {
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(min()), augment.damageFactor(max()));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0), max(0));
		}
	}

	public String upgradeAbilityStat(int level){
		return augment.damageFactor(min(level)) + "-" + augment.damageFactor(max(level));
	}
}
