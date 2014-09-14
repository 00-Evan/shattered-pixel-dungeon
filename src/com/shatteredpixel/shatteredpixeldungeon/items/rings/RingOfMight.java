package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

/**
 * Created by debenhame on 10/09/2014.
 */
public class RingOfMight extends Ring {
    //TODO: test this, numbers tweaking
    //specifically, make sure this works with levelling up the ring
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
                "A cursed ring will weaken the wearer." :
                super.desc();
    }

    public class Might extends RingBuff {

        @Override
        public boolean attachTo( Char target ) {

            if (target instanceof Hero){
                ((Hero) target).STR += level;
            }
            if (level > 0){
                target.HT += level*5;
            }
            return super.attachTo(target);
        }

        @Override
        public void detach(){
            if (target instanceof Hero){
                ((Hero) target).STR -= level;
            }
            if (level > 0){
                target.HT -= level*5;
            }
            super.detach();
        }
    }
}

