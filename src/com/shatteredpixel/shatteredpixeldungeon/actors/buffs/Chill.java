package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.text.DecimalFormat;

/**
 * Created by debenhame on 23/04/2015.
 */
public class Chill extends FlavourBuff {

	private static final String TXT_FREEZES = "%s freezes!";

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public boolean attachTo(Char target) {
		//can't chill what's frozen!
		if (target.buff(Frost.class) != null) return false;

		if (super.attachTo(target)){
			Burning.detach( target, Burning.class );

			//chance of potion breaking is the same as speed factor.
			if (Random.Float(1f) > speedFactor() && target instanceof Hero) {

				Hero hero = (Hero)target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Potion) {

					item = item.detach( hero.belongings.backpack );
					GLog.w(TXT_FREEZES, item.toString());
					((Potion) item).shatter(hero.pos);

				} else if (item instanceof MysteryMeat) {

					item = item.detach( hero.belongings.backpack );
					FrozenCarpaccio carpaccio = new FrozenCarpaccio();
					if (!carpaccio.collect( hero.belongings.backpack )) {
						Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
					}
					GLog.w(TXT_FREEZES, item.toString());

				}
			} else if (target instanceof Thief && ((Thief)target).item instanceof Potion) {

				((Potion) ((Thief)target).item).shatter( target.pos );
				((Thief) target).item = null;

			}
			return true;
		} else {
			return false;
		}
	}

	//reduces speed by 10% for every turn remaining, capping at 50%
	public float speedFactor(){
		return Math.max(0.5f, 1 - cooldown()*0.1f);
	}

	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.CHILLED);
		else target.sprite.remove(CharSprite.State.CHILLED);
	}

	@Override
	public String toString() {
		return "Chilled";
	}

	@Override
	public String desc() {
		return "Not quite frozen, but still much too cold.\n" +
				"\n" +
				"Chilled targets perform all actions more slowly, depending on how many turns are left in the effect. " +
				"At it's worst, this is equivalent to being slowed.\n" +
				"\n" +
				"This chilled will last for " + dispTurns() + ", " +
				"and is currently reducing speed by " + new DecimalFormat("#.##").format((1f-speedFactor())*100f) + "%";
	}
}
