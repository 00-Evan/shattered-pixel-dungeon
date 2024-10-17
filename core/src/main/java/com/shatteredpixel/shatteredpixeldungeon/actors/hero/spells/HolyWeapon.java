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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

public class HolyWeapon extends ClericSpell {

	public static HolyWeapon INSTANCE = new HolyWeapon();

	@Override
	public int icon() {
		return HeroIcon.ELEMENTAL_STRIKE; //TODO unique icon
	}

	@Override
	public float chargeUse(Hero hero) {
		return 2;
	}

	@Override
	protected void activate(HolyTome tome, Hero hero, Integer target) {

		Buff.affect(hero, HolyWepBuff.class, 50f);
		Item.updateQuickslot();

		Sample.INSTANCE.play(Assets.Sounds.READ);
		tome.spendCharge( 2f );
		hero.sprite.operate(hero.pos);
		hero.spend( 1f );
		hero.next();
	}

	public static class HolyWepBuff extends FlavourBuff {

		@Override
		public int icon() {
			return BuffIndicator.WEAPON;
		}

		@Override
		public void detach() {
			super.detach();
			Item.updateQuickslot();
		}
	}

}
