package com.shatteredpixel.shatteredpixeldungeon.net.events.send;

public class Descend {
    public int type;
    public int depth;
    public int pos;

    public Descend(int depth, int pos){
        this.type = Actions.DESC;
        this.depth = depth;
        this.pos = pos;
    }
}
