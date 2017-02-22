/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CapeOfThorns;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.MasterThievesArmband;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorruption;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFireblast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfVenom;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dirk;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatshield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.HandAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Knuckles;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Mace;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.NewShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Boomerang;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.CurareDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Dart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.IncendiaryDart;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Javelin;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Shuriken;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Tamahawk;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Dreamfoil;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Generator {

	public static enum Category {
		WEAPON	( 100,	Weapon.class ),
		WEP_T1	( 0, 	Weapon.class),
		WEP_T2	( 0,	Weapon.class),
		WEP_T3	( 0, 	Weapon.class),
		WEP_T4	( 0, 	Weapon.class),
		WEP_T5	( 0, 	Weapon.class),
		ARMOR	( 60,	Armor.class ),
		POTION	( 500,	Potion.class ),
		SCROLL	( 400,	Scroll.class ),
		WAND	( 40,	Wand.class ),
		RING	( 15,	Ring.class ),
		ARTIFACT( 15,   Artifact.class),
		SEED	( 50,	Plant.Seed.class ),
		FOOD	( 0,	Food.class ),
		GOLD	( 500,	Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}
	};
	
	private static HashMap<Category,Float> categoryProbs = new HashMap<Generator.Category, Float>();

	private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1};
	
	static {
		
		Category.GOLD.classes = new Class<?>[]{
			Gold.class };
		Category.GOLD.probs = new float[]{ 1 };
		
		Category.SCROLL.classes = new Class<?>[]{
			ScrollOfIdentify.class,
			ScrollOfTeleportation.class,
			ScrollOfRemoveCurse.class,
			ScrollOfUpgrade.class,
			ScrollOfRecharging.class,
			ScrollOfMagicMapping.class,
			ScrollOfRage.class,
			ScrollOfTerror.class,
			ScrollOfLullaby.class,
			ScrollOfMagicalInfusion.class,
			ScrollOfPsionicBlast.class,
			ScrollOfMirrorImage.class };
		Category.SCROLL.probs = new float[]{ 30, 10, 20, 0, 15, 15, 12, 8, 8, 0, 4, 10 };
		
		Category.POTION.classes = new Class<?>[]{
			PotionOfHealing.class,
			PotionOfExperience.class,
			PotionOfToxicGas.class,
			PotionOfParalyticGas.class,
			PotionOfLiquidFlame.class,
			PotionOfLevitation.class,
			PotionOfStrength.class,
			PotionOfMindVision.class,
			PotionOfPurity.class,
			PotionOfInvisibility.class,
			PotionOfMight.class,
			PotionOfFrost.class };
		Category.POTION.probs = new float[]{ 45, 4, 15, 10, 15, 10, 0, 20, 12, 10, 0, 10 };

		//TODO: add last ones when implemented
		Category.WAND.classes = new Class<?>[]{
			WandOfMagicMissile.class,
			WandOfLightning.class,
			WandOfDisintegration.class,
			WandOfFireblast.class,
			WandOfVenom.class,
			WandOfBlastWave.class,
			//WandOfLivingEarth.class,
			WandOfFrost.class,
			WandOfPrismaticLight.class,
			//WandOfWarding.class,
			WandOfTransfusion.class,
			WandOfCorruption.class,
			WandOfRegrowth.class };
		Category.WAND.probs = new float[]{ 5, 4, 4, 4, 4, 3, /*3,*/ 3, 3, /*3,*/ 3, 3, 3 };

		//see generator.randomWeapon
		Category.WEAPON.classes = new Class<?>[]{};
		Category.WEAPON.probs = new float[]{};

		Category.WEP_T1.classes = new Class<?>[]{
			WornShortsword.class,
			Knuckles.class,
			Dagger.class,
			MagesStaff.class,
			Boomerang.class,
			Dart.class
		};
		Category.WEP_T1.probs = new float[]{ 1, 1, 1, 0, 0, 1 };

		Category.WEP_T2.classes = new Class<?>[]{
			NewShortsword.class,
			HandAxe.class,
			Spear.class,
			Quarterstaff.class,
			Dirk.class,
			IncendiaryDart.class
		};
		Category.WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4, 6 };

		Category.WEP_T3.classes = new Class<?>[]{
			Sword.class,
			Mace.class,
			Scimitar.class,
			RoundShield.class,
			Sai.class,
			Whip.class,
			Shuriken.class,
			CurareDart.class
		};
		Category.WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4, 6, 6 };

		Category.WEP_T4.classes = new Class<?>[]{
			Longsword.class,
			BattleAxe.class,
			Flail.class,
			RunicBlade.class,
			AssassinsBlade.class,
			Javelin.class
		};
		Category.WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 6 };

		Category.WEP_T5.classes = new Class<?>[]{
			Greatsword.class,
			WarHammer.class,
			Glaive.class,
			Greataxe.class,
			Greatshield.class,
			Tamahawk.class
		};
		Category.WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 6 };

		//see Generator.randomArmor
		Category.ARMOR.classes = new Class<?>[]{
			ClothArmor.class,
			LeatherArmor.class,
			MailArmor.class,
			ScaleArmor.class,
			PlateArmor.class };
		Category.ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
		
		Category.FOOD.classes = new Class<?>[]{
			Food.class,
			Pasty.class,
			MysteryMeat.class };
		Category.FOOD.probs = new float[]{ 4, 1, 0 };
			
		Category.RING.classes = new Class<?>[]{
			RingOfAccuracy.class,
			RingOfEvasion.class,
			RingOfElements.class,
			RingOfForce.class,
			RingOfFuror.class,
			RingOfHaste.class,
			RingOfMagic.class, //currently removed from drop tables, pending rework
			RingOfMight.class,
			RingOfSharpshooting.class,
			RingOfTenacity.class,
			RingOfWealth.class};
		Category.RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 };

		Category.ARTIFACT.classes = new Class<?>[]{
			CapeOfThorns.class,
			ChaliceOfBlood.class,
			CloakOfShadows.class,
			HornOfPlenty.class,
			MasterThievesArmband.class,
			SandalsOfNature.class,
			TalismanOfForesight.class,
			TimekeepersHourglass.class,
			UnstableSpellbook.class,
			AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
			DriedRose.class, //starts with no chance of spawning, chance is set directly after beating ghost quest.
			LloydsBeacon.class,
			EtherealChains.class
			};
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
		
		Category.SEED.classes = new Class<?>[]{
			Firebloom.Seed.class,
			Icecap.Seed.class,
			Sorrowmoss.Seed.class,
			Blindweed.Seed.class,
			Sungrass.Seed.class,
			Earthroot.Seed.class,
			Fadeleaf.Seed.class,
			Rotberry.Seed.class,
			BlandfruitBush.Seed.class,
			Dreamfoil.Seed.class,
			Stormvine.Seed.class,
			Starflower.Seed.class};
		Category.SEED.probs = new float[]{ 12, 12, 12, 12, 12, 12, 12, 0, 4, 12, 12, 1 };
	}
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		return random( Random.chances( categoryProbs ) );
	}
	
	public static Item random( Category cat ) {
		try {
			
			categoryProbs.put( cat, categoryProbs.get( cat ) / 2 );
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			ShatteredPixelDungeon.reportException(e);
			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			ShatteredPixelDungeon.reportException(e);
			return null;
			
		}
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Armor a = (Armor)Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
			a.random();
			return a;
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static Weapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static Weapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
			Weapon w = (Weapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}

			Artifact artifact = (Artifact)cat.classes[i].newInstance();

			//remove the chance of spawning this artifact.
			cat.probs[i] = 0;
			spawnedArtifacts.add(cat.classes[i].getSimpleName());

			artifact.random();

			return artifact;

		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}

	public static boolean removeArtifact(Artifact artifact) {
		if (spawnedArtifacts.contains(artifact.getClass().getSimpleName()))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact.getClass())) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact.getClass().getSimpleName());
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();

		//checks for dried rose quest completion, adds the rose in accordingly.
		if (Ghost.Quest.completed()) Category.ARTIFACT.probs[10] = 1;

		spawnedArtifacts = new ArrayList<String>();
	}

	private static ArrayList<String> spawnedArtifacts = new ArrayList<String>();

	private static final String ARTIFACTS = "artifacts";

	//used to store information on which artifacts have been spawned.
	public static void storeInBundle(Bundle bundle) {
		bundle.put( ARTIFACTS, spawnedArtifacts.toArray(new String[spawnedArtifacts.size()]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		initArtifacts();

		if (bundle.contains(ARTIFACTS)) {
			Collections.addAll(spawnedArtifacts, bundle.getStringArray(ARTIFACTS));
			Category cat = Category.ARTIFACT;

			for (String artifact : spawnedArtifacts)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}
