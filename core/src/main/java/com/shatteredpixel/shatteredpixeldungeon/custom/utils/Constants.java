package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.watabou.noosa.Game;

public class Constants {

    //Chapter length.
    public static final int CHAPTER_LENGTH = 5;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int MAX_DEPTH = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;

    //
    //############################## UI STUFF ##############################
    //

    public static final int MAX_QUICKSLOTS = 12;
    public static final int MIN_QUICKSLOTS = 4;

    public static boolean gameIsAndroid(){
        return Game.platform.getClass().getSimpleName().contains("Android");
    }

    public static class Colours {

        public static final int PURE_RED   = 0xFF0000;
        public static final int PURE_GREEN = 0x00FF00;
        public static final int PURE_BLUE  = 0x0000FF;
        public static final int YELLOW     = 0xFFFF00;
        public static final int PURPLE     = 0xFF00FF;
        public static final int LIGHT_BLUE = 0x6496FF;
        public static final int PURE_WHITE = 0xFFFFFF;

    }
}
