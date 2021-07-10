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

package com.saqfish.spdnet.items.spells;

import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.actors.hero.Hero;
import com.saqfish.spdnet.items.potions.AlchemicalCatalyst;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.scenes.AlchemyScene;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.utils.GLog;

public class Alchemize extends Spell implements AlchemyScene.AlchemyProvider {
	
	{
		image = ItemSpriteSheet.ALCHEMIZE;
	}
	
	@Override
	protected void onCast(Hero hero) {
		if (hero.visibleEnemies() > hero.mindVisionEnemies.size()) {
			GLog.i( Messages.get(this, "enemy_near") );
			return;
		}
		detach( curUser.belongings.backpack );
		updateQuickslot();
		AlchemyScene.setProvider(this);
		ShatteredPixelDungeon.switchScene(AlchemyScene.class);
	}
	
	@Override
	public int getEnergy() {
		return 0;
	}
	
	@Override
	public void spendEnergy(int reduction) {
		//do nothing
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((40 + 40) / 4f));
	}
	
	public static class Recipe extends com.saqfish.spdnet.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ArcaneCatalyst.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = Alchemize.class;
			outQuantity = 4;
		}
		
	}
}
