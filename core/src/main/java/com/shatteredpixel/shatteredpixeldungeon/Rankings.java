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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.King;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Yog;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.UUID;

public enum Rankings {
	
	INSTANCE;
	
	public static final int TABLE_SIZE	= 11;
	
	public static final String RANKINGS_FILE = "rankings.dat";
	
	public ArrayList<Record> records;
	public int lastRecord;
	public int totalNumber;
	public int wonNumber;

	public void submit( boolean win, Class cause ) {

		load();
		
		Record rec = new Record();
		
		rec.cause = cause;
		rec.win		= win;
		rec.heroClass	= Dungeon.hero.heroClass;
		rec.armorTier	= Dungeon.hero.tier();
		rec.herolevel	= Dungeon.hero.lvl;
		rec.depth		= Dungeon.depth;
		rec.score	= score( win );
		
		INSTANCE.saveGameData(rec);

		rec.gameID = UUID.randomUUID().toString();
		
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
		
		totalNumber++;
		if (win) {
			wonNumber++;
		}

		Badges.validateGamesPlayed();
		
		save();
	}

	private int score( boolean win ) {
		return (Statistics.goldCollected + Dungeon.hero.lvl * (win ? 26 : Dungeon.depth ) * 100) * (win ? 2 : 1);
	}

	public static final String HERO = "hero";
	public static final String STATS = "stats";
	public static final String BADGES = "badges";
	public static final String HANDLERS = "handlers";

	public void saveGameData(Record rec){
		rec.gameData = new Bundle();

		Belongings belongings = Dungeon.hero.belongings;

		//save the hero and belongings
		ArrayList<Item> allItems = (ArrayList<Item>) belongings.backpack.items.clone();
		//remove items that won't show up in the rankings screen
		for (Item item : belongings.backpack.items.toArray( new Item[0])) {
			if (item instanceof Bag){
				for (Item bagItem : ((Bag) item).items.toArray( new Item[0])){
					if (Dungeon.quickslot.contains(bagItem)) belongings.backpack.items.add(bagItem);
				}
				belongings.backpack.items.remove(item);
			} else if (!Dungeon.quickslot.contains(item))
				belongings.backpack.items.remove(item);
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
		//include worn rings
		if (belongings.misc1 != null) belongings.backpack.items.add(belongings.misc1);
		if (belongings.misc2 != null) belongings.backpack.items.add(belongings.misc2);
		Ring.saveSelectively(handler, belongings.backpack.items);
		rec.gameData.put( HANDLERS, handler);

		//restore items now that we're done saving
		belongings.backpack.items = allItems;
	}

	public void loadGameData(Record rec){
		Bundle data = rec.gameData;

		Dungeon.hero = null;
		Dungeon.level = null;
		Generator.reset();
		Dungeon.quickslot.reset();
		QuickSlotButton.reset();

		Bundle handler = data.getBundle(HANDLERS);
		Scroll.restore(handler);
		Potion.restore(handler);
		Ring.restore(handler);

		Badges.loadLocal(data.getBundle(BADGES));

		Dungeon.hero = (Hero)data.get(HERO);

		Statistics.restoreFromBundle(data.getBundle(STATS));

	}
	
	private static final String RECORDS	= "records";
	private static final String LATEST	= "latest";
	private static final String TOTAL	= "total";
	private static final String WON     = "won";

	public void save() {
		Bundle bundle = new Bundle();
		bundle.put( RECORDS, records );
		bundle.put( LATEST, lastRecord );
		bundle.put( TOTAL, totalNumber );
		bundle.put( WON, wonNumber );

		try {
			OutputStream output = Game.instance.openFileOutput( RANKINGS_FILE, Game.MODE_PRIVATE );
			Bundle.write( bundle, output );
			output.close();
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
			InputStream input = Game.instance.openFileInput( RANKINGS_FILE );
			Bundle bundle = Bundle.read( input );
			input.close();
			
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

		} catch (IOException e) {
		}
	}

	public static class Record implements Bundlable {

		//pre 0.3.4
		public String info;
		private static final String REASON	= "reason";

		//pre 0.4.1
		public String gameFile;
		private static final String FILE    = "gameFile";

		private static final String CAUSE   = "cause";
		private static final String WIN		= "win";
		private static final String SCORE	= "score";
		private static final String TIER	= "tier";
		private static final String LEVEL	= "level";
		private static final String DEPTH	= "depth";
		private static final String DATA	= "gameData";
		private static final String ID      = "gameID";

		public Class cause;
		public boolean win;
		
		public HeroClass heroClass;
		public int armorTier;
		public int herolevel;
		public int depth;
		
		public Bundle gameData;
		public String gameID;

		public int score;

		public String desc(){
			if (cause == null) {
				if (info != null){
					//support for pre-0.3.4 saves
					if (Messages.lang() == Languages.ENGLISH) {
						return info;
					} else {
						return Messages.get(this, "something");
					}
				} else {
					return Messages.get(this, "something");
				}
			} else {
				String result = Messages.get(cause, "rankings_desc", (Messages.get(cause, "name")));
				if (result.contains("!!!NO TEXT FOUND!!!")){
					return Messages.get(this, "something");
				} else {
					return result;
				}
			}
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {

			//conversion logic for pre-0.3.4 saves
			if (bundle.contains( REASON )){
				String info = bundle.getString( REASON ).toLowerCase(Locale.ENGLISH);
				if (info.equals("obtained the amulet of yendor"))   cause = Amulet.class;
				else if (info.contains("goo"))                      cause = Goo.class;
				else if (info.contains("tengu"))                    cause = Tengu.class;
				else if (info.contains("dm-300"))                   cause = DM300.class;
				else if (info.contains("king"))                     cause = King.class;
				else if (info.contains("yog"))                      cause = Yog.class;
				else if (info.contains("fist"))                     cause = Yog.class;
				else if (info.contains("larva"))                    cause = Yog.class;
				else if (info.equals("burned to ash"))              cause = Burning.class;
				else if (info.equals("starved to death"))           cause = Hunger.class;
				else if (info.equals("succumbed to poison"))        cause = Poison.class;
				else if (info.equals("suffocated"))                 cause = ToxicGas.class;
				else if (info.equals("bled to death"))              cause = Bleeding.class;
				else if (info.equals("melted away"))                cause = Ooze.class;
				else if (info.equals("died on impact"))             cause = Chasm.class;
				//can't get the all, just keep what remains as-is
				else                                                this.info = info;
			} else {
				cause   = bundle.getClass( CAUSE );
			}

			win		= bundle.getBoolean( WIN );
			score	= bundle.getInt( SCORE );
			
			heroClass	= HeroClass.restoreInBundle( bundle );
			armorTier	= bundle.getInt( TIER );
			
			if (bundle.contains(FILE))  gameFile = bundle.getString(FILE);
			if (bundle.contains(DATA))  gameData = bundle.getBundle(DATA);
			if (bundle.contains(ID))    gameID = bundle.getString(ID);

			depth = bundle.getInt( DEPTH );
			herolevel = bundle.getInt( LEVEL );

		}
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			
			if (info != null) bundle.put( REASON, info );
			else bundle.put( CAUSE, cause );

			bundle.put( WIN, win );
			bundle.put( SCORE, score );
			
			heroClass.storeInBundle( bundle );
			bundle.put( TIER, armorTier );
			bundle.put( LEVEL, herolevel );
			bundle.put( DEPTH, depth );
			
			if (gameData != null) bundle.put( DATA, gameData );
			bundle.put( ID, gameID );
		}
	}

	private static final Comparator<Record> scoreComparator = new Comparator<Rankings.Record>() {
		@Override
		public int compare( Record lhs, Record rhs ) {
			return (int)Math.signum( rhs.score - lhs.score );
		}
	};
}
