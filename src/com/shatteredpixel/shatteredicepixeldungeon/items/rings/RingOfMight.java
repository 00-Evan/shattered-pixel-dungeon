package com.shatteredpixel.shatteredicepixeldungeon.items.rings;


/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfMight extends Ring {

    {
        name = "Ring of Might";
    }

    @Override
    protected RingBuff buff( ) {
        return new Might();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "This ring enhances the physical traits of the wearer, " +
                "granting them greater physical strength and constitution. " +
                "A degraded ring will weaken the wearer." :
                super.desc();
    }

    public class Might extends RingBuff {
    }
}

