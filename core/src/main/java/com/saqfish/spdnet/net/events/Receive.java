package com.saqfish.spdnet.net.events;

import com.saqfish.spdnet.items.Item;

public class Receive {
    public static final int MOVE = 0;
    public static final int JOIN = 1;
    public static final int JOIN_LIST = 2;
    public static final int LEAVE = 3;
    public static final int ITEM = 4;
    public static final int DEATH = 5;

    public static class Motd {
        public String motd;
        public long seed;
    }

    public static class Transfer {
        public String id;
        public String className;
        public int level;
        public boolean cursed;
        public boolean identified;
    }


    public static class Player {
        public String nick;
        public Integer playerClass;
        public Integer depth;
        public NetItems items;
    }

    public static class PlayerList {
        public Player[]list;
    }


    public static class Action {
        public int type;
        public String data;
    }

    public static class Death {
        public String nick;
        public String cause;
    }

    public static class Join {
        public String id;
        public int playerClass;
        public String nick;
        public int depth;
        public int pos;
        public NetItems items;
    }

    public static class NetItem {
        public int type;
        public String className;
        public int level;
    }

    public static class NetItems {
        public int type;
        public NetItem weapon;
        public NetItem armor;
        public NetItem artifact;
        public NetItem misc;
        public NetItem ring;
    }

    public static class JoinList {
        public Join[]players;
    }

    public static class Leave {
        public String id;
        public int playerClass;
        public String nick;
        public int depth;
        public int pos;
    }

    public static class Move {
        public String id;
        public int playerClass;
        public String nick;
        public int depth;
        public int pos;
    }
}
