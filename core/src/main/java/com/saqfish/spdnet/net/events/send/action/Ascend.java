package com.saqfish.spdnet.net.events.send.action;

public class Ascend {
    public int playerClass;
    public int depth;
    public int pos;

    public Ascend(int playerClass, int depth, int pos){
        this.playerClass = playerClass;
        this.depth = depth;
        this.pos = pos;
    }
}
