package com.shatteredpixel.shatteredpixeldungeon.actors;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.watabou.utils.Random;

public class Boss extends Mob {

    protected static float baseMin;
    protected static float baseMax;
    protected static float baseAcc;
    protected static float baseEva;
    protected static float baseHT;
    protected static float baseMinDef;
    protected static float baseMaxDef;

    protected void initProperty() {
        properties.add(Property.BOSS);
        immunities.add(Grim.class);
        immunities.add(ScrollOfPsionicBlast.class);
        immunities.add(ScrollOfRetribution.class);
    }

    protected void initBaseStatus(float min, float max, float acc, float eva, float ht, float mid, float mad) {
        baseMin = min;
        baseMax = max;
        baseAcc = acc;
        baseEva = eva;
        baseHT = ht;
        baseMinDef = mid;
        baseMaxDef = mad;
    }

    protected void initStatus(int exp) {
        defenseSkill = Math.round(baseEva);
        EXP = exp;
        HP = HT = Math.round(baseHT);
    }

    @Override
    public int damageRoll() {
        return Math.round(Random.NormalFloat( baseMin, baseMax ));
    }

    @Override
    public int attackSkill( Char target ) {
        return Math.round(baseAcc);
    }

    @Override
    public int drRoll() {
        return Math.round(Random.NormalFloat(baseMinDef, baseMaxDef));
    }
}

