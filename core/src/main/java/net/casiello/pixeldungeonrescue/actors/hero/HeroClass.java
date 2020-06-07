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

package net.casiello.pixeldungeonrescue.actors.hero;

import net.casiello.pixeldungeonrescue.Assets;
import net.casiello.pixeldungeonrescue.Badges;
import net.casiello.pixeldungeonrescue.Challenges;
import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.items.BrokenSeal;
import net.casiello.pixeldungeonrescue.items.Item;
import net.casiello.pixeldungeonrescue.items.armor.ClothArmor;
import net.casiello.pixeldungeonrescue.items.artifacts.CloakOfShadows;
import net.casiello.pixeldungeonrescue.items.bags.PotionBandolier;
import net.casiello.pixeldungeonrescue.items.bags.ScrollHolder;
import net.casiello.pixeldungeonrescue.items.bags.VelvetPouch;
import net.casiello.pixeldungeonrescue.items.food.Food;
import net.casiello.pixeldungeonrescue.items.food.SmallRation;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfHealing;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfInvisibility;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfLiquidFlame;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfMindVision;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfIdentify;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfLullaby;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfMagicMapping;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfRage;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfUpgrade;
import net.casiello.pixeldungeonrescue.items.wands.WandOfMagicMissile;
import net.casiello.pixeldungeonrescue.items.weapon.SpiritBow;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Dagger;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Gloves;
import net.casiello.pixeldungeonrescue.items.weapon.melee.MagesStaff;
import net.casiello.pixeldungeonrescue.items.weapon.melee.WornShortsword;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingKnife;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingStone;
import net.casiello.pixeldungeonrescue.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( "warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( "mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( "rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( "huntress", HeroSubClass.SNIPER, HeroSubClass.WARDEN );

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass( String title, HeroSubClass...subClasses ) {
		this.title = title;
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SmallRation().collect();
		}
		
		new ScrollOfIdentify().identify();

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}
		
		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		
		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;
		
		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		
		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		
		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public HeroSubClass[] subClasses() {
		return subClasses;
	}
	
	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.WARRIOR;
			case MAGE:
				return Assets.MAGE;
			case ROGUE:
				return Assets.ROGUE;
			case HUNTRESS:
				return Assets.HUNTRESS;
		}
	}
	
	public String[] perks() {
		switch (this) {
			case WARRIOR: default:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
		}
	}
	
	public String unlockMsg() {
		switch (this){
			case WARRIOR: default:
				return "";
			case MAGE:
				return Messages.get(HeroClass.class, "mage_unlock");
			case ROGUE:
				return Messages.get(HeroClass.class, "rogue_unlock");
			case HUNTRESS:
				return Messages.get(HeroClass.class, "huntress_unlock");
		}
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
