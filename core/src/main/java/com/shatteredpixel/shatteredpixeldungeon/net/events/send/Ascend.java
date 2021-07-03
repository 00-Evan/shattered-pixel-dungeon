package com.shatteredpixel.shatteredpixeldungeon.net.events.send;

public class Ascend {
    public int type;
    public int depth;
    public int pos;

    public Ascend(int depth, int pos){
        this.type = Actions.ASC;
        this.depth = depth;
    }
}
