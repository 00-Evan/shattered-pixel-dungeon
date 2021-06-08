package com.elementalpixel.elementalpixeldungeon.items;

import com.elementalpixel.elementalpixeldungeon.Badges;
import com.elementalpixel.elementalpixeldungeon.Challenges;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.GamesInProgress;
import com.elementalpixel.elementalpixeldungeon.ShatteredPixelDungeon;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.actors.hero.HeroAction;
import com.elementalpixel.elementalpixeldungeon.levels.Level;
import com.elementalpixel.elementalpixeldungeon.scenes.AmuletScene;
import com.elementalpixel.elementalpixeldungeon.scenes.ElementalOrbScene;
import com.elementalpixel.elementalpixeldungeon.scenes.InterlevelScene;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.ArrayList;

public class ElementalOrb extends Item {
    String AC_MOVE = "move";

    {
        image = ItemSpriteSheet.DEWDROP;
        unique = true;
        stackable = false;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_MOVE);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_MOVE)) {
            showElementalOrbScene( true );
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

    private void showElementalOrbScene( boolean showText ) {
        try {
            Dungeon.saveAll();
            ElementalOrbScene.noText = !showText;
            Game.switchScene( ElementalOrbScene.class, new Game.SceneChangeCallback() {
                @Override
                public void beforeCreate() {

                }

                @Override
                public void afterCreate() {
                }
            });
        } catch (IOException e) {
            ShatteredPixelDungeon.reportException(e);
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


