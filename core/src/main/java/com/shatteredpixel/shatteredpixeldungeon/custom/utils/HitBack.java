package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;

public class HitBack {
    //throw defender
    public static Ballistica hitBack(Char attacker, Char defender){
        Ballistica trajectory = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_TARGET);
        return new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
    }
    //throw attacker
    public static Ballistica bounceBack(Char attacker, Char defender){
        int opposite = attacker.pos + (attacker.pos - defender.pos);
        return new Ballistica(attacker.pos, opposite, Ballistica.MAGIC_BOLT);
    }
}
