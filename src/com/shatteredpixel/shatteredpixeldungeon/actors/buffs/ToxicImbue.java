package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import java.util.HashSet;

/**
 * Created by debenhame on 19/11/2014.
 */
public class ToxicImbue extends Buff {

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
        GameScene.add(Blob.seed(target.pos, 50, ToxicGas.class));

        spend(TICK);
        left -= TICK;
        if (left <= 0)
            detach();

        return true;
    }

    @Override
    public int icon() {
        return BuffIndicator.IMMUNITY;
    }

    @Override
    public String toString() {
        return "Imbued with Toxicity";
    }

    {
        immunities.add( ToxicGas.class );
        immunities.add( Poison.class );
    }
}
