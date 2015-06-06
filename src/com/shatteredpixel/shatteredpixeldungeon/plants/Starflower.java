package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Starflower extends Plant {

	private static final String TXT_DESC =
			"An extremely rare plant, " +
			"Starflower is said to grant holy power to whomever touches it.";

	{
		image = 11;
		plantName = "Starflower";
	}

	@Override
	public void activate() {
		Char ch = Actor.findChar(pos);

		if (ch != null) Buff.prolong(ch, Bless.class, 30f);

		if (Random.Int(5) == 0){
			Dungeon.level.drop(new Seed(), pos).sprite.drop();
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed{

		{
			plantName = "Starflower";

			name = "Seed of " + plantName;
			image = ItemSpriteSheet.SEED_STARFLOWER;

			plantClass = Starflower.class;
			alchemyClass = PotionOfExperience.class;
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
