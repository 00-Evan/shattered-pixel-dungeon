package com.shatteredpixel.shatteredpixeldungeon.items.rings;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfTenacity extends Ring {
    //TODO: numbers tweaking
    {
        name = "Ring of Tenacity";
    }

    @Override
    protected RingBuff buff( ) {
        return new Tenacity();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "When worn, this ring will allow the wearer to resist normally mortal strikes. " +
                "The weaker the user becomes, the more resistant they will be to physical damage. " +
                "A cursed ring will instead make it easier for the wearer to be executed." :
                super.desc();
    }

    public class Tenacity extends RingBuff {
    }
}

