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

package net.casiello.pixeldungeonrescue.items;

import net.casiello.pixeldungeonrescue.Dungeon;
import net.casiello.pixeldungeonrescue.items.armor.Armor;
import net.casiello.pixeldungeonrescue.items.armor.ClothArmor;
import net.casiello.pixeldungeonrescue.items.armor.LeatherArmor;
import net.casiello.pixeldungeonrescue.items.armor.MailArmor;
import net.casiello.pixeldungeonrescue.items.armor.PlateArmor;
import net.casiello.pixeldungeonrescue.items.armor.ScaleArmor;
import net.casiello.pixeldungeonrescue.items.artifacts.AlchemistsToolkit;
import net.casiello.pixeldungeonrescue.items.artifacts.Artifact;
import net.casiello.pixeldungeonrescue.items.artifacts.CapeOfThorns;
import net.casiello.pixeldungeonrescue.items.artifacts.ChaliceOfBlood;
import net.casiello.pixeldungeonrescue.items.artifacts.CloakOfShadows;
import net.casiello.pixeldungeonrescue.items.artifacts.DriedRose;
import net.casiello.pixeldungeonrescue.items.artifacts.EtherealChains;
import net.casiello.pixeldungeonrescue.items.artifacts.HornOfPlenty;
import net.casiello.pixeldungeonrescue.items.artifacts.LloydsBeacon;
import net.casiello.pixeldungeonrescue.items.artifacts.MasterThievesArmband;
import net.casiello.pixeldungeonrescue.items.artifacts.SandalsOfNature;
import net.casiello.pixeldungeonrescue.items.artifacts.TalismanOfForesight;
import net.casiello.pixeldungeonrescue.items.artifacts.TimekeepersHourglass;
import net.casiello.pixeldungeonrescue.items.artifacts.UnstableSpellbook;
import net.casiello.pixeldungeonrescue.items.bags.Bag;
import net.casiello.pixeldungeonrescue.items.food.Food;
import net.casiello.pixeldungeonrescue.items.food.MysteryMeat;
import net.casiello.pixeldungeonrescue.items.food.Pasty;
import net.casiello.pixeldungeonrescue.items.potions.Potion;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfExperience;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfFrost;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfHaste;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfHealing;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfInvisibility;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfLevitation;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfLiquidFlame;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfMindVision;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfParalyticGas;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfPurity;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfStrength;
import net.casiello.pixeldungeonrescue.items.potions.PotionOfToxicGas;
import net.casiello.pixeldungeonrescue.items.rings.Ring;
import net.casiello.pixeldungeonrescue.items.rings.RingOfAccuracy;
import net.casiello.pixeldungeonrescue.items.rings.RingOfElements;
import net.casiello.pixeldungeonrescue.items.rings.RingOfEnergy;
import net.casiello.pixeldungeonrescue.items.rings.RingOfEvasion;
import net.casiello.pixeldungeonrescue.items.rings.RingOfForce;
import net.casiello.pixeldungeonrescue.items.rings.RingOfFuror;
import net.casiello.pixeldungeonrescue.items.rings.RingOfHaste;
import net.casiello.pixeldungeonrescue.items.rings.RingOfMight;
import net.casiello.pixeldungeonrescue.items.rings.RingOfSharpshooting;
import net.casiello.pixeldungeonrescue.items.rings.RingOfTenacity;
import net.casiello.pixeldungeonrescue.items.rings.RingOfWealth;
import net.casiello.pixeldungeonrescue.items.scrolls.Scroll;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfIdentify;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfLullaby;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfMagicMapping;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfMirrorImage;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfRage;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfRecharging;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfRemoveCurse;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfRetribution;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTeleportation;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTerror;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfTransmutation;
import net.casiello.pixeldungeonrescue.items.scrolls.ScrollOfUpgrade;
import net.casiello.pixeldungeonrescue.items.stones.Runestone;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfAffection;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfAggression;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfAugmentation;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfBlast;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfBlink;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfClairvoyance;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfDeepenedSleep;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfDisarming;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfEnchantment;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfFlock;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfIntuition;
import net.casiello.pixeldungeonrescue.items.stones.StoneOfShock;
import net.casiello.pixeldungeonrescue.items.wands.Wand;
import net.casiello.pixeldungeonrescue.items.wands.WandOfBlastWave;
import net.casiello.pixeldungeonrescue.items.wands.WandOfCorrosion;
import net.casiello.pixeldungeonrescue.items.wands.WandOfCorruption;
import net.casiello.pixeldungeonrescue.items.wands.WandOfDisintegration;
import net.casiello.pixeldungeonrescue.items.wands.WandOfFireblast;
import net.casiello.pixeldungeonrescue.items.wands.WandOfFrost;
import net.casiello.pixeldungeonrescue.items.wands.WandOfLightning;
import net.casiello.pixeldungeonrescue.items.wands.WandOfLivingEarth;
import net.casiello.pixeldungeonrescue.items.wands.WandOfMagicMissile;
import net.casiello.pixeldungeonrescue.items.wands.WandOfPrismaticLight;
import net.casiello.pixeldungeonrescue.items.wands.WandOfRegrowth;
import net.casiello.pixeldungeonrescue.items.wands.WandOfTransfusion;
import net.casiello.pixeldungeonrescue.items.wands.WandOfWarding;
import net.casiello.pixeldungeonrescue.items.weapon.melee.AssassinsBlade;
import net.casiello.pixeldungeonrescue.items.weapon.melee.BattleAxe;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Crossbow;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Dagger;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Dirk;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Flail;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Gauntlet;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Glaive;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Gloves;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Greataxe;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Greatshield;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Greatsword;
import net.casiello.pixeldungeonrescue.items.weapon.melee.HandAxe;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Longsword;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Mace;
import net.casiello.pixeldungeonrescue.items.weapon.melee.MagesStaff;
import net.casiello.pixeldungeonrescue.items.weapon.melee.MeleeWeapon;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Quarterstaff;
import net.casiello.pixeldungeonrescue.items.weapon.melee.RoundShield;
import net.casiello.pixeldungeonrescue.items.weapon.melee.RunicBlade;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Sai;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Scimitar;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Shortsword;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Spear;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Sword;
import net.casiello.pixeldungeonrescue.items.weapon.melee.WarHammer;
import net.casiello.pixeldungeonrescue.items.weapon.melee.Whip;
import net.casiello.pixeldungeonrescue.items.weapon.melee.WornShortsword;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.HeavyBoomerang;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.Bolas;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.FishingSpear;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ForceCube;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.Javelin;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.Kunai;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.MissileWeapon;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.Shuriken;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingClub;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingHammer;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingKnife;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingSpear;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.ThrowingStone;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.Tomahawk;
import net.casiello.pixeldungeonrescue.items.weapon.missiles.Trident;
import net.casiello.pixeldungeonrescue.plants.Blindweed;
import net.casiello.pixeldungeonrescue.plants.Dreamfoil;
import net.casiello.pixeldungeonrescue.plants.Earthroot;
import net.casiello.pixeldungeonrescue.plants.Fadeleaf;
import net.casiello.pixeldungeonrescue.plants.Firebloom;
import net.casiello.pixeldungeonrescue.plants.Icecap;
import net.casiello.pixeldungeonrescue.plants.Plant;
import net.casiello.pixeldungeonrescue.plants.Rotberry;
import net.casiello.pixeldungeonrescue.plants.Sorrowmoss;
import net.casiello.pixeldungeonrescue.plants.Starflower;
import net.casiello.pixeldungeonrescue.plants.Stormvine;
import net.casiello.pixeldungeonrescue.plants.Sungrass;
import net.casiello.pixeldungeonrescue.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 6,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 4,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		WAND	( 3,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		FOOD	( 0,    Food.class ),
		
		POTION	( 20,   Potion.class ),
		SEED	( 0,    Plant.Seed.class ), //dropped by grass
		
		SCROLL	( 20,   Scroll.class ),
		STONE   ( 2,    Runestone.class),
		
		GOLD	( 18,   Gold.class );
		
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
		
		private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1};
		
		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			POTION.classes = new Class<?>[]{
					PotionOfStrength.class, //2 drop every chapter, see Dungeon.posNeeded()
					PotionOfHealing.class,
					PotionOfMindVision.class,
					PotionOfFrost.class,
					PotionOfLiquidFlame.class,
					PotionOfToxicGas.class,
					PotionOfHaste.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfParalyticGas.class,
					PotionOfPurity.class,
					PotionOfExperience.class};
			POTION.probs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			
			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Blindweed.Seed.class,
					Dreamfoil.Seed.class,
					Earthroot.Seed.class,
					Fadeleaf.Seed.class,
					Firebloom.Seed.class,
					Icecap.Seed.class,
					Sorrowmoss.Seed.class,
					Stormvine.Seed.class,
					Sungrass.Seed.class,
					Swiftthistle.Seed.class,
					Starflower.Seed.class};
			SEED.probs = new float[]{ 0, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 1 };
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfUpgrade.class, //3 drop every chapter, see Dungeon.souNeeded()
					ScrollOfIdentify.class,
					ScrollOfRemoveCurse.class,
					ScrollOfMirrorImage.class,
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfLullaby.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfRetribution.class,
					ScrollOfTerror.class,
					ScrollOfTransmutation.class
			};
			SCROLL.probs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			
			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 is guaranteed to drop on floors 6-19
					StoneOfAugmentation.class,  //1 is sold in each shop
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfAggression.class,
					StoneOfAffection.class,
					StoneOfBlast.class,
					StoneOfBlink.class,
					StoneOfClairvoyance.class,
					StoneOfDeepenedSleep.class,
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class
			};
			STONE.probs = new float[]{ 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Gloves.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 6, 5 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 6, 5, 4 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 6, 5, 4 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{ 6, 5, 4 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{ 6, 5, 4 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class,
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0,  0, 40, 50, 10},
			{0,  0, 20, 40, 40},
			{0,  0,  0, 20, 80}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		switch (cat) {
		case ARMOR:
			return randomArmor();
		case WEAPON:
			return randomWeapon();
		case MISSILE:
			return randomMissile();
		case ARTIFACT:
			Item item = randomArtifact();
			//if we're out of artifacts, return a ring instead.
			return item != null ? item : random(Category.RING);
		default:
			return ((Item) Reflection.newInstance(cat.classes[Random.chances( cat.probs )])).random();
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Armor a = (Armor)Reflection.newInstance(Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])]);
		a.random();
		return a;
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
		MeleeWeapon w = (MeleeWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
		MissileWeapon w = (MissileWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		Category cat = Category.ARTIFACT;
		int i = Random.chances( cat.probs );

		//if no artifacts are left, return null
		if (i == -1){
			return null;
		}
		
		Class<?extends Artifact> art = (Class<? extends Artifact>) cat.classes[i];

		if (removeArtifact(art)) {
			Artifact artifact = Reflection.newInstance(art);
			artifact.random();
			return artifact;
		} else {
			return null;
		}
	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		if (spawnedArtifacts.contains(artifact))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact)) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact);
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = Category.INITIAL_ARTIFACT_PROBS.clone();
		spawnedArtifacts = new ArrayList<>();
	}

	private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();
	
	private static final String GENERAL_PROBS = "general_probs";
	private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);
		
		bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		} else {
			reset();
		}
		
		initArtifacts();
		
		for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
			removeArtifact(artifact);
		}
		
	}
}
