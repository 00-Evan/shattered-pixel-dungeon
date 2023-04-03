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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class Flail extends MeleeWeapon {

	{
		image = ItemSpriteSheet.FLAIL;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 0.8f;

		tier = 4;
		ACC = 0.8f; //0.8x accuracy
		//also cannot surprise attack, see Hero.canSurpriseAttack
	}

	@Override
	public int max(int lvl) {
		return  Math.round(7*(tier+1)) +        //35 base, up from 25
				lvl*Math.round(1.6f*(tier+1));  //+8 per level, up from +5
	}

	private static float spinBonus = 1f;

	@Override
	public int damageRoll(Char owner) {
		int dmg = Math.round(super.damageRoll(owner) * spinBonus);
		if (spinBonus == 2f) Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
		spinBonus = 1f;
		return dmg;
	}

	@Override
	public float accuracyFactor(Char owner, Char target) {
		SpinAbilityTracker spin = owner.buff(SpinAbilityTracker.class);
		if (spin != null) {
			//have to handle this in an actor tied to the regular attack =S
			Actor.add(new Actor() {
				{ actPriority = VFX_PRIO; }
				@Override
				protected boolean act() {
					if (owner instanceof Hero && !target.isAlive()){
						onAbilityKill((Hero)owner);
					}
					Actor.remove(this);
					return true;
				}
			});
			//we detach and calculate bonus here in case the attack misses
			spin.detach();
			spinBonus = 1f + (spin.spins/3f);
			if (spinBonus == 2f){
				return Float.POSITIVE_INFINITY;
			} else {
				return super.accuracyFactor(owner, target);
			}
		} else {
			spinBonus = 1f;
			return super.accuracyFactor(owner, target);
		}
	}

	public float abilityChargeUse( Hero hero ) {
		if (Dungeon.hero.buff(SpinAbilityTracker.class) != null){
			return 0;
		} else {
			return 2*super.abilityChargeUse(hero);
		}
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {

		SpinAbilityTracker spin = hero.buff(SpinAbilityTracker.class);
		if (spin != null && spin.spins >= 3){
			GLog.w(Messages.get(this, "spin_warn"));
			return;
		}

		beforeAbilityUsed(hero);
		if (spin == null){
			spin = Buff.affect(hero, SpinAbilityTracker.class, 3f);
		}

		spin.spins++;
		Buff.prolong(hero, SpinAbilityTracker.class, 3f);
		Sample.INSTANCE.play(Assets.Sounds.CHAINS, 1, 1, 0.9f + 0.1f*spin.spins);
		hero.sprite.operate(hero.pos);
		hero.spendAndNext(Actor.TICK);
		BuffIndicator.refreshHero();

		afterAbilityUsed(hero);
	}

	public static class SpinAbilityTracker extends FlavourBuff {

		{
			type = buffType.POSITIVE;
		}

		public int spins = 0;

		@Override
		public int icon() {
			return BuffIndicator.DUEL_SPIN;
		}

		@Override
		public void tintIcon(Image icon) {
			switch (spins){
				case 1: default:
					icon.hardlight(0, 1, 0);
					break;
				case 2:
					icon.hardlight(1, 1, 0);
					break;
				case 3:
					icon.hardlight(1, 0, 0);
					break;
			}
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (3 - visualcooldown()) / 3);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", (int)Math.round((spins/3f)*100f), dispTurns());
		}

		public static String SPINS = "spins";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(SPINS, spins);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			spins = bundle.getInt(SPINS);
		}
	}
}
