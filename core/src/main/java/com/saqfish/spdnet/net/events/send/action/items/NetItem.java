package com.saqfish.spdnet.net.events.send.action.items;

import com.saqfish.spdnet.items.Item;

public class NetItem {
    public int type;
    public String className;
    public int level;

    public NetItem(){
        this.type = 0;
        this.className = null;
        this.level = 0;
    }

    public NetItem(int t, Object i){
        this.type = t;
        try{
            this.className = i.getClass().getName();
            this.level = ((Item)i).level();
        } catch(Exception ignored){}
    }
}
