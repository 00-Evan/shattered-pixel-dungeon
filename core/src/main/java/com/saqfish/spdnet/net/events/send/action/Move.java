package com.saqfish.spdnet.net.events.send.action;

public class Move {
    public int playerClass;
    public int pos;
    public int depth;

    public Move(int playerClass, int depth, int pos){
        this.playerClass = playerClass;
        this.depth = depth;
        this.pos = pos;
    }
}
