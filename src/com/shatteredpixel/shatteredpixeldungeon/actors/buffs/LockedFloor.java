package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

/**
 * Created by Evan on 04/04/2015.
 */
public class LockedFloor extends Buff {
	//this buff is purely meant as a visual indicator that the gameplay implications of a level seal are in effect.

	@Override
	public boolean act() {
		spend(TICK);

		if (!Dungeon.level.locked)
			detach();

		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.LOCKED_FLOOR;
	}

	@Override
	public String toString() {
		return "Floor is Locked";
	}

	@Override
	public String desc() {
		return "The current floor is locked, and you are unable to leave it!\n" +
				"\n" +
				"While a floor is locked, you will not gain hunger, or take damage from starving, " +
				"but your current hunger state is still in effect. For example, if you are starving you won't take " +
				"damage, but will still not regenerate health.\n" +
				"\n" +
				"Additionally, if you are revived by an unblessed ankh while the floor is locked, then it will reset.\n" +
				"\n" +
				"Kill this floor's boss to break the lock.\n";
	}
}
