package com.elementalpixel.elementalpixeldungeon.items.fragments;

import com.elementalpixel.elementalpixeldungeon.items.Item;


public class Fragment extends Item {
    {
        stackable = false;

        bones = false;
        unique = true;
    }

    @Override
    public boolean isUpgradable() { return false; }

    @Override
    public boolean isIdentified() { return true; }
}
