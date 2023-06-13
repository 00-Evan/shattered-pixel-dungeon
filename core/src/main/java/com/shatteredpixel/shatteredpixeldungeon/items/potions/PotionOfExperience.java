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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfExperience extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_EXP;

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		identify();
		hero.earnExp( hero.maxExp(), getClass() );
		new Flare( 6, 32 ).color(0xFFFF00, true).show( curUser.sprite, 2f );
	}

	@Override
	public void applyChar( Char ch ) {
		//whitephial
		identify();
		int treat = 1 + Dungeon.hero.pointsInTalent(CONTACTLESS_TREATMENT);
		if (ch.allyLVL < 5) {
			GLog.p( Messages.get(this, "new_level_ally"), ch.name() );
			ch.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );
			Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
			ch.allyLVL++;
		} else {
			new Flare(6, 32).color(0xFFFF00, true).show(ch.sprite, 2f);
			Buff.prolong(ch, Bless.class, Bless.DURATION * treat/4f);
			Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
		}
	}
	
	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}

	@Override
	public int energyVal() {
		boolean potiontest = Dungeon.hero.hasTalent(Talent.POTION_ENERGY);
		if (potiontest && Dungeon.hero.buff(Talent.PotionEnergyCounter.class) != null){
			return (int) (( 8 + Dungeon.hero.buff(Talent.PotionEnergyCounter.class).count() )*quantity);
		} else {
			return 8 * quantity;
		}
	}
}
