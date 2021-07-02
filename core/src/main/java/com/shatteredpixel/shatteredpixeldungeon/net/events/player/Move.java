package com.shatteredpixel.shatteredpixeldungeon.net.events.player;

import com.shatteredpixel.shatteredpixeldungeon.net.emit.Actions;

public class Move {
    public int type;
    public int dst;

    public Move(int dst){
        this.type = Actions.MOVE;
        this.dst = dst;
    }
}
