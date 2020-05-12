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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class ScrollOfTransmutation extends InventoryScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_TRANSMUTE;
		mode = WndBag.Mode.TRANMSUTABLE;
		
		bones = true;
	}
	
	public static boolean canTransmute(Item item){
		return item instanceof MeleeWeapon ||
				(item instanceof MissileWeapon && !(item instanceof Dart)) ||
				(item instanceof Potion && !(item instanceof Elixir || item instanceof Brew || item instanceof AlchemicalCatalyst)) ||
				item instanceof Scroll ||
				item instanceof Ring ||
				item instanceof Wand ||
				item instanceof Plant.Seed ||
				item instanceof Runestone ||
				item instanceof Artifact;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		Item result;
		
		if (item instanceof MagesStaff) {
			result = changeStaff( (MagesStaff)item );
		} else if (item instanceof MeleeWeapon || item instanceof MissileWeapon) {
			result = changeWeapon( (Weapon)item );
		} else if (item instanceof Scroll) {
			result = changeScroll( (Scroll)item );
		} else if (item instanceof Potion) {
			result = changePotion( (Potion)item );
		} else if (item instanceof Ring) {
			result = changeRing( (Ring)item );
		} else if (item instanceof Wand) {
			result = changeWand( (Wand)item );
		} else if (item instanceof Plant.Seed) {
			result = changeSeed((Plant.Seed) item);
		} else if (item instanceof Runestone) {
			result = changeStone((Runestone) item);
		} else if (item instanceof Artifact) {
			result = changeArtifact( (Artifact)item );
		} else {
			result = null;
		}
		
		if (result == null){
			//This shouldn't ever trigger
			GLog.n( Messages.get(this, "nothing") );
			curItem.collect( curUser.belongings.backpack );
		} else {
			if (item.isEquipped(Dungeon.hero)){
				item.cursed = false; //to allow it to be unequipped
				((EquipableItem)item).doUnequip(Dungeon.hero, false);
				((EquipableItem)result).doEquip(Dungeon.hero);
			} else {
				item.detach(Dungeon.hero.belongings.backpack);
				if (!result.collect()){
					Dungeon.level.drop(result, curUser.pos).sprite.drop();
				}
			}
			if (result.isIdentified()){
				Catalog.setSeen(result.getClass());
			}
			//TODO visuals
			GLog.p( Messages.get(this, "morph") );
		}
		
	}
	
	private MagesStaff changeStaff( MagesStaff staff ){
		Class<?extends Wand> wandClass = staff.wandClass();
		
		if (wandClass == null){
			return null;
		} else {
			Wand n;
			do {
				n = (Wand) Generator.random(Generator.Category.WAND);
			} while (Challenges.isItemBlocked(n) || n.getClass() == wandClass);
			n.level(0);
			n.identify();
			staff.imbueWand(n, null);
		}
		
		return staff;
	}
	
	private Weapon changeWeapon( Weapon w ) {
		
		Weapon n;
		Generator.Category c;
		if (w instanceof MeleeWeapon) {
			c = Generator.wepTiers[((MeleeWeapon)w).tier - 1];
		} else {
			c = Generator.misTiers[((MissileWeapon)w).tier - 1];
		}
		
		do {
			n = (Weapon) Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		} while (Challenges.isItemBlocked(n) || n.getClass() == w.getClass());
		
		int level = w.level();
		if (w.curseInfusionBonus) level--;
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.enchantment = w.enchantment;
		n.curseInfusionBonus = w.curseInfusionBonus;
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		n.augment = w.augment;
		
		return n;
		
	}
	
	private Ring changeRing( Ring r ) {
		Ring n;
		do {
			n = (Ring)Generator.random( Generator.Category.RING );
		} while (Challenges.isItemBlocked(n) || n.getClass() == r.getClass());
		
		n.level(0);
		
		int level = r.level();
		if (level > 0) {
			n.upgrade( level );
		} else if (level < 0) {
			n.degrade( -level );
		}
		
		n.levelKnown = r.levelKnown;
		n.cursedKnown = r.cursedKnown;
		n.cursed = r.cursed;
		
		return n;
	}
	
	private Artifact changeArtifact( Artifact a ) {
		Artifact n = Generator.randomArtifact();
		
		if (n != null && !Challenges.isItemBlocked(n)){
			n.cursedKnown = a.cursedKnown;
			n.cursed = a.cursed;
			n.levelKnown = a.levelKnown;
			n.transferUpgrade(a.visiblyUpgraded());
			return n;
		}
		
		return null;
	}
	
	private Wand changeWand( Wand w ) {
		
		Wand n;
		do {
			n = (Wand)Generator.random( Generator.Category.WAND );
		} while ( Challenges.isItemBlocked(n) || n.getClass() == w.getClass());
		
		n.level( 0 );
		int level = w.level();
		if (w.curseInfusionBonus) level--;
		n.upgrade( level );
		
		n.levelKnown = w.levelKnown;
		n.cursedKnown = w.cursedKnown;
		n.cursed = w.cursed;
		n.curseInfusionBonus = w.curseInfusionBonus;
		
		return n;
	}
	
	private Plant.Seed changeSeed( Plant.Seed s ) {
		
		Plant.Seed n;
		
		do {
			n = (Plant.Seed)Generator.random( Generator.Category.SEED );
		} while (n.getClass() == s.getClass());
		
		return n;
	}
	
	private Runestone changeStone( Runestone r ) {
		
		Runestone n;
		
		do {
			n = (Runestone) Generator.random( Generator.Category.STONE );
		} while (n.getClass() == r.getClass());
		
		return n;
	}
	
	private Scroll changeScroll( Scroll s ) {
		if (s instanceof ExoticScroll) {
			return Reflection.newInstance(ExoticScroll.exoToReg.get(s.getClass()));
		} else {
			return Reflection.newInstance(ExoticScroll.regToExo.get(s.getClass()));
		}
	}
	
	private Potion changePotion( Potion p ) {
		if	(p instanceof ExoticPotion) {
			return Reflection.newInstance(ExoticPotion.exoToReg.get(p.getClass()));
		} else {
			return Reflection.newInstance(ExoticPotion.regToExo.get(p.getClass()));
		}
	}
	
	@Override
	public void empoweredRead() {
		//does nothing, this shouldn't happen
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
