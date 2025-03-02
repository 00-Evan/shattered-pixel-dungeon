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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.DivineSense;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfRegrowth;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfWarding;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.HallsLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.LastLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.PrisonLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

public class Dungeon {

	//enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public static enum LimitedDrops {
		//limited world drops
		STRENGTH_POTIONS,
		UPGRADE_SCROLLS,
		ARCANE_STYLI,
		ENCH_STONE,
		INT_STONE,
		TRINKET_CATA,
		LAB_ROOM, //actually a room, but logic is the same

		//Health potion sources
		//enemies
		SWARM_HP,
		NECRO_HP,
		BAT_HP,
		WARLOCK_HP,
		//Demon spawners are already limited in their spawnrate, no need to limit their health drops
		//alchemy
		COOKING_HP,
		BLANDFRUIT_SEED,

		//Other limited enemy drops
		SLIME_WEP,
		SKELE_WEP,
		THEIF_MISC,
		GUARD_ARM,
		SHAMAN_WAND,
		DM200_EQUIP,
		GOLEM_EQUIP,

		//containers
		VELVET_POUCH,
		SCROLL_HOLDER,
		POTION_BANDOLIER,
		MAGICAL_HOLSTER,

		//lore documents
		LORE_SEWERS,
		LORE_PRISON,
		LORE_CAVES,
		LORE_CITY,
		LORE_HALLS;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		public boolean dropped(){
			return count != 0;
		}
		public void drop(){
			count = 1;
		}

		public static void reset(){
			for (LimitedDrops lim : values()){
				lim.count = 0;
			}
		}

		public static void store( Bundle bundle ){
			for (LimitedDrops lim : values()){
				bundle.put(lim.name(), lim.count);
			}
		}

		public static void restore( Bundle bundle ){
			for (LimitedDrops lim : values()){
				if (bundle.contains(lim.name())){
					lim.count = bundle.getInt(lim.name());
				} else {
					lim.count = 0;
				}
				
			}
		}

	}

	public static int challenges;
	public static int mobsToChampion;

	public static Hero hero;
	public static Level level;

	public static QuickSlot quickslot = new QuickSlot();
	
	public static int depth;
	//determines path the hero is on. Current uses:
	// 0 is the default path
	// 1 is for quest sub-floors
	public static int branch;

	//keeps track of what levels the game should try to load instead of creating fresh
	public static ArrayList<Integer> generatedLevels = new ArrayList<>();

	public static int gold;
	public static int energy;
	
	public static HashSet<Integer> chapters;

	public static SparseArray<ArrayList<Item>> droppedItems;

	//first variable is only assigned when game is started, second is updated every time game is saved
	public static int initialVersion;
	public static int version;

	public static boolean daily;
	public static boolean dailyReplay;
	public static String customSeedText = "";
	public static long seed;
	public static long lastPlayed;

	//we initialize the seed separately so that things like interlevelscene can access it early
	public static void initSeed(){
		if (daily) {
			//Ensures that daily seeds are not in the range of user-enterable seeds
			seed = SPDSettings.lastDaily() + DungeonSeed.TOTAL_SEEDS;
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			customSeedText = format.format(new Date(SPDSettings.lastDaily()));
		} else if (!SPDSettings.customSeed().isEmpty()){
			customSeedText = SPDSettings.customSeed();
			seed = DungeonSeed.convertFromText(customSeedText);
		} else {
			customSeedText = "";
			seed = DungeonSeed.randomSeed();
		}
	}
	
	public static void init() {

		initialVersion = version = Game.versionCode;
		challenges = SPDSettings.challenges();
		mobsToChampion = -1;

		Actor.clear();
		Actor.resetNextID();

		//offset seed slightly to avoid output patterns
		Random.pushGenerator( seed+1 );

			Scroll.initLabels();
			Potion.initColors();
			Ring.initGems();

			SpecialRoom.initForRun();
			SecretRoom.initForRun();

			Generator.fullReset();

		Random.resetGenerators();
		
		Statistics.reset();
		Notes.reset();

		quickslot.reset();
		QuickSlotButton.reset();
		Toolbar.swappedQuickslots = false;
		
		depth = 1;
		branch = 0;
		generatedLevels.clear();

		gold = 0;
		energy = 0;

		droppedItems = new SparseArray<>();

		LimitedDrops.reset();
		
		chapters = new HashSet<>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		GamesInProgress.selectedClass.initHero( hero );
	}

	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}

	public static boolean levelHasBeenGenerated(int depth, int branch){
		return generatedLevels.contains(depth + 1000*branch);
	}
	
	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		
		Level level;
		if (branch == 0) {
			switch (depth) {
				case 1:
				case 2:
				case 3:
				case 4:
					level = new SewerLevel();
					break;
				case 5:
					level = new SewerBossLevel();
					break;
				case 6:
				case 7:
				case 8:
				case 9:
					level = new PrisonLevel();
					break;
				case 10:
					level = new PrisonBossLevel();
					break;
				case 11:
				case 12:
				case 13:
				case 14:
					level = new CavesLevel();
					break;
				case 15:
					level = new CavesBossLevel();
					break;
				case 16:
				case 17:
				case 18:
				case 19:
					level = new CityLevel();
					break;
				case 20:
					level = new CityBossLevel();
					break;
				case 21:
				case 22:
				case 23:
				case 24:
					level = new HallsLevel();
					break;
				case 25:
					level = new HallsBossLevel();
					break;
				case 26:
					level = new LastLevel();
					break;
				default:
					level = new DeadEndLevel();
			}
		} else if (branch == 1) {
			switch (depth) {
				case 11:
				case 12:
				case 13:
				case 14:
					level = new MiningLevel();
					break;
				default:
					level = new DeadEndLevel();
			}
		} else {
			level = new DeadEndLevel();
		}

		//dead end levels get cleared, don't count as generated
		if (!(level instanceof DeadEndLevel)){
			//this assumes that we will never have a depth value outside the range 0 to 999
			// or -500 to 499, etc.
			if (!generatedLevels.contains(depth + 1000*branch)) {
				generatedLevels.add(depth + 1000 * branch);
			}

			if (depth > Statistics.deepestFloor && branch == 0) {
				Statistics.deepestFloor = depth;

				if (Statistics.qualifiedForNoKilling) {
					Statistics.completedWithNoKilling = true;
				} else {
					Statistics.completedWithNoKilling = false;
				}
			}
		}

		Statistics.qualifiedForBossRemainsBadge = false;
		
		level.create();
		
		if (branch == 0) Statistics.qualifiedForNoKilling = !bossLevel();
		Statistics.qualifiedForBossChallengeBadge = false;
		
		return level;
	}
	
	public static void resetLevel() {
		
		Actor.clear();
		
		level.reset();
		switchLevel( level, level.entrance() );
	}

	public static long seedCurDepth(){
		return seedForDepth(depth, branch);
	}

	public static long seedForDepth(int depth, int branch){
		int lookAhead = depth;
		lookAhead += 30*branch; //Assumes depth is always 1-30, and branch is always 0 or higher

		Random.pushGenerator( seed );

			for (int i = 0; i < lookAhead; i ++) {
				Random.Long(); //we don't care about these values, just need to go through them
			}
			long result = Random.Long();

		Random.popGenerator();
		return result;
	}
	
	public static boolean shopOnLevel() {
		return depth == 6 || depth == 11 || depth == 16;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}
	
	public static boolean bossLevel( int depth ) {
		return depth == 5 || depth == 10 || depth == 15 || depth == 20 || depth == 25;
	}

	//value used for scaling of damage values and other effects.
	//is usually the dungeon depth, but can be set to 26 when ascending
	public static int scalingDepth(){
		if (Dungeon.hero != null && Dungeon.hero.buff(AscensionChallenge.class) != null){
			return 26;
		} else {
			return depth;
		}
	}

	public static boolean interfloorTeleportAllowed(){
		if (Dungeon.level.locked
				|| Dungeon.level instanceof MiningLevel
				|| (Dungeon.hero != null && Dungeon.hero.belongings.getItem(Amulet.class) != null)){
			return false;
		}
		return true;
	}
	
	public static void switchLevel( final Level level, int pos ) {

		//Position of -2 specifically means trying to place the hero the exit
		if (pos == -2){
			LevelTransition t = level.getTransition(LevelTransition.Type.REGULAR_EXIT);
			if (t != null) pos = t.cell();
		}

		//Place hero at the entrance if they are out of the map (often used for pos = -1)
		// or if they are in invalid terrain terrain (except in the mining level, where that happens normally)
		if (pos < 0 || pos >= level.length() || level.invalidHeroPos(pos)){
			pos = level.getTransition(null).cell();
		}
		
		PathFinder.setMapSize(level.width(), level.height());
		
		Dungeon.level = level;
		hero.pos = pos;

		if (hero.buff(AscensionChallenge.class) != null){
			hero.buff(AscensionChallenge.class).onLevelSwitch();
		}

		Mob.restoreAllies( level, pos );

		Actor.init();

		level.addRespawner();
		
		for(Mob m : level.mobs){
			if (m.pos == hero.pos && !Char.hasProp(m, Char.Property.IMMOVABLE)){
				//displace mob
				for(int i : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(m.pos+i) == null && level.passable[m.pos + i]){
						m.pos += i;
						break;
					}
				}
			}
		}
		
		Light light = hero.buff( Light.class );
		hero.viewDistance = light == null ? level.viewDistance : Math.max( Light.DISTANCE, level.viewDistance );
		
		hero.curAction = hero.lastAction = null;

		observe();
		try {
			saveAll();
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<>() );
		}
		dropped.add( item );
	}

	public static boolean posNeeded() {
		//2 POS each floor set
		int posLeftThisSet = 2 - (LimitedDrops.STRENGTH_POTIONS.count - (depth / 5) * 2);
		if (posLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);

		//pos drops every two floors, (numbers 1-2, and 3-4) with a 50% chance for the earlier one each time.
		int targetPOSLeft = 2 - floorThisSet/2;
		if (floorThisSet % 2 == 1 && Random.Int(2) == 0) targetPOSLeft --;

		if (targetPOSLeft < posLeftThisSet) return true;
		else return false;

	}
	
	public static boolean souNeeded() {
		int souLeftThisSet;
		//3 SOU each floor set
		souLeftThisSet = 3 - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 3);
		if (souLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < souLeftThisSet;
	}
	
	public static boolean asNeeded() {
		//1 AS each floor set
		int asLeftThisSet = 1 - (LimitedDrops.ARCANE_STYLI.count - (depth / 5));
		if (asLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < asLeftThisSet;
	}

	public static boolean enchStoneNeeded(){
		//1 enchantment stone, spawns on chapter 2 or 3
		if (!LimitedDrops.ENCH_STONE.dropped()){
			int region = 1+depth/5;
			if (region > 1){
				int floorsVisited = depth - 5;
				if (floorsVisited > 4) floorsVisited--; //skip floor 10
				return Random.Int(9-floorsVisited) == 0; //1/8 chance each floor
			}
		}
		return false;
	}

	public static boolean intStoneNeeded(){
		//one stone on floors 1-3
		return depth < 5 && !LimitedDrops.INT_STONE.dropped() && Random.Int(4-depth) == 0;
	}

	public static boolean trinketCataNeeded(){
		//one trinket catalyst on floors 1-3
		return depth < 5 && !LimitedDrops.TRINKET_CATA.dropped() && Random.Int(4-depth) == 0;
	}

	public static boolean labRoomNeeded(){
		//one laboratory each floor set, in floor 3 or 4, 1/2 chance each floor
		int region = 1+depth/5;
		if (region > LimitedDrops.LAB_ROOM.count){
			int floorThisRegion = depth%5;
			if (floorThisRegion >= 4 || (floorThisRegion == 3 && Random.Int(2) == 0)){
				return true;
			}
		}
		return false;
	}

	private static final String INIT_VER	= "init_ver";
	public  static final String VERSION		= "version";
	private static final String SEED		= "seed";
	private static final String CUSTOM_SEED	= "custom_seed";
	private static final String DAILY	    = "daily";
	private static final String DAILY_REPLAY= "daily_replay";
	private static final String LAST_PLAYED = "last_played";
	private static final String CHALLENGES	= "challenges";
	private static final String MOBS_TO_CHAMPION	= "mobs_to_champion";
	private static final String HERO		= "hero";
	private static final String DEPTH		= "depth";
	private static final String BRANCH		= "branch";
	private static final String GENERATED_LEVELS    = "generated_levels";
	private static final String GOLD		= "gold";
	private static final String ENERGY		= "energy";
	private static final String DROPPED     = "dropped%d";
	private static final String PORTED      = "ported%d";
	private static final String LEVEL		= "level";
	private static final String LIMDROPS    = "limited_drops";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	
	public static void saveGame( int save ) {
		try {
			Bundle bundle = new Bundle();

			bundle.put( INIT_VER, initialVersion );
			bundle.put( VERSION, version = Game.versionCode );
			bundle.put( SEED, seed );
			bundle.put( CUSTOM_SEED, customSeedText );
			bundle.put( DAILY, daily );
			bundle.put( DAILY_REPLAY, dailyReplay );
			bundle.put( LAST_PLAYED, lastPlayed = Game.realTime);
			bundle.put( CHALLENGES, challenges );
			bundle.put( MOBS_TO_CHAMPION, mobsToChampion );
			bundle.put( HERO, hero );
			bundle.put( DEPTH, depth );
			bundle.put( BRANCH, branch );

			bundle.put( GOLD, gold );
			bundle.put( ENERGY, energy );

			for (int d : droppedItems.keyArray()) {
				bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));
			}

			quickslot.storePlaceholders( bundle );

			Bundle limDrops = new Bundle();
			LimitedDrops.store( limDrops );
			bundle.put ( LIMDROPS, limDrops );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			SpecialRoom.storeRoomsInBundle( bundle );
			SecretRoom.storeRoomsInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Notes.storeInBundle( bundle );
			Generator.storeInBundle( bundle );

			int[] bundleArr = new int[generatedLevels.size()];
			for (int i = 0; i < generatedLevels.size(); i++){
				bundleArr[i] = generatedLevels.get(i);
			}
			bundle.put( GENERATED_LEVELS, bundleArr);
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Ring.save( bundle );

			Actor.storeNextID( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			FileUtils.bundleToFile( GamesInProgress.gameFile(save), bundle);
			
		} catch (IOException e) {
			GamesInProgress.setUnknown( save );
			ShatteredPixelDungeon.reportException(e);
		}
	}
	
	public static void saveLevel( int save ) throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		FileUtils.bundleToFile(GamesInProgress.depthFile( save, depth, branch ), bundle);
	}
	
	public static void saveAll() throws IOException {
		if (hero != null && (hero.isAlive() || WndResurrect.instance != null)) {
			
			Actor.fixTime();
			updateLevelExplored();
			saveGame( GamesInProgress.curSlot );
			saveLevel( GamesInProgress.curSlot );

			GamesInProgress.set( GamesInProgress.curSlot );

		}
	}
	
	public static void loadGame( int save ) throws IOException {
		loadGame( save, true );
	}
	
	public static void loadGame( int save, boolean fullLoad ) throws IOException {
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.gameFile( save ) );

		initialVersion = bundle.getInt( INIT_VER );
		version = bundle.getInt( VERSION );

		seed = bundle.contains( SEED ) ? bundle.getLong( SEED ) : DungeonSeed.randomSeed();
		customSeedText = bundle.getString( CUSTOM_SEED );
		daily = bundle.getBoolean( DAILY );
		dailyReplay = bundle.getBoolean( DAILY_REPLAY );

		Actor.clear();
		Actor.restoreNextID( bundle );

		quickslot.reset();
		QuickSlotButton.reset();
		Toolbar.swappedQuickslots = false;

		Dungeon.challenges = bundle.getInt( CHALLENGES );
		Dungeon.mobsToChampion = bundle.getInt( MOBS_TO_CHAMPION );
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Ring.restore( bundle );

		quickslot.restorePlaceholders( bundle );
		
		if (fullLoad) {
			
			LimitedDrops.restore( bundle.getBundle(LIMDROPS) );

			chapters = new HashSet<>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}
			
			SpecialRoom.restoreRoomsFromBundle(bundle);
			SecretRoom.restoreRoomsFromBundle(bundle);

			generatedLevels.clear();
			for (int i : bundle.getIntArray(GENERATED_LEVELS)){
				generatedLevels.add(i);
			}

			droppedItems = new SparseArray<>();
			for (int i=1; i <= 26; i++) {

				//dropped items
				ArrayList<Item> items = new ArrayList<>();
				if (bundle.contains(Messages.format( DROPPED, i )))
					for (Bundlable b : bundle.getCollection( Messages.format( DROPPED, i ) ) ) {
						items.add( (Item)b );
					}
				if (!items.isEmpty()) {
					droppedItems.put( i, items );
				}

			}
		}
		
		Bundle badges = bundle.getBundle(BADGES);
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		Notes.restoreFromBundle( bundle );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );
		
		depth = bundle.getInt( DEPTH );
		branch = bundle.getInt( BRANCH );

		gold = bundle.getInt( GOLD );
		energy = bundle.getInt( ENERGY );

		Statistics.restoreFromBundle( bundle );
		Generator.restoreFromBundle( bundle );

	}
	
	public static Level loadLevel( int save ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();

		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.depthFile( save, depth, branch ));

		Level level = (Level)bundle.get( LEVEL );

		if (level == null){
			throw new IOException();
		} else {
			return level;
		}
	}
	
	public static void deleteGame( int save, boolean deleteLevels ) {

		if (deleteLevels) {
			String folder = GamesInProgress.gameFolder(save);
			for (String file : FileUtils.filesInDir(folder)){
				if (file.contains("depth")){
					FileUtils.deleteFile(folder + "/" + file);
				}
			}
		}

		FileUtils.overwriteFile(GamesInProgress.gameFile(save), 1);
		
		GamesInProgress.delete( save );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.version = bundle.getInt( VERSION );
		info.challenges = bundle.getInt( CHALLENGES );
		info.seed = bundle.getLong( SEED );
		info.customSeed = bundle.getString( CUSTOM_SEED );
		info.daily = bundle.getBoolean( DAILY );
		info.dailyReplay = bundle.getBoolean( DAILY_REPLAY );
		info.lastPlayed = bundle.getLong( LAST_PLAYED );

		Hero.preview( info, bundle.getBundle( HERO ) );
		Statistics.preview( info, bundle );
	}
	
	public static void fail( Object cause ) {
		if (WndResurrect.instance == null) {
			updateLevelExplored();
			Statistics.gameWon = false;
			Rankings.INSTANCE.submit( false, cause );
		}
	}
	
	public static void win( Object cause ) {

		updateLevelExplored();
		Statistics.gameWon = true;

		hero.belongings.identify();

		Rankings.INSTANCE.submit( true, cause );
	}

	public static void updateLevelExplored(){
		if (branch == 0 && level instanceof RegularLevel && !Dungeon.bossLevel()){
			Statistics.floorsExplored.put( depth, level.isLevelExplored(depth));
		}
	}

	//default to recomputing based on max hero vision, in case vision just shrank/grew
	public static void observe(){
		int dist = Math.max(Dungeon.hero.viewDistance, 8);
		dist *= 1f + 0.25f*Dungeon.hero.pointsInTalent(Talent.FARSIGHT);

		if (Dungeon.hero.buff(MagicalSight.class) != null){
			dist = Math.max( dist, MagicalSight.DISTANCE );
		}

		observe( dist+1 );
	}
	
	public static void observe( int dist ) {

		if (level == null) {
			return;
		}
		
		level.updateFieldOfView(hero, level.heroFOV);

		int x = hero.pos % level.width();
		int y = hero.pos / level.width();
	
		//left, right, top, bottom
		int l = Math.max( 0, x - dist );
		int r = Math.min( x + dist, level.width() - 1 );
		int t = Math.max( 0, y - dist );
		int b = Math.min( y + dist, level.height() - 1 );
	
		int width = r - l + 1;
		int height = b - t + 1;
		
		int pos = l + t * level.width();
	
		for (int i = t; i <= b; i++) {
			BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
			pos+=level.width();
		}

		//always visit adjacent tiles, even if they aren't seen
		for (int i : PathFinder.NEIGHBOURS9){
			level.visited[hero.pos+i] = true;
		}
	
		GameScene.updateFog(l, t, width, height);

		if (hero.buff(MindVision.class) != null || hero.buff(DivineSense.DivineSenseTracker.class) != null){
			for (Mob m : level.mobs.toArray(new Mob[0])){
				if (m instanceof Mimic && m.alignment == Char.Alignment.NEUTRAL && ((Mimic) m).stealthy()){
					continue;
				}

				BArray.or( level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, m.pos - 1, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited );
				//updates adjacent cells too
				GameScene.updateFog(m.pos, 2);
			}
		}

		if (hero.buff(Awareness.class) != null){
			for (Heap h : level.heaps.valueList()){
				BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
				GameScene.updateFog(h.pos, 2);
			}
		}

		for (TalismanOfForesight.CharAwareness c : hero.buffs(TalismanOfForesight.CharAwareness.class)){
			Char ch = (Char) Actor.findById(c.charID);
			if (ch == null || !ch.isAlive()) continue;
			BArray.or( level.visited, level.heroFOV, ch.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, ch.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, ch.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(ch.pos, 2);
		}

		for (TalismanOfForesight.HeapAwareness h : hero.buffs(TalismanOfForesight.HeapAwareness.class)){
			if (Dungeon.depth != h.depth || Dungeon.branch != h.branch) continue;
			BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(h.pos, 2);
		}

		for (RevealedArea a : hero.buffs(RevealedArea.class)){
			if (Dungeon.depth != a.depth || Dungeon.branch != a.branch) continue;
			BArray.or( level.visited, level.heroFOV, a.pos - 1 - level.width(), 3, level.visited );
			BArray.or( level.visited, level.heroFOV, a.pos - 1, 3, level.visited );
			BArray.or( level.visited, level.heroFOV, a.pos - 1 + level.width(), 3, level.visited );
			GameScene.updateFog(a.pos, 2);
		}

		for (Char ch : Actor.chars()){
			if (ch instanceof WandOfWarding.Ward
					|| ch instanceof WandOfRegrowth.Lotus
					|| ch instanceof SpiritHawk.HawkAlly
					|| ch.buff(PowerOfMany.PowerBuff.class) != null){
				x = ch.pos % level.width();
				y = ch.pos / level.width();

				//left, right, top, bottom
				dist = ch.viewDistance+1;
				l = Math.max( 0, x - dist );
				r = Math.min( x + dist, level.width() - 1 );
				t = Math.max( 0, y - dist );
				b = Math.min( y + dist, level.height() - 1 );

				width = r - l + 1;
				height = b - t + 1;

				pos = l + t * level.width();

				for (int i = t; i <= b; i++) {
					BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
					pos+=level.width();
				}
				GameScene.updateFog(ch.pos, dist);
			}
		}

		GameScene.afterObserve();
	}

	//we store this to avoid having to re-allocate the array with each pathfind
	private static boolean[] passable;

	private static void setupPassable(){
		if (passable == null || passable.length != Dungeon.level.length())
			passable = new boolean[Dungeon.level.length()];
		else
			BArray.setFalse(passable);
	}

	public static boolean[] findPassable(Char ch, boolean[] pass, boolean[] vis, boolean chars){
		return findPassable(ch, pass, vis, chars, chars);
	}

	public static boolean[] findPassable(Char ch, boolean[] pass, boolean[] vis, boolean chars, boolean considerLarge){
		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}

		if (considerLarge && Char.hasProp(ch, Char.Property.LARGE)){
			BArray.and( passable, Dungeon.level.openSpace, passable );
		}

		ch.modifyPassable(passable);

		if (chars) {
			for (Char c : Actor.chars()) {
				if (vis[c.pos]) {
					passable[c.pos] = false;
				}
			}
		}

		return passable;
	}

	public static PathFinder.Path findPath(Char ch, int to, boolean[] pass, boolean[] vis, boolean chars) {

		return PathFinder.find( ch.pos, to, findPassable(ch, pass, vis, chars) );

	}
	
	public static int findStep(Char ch, int to, boolean[] pass, boolean[] visible, boolean chars ) {

		if (Dungeon.level.adjacent( ch.pos, to )) {
			return Actor.findChar( to ) == null && pass[to] ? to : -1;
		}

		return PathFinder.getStep( ch.pos, to, findPassable(ch, pass, visible, chars) );

	}

	public static int flee( Char ch, int from, boolean[] pass, boolean[] visible, boolean chars ) {
		boolean[] passable = findPassable(ch, pass, visible, false, true);
		passable[ch.pos] = true;

		//chars affected by terror have a shorter lookahead and can't approach the fear source
		boolean canApproachFromPos = ch.buff(Terror.class) == null && ch.buff(Dread.class) == null;
		int step = PathFinder.getStepBack( ch.pos, from, canApproachFromPos ? 8 : 4, passable, canApproachFromPos );

		//only consider chars impassable if our retreat step runs into them
		while (step != -1 && Actor.findChar(step) != null && chars){
			passable[step] = false;
			step = PathFinder.getStepBack( ch.pos, from, canApproachFromPos ? 8 : 4, passable, canApproachFromPos );
		}
		return step;

	}

}
