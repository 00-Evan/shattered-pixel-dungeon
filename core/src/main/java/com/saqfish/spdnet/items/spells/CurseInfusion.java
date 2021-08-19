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

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Badges;
import com.saqfish.spdnet.effects.CellEmitter;
import com.saqfish.spdnet.effects.particles.ShadowParticle;
import com.saqfish.spdnet.items.EquipableItem;
import com.saqfish.spdnet.items.Item;
import com.saqfish.spdnet.items.armor.Armor;
import com.saqfish.spdnet.items.quest.MetalShard;
import com.saqfish.spdnet.items.scrolls.ScrollOfRemoveCurse;
import com.saqfish.spdnet.items.wands.Wand;
import com.saqfish.spdnet.items.weapon.SpiritBow;
import com.saqfish.spdnet.items.weapon.Weapon;
import com.saqfish.spdnet.items.weapon.melee.MagesStaff;
import com.saqfish.spdnet.items.weapon.melee.MeleeWeapon;
import com.saqfish.spdnet.items.weapon.missiles.MissileWeapon;
import com.saqfish.spdnet.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class CurseInfusion extends InventorySpell {
	
	{
		image = ItemSpriteSheet.CURSE_INFUSE;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return ((item instanceof EquipableItem && !(item instanceof MissileWeapon)) || item instanceof Wand);
	}

	@Override
	protected void onItemSelected(Item item) {
		
		CellEmitter.get(curUser.pos).burst(ShadowParticle.UP, 5);
		Sample.INSTANCE.play(Assets.Sounds.CURSED);
		
		item.cursed = true;
		if (item instanceof MeleeWeapon || item instanceof SpiritBow) {
			Weapon w = (Weapon) item;
			if (w.enchantment != null) {
				w.enchant(Weapon.Enchantment.randomCurse(w.enchantment.getClass()));
			} else {
				w.enchant(Weapon.Enchantment.randomCurse());
			}
			w.curseInfusionBonus = true;
			if (w instanceof MagesStaff){
				((MagesStaff) w).updateWand(true);
			}
		} else if (item instanceof Armor){
			Armor a = (Armor) item;
			if (a.glyph != null){
				a.inscribe(Armor.Glyph.randomCurse(a.glyph.getClass()));
			} else {
				a.inscribe(Armor.Glyph.randomCurse());
			}
			a.curseInfusionBonus = true;
		} else if (item instanceof Wand){
			((Wand) item).curseInfusionBonus = true;
			((Wand) item).updateLevel();
		}
		Badges.validateItemLevelAquired(item);
		updateQuickslot();
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 100) / 3f));
	}
	
	public static class Recipe extends com.saqfish.spdnet.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfRemoveCurse.class, MetalShard.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = CurseInfusion.class;
			outQuantity = 3;
		}
		
	}
}
