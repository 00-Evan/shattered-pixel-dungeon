package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.HashSet;

/**
 * Created by debenhame on 19/11/2014.
 */
public class ToxicImbue extends Buff {

    public static final float DURATION	= 20f;

    @Override
    public boolean act() {
        GameScene.add(Blob.seed(target.pos, 50, ToxicGas.class));

        spend(TICK);
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

    static {
        immunities.add( ToxicGas.class );
        immunities.add( Poison.class );
    }
}
