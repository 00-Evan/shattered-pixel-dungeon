package com.saqfish.spdnet.net.windows;

import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.net.events.send.action.items.NetItem;
import com.saqfish.spdnet.net.events.send.action.items.NetItems;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.ui.ItemSlot;
import com.saqfish.spdnet.windows.WndBag;
import com.watabou.noosa.Game;
import com.watabou.utils.Reflection;

public class WndItemsList extends NetWindow{
    public WndItemsList(NetItems item) {
        super();

        int ITEM_HEIGHT = 23;
        int ITEM_WIDTH = 23;
        int x = 0;

        ItemSlot weaponSlot = itemSlot(item.weapon, ItemSpriteSheet.WEAPON_HOLDER);
        weaponSlot.setRect(x, 0, ITEM_WIDTH, ITEM_HEIGHT);
        add(weaponSlot);
        x += ITEM_WIDTH;

        ItemSlot armorSlot = itemSlot(item.armor, ItemSpriteSheet.ARMOR_HOLDER);
        armorSlot.setRect(x, 0, ITEM_WIDTH, ITEM_HEIGHT);
        add(armorSlot);
        x += ITEM_WIDTH;

        ItemSlot artifactSlot = itemSlot(item.artifact, ItemSpriteSheet.ARTIFACT_HOLDER);
        artifactSlot.setRect(x, 0, ITEM_WIDTH, ITEM_HEIGHT);
        add(artifactSlot);
        x += ITEM_WIDTH;

        ItemSlot miscSlot = itemSlot(item.misc, ItemSpriteSheet.SOMETHING);
        miscSlot.setRect(x, 0, ITEM_WIDTH, ITEM_HEIGHT);
        add(miscSlot);
        x += ITEM_WIDTH;

        ItemSlot ringSlot = itemSlot(item.ring, ItemSpriteSheet.RING_HOLDER);
        ringSlot.setRect(x, 0, ITEM_WIDTH, ITEM_HEIGHT);
        add(ringSlot);
        x += ITEM_WIDTH;

        resize((int) x, ITEM_HEIGHT);
    }

    private ItemSlot itemSlot (NetItem item, int placeHolder){
        ItemSlot slot;
        try{
            Class<?> k = Reflection.forNameUnhandled(addPkgName(item.className));
            Item a = (Item)Reflection.newInstance(k);
            a.level(item.level);
            slot = new ItemSlot(a);
        }catch(Exception ignored){
            WndBag.Placeholder p = new WndBag.Placeholder(placeHolder);
            slot = new ItemSlot(p);
        }
        return slot;
    }

    private String addPkgName(String c){
        return Game.pkgName+".items."+c;
    }
}
