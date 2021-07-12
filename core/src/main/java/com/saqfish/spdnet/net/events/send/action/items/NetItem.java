package com.saqfish.spdnet.net.events.send.action.items;

import com.saqfish.spdnet.items.Item;

public class NetItem {
    public int type;
    public String className;
    public int level;

    public NetItem(int t, Object i){
        this.type = t;
        try{
            this.className = i.getClass().getSimpleName();
            this.level = ((Item)i).level();
        } catch(Exception ignored){}
    }
}
