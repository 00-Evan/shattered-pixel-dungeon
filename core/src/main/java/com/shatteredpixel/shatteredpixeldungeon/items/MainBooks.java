package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class MainBooks extends Item {
    //extends 相当于 copyfrom,但不同的是 它是一种继承自己

    @Override
    public String info() {
        return desc()+"\n\n"+author;
    }
    @Override
    public int value() {
        return 20 * quantity;
    }
    public String author = Messages.get(this, "author");
}
