package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

/**
 * Created by debenhame on 05/02/2015.
 */
public class PotionBandolier extends Bag {

	{
		name = "potion bandolier";
		image = ItemSpriteSheet.BANDOLIER;

		size = 12;
	}

	@Override
	public boolean grab( Item item ) {
		return item instanceof Potion;
	}

	@Override
	public int price() {
		return 50;
	}

	@Override
	public String info() {
		return
			"This thick bandoler fits around your chest like a sash, holding onto your potions for you.\n\n" +
			"It is made of warm animal hide, which should protect your potions from the cold.";
	}
}
