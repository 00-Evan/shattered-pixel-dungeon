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

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Toolbar;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Rankings {
	
	INSTANCE;
	
	public static final int TABLE_SIZE	= 11;
	
	public static final String RANKINGS_FILE = "rankings.dat";
	
	public ArrayList<Record> records;
	public int lastRecord;
	public int totalNumber;
	public int wonNumber;

	//The number of runs which are only present locally, not in the cloud
	public int localTotal;
	public int localWon;

	public Record latestDaily;
	public Record latestDailyReplay = null; //not stored, only meant to be temp
	public LinkedHashMap<Long, Integer> dailyScoreHistory = new LinkedHashMap<>();

	public void submit( boolean win, Class cause ) {

		load();
		
		Record rec = new Record();

		//we trim version to just the numbers, ignoring alpha/beta, etc.
		Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+");
		Matcher m = p.matcher(ShatteredPixelDungeon.version);
		if (m.find()) {
			rec.version = "v" + m.group();
		} else {
			rec.version = "";
		}

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
		rec.date = format.format(new Date(Game.realTime));

		rec.cause = cause;
		rec.win		= win;
		rec.heroClass	= Dungeon.hero.heroClass;
		rec.armorTier	= Dungeon.hero.tier();
		rec.herolevel	= Dungeon.hero.lvl;
		if (Statistics.highestAscent == 0){
			rec.depth = Statistics.deepestFloor;
			rec.ascending = false;
		} else {
			rec.depth = Statistics.highestAscent;
			rec.ascending = true;
		}
		rec.score       = calculateScore();
		rec.customSeed  = Dungeon.customSeedText;
		rec.daily       = Dungeon.daily;

		Badges.validateHighScore( rec.score );
		
		INSTANCE.saveGameData(rec);

		rec.gameID = UUID.randomUUID().toString();

		if (rec.daily){
			if (Dungeon.dailyReplay){
				latestDailyReplay = rec;
				return;
			}

			latestDaily = rec;
			if (Dungeon.seed <= DungeonSeed.TOTAL_SEEDS) {
				dailyScoreHistory.put(Dungeon.seed, rec.score);
			} else {
				dailyScoreHistory.put(Dungeon.seed - DungeonSeed.TOTAL_SEEDS, rec.score);
			}
			save();
			return;
		}

		records.add( rec );
		
		Collections.sort( records, scoreComparator );
		
		lastRecord = records.indexOf( rec );
		int size = records.size();
		while (size > TABLE_SIZE) {

			if (lastRecord == size - 1) {
				records.remove( size - 2 );
				lastRecord--;
			} else {
				records.remove( size - 1 );
			}

			size = records.size();
		}

		if (rec.customSeed.isEmpty()) {
			totalNumber++;
			if (win) {
				wonNumber++;
			}
		}

		Badges.validateGamesPlayed();
		
		save();
	}

	private int score( boolean win ) {
		return (Statistics.goldCollected + Dungeon.hero.lvl * (win ? 26 : Dungeon.depth ) * 100) * (win ? 2 : 1);
	}

	//assumes a ranking is loaded, or game is ending
	public int calculateScore(){

		if (Dungeon.initialVersion > ShatteredPixelDungeon.v1_2_3){
			Statistics.progressScore = Dungeon.hero.lvl * Statistics.deepestFloor * 65;
			Statistics.progressScore = Math.min(Statistics.progressScore, 50_000);

			if (Statistics.heldItemValue == 0) {
				for (Item i : Dungeon.hero.belongings) {
					Statistics.heldItemValue += i.value();
					if (i instanceof CorpseDust && Statistics.deepestFloor >= 10){
						// in case player kept the corpse dust, for a necromancer run
						Statistics.questScores[1] = 2000;
					}
				}
			}
			Statistics.treasureScore = Statistics.goldCollected + Statistics.heldItemValue;
			Statistics.treasureScore = Math.min(Statistics.treasureScore, 20_000);

			Statistics.exploreScore = 0;
			int scorePerFloor = Statistics.floorsExplored.size * 50;
			for (Boolean b : Statistics.floorsExplored.valueList()){
				if (b) Statistics.exploreScore += scorePerFloor;
			}

			Statistics.totalBossScore = 0;
			for (int i : Statistics.bossScores){
				if (i > 0) Statistics.totalBossScore += i;
			}

			Statistics.totalQuestScore = 0;
			for (int i : Statistics.questScores){
				if (i > 0) Statistics.totalQuestScore += i;
			}

			Statistics.winMultiplier = 1f;
			if (Statistics.gameWon)         Statistics.winMultiplier += 1f;
			if (Statistics.ascended)        Statistics.winMultiplier += 0.5f;

		//pre v1.3.0 runs have different score calculations
		//only progress and treasure score, and they are each up to 50% bigger
		//win multiplier is a simple 2x if run was a win, challenge multi is the same as 1.3.0
		} else {
			Statistics.progressScore = Dungeon.hero.lvl * Statistics.deepestFloor * 100;
			Statistics.treasureScore = Math.min(Statistics.goldCollected, 30_000);

			Statistics.exploreScore = Statistics.totalBossScore = Statistics.totalQuestScore = 0;

			Statistics.winMultiplier = Statistics.gameWon ? 2 : 1;

		}

		Statistics.chalMultiplier = (float)Math.pow(1.25, Challenges.activeChallenges());
		Statistics.chalMultiplier = Math.round(Statistics.chalMultiplier*20f)/20f;

		Statistics.totalScore = Statistics.progressScore + Statistics.treasureScore + Statistics.exploreScore
					+ Statistics.totalBossScore + Statistics.totalQuestScore;

		Statistics.totalScore *= Statistics.winMultiplier * Statistics.chalMultiplier;

		return Statistics.totalScore;
	}

	public static final String HERO         = "hero";
	public static final String STATS        = "stats";
	public static final String BADGES       = "badges";
	public static final String HANDLERS     = "handlers";
	public static final String CHALLENGES   = "challenges";
	public static final String GAME_VERSION = "game_version";
	public static final String SEED         = "seed";
	public static final String CUSTOM_SEED	= "custom_seed";
	public static final String DAILY	    = "daily";
	public static final String DAILY_REPLAY	= "daily_replay";

	public void saveGameData(Record rec){
		rec.gameData = new Bundle();

		Belongings belongings = Dungeon.hero.belongings;

		//save the hero and belongings
		ArrayList<Item> allItems = (ArrayList<Item>) belongings.backpack.items.clone();
		//remove items that won't show up in the rankings screen
		for (Item item : belongings.backpack.items.toArray( new Item[0])) {
			if (item instanceof Bag){
				for (Item bagItem : ((Bag) item).items.toArray( new Item[0])){
					if (Dungeon.quickslot.contains(bagItem)
							&& !Dungeon.quickslot.contains(item)){
						belongings.backpack.items.add(bagItem);
					}
				}
			}
			if (!Dungeon.quickslot.contains(item)) {
				belongings.backpack.items.remove(item);
			}
		}

		//remove all buffs (ones tied to equipment will be re-applied)
		for(Buff b : Dungeon.hero.buffs()){
			Dungeon.hero.remove(b);
		}

		rec.gameData.put( HERO, Dungeon.hero );

		//save stats
		Bundle stats = new Bundle();
		Statistics.storeInBundle(stats);
		rec.gameData.put( STATS, stats);

		//save badges
		Bundle badges = new Bundle();
		Badges.saveLocal(badges);
		rec.gameData.put( BADGES, badges);

		//save handler information
		Bundle handler = new Bundle();
		Scroll.saveSelectively(handler, belongings.backpack.items);
		Potion.saveSelectively(handler, belongings.backpack.items);
		//include potentially worn rings
		if (belongings.misc != null)        belongings.backpack.items.add(belongings.misc);
		if (belongings.ring != null)        belongings.backpack.items.add(belongings.ring);
		Ring.saveSelectively(handler, belongings.backpack.items);
		rec.gameData.put( HANDLERS, handler);

		//restore items now that we're done saving
		belongings.backpack.items = allItems;
		
		//save challenges
		rec.gameData.put( CHALLENGES, Dungeon.challenges );

		rec.gameData.put( GAME_VERSION, Dungeon.initialVersion );

		rec.gameData.put( SEED, Dungeon.seed );
		rec.gameData.put( CUSTOM_SEED, Dungeon.customSeedText );
		rec.gameData.put( DAILY, Dungeon.daily );
		rec.gameData.put( DAILY_REPLAY, Dungeon.dailyReplay );
	}

	public void loadGameData(Record rec){
		Bundle data = rec.gameData;

		Actor.clear();
		Dungeon.hero = null;
		Dungeon.level = null;
		Generator.fullReset();
		Notes.reset();
		Dungeon.quickslot.reset();
		QuickSlotButton.reset();
		Toolbar.swappedQuickslots = false;

		Bundle handler = data.getBundle(HANDLERS);
		Scroll.restore(handler);
		Potion.restore(handler);
		Ring.restore(handler);

		Badges.loadLocal(data.getBundle(BADGES));

		Dungeon.hero = (Hero)data.get(HERO);
		Dungeon.hero.belongings.identify();

		Statistics.restoreFromBundle(data.getBundle(STATS));
		
		Dungeon.challenges = data.getInt(CHALLENGES);

		Dungeon.initialVersion = data.getInt(GAME_VERSION);

		if (Dungeon.initialVersion <= ShatteredPixelDungeon.v1_2_3){
			Statistics.gameWon = rec.win;
		}
		rec.score = calculateScore();

		if (rec.gameData.contains(SEED)){
			Dungeon.seed = rec.gameData.getLong(SEED);
			Dungeon.customSeedText = rec.gameData.getString(CUSTOM_SEED);
			Dungeon.daily = rec.gameData.getBoolean(DAILY);
			Dungeon.dailyReplay = rec.gameData.getBoolean(DAILY_REPLAY);
		} else {
			Dungeon.seed = -1;
			Dungeon.customSeedText = "";
			Dungeon.daily = Dungeon.dailyReplay = false;
		}
	}
	
	private static final String RECORDS	= "records";
	private static final String LATEST	= "latest";
	private static final String TOTAL	= "total";
	private static final String WON     = "won";

	public static final String LATEST_DAILY	        = "latest_daily";
	public static final String DAILY_HISTORY_DATES  = "daily_history_dates";
	public static final String DAILY_HISTORY_SCORES = "daily_history_scores";

	public void save() {
		Bundle bundle = new Bundle();
		bundle.put( RECORDS, records );
		bundle.put( LATEST, lastRecord );
		bundle.put( TOTAL, totalNumber );
		bundle.put( WON, wonNumber );

		bundle.put(LATEST_DAILY, latestDaily);

		long[] dates = new long[dailyScoreHistory.size()];
		int[] scores = new int[dailyScoreHistory.size()];
		int i = 0;
		for (Long l : dailyScoreHistory.keySet()){
			dates[i] = l;
			scores[i] = dailyScoreHistory.get(l);
			i++;
		}
		bundle.put(DAILY_HISTORY_DATES, dates);
		bundle.put(DAILY_HISTORY_SCORES, scores);

		try {
			FileUtils.bundleToFile( RANKINGS_FILE, bundle);
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}

	}
	
	public void load() {
		
		if (records != null) {
			return;
		}
		
		records = new ArrayList<>();
		
		try {
			Bundle bundle = FileUtils.bundleFromFile( RANKINGS_FILE );
			
			for (Bundlable record : bundle.getCollection( RECORDS )) {
				records.add( (Record)record );
			}
			lastRecord = bundle.getInt( LATEST );
			
			totalNumber = bundle.getInt( TOTAL );
			if (totalNumber == 0) {
				totalNumber = records.size();
			}

			wonNumber = bundle.getInt( WON );
			if (wonNumber == 0) {
				for (Record rec : records) {
					if (rec.win) {
						wonNumber++;
					}
				}
			}

			if (bundle.contains(LATEST_DAILY)){
				latestDaily = (Record) bundle.get(LATEST_DAILY);

				dailyScoreHistory.clear();
				int[] scores = bundle.getIntArray(DAILY_HISTORY_SCORES);
				int i = 0;
				long latestDate = 0;
				for (long date : bundle.getLongArray(DAILY_HISTORY_DATES)){
					dailyScoreHistory.put(date, scores[i]);
					if (date > latestDate) latestDate = date;
					i++;
				}
				if (latestDate > SPDSettings.lastDaily()){
					SPDSettings.lastDaily(latestDate);
				}
			}

		} catch (IOException e) {
		}
	}
	
	public static class Record implements Bundlable {

		private static final String CAUSE   = "cause";
		private static final String WIN		= "win";
		private static final String SCORE	= "score";
		private static final String CLASS	= "class";
		private static final String TIER	= "tier";
		private static final String LEVEL	= "level";
		private static final String DEPTH	= "depth";
		private static final String ASCEND	= "ascending";
		private static final String DATA	= "gameData";
		private static final String ID      = "gameID";
		private static final String SEED    = "custom_seed";
		private static final String DAILY   = "daily";

		private static final String DATE    = "date";
		private static final String VERSION = "version";

		public Class cause;
		public boolean win;

		public HeroClass heroClass;
		public int armorTier;
		public int herolevel;
		public int depth;
		public boolean ascending;

		public Bundle gameData;
		public String gameID;

		//Note this is for summary purposes, visible score should be re-calculated from game data
		public int score;

		public String customSeed;
		public boolean daily;

		public String date;
		public String version;

		public String desc(){
			if (win){
				if (ascending){
					return Messages.get(this, "ascended");
				} else {
					return Messages.get(this, "won");
				}
			} else if (cause == null) {
				return Messages.get(this, "something");
			} else {
				String result = Messages.get(cause, "rankings_desc", (Messages.get(cause, "name")));
				if (result.contains(Messages.NO_TEXT_FOUND)){
					return Messages.get(this, "something");
				} else {
					return result;
				}
			}
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			
			if (bundle.contains( CAUSE )) {
				cause = bundle.getClass( CAUSE );
			} else {
				cause = null;
			}
			
			win		    = bundle.getBoolean( WIN );
			score	    = bundle.getInt( SCORE );
			customSeed  = bundle.getString( SEED );
			daily       = bundle.getBoolean( DAILY );

			heroClass	= bundle.getEnum( CLASS, HeroClass.class );
			armorTier	= bundle.getInt( TIER );
			herolevel   = bundle.getInt( LEVEL );
			depth       = bundle.getInt( DEPTH );
			ascending   = bundle.getBoolean( ASCEND );

			if (bundle.contains( DATE )){
				date = bundle.getString( DATE );
				version = bundle.getString( VERSION );
			} else {
				date = version = null;
			}

			if (bundle.contains(DATA))  gameData = bundle.getBundle(DATA);
			if (bundle.contains(ID))   gameID = bundle.getString(ID);
			
			if (gameID == null) gameID = UUID.randomUUID().toString();

		}
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			
			if (cause != null) bundle.put( CAUSE, cause );

			bundle.put( WIN, win );
			bundle.put( SCORE, score );
			bundle.put( SEED, customSeed );
			bundle.put( DAILY, daily );

			bundle.put( CLASS, heroClass );
			bundle.put( TIER, armorTier );
			bundle.put( LEVEL, herolevel );
			bundle.put( DEPTH, depth );
			bundle.put( ASCEND, ascending );

			bundle.put( DATE, date );
			bundle.put( VERSION, version );

			if (gameData != null) bundle.put( DATA, gameData );
			bundle.put( ID, gameID );
		}
	}

	public static final Comparator<Record> scoreComparator = new Comparator<Rankings.Record>() {
		@Override
		public int compare( Record lhs, Record rhs ) {
			//this covers custom seeded runs and dailies
			if (rhs.customSeed.isEmpty() && !lhs.customSeed.isEmpty()){
				return +1;
			} else if (lhs.customSeed.isEmpty() && !rhs.customSeed.isEmpty()){
				return -1;
			}

			int result = (int)Math.signum( rhs.score - lhs.score );
			if (result == 0) {
				return (int)Math.signum( rhs.gameID.hashCode() - lhs.gameID.hashCode());
			} else {
				return result;
			}
		}
	};
}
