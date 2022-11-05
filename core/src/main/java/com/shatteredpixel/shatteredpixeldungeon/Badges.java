/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.CollectionUtils;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.HUNTRESS;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.MAGE;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.ROGUE;
import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.WARRIOR;
import static com.shatteredpixel.shatteredpixeldungeon.utils.CollectionUtils.allMatch;
import static com.shatteredpixel.shatteredpixeldungeon.utils.CollectionUtils.filter;
import static com.shatteredpixel.shatteredpixeldungeon.utils.CollectionUtils.getFirst;

public class Badges {

	public enum Badge {
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		FOUND_RATMOGRIFY,

		//bronze
		UNLOCK_MAGE                 ( 1 ),
		UNLOCK_ROGUE                ( 2 ),
		UNLOCK_HUNTRESS             ( 3 ),
		MONSTERS_SLAIN_1            ( 4 ),
		MONSTERS_SLAIN_2            ( 5 ),
		GOLD_COLLECTED_1            ( 6 ),
		GOLD_COLLECTED_2            ( 7 ),
		ITEM_LEVEL_1                ( 8 ),
		LEVEL_REACHED_1             ( 9 ),
		STRENGTH_ATTAINED_1         ( 10 ),
		FOOD_EATEN_1                ( 11 ),
		ITEMS_CRAFTED_1             ( 12 ),
		BOSS_SLAIN_1                ( 13 ),
		DEATH_FROM_FIRE             ( 14, Badges::validateDeath ),
		DEATH_FROM_POISON           ( 15, Badges::validateDeath ),
		DEATH_FROM_GAS              ( 16, Badges::validateDeath ),
		DEATH_FROM_HUNGER           ( 17, Badges::validateDeath ),
		DEATH_FROM_FALLING          ( 18, Badges::validateDeath ),
		GAMES_PLAYED_1              ( 19, true ),
		HIGH_SCORE_1                ( 20 ),

		//silver
		NO_MONSTERS_SLAIN           ( 32 ),
		MONSTERS_SLAIN_3            ( 33 ),
		MONSTERS_SLAIN_4            ( 34 ),
		GOLD_COLLECTED_3            ( 35 ),
		GOLD_COLLECTED_4            ( 36 ),
		ITEM_LEVEL_2                ( 37 ),
		ITEM_LEVEL_3                ( 38 ),
		LEVEL_REACHED_2             ( 39 ),
		LEVEL_REACHED_3             ( 40 ),
		STRENGTH_ATTAINED_2         ( 41 ),
		STRENGTH_ATTAINED_3         ( 42 ),
		FOOD_EATEN_2                ( 43 ),
		FOOD_EATEN_3                ( 44 ),
		ITEMS_CRAFTED_2             ( 45 ),
		ITEMS_CRAFTED_3             ( 46 ),
		BOSS_SLAIN_2                ( 47 ),
		BOSS_SLAIN_3                ( 48 ),
		ALL_POTIONS_IDENTIFIED      ( 49 ),
		ALL_SCROLLS_IDENTIFIED      ( 50 ),
		DEATH_FROM_ENEMY_MAGIC      ( 51, Badges::validateDeath ),
		DEATH_FROM_FRIENDLY_MAGIC   ( 52, Badges::validateDeath ),
		DEATH_FROM_SACRIFICE        ( 53, Badges::validateDeath ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,
		BOSS_SLAIN_1_ALL_CLASSES    ( 54, true ),
		GAMES_PLAYED_2              ( 55, true ),
		HIGH_SCORE_2                ( 56 ),

		//gold
		PIRANHAS                    ( 64 ),
		GRIM_WEAPON                 ( 65 ),
		BAG_BOUGHT_VELVET_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_MAGICAL_HOLSTER,
		ALL_BAGS_BOUGHT             ( 66 ),
		MASTERY_COMBO               ( 67 ),
		MONSTERS_SLAIN_5            ( 68 ),
		GOLD_COLLECTED_5            ( 69 ),
		ITEM_LEVEL_4                ( 70 ),
		LEVEL_REACHED_4             ( 71 ),
		STRENGTH_ATTAINED_4         ( 72 ),
		STRENGTH_ATTAINED_5         ( 73 ),
		FOOD_EATEN_4                ( 74 ),
		FOOD_EATEN_5                ( 75 ),
		ITEMS_CRAFTED_4             ( 76 ),
		ITEMS_CRAFTED_5             ( 77 ),
		BOSS_SLAIN_4                ( 78 ),
		ALL_RINGS_IDENTIFIED        ( 79 ),
		ALL_ARTIFACTS_IDENTIFIED    ( 80 ),
		DEATH_FROM_GRIM_TRAP        ( 81, Badges::validateDeath ),
		VICTORY                     ( 82 ),
		BOSS_CHALLENGE_1            ( 83 ),
		BOSS_CHALLENGE_2            ( 84 ),
		GAMES_PLAYED_3              ( 85, true ),
		HIGH_SCORE_3                ( 86 ),

		//platinum
		ITEM_LEVEL_5                ( 96 ),
		LEVEL_REACHED_5             ( 97 ),
		HAPPY_END                   ( 98 ),
		ALL_WEAPONS_IDENTIFIED      ( 99 ),
		ALL_ARMOR_IDENTIFIED        ( 100 ),
		ALL_WANDS_IDENTIFIED        ( 101 ),
		ALL_ITEMS_IDENTIFIED        ( 102, true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY_ALL_CLASSES         ( 103, true ),
		DEATH_FROM_ALL              ( 104, true ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_BERSERKER,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_ASSASSIN,
		BOSS_SLAIN_3_SNIPER,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_ALL_SUBCLASSES ( 105, true ),
		BOSS_CHALLENGE_3            ( 106 ),
		BOSS_CHALLENGE_4            ( 107 ),
		GAMES_PLAYED_4              ( 108, true ),
		HIGH_SCORE_4                ( 109 ),
		CHAMPION_1                  ( 110 ),

		//diamond
		BOSS_CHALLENGE_5            ( 120 ),
		GAMES_PLAYED_5              ( 121, true ),
		HIGH_SCORE_5                ( 122 ),
		CHAMPION_2                  ( 123 ),
		CHAMPION_3                  ( 124 );

		public final boolean meta;

		public final int image;

		final Runnable validator;

		Badge( int image ) {
			this( image, false );
		}
		Badge( int image, Function<Badge, Runnable> validatorProvider ) {
			this( image, false, validatorProvider );
		}

		Badge( int image, boolean meta ) {
			this(image, false, null);
		}

		Badge( int image, boolean meta, Function<Badge, Runnable> validatorProvider ) {
			this.image = image;
			this.meta = meta;
			this.validator = validatorProvider != null
					? validatorProvider.apply(this) : () -> Badges.validate(this);
		}

		public String title(){
			return Messages.get(this, name()+".title");
		}

		public String desc(){
			return Messages.get(this, name()+".desc");
		}

		public void validate() {
			validator.run();
		}

		Badge() {
			this( -1 );
		}
	}

	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<>();

	private static boolean saveNeeded = false;

	public static void reset() {
		local.clear();
		loadGlobal();
	}

	public static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";

	private static final HashSet<String> removedBadges = new HashSet<>();
	static{
		//v1.3.0 (These were removed and re-added internally as new unlock reqs were added)
		removedBadges.add("YASD");
		removedBadges.add("DEATH_FROM_GLYPH");
	}

	private static final HashMap<String, String> renamedBadges = new HashMap<>();
	static{
		//v1.1.0 (some names were from before 1.1.0, but conversion was added then)
		renamedBadges.put("BAG_BOUGHT_SEED_POUCH",      "BAG_BOUGHT_VELVET_POUCH");
		renamedBadges.put("BAG_BOUGHT_WAND_HOLSTER",    "BAG_BOUGHT_MAGICAL_HOLSTER");

		renamedBadges.put("POTIONS_COOKED_1", "ITEMS_CRAFTED_1");
		renamedBadges.put("POTIONS_COOKED_2", "ITEMS_CRAFTED_2");
		renamedBadges.put("POTIONS_COOKED_3", "ITEMS_CRAFTED_3");
		renamedBadges.put("POTIONS_COOKED_4", "ITEMS_CRAFTED_4");
	}

	public static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<>();
		if (bundle == null) return badges;

		String[] names = bundle.getStringArray( BADGES );
		if (names == null) return badges;

		for (int i=0; i < names.length; i++) {
			try {
				if (renamedBadges.containsKey(names[i])){
					names[i] = renamedBadges.get(names[i]);
				}
				if (!removedBadges.contains(names[i])){
					badges.add( Badge.valueOf( names[i] ) );
				}
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}

		addReplacedBadges(badges);

		return badges;
	}

	public static void store( Bundle bundle, HashSet<Badge> badges ) {
		addReplacedBadges(badges);

		int count = 0;
		String names[] = new String[badges.size()];

		for (Badge badge:badges) {
			names[count++] = badge.name();
		}
		bundle.put( BADGES, names );
	}

	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}

	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}

	public static void loadGlobal() {
		if (global == null) {
			try {
				Bundle bundle = FileUtils.bundleFromFile( BADGES_FILE );
				global = restore( bundle );

			} catch (IOException e) {
				global = new HashSet<>();
			}
		}
	}

	public static void saveGlobal() {
		if (saveNeeded) {

			Bundle bundle = new Bundle();
			store( bundle, global );

			try {
				FileUtils.bundleToFile(BADGES_FILE, bundle);
				saveNeeded = false;
			} catch (IOException e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
	}

	public static int totalUnlocked(boolean global){
		if (global) return Badges.global.size();
		else        return Badges.local.size();
	}

	public static void validateLevels(Map<Badge, Integer> levels, int value) {
		Badge badge = null;
		for (Map.Entry<Badge, Integer> level : levels.entrySet()) {
			if (!local.contains( level.getKey() ) && value >= level.getValue()) {
				if (badge != null) unlock(badge);
				badge = level.getKey();
				local.add( badge );
			}
		}

		displayBadge( badge );
	}

	static final Map<Badge, Integer> monsterSlains = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.MONSTERS_SLAIN_1, 10)
			.put(Badge.MONSTERS_SLAIN_2, 50)
			.put(Badge.MONSTERS_SLAIN_3, 100)
			.put(Badge.MONSTERS_SLAIN_4, 250)
			.put(Badge.MONSTERS_SLAIN_5, 500)
			.build();

	public static void validateMonstersSlain() {
		validateLevels(monsterSlains, Statistics.enemiesSlain);
	}

	static final Map<Badge, Integer> goldCollected = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.GOLD_COLLECTED_1, 250)
			.put(Badge.GOLD_COLLECTED_2, 1000)
			.put(Badge.GOLD_COLLECTED_3, 2500)
			.put(Badge.GOLD_COLLECTED_4, 7500)
			.put(Badge.GOLD_COLLECTED_5, 15_000)
			.build();

	public static void validateGoldCollected() {
		validateLevels(goldCollected, Statistics.goldCollected);
	}

	static final Map<Badge, Integer> levelReached = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.LEVEL_REACHED_1, 6)
			.put(Badge.LEVEL_REACHED_2, 12)
			.put(Badge.LEVEL_REACHED_3, 18)
			.put(Badge.LEVEL_REACHED_4, 24)
			.put(Badge.LEVEL_REACHED_5, 30)
			.build();

	public static void validateLevelReached() {
		validateLevels(levelReached, Dungeon.hero.lvl);
	}

	static final Map<Badge, Integer> strengthAttained = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.STRENGTH_ATTAINED_1, 12)
			.put(Badge.STRENGTH_ATTAINED_2, 14)
			.put(Badge.STRENGTH_ATTAINED_3, 16)
			.put(Badge.STRENGTH_ATTAINED_4, 18)
			.put(Badge.STRENGTH_ATTAINED_5, 20)
			.build();

	public static void validateStrengthAttained() {
		validateLevels(strengthAttained, Dungeon.hero.STR);
	}

	static final Map<Badge, Integer> foodEaten = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.STRENGTH_ATTAINED_1, 12)
			.put(Badge.STRENGTH_ATTAINED_2, 14)
			.put(Badge.STRENGTH_ATTAINED_3, 16)
			.put(Badge.STRENGTH_ATTAINED_4, 18)
			.put(Badge.STRENGTH_ATTAINED_5, 20)
			.build();

	public static void validateFoodEaten() {
		validateLevels(foodEaten, Statistics.foodEaten);
	}

	static final Map<Badge, Integer> itemsCrafted = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.ITEMS_CRAFTED_1, 3)
			.put(Badge.ITEMS_CRAFTED_2, 8)
			.put(Badge.ITEMS_CRAFTED_3, 15)
			.put(Badge.ITEMS_CRAFTED_4, 24)
			.put(Badge.ITEMS_CRAFTED_5, 35)
			.build();

	public static void validateItemsCrafted() {
		validateLevels(itemsCrafted, Statistics.itemsCrafted);
	}

	public static void validatePiranhasKilled() {
		maybeDisplayBadge(Badge.PIRANHAS, b -> Statistics.piranhasKilled >= 6);
	}

	static final Map<Badge, Integer> itemLevelAcquired = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.ITEM_LEVEL_1, 3)
			.put(Badge.ITEM_LEVEL_2, 6)
			.put(Badge.ITEM_LEVEL_3, 9)
			.put(Badge.ITEM_LEVEL_4, 12)
			.put(Badge.ITEM_LEVEL_5, 15)
			.build();

	public static void validateItemLevelAquired(Item item ) {

		// This method should be called:
		// 1) When an item is obtained (Item.collect)
		// 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item is identified

		// Note that artifacts should never trigger this badge as they are alternatively upgraded
		if (!item.levelKnown || item instanceof Artifact) {
			return;
		}

		validateLevels(itemLevelAcquired, item.level());
	}

	/**
	 * Map of bags enum values to classes representing these bags.
	 */
	static final Map<Badge, Class> bags = CollectionUtils.<Badge, Class>linkedMapBuilder()
			.put(Badge.BAG_BOUGHT_VELVET_POUCH, VelvetPouch.class)
			.put(Badge.BAG_BOUGHT_SCROLL_HOLDER, ScrollHolder.class)
			.put(Badge.BAG_BOUGHT_POTION_BANDOLIER, PotionBandolier.class)
			.put(Badge.BAG_BOUGHT_MAGICAL_HOLSTER, MagicalHolster.class)
			.build();


	public static void validateAllBagsBought( Item bag ) {
		Map.Entry<Badge, Class> badge =
				getFirst(bags.entrySet(), bagEntry -> bagEntry.getValue().isAssignableFrom(bag.getClass()));

		if (badge != null) {
			local.add( badge.getKey() );
			maybeDisplayBadge(Badge.ALL_BAGS_BOUGHT, b -> allMatch(bags.keySet(), local::contains));
		}
	}

	public static void validateItemsIdentified() {
		filter(Catalog.catalogs(), Catalog::allSeen)
				.forEach(cat -> maybeDisplayBadge(Catalog.catalogBadges.get(cat), b -> !isUnlocked(b)));

		if (isUnlocked( Badge.ALL_WEAPONS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_ARMOR_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_WANDS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_RINGS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_ARTIFACTS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_POTIONS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_SCROLLS_IDENTIFIED )) {

			Badge badge = Badge.ALL_ITEMS_IDENTIFIED;
			if (!isUnlocked( badge )) {
				displayBadge( badge );
			}
		}
	}

	public static void validate(Badge badge) {
		local.add( badge );
		displayBadge( badge );
	}

	public static Runnable validateDeath(Badge badge) {
		return () -> {
			validate(badge);
			validateDeathFromAll();
		};
	}

	private static void validateDeathFromAll() {
		if (isUnlocked( Badge.DEATH_FROM_FIRE ) &&
				isUnlocked( Badge.DEATH_FROM_POISON ) &&
				isUnlocked( Badge.DEATH_FROM_GAS ) &&
				isUnlocked( Badge.DEATH_FROM_HUNGER) &&
				isUnlocked( Badge.DEATH_FROM_FALLING) &&
				isUnlocked( Badge.DEATH_FROM_ENEMY_MAGIC) &&
				isUnlocked( Badge.DEATH_FROM_FRIENDLY_MAGIC) &&
				isUnlocked( Badge.DEATH_FROM_SACRIFICE) &&
				isUnlocked( Badge.DEATH_FROM_GRIM_TRAP)) {

			Badge badge = Badge.DEATH_FROM_ALL;
			if (!isUnlocked( badge )) {
				displayBadge( badge );
			}
		}
	}

	private static LinkedHashMap<HeroClass, Badge> firstBossClassBadges = new LinkedHashMap<>();
	static {
		firstBossClassBadges.put(WARRIOR, Badge.BOSS_SLAIN_1_WARRIOR);
		firstBossClassBadges.put(MAGE, Badge.BOSS_SLAIN_1_MAGE);
		firstBossClassBadges.put(ROGUE, Badge.BOSS_SLAIN_1_ROGUE);
		firstBossClassBadges.put(HUNTRESS, Badge.BOSS_SLAIN_1_HUNTRESS);
	}

	private static LinkedHashMap<HeroClass, Badge> victoryClassBadges = new LinkedHashMap<>();
	static {
		victoryClassBadges.put(WARRIOR, Badge.VICTORY_WARRIOR);
		victoryClassBadges.put(MAGE, Badge.VICTORY_MAGE);
		victoryClassBadges.put(ROGUE, Badge.VICTORY_ROGUE);
		victoryClassBadges.put(HUNTRESS, Badge.VICTORY_HUNTRESS);
	}

	private static LinkedHashMap<HeroSubClass, Badge> thirdBossSubclassBadges = new LinkedHashMap<>();
	static {
		thirdBossSubclassBadges.put(HeroSubClass.BERSERKER, Badge.BOSS_SLAIN_3_BERSERKER);
		thirdBossSubclassBadges.put(HeroSubClass.GLADIATOR, Badge.BOSS_SLAIN_3_GLADIATOR);
		thirdBossSubclassBadges.put(HeroSubClass.BATTLEMAGE, Badge.BOSS_SLAIN_3_BATTLEMAGE);
		thirdBossSubclassBadges.put(HeroSubClass.WARLOCK, Badge.BOSS_SLAIN_3_WARLOCK);
		thirdBossSubclassBadges.put(HeroSubClass.ASSASSIN, Badge.BOSS_SLAIN_3_ASSASSIN);
		thirdBossSubclassBadges.put(HeroSubClass.FREERUNNER, Badge.BOSS_SLAIN_3_FREERUNNER);
		thirdBossSubclassBadges.put(HeroSubClass.SNIPER, Badge.BOSS_SLAIN_3_SNIPER);
		thirdBossSubclassBadges.put(HeroSubClass.WARDEN, Badge.BOSS_SLAIN_3_WARDEN);
	}

	static final Map<Integer, Badge> bossSlains = CollectionUtils.<Integer, Badge>mapBuilder()
			.put(5, Badge.BOSS_SLAIN_1)
			.put(10, Badge.BOSS_SLAIN_2)
			.put(15, Badge.BOSS_SLAIN_3)
			.put(20, Badge.BOSS_SLAIN_4)
			.build();

	public static void validateBossSlain() {
		Badge badge = bossSlains.get(Dungeon.depth);

		if (badge != null) {
			local.add( badge );
			displayBadge( badge );

			if (badge == Badge.BOSS_SLAIN_1) {
				badge = firstBossClassBadges.get(Dungeon.hero.heroClass);
				if (badge == null) return;
				local.add( badge );
				unlock(badge);

				boolean allUnlocked = allMatch(firstBossClassBadges.values(), Badges::isUnlocked);
				if (allUnlocked) {
					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			} else if (badge == Badge.BOSS_SLAIN_3) {
				badge = thirdBossSubclassBadges.get(Dungeon.hero.subClass);
				if (badge == null) return;
				local.add( badge );
				unlock(badge);

				boolean allUnlocked = allMatch(thirdBossSubclassBadges.values(), Badges::isUnlocked);
				if (allUnlocked) {
					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			}
		}
	}

	static final Map<Integer, Badge> bossChallenges = CollectionUtils.<Integer, Badge>mapBuilder()
			.put(5, Badge.BOSS_CHALLENGE_1)
			.put(10, Badge.BOSS_CHALLENGE_2)
			.put(15, Badge.BOSS_CHALLENGE_3)
			.put(20, Badge.BOSS_CHALLENGE_4)
			.put(25, Badge.BOSS_CHALLENGE_4)
			.build();

	public static void validateBossChallengeCompleted(){
		Badge badge = bossChallenges.get(Dungeon.depth);

		if (badge != null) {
			local.add(badge);
			displayBadge(badge);
		}
	}

	static final Map<HeroClass, Badge> masteryMap = CollectionUtils.<HeroClass, Badge>mapBuilder()
			.put(WARRIOR, Badge.MASTERY_WARRIOR)
			.put(MAGE, Badge.MASTERY_MAGE)
			.put(ROGUE, Badge.MASTERY_ROGUE)
			.put(HUNTRESS, Badge.MASTERY_HUNTRESS)
			.build();

	public static void validateMastery() {
		unlock(masteryMap.get(Dungeon.hero.heroClass));
	}

	public static void validateRatmogrify() {
		unlock(Badge.FOUND_RATMOGRIFY);
	}

	public static void validateMageUnlock(){
		if (Statistics.upgradesUsed >= 1 && !isUnlocked(Badge.UNLOCK_MAGE)){
			displayBadge( Badge.UNLOCK_MAGE );
		}
	}

	public static void validateRogueUnlock(){
		if (Statistics.sneakAttacks >= 10 && !isUnlocked(Badge.UNLOCK_ROGUE)){
			displayBadge( Badge.UNLOCK_ROGUE );
		}
	}

	public static void validateHuntressUnlock(){
		if (Statistics.thrownAttacks >= 10 && !isUnlocked(Badge.UNLOCK_HUNTRESS)){
			displayBadge( Badge.UNLOCK_HUNTRESS );
		}
	}

	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 10) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateVictory() {

		maybeDisplayBadge(Badge.VICTORY, null);

		Badge badge = victoryClassBadges.get(Dungeon.hero.heroClass);
		if (badge == null) return;
		local.add( badge );
		unlock(badge);

		boolean allUnlocked = allMatch(victoryClassBadges.values(), Badges::isUnlocked);
		// fixme: is it correct that it will additionally do local.add(badge)?
		maybeDisplayBadge(Badge.VICTORY_ALL_CLASSES, b -> allUnlocked);
	}

	/** Add badge and display if condition is met (or absent). */
	static void maybeDisplayBadge(Badge badge, Predicate<Badge> condition) {
		if (!local.contains( badge ) && (condition == null || condition.test(badge))) {
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateNoKilling() {
		maybeDisplayBadge(Badge.NO_MONSTERS_SLAIN, b -> Statistics.completedWithNoKilling);
	}

	public static void validateGrimWeapon() {
		maybeDisplayBadge(Badge.GRIM_WEAPON, null);
	}

	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10 || Rankings.INSTANCE.wonNumber >= 1) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 25 || Rankings.INSTANCE.wonNumber >= 3) {
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 50 || Rankings.INSTANCE.wonNumber >= 5) {
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 200 || Rankings.INSTANCE.wonNumber >= 10) {
			badge = Badge.GAMES_PLAYED_4;
		}
		if (Rankings.INSTANCE.totalNumber >= 1000 || Rankings.INSTANCE.wonNumber >= 25) {
			badge = Badge.GAMES_PLAYED_5;
		}

		displayBadge( badge );
	}

	static final Map<Badge, Integer> highScore = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.HIGH_SCORE_1, 5000)
			.put(Badge.HIGH_SCORE_2, 25_000)
			.put(Badge.HIGH_SCORE_3, 100_000)
			.put(Badge.HIGH_SCORE_4, 250_000)
			.put(Badge.HIGH_SCORE_5, 1_000_000)
			.build();

	public static void validateHighScore( int score ) {
		validateLevels(highScore, score);
	}

	//necessary in order to display the happy end badge in the surface scene
	public static void silentValidateHappyEnd() {
		if (!local.contains( Badge.HAPPY_END )){
			local.add( Badge.HAPPY_END );
		}
	}

	public static void validateHappyEnd() {
		displayBadge( Badge.HAPPY_END );
	}

	static final Map<Badge, Integer> champion = CollectionUtils.<Badge, Integer>linkedMapBuilder()
			.put(Badge.CHAMPION_1, 1)
			.put(Badge.CHAMPION_2, 3)
			.put(Badge.CHAMPION_3, 6)
			.build();

	public static void validateChampion( int challenges ) {
		validateLevels(champion, challenges);
	}

	private static void displayBadge( Badge badge ) {

		if (badge == null || !Dungeon.customSeedText.isEmpty()) {
			return;
		}

		if (isUnlocked( badge )) {

			if (!badge.meta) {
				GLog.h( Messages.get(Badges.class, "endorsed", badge.title()) );
				GLog.newLine();
			}

		} else {

			unlock(badge);

			GLog.h( Messages.get(Badges.class, "new", badge.title() + " (" + badge.desc() + ")") );
			GLog.newLine();
			PixelScene.showBadge( badge );
		}
	}

	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}

	public static HashSet<Badge> allUnlocked(){
		loadGlobal();
		return new HashSet<>(global);
	}

	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}

	public static void unlock( Badge badge ){
		if (!isUnlocked(badge) && Dungeon.customSeedText.isEmpty()){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static List<Badge> filterReplacedBadges( boolean global ) {
		List<Badge> badges = filter(
				global ? Badges.global : Badges.local,
				badge -> !((!global && badge.meta) || badge.image == -1));

		Collections.sort(badges);

		return filterReplacedBadges(badges);
	}

	//only show the highest unlocked and the lowest locked
	private static final Badge[][] tierBadgeReplacements = new Badge[][]{
			{Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4, Badge.MONSTERS_SLAIN_5},
			{Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5},
			{Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4, Badge.ITEM_LEVEL_5},
			{Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4, Badge.LEVEL_REACHED_5},
			{Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4, Badge.STRENGTH_ATTAINED_5},
			{Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4, Badge.FOOD_EATEN_5},
			{Badge.ITEMS_CRAFTED_1, Badge.ITEMS_CRAFTED_2, Badge.ITEMS_CRAFTED_3, Badge.ITEMS_CRAFTED_4, Badge.ITEMS_CRAFTED_5},
			{Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4},
			{Badge.HIGH_SCORE_1, Badge.HIGH_SCORE_2, Badge.HIGH_SCORE_3, Badge.HIGH_SCORE_4, Badge.HIGH_SCORE_5},
			{Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4, Badge.GAMES_PLAYED_5},
			{Badge.CHAMPION_1, Badge.CHAMPION_2, Badge.CHAMPION_3}
	};

	//don't show the later badge if the earlier one isn't unlocked
	private static final Badge[][] prerequisiteBadges = new Badge[][]{
			{Badge.BOSS_SLAIN_1, Badge.BOSS_CHALLENGE_1},
			{Badge.BOSS_SLAIN_2, Badge.BOSS_CHALLENGE_2},
			{Badge.BOSS_SLAIN_3, Badge.BOSS_CHALLENGE_3},
			{Badge.BOSS_SLAIN_4, Badge.BOSS_CHALLENGE_4},
			{Badge.VICTORY,      Badge.BOSS_CHALLENGE_5},
	};

	//If the summary badge is unlocked, don't show the component badges
	private static final Badge[][] summaryBadgeReplacements = new Badge[][]{
			{Badge.DEATH_FROM_FIRE, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_GAS, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_HUNGER, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_POISON, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_FALLING, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_ENEMY_MAGIC, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_FRIENDLY_MAGIC, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_SACRIFICE, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_GRIM_TRAP, Badge.DEATH_FROM_ALL},

			{Badge.ALL_WEAPONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARMOR_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARTIFACTS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED}
	};

	public static List<Badge> filterReplacedBadges( List<Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveBest( badges, tierReplace );
		}

		for (Badge[] metaReplace : summaryBadgeReplacements){
			leaveBest( badges, metaReplace );
		}

		return badges;
	}

	private static void leaveBest( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static List<Badge> filterBadgesWithoutPrerequisites(List<Badges.Badge> badges ) {

		for (Badge[] prereqReplace : prerequisiteBadges){
			leaveWorst( badges, prereqReplace );
		}

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
		for (int i=0; i < badges.length; i++) {
			if (list.contains( badges[i])) {
				for (int j=i+1; j < badges.length; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			addLower( badges, tierReplace );
		}

		for (Badge[] metaReplace : summaryBadgeReplacements){
			addLower( badges, metaReplace );
		}

		return badges;
	}

	private static void addLower( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.add( badges[j] );
				}
				break;
			}
		}
	}

	//used for badges with completion progress that would otherwise be hard to track
	public static String showCompletionProgress( Badge badge ){
		if (isUnlocked(badge)) return null;

		String result = "\n";

		if (badge == Badge.BOSS_SLAIN_1_ALL_CLASSES){
			for (HeroClass cls : HeroClass.values()){
				result += "\n";
				if (isUnlocked(firstBossClassBadges.get(cls)))  result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                            result += Messages.titleCase(cls.title());
			}

			return result;

		} else if (badge == Badge.VICTORY_ALL_CLASSES) {

			for (HeroClass cls : HeroClass.values()){
				result += "\n";
				if (isUnlocked(victoryClassBadges.get(cls)))    result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                            result += Messages.titleCase(cls.title());
			}

			return result;

		} else if (badge == Badge.BOSS_SLAIN_3_ALL_SUBCLASSES){

			for (HeroSubClass cls : HeroSubClass.values()){
				if (cls == HeroSubClass.NONE) continue;
				result += "\n";
				if (isUnlocked(thirdBossSubclassBadges.get(cls)))   result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                                result += Messages.titleCase(cls.title()) ;
			}

			return result;
		}

		return null;
	}
}
