package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.HashSet;

/**
 * Created by debenhame on 19/11/2014.
 */
public class EarthImbue extends FlavourBuff {

    public static final float DURATION	= 20f;

    public void proc(Char enemy){
        Buff.affect(enemy, Roots.class);
    }

    @Override
    public int icon() {
        return BuffIndicator.IMMUNITY;
    }

    @Override
    public String toString() {
        return "Imbued with Earth";
    }

    static {
        immunities.add( Paralysis.class );
        immunities.add( Roots.class );
        immunities.add( Slow.class );
    }
}