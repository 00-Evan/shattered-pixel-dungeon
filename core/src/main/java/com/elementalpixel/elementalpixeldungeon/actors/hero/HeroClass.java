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

package com.elementalpixel.elementalpixeldungeon.actors.hero;

import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Badges;
import com.elementalpixel.elementalpixeldungeon.Challenges;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.items.AddItems;
import com.elementalpixel.elementalpixeldungeon.items.Amulet;
import com.elementalpixel.elementalpixeldungeon.items.ArmorKit;
import com.elementalpixel.elementalpixeldungeon.items.BrokenAmulet;
import com.elementalpixel.elementalpixeldungeon.items.BrokenSeal;
import com.elementalpixel.elementalpixeldungeon.items.Item;
import com.elementalpixel.elementalpixeldungeon.items.TomeOfMastery;
import com.elementalpixel.elementalpixeldungeon.items.armor.ClothArmor;
import com.elementalpixel.elementalpixeldungeon.items.armor.PlateArmor;
import com.elementalpixel.elementalpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.elementalpixel.elementalpixeldungeon.items.artifacts.CloakOfShadows;
import com.elementalpixel.elementalpixeldungeon.items.artifacts.HornOfPlenty;
import com.elementalpixel.elementalpixeldungeon.items.bags.MagicalHolster;
import com.elementalpixel.elementalpixeldungeon.items.bags.PotionBandolier;
import com.elementalpixel.elementalpixeldungeon.items.bags.ScrollHolder;
import com.elementalpixel.elementalpixeldungeon.items.bags.VelvetPouch;
import com.elementalpixel.elementalpixeldungeon.items.food.Food;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfExperience;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfFrost;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfHaste;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfHealing;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfInvisibility;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfLevitation;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfMindVision;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfParalyticGas;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfPurity;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfStrength;
import com.elementalpixel.elementalpixeldungeon.items.potions.PotionOfToxicGas;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfRage;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.elementalpixel.elementalpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.elementalpixel.elementalpixeldungeon.items.teleport;
import com.elementalpixel.elementalpixeldungeon.items.wands.WandOfMagicMissile;
import com.elementalpixel.elementalpixeldungeon.items.weapon.AlchemistFlask;
import com.elementalpixel.elementalpixeldungeon.items.weapon.SpiritBow;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.AlchemistDagger;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.Dagger;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.Glaive;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.Gloves;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.MagesStaff;
import com.elementalpixel.elementalpixeldungeon.items.weapon.melee.WornShortsword;
import com.elementalpixel.elementalpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.elementalpixel.elementalpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.plants.Earthroot;
import com.elementalpixel.elementalpixeldungeon.plants.Fadeleaf;
import com.elementalpixel.elementalpixeldungeon.plants.Rotberry;
import com.elementalpixel.elementalpixeldungeon.plants.Starflower;
import com.elementalpixel.elementalpixeldungeon.plants.Sungrass;
import com.elementalpixel.elementalpixeldungeon.windows.WndSettings;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( "warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( "mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( "rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( "huntress", HeroSubClass.SNIPER, HeroSubClass.WARDEN ),
	ALCHEMIST("alchemist", HeroSubClass.ELEMENTALIST, HeroSubClass.SCIENTIST);

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass( String title, HeroSubClass...subClasses ) {
		this.title = title;
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

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

			case ALCHEMIST:
				initAlchemist( hero );
				break;
		}

	}

	private static void initCommon( Hero hero ) {
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new AddItems().collect();
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
			case ALCHEMIST:
				return Badges.Badge.MASTERY_ALCHEMIST;
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
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

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

	private static void initAlchemist( Hero hero ) {

		(hero.belongings.weapon = new AlchemistDagger()).identify();

		AlchemistFlask flask = new AlchemistFlask();
		flask.identify().collect();

		Dungeon.quickslot.setSlot(0, flask);

		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();

		new PotionOfToxicGas().identify();
		new ScrollOfTeleportation().identify();

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
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case ALCHEMIST:
				return Assets.Sprites.ALCHEMIST;
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			case ALCHEMIST:
				return Assets.Splashes.ALCHEMIST;
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
			case ALCHEMIST:
				return new String[]{
						Messages.get(HeroClass.class, "alchemist_perk1"),
						Messages.get(HeroClass.class, "alchemist_perk2"),
						Messages.get(HeroClass.class, "alchemist_perk3"),
						Messages.get(HeroClass.class, "alchemist_perk4"),
						Messages.get(HeroClass.class, "alchemist_perk5"),
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
			case ALCHEMIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ALCHEMIST);
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
			case ALCHEMIST:
				return Messages.get(HeroClass.class, "alchemist_unlock");
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
