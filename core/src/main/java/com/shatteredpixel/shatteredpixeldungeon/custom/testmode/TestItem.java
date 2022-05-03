package com.shatteredpixel.shatteredpixeldungeon.custom.testmode;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;

public class TestItem extends Item {
    {
        unique = true;
        bones = false;
    }

    protected boolean changeDefAct = false;

    protected void changeDefaultAction(String action){
        if(!allowChange(action)) return;
        defaultAction = action;
    }

    protected boolean allowChange(String action){
        return !(action.equals(AC_DROP) || action.equals(AC_THROW));
    }

    @Override
    public void execute(Hero hero, String action){
        super.execute(hero, action);
        if(changeDefAct) changeDefaultAction(action);
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
