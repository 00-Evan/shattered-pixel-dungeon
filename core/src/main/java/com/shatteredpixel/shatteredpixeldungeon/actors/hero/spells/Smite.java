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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Smite extends TargetedClericSpell {

	public static Smite INSTANCE = new Smite();

	@Override
	public int icon() {
		return HeroIcon.SMITE;
	}

	@Override
	public int targetingFlags() {
		return Ballistica.STOP_TARGET; //no auto-aim
	}

	@Override
	public String desc() {
		int min = 5 + Dungeon.hero.lvl/2;
		int max = 10 + Dungeon.hero.lvl;
		return Messages.get(this, "desc", min, max) + "\n\n" + Messages.get(this, "charge_cost", (int)chargeUse(Dungeon.hero));
	}

	@Override
	public float chargeUse(Hero hero) {
		return 2f;
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero) && hero.subClass == HeroSubClass.PALADIN;
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {
		if (target == null) {
			return;
		}

		Char enemy = Actor.findChar(target);
		if (enemy == null || enemy == hero){
			GLog.w(Messages.get(this, "no_target"));
			return;
		}

		//we apply here because of projecting
		SmiteTracker tracker = Buff.affect(hero, SmiteTracker.class);
		if (hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target] || !hero.canAttack(enemy)) {
			GLog.w(Messages.get(this, "invalid_enemy"));
			tracker.detach();
			return;
		}

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				AttackIndicator.target(enemy);

				float accMult = 1;
				if (!(hero.belongings.attackingWeapon() instanceof Weapon)
						|| ((Weapon) hero.belongings.attackingWeapon()).STRReq() <= hero.STR()){
					accMult = Char.INFINITE_ACCURACY;
				}
				if (hero.attack(enemy, 1, 0, accMult)){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
					enemy.sprite.burst(0xFFFFFFFF, 10);
				}
				tracker.detach();

				Invisibility.dispel();

				hero.spendAndNext(hero.attackDelay());
				onSpellCast(tome, hero);
			}
		});

	}

	public static int bonusDmg( Hero attacker, Char defender){
		int min = 5 + attacker.lvl/2;
		int max = 10 + attacker.lvl;
		if (Char.hasProp(defender, Char.Property.UNDEAD) || Char.hasProp(defender, Char.Property.DEMONIC)){
			return max;
		} else {
			return Random.NormalIntRange(min, max);
		}
	}

	public static class SmiteTracker extends FlavourBuff {};

}
