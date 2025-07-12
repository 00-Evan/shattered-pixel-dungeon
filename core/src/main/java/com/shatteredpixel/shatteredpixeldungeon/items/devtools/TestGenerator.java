package com.shatteredpixel.shatteredpixeldungeon.items.devtools;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.devtools.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;
public class TestGenerator  extends TestItem {
    {
        bones = false;
        defaultAction = AC_GIVE;
        unique = true;
        image = ItemSpriteSheet.SCROLL_GYFU;
    }
    protected static final String AC_GIVE = "GIVE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_GIVE );
        return actions;
    }
    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
    }
    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}