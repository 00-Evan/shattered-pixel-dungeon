package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Venom;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;

/**
 * Created by Evan on 12/04/2015.
 */
public class VenomGas extends Blob {

    @Override
    protected void evolve() {
        super.evolve();

        Char ch;
        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                if (!ch.immunities().contains(this.getClass()))
                    Buff.affect(ch, Venom.class).set(2f);
            }
        }
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
