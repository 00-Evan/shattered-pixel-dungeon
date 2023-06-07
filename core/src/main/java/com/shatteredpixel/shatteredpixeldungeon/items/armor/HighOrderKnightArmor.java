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


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HolyHealing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HolyLight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.StewedMeat;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;


public class HighOrderKnightArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_PLATE;

		armorType = ArmorType.metaled;
	}

	public HighOrderKnightArmor() {
		super( 5 );
	}

	@Override
	public int DRMin(int lvl) {
		return  2 +    //最小防御为2
				lvl;		//每升1级就加1
	}

	@Override
	public int DRMax(int lvl) {
		return  7 +    //最大防御为7
				6*lvl;		//每升1级就加6
	}
	//HolyHealing回血速度与回血量
	@Override
	public int Holyspeed(int lvl) {
		if (lvl < 5){		//如果护甲等级小于4
		return  10 +    	//
				2*lvl;		//每升1级就加2
		}else {
			return 14 + lvl;
		}
	}
	@Override
	public int Holyadd(int lvl) {
		return   3 +  //
				lvl;		//每升1级就加1
	}
	//穿上提供buff
	@Override
	public void execute(Hero hero, String action ){
		super.execute(hero ,action);
		if( action.equals( AC_EQUIP )){
				Buff.affect( hero, HolyHealing.class );
				Buff.affect( hero, HolyLight.class  );
			}
		}

}


