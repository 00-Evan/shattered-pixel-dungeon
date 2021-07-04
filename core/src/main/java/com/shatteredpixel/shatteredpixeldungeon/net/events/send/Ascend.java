package com.shatteredpixel.shatteredpixeldungeon.net.events.send;

public class Ascend {
    public int type;
    public int playerClass;
    public int depth;
    public int pos;

    public Ascend(int playerClass, int depth, int pos){
        this.type = Actions.ASC;
        this.playerClass = playerClass;
        this.depth = depth;
        this.pos = pos;
    }
}
