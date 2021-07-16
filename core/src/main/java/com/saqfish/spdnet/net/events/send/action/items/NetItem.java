package com.saqfish.spdnet.net.events.send.action.items;

import com.saqfish.spdnet.items.Item;
import com.watabou.noosa.Game;

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
            this.className = clean(i.getClass().getName());
            System.out.println(this.className);
            this.level = ((Item)i).level();
        } catch(Exception ignored){}
    }

    private String clean(String name){
        return name.replace(Game.pkgName+".items.","");
    }
}
