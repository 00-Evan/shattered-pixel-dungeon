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

package net.casiello.pixeldungeonrescue.journal;

import net.casiello.pixeldungeonrescue.Badges;
import net.casiello.pixeldungeonrescue.items.Item;
import net.casiello.pixeldungeonrescue.items.armor.ClothArmor;
import net.casiello.pixeldungeonrescue.items.armor.HuntressArmor;
import net.casiello.pixeldungeonrescue.items.armor.LeatherArmor;
import net.casiello.pixeldungeonrescue.items.armor.MageArmor;
import net.casiello.pixeldungeonrescue.items.armor.MailArmor;
import net.casiello.pixeldungeonrescue.items.armor.PlateArmor;
import net.casiello.pixeldungeonrescue.items.armor.RogueArmor;
import net.casiello.pixeldungeonrescue.items.armor.ScaleArmor;
import net.casiello.pixeldungeonrescue.items.armor.WarriorArmor;
import net.casiello.pixeldungeonrescue.items.artifacts.AlchemistsToolkit;
import net.casiello.pixeldungeonrescue.items.artifacts.ChaliceOfBlood;
import net.casiello.pixeldungeonrescue.items.artifacts.CloakOfShadows;
import net.casiello.pixeldungeonrescue.items.artifacts.DriedRose;
import net.casiello.pixeldungeonrescue.items.artifacts.EtherealChains;
import net.casiello.pixeldungeonrescue.items.artifacts.HornOfPlenty;
import net.casiello.pixeldungeonrescue.items.artifacts.MasterThievesArmband;
import net.casiello.pixeldungeonrescue.items.artifacts.SandalsOfNature;
import net.casiello.pixeldungeonrescue.items.artifacts.TalismanOfForesight;
import net.casiello.pixeldungeonrescue.items.artifacts.TimekeepersHourglass;
import net.casiello.pixeldungeonrescue.items.artifacts.UnstableSpellbook;
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
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Catalog {
	
	WEAPONS,
	ARMOR,
	WANDS,
	RINGS,
	ARTIFACTS,
	POTIONS,
	SCROLLS;
	
	private LinkedHashMap<Class<? extends Item>, Boolean> seen = new LinkedHashMap<>();
	
	public Collection<Class<? extends Item>> items(){
		return seen.keySet();
	}
	
	public boolean allSeen(){
		for (Class<?extends Item> item : items()){
			if (!seen.get(item)){
				return false;
			}
		}
		return true;
	}
	
	static {
		WEAPONS.seen.put( WornShortsword.class,             false);
		WEAPONS.seen.put( Gloves.class,                     false);
		WEAPONS.seen.put( Dagger.class,                     false);
		WEAPONS.seen.put( MagesStaff.class,                 false);
		//WEAPONS.seen.put( Boomerang.class,                  false);
		WEAPONS.seen.put( Shortsword.class,                 false);
		WEAPONS.seen.put( HandAxe.class,                    false);
		WEAPONS.seen.put( Spear.class,                      false);
		WEAPONS.seen.put( Quarterstaff.class,               false);
		WEAPONS.seen.put( Dirk.class,                       false);
		WEAPONS.seen.put( Sword.class,                      false);
		WEAPONS.seen.put( Mace.class,                       false);
		WEAPONS.seen.put( Scimitar.class,                   false);
		WEAPONS.seen.put( RoundShield.class,                false);
		WEAPONS.seen.put( Sai.class,                        false);
		WEAPONS.seen.put( Whip.class,                       false);
		WEAPONS.seen.put( Longsword.class,                  false);
		WEAPONS.seen.put( BattleAxe.class,                  false);
		WEAPONS.seen.put( Flail.class,                      false);
		WEAPONS.seen.put( RunicBlade.class,                 false);
		WEAPONS.seen.put( AssassinsBlade.class,             false);
		WEAPONS.seen.put( Crossbow.class,                   false);
		WEAPONS.seen.put( Greatsword.class,                 false);
		WEAPONS.seen.put( WarHammer.class,                  false);
		WEAPONS.seen.put( Glaive.class,                     false);
		WEAPONS.seen.put( Greataxe.class,                   false);
		WEAPONS.seen.put( Greatshield.class,                false);
		WEAPONS.seen.put( Gauntlet.class,                   false);
	
		ARMOR.seen.put( ClothArmor.class,                   false);
		ARMOR.seen.put( LeatherArmor.class,                 false);
		ARMOR.seen.put( MailArmor.class,                    false);
		ARMOR.seen.put( ScaleArmor.class,                   false);
		ARMOR.seen.put( PlateArmor.class,                   false);
		ARMOR.seen.put( WarriorArmor.class,                 false);
		ARMOR.seen.put( MageArmor.class,                    false);
		ARMOR.seen.put( RogueArmor.class,                   false);
		ARMOR.seen.put( HuntressArmor.class,                false);
	
		WANDS.seen.put( WandOfMagicMissile.class,           false);
		WANDS.seen.put( WandOfLightning.class,              false);
		WANDS.seen.put( WandOfDisintegration.class,         false);
		WANDS.seen.put( WandOfFireblast.class,              false);
		WANDS.seen.put( WandOfCorrosion.class,              false);
		WANDS.seen.put( WandOfBlastWave.class,              false);
		WANDS.seen.put( WandOfLivingEarth.class,            false);
		WANDS.seen.put( WandOfFrost.class,                  false);
		WANDS.seen.put( WandOfPrismaticLight.class,         false);
		WANDS.seen.put( WandOfWarding.class,                false);
		WANDS.seen.put( WandOfTransfusion.class,            false);
		WANDS.seen.put( WandOfCorruption.class,             false);
		WANDS.seen.put( WandOfRegrowth.class,               false);
	
		RINGS.seen.put( RingOfAccuracy.class,               false);
		RINGS.seen.put( RingOfEnergy.class,                 false);
		RINGS.seen.put( RingOfElements.class,               false);
		RINGS.seen.put( RingOfEvasion.class,                false);
		RINGS.seen.put( RingOfForce.class,                  false);
		RINGS.seen.put( RingOfFuror.class,                  false);
		RINGS.seen.put( RingOfHaste.class,                  false);
		RINGS.seen.put( RingOfMight.class,                  false);
		RINGS.seen.put( RingOfSharpshooting.class,          false);
		RINGS.seen.put( RingOfTenacity.class,               false);
		RINGS.seen.put( RingOfWealth.class,                 false);
	
		ARTIFACTS.seen.put( AlchemistsToolkit.class,        false);
		//ARTIFACTS.seen.put( CapeOfThorns.class,             false);
		ARTIFACTS.seen.put( ChaliceOfBlood.class,           false);
		ARTIFACTS.seen.put( CloakOfShadows.class,           false);
		ARTIFACTS.seen.put( DriedRose.class,                false);
		ARTIFACTS.seen.put( EtherealChains.class,           false);
		ARTIFACTS.seen.put( HornOfPlenty.class,             false);
		//ARTIFACTS.seen.put( LloydsBeacon.class,             false);
		ARTIFACTS.seen.put( MasterThievesArmband.class,     false);
		ARTIFACTS.seen.put( SandalsOfNature.class,          false);
		ARTIFACTS.seen.put( TalismanOfForesight.class,      false);
		ARTIFACTS.seen.put( TimekeepersHourglass.class,     false);
		ARTIFACTS.seen.put( UnstableSpellbook.class,        false);
	
		POTIONS.seen.put( PotionOfHealing.class,            false);
		POTIONS.seen.put( PotionOfStrength.class,           false);
		POTIONS.seen.put( PotionOfLiquidFlame.class,        false);
		POTIONS.seen.put( PotionOfFrost.class,              false);
		POTIONS.seen.put( PotionOfToxicGas.class,           false);
		POTIONS.seen.put( PotionOfParalyticGas.class,       false);
		POTIONS.seen.put( PotionOfPurity.class,             false);
		POTIONS.seen.put( PotionOfLevitation.class,         false);
		POTIONS.seen.put( PotionOfMindVision.class,         false);
		POTIONS.seen.put( PotionOfInvisibility.class,       false);
		POTIONS.seen.put( PotionOfExperience.class,         false);
		POTIONS.seen.put( PotionOfHaste.class,              false);
	
		SCROLLS.seen.put( ScrollOfIdentify.class,           false);
		SCROLLS.seen.put( ScrollOfUpgrade.class,            false);
		SCROLLS.seen.put( ScrollOfRemoveCurse.class,        false);
		SCROLLS.seen.put( ScrollOfMagicMapping.class,       false);
		SCROLLS.seen.put( ScrollOfTeleportation.class,      false);
		SCROLLS.seen.put( ScrollOfRecharging.class,         false);
		SCROLLS.seen.put( ScrollOfMirrorImage.class,        false);
		SCROLLS.seen.put( ScrollOfTerror.class,             false);
		SCROLLS.seen.put( ScrollOfLullaby.class,            false);
		SCROLLS.seen.put( ScrollOfRage.class,               false);
		SCROLLS.seen.put( ScrollOfRetribution.class,        false);
		SCROLLS.seen.put( ScrollOfTransmutation.class,      false);
	}
	
	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();
	static {
		catalogBadges.put(WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(ARMOR, Badges.Badge.ALL_ARMOR_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}
	
	public static boolean isSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass)) {
				return cat.seen.get(itemClass);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass) && !cat.seen.get(itemClass)) {
				cat.seen.put(itemClass, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}
	
	private static final String CATALOGS = "catalogs";
	
	public static void store( Bundle bundle ){
		
		Badges.loadGlobal();
		
		ArrayList<String> seen = new ArrayList<>();
		
		//if we have identified all items of a set, we use the badge to keep track instead.
		if (!Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)) {
			for (Catalog cat : values()) {
				if (!Badges.isUnlocked(catalogBadges.get(cat))) {
					for (Class<? extends Item> item : cat.items()) {
						if (cat.seen.get(item)) seen.add(item.getSimpleName());
					}
				}
			}
		}
		
		bundle.put( CATALOGS, seen.toArray(new String[0]) );
		
	}
	
	public static void restore( Bundle bundle ){
		
		Badges.loadGlobal();
		
		//logic for if we have all badges
		if (Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)){
			for ( Catalog cat : values()){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
			return;
		}
		
		//catalog-specific badge logic
		for (Catalog cat : values()){
			if (Badges.isUnlocked(catalogBadges.get(cat))){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
		}
		
		//general save/load
		if (bundle.contains(CATALOGS)) {
			List<String> seen = Arrays.asList(bundle.getStringArray(CATALOGS));
			
			//TODO should adjust this to tie into the bundling system's class array
			for (Catalog cat : values()) {
				for (Class<? extends Item> item : cat.items()) {
					if (seen.contains(item.getSimpleName())) {
						cat.seen.put(item, true);
					}
				}
			}
		}
	}
	
}
