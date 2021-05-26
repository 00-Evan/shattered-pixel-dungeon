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

package com.elementalpixel.elementalpixeldungeon.items.spells;


import com.elementalpixel.elementalpixeldungeon.Badges;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.Statistics;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Talent;
import com.elementalpixel.elementalpixeldungeon.effects.Speck;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.armor.Armor;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.elementalpixel.elementalpixeldungeon.items.weapon.Weapon;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSpriteSheet;
import com.elementalpixel.elementalpixeldungeon.utils.GLog;
import com.elementalpixel.elementalpixeldungeon.windows.WndBag;

public class MagicalInfusion extends InventorySpell {
	
	{
		mode = WndBag.Mode.UPGRADEABLE;
		image = ItemSpriteSheet.MAGIC_INFUSE;

		unique = true;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		if (item instanceof Weapon && ((Weapon) item).enchantment != null && !((Weapon) item).hasCurseEnchant()) {
			((Weapon) item).upgrade(true);
		} else if (item instanceof Armor && ((Armor) item).glyph != null && !((Armor) item).hasCurseGlyph()) {
			((Armor) item).upgrade(true);
		} else {
			item.upgrade();
		}
		
		GLog.p( Messages.get(this, "infuse", item.name()) );
		Talent.onUpgradeScrollUsed( Dungeon.hero );
		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Statistics.upgradesUsed++;
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 1f));
	}
	
	public static class Recipe extends com.elementalpixel.elementalpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfUpgrade.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = MagicalInfusion.class;
			outQuantity = 1;
		}
		
	}
}
