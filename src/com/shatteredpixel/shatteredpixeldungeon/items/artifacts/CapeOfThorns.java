package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.utils.Random;

/**
 * Created by debenhame on 03/09/2014.
 */
public class CapeOfThorns extends Artifact {

    {
        name = "Cape of Thorns";
        image = ItemSpriteSheet.ARTIFACT_CAPE;
        level = 0;
        levelCap = 10;
        charge = 0;
        chargeCap = 100;
        defaultAction = "NONE";
        //partialcharge is unused
    }

    private int timer = 0;

    @Override
    public String status() {
        if (timer == 0)
            return Utils.format("%d%%", charge);
        else
            return Utils.format("%d", timer);
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Thorns();
    }

    @Override
    public String desc() {
        String desc = "These collapsed sheets of metal from the DM-300 have formed together into a rigid wearable " +
                "cape. The metal is old and coated in thick flakes of rust. It seems to store a deep energy, " +
                "perhaps it has some of the DM-300's power?";
        if (isEquipped( Dungeon.hero )) {
            desc += "\n\n";
            if (timer == 0)
                desc += "The cape feels reassuringly heavy on your shoulders. You're not sure if it will directly " +
                        "help you in a fight, but it seems to be gaining energy from the battles you are in.";
            else
                desc += "The cape seems to be releasing some stored energy, it is radiating power at all angles. " +
                        "You feel very confident that the cape can protect you right now.";
        }

        return desc;
    }

    @Override
    public String toString(){
        if (level > 0)
            return Utils.format("%s+%d %d%%", name, level, charge);
        else
            return Utils.format("%s %d%%", name, charge);
    }

    public class Thorns extends ArtifactBuff{

        @Override
        public boolean act(){
            if (timer > 0) {
                timer--;
                if (timer == 0) {
                    BuffIndicator.refreshHero();
                    GLog.w("Your Cape becomes inert again.");
                }
                QuickSlot.refresh();
            }
            spend(TICK);
            return true;
        }

        public int proc(int damage, Char attacker){
            if (timer == 0){
                charge += damage*(0.5+level*0.025);
                if (charge >= chargeCap){
                    charge = 0;
                    timer = 5+level;
                    GLog.p("Your Cape begins radiating energy, you feel protected!");
                    BuffIndicator.refreshHero();
                }
            }

            if (timer != 0){
                int deflected = Random.NormalIntRange((int)(damage*0.33), damage);
                damage -= deflected;

                attacker.damage(deflected, this);

                exp+= deflected;

                if (exp >= (level+1)*10 && level < levelCap){
                    exp -= (level+1)*10;
                    upgrade();
                    GLog.p("Your Cape grows stronger!");
                }

            }
            QuickSlot.refresh();
            return damage;
        }

        @Override
        public String toString() {
                return "Thorns";
        }

        @Override
        public int icon() {
            if (timer == 0)
                return BuffIndicator.NONE;
            else
                return BuffIndicator.THORNS;
        }

        @Override
        public void detach(){
            timer = 0;
            charge = 0;
            super.detach();
        }

    }


}
