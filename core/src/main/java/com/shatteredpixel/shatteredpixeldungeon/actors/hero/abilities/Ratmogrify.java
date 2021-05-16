package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;

public class Ratmogrify extends ArmorAbility {

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		//TODO
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{ Talent.RK_1, Talent.RK_2, Talent.RK_3, Talent.HEROIC_ENERGY};
	}
}
