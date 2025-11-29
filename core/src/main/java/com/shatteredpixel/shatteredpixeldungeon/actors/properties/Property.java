package com.shatteredpixel.shatteredpixeldungeon.actors.properties;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.*;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;

import java.util.Arrays;
import java.util.HashSet;

public enum Property {
    BOSS(new HashSet<Class>(Arrays.asList(Grim.class, GrimTrap.class, ScrollOfRetribution.class, ScrollOfPsionicBlast.class)),
            new HashSet<Class>(Arrays.asList(AllyBuff.class, Dread.class))),
    MINIBOSS(EMPTY.VALUE,
            new HashSet<Class>(Arrays.asList(AllyBuff.class, Dread.class))),
    BOSS_MINION,
    UNDEAD,
    DEMONIC,
    INORGANIC(EMPTY.VALUE,
            new HashSet<Class>(Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class))),
    FIERY(new HashSet<Class>(Arrays.asList(WandOfFireblast.class, Elemental.FireElemental.class)),
            new HashSet<Class>(Arrays.asList(Burning.class, Blazing.class))),
    ICY(new HashSet<Class>(Arrays.asList(WandOfFrost.class, Elemental.FrostElemental.class)),
            new HashSet<Class>(Arrays.asList(Frost.class, Chill.class))),
    ACIDIC(new HashSet<Class>(Arrays.asList(Corrosion.class)),
            new HashSet<Class>(Arrays.asList(Ooze.class))),
    ELECTRIC(new HashSet<Class>(Arrays.asList(WandOfLightning.class, Shocking.class, Potential.class,
            Electricity.class, ShockingDart.class, Elemental.ShockElemental.class)),
            EMPTY.VALUE),
    LARGE,
    IMMOVABLE(EMPTY.VALUE,
            new HashSet<Class>(Arrays.asList(Vertigo.class))),
    //A character that acts in an unchanging manner. immune to AI state debuffs or stuns/slows
    STATIC(EMPTY.VALUE,
            new HashSet<Class>(Arrays.asList(AllyBuff.class, Dread.class, Terror.class, Amok.class, Charm.class, Sleep.class,
                    Paralysis.class, Frost.class, Chill.class, Slow.class, Speed.class))),
    // below are properties from HPD:
    WITHHEAD,
    ;

    private final HashSet<Class> resistances;
    private final HashSet<Class> immunities;

    private static final class EMPTY {
        private  final static HashSet<Class> VALUE =new HashSet<>();
    }


    Property() {
        this(EMPTY.VALUE, EMPTY.VALUE);
    }

    Property(HashSet<Class> resistances, HashSet<Class> immunities) {
        this.resistances = resistances;
        this.immunities = immunities;
    }

    public HashSet<Class> resistances() {
        if(resistances.isEmpty())return EMPTY.VALUE;
        else return new HashSet<>(resistances);
    }

    public HashSet<Class> immunities() {
        if(immunities.isEmpty())return EMPTY.VALUE;
        else return new HashSet<>(immunities);
    }

}
