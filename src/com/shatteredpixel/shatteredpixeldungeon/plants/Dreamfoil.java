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
package com.shatteredpixel.shatteredpixeldungeon.plants;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Dreamfoil extends Plant {

    private static final String TXT_DESC =
            "The Dreamfoil's prickly flowers contain a toxin " +
            "which renders most weaker beings unconscious.";

    {
        image = 10;
        plantName = "Dreamfoil";
    }

    @Override
    public void activate( Char ch ) {
        super.activate( ch );

        if (ch != null) {
            if (ch instanceof Mob)
                Buff.affect(ch, MagicalSleep.class);
        }
    }

    @Override
    public String desc() {
        return TXT_DESC;
    }

    public static class Seed extends Plant.Seed {
        {
            plantName = "Dreamfoil";

            name = "seed of " + plantName;
            image = ItemSpriteSheet.SEED_DREAMFOIL;

            plantClass = Dreamfoil.class;
            alchemyClass = PotionOfPurity.class;
        }

        @Override
        public String desc() {
            return TXT_DESC;
        }
    }
}