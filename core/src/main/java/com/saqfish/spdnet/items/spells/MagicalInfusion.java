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

import com.saqfish.spdnet.Badges;
import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.Statistics;
import com.saqfish.spdnet.actors.buffs.Degrade;
import com.saqfish.spdnet.actors.hero.Talent;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.armor.Armor;
import com.saqfish.spdnet.items.scrolls.ScrollOfUpgrade;
import com.saqfish.spdnet.items.weapon.Weapon;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.saqfish.spdnet.utils.GLog;

public class MagicalInfusion extends InventorySpell {
	
	{
		image = ItemSpriteSheet.MAGIC_INFUSE;

		unique = true;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return item.isUpgradable();
	}

	@Override
	protected void onItemSelected( Item item ) {

		ScrollOfUpgrade.upgrade(curUser);

		Degrade.detach( curUser, Degrade.class );

		if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
			((Weapon) item).upgrade(true);
		} else if (item instanceof Armor && ((Armor) item).glyph != null) {
			((Armor) item).upgrade(true);
		} else {
			item.upgrade();
		}
		
		GLog.p( Messages.get(this, "infuse", item.name()) );
		Talent.onUpgradeScrollUsed( Dungeon.hero );
		Badges.validateItemLevelAquired(item);

		Statistics.upgradesUsed++;
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 1f));
	}
	
	public static class Recipe extends com.saqfish.spdnet.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfUpgrade.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = MagicalInfusion.class;
			outQuantity = 1;
		}
		
	}
}
