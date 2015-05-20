package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

/**
 * Created by debenhame on 27/04/2015.
 */
public class Bless extends FlavourBuff {

	@Override
	public int icon() {
		//TODO: add icon
		return BuffIndicator.FIRE;
	}

	@Override
	public String toString() {
		return "Blessed";
	}

}
