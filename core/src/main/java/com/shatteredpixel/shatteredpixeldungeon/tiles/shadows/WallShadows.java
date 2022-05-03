package com.shatteredpixel.shatteredpixeldungeon.tiles.shadows;

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class WallShadows implements Bundlable {

    public int image;
    public int pos;

    public WallShadows set(int pos){
        this.pos = pos;
        return this;
    }

    private static final String POS	= "pos";

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        pos = bundle.getInt( POS );
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        bundle.put( POS, pos );
    }
}
