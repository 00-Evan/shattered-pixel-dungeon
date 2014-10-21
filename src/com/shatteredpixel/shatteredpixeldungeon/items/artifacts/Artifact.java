package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

/**
 * Created by Evan on 24/08/2014.
 */
public class Artifact extends KindofMisc {
//TODO: add artifact transform method and tie it into well of transformation, scheduled for 0.2.2
    {
        levelKnown = true;
    }

    private static final float TIME_TO_EQUIP = 1f;

    private static final String TXT_TO_STRING		        = "%s";
    private static final String TXT_TO_STRING_CHARGE		= "%s (%d/%d)";
    private static final String TXT_TO_STRING_LVL	        = "%s%+d";
    private static final String TXT_TO_STRING_LVL_CHARGE	= "%s%+d (%d/%d)";

    protected Buff passiveBuff;
    protected Buff activeBuff;

    //level is used internally to track upgrades to artifacts, size/logic varies per artifact.
    //already inherited from item superclass
    //exp is used to count progress towards levels for some artifacts
    protected int exp = 0;
    //levelCap is the artifact's maximum level
    protected int levelCap = 0;

    //the current artifact charge
    protected int charge = 0;
    //the build towards next charge, usually rolls over at 1.
    //better to keep charge as an int and use a separate float than casting.
    protected float partialCharge = 0;
    //the maximum charge, varies per artifact, not all artifacts use this.
    protected int chargeCap = 0;



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

            GLog.w("you can only wear 2 misc items at a time");
            return false;

        } else if ((hero.belongings.misc1 != null && hero.belongings.misc1.getClass() == this.getClass())
                || (hero.belongings.misc2 != null && hero.belongings.misc2.getClass() == this.getClass())){

            GLog.w("you cannot wear two of the same artifact");
            return false;

        } else {

            if (hero.belongings.misc1 == null) {
                hero.belongings.misc1 = this;
            } else {
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
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

        if (hero.belongings.misc1 == this) {
            hero.belongings.misc1 = null;
        } else {
            hero.belongings.misc2 = null;
        }

        passiveBuff.detach();
        passiveBuff = null;

        return true;

        } else {

            return false;

        }
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
    public int visiblyUpgraded() {
        return ((level*10)/levelCap);
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public String toString() {

        if (levelKnown && level != 0) {
            if (chargeCap > 0) {
                return Utils.format( TXT_TO_STRING_LVL_CHARGE, name(), visiblyUpgraded(), charge, chargeCap );
            } else {
                return Utils.format( TXT_TO_STRING_LVL, name(), visiblyUpgraded() );
            }
        } else {
            if (chargeCap > 0) {
                return Utils.format( TXT_TO_STRING_CHARGE, name(), charge, chargeCap );
            } else {
                return Utils.format( TXT_TO_STRING, name() );
            }
        }
    }

    protected ArtifactBuff passiveBuff() {
        return null;
    }

    protected ArtifactBuff activeBuff() {return null; }

    public class ArtifactBuff extends Buff {

    }

    private static final String IMAGE = "image";
    private static final String EXP = "exp";
    private static final String CHARGE = "charge";
    private static final String PARTIALCHARGE = "partialcharge";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( IMAGE, image );
        bundle.put( EXP , exp );
        bundle.put( CHARGE , charge );
        bundle.put( PARTIALCHARGE , partialCharge );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        image = bundle.getInt( IMAGE );
        exp = bundle.getInt( EXP );
        charge = bundle.getInt( CHARGE );
        partialCharge = bundle.getFloat( PARTIALCHARGE );
    }

    @Override
    public int price() {
        int price = 200;
        if (level > 0)
            price += 30*((level*10)/levelCap);
        if (cursed && cursedKnown) {
            price /= 2;
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }
}
