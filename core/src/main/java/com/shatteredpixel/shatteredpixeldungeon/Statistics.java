/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.watabou.utils.Bundle;
import com.watabou.utils.SparseArray;

public class Statistics {

	public static int goldCollected;
	public static int deepestFloor;
	public static int highestAscent;
	public static int enemiesSlain;
	public static int foodEaten;
	public static int itemsCrafted;
	public static int piranhasKilled;
	public static int ankhsUsed;

	//These are used for score calculation
	// some are built incrementally, most are assigned when full score is calculated
	public static int progressScore;
	public static int heldItemValue;
	public static int treasureScore;
	public static SparseArray<Boolean> floorsExplored = new SparseArray<>();
	public static int exploreScore;
	public static int[] bossScores = new int[5];
	public static int totalBossScore;
	public static int[] questScores = new int[5];
	public static int totalQuestScore;
	public static float winMultiplier;
	public static float chalMultiplier;
	public static int totalScore;

	//used for hero unlock badges
	public static int upgradesUsed;
	public static int sneakAttacks;
	public static int thrownAttacks;

	public static int spawnersAlive;
	
	public static float duration;
	
	public static boolean qualifiedForNoKilling = false;
	public static boolean completedWithNoKilling = false;
	public static boolean qualifiedForBossChallengeBadge = false;
	
	public static boolean amuletObtained = false;
	public static boolean gameWon = false;
	public static boolean ascended = false;
	
	public static void reset() {
		
		goldCollected	= 0;
		deepestFloor	= 0;
		highestAscent	= 0;
		enemiesSlain	= 0;
		foodEaten		= 0;
		itemsCrafted    = 0;
		piranhasKilled	= 0;
		ankhsUsed		= 0;

		progressScore   = 0;
		heldItemValue   = 0;
		treasureScore   = 0;
		floorsExplored  = new SparseArray<>();
		exploreScore    = 0;
		bossScores      = new int[5];
		totalBossScore  = 0;
		questScores     = new int[5];
		totalQuestScore = 0;
		winMultiplier   = 1;
		chalMultiplier  = 1;
		totalScore      = 0;
		
		upgradesUsed    = 0;
		sneakAttacks    = 0;
		thrownAttacks   = 0;

		spawnersAlive   = 0;
		
		duration	    = 0;
		
		qualifiedForNoKilling = false;
		qualifiedForBossChallengeBadge = false;
		
		amuletObtained = false;
		gameWon = false;
		ascended = false;
		
	}
	
	private static final String GOLD		= "score";
	private static final String DEEPEST		= "maxDepth";
	private static final String HIGHEST		= "maxAscent";
	private static final String SLAIN		= "enemiesSlain";
	private static final String FOOD		= "foodEaten";
	private static final String ALCHEMY		= "potionsCooked";
	private static final String PIRANHAS	= "priranhas";
	private static final String ANKHS		= "ankhsUsed";

	private static final String PROG_SCORE	    = "prog_score";
	private static final String ITEM_VAL	    = "item_val";
	private static final String TRES_SCORE      = "tres_score";
	private static final String FLR_EXPL        = "flr_expl";
	private static final String EXPL_SCORE      = "expl_score";
	private static final String BOSS_SCORES		= "boss_scores";
	private static final String TOT_BOSS		= "tot_boss";
	private static final String QUEST_SCORES	= "quest_scores";
	private static final String TOT_QUEST		= "tot_quest";
	private static final String WIN_MULT		= "win_mult";
	private static final String CHAL_MULT		= "chal_mult";
	private static final String TOTAL_SCORE		= "total_score";
	
	private static final String UPGRADES	= "upgradesUsed";
	private static final String SNEAKS		= "sneakAttacks";
	private static final String THROWN		= "thrownAssists";

	private static final String SPAWNERS	= "spawnersAlive";
	
	private static final String DURATION	= "duration";

	private static final String NO_KILLING_QUALIFIED	= "qualifiedForNoKilling";
	private static final String BOSS_CHALLENGE_QUALIFIED= "qualifiedForBossChallengeBadge";
	
	private static final String AMULET          = "amuletObtained";
	private static final String WON		        = "won";
	private static final String ASCENDED		= "ascended";
	
	public static void storeInBundle( Bundle bundle ) {
		bundle.put( GOLD,		goldCollected );
		bundle.put( DEEPEST,	deepestFloor );
		bundle.put( HIGHEST,	highestAscent );
		bundle.put( SLAIN,		enemiesSlain );
		bundle.put( FOOD,		foodEaten );
		bundle.put( ALCHEMY,    itemsCrafted );
		bundle.put( PIRANHAS,	piranhasKilled );
		bundle.put( ANKHS,		ankhsUsed );

		bundle.put( PROG_SCORE,  progressScore );
		bundle.put( ITEM_VAL,    heldItemValue );
		bundle.put( TRES_SCORE,  treasureScore );
		for (int i = 1; i < 26; i++){
			if (floorsExplored.containsKey(i)){
				bundle.put( FLR_EXPL+i, floorsExplored.get(i) );
			}
		}
		bundle.put( EXPL_SCORE,  exploreScore );
		bundle.put( BOSS_SCORES, bossScores );
		bundle.put( TOT_BOSS,    totalBossScore );
		bundle.put( QUEST_SCORES,questScores );
		bundle.put( TOT_QUEST,   totalQuestScore );
		bundle.put( WIN_MULT,    winMultiplier );
		bundle.put( CHAL_MULT,   chalMultiplier );
		bundle.put( TOTAL_SCORE, totalScore );
		
		bundle.put( UPGRADES,   upgradesUsed );
		bundle.put( SNEAKS,		sneakAttacks );
		bundle.put( THROWN,     thrownAttacks);

		bundle.put( SPAWNERS,	spawnersAlive );
		
		bundle.put( DURATION,	duration );

		bundle.put(NO_KILLING_QUALIFIED, qualifiedForNoKilling);
		bundle.put(BOSS_CHALLENGE_QUALIFIED, qualifiedForBossChallengeBadge);
		
		bundle.put( AMULET,		amuletObtained );
		bundle.put( WON,        gameWon );
		bundle.put( ASCENDED,   ascended );
	}
	
	public static void restoreFromBundle( Bundle bundle ) {
		goldCollected	= bundle.getInt( GOLD );
		deepestFloor	= bundle.getInt( DEEPEST );
		highestAscent   = bundle.getInt( HIGHEST );
		enemiesSlain	= bundle.getInt( SLAIN );
		foodEaten		= bundle.getInt( FOOD );
		itemsCrafted    = bundle.getInt( ALCHEMY );
		piranhasKilled	= bundle.getInt( PIRANHAS );
		ankhsUsed		= bundle.getInt( ANKHS );

		progressScore   = bundle.getInt( PROG_SCORE );
		heldItemValue   = bundle.getInt( ITEM_VAL );
		treasureScore   = bundle.getInt( TRES_SCORE );
		floorsExplored.clear();
		for (int i = 1; i < 26; i++){
			if (bundle.contains( FLR_EXPL+i )){
				floorsExplored.put(i, bundle.getBoolean( FLR_EXPL+i ));
			}
		}
		exploreScore    = bundle.getInt( EXPL_SCORE );
		if (bundle.contains( BOSS_SCORES )) bossScores = bundle.getIntArray( BOSS_SCORES );
		else                                bossScores = new int[5];
		totalBossScore  = bundle.getInt( TOT_BOSS );
		if (bundle.contains( QUEST_SCORES ))questScores = bundle.getIntArray( QUEST_SCORES );
		else                                questScores = new int[5];
		totalQuestScore = bundle.getInt( TOT_QUEST );
		winMultiplier   = bundle.getFloat( WIN_MULT );
		chalMultiplier  = bundle.getFloat( CHAL_MULT );
		totalScore      = bundle.getInt( TOTAL_SCORE );
		
		upgradesUsed    = bundle.getInt( UPGRADES );
		sneakAttacks    = bundle.getInt( SNEAKS );
		thrownAttacks   = bundle.getInt( THROWN );

		spawnersAlive   = bundle.getInt( SPAWNERS );
		
		duration		= bundle.getFloat( DURATION );

		qualifiedForNoKilling = bundle.getBoolean( NO_KILLING_QUALIFIED );

		qualifiedForBossChallengeBadge = bundle.getBoolean( BOSS_CHALLENGE_QUALIFIED );
		
		amuletObtained	= bundle.getBoolean( AMULET );
		gameWon         = bundle.getBoolean( WON );
		ascended        = bundle.getBoolean( ASCENDED );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ){
		info.goldCollected  = bundle.getInt( GOLD );
		info.maxDepth       = bundle.getInt( DEEPEST );
	}

}
