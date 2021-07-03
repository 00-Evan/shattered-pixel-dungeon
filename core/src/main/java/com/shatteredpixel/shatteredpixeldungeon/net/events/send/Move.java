package com.shatteredpixel.shatteredpixeldungeon.net.events.send;

public class Move {
    public int type;
    public int pos;
    public int depth;

    public Move(int depth, int pos){
        this.type = Actions.MOVE;
        this.depth = depth;
        this.pos = pos;
    }
}
