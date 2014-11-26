package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlot;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by debenhame on 26/11/2014.
 */
public class UnstableSpellbook extends Artifact {
    //TODO: add levelling logic, polish, bugfixes

    {
        name = "unstable spellbook";
        image = 0;

        level = 0;
        levelCap = 10;
        charge = (int)((level*0.4)+1);
        chargeCap = (int)((level*0.4)+1);
        defaultAction = AC_READ;
    }

    public static final String AC_READ = "READ";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge > 0)
            actions.add(AC_READ);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_READ)) {
            charge --;

            Scroll scroll = null;
            do {
                scroll = (Scroll) Generator.random(Generator.Category.SCROLL);
            } while (scroll != null && scroll instanceof ScrollOfPsionicBlast);

            //TODO: prevent scrolls from being IDed here, consider merging functionality with potion's ownedByFruit if possible.
            scroll.execute(hero, AC_READ);

        } else
            super.execute( hero, action );
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new bookRecharge();
    }

    @Override
    public Item upgrade() {
        chargeCap = (int)((level*0.4)+1);
        return super.upgrade();
    }

    @Override
    public String desc() {
        return "";
    }

    //needs to bundle chargecap as it is dynamic.
    private static final String CHARGECAP = "chargecap";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGECAP, chargeCap );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        chargeCap = bundle.getInt( CHARGECAP );
    }

    public class bookRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            if (charge < chargeCap) {
                partialCharge += 1 / (200f - (chargeCap - charge)*20f);

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


}
