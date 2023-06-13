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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.potionystbrews;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent.CONTACTLESS_TREATMENT;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Disguise;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class PotionOfDisguise extends PotionystBrews {

	{
		icon = ItemSpriteSheet.Icons.POTION_DISGUISE;
		image = ItemSpriteSheet.LIQUID_DISGUISE;
	}



	@Override
	public void apply( Hero hero ) {
		Sample.INSTANCE.play( Assets.Sounds.DRINK );

		Talent.PotionTickCounter counter = Dungeon.hero.buff(Talent.PotionTickCounter.class);
		if (counter != null) {
			Buff.affect(hero, Disguise.class, (int) counter.count());
			counter.detach();
		}
		GLog.i( Messages.get(PotionOfDisguise.class, "disguise") );
		CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
		Sample.INSTANCE.play( Assets.Sounds.PUFF );

		Buff.affect(hero, Disguise.class, Disguise.DURATION );
		Buff.affect(hero, Talent.FindSeedCounter.class).countUp(1);

		((HeroSprite)hero.sprite).updateArmor();
	}

	@Override
	public void applyChar( Char ch ) {
		//whitephial
		identify();
		int treat = 1 + Dungeon.hero.pointsInTalent(CONTACTLESS_TREATMENT);
		Talent.PotionTickCounter counter = Dungeon.hero.buff(Talent.PotionTickCounter.class);
		if (counter != null) {
			Buff.affect(ch, Disguise.class, (int) counter.count());
			counter.detach();
		}
		GLog.i( Messages.get(PotionOfDisguise.class, "disguise_ally"), ch.name() );
		CellEmitter.get( ch.pos ).burst( Speck.factory( Speck.WOOL ), 10 );
		Sample.INSTANCE.play( Assets.Sounds.PUFF );

		Buff.affect(ch, Disguise.class, Disguise.DURATION * treat/4f );

	}

		@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}


	public static class SeedToLiquid extends Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean catalyst = false;
			boolean seeds = false;

			if (Dungeon.hero.hasTalent(Talent.POTION_RECIPE)) {
				for (Item ingredient : ingredients) {
					if (ingredient.quantity() > 0) {
						if (ingredient instanceof ArcaneCatalyst) {
							catalyst = true;
						} else if (ingredient instanceof Plant.Seed) {
							seeds = true;
						}
					}
				}
			}

			return catalyst && seeds;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 0;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item ingredient : ingredients){
				ingredient.quantity(ingredient.quantity() - 1);
			}

			return sampleOutput(null);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new PotionOfDisguise();
		}
	}
}
