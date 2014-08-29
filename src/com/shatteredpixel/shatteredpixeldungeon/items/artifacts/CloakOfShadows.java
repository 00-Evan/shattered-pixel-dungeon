package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by debenhame on 25/08/2014.
 */
public class CloakOfShadows extends Artifact {
    //TODO: testing, add polish

    {
        name = "Cloak of Shadows";
        image = ItemSpriteSheet.ARTIFACT_CLOAK;
        level = 0;
        charge = level+5;
        chargeCap = level+5;
        defaultAction = AC_STEALTH;
    }

    private boolean stealthed = false;

    private int exp = 0;

    public static final String AC_STEALTH = "STEALTH";

    private static final String TXT_CHARGE  = "%d/%d";
    private static final String TXT_CD	    = "%d";

    private int cooldown = 0;

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ))
            actions.add(AC_STEALTH);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_STEALTH )) {

            if (!stealthed){
                if (cooldown <= 0 && charge > 0 && isEquipped(hero)) {
                    stealthed = true;
                    hero.spend( 1f );
                    Sample.INSTANCE.play(Assets.SND_MELD);
                    activeBuff = activeBuff();
                    activeBuff.attachTo(hero);
                    if (hero.sprite.parent != null) {
                        hero.sprite.parent.add(new AlphaTweener(hero.sprite, 0.4f, 0.4f));
                    } else {
                        hero.sprite.alpha(0.4f);
                    }
                    hero.sprite.operate(hero.pos);
                    GLog.i("Your cloak blends you into the shadows.");
                } else if (!isEquipped(hero)) {
                    GLog.i("You need to equip your cloak to do that.");
                } else if (cooldown > 0) {
                    GLog.i("Your cloak needs " + cooldown + " more rounds to re-energize.");
                } else if (charge == 0){
                    GLog.i("Your cloak is out of charge.");
                }
            } else {
                stealthed = false;
                activeBuff.detach();
                activeBuff = null;
                hero.sprite.operate( hero.pos );
                GLog.i("You return from behind your cloak.");
            }

        } else {
            if (stealthed) {
                stealthed = false;
                activeBuff.detach();
                activeBuff = null;
                GLog.i("You return from behind your cloak.");
            }

            super.execute(hero, action);
        }
    }

    @Override
    public void activate(Char ch){
        super.activate(ch);
        if (stealthed){
            activeBuff = activeBuff();
            activeBuff.attachTo(ch);
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new cloakRecharge();
    }

    @Override
    protected ArtifactBuff activeBuff( ) {
        return new cloakStealth();
    }

    @Override
    public String desc() {
        //TODO: add description
        return "";
    }

    @Override
    public String status() {
        if (cooldown == 0)
            return Utils.format(TXT_CHARGE, charge, chargeCap);
        else
            return  Utils.format(TXT_CD, cooldown);
    }

    @Override
    public String toString() {
        return super.toString() + " (" + Utils.format(TXT_CHARGE, charge, chargeCap) + ")" ;
    }

    public class cloakRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            if (charge < chargeCap) {
                if (!stealthed)
                    partialCharge += (1f / (50-(level*2)));

                if (partialCharge >= 1) {
                    charge++;
                    partialCharge -= 1;
                    if (charge == chargeCap){
                        GLog.p("Your cloak is fully charged.");
                        partialCharge = 0;
                    }

                }
            } else
                partialCharge = 0;

            if (cooldown > 0)
                cooldown --;

            QuickSlot.refresh();

            spend( TICK );

            return true;
        }

    }

    public class cloakStealth extends ArtifactBuff{
        @Override
        public int icon() {
            return BuffIndicator.INVISIBLE;
        }

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {
                target.invisible++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean act(){
            charge--;
            if (charge <= 0) {
                detach();
                GLog.w("Your cloak has run out of energy.");
            }

            exp += 10 + ((Hero)target).lvl;

            //max level is 15 (20 charges)
            if (exp >= (level+1)*50 && level < 15) {
                level++;
                chargeCap++;
                exp -= level*50;
                GLog.p("Your Cloak Grows Stronger!");
            }

            QuickSlot.refresh();

            spend( TICK );

            return true;
        }

        @Override
        public String toString() {
            return "Cloaked";
        }

        @Override
        public void detach() {
            if (target.invisible > 0)
                target.invisible--;
            stealthed = false;
            cooldown = 10 - (level / 3);


            QuickSlot.refresh();
            super.detach();
        }
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put("stealthed", stealthed);
        bundle.put("cooldown", cooldown);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        stealthed = bundle.getBoolean("stealthed");
        cooldown = bundle.getInt("cooldown");
    }
}
