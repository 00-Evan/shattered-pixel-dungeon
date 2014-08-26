package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by Evan on 24/08/2014.
 */
public class Artifact extends KindofMisc {

    private static final float TIME_TO_EQUIP = 1f;

    protected Buff passiveBuff;
    protected Buff activeBuff;

    protected int level = 1;
    protected int charge = 0;
    protected int chargeCap;


    public Artifact(){
        super();
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
        return actions;
    }

    @Override
    public boolean doEquip( Hero hero ) {

        if (hero.belongings.misc1 != null && hero.belongings.misc2 != null) {

            GLog.w("you can only wear 2 rings at a time");
            return false;

        } else {

            if (hero.belongings.misc1 == null) {
                hero.belongings.misc1 = this;
            } else {
                //TODO: decide if I want player to equip two of the same artifact, change logic here accordingly
                hero.belongings.misc2 = this;
            }

            detach( hero.belongings.backpack );

            activate( hero );

            hero.spendAndNext( TIME_TO_EQUIP );
            return true;

        }

    }

    public void activate( Char ch ) {
        passiveBuff = passiveBuff();
        passiveBuff.attachTo(ch);
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect ) {

        if (hero.belongings.misc1 == this) {
            hero.belongings.misc1 = null;
        } else {
            hero.belongings.misc2 = null;
        }

        hero.remove(passiveBuff);
        passiveBuff = null;

        hero.spendAndNext( TIME_TO_EQUIP );

        if (collect && !collect( hero.belongings.backpack )) {
            Dungeon.level.drop( this, hero.pos );
        }

        return true;
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.misc1 == this || hero.belongings.misc2 == this;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    protected ArtifactBuff passiveBuff() {
        return null;
    }

    protected ArtifactBuff activeBuff() {return null; }

    public class ArtifactBuff extends Buff {

    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        bundle.put( "level", level );
        bundle.put( "charge", charge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        level = bundle.getInt("level");
        charge = bundle.getInt("charge");
    }



}
