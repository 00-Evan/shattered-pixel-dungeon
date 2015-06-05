package com.shatteredpixel.shatteredicepixeldungeon.items;

import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * Created by debenhame on 16/03/2015.
 */
public class MerchantsBeacon extends Item {

	private static final String AC_USE = "USE";


	{
		name = "merchant's beacon";
		image = ItemSpriteSheet.BEACON;

		stackable = true;

		defaultAction = AC_USE;

		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_USE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_USE)) {
			detach( hero.belongings.backpack );
			Shopkeeper.sell();
			Sample.INSTANCE.play( Assets.SND_BEACON );
		} else
			super.execute(hero, action);
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int price() {
		return 5 * quantity;
	}

	@Override
	public String info() {
		return "This odd piece of dwarven technology allows you to communicate from great distances." +
				"\n\nAfter being activated, this beacon will let you sell items to Pixel Mart from anywhere in the dungeon." +
				"\n\nHowever, the magic within the beacon will only last for one session, so use it wisely.";
	}

}
