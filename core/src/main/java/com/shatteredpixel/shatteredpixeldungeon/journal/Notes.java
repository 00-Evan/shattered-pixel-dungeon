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

package com.shatteredpixel.shatteredpixeldungeon.journal;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Foliage;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SacrificialFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfAwareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.WaterOfHealth;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DemonSpawner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.Key;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.WeakFloorRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ImpSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.StatueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Notes {
	
	public static abstract class Record implements Comparable<Record>, Bundlable {

		//TODO currently notes can only relate to branch = 0, add branch support here if that changes
		protected int depth;

		public int depth(){
			return depth;
		}

		public Image icon() { return Icons.STAIRS.get(); }

		public Visual secondIcon() { return null; }

		public int quantity() { return 1; }
		
		public abstract String title();

		public abstract String desc();
		
		@Override
		public abstract boolean equals(Object obj);
		
		@Override
		public int compareTo( Record another ) {
			return another.depth() - depth();
		}
		
		private static final String DEPTH	= "depth";
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			depth = bundle.getInt( DEPTH );
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
			bundle.put( DEPTH, depth );
		}
	}
	
	public enum Landmark {
		CHASM_FLOOR,
		WATER_FLOOR,
		GRASS_FLOOR,
		DARK_FLOOR,
		LARGE_FLOOR,
		TRAPS_FLOOR,
		SECRETS_FLOOR,

		SHOP,
		ALCHEMY,
		GARDEN,
		DISTANT_WELL,
		WELL_OF_HEALTH,
		WELL_OF_AWARENESS,
		SACRIFICIAL_FIRE,
		STATUE,
		
		GHOST,
		RAT_KING,
		WANDMAKER,
		TROLL,
		IMP,

		DEMON_SPAWNER;
	}
	
	public static class LandmarkRecord extends Record {
		
		protected Landmark landmark;
		
		public LandmarkRecord() {}
		
		public LandmarkRecord(Landmark landmark, int depth ) {
			this.landmark = landmark;
			this.depth = depth;
		}

		public Image icon(){
			switch (landmark){
				default:
					return Icons.STAIRS.get();

				case CHASM_FLOOR:
					return Icons.STAIRS_CHASM.get();
				case WATER_FLOOR:
					return Icons.STAIRS_WATER.get();
				case GRASS_FLOOR:
					return Icons.STAIRS_GRASS.get();
				case DARK_FLOOR:
					return Icons.STAIRS_DARK.get();
				case LARGE_FLOOR:
					return Icons.STAIRS_LARGE.get();
				case TRAPS_FLOOR:
					return Icons.STAIRS_TRAPS.get();
				case SECRETS_FLOOR:
					return Icons.STAIRS_SECRETS.get();

				case SHOP:
					if (depth == 20)    return new Image(new ImpSprite());
					else                return new Image(new ShopkeeperSprite());
				case ALCHEMY:
					return Icons.get(Icons.ALCHEMY);
				case GARDEN:
					return Icons.get(Icons.GRASS);
				case DISTANT_WELL:
					return Icons.get(Icons.DISTANT_WELL);
				case WELL_OF_HEALTH:
					return Icons.get(Icons.WELL_HEALTH);
				case WELL_OF_AWARENESS:
					return Icons.get(Icons.WELL_AWARENESS);
				case SACRIFICIAL_FIRE:
					return Icons.get(Icons.SACRIFICE_ALTAR);
				case STATUE:
					return new Image(new StatueSprite());

				case GHOST:
					return new Image(new GhostSprite());
				case RAT_KING:
					return new Image(new RatKingSprite());
				case WANDMAKER:
					return new Image(new WandmakerSprite());
				case TROLL:
					return new Image(new BlacksmithSprite());
				case IMP:
					return new Image(new ImpSprite());

				case DEMON_SPAWNER:
					return new Image(new SpawnerSprite());
			}
		}

		@Override
		public String title() {
			switch (landmark) {
				default:            return Messages.get(Landmark.class, landmark.name());
				case CHASM_FLOOR:   return Messages.get(Level.Feeling.class, "chasm_title");
				case WATER_FLOOR:   return Messages.get(Level.Feeling.class, "water_title");
				case GRASS_FLOOR:   return Messages.get(Level.Feeling.class, "grass_title");
				case DARK_FLOOR:    return Messages.get(Level.Feeling.class, "dark_title");
				case LARGE_FLOOR:   return Messages.get(Level.Feeling.class, "large_title");
				case TRAPS_FLOOR:   return Messages.get(Level.Feeling.class, "traps_title");
				case SECRETS_FLOOR: return Messages.get(Level.Feeling.class, "secrets_title");
			}
		}

		@Override
		public String desc() {
			switch (landmark) {
				default:            return "";

				case CHASM_FLOOR:   return Messages.get(Level.Feeling.class, "chasm_desc");
				case WATER_FLOOR:   return Messages.get(Level.Feeling.class, "water_desc");
				case GRASS_FLOOR:   return Messages.get(Level.Feeling.class, "grass_desc");
				case DARK_FLOOR:    return Messages.get(Level.Feeling.class, "dark_desc");
				case LARGE_FLOOR:   return Messages.get(Level.Feeling.class, "large_desc");
				case TRAPS_FLOOR:   return Messages.get(Level.Feeling.class, "traps_desc");
				case SECRETS_FLOOR: return Messages.get(Level.Feeling.class, "secrets_desc");

				case SHOP:
					if (depth == 20)    return Messages.get(ImpShopkeeper.class, "desc");
					else                return Messages.get(Shopkeeper.class, "desc");
				case ALCHEMY:           return Messages.get(Level.class, "alchemy_desc");
				case GARDEN:            return Messages.get(Foliage.class, "desc");
				case DISTANT_WELL:      return Messages.get(WeakFloorRoom.HiddenWell.class, "desc");
				case WELL_OF_HEALTH:    return Messages.get(WaterOfHealth.class, "desc");
				case WELL_OF_AWARENESS: return Messages.get(WaterOfAwareness.class, "desc");
				case SACRIFICIAL_FIRE:  return Messages.get(SacrificialFire.class, "desc");
				case STATUE:            return Messages.get(Statue.class, "desc");

				case GHOST:         return Messages.get(Ghost.class, "desc");
				case RAT_KING:      return Messages.get(RatKing.class, "desc");
				case WANDMAKER:     return Messages.get(Wandmaker.class, "desc");
				case TROLL:         return Messages.get(Blacksmith.class, "desc");
				case IMP:           return Messages.get(Imp.class, "desc");

				case DEMON_SPAWNER: return Messages.get(DemonSpawner.class, "desc");
			}
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof LandmarkRecord)
					&& landmark == ((LandmarkRecord) obj).landmark
					&& depth() == ((LandmarkRecord) obj).depth();
		}
		
		private static final String LANDMARK	= "landmark";
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			landmark = Landmark.valueOf(bundle.getString(LANDMARK));
		}
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( LANDMARK, landmark.name() );
		}
	}
	
	public static class KeyRecord extends Record {
		
		protected Key key;
		
		public KeyRecord() {}
		
		public KeyRecord( Key key ){
			this.key = key;
		}
		
		@Override
		public int depth() {
			return key.depth;
		}

		@Override
		public Image icon() {
			return new ItemSprite(key);
		}

		@Override
		public Visual secondIcon() {
			if (quantity() > 1){
				BitmapText text = new BitmapText(Integer.toString(quantity()), PixelScene.pixelFont);
				text.measure();
				return text;
			} else {
				return null;
			}
		}

		@Override
		public String title() {
			return key.title();
		}

		@Override
		public String desc() {
			return key.desc();
		}
		
		public Class<? extends Key> type(){
			return key.getClass();
		}

		public int quantity(){
			return key.quantity();
		}
		
		public void quantity(int num){
			key.quantity(num);
		}
		
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof KeyRecord)
					&& key.isSimilar(((KeyRecord) obj).key);
		}
		
		private static final String KEY	= "key";
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			key = (Key) bundle.get(KEY);
		}
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( KEY, key );
		}
	}
	
	private static ArrayList<Record> records;
	
	public static void reset() {
		records = new ArrayList<>();
	}
	
	private static final String RECORDS	= "records";
	
	public static void storeInBundle( Bundle bundle ) {
		bundle.put( RECORDS, records );
	}
	
	public static void restoreFromBundle( Bundle bundle ) {
		records = new ArrayList<>();
		for (Bundlable rec : bundle.getCollection( RECORDS ) ) {
			records.add( (Record) rec );
		}
	}
	
	public static boolean add( Landmark landmark ) {
		LandmarkRecord l = new LandmarkRecord( landmark, Dungeon.depth );
		if (!records.contains(l)) {
			boolean result = records.add(new LandmarkRecord(landmark, Dungeon.depth));
			Collections.sort(records);
			return result;
		}
		return false;
	}
	
	public static boolean remove( Landmark landmark ) {
		return records.remove( new LandmarkRecord(landmark, Dungeon.depth) );
	}
	
	public static boolean add( Key key ){
		KeyRecord k = new KeyRecord(key);
		if (!records.contains(k)){
			boolean result = records.add(k);
			Collections.sort(records);
			return result;
		} else {
			k = (KeyRecord) records.get(records.indexOf(k));
			k.quantity(k.quantity() + key.quantity());
			return true;
		}
	}
	
	public static boolean remove( Key key ){
		KeyRecord k = new KeyRecord( key );
		if (records.contains(k)){
			Catalog.countUses(key.getClass(), key.quantity());
			k = (KeyRecord) records.get(records.indexOf(k));
			k.quantity(k.quantity() - key.quantity());
			if (k.quantity() <= 0){
				records.remove(k);
			}
			return true;
		}
		return false;
	}
	
	public static int keyCount( Key key ){
		KeyRecord k = new KeyRecord( key );
		if (records.contains(k)){
			k = (KeyRecord) records.get(records.indexOf(k));
			return k.quantity();
		} else {
			return 0;
		}
	}
	
	public static ArrayList<Record> getRecords(){
		return getRecords(Record.class);
	}
	
	public static <T extends Record> ArrayList<T> getRecords( Class<T> recordType ){
		ArrayList<T> filtered = new ArrayList<>();
		for (Record rec : records){
			if (recordType.isInstance(rec)){
				filtered.add((T)rec);
			}
		}
		return filtered;
	}

	public static ArrayList<Record> getRecords(int depth){
		ArrayList<Record> filtered = new ArrayList<>();
		for (Record rec : records){
			if (rec.depth() == depth){
				filtered.add(rec);
			}
		}

		Collections.sort(filtered, comparator);

		return filtered;
	}

	private static final Comparator<Record> comparator = new Comparator<Record>() {
		@Override
		public int compare(Record r1, Record r2) {
			if (r1 instanceof LandmarkRecord){
				if (r2 instanceof LandmarkRecord){
					return ((LandmarkRecord) r1).landmark.ordinal() - ((LandmarkRecord) r2).landmark.ordinal();
				} else {
					return -1;
				}
			} else if (r2 instanceof LandmarkRecord){
				return 1;
			} else {
				//matches order in key display
				return Generator.Category.order(((KeyRecord)r2).key) - Generator.Category.order(((KeyRecord)r1).key);
			}
		}
	};
	
	public static void remove( Record rec ){
		records.remove(rec);
	}
	
}
