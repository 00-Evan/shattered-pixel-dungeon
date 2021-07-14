package com.saqfish.spdnet.net.windows;

import com.saqfish.spdnet.items.KindOfWeapon;
import com.saqfish.spdnet.items.KindofMisc;
import com.saqfish.spdnet.items.armor.Armor;
import com.saqfish.spdnet.items.artifacts.Artifact;
import com.saqfish.spdnet.items.rings.Ring;
import com.saqfish.spdnet.net.events.send.action.items.NetItems;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.ui.ItemSlot;
import com.saqfish.spdnet.windows.WndBag;
import com.watabou.utils.Reflection;

public class WndItemsList extends NetWindow{
    public WndItemsList(NetItems item) {
        super();
        long x = 0;
        long y = 0;

        //TODO: clean this up
        ItemSlot weaponSlot;
        try{
            Class k = Reflection.forNameUnhandled(item.weapon.className);
            KindOfWeapon weapon = (KindOfWeapon)Reflection.newInstance(k) ;
            weaponSlot = new ItemSlot(weapon);
        }catch(Exception ignored){
            WndBag.Placeholder p = new WndBag.Placeholder( ItemSpriteSheet.WEAPON_HOLDER );
            weaponSlot = new ItemSlot(p);
        }
        weaponSlot.setRect( x, 0, 28, 23 );
        add(weaponSlot);
        x+=weaponSlot.width();
        y+=weaponSlot.height();

        ItemSlot armorSlot;
        try{
            Class k = Reflection.forNameUnhandled(item.armor.className);
            Armor a = (Armor)Reflection.newInstance(k);
            armorSlot = new ItemSlot(a);
        }catch(Exception ignored){
            WndBag.Placeholder p = new WndBag.Placeholder( ItemSpriteSheet.ARMOR_HOLDER  );
            armorSlot = new ItemSlot(p);
        }
        armorSlot.setRect( x, 0, 28, 23 );
        add(armorSlot);
        x+=armorSlot.width();

        ItemSlot artifactSlot;
        try{
            Class k = Reflection.forNameUnhandled(item.artifact.className);
            Artifact a = (Artifact)Reflection.newInstance(k);
            artifactSlot = new ItemSlot(a);
        }catch(Exception ignored){
            WndBag.Placeholder p = new WndBag.Placeholder( ItemSpriteSheet.ARTIFACT_HOLDER );
            artifactSlot = new ItemSlot(p);
        }
        artifactSlot.setRect( x, 0, 28, 23 );
        add(artifactSlot);
        x+=artifactSlot.width();

        ItemSlot miscSlot;
        try{
            Class k = Reflection.forNameUnhandled(item.misc.className);
            KindofMisc m = (KindofMisc) Reflection.newInstance(k);
            miscSlot = new ItemSlot(m);
        }catch(Exception ignored){
            WndBag.Placeholder p = new WndBag.Placeholder( ItemSpriteSheet.SOMETHING );
            miscSlot = new ItemSlot(p);
        }
        miscSlot.setRect( x, 0, 28, 23 );
        add(miscSlot);
        x+=miscSlot.width();

        ItemSlot ringSlot;
        try{
            Class k = Reflection.forNameUnhandled(item.ring.className);
            Ring a = (Ring)Reflection.newInstance(k);
            ringSlot = new ItemSlot(a);
        }catch(Exception ignored){
            WndBag.Placeholder p = new WndBag.Placeholder( ItemSpriteSheet.RING_HOLDER );
            ringSlot = new ItemSlot(p);
        }
        ringSlot.setRect( x, 0, 28, 23 );
        add(ringSlot);
        x+=ringSlot.width();

        resize((int)x, (int)y);
    }
}
