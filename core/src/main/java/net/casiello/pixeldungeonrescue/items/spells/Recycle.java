/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package net.casiello.pixeldungeonrescue.items.spells;

import net.casiello.pixeldungeonrescue.Challenges;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.items.Generator;
import net.casiello.pixeldungeonrescue.items.Item;
import net.casiello.pixeldungeonrescue.items.potions.Potion;
import net.casiello.pixeldungeonrescue.items.potions.brews.Brew;
import net.casiello.pixeldungeonrescue.items.potions.elixirs.Elixir;
import net.casiello.pixeldungeonrescue.items.potions.exotic.ExoticPotion;
import net.casiello.pixeldungeonrescue.items.scrolls.Scroll;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTransmutation;
import net.casiello.pixeldungeonrescue.items.scrolls.exotic.ExoticScroll;
import net.casiello.pixeldungeonrescue.items.stones.Runestone;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.plants.Plant;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.GLog;
import net.casiello.pixeldungeonrescue.windows.WndBag;
import com.watabou.utils.Reflection;

public class Recycle extends InventorySpell {
	
	{
		image = ItemSpriteSheet.RECYCLE;
		mode = WndBag.Mode.RECYCLABLE;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		Item result;
		do {
			if (item instanceof Potion) {
				result = Generator.random(Generator.Category.POTION);
				if (item instanceof ExoticPotion){
					result = Reflection.newInstance(ExoticPotion.regToExo.get(result.getClass()));
				}
			} else if (item instanceof Scroll) {
				result = Generator.random(Generator.Category.SCROLL);
				if (item instanceof ExoticScroll){
					result = Reflection.newInstance(ExoticScroll.regToExo.get(result.getClass()));
				}
			} else if (item instanceof Plant.Seed) {
				result = Generator.random(Generator.Category.SEED);
			} else {
				result = Generator.random(Generator.Category.STONE);
			}
		} while (result.getClass() == item.getClass() || Challenges.isItemBlocked(result));
		
		item.detach(curUser.belongings.backpack);
		GLog.p(Messages.get(this, "recycled", result.name()));
		if (!result.collect()){
			Dungeon.level.drop(result, curUser.pos).sprite.drop();
		}
		//TODO visuals
	}
	
	public static boolean isRecyclable(Item item){
		return (item instanceof Potion && !(item instanceof Elixir || item instanceof Brew)) ||
				item instanceof Scroll ||
				item instanceof Plant.Seed ||
				item instanceof Runestone;
	}
	
	@Override
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 8f));
	}
	
	public static class Recipe extends net.casiello.pixeldungeonrescue.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfTransmutation.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = Recycle.class;
			outQuantity = 8;
		}
		
	}
}
