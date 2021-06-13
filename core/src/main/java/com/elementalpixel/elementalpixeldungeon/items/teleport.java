package com.elementalpixel.elementalpixeldungeon.items;

import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.GamesInProgress;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroAction;
import com.elementalpixel.elementalpixeldungeon.levels.Level;
import com.elementalpixel.elementalpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.ArrayList;


/*Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful
Do not look at this file, it's awful*/

public class teleport extends Item {
    String AC_TELEPORT = "teleport";
    String AC_TELEPORT2 = "teleport2";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_TELEPORT);
        actions.add(AC_TELEPORT2);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        if (action.equals(AC_TELEPORT)) {
            Dungeon.depth = 25;
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene( InterlevelScene.class);
        } else if (action.equals(AC_TELEPORT2)) {
            //Dungeon.depth = 14;
            //Dungeon.depth++;
            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
            Game.switchScene( InterlevelScene.class);
        }
    }

    @Override
    public boolean doPickUp( Hero hero ) {
        if (super.doPickUp( hero )) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

}


