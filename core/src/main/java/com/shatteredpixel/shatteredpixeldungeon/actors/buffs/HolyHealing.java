package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;



import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HighOrderKnightArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PapalKnightArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PastorClothe;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class HolyHealing extends Buff {

    {
        type = buffType.POSITIVE;
    }

    public static int level = 1;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            if(Dungeon.hero.buff(HolyHealing.class) != null ){
                int healAmt;
                if( hero.belongings.armor() != null & !hero.isStarving() ){
                    healAmt = Math.min( hero.HT, 1 + hero.belongings.armor().Holyadd() );
                }else{
                    healAmt = Math.min( hero.HT, 1);
                }

                healAmt = Math.min( healAmt, hero.HT - hero.HP);
                hero.HP += healAmt;

                if( hero.belongings.armor() != null ){
                    spend( 30f - hero.belongings.armor().Holyspeed() );
                }else{
                    spend( 999f );
                }


               // GLog.w("每10回合+1血量，我是说真的（");
            }

            spend(interval);
            if (level <= 0 || ( !( hero.belongings.armor instanceof PastorClothe)
                    && !( hero.belongings.armor instanceof PapalKnightArmor)
                    && !( hero.belongings.armor instanceof HighOrderKnightArmor)
                    && !( hero.belongings.armor instanceof ClassArmor)      ) ) {

                detach();
            }

        }

        return true;
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        //decide whether to override, preferring high value + low interval
        if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

  //  @Override
   // public float iconFadePercent() {
       // if (target instanceof Hero){
        //    float max = ((Hero) target).lvl;
        //    return Math.max(0, (max-level)/max);}
   //     return 0;
  //  }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight( 0xffff0080);
    }

    @Override
    public int icon() {
        return BuffIndicator.NULL_ARMOR;
    }


}



