/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Endure extends ArmorAbility {

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		Buff.prolong(hero, EndureTracker.class, 13f).setup(hero);
		//TODO extend combo maybe?
		hero.sprite.operate(hero.pos);
		//TODO vfx/sfx?

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		hero.spendAndNext(3f);
	}

	//TODO visual buff stuff
	public static class EndureTracker extends FlavourBuff {

		public boolean enduring;

		public int damageBonus;
		public int maxDmgTaken;
		public int hitsLeft;

		@Override
		public int icon() {
			return enduring ? BuffIndicator.NONE : BuffIndicator.AMOK;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (10f - visualcooldown()) / 10f);
		}

		public void setup(Hero hero){
			enduring = true;
			maxDmgTaken = (int) (hero.HT * Math.pow(0.76f, hero.pointsInTalent(Talent.SHRUG_IT_OFF)));
			damageBonus = 0;
			hitsLeft = 0;
		}

		public int adjustDamageTaken(int damage){
			if (enduring) {
				damageBonus += damage/4;
				return damage/2;
			}
			return damage;
		}

		public int enforceDamagetakenLimit(int damage){
			if (damage >= maxDmgTaken) {
				damage = maxDmgTaken;
				maxDmgTaken = 0;
			} else {
				maxDmgTaken -= damage;
			}
			return damage;
		}

		public void endEnduring(){
			if (!enduring){
				return;
			}

			enduring = false;
			damageBonus *= 1f + 0.1f*Dungeon.hero.pointsInTalent(Talent.SUSTAINED_RETRIBUTION);

			int nearby = 0;
			for (Char ch : Actor.chars()){
				if (ch.alignment == Char.Alignment.ENEMY && Dungeon.level.distance(target.pos, ch.pos) <= 2){
					nearby ++;
				}
			}
			damageBonus *= 1f + (nearby*0.05f*Dungeon.hero.pointsInTalent(Talent.EVEN_THE_ODDS));

			hitsLeft = 1+Dungeon.hero.pointsInTalent(Talent.SUSTAINED_RETRIBUTION);
			damageBonus /= hitsLeft;
		}

		public int damageFactor(int damage){
			if (enduring){
				return damage;
			} else {
				int bonusDamage = damageBonus;
				hitsLeft--;

				if (hitsLeft <= 0){
					detach();
				}
				return damage + bonusDamage;
			}
		}

		public static String ENDURING       = "enduring";
		public static String DAMAGE_BONUS   = "damage_bonus";
		public static String MAX_DMG_TAKEN  = "max_dmg_taken";
		public static String HITS_LEFT      = "hits_left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(ENDURING, enduring);
			bundle.put(DAMAGE_BONUS, damageBonus);
			bundle.put(MAX_DMG_TAKEN, maxDmgTaken);
			bundle.put(HITS_LEFT, hitsLeft);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			enduring = bundle.getBoolean(ENDURING);
			damageBonus = bundle.getInt(DAMAGE_BONUS);
			maxDmgTaken = bundle.getInt(ENDURING);
			hitsLeft = bundle.getInt(HITS_LEFT);
		}
	};

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.SUSTAINED_RETRIBUTION, Talent.SHRUG_IT_OFF, Talent.EVEN_THE_ODDS, Talent.HEROIC_ENERGY};
	}
}
