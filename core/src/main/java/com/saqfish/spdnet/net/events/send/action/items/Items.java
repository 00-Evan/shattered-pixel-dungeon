package com.saqfish.spdnet.net.events.send.action.items;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.items.KindOfWeapon;
import com.saqfish.spdnet.items.KindofMisc;
import com.saqfish.spdnet.items.armor.Armor;
import com.saqfish.spdnet.items.artifacts.Artifact;
import com.saqfish.spdnet.items.rings.Ring;
import com.saqfish.spdnet.net.events.send.Send;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class Items {
    public int type;
    public NetItem weapon;
    public NetItem armor;
    public NetItem artifact;
    public NetItem misc;
    public NetItem ring;

    public Items(){
        this.type = Types.ALL;
        this.weapon = new NetItem(Types.WEAPON, Dungeon.hero.belongings.weapon);
        this.armor = new NetItem(Types.ARMOR, Dungeon.hero.belongings.armor);
        this.artifact = new NetItem(Types.ARTIFACT, Dungeon.hero.belongings.artifact);
        this.misc = new NetItem(Types.MISC, Dungeon.hero.belongings.misc);
        this.ring = new NetItem(Types.RING, Dungeon.hero.belongings.ring);
    }

    public static void send(){
        Items items = new Items();
        try {
            String json = net().mapper().writeValueAsString(items);
            net().sendAction(Send.ITEM, json);
        } catch (JsonProcessingException ignored) { }
    }

    public static void sendSingle(Object o){
        int type = 0;
        NetItem n = null;

        if(o instanceof KindOfWeapon) type = Types.WEAPON;
        if(o instanceof Armor) type = Types.ARMOR;
        if(o instanceof Artifact) type = Types.ARTIFACT;
        if(o instanceof KindofMisc) type = Types.MISC;
        if(o instanceof Ring) type = Types.RING;

        switch (type){
            case Types.WEAPON:
                n = new NetItem(Types.WEAPON, Dungeon.hero.belongings.weapon);
                break;
            case Types.ARMOR:
                n = new NetItem(Types.ARMOR, Dungeon.hero.belongings.armor);
                break;
            case Types.ARTIFACT:
                n = new NetItem(Types.ARTIFACT, Dungeon.hero.belongings.artifact);
                break;
            case Types.MISC:
                n = new NetItem(Types.MISC, Dungeon.hero.belongings.misc);
                break;
            case Types.RING:
                n = new NetItem(Types.RING, Dungeon.hero.belongings.ring);
                break;
        }

        try {
            String json = net().mapper().writeValueAsString(n);
            net().sendAction(Send.ITEM, json);
        } catch (JsonProcessingException ignored) { }
    }
}
