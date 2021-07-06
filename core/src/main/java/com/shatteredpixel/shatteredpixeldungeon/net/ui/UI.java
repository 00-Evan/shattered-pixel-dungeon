package com.shatteredpixel.shatteredpixeldungeon.net.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.NinePatch;

public class UI {
    public enum Type {
        WINDOW,
        BUTTON
    }

    public static NinePatch get(Type type ) {
        String Asset = Assets.Interfaces.NETUI;
        switch (type) {
            case WINDOW:
                return new NinePatch(Asset, 0, 0, 20, 20, 6);
            case BUTTON:
                return new NinePatch( Asset, 20, 0, 6, 6, 2 );
            default:
                return null;
        }
    }
}
