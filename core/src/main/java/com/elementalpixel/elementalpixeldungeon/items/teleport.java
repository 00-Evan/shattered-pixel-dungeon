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

public class teleport extends Item {
    String AC_TELEPORT = "teleport";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_TELEPORT);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);
        Dungeon.depth = 30;
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        Game.switchScene( InterlevelScene.class);


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


