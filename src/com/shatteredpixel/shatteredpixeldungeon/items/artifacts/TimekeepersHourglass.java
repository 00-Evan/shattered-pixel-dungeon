package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by debenhame on 01/12/2014.
 */
public class TimekeepersHourglass extends Artifact {
    //TODO: string, effect implementation.

    {
        name = "timekeeper's hourglass";
        image = 0;

        level = 0;
        levelCap = 5;
        charge = 5+level;
        chargeCap = 5+level;
        defaultAction = AC_ACTIVATE;
    }

    public static final String AC_ACTIVATE = "ACTIVATE";

    //keeps track of generated sandbags.
    public int sandBags = 0;

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge > 0)
            actions.add(AC_ACTIVATE);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals(AC_ACTIVATE)){
            //todo: add logic here
        } else
            super.execute(hero, action);
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new hourglassRecharge();
    }

    @Override
    public Item upgrade() {
        chargeCap++;

        //for artifact transmutation.
        while (level+1 > sandBags)
            sandBags ++;

        return super.upgrade();
    }

    @Override
    public String desc() {
        return "";
    }

    //needs to bundle chargecap as it is dynamic.
    private static final String CHARGECAP = "chargecap";
    private static final String SANDBAGS =  "sandbags";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGECAP, chargeCap );
        bundle.put( SANDBAGS, sandBags );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        chargeCap = bundle.getInt( CHARGECAP );
        sandBags = bundle.getInt( SANDBAGS );
    }

    public class hourglassRecharge extends ArtifactBuff {
        @Override
        public boolean act() {
            if (charge < chargeCap) {
                partialCharge += 1 / (40f - (chargeCap - charge)*3f);

                if (partialCharge >= 1) {
                    partialCharge --;
                    charge ++;

                    if (charge == chargeCap){
                        partialCharge = 0;
                    }
                }
            }

            QuickSlot.refresh();

            spend( TICK );

            return true;
        }
    }

    public class timeStasis extends ArtifactBuff {
        //todo: add logic here
    }

    public class timeFreeze extends ArtifactBuff {
        //todo: add logic here
    }

    public static class sandBag extends Item {

        {
            name = "bag of magic sand";
            image = 0;
        }

        @Override
        public boolean doPickUp( Hero hero ) {
            TimekeepersHourglass hourglass = hero.belongings.getItem( TimekeepersHourglass.class );
            if (hourglass != null) {
                hourglass.upgrade();
                Sample.INSTANCE.play( Assets.SND_ITEM );
                if (hourglass.level == hourglass.levelCap)
                    GLog.p("Your hourglass is filled with magical sand!");
                else
                    GLog.i("you add the sand to your hourglass.");
                hero.spendAndNext(TIME_TO_PICK_UP);
                return true;
            } else {
                GLog.w("You have no hourglass to place this sand into.");
                return false;
            }
        }

        @Override
        public String desc(){
            return "This small bag of finely ground sand should work perfectly with your hourglass.\n\n" +
                    "It seems odd that the shopkeeper would have this specific item right when you need it.";
        }

        @Override
        public int price() {
            return 20;
        }
    }


}
