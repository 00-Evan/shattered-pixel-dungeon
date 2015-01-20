package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by debenhame on 25/08/2014.
 */
public class CloakOfShadows extends Artifact {

    {
        name = "Cloak of Shadows";
        image = ItemSpriteSheet.ARTIFACT_CLOAK;

        level = 0;
        exp = 0;
        levelCap = 15;

        charge = level+5;
        partialCharge = 0;
        chargeCap = level+5;

        cooldown = 0;

        defaultAction = AC_STEALTH;

        bones = false;
    }

    private boolean stealthed = false;

    public static final String AC_STEALTH = "STEALTH";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge > 1)
            actions.add(AC_STEALTH);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_STEALTH )) {

            if (!stealthed){
                if (!isEquipped(hero)) GLog.i("You need to equip your cloak to do that.");
                else if (cooldown > 0) GLog.i("Your cloak needs " + cooldown + " more rounds to re-energize.");
                else if (charge <= 1)  GLog.i("Your cloak hasn't recharged enough to be usable yet.");
                else {
                    stealthed = true;
                    hero.spend( 1f );
                    hero.busy();
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
                }
            } else {
                stealthed = false;
                activeBuff.detach();
                activeBuff = null;
                hero.sprite.operate( hero.pos );
                GLog.i("You return from underneath your cloak.");
            }

        } else
            super.execute(hero, action);
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
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            stealthed = false;
            return true;
        } else
            return false;
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
    public Item upgrade() {
        chargeCap++;
        return super.upgrade();
    }

    @Override
    public String desc() {
        String desc = "This light silken cloak shimmers in and out of your vision as it sways in the air. When worn, " +
                "it can be used to hide your presence for a short time.\n\n";

        if (level < 5)
         desc += "The cloak's magic has faded and it is not very powerful, perhaps it will regain strength through use.";
        else if (level < 10)
            desc += "The cloak's power has begun to return.";
        else if (level < 15)
            desc += "The cloak has almost returned to full strength.";
        else
            desc += "The cloak is at full potential and will work for extended durations.";


        if ( isEquipped (Dungeon.hero) )
            desc += "\n\nThe cloak rests around your shoulders.";


        return desc;
    }

    //Note: cloak needs to bundle chargecap as it is dynamic.
    private static final String CHARGECAP = "chargecap";
    private static final String STEALTHED = "stealthed";
    private static final String COOLDOWN = "cooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGECAP, chargeCap );
        bundle.put( STEALTHED, stealthed );
        bundle.put( COOLDOWN, cooldown );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        chargeCap = bundle.getInt( CHARGECAP );
        stealthed = bundle.getBoolean( STEALTHED );
        cooldown = bundle.getInt( COOLDOWN );
    }

    public class cloakRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            if (charge < chargeCap) {
                if (!stealthed)
                    partialCharge += (1f / (60 - (chargeCap-charge)*2));

                if (partialCharge >= 1) {
                    charge++;
                    partialCharge -= 1;
                    if (charge == chargeCap){
                        partialCharge = 0;
                    }

                }
            } else
                partialCharge = 0;

            if (cooldown > 0)
                cooldown --;

            QuickSlotButton.refresh();

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
                ((Hero)target).interrupt();
            }

            exp += 10 + ((Hero)target).lvl;

            if (exp >= (level+1)*50 && level < levelCap) {
                upgrade();
                exp -= level*50;
                GLog.p("Your Cloak Grows Stronger!");
            }

            QuickSlotButton.refresh();

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


            QuickSlotButton.refresh();
            super.detach();
        }
    }
}
