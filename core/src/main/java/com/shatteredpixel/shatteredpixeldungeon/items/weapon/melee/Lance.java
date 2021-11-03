/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.ArcaneCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Lance extends MeleeWeapon {

    {
        image = ItemSpriteSheet.LANCE;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.2f;

        tier = 6;
        RCH = 2;
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier-1, lvl); //18 base strength req
    }

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //28 base
                lvl*(tier);     //scaling +6 per +1
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Glaive.class, ArcaneCatalyst.class, Spear.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 30;

            output = Lance.class;
            outQuantity = 1;
        }

    }

}
