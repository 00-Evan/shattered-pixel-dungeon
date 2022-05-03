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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_STRENGTH;

		unique = true;
	}

	@Override
	public void apply( Hero hero ) {
		identify();
		if (Dungeon.isChallenged(Challenges.EXSG)) {
			hero.STR--;
			hero.sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "bsg_1") );
			GLog.n( Messages.get(this, "bsg_2") );
			//癔症 力量减一
		} else {
			hero.STR++;
			hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "msg_1") );
			GLog.p( Messages.get(this, "msg_2") );
			Badges.validateStrengthAttained();
		}
	}

	@Override
	public String info() {
		if (Dungeon.isChallenged(Challenges.EXSG)) {
			return isKnown() ? baddesc() : Messages.get(this, "unknown_desc");
		} else {
			return isKnown() ? desc() : Messages.get(this, "unknown_desc");
		}
	}

	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}
}
