package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;

import java.util.ArrayList;

public class ThrownWeapon extends MissileWeapon{
    {
        stackable = true;
        levelKnown = true;

        bones = true;

        defaultAction = AC_THROW;
        usesTargeting = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if( !actions.contains( AC_UNEQUIP ) ) actions.add( AC_EQUIP );
        return actions;
    }
}
