package com.shatteredpixel.shatteredpixeldungeon.net.events.player.send;

import com.shatteredpixel.shatteredpixeldungeon.net.emit.Actions;

public class Move {
    public int type;
    public int dst;
    public int depth;

    public Move(int depth, int dst){
        this.type = Actions.MOVE;
        this.depth = depth;
        this.dst = dst;
    }
}
