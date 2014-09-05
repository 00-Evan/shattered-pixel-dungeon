package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

/**
 * Created by debenhame on 03/09/2014.
 */
public class MasterThievesArmband extends Artifact {

    {
        name = "Master Thieves' Armband";
        image = ItemSpriteSheet.ARTIFACT_ARMBAND;
        level = 0;
        levelCap = 10;
        charge = 0;
        //partialcharge and chargeCap are unused
    }

    private int exp = 0;

    @Override
    public String status() {
        if (charge > 0)
            return Utils.format("%d", charge);
        else
            return null;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Thievery();
    }

    @Override
    public String desc() {
        //TODO: add description
        return "";
    }

    public class Thievery extends ArtifactBuff{
        public void collect(int gold){
            charge += gold/2;
        }

        public boolean steal(Item item){
            if (item.price() <= charge){
                charge -= item.price();
                exp += item.price();
            } else {
                float chance = stealChance(item.price());
                if (Random.Float() > chance)
                    return false;
                else {
                    if (chance <= 1)
                        charge = 0;
                    else
                        //removes the charge it took you to reach 100%
                        charge -= charge/chance;
                    exp += item.price();
                }
            }
            while(exp >= 1000 && level < levelCap) {
                exp -= 1000;
                level++;
            }
            return true;
        }

        public float stealChance(int value){
                //get lvl*100 gold or lvl*5% item value of free charge, whichever is less.
                int chargeBonus = Math.min(level*100, (value*level)/20);

                return (((float)charge + chargeBonus)/value);
        }
    }
}
