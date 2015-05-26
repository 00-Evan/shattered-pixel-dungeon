package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

/**
 * Created by Evan on 12/04/2015.
 */
public class Venom extends Poison implements Hero.Doom {

    private int damage = 1+ Dungeon.depth/5;

    private static final String DAMAGE	= "damage";

    {
        type = buffType.NEGATIVE;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( DAMAGE, damage );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        damage = bundle.getInt( DAMAGE );
    }

    @Override
    //TODO: new icon?
    public int icon() {
        return BuffIndicator.POISON;
    }

    @Override
    public String toString() {
        return "Venomed";
    }

    @Override
    public String desc() {
        return "Venom is a extremely caustic and dangerous poison.\n" +
                "\n" +
                "Unlike poison, whose damage lowers over time, venom does increasing damage the longer it stays on a target.\n" +
                "\n" +
                "This venom will last for " + dispTurns(left) + ", and is currently dealing " + damage + " damage.";
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {
            target.damage(damage, this);
            damage = Math.min(damage+1+Dungeon.depth/10, ((Dungeon.depth+1)/2)+1);

            //want it to act after the cloud of venom it came from.
            spend( TICK+0.1f );
            if ((left -= TICK) <= 0) {
                detach();
            }
        } else {
            detach();
        }

        return true;
    }

}
