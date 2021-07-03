package com.shatteredpixel.shatteredpixeldungeon.net;

public class Types {
    public class Recieve {
        public static final String AUTH = "auth";
        public static final String MOTD = "motd";
        public static final String MESSAGE = "message";
        public static final String ACTION = "action";
    }
    public class Send {
        public static final int AUTH = 0;
        public static final int ACTION = 1;
    }
}
