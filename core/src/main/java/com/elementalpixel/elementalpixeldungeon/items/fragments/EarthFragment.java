package com.elementalpixel.elementalpixeldungeon.items.fragments;

import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;

public class EarthFragment extends Fragment {
    {
        image = ItemSpriteSheet.EARTH_FRAGMENT;
        unique = true;
        stackable = false;
        bones = false;
    }

    public EarthFragment(int depth) {
        super();
    }

    @Override
    public boolean doPickUp( Hero hero ) {
        if (super.doPickUp( hero )) {
            return true;
        } else {
            return false;
        }
    }
}