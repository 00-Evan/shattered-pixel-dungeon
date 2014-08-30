package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ResultDescriptions;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

import java.util.ArrayList;

/**
 * Created by debenhame on 27/08/2014.
 */
public class ChaliceOfBlood extends Artifact {
    //TODO: add polish
    //TODO: add sprite switching

    {
        name = "Chalice of Blood";
        image = ItemSpriteSheet.ARTIFACT_CHALICE;
        level = 0;
        //charge & chargecap are unused
    }

    public static final String AC_PRICK = "PRICK";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero ) && level < 8)
            actions.add(AC_PRICK);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_PRICK)){
            //TODO: add warning screen if chalice would take more than 75% current hp.

            hero.spendAndNext(1f);
            hero.damage((level*2)*(level*2), this);

            if (!hero.isAlive()) {
                Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name, Dungeon.depth));
                GLog.n("The Chalice sucks your life essence dry...");
            } else
                level++;

        }

    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new chaliceRegen();
    }

    public class chaliceRegen extends ArtifactBuff {
        public int level() {
            return level;
        }
    }

}
