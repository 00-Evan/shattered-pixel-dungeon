package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

/**
 * Created by debenhame on 27/04/2015.
 */
public class Bless extends FlavourBuff {

	{
		type = buffType.POSITIVE;
	}

	@Override
	public int icon() {
		return BuffIndicator.BLESS;
	}

	@Override
	public String toString() {
		return "Blessed";
	}

	@Override
	public String desc() {
		return "A great burst of focus, some say it is inspired by the gods.\n" +
				"\n" +
				"Blessing significantly increases accuracy and evasion, making the blessed much more effective in combat.\n" +
				"\n" +
				"This blessing will last for  " + dispTurns() + ".";
	}
}
