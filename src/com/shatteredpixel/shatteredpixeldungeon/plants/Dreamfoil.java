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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Dreamfoil extends Plant {

    private static final String TXT_DESC =
            "The Dreamfoil's prickly flowers contain a chemical which is known for its " +
            "properties as a strong neutralizing agent. Most weaker creatures are overwhelmed " +
            "and knocked unconscious, which gives the plant its namesake.";

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
            else if (ch instanceof Hero){
                GLog.i( "You feel refreshed." );
                Buff.detach( ch, Poison.class );
                Buff.detach( ch, Cripple.class );
                Buff.detach( ch, Weakness.class );
                Buff.detach( ch, Bleeding.class );
                Buff.detach( ch, Drowsy.class );
                Buff.detach( ch, Slow.class );
                Buff.detach( ch, Vertigo.class);
           }
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