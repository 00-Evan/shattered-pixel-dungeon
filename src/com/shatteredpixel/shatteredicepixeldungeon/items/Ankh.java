/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.shatteredicepixeldungeon.items;

import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite.Glowing;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Ankh extends Item {

    public static final String AC_BLESS = "BLESS";

    public static final String TXT_DESC_NOBLESS = "Upon resurrection all non-equipped items are lost. " +
                                                "Using a full dew vial, the ankh can be blessed with extra strength.";
    public static final String TXT_DESC_BLESSED = "The ankh has been blessed and is now much stronger. " +
                                              "The Ankh will sacrifice itself to save you in a moment of deadly peril.";

    public static final String TXT_BLESS = "You bless the ankh with clean water.";
    public static final String TXT_REVIVE = "The ankh explodes with life-giving energy!";



    {
		name = "Ankh";
		image = ItemSpriteSheet.ANKH;

        //You tell the ankh no, don't revive me, and then it comes back to revive you again in another run.
        //I'm not sure if that's enthusiasm or passive-aggression.
        bones = true;
    }

    private Boolean blessed = false;
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        DewVial vial = hero.belongings.getItem(DewVial.class);
        if (vial != null && vial.isFull() && !blessed)
            actions.add( AC_BLESS );
        return actions;
    }

    @Override
    public void execute( final Hero hero, String action ) {
        if (action.equals( AC_BLESS )) {

            DewVial vial = hero.belongings.getItem(DewVial.class);
            if (vial != null){
                blessed = true;
                vial.empty();
                GLog.p( TXT_BLESS );
                hero.spend( 1f );
                hero.busy();


                Sample.INSTANCE.play( Assets.SND_DRINK );
                CellEmitter.get(hero.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                hero.sprite.operate( hero.pos );
            }
        } else {

            super.execute( hero, action );

        }

    }
	
	@Override
	public String info() {
		if (blessed)
            return
            "This ancient symbol of immortality grants the ability to return to life after death. " +
            TXT_DESC_BLESSED;
        else
            return
			"This ancient symbol of immortality grants the ability to return to life after death. " +
            TXT_DESC_NOBLESS;
	}

    public Boolean isBlessed(){
        return blessed;
    }

    private static final Glowing WHITE = new Glowing( 0xFFFFCC );

    @Override
    public Glowing glowing() {
        return isBlessed() ? WHITE : null;
    }

    private static final String BLESSED = "blessed";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( BLESSED, blessed );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        blessed	= bundle.getBoolean( BLESSED );
    }
	
	@Override
	public int price() {
		return 50 * quantity;
	}
}
