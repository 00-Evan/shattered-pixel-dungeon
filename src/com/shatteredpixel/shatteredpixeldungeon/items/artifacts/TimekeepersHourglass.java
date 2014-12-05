package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by debenhame on 01/12/2014.
 */
public class TimekeepersHourglass extends Artifact {
    //TODO: string, effect implementation.

    private static final String TXT_HGLASS	= "Timekeeper's Hourglass";
    private static final String TXT_STASIS	= "Put myself in stasis";
    private static final String TXT_FREEZE	= "Freeze time around me";
    private static final String TXT_DESC 	=
            "...";

    {
        name = "timekeeper's hourglass";
        image = ItemSpriteSheet.ARTIFACT_HOURGLASS;

        level = 0;
        levelCap = 5;
        charge = 5+level;
        chargeCap = 5+level;
        defaultAction = AC_ACTIVATE;
    }

    private static final String TXT_CHARGE  = "%d/%d";

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
            GameScene.show(
                    new WndOptions(TXT_HGLASS, TXT_STASIS, TXT_FREEZE, TXT_DESC) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                GLog.i("WIP");
                            } else if (index == 1){
                                GLog.i("everything around you slows to a halt.");
                                GameScene.flash( 0xFFFFFF );
                                Sample.INSTANCE.play( Assets.SND_BLAST );

                                activeBuff = new timeFreeze();
                                activeBuff.attachTo(Dungeon.hero);
                            }

                        };
                    }
            );
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

    @Override
    public String status() {
        return Utils.format(TXT_CHARGE, charge, chargeCap);
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
        //todo: add visuals, test


        @Override
        public boolean attachTo(Char target) {
            spend(charge*2);
            ((Hero)target).spend(charge*2);

            Hunger hunger = target.buff(Hunger.class);
            if (hunger != null && !hunger.isStarving())
                hunger.satisfy(charge*2);

            charge = 0;

            target.invisible++;

            QuickSlot.refresh();

            return super.attachTo(target);
        }

        @Override
        public boolean act() {
            target.invisible --;
            detach();
            return true;
        }
    }

    public class timeFreeze extends ArtifactBuff {
        //todo: add visual effects

        float partialTime = 0f;

        ArrayList<Integer> presses = new ArrayList<Integer>();

        public boolean processTime(float time){
            partialTime += time;

            while (partialTime >= 1){
                partialTime --;
                charge --;
            }

            QuickSlot.refresh();

            if (charge <= 0){
                detach();
                return false;
            } else
                return true;

        }

        public void delayedPress(int cell){
            if (!presses.contains(cell))
                presses.add(cell);
        }

        @Override
        public void detach(){
            for (int cell : presses)
                Dungeon.level.press(cell, null);

            charge = 0;
            QuickSlot.refresh();
            super.detach();
        }
    }

    public static class sandBag extends Item {

        {
            name = "bag of magic sand";
            image = ItemSpriteSheet.SANDBAG;
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
