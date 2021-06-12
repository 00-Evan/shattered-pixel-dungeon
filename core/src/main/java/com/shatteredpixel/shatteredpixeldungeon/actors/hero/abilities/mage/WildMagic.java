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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WildMagic extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target == null){
			return;
		}

		if (target == hero.pos){
			GLog.w(Messages.get(this, "self_target"));
			return;
		}

		ArrayList<Wand> wands = hero.belongings.getAllItems(Wand.class);
		Random.shuffle(wands);

		float chargeUsePerShot = (float)Math.pow(0.563f, hero.pointsInTalent(Talent.CONSERVED_MAGIC));

		for (Wand w : wands.toArray(new Wand[0])){
			if (w.curCharges < 1 && w.partialCharge < chargeUsePerShot){
				wands.remove(w);
			}
		}

		int maxWands = 4 + Dungeon.hero.pointsInTalent(Talent.FIRE_EVERYTHING);

		if (wands.size() < maxWands){
			ArrayList<Wand> dupes = new ArrayList<>(wands);

			for (Wand w : dupes.toArray(new Wand[0])){
				float totalCharge = w.curCharges + w.partialCharge;
				if (totalCharge < 2*chargeUsePerShot){
					dupes.remove(w);
				}
			}

			Random.shuffle(dupes);
			while (!dupes.isEmpty() && wands.size() < maxWands){
				wands.add(dupes.remove(0));
			}
		}

		if (wands.size() == 0){
			GLog.w(Messages.get(this, "no_wands"));
			return;
		}

		hero.busy();

		Random.shuffle(wands);

		Buff.affect(hero, WildMagicTracker.class, 0f);

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		zapWand(wands, hero, target);

	}

	public static class WildMagicTracker extends FlavourBuff{};

	private void zapWand( ArrayList<Wand> wands, Hero hero, int target){
		Wand cur = wands.remove(0);

		Ballistica aim = new Ballistica(hero.pos, target, cur.collisionProperties(target));

		hero.sprite.zap(target);
		cur.fx(aim, new Callback() {
			@Override
			public void call() {
				cur.onZap(aim);
				cur.partialCharge -= (float)Math.pow(0.563f, hero.pointsInTalent(Talent.CONSERVED_MAGIC));
				if (cur.partialCharge < 0){
					cur.partialCharge++;
					cur.curCharges--;
				}
				if (!wands.isEmpty()){
					zapWand(wands, hero, target);
				} else {
					if (hero.buff(WildMagicTracker.class) != null){
						hero.buff(WildMagicTracker.class).detach();
					}
					Item.updateQuickslot();
					Invisibility.dispel();
					hero.spendAndNext(Actor.TICK);
				}
			}
		});
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.WILD_POWER, Talent.FIRE_EVERYTHING, Talent.CONSERVED_MAGIC, Talent.HEROIC_ENERGY};
	}
}
