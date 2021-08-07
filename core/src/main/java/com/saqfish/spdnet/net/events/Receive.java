/*
 * Pixel Dungeon
 * Copyright (C) 2021 saqfish
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.saqfish.spdnet.net.events;

import com.saqfish.spdnet.items.Item;

public class Receive {
    public static final int MOVE = 0;
    public static final int JOIN = 1;
    public static final int JOIN_LIST = 2;
    public static final int LEAVE = 3;
    public static final int ITEM = 4;
    public static final int GLOG = 5;

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


    public static class Record {
        public String nick;
        public Integer playerClass;
        public Integer depth;
        public NetItems items;
        public int wins;
    }

    public static class Records {
        public Record[] records;
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


    public static class Glog {
        public String msg;
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
