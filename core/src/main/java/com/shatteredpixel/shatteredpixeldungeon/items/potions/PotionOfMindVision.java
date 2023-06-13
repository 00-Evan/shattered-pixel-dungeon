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

package com.shatteredpixel.shatteredpixeldungeon.items.potions;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.CONTACTLESS_TREATMENT;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_MINDVIS;
	}

	@Override
	public void apply( Hero hero ) {
		identify();
		Talent.PotionTickCounter counter = Dungeon.hero.buff(Talent.PotionTickCounter.class);
		if (counter != null) {
			Buff.affect( hero, MindVision.class, (int)counter.count());
			counter.detach();
		}
		Buff.affect( hero, MindVision.class, MindVision.DURATION );
		SpellSprite.show(hero, SpellSprite.VISION, 1, 0.77f, 0.9f);
		Dungeon.observe();
		
		if (Dungeon.level.mobs.size() > 0) {
			GLog.i( Messages.get(this, "see_mobs") );
		} else {
			GLog.i( Messages.get(this, "see_none") );
		}
	}

	@Override
	public void applyChar( Char ch ) {
		//whitephial
		identify();
		int treat = 1 + Dungeon.hero.pointsInTalent(CONTACTLESS_TREATMENT);
		Talent.PotionTickCounter counter = Dungeon.hero.buff(Talent.PotionTickCounter.class);
		if (counter != null) {
			Buff.affect( ch, MindVision.class, (int)counter.count());
			counter.detach();
		}
		Buff.affect( ch, MindVision.class, MindVision.DURATION * treat/4f);
		SpellSprite.show(ch, SpellSprite.VISION, 1, 0.77f, 0.9f);
		Dungeon.observe();

		if (Dungeon.level.mobs.size() > 0) {
			GLog.i( Messages.get(this, "see_mobs_ally"),ch.name() );
		}
	}


	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}
}
