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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.AscendedForm;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Flash extends TargetedClericSpell {

	public static Flash INSTANCE = new Flash();

	@Override
	public int icon() {
		return HeroIcon.FLASH;
	}

	@Override
	public float chargeUse(Hero hero) {
		if (hero.buff(AscendedForm.AscendBuff.class) != null){
			return 1 + hero.buff(AscendedForm.AscendBuff.class).flashCasts;
		} else {
			return 1;
		}
	}

	@Override
	public boolean canCast(Hero hero) {
		return super.canCast(hero)
				&& hero.hasTalent(Talent.FLASH)
				&& hero.buff(AscendedForm.AscendBuff.class) != null;
	}

	@Override
	public int targetingFlags() {
		return -1; //targets an empty cell, not an enemy
	}

	@Override
	protected void onTargetSelected(HolyTome tome, Hero hero, Integer target) {

		if (target == null){
			return;
		}

		if (Dungeon.level.solid[target] || (!Dungeon.level.mapped[target] && !Dungeon.level.visited[target])
				|| Dungeon.level.distance(hero.pos, target) > 2+hero.pointsInTalent(Talent.FLASH)){
			GLog.w(Messages.get(this, "invalid_target"));
			return;
		}

		if (ScrollOfTeleportation.teleportToLocation(hero, target)){
			hero.spendAndNext( 1f );
			onSpellCast(tome, hero);
			hero.buff(AscendedForm.AscendBuff.class).flashCasts++;
		}

	}
}
