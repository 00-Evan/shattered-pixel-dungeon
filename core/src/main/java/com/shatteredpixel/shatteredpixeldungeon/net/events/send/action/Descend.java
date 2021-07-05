package com.shatteredpixel.shatteredpixeldungeon.net.events.send.action;

public class Descend {
    public int type;
    public int playerClass;
    public int depth;
    public int pos;

    public Descend(int playerClass, int depth, int pos){
        this.type = Actions.DESC;
        this.playerClass = playerClass;
        this.depth = depth;
        this.pos = pos;
    }
}
