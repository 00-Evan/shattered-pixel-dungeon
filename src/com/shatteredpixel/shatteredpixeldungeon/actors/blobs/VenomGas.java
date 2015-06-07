package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Venom;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.utils.Bundle;

/**
 * Created by Evan on 12/04/2015.
 */
public class VenomGas extends Blob {

    private int strength = 0;

    @Override
    protected void evolve() {
        super.evolve();

        if (volume == 0){
            strength = 0;
        } else {
            Char ch;
            for (int i = 0; i < LENGTH; i++) {
                if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                    if (!ch.immunities().contains(this.getClass()))
                        Buff.affect(ch, Venom.class).set(2f, strength);
                }
            }
        }
    }

    public void setStrength(int str){
        if (str > strength)
            strength = str;
    }

    private static final String STRENGTH = "strength";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        strength = bundle.getInt( STRENGTH );
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( STRENGTH, strength );
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.VENOM), 0.6f );
    }

    @Override
    public String tileDesc() {
        return "A could of foul acidic venom is swirling here.";
    }
}
