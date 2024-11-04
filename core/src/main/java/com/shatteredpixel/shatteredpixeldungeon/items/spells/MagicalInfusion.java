/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUpgrade;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class MagicalInfusion extends InventorySpell {
	
	{
		image = ItemSpriteSheet.MAGIC_INFUSE;

		unique = true;

		talentFactor = 2;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return item.isUpgradable();
	}

	@Override
	protected void onItemSelected( Item item ) {

		GameScene.show(new WndUpgrade(this, item, false));

	}

	public void reShowSelector(){
		curItem = this;
		GameScene.selectItem(itemSelector);
	}

	public void useAnimation(){
		curUser.spend(1f);
		curUser.busy();
		(curUser.sprite).operate(curUser.pos);

		Sample.INSTANCE.play(Assets.Sounds.READ);
		Invisibility.dispel();

		Catalog.countUse(curItem.getClass());
		if (Random.Float() < ((Spell) curItem).talentChance) {
			Talent.onScrollUsed(curUser, curUser.pos, ((Spell) curItem).talentFactor, getClass());
		}
	}

	public Item upgradeItem( Item item ){
		ScrollOfUpgrade.upgrade(curUser);

		Degrade.detach( curUser, Degrade.class );

		if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
			item = ((Weapon) item).upgrade(true);
		} else if (item instanceof Armor && ((Armor) item).glyph != null) {
			item = ((Armor) item).upgrade(true);
		} else {
			boolean wasCursed = item.cursed;
			boolean wasCurseInfused = item instanceof Wand && ((Wand) item).curseInfusionBonus;
			item = item.upgrade();
			if (wasCursed) item.cursed = true;
			if (wasCurseInfused) ((Wand) item).curseInfusionBonus = true;
		}

		GLog.p( Messages.get(this, "infuse") );
		Badges.validateItemLevelAquired(item);

		Catalog.countUse(item.getClass());

		Statistics.upgradesUsed++;

		return item;
	}
	
	@Override
	public int value() {
		return 60 * quantity;
	}

	@Override
	public int energyVal() {
		return 12 * quantity;
	}
	
	public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfUpgrade.class};
			inQuantity = new int[]{1};
			
			cost = 12;
			
			output = MagicalInfusion.class;
			outQuantity = 1;
		}
		
	}
}
