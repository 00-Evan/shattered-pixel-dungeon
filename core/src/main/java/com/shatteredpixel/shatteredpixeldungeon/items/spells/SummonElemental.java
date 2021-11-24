package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SummonElemental extends Spell {

	{
		image = ItemSpriteSheet.SUMMON_ELE;
	}

	@Override
	protected void onCast(Hero hero) {

	}

	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{Embers.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};

			cost = 8;

			output = SummonElemental.class;
			outQuantity = 3;
		}

	}
}
