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

}
