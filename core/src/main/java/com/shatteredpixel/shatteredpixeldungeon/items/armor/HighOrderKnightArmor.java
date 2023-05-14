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
		return  2 +    //最小防御为0
				lvl;		//每升1级就加1
	}

	@Override
	public int DRMax(int lvl) {
		return  7 +    //最大防御为3
				7*lvl;		//每升1级就加1
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
		return   5 +  //
				lvl;		//每升1级就加1
	}


	//合成配方
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients){

			boolean pasty = false;
			boolean ration = false;


			for (Item ingredient : ingredients){
				if (ingredient.quantity() > 0) {
					if (ingredient instanceof Pasty) {
						pasty = true;
					} else if (ingredient.getClass() == Food.class) {
						ration = true;
					}
				}
			}
			return pasty && ration;
		}
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 6;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item ingredient : ingredients){
				ingredient.quantity(ingredient.quantity() - 1);
			}

			return sampleOutput(null);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new HighOrderKnightArmor();
		}
	}


	//穿上提供buff
	@Override
	public void execute(Hero hero, String action ){
		super.execute(hero ,action);
		switch (action){
			case AC_EQUIP:
				Buff.affect( hero, HolyHealing.class );
				Buff.affect( hero, HolyLight.class );
				break;
			case AC_UNEQUIP:
				doUnequip( hero,true );
				Buff.detach(hero, HolyHealing.class);
				Buff.detach(hero, HolyLight.class);
				break;
			case AC_THROW:
				super.doThrow( hero );
				Buff.detach(hero, HolyHealing.class);
				Buff.detach(hero, HolyLight.class);
				break;

		}
		if( hero.act( ) == doUnequip( hero,true )){
			Buff.detach(hero, HolyHealing.class);
			Buff.detach(hero, HolyLight.class);
		}
	}


}
