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

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Trinity extends ArmorAbility {

	{
		baseChargeUse = 25;
	}

	private Class form = null;

	@Override
	public String targetingPrompt() {
		//TODO varies based on form? Or just directly jump into item logic?
		return super.targetingPrompt();
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		//always happens atm
		if (form == null){
			GLog.w("Trinity has no form currently, use one of its spells!");
		}

	}

	private static final String FORM = "form";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (form != null){
			bundle.put(FORM, form);
		}
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		if (bundle.contains(FORM)){
			form = bundle.getClass(FORM);
		}
	}

	@Override
	public int icon() {
		return HeroIcon.TRINITY;
	}

	@Override
	public String desc() {
		//TODO a lot of variance here based on form probably.
		return super.desc();
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.BODY_FORM, Talent.MIND_FORM, Talent.SPIRIT_FORM, Talent.HEROIC_ENERGY};
	}

}
