package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Created by debenhame on 19/11/2014.
 */
public class FireImbue extends Buff {

    public static final float DURATION	= 20f;

    @Override
    public boolean act() {
        if (Dungeon.level.map[target.pos] == Terrain.GRASS)
            Dungeon.level.set(target.pos, Terrain.EMBERS);

        spend(TICK);
        return true;
    }

    public void proc(Char enemy){
        if (Random.Int(3) == 0)
            Buff.affect( enemy, Burning.class ).reignite( enemy );

        enemy.sprite.emitter().burst( FlameParticle.FACTORY, 2 );
    }

    @Override
    public int icon() {
        return BuffIndicator.IMMUNITY;
    }

    @Override
    public String toString() {
        return "Imbued with Fire";
    }

    public static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Burning.class );
    }
}
