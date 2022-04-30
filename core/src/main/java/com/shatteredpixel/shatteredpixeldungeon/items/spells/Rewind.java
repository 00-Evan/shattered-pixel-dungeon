package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

public class Rewind extends Spell{
    {
        image = ItemSpriteSheet.REWIND;
    }

    @Override
    protected void onCast(Hero hero) {
        TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
        if (timeFreeze != null) timeFreeze.disarmPressedTraps();
        Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
        if (timeBubble != null) timeBubble.disarmPressedTraps();

        InterlevelScene.mode = InterlevelScene.Mode.RETURN;//RETURN/DESCEND
        InterlevelScene.returnDepth = Dungeon.depth;
        InterlevelScene.returnPos = -1;
        Game.switchScene( InterlevelScene.class );

        Buff.affect(hero, Healing.class).setHeal((int) (hero.HT), 1f, 0);
        Buff.detach( hero, Poison.class );
        Buff.detach( hero, Cripple.class );
        Buff.detach( hero, Weakness.class );
        Buff.detach( hero, Vulnerable.class );
        Buff.detach( hero, Bleeding.class );
        Buff.detach( hero, Blindness.class );
        Buff.detach( hero, Drowsy.class );
        Buff.detach( hero, Slow.class );
        Buff.detach( hero, Vertigo.class);
        GLog.p( Messages.get(this, "rewind") );
        curItem.detach(Dungeon.hero.belongings.backpack);
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return Math.round(quantity * (100 / 2f));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScrollOfTeleportation.class, PotionOfHealing.class};
            inQuantity = new int[]{1, 1};

            cost = 7;

            output = Rewind.class;
            outQuantity = 1;
        }

    }
}
