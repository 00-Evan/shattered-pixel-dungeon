package com.shatteredpixel.shatteredpixeldungeon.items.rings;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfWealth extends Ring {
    //TODO: monitor this one as it goes, super hard to balance so you'll need some feedback.
    {
        name = "Ring of Wealth";
    }

    @Override
    protected RingBuff buff( ) {
        return new Wealth();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "It's not clear what this ring does exactly, good luck may influence " +
                "the life an an adventurer in many subtle ways. " +
                "Naturally a cursed ring would give bad luck." :
                super.desc();
    }

    public class Wealth extends RingBuff {
    }
}
