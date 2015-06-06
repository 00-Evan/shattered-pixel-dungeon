package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

/**
 * Created by Evan on 14/05/2015.
 */
public class Corruption extends Buff {

	{
		type = buffType.NEGATIVE;
	}

	private float buildToDamage = 0f;

	@Override
	public boolean act() {
		buildToDamage += target.HT/100f;

		int damage = (int)buildToDamage;
		buildToDamage -= damage;

		if (damage > 0)
			target.damage(damage, this);

		spend(TICK);

		return true;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add( CharSprite.State.DARKENED );
		else if (target.invisible == 0) target.sprite.remove( CharSprite.State.DARKENED );
	}

	@Override
	public int icon() {
		return BuffIndicator.CORRUPT;
	}

	@Override
	public String toString() {
		return "Corrupted";
	}

	@Override
	public String desc() {
		return "Corruption seeps into the essence of a being, twisting them against their former nature.\n" +
				"\n" +
				"Corrupted creatures will attack and aggravate their allies, and ignore their former enemies. " +
				"Corruption is damaging as well, and will slowly cause its target to succumb.\n" +
				"\n" +
				"Corruption is permanent, its effects only end in death.";
	}
}
