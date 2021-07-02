package com.shatteredpixel.shatteredpixeldungeon.net.events.player.recieve;

public class Move {
    public int type;
    public Player player;
    public Data data;

    public class Player {
        public String nick;
    }
    public class Data {
        public int dst;
        public int depth;
    }
}
