package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;

/**
 * Created by Evan on 24/08/2014.
 */
public abstract class KindofMisc extends EquipableItem {

    public abstract void activate(Char ch);
}
