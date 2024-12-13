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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class AscendedForm extends ArmorAbility {

	{
		baseChargeUse = 50;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		Buff.prolong(hero, AscendBuff.class, AscendBuff.DURATION);
		hero.sprite.operate(hero.pos);
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		new Flare(6, 48).color(0xFFFF00, true).show(hero.sprite, 2f);

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();
		Invisibility.dispel();
		hero.spendAndNext(Actor.TICK);

	}

	@Override
	public int icon() {
		return HeroIcon.ASCENDED_FORM;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.DIVINE_INTERVENTION, Talent.JUDGEMENT, Talent.FLASH, Talent.HEROIC_ENERGY};
	}

	//TODO become shielding buff
	public static class AscendBuff extends FlavourBuff {

		public static float DURATION = 10f;

		public int flashUses = 0;

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add(CharSprite.State.GLOWING);
			else    target.sprite.remove(CharSprite.State.GLOWING);
		}

	}

}
