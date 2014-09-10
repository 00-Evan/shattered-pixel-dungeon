package com.shatteredpixel.shatteredpixeldungeon.items.rings;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfFuror extends Ring {
    //TODO: tie this into game logic
    {
        name = "Ring of Furor";
    }

    @Override
    protected RingBuff buff( ) {
        return new Furor();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "This ring grants the wearer a sort of rising inner fury. " +
                "Every successful attack will increase the wearers attacking speed " +
                "until they stop fighting or miss too frequently. " +
                "A cursed ring will instead slow the wearer's speed of attack." :
                super.desc();
    }

    public class Furor extends RingBuff {
    }
}
