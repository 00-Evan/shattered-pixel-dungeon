/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public class ExoticPotion extends Potion {
	
	{
		//sprite = equivalent potion sprite but one row down
	}
	
	public static final HashMap<Class<?extends Potion>, Class<?extends ExoticPotion>> regToExo = new HashMap<>();
	public static final HashMap<Class<?extends ExoticPotion>, Class<?extends Potion>> exoToReg = new HashMap<>();
	static{
		regToExo.put(PotionOfHealing.class, PotionOfShielding.class);
		exoToReg.put(PotionOfShielding.class, PotionOfHealing.class);
		
		regToExo.put(PotionOfToxicGas.class, PotionOfCorrosiveGas.class);
		exoToReg.put(PotionOfCorrosiveGas.class, PotionOfToxicGas.class);
		
		regToExo.put(PotionOfStrength.class, PotionOfMastery.class);
		exoToReg.put(PotionOfMastery.class, PotionOfStrength.class);
		
		regToExo.put(PotionOfFrost.class, PotionOfSnapFreeze.class);
		exoToReg.put(PotionOfSnapFreeze.class, PotionOfFrost.class);
		
		regToExo.put(PotionOfHaste.class, PotionOfStamina.class);
		exoToReg.put(PotionOfStamina.class, PotionOfHaste.class);
		
		regToExo.put(PotionOfLiquidFlame.class, PotionOfDragonsBreath.class);
		exoToReg.put(PotionOfDragonsBreath.class, PotionOfLiquidFlame.class);
		
		regToExo.put(PotionOfInvisibility.class, PotionOfShroudingFog.class);
		exoToReg.put(PotionOfShroudingFog.class, PotionOfInvisibility.class);
		
		regToExo.put(PotionOfMindVision.class, PotionOfMagicalSight.class);
		exoToReg.put(PotionOfMagicalSight.class, PotionOfMindVision.class);
		
		regToExo.put(PotionOfLevitation.class, PotionOfStormClouds.class);
		exoToReg.put(PotionOfStormClouds.class, PotionOfLevitation.class);
		
		regToExo.put(PotionOfExperience.class, PotionOfDivineInspiration.class);
		exoToReg.put(PotionOfDivineInspiration.class, PotionOfExperience.class);
		
		regToExo.put(PotionOfPurity.class, PotionOfCleansing.class);
		exoToReg.put(PotionOfCleansing.class, PotionOfPurity.class);
		
		regToExo.put(PotionOfParalyticGas.class, PotionOfEarthenArmor.class);
		exoToReg.put(PotionOfEarthenArmor.class, PotionOfParalyticGas.class);
	}
	
	@Override
	public boolean isKnown() {
		return anonymous || (handler != null && handler.isKnown( exoToReg.get(this.getClass()) ));
	}
	
	@Override
	public void setKnown() {
		if (!isKnown()) {
			handler.know(exoToReg.get(this.getClass()));
			updateQuickslot();
			Potion p = Dungeon.hero.belongings.getItem(getClass());
			if (p != null)  p.setAction();
			p = Dungeon.hero.belongings.getItem(exoToReg.get(this.getClass()));
			if (p != null)  p.setAction();
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		if (handler != null && handler.contains(exoToReg.get(this.getClass()))) {
			image = handler.image(exoToReg.get(this.getClass())) + 16;
			color = handler.label(exoToReg.get(this.getClass()));
		}
	}
	
	@Override
	//20 gold more than its none-exotic equivalent
	public int value() {
		return (Reflection.newInstance(exoToReg.get(getClass())).value() + 20) * quantity;
	}

	@Override
	//4 more energy than its none-exotic equivalent
	public int energyVal() {
		return (Reflection.newInstance(exoToReg.get(getClass())).energyVal() + 4) * quantity;
	}

	public static class PotionToExotic extends Recipe{

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() == 1 && regToExo.containsKey(ingredients.get(0).getClass())){
				return true;
			}

			return false;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 4;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			for (Item i : ingredients){
				i.quantity(i.quantity()-1);
			}

			return Reflection.newInstance(regToExo.get(ingredients.get(0).getClass()));
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return Reflection.newInstance(regToExo.get(ingredients.get(0).getClass()));
		}
	}
}
