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
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.GoldenLiquid;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfGoldenLiquid extends PotionystBrews {

	{
		icon = ItemSpriteSheet.Icons.POTION_GOLDEN;
		image = ItemSpriteSheet.LIQUID_GOLDEN;
	}
	public String defaultAction() {
		return AC_THROW;
	}

	@Override
	public void shatter( int cell ) {

		if (Dungeon.level.heroFOV[cell]) {
			identify();

			splash( cell );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
		}

		for (int offset : PathFinder.NEIGHBOURS9){
			if (!Dungeon.level.solid[cell+offset]) {
				Splash.at( cell + offset, 0xFFFF00, 3);
				int turnsToAdd = Dungeon.level.water[cell+ offset] ? 1 : 0;
				GameScene.add(Blob.seed(cell + offset, 2 + turnsToAdd, GoldenLiquid.class));

			}
		}
	}


	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}


	public static class SeedToLiquid extends Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean liquidmetal = false;
			boolean seeds = false;

			if (Dungeon.hero.hasTalent(Talent.POTION_RECIPE)) {
				for (Item ingredient : ingredients) {
					if (ingredient.quantity() > 0) {
						if (ingredient instanceof LiquidMetal) {
							liquidmetal = true;
						} else if (ingredient instanceof Plant.Seed) {
							seeds = true;
						}
					}
				}
			}

			return liquidmetal && seeds ;
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
			return new PotionOfGoldenLiquid();
		}
	}
}
