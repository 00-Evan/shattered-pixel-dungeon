package com.shatteredpixel.shatteredpixeldungeon.net.events.send;

public class Move {
    public int type;
    public int playerClass;
    public int pos;
    public int depth;

    public Move(int playerClass, int depth, int pos){
        this.type = Actions.MOVE;
        this.playerClass = playerClass;
        this.depth = depth;
        this.pos = pos;
    }
}
