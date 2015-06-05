package com.shatteredpixel.shatteredicepixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

/**
 * Created by debenhame on 19/11/2014.
 */
public class FireImbue extends Buff {

    public static final float DURATION	= 30f;

    protected float left;

    private static final String LEFT	= "left";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        left = bundle.getFloat( LEFT );
    }

    public void set( float duration ) {
        this.left = duration;
    };

    @Override
    public boolean act() {
        if (Dungeon.level.map[target.pos] == Terrain.GRASS) {
            Dungeon.level.set(target.pos, Terrain.EMBERS);
            GameScene.updateMap(target.pos);
        }

        spend(TICK);
        left -= TICK;
        if (left <= 0)
            detach();

        return true;
    }

    public void proc(Char enemy){
        if (Random.Int(2) == 0)
            Buff.affect( enemy, Burning.class ).reignite( enemy );

        enemy.sprite.emitter().burst( FlameParticle.FACTORY, 2 );
    }

    @Override
    public int icon() {
        return BuffIndicator.FIRE;
    }

    @Override
    public String toString() {
        return "Imbued with Fire";
    }

    {
        immunities.add( Burning.class );
    }
}
