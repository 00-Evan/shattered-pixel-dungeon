package com.shatteredpixel.shatteredicepixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredicepixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.HashSet;

/**
 * Created by debenhame on 19/11/2014.
 */
public class EarthImbue extends FlavourBuff {

    public static final float DURATION	= 30f;

    public void proc(Char enemy){
        Buff.affect(enemy, Roots.class, 2);
        CellEmitter.bottom(enemy.pos).start(EarthParticle.FACTORY, 0.05f, 8);
    }

    @Override
    public int icon() {
        return BuffIndicator.ROOTS;
    }

    @Override
    public String toString() {
        return "Imbued with Earth";
    }

    {
        immunities.add( Paralysis.class );
        immunities.add( Roots.class );
        immunities.add( Slow.class );
    }
}