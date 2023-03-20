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

package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
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
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant.Seed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Blandfruit extends Food {

	public Potion potionAttrib = null;
	public ItemSprite.Glowing potionGlow = null;

	{
		stackable = true;
		image = ItemSpriteSheet.BLANDFRUIT;

		//only applies when blandfruit is cooked
		energy = Hunger.STARVING;

		bones = true;
	}

	@Override
	public boolean isSimilar( Item item ) {
		if ( super.isSimilar(item) ){
			Blandfruit other = (Blandfruit) item;
			if (potionAttrib == null && other.potionAttrib == null) {
					return true;
			} else if (potionAttrib != null && other.potionAttrib != null
					&& potionAttrib.isSimilar(other.potionAttrib)){
					return true;
			}
		}
		return false;
	}

	@Override
	public String defaultAction() {
		if (potionAttrib == null){
			return null;
		} else if (potionAttrib.defaultAction().equals(Potion.AC_DRINK)) {
			return AC_EAT;
		} else {
			return potionAttrib.defaultAction();
		}
	}

	@Override
	public void execute( Hero hero, String action ) {

		if (action.equals( Potion.AC_CHOOSE )){

			GameScene.show(new WndUseItem(null, this) );
			return;

		}

		if (action.equals( AC_EAT ) && potionAttrib == null) {

			GLog.w( Messages.get(this, "raw"));
			return;

		}

		super.execute(hero, action);

		if (action.equals( AC_EAT ) && potionAttrib != null){

			potionAttrib.apply(hero);

		}
	}

	@Override
	public String name() {
		if (potionAttrib instanceof PotionOfHealing)        return Messages.get(this, "sunfruit");
		if (potionAttrib instanceof PotionOfStrength)       return Messages.get(this, "rotfruit");
		if (potionAttrib instanceof PotionOfParalyticGas)   return Messages.get(this, "earthfruit");
		if (potionAttrib instanceof PotionOfInvisibility)   return Messages.get(this, "blindfruit");
		if (potionAttrib instanceof PotionOfLiquidFlame)    return Messages.get(this, "firefruit");
		if (potionAttrib instanceof PotionOfFrost)          return Messages.get(this, "icefruit");
		if (potionAttrib instanceof PotionOfMindVision)     return Messages.get(this, "fadefruit");
		if (potionAttrib instanceof PotionOfToxicGas)       return Messages.get(this, "sorrowfruit");
		if (potionAttrib instanceof PotionOfLevitation)     return Messages.get(this, "stormfruit");
		if (potionAttrib instanceof PotionOfPurity)         return Messages.get(this, "dreamfruit");
		if (potionAttrib instanceof PotionOfExperience)     return Messages.get(this, "starfruit");
		if (potionAttrib instanceof PotionOfHaste)          return Messages.get(this, "swiftfruit");
		return super.name();
	}

	@Override
	public String desc() {
		if (potionAttrib== null) {
			return super.desc();
		} else {
			String desc = Messages.get(this, "desc_cooked") + "\n\n";
			if (potionAttrib instanceof PotionOfFrost
				|| potionAttrib instanceof PotionOfLiquidFlame
				|| potionAttrib instanceof PotionOfToxicGas
				|| potionAttrib instanceof PotionOfParalyticGas) {
				desc += Messages.get(this, "desc_throw");
			} else {
				desc += Messages.get(this, "desc_eat");
			}
			return desc;
		}
	}

	@Override
	public int value() {
		return 20 * quantity;
	}

	public Item cook(Seed seed){
		return imbuePotion(Reflection.newInstance(Potion.SeedToPotion.types.get(seed.getClass())));
	}

	public Item imbuePotion(Potion potion){

		potionAttrib = potion;
		potionAttrib.anonymize();

		potionAttrib.image = ItemSpriteSheet.BLANDFRUIT;

		if (potionAttrib instanceof PotionOfHealing)        potionGlow = new ItemSprite.Glowing( 0x2EE62E );
		if (potionAttrib instanceof PotionOfStrength)       potionGlow = new ItemSprite.Glowing( 0xCC0022 );
		if (potionAttrib instanceof PotionOfParalyticGas)   potionGlow = new ItemSprite.Glowing( 0x67583D );
		if (potionAttrib instanceof PotionOfInvisibility)   potionGlow = new ItemSprite.Glowing( 0xD9D9D9 );
		if (potionAttrib instanceof PotionOfLiquidFlame)    potionGlow = new ItemSprite.Glowing( 0xFF7F00 );
		if (potionAttrib instanceof PotionOfFrost)          potionGlow = new ItemSprite.Glowing( 0x66B3FF );
		if (potionAttrib instanceof PotionOfMindVision)     potionGlow = new ItemSprite.Glowing( 0x919999 );
		if (potionAttrib instanceof PotionOfToxicGas)       potionGlow = new ItemSprite.Glowing( 0xA15CE5 );
		if (potionAttrib instanceof PotionOfLevitation)     potionGlow = new ItemSprite.Glowing( 0x1B5F79 );
		if (potionAttrib instanceof PotionOfPurity)         potionGlow = new ItemSprite.Glowing( 0xC152AA );
		if (potionAttrib instanceof PotionOfExperience)     potionGlow = new ItemSprite.Glowing( 0x404040 );
		if (potionAttrib instanceof PotionOfHaste)          potionGlow = new ItemSprite.Glowing( 0xCCBB00 );

		return this;
	}

	public static final String POTIONATTRIB = "potionattrib";
	
	@Override
	protected void onThrow(int cell) {
		if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {
			super.onThrow( cell );
			
		} else if (potionAttrib instanceof PotionOfLiquidFlame ||
				potionAttrib instanceof PotionOfToxicGas ||
				potionAttrib instanceof PotionOfParalyticGas ||
				potionAttrib instanceof PotionOfFrost ||
				potionAttrib instanceof PotionOfLevitation ||
				potionAttrib instanceof PotionOfPurity) {

			potionAttrib.shatter( cell );
			Dungeon.level.drop(new Chunks(), cell).sprite.drop();
			
		} else {
			super.onThrow( cell );
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		if (potionAttrib != null) {
			imbuePotion(potionAttrib);
		}
	}
	
	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put( POTIONATTRIB , potionAttrib);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(POTIONATTRIB)) {
			imbuePotion((Potion) bundle.get(POTIONATTRIB));
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return potionGlow;
	}
	
	public static class CookFruit extends Recipe {
		
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;
			
			if (ingredients.get(0) instanceof Blandfruit){
				if (!(ingredients.get(1) instanceof Seed)){
					return false;
				}
			} else if (ingredients.get(0) instanceof Seed){
				if (ingredients.get(1) instanceof Blandfruit){
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}
			
			Blandfruit fruit = (Blandfruit) ingredients.get(0);
			Seed seed = (Seed) ingredients.get(1);
			
			if (fruit.quantity() >= 1 && fruit.potionAttrib == null
				&& seed.quantity() >= 1){

				return true;
			}
			
			return false;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 2;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			ingredients.get(0).quantity(ingredients.get(0).quantity() - 1);
			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);
			
			
			return new Blandfruit().cook((Seed) ingredients.get(1));
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			return new Blandfruit().cook((Seed) ingredients.get(1));
		}
	}

	public static class Chunks extends Food {

		{
			stackable = true;
			image = ItemSpriteSheet.BLAND_CHUNKS;

			energy = Hunger.STARVING;

			bones = true;
		}

	}

}
