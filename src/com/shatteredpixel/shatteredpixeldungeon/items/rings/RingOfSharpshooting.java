package com.shatteredpixel.shatteredpixeldungeon.items.rings;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfSharpshooting extends Ring {

    {
        name = "Ring of Sharpshooting";
    }

    @Override
    protected RingBuff buff( ) {
        return new Aim();
    }

    @Override
    public String desc() {
        return isKnown() ?
                "This ring enhances the wearer's precision and aim, which will " +
                "make all projectile weapons more accurate and durable. " +
                "A degraded ring will have the opposite effect.":
                super.desc();
    }

    public class Aim extends RingBuff {
    }
}
