package com.shatteredpixel.shatteredpixeldungeon.items.rings;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfForce extends Ring {

    {
        name = "Ring of Force";
    }

    @Override
    protected RingBuff buff( ) {
        return new Force();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "This ring enhances the force of the wearer's blows. " +
                "This extra power is largely wasted when wielding weapons, " +
                "but an unarmed attack will be made much stronger. " +
                "A cursed ring will instead weaken the wearer's blows." :
                 super.desc();
    }

    public class Force extends RingBuff {
    }
}

