package com.saqfish.spdnet.net.events.send.action;

public class Descend {
    public int playerClass;
    public int depth;
    public int pos;

    public Descend(int playerClass, int depth, int pos){
        this.playerClass = playerClass;
        this.depth = depth;
        this.pos = pos;
    }
}
