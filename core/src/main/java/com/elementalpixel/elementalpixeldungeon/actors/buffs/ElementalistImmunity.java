
package com.elementalpixel.elementalpixeldungeon.actors.buffs;


import com.elementalpixel.elementalpixeldungeon.actors.blobs.ConfusionGas;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;

public class ElementalistImmunity extends FlavourBuff {

    {
        type = buffType.POSITIVE;
    }

    public static final float DURATION = 99999999999999999999999999999999999999f;

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    {
        //all harmful blobs
        immunities.add( ConfusionGas.class );
        immunities.add( Paralysis.class );
        immunities.add( Vertigo.class );
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}
