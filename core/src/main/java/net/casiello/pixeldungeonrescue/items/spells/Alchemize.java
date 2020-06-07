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

import net.casiello.pixeldungeonrescue.ShatteredPixelDungeon;
import net.casiello.pixeldungeonrescue.actors.hero.Hero;
import net.casiello.pixeldungeonrescue.items.potions.AlchemicalCatalyst;
import net.casiello.pixeldungeonrescue.messages.Messages;
import net.casiello.pixeldungeonrescue.scenes.AlchemyScene;
import net.casiello.pixeldungeonrescue.sprites.ItemSpriteSheet;
import net.casiello.pixeldungeonrescue.utils.GLog;

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
	public int price() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((40 + 40) / 4f));
	}
	
	public static class Recipe extends net.casiello.pixeldungeonrescue.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ArcaneCatalyst.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = Alchemize.class;
			outQuantity = 4;
		}
		
	}
}
