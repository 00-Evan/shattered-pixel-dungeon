package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

/**
 * Created by Evan on 13/08/2014.
 */
public class BlandfruitBush extends Plant {

    private static final String TXT_DESC =
            "Distant cousin of the Rotberry, the pear-shaped produce of the Blandfruit bush tastes like caked dust. " +
            "The fruit is gross and unsubstantial but isn't poisonous. perhaps it could be cooked.";

    {
        image = 8;
        plantName = "Blandfruit";
    }

    @Override
    public void activate( Char ch ) {
        super.activate( ch );

        Dungeon.level.drop( new Seed(), pos ).sprite.drop();
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed extends Plant.Seed {
        {
            plantName = "Blandfruit";

            name = "seed of " + plantName;
            image = ItemSpriteSheet.SEED_BLINDWEED;

            plantClass = BlandfruitBush.class;
            alchemyClass = null;
        }

        @Override
        public String desc() {
            return TXT_DESC;
        }
    }
}
