package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.watabou.noosa.Game;

public class Constants {
    public static final int MAX_DEPTH = 26;

    //public static Group parent = Dungeon.hero.sprite.parent;

    public static boolean gameIsAndroid(){
        return Game.platform.getClass().getSimpleName().contains("Android");
    }
}
