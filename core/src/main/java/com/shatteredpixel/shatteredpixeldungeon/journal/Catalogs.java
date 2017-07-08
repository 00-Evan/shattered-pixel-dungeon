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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.HuntressArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MageArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.RogueArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.WarriorArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
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
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
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
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RoundShield;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RunicBlade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Shortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WarHammer;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Boomerang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class Catalogs {
	
	private static final LinkedHashMap<Class<? extends Item>, Boolean> weapons = new LinkedHashMap<>();
	static {
		weapons.put( WornShortsword.class,  false);
		weapons.put( Knuckles.class,        false);
		weapons.put( Dagger.class,          false);
		weapons.put( MagesStaff.class,      false);
		weapons.put( Boomerang.class,       false);
		weapons.put( Shortsword.class,      false);
		weapons.put( HandAxe.class,         false);
		weapons.put( Spear.class,           false);
		weapons.put( Quarterstaff.class,    false);
		weapons.put( Dirk.class,            false);
		weapons.put( Sword.class,           false);
		weapons.put( Mace.class,            false);
		weapons.put( Scimitar.class,        false);
		weapons.put( RoundShield.class,     false);
		weapons.put( Sai.class,             false);
		weapons.put( Whip.class,            false);
		weapons.put( Longsword.class,       false);
		weapons.put( BattleAxe.class,       false);
		weapons.put( Flail.class,           false);
		weapons.put( RunicBlade.class,      false);
		weapons.put( AssassinsBlade.class,  false);
		weapons.put( Greatsword.class,      false);
		weapons.put( WarHammer.class,       false);
		weapons.put( Glaive.class,          false);
		weapons.put( Greataxe.class,        false);
		weapons.put( Greatshield.class,     false);
	}
	
	public static Collection<Class<? extends Item>> weapons(){
		return weapons.keySet();
	}
	
	public static boolean allWeaponsSeen(){
		for (Boolean val : weapons.values()){
			if (!val) return false;
		}
		return true;
	}
	
	private static final LinkedHashMap<Class<? extends Item>, Boolean> armor = new LinkedHashMap<>();
	static {
		armor.put( ClothArmor.class,       false);
		armor.put( LeatherArmor.class,     false);
		armor.put( MailArmor.class,        false);
		armor.put( ScaleArmor.class,       false);
		armor.put( PlateArmor.class,       false);
		armor.put( WarriorArmor.class,     false);
		armor.put( MageArmor.class,        false);
		armor.put( RogueArmor.class,       false);
		armor.put( HuntressArmor.class,    false);
	}
	
	public static Collection<Class<? extends Item>> armor(){
		return armor.keySet();
	}
	
	public static boolean allArmorSeen(){
		for (Boolean val : armor.values()){
			if (!val) return false;
		}
		return true;
	}
	
	private static final LinkedHashMap<Class<? extends Item>, Boolean> wands = new LinkedHashMap<>();
	static {
		wands.put( WandOfMagicMissile.class,    false);
		wands.put( WandOfLightning.class,       false);
		wands.put( WandOfDisintegration.class,  false);
		wands.put( WandOfFireblast.class,       false);
		wands.put( WandOfVenom.class,           false);
		wands.put( WandOfBlastWave.class,       false);
		//wands.put( WandOfLivingEarth.class,   false);
		wands.put( WandOfFrost.class,           false);
		wands.put( WandOfPrismaticLight.class,  false);
		//wands.put( WandOfWarding.class,       false);
		wands.put( WandOfTransfusion.class,     false);
		wands.put( WandOfCorruption.class,      false);
		wands.put( WandOfRegrowth.class,        false);
	}
	
	public static Collection<Class<? extends Item>> wands(){
		return wands.keySet();
	}
	
	public static boolean allWandsSeen(){
		for (Boolean val : wands.values()){
			if (!val) return false;
		}
		return true;
	}
	
	private static final LinkedHashMap<Class<? extends Item>, Boolean> rings = new LinkedHashMap<>();
	static {
		rings.put( RingOfAccuracy.class,        false);
		rings.put( RingOfElements.class,        false);
		rings.put( RingOfEvasion.class,         false);
		rings.put( RingOfForce.class,           false);
		rings.put( RingOfFuror.class,           false);
		rings.put( RingOfHaste.class,           false);
		//rings.put( RingOfMagic.class,         false);
		rings.put( RingOfMight.class,           false);
		rings.put( RingOfSharpshooting.class,   false);
		rings.put( RingOfTenacity.class,        false);
		rings.put( RingOfWealth.class,          false);
	}
	
	public static Collection<Class<? extends Item>> rings(){
		return rings.keySet();
	}
	
	public static boolean allRingsSeen(){
		for (Boolean val : rings.values()){
			if (!val) return false;
		}
		return true;
	}
	
	private static final LinkedHashMap<Class<? extends Item>, Boolean> artifacts = new LinkedHashMap<>();
	static{
		artifacts.put( AlchemistsToolkit.class,     false);
		artifacts.put( CapeOfThorns.class,          false);
		artifacts.put( ChaliceOfBlood.class,        false);
		artifacts.put( CloakOfShadows.class,        false);
		artifacts.put( DriedRose.class,             false);
		artifacts.put( EtherealChains.class,        false);
		artifacts.put( HornOfPlenty.class,          false);
		artifacts.put( LloydsBeacon.class,          false);
		artifacts.put( MasterThievesArmband.class,  false);
		artifacts.put( SandalsOfNature.class,       false);
		artifacts.put( TalismanOfForesight.class,   false);
		artifacts.put( TimekeepersHourglass.class,  false);
		artifacts.put(UnstableSpellbook.class,      false);
	}
	
	public static Collection<Class<? extends Item>> artifacts(){
		return artifacts.keySet();
	}
	
	public static boolean allArtifactsSeen(){
		for (Boolean val : artifacts.values()){
			if (!val) return false;
		}
		return true;
	}
	
	private static final LinkedHashMap<Class<? extends Item>, Boolean> potions = new LinkedHashMap<>();
	static {
		potions.put( PotionOfHealing.class,         false);
		potions.put( PotionOfStrength.class,        false);
		potions.put( PotionOfLiquidFlame.class,     false);
		potions.put( PotionOfFrost.class,           false);
		potions.put( PotionOfToxicGas.class,        false);
		potions.put( PotionOfParalyticGas.class,    false);
		potions.put( PotionOfPurity.class,          false);
		potions.put( PotionOfLevitation.class,      false);
		potions.put( PotionOfMindVision.class,      false);
		potions.put( PotionOfInvisibility.class,    false);
		potions.put( PotionOfExperience.class,      false);
		potions.put( PotionOfMight.class,           false);
	}
	
	public static Collection<Class<? extends Item>> potions(){
		return potions.keySet();
	}
	
	public static boolean allPotionsSeen(){
		for (Boolean val : potions.values()){
			if (!val) return false;
		}
		return true;
	}

	private static final LinkedHashMap<Class<? extends Item>, Boolean> scrolls = new LinkedHashMap<>();
	static {
		scrolls.put( ScrollOfIdentify.class,        false);
		scrolls.put( ScrollOfUpgrade.class,         false);
		scrolls.put( ScrollOfRemoveCurse.class,     false);
		scrolls.put( ScrollOfMagicMapping.class,    false);
		scrolls.put( ScrollOfTeleportation.class,   false);
		scrolls.put( ScrollOfRecharging.class,      false);
		scrolls.put( ScrollOfMirrorImage.class,     false);
		scrolls.put( ScrollOfTerror.class,          false);
		scrolls.put( ScrollOfLullaby.class,         false);
		scrolls.put( ScrollOfRage.class,            false);
		scrolls.put( ScrollOfPsionicBlast.class,    false);
		scrolls.put( ScrollOfMagicalInfusion.class, false);
	}
	
	public static Collection<Class<? extends Item>> scrolls(){
		return scrolls.keySet();
	}
	
	public static boolean allScrollsSeen(){
		for (Boolean val : scrolls.values()){
			if (!val) return false;
		}
		return true;
	}
	
	private static final ArrayList<LinkedHashMap<Class<? extends Item>, Boolean>> allCatalogs = new ArrayList<>();
	static{
		allCatalogs.add(weapons);
		allCatalogs.add(armor);
		allCatalogs.add(wands);
		allCatalogs.add(rings);
		allCatalogs.add(artifacts);
		allCatalogs.add(potions);
		allCatalogs.add(scrolls);
	}
	
	public static boolean isSeen(Class<? extends Item> itemClass){
		for (LinkedHashMap<Class<? extends Item>, Boolean> catalog : allCatalogs) {
			if (catalog.containsKey(itemClass)) {
				return catalog.get(itemClass);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<? extends Item> itemClass){
		for (LinkedHashMap<Class<? extends Item>, Boolean> catalog : allCatalogs) {
			if (catalog.containsKey(itemClass)) {
				catalog.put(itemClass, true);
			}
		}
		Badges.validateItemsIdentified();
	}
	
}
