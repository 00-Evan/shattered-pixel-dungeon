package com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.action;

public class Move {
    public int type;
    public Data data;

    public class Data {
        public int dst;
        public int pos;
    }
}
