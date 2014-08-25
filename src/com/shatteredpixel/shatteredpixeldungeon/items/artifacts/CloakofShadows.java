package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

/**
 * Created by debenhame on 25/08/2014.
 */
public class CloakofShadows extends Artifact {
    //TODO: add requirements for entering stealth, add levelling mechanic, add bundle support, add polish

    {
        name = "Cloak of Shadows";
        image = ItemSpriteSheet.ARTIFACT_CLOAK;
    }

    private boolean stealthed = false;

    public static final String AC_STEALTH = "STEALTH";

    private static final String TXT_STATUS	= "%d/%d";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && charge > 0)
            actions.add(AC_STEALTH);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_STEALTH )) {

            if (!stealthed){
                stealthed = true;
                Sample.INSTANCE.play( Assets.SND_MELD );
                activeBuff = activeBuff();
                activeBuff.attachTo(hero);
            } else {
                stealthed = false;
                hero.remove(activeBuff);
                activeBuff = null;
            }

        } else {

            super.execute(hero, action);

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
        return "Need to add a description.";
    }

    @Override
    public String status() {
        return Utils.format(TXT_STATUS, charge, level+4);
    }

    @Override
    public String toString() {
        return super.toString() + " (" + status() +  ")" ;
    }

    public class cloakRecharge extends ArtifactBuff{
        int partialCharge = 0;
        @Override
        public boolean act() {
            if (charge < level+4) {
                if (!stealthed)
                    partialCharge += (level + 4) / 300;

                if (partialCharge >= 100) {
                    charge++;
                    partialCharge -= 100;
                }
            } else
                partialCharge = 0;

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
            if (charge <= 0)
                detach();

            spend( TICK );

            return true;
        }

        @Override
        public String toString() {
            return "Cloaked";
        }

        @Override
        public void detach() {
            target.invisible--;
            stealthed = false;
            super.detach();
        }
    }
}
