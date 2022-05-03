package com.shatteredpixel.shatteredpixeldungeon.tiles.butters;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

public abstract class Butter implements Bundlable {

    public int image;
    public int pos;

    public String name = Messages.get(PotionOfLiquidFlame.class, "buttername");
    public String desc = Messages.get(PotionOfLiquidFlame.class, "butterdesc");

    public void melt(){
        Dungeon.level.butter.remove(pos);
        GameScene.updateMap(pos);
    }

    public Butter set(int pos){
        this.pos = pos;
        return this;
    }

    public abstract void stepOnEffect(Char ch);

    private static final String POS	= "pos";

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        pos = bundle.getInt( POS );
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        bundle.put( POS, pos );
    }
}
