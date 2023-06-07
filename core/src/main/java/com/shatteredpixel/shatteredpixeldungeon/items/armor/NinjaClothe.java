/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.armor;

import com.shatteredpixel.shatteredpixeldungeon.items.sundry.ClothStrip;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.BoneSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;


public class NinjaClothe extends Armor {

	{
		image = ItemSpriteSheet.NINJACLOTHE;

		armorType = ArmorType.clothed;
	}

	public NinjaClothe() {
		super( 2 );
	}


	@Override
	public int DRMin(int lvl) {
		return  0 +    //最小防御为0
				lvl;		//每升1级就加1
	}

	@Override
	public int DRMax(int lvl) {
		return  3 +    //最大防御为3
				Math.round(lvl*1.5f);		//每升1级就加1.5

	}

	@Override
	public int  ArmorEvasion( int lvl ){
		return 5 +
				lvl;
	}
	//额外五点闪避（DefenseSkill）


	//合成配方
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{ClothStrip.class, BoneSpike.class, Shuriken.class};
			inQuantity = new int[]{2, 1, 1};

			cost = 6;

			output = NinjaClothe.class;
			outQuantity = 1;
		}
	}


}
