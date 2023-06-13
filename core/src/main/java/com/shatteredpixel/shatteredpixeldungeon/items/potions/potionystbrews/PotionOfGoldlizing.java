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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Goldlize;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class PotionOfGoldlizing extends PotionystBrews {

	{
		icon = ItemSpriteSheet.Icons.POTION_GOLDLIZE;
		image = ItemSpriteSheet.LIQUID_GOLILIZE;
	}


	@Override
	public void apply( Hero hero ) {
		Sample.INSTANCE.play( Assets.Sounds.DRINK );

		new Flare( 6, 32 ).color(0xFFFF00, true).show( curUser.sprite, 2f );

		Goldlize Armor = curUser.buff(Goldlize.class);
		if (Armor == null){

			Buff.affect(curUser,Goldlize.class).addArmor(Dungeon.gold/10);

		} else {

			Armor.addArmor( Dungeon.gold/10);

		}
		Dungeon.gold -= Dungeon.gold/10;


	}


	@Override
	public int value() {
		return isKnown() ? (30 + 20) * quantity : super.value();
	}

	@Override
	public int energyVal() {
		return (Reflection.newInstance(PotionOfGoldenLiquid.class).energyVal() + 4) * quantity;
	}


	public static class potionToLiquid extends Recipe {


		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean goldenliquid = false;

			if (Dungeon.hero.pointsInTalent(Talent.POTION_RECIPE) >= 2) {
				for (Item ingredient : ingredients) {
					if (ingredient.quantity() > 0) {
						if (ingredient instanceof PotionOfGoldenLiquid) {
							goldenliquid = true;
						}
					}
				}
			}

			return goldenliquid  ;
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
			return new PotionOfGoldlizing();
		}
	}
}
