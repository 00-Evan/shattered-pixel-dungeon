package com.shatteredpixel.shatteredpixeldungeon.net.events.player;

import com.shatteredpixel.shatteredpixeldungeon.net.emit.Actions;

public class Ascend {
    public int type;
    public int depth;

    public Ascend(int depth){
        this.type = Actions.ASC;
        this.depth = depth;
    }
}
