package com.shatteredpixel.shatteredpixeldungeon.net.events.player;

import com.shatteredpixel.shatteredpixeldungeon.net.emit.Actions;

public class Descend {
    public int type;
    public int depth;

    public Descend(int depth){
        this.type = Actions.DESC;
        this.depth = depth;
    }
}
