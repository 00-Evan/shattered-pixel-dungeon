/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredicedungeon.levels;

import com.shatteredicedungeon.Assets;
import com.shatteredicedungeon.Challenges;
import com.shatteredicedungeon.Dungeon;
import com.shatteredicedungeon.Statistics;
import com.shatteredicedungeon.actors.Actor;
import com.shatteredicedungeon.actors.Char;
import com.shatteredicedungeon.actors.blobs.Alchemy;
import com.shatteredicedungeon.actors.blobs.Blob;
import com.shatteredicedungeon.actors.blobs.WellWater;
import com.shatteredicedungeon.actors.buffs.Awareness;
import com.shatteredicedungeon.actors.buffs.Blindness;
import com.shatteredicedungeon.actors.buffs.Buff;
import com.shatteredicedungeon.actors.buffs.LockedFloor;
import com.shatteredicedungeon.actors.buffs.MindVision;
import com.shatteredicedungeon.actors.buffs.Shadows;
import com.shatteredicedungeon.actors.hero.Hero;
import com.shatteredicedungeon.actors.hero.HeroClass;
import com.shatteredicedungeon.actors.mobs.Bestiary;
import com.shatteredicedungeon.actors.mobs.Mob;
import com.shatteredicedungeon.effects.particles.FlowParticle;
import com.shatteredicedungeon.effects.particles.WindParticle;
import com.shatteredicedungeon.items.Dewdrop;
import com.shatteredicedungeon.items.Generator;
import com.shatteredicedungeon.items.Heap;
import com.shatteredicedungeon.items.Item;
import com.shatteredicedungeon.items.Stylus;
import com.shatteredicedungeon.items.Torch;
import com.shatteredicedungeon.items.armor.Armor;
import com.shatteredicedungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredicedungeon.items.artifacts.DriedRose;
import com.shatteredicedungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredicedungeon.items.bags.ScrollHolder;
import com.shatteredicedungeon.items.bags.SeedPouch;
import com.shatteredicedungeon.items.food.Blandfruit;
import com.shatteredicedungeon.items.food.Food;
import com.shatteredicedungeon.items.potions.PotionOfHealing;
import com.shatteredicedungeon.items.potions.PotionOfMight;
import com.shatteredicedungeon.items.potions.PotionOfStrength;
import com.shatteredicedungeon.items.rings.RingOfWealth;
import com.shatteredicedungeon.items.scrolls.Scroll;
import com.shatteredicedungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredicedungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredicedungeon.levels.features.Chasm;
import com.shatteredicedungeon.levels.features.Door;
import com.shatteredicedungeon.levels.features.HighGrass;
import com.shatteredicedungeon.levels.painters.Painter;
import com.shatteredicedungeon.levels.traps.*;
import com.shatteredicedungeon.mechanics.ShadowCaster;
import com.shatteredicedungeon.plants.BlandfruitBush;
import com.shatteredicedungeon.plants.Plant;
import com.shatteredicedungeon.scenes.GameScene;
import com.shatteredicedungeon.sprites.ItemSprite;
import com.shatteredicedungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Level implements Bundlable {
	
	public static enum Feeling {
		NONE,
		CHASM,
		WATER,
		GRASS,
		DARK
	}
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int LENGTH = WIDTH * HEIGHT;
	
	public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1};
	public static final int[] NEIGHBOURS8 = {-WIDTH, +1-WIDTH, +1, +1+WIDTH, +WIDTH, -1+WIDTH, -1, -1-WIDTH};
	public static final int[] NEIGHBOURS9 = {0, -WIDTH, +1-WIDTH, +1, +1+WIDTH, +WIDTH, -1+WIDTH, -1, -1-WIDTH};

	//make sure to check insideMap() when using these, as there's a risk something may be outside the map
	public static final int[] NEIGHBOURS8DIST2 = {+2+2*WIDTH, +1+2*WIDTH, 2*WIDTH, -1+2*WIDTH, -2+2*WIDTH,
													+2+WIDTH, +1+WIDTH, +WIDTH, -1+WIDTH, -2+WIDTH,
													+2, +1, -1, -2,
													+2-WIDTH, +1-WIDTH, -WIDTH, -1-WIDTH, -2-WIDTH,
													+2-2*WIDTH, +1-2*WIDTH, -2*WIDTH, -1-2*WIDTH, -2-2*WIDTH};
	public static final int[] NEIGHBOURS9DIST2 = {+2+2*WIDTH, +1+2*WIDTH, 2*WIDTH, -1+2*WIDTH, -2+2*WIDTH,
													+2+WIDTH, +1+WIDTH, +WIDTH, -1+WIDTH, -2+WIDTH,
													+2, +1, 0, -1, -2,
													+2-WIDTH, +1-WIDTH, -WIDTH, -1-WIDTH, -2-WIDTH,
													+2-2*WIDTH, +1-2*WIDTH, -2*WIDTH, -1-2*WIDTH, -2-2*WIDTH};

	
	protected static final float TIME_TO_RESPAWN	= 50;
	
	private static final String TXT_HIDDEN_PLATE_CLICKS = "A hidden pressure plate clicks!";
	
	public static boolean resizingNeeded;
	public static int loadedMapSize;

	public int version;
	public int[] map;
	public boolean[] visited;
	public boolean[] mapped;

	public int viewDistance = Dungeon.isChallenged( Challenges.DARKNESS ) ? 3: 8;
	
	public static boolean[] fieldOfView = new boolean[LENGTH];
	
	public static boolean[] passable	= new boolean[LENGTH];
	public static boolean[] losBlocking	= new boolean[LENGTH];
	public static boolean[] flamable	= new boolean[LENGTH];
	public static boolean[] secret		= new boolean[LENGTH];
	public static boolean[] solid		= new boolean[LENGTH];
	public static boolean[] avoid		= new boolean[LENGTH];
	public static boolean[] water		= new boolean[LENGTH];
	public static boolean[] pit			= new boolean[LENGTH];
	
	public static boolean[] discoverable	= new boolean[LENGTH];
	
	public Feeling feeling = Feeling.NONE;
	
	public int entrance;
	public int exit;

	//when a boss level has become locked.
	public boolean locked = false;
	
	public HashSet<Mob> mobs;
	public SparseArray<Heap> heaps;
	public HashMap<Class<? extends Blob>,Blob> blobs;
	public SparseArray<Plant> plants;
	public SparseArray<Trap> traps;
	
	protected ArrayList<Item> itemsToSpawn = new ArrayList<>();
	
	public int color1 = 0x004400;
	public int color2 = 0x88CC44;
	
	protected static boolean pitRoomNeeded = false;
	protected static boolean weakFloorCreated = false;

	private static final String VERSION     = "version";
	private static final String MAP			= "map";
	private static final String VISITED		= "visited";
	private static final String MAPPED		= "mapped";
	private static final String ENTRANCE	= "entrance";
	private static final String EXIT		= "exit";
	private static final String LOCKED      = "locked";
	private static final String HEAPS		= "heaps";
	private static final String PLANTS		= "plants";
	private static final String TRAPS       = "traps";
	private static final String MOBS		= "mobs";
	private static final String BLOBS		= "blobs";
	private static final String FEELING		= "feeling";

	public void create() {
		
		resizingNeeded = false;
		
		map = new int[LENGTH];
		visited = new boolean[LENGTH];
		Arrays.fill( visited, false );
		mapped = new boolean[LENGTH];
		Arrays.fill( mapped, false );
		
		if (!Dungeon.bossLevel()) {
			addItemToSpawn( Generator.random( Generator.Category.FOOD ) );
			if (Dungeon.posNeeded()) {
				addItemToSpawn( new PotionOfStrength() );
				Dungeon.limitedDrops.strengthPotions.count++;
			}
			if (Dungeon.souNeeded()) {
				addItemToSpawn( new ScrollOfUpgrade() );
				Dungeon.limitedDrops.upgradeScrolls.count++;
			}
			if (Dungeon.asNeeded()) {
				addItemToSpawn( new Stylus() );
				Dungeon.limitedDrops.arcaneStyli.count++;
			}

			int bonus = 0;
			for (Buff buff : Dungeon.hero.buffs(RingOfWealth.Wealth.class)) {
				bonus += ((RingOfWealth.Wealth) buff).level;
			}
			if (Random.Float() > Math.pow(0.95, bonus)){
				if (Random.Int(2) == 0)
					 addItemToSpawn( new ScrollOfMagicalInfusion() );
				else
					addItemToSpawn( new PotionOfMight() );
			}

			DriedRose rose = Dungeon.hero.belongings.getItem( DriedRose.class );
			if (rose != null && !rose.cursed){
				//this way if a rose is dropped later in the game, player still has a chance to max it out.
				int petalsNeeded = (int) Math.ceil((float)((Dungeon.depth / 2) - rose.droppedPetals) / 3);

				for (int i=1; i <= petalsNeeded; i++) {
					//the player may miss a single petal and still max their rose.
					if (rose.droppedPetals < 11) {
						addItemToSpawn(new DriedRose.Petal());
						rose.droppedPetals++;
					}
				}
			}
			
			if (Dungeon.depth > 1) {
				switch (Random.Int( 10 )) {
				case 0:
					if (!Dungeon.bossLevel( Dungeon.depth + 1 )) {
						feeling = Feeling.CHASM;
					}
					break;
				case 1:
					feeling = Feeling.WATER;
					break;
				case 2:
					feeling = Feeling.GRASS;
					break;
				case 3:
					feeling = Feeling.DARK;
					addItemToSpawn(new Torch());
					viewDistance = (int)Math.ceil(viewDistance/3f);
					break;
				}
			}
		}
		
		boolean pitNeeded = Dungeon.depth > 1 && weakFloorCreated;
		
		do {
			Arrays.fill( map, feeling == Feeling.CHASM ? Terrain.CHASM : Terrain.WALL );
			
			pitRoomNeeded = pitNeeded;
			weakFloorCreated = false;

			mobs = new HashSet<>();
			heaps = new SparseArray<>();
			blobs = new HashMap<>();
			plants = new SparseArray<>();
			traps = new SparseArray<>();
			
		} while (!build());
		decorate();
		
		buildFlagMaps();
		cleanWalls();
		
		createMobs();
		createItems();
	}
	
	public void reset() {
		
		for (Mob mob : mobs.toArray( new Mob[0] )) {
			if (!mob.reset()) {
				mobs.remove( mob );
			}
		}
		createMobs();
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {

		version = bundle.getInt( VERSION );
		
		mobs = new HashSet<>();
		heaps = new SparseArray<>();
		blobs = new HashMap<>();
		plants = new SparseArray<>();
		traps = new SparseArray<>();
		
		map		= bundle.getIntArray( MAP );

		visited	= bundle.getBooleanArray( VISITED );
		mapped	= bundle.getBooleanArray( MAPPED );
		
		entrance	= bundle.getInt( ENTRANCE );
		exit		= bundle.getInt( EXIT );

		locked      = bundle.getBoolean( LOCKED );
		
		weakFloorCreated = false;
		
		adjustMapSize();

		//for pre-0.3.0c saves
		if (version < 44){
			map = Terrain.convertTrapsFrom43( map, traps );
		}
		
		Collection<Bundlable> collection = bundle.getCollection( HEAPS );
		for (Bundlable h : collection) {
			Heap heap = (Heap)h;
			if (resizingNeeded) {
				heap.pos = adjustPos( heap.pos );
			}
			if (!heap.isEmpty())
				heaps.put( heap.pos, heap );
		}
		
		collection = bundle.getCollection( PLANTS );
		for (Bundlable p : collection) {
			Plant plant = (Plant)p;
			if (resizingNeeded) {
				plant.pos = adjustPos( plant.pos );
			}
			plants.put( plant.pos, plant );
		}

		collection = bundle.getCollection( TRAPS );
		for (Bundlable p : collection) {
			Trap trap = (Trap)p;
			if (resizingNeeded) {
				trap.pos = adjustPos( trap.pos );
			}
			traps.put( trap.pos, trap );
		}
		
		collection = bundle.getCollection( MOBS );
		for (Bundlable m : collection) {
			Mob mob = (Mob)m;
			if (mob != null) {
				if (resizingNeeded) {
					mob.pos = adjustPos( mob.pos );
				}
				mobs.add( mob );
			}
		}
		
		collection = bundle.getCollection( BLOBS );
		for (Bundlable b : collection) {
			Blob blob = (Blob)b;
			blobs.put( blob.getClass(), blob );
		}

		feeling = bundle.getEnum( FEELING, Feeling.class );
		if (feeling == Feeling.DARK)
			viewDistance = (int)Math.ceil(viewDistance/3f);

		buildFlagMaps();
		cleanWalls();
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( VERSION, Game.versionCode );
		bundle.put( MAP, map );
		bundle.put( VISITED, visited );
		bundle.put( MAPPED, mapped );
		bundle.put( ENTRANCE, entrance );
		bundle.put( EXIT, exit );
		bundle.put( LOCKED, locked );
		bundle.put( HEAPS, heaps.values() );
		bundle.put( PLANTS, plants.values() );
		bundle.put( TRAPS, traps.values() );
		bundle.put( MOBS, mobs );
		bundle.put( BLOBS, blobs.values() );
		bundle.put( FEELING, feeling );
	}
	
	public int tunnelTile() {
		return feeling == Feeling.CHASM ? Terrain.EMPTY_SP : Terrain.EMPTY;
	}
	
	private void adjustMapSize() {
		// For levels saved before 1.6.3
		// Seeing as shattered started on 1.7.1 this is never used, but the code may be resused in future.
		if (map.length < LENGTH) {
			
			resizingNeeded = true;
			loadedMapSize = (int)Math.sqrt( map.length );
			
			int[] map = new int[LENGTH];
			Arrays.fill( map, Terrain.WALL );
			
			boolean[] visited = new boolean[LENGTH];
			Arrays.fill( visited, false );
			
			boolean[] mapped = new boolean[LENGTH];
			Arrays.fill( mapped, false );
			
			for (int i=0; i < loadedMapSize; i++) {
				System.arraycopy( this.map, i * loadedMapSize, map, i * WIDTH, loadedMapSize );
				System.arraycopy( this.visited, i * loadedMapSize, visited, i * WIDTH, loadedMapSize );
				System.arraycopy( this.mapped, i * loadedMapSize, mapped, i * WIDTH, loadedMapSize );
			}
			
			this.map = map;
			this.visited = visited;
			this.mapped = mapped;
			
			entrance = adjustPos( entrance );
			exit = adjustPos( exit );
		} else {
			resizingNeeded = false;
		}
	}
	
	public int adjustPos( int pos ) {
		return (pos / loadedMapSize) * WIDTH + (pos % loadedMapSize);
	}
	
	public String tilesTex() {
		return null;
	}
	
	public String waterTex() {
		return null;
	}
	
	abstract protected boolean build();

	abstract protected void decorate();

	abstract protected void createMobs();

	abstract protected void createItems();

	public void seal(){
		if (!locked) {
			locked = true;
			Buff.affect(Dungeon.hero, LockedFloor.class);
		}
	}

	public void unseal(){
		if (locked) {
			locked = false;
		}
	}

	public void addVisuals( Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (pit[i]) {
				scene.add( new WindParticle.Wind( i ) );
				if (i >= WIDTH && water[i-WIDTH]) {
					scene.add( new FlowParticle.Flow( i - WIDTH ) );
				}
			}
		}
	}
	
	public int nMobs() {
		return 0;
	}

	public Mob findMob( int pos ){
		for (Mob mob : mobs){
			if (mob.pos == pos){
				return mob;
			}
		}
		return null;
	}
	
	public Actor respawner() {
		return new Actor() {

			{
				actPriority = 1; //as if it were a buff.
			}

			@Override
			protected boolean act() {
				if (mobs.size() < nMobs()) {

					Mob mob = Bestiary.mutable( Dungeon.depth );
					mob.state = mob.WANDERING;
					mob.pos = randomRespawnCell();
					if (Dungeon.hero.isAlive() && mob.pos != -1) {
						GameScene.add( mob );
						if (Statistics.amuletObtained) {
							mob.beckon( Dungeon.hero.pos );
						}
					}
				}
				spend( Dungeon.level.feeling == Feeling.DARK || Statistics.amuletObtained ? TIME_TO_RESPAWN / 2 : TIME_TO_RESPAWN );
				return true;
			}
		};
	}
	
	public int randomRespawnCell() {
		int cell;
		do {
			cell = Random.Int( LENGTH );
		} while (!passable[cell] || Dungeon.visible[cell] || Actor.findChar( cell ) != null);
		return cell;
	}
	
	public int randomDestination() {
		int cell;
		do {
			cell = Random.Int( LENGTH );
		} while (!passable[cell]);
		return cell;
	}
	
	public void addItemToSpawn( Item item ) {
		if (item != null) {
			itemsToSpawn.add( item );
		}
	}

	public Item findPrizeItem(){ return findPrizeItem(null); }

	public Item findPrizeItem(Class<?extends Item> match){
		if (itemsToSpawn.size() == 0)
			return null;

		if (match == null){
			Item item = Random.element(itemsToSpawn);
			itemsToSpawn.remove(item);
			return item;
		}

		for (Item item : itemsToSpawn){
			if (match.isInstance(item)){
				itemsToSpawn.remove( item );
				return item;
			}
		}

		return null;
	}
	
	private void buildFlagMaps() {
		
		for (int i=0; i < LENGTH; i++) {
			int flags = Terrain.flags[map[i]];
			passable[i]		= (flags & Terrain.PASSABLE) != 0;
			losBlocking[i]	= (flags & Terrain.LOS_BLOCKING) != 0;
			flamable[i]		= (flags & Terrain.FLAMABLE) != 0;
			secret[i]		= (flags & Terrain.SECRET) != 0;
			solid[i]		= (flags & Terrain.SOLID) != 0;
			avoid[i]		= (flags & Terrain.AVOID) != 0;
			water[i]		= (flags & Terrain.LIQUID) != 0;
			pit[i]			= (flags & Terrain.PIT) != 0;
		}
		
		int lastRow = LENGTH - WIDTH;
		for (int i=0; i < WIDTH; i++) {
			passable[i] = avoid[i] = false;
			passable[lastRow + i] = avoid[lastRow + i] = false;
		}
		for (int i=WIDTH; i < lastRow; i += WIDTH) {
			passable[i] = avoid[i] = false;
			passable[i + WIDTH-1] = avoid[i + WIDTH-1] = false;
		}
		 
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			
			if (water[i]) {
				int t = Terrain.WATER_TILES;
				for (int j=0; j < NEIGHBOURS4.length; j++) {
					if ((Terrain.flags[map[i + NEIGHBOURS4[j]]] & Terrain.UNSTITCHABLE) != 0) {
						t += 1 << j;
					}
				}
				map[i] = t;
			}
			
			if (pit[i]) {
				if (!pit[i - WIDTH]) {
					int c = map[i - WIDTH];
					if (c == Terrain.EMPTY_SP || c == Terrain.STATUE_SP) {
						map[i] = Terrain.CHASM_FLOOR_SP;
					} else if (water[i - WIDTH]) {
						map[i] = Terrain.CHASM_WATER;
					} else if ((Terrain.flags[c] & Terrain.UNSTITCHABLE) != 0) {
						map[i] = Terrain.CHASM_WALL;
					} else {
						map[i] = Terrain.CHASM_FLOOR;
					}
				}
			}
		}
	}
	
	private void cleanWalls() {
		for (int i=0; i < LENGTH; i++) {
			
			boolean d = false;
			
			for (int j=0; j < NEIGHBOURS9.length; j++) {
				int n = i + NEIGHBOURS9[j];
				if (n >= 0 && n < LENGTH && map[n] != Terrain.WALL && map[n] != Terrain.WALL_DECO) {
					d = true;
					break;
				}
			}
			
			if (d) {
				d = false;
				
				for (int j=0; j < NEIGHBOURS9.length; j++) {
					int n = i + NEIGHBOURS9[j];
					if (n >= 0 && n < LENGTH && !pit[n]) {
						d = true;
						break;
					}
				}
			}
			
			discoverable[i] = d;
		}
	}
	
	public static void set( int cell, int terrain ) {
		Painter.set( Dungeon.level, cell, terrain );

		if (terrain != Terrain.TRAP && terrain != Terrain.SECRET_TRAP){
			Dungeon.level.traps.remove( cell );
		}

		int flags = Terrain.flags[terrain];
		passable[cell]		= (flags & Terrain.PASSABLE) != 0;
		losBlocking[cell]	= (flags & Terrain.LOS_BLOCKING) != 0;
		flamable[cell]		= (flags & Terrain.FLAMABLE) != 0;
		secret[cell]		= (flags & Terrain.SECRET) != 0;
		solid[cell]			= (flags & Terrain.SOLID) != 0;
		avoid[cell]			= (flags & Terrain.AVOID) != 0;
		pit[cell]			= (flags & Terrain.PIT) != 0;
		water[cell]			= terrain == Terrain.WATER || terrain >= Terrain.WATER_TILES;
	}
	
	public Heap drop( Item item, int cell ) {

		//This messy if statement deals will items which should not drop in challenges primarily.
		if ((Dungeon.isChallenged( Challenges.NO_FOOD ) && (item instanceof Food || item instanceof BlandfruitBush.Seed)) ||
			(Dungeon.isChallenged( Challenges.NO_ARMOR ) && item instanceof Armor) ||
			(Dungeon.isChallenged( Challenges.NO_HEALING ) && item instanceof PotionOfHealing) ||
			(Dungeon.isChallenged( Challenges.NO_HERBALISM ) && (item instanceof Plant.Seed || item instanceof Dewdrop || item instanceof SeedPouch)) ||
			(Dungeon.isChallenged( Challenges.NO_SCROLLS ) && ((item instanceof Scroll && !(item instanceof ScrollOfUpgrade)) || item instanceof ScrollHolder)) ||
			item == null) {

			//create a dummy heap, give it a dummy sprite, don't add it to the game, and return it.
			//effectively nullifies whatever the logic calling this wants to do, including dropping items.
			Heap heap = new Heap();
			ItemSprite sprite = heap.sprite = new ItemSprite();
			sprite.link( heap );
			return heap;

		}

		if ((map[cell] == Terrain.ALCHEMY) && (
				!(item instanceof Plant.Seed || item instanceof Blandfruit) ||
				item instanceof BlandfruitBush.Seed ||
				(item instanceof Blandfruit && (((Blandfruit) item).potionAttrib != null || heaps.get(cell) != null))||
				Dungeon.hero.buff(AlchemistsToolkit.alchemy.class) != null && Dungeon.hero.buff(AlchemistsToolkit.alchemy.class).isCursed())) {
			int n;
			do {
				n = cell + NEIGHBOURS8[Random.Int( 8 )];
			} while (map[n] != Terrain.EMPTY_SP);
			cell = n;
		}
		
		Heap heap = heaps.get( cell );
		if (heap == null) {
			
			heap = new Heap();
			heap.pos = cell;
			if (map[cell] == Terrain.CHASM || (Dungeon.level != null && pit[cell])) {
				Dungeon.dropToChasm( item );
				GameScene.discard( heap );
			} else {
				heaps.put( cell, heap );
				GameScene.add( heap );
			}
			
		} else if (heap.type == Heap.Type.LOCKED_CHEST || heap.type == Heap.Type.CRYSTAL_CHEST) {
			
			int n;
			do {
				n = cell + Level.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Level.passable[n] && !Level.avoid[n]);
			return drop( item, n );
			
		}
		heap.drop( item );
		
		if (Dungeon.level != null) {
			press( cell, null );
		}
				
		return heap;
	}
	
	public Plant plant( Plant.Seed seed, int pos ) {

		Plant plant = plants.get( pos );
		if (plant != null) {
			plant.wither();
		}

		if (map[pos] == Terrain.HIGH_GRASS ||
				map[pos] == Terrain.EMPTY ||
				map[pos] == Terrain.EMBERS ||
				map[pos] == Terrain.EMPTY_DECO) {
			map[pos] = Terrain.GRASS;
			flamable[pos] = true;
			GameScene.updateMap( pos );
		}
		
		plant = seed.couch( pos );
		plants.put( pos, plant );
		
		GameScene.add( plant );
		
		return plant;
	}
	
	public void uproot( int pos ) {
		plants.delete( pos );
	}

	public Trap setTrap( Trap trap, int pos ){
		trap.set( pos );
		traps.put( pos, trap );
		GameScene.add(trap);
		return trap;
	}

	public void disarmTrap( int pos ) {
		traps.delete(pos);
		set(pos, Terrain.INACTIVE_TRAP);
		GameScene.updateMap(pos);
	}

	public void discover( int cell ) {
		set( cell, Terrain.discover( map[cell] ) );
		Trap trap = traps.get( cell );
		if (trap != null)
			trap.reveal();
		GameScene.updateMap( cell );
	}
	
	public int pitCell() {
		return randomRespawnCell();
	}
	
	public void press( int cell, Char ch ) {

		if (ch != null && pit[cell] && !ch.flying) {
			if (ch == Dungeon.hero) {
				Chasm.heroFall(cell);
			} else if (ch instanceof Mob) {
				Chasm.mobFall( (Mob)ch );
			}
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean frozen = timeFreeze != null;
		
		Trap trap = null;
		
		switch (map[cell]) {
		
		case Terrain.SECRET_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.TRAP:
			trap = traps.get( cell );
			break;
			
		case Terrain.HIGH_GRASS:
			HighGrass.trample( this, cell, ch );
			break;
			
		case Terrain.WELL:
			WellWater.affectCell( cell );
			break;
			
		case Terrain.ALCHEMY:
			if (ch == null) {
				Alchemy.transmute( cell );
			}
			break;
			
		case Terrain.DOOR:
			Door.enter( cell );
			break;
		}
		
		if (trap != null && !frozen) {

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			trap.trigger();

		} else if (trap != null && frozen){

			Sample.INSTANCE.play(Assets.SND_TRAP);

			discover(cell);

			timeFreeze.setDelayedPress( cell );

		}
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			plant.trigger();
		}
	}
	
	public void mobPress( Mob mob ) {

		int cell = mob.pos;
		
		if (pit[cell] && !mob.flying) {
			Chasm.mobFall( mob );
			return;
		}
		
		Trap trap = null;
		switch (map[cell]) {
		
		case Terrain.TRAP:
			trap = traps.get( cell );
			break;
			
		case Terrain.DOOR:
			Door.enter( cell );
			break;
		}
		
		if (trap != null) {
			trap.trigger();
		}
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			plant.trigger();
		}
	}
	
	public boolean[] updateFieldOfView( Char c ) {
		
		int cx = c.pos % WIDTH;
		int cy = c.pos / WIDTH;
		
		boolean sighted = c.buff( Blindness.class ) == null && c.buff( Shadows.class ) == null
						&& c.buff( TimekeepersHourglass.timeStasis.class ) == null && c.isAlive();
		if (sighted) {
			ShadowCaster.castShadow( cx, cy, fieldOfView, c.viewDistance );
		} else {
			Arrays.fill( fieldOfView, false );
		}
		
		int sense = 1;
		if (c.isAlive()) {
			for (Buff b : c.buffs( MindVision.class )) {
				sense = Math.max( ((MindVision)b).distance, sense );
			}
		}
		
		if ((sighted && sense > 1) || !sighted) {
			
			int ax = Math.max( 0, cx - sense );
			int bx = Math.min( cx + sense, WIDTH - 1 );
			int ay = Math.max( 0, cy - sense );
			int by = Math.min( cy + sense, HEIGHT - 1 );

			int len = bx - ax + 1;
			int pos = ax + ay * WIDTH;
			for (int y = ay; y <= by; y++, pos+=WIDTH) {
				Arrays.fill( fieldOfView, pos, pos + len, true );
			}
			
			for (int i=0; i < LENGTH; i++) {
				fieldOfView[i] &= discoverable[i];
			}
		}
		
		if (c.isAlive()) {
			if (c.buff( MindVision.class ) != null) {
				for (Mob mob : mobs) {
					int p = mob.pos;
					fieldOfView[p] = true;
					fieldOfView[p + 1] = true;
					fieldOfView[p - 1] = true;
					fieldOfView[p + WIDTH + 1] = true;
					fieldOfView[p + WIDTH - 1] = true;
					fieldOfView[p - WIDTH + 1] = true;
					fieldOfView[p - WIDTH - 1] = true;
					fieldOfView[p + WIDTH] = true;
					fieldOfView[p - WIDTH] = true;
				}
			} else if (c == Dungeon.hero && ((Hero)c).heroClass == HeroClass.HUNTRESS) {
				for (Mob mob : mobs) {
					int p = mob.pos;
					if (distance( c.pos, p) == 2) {
						fieldOfView[p] = true;
						fieldOfView[p + 1] = true;
						fieldOfView[p - 1] = true;
						fieldOfView[p + WIDTH + 1] = true;
						fieldOfView[p + WIDTH - 1] = true;
						fieldOfView[p - WIDTH + 1] = true;
						fieldOfView[p - WIDTH - 1] = true;
						fieldOfView[p + WIDTH] = true;
						fieldOfView[p - WIDTH] = true;
					}
				}
			}
			if (c.buff( Awareness.class ) != null) {
				for (Heap heap : heaps.values()) {
					int p = heap.pos;
					fieldOfView[p] = true;
					fieldOfView[p + 1] = true;
					fieldOfView[p - 1] = true;
					fieldOfView[p + WIDTH + 1] = true;
					fieldOfView[p + WIDTH - 1] = true;
					fieldOfView[p - WIDTH + 1] = true;
					fieldOfView[p - WIDTH - 1] = true;
					fieldOfView[p + WIDTH] = true;
					fieldOfView[p - WIDTH] = true;
				}
			}
		}
		
		return fieldOfView;
	}
	
	public static int distance( int a, int b ) {
		int ax = a % WIDTH;
		int ay = a / WIDTH;
		int bx = b % WIDTH;
		int by = b / WIDTH;
		return Math.max( Math.abs( ax - bx ), Math.abs( ay - by ) );
	}
	
	public static boolean adjacent( int a, int b ) {
		int diff = Math.abs( a - b );
		return diff == 1 || diff == WIDTH || diff == WIDTH + 1 || diff == WIDTH - 1;
	}

	//returns true if the input is a valid tile within the level
	public static boolean insideMap( int tile ){
				//outside map array
		return !((tile <= -1 || tile >= LENGTH) ||
				//top and bottom row
				 (tile <= 31 || tile >= LENGTH - WIDTH) ||
				//left and right column
				(tile % WIDTH == 0 || tile % WIDTH == 31));
	}
	
	public String tileName( int tile ) {
		
		if (tile >= Terrain.WATER_TILES) {
			return tileName( Terrain.WATER );
		}
		
		if (tile != Terrain.CHASM && (Terrain.flags[tile] & Terrain.PIT) != 0) {
			return tileName( Terrain.CHASM );
		}
		
		switch (tile) {
		case Terrain.CHASM:
			return "Chasm";
		case Terrain.EMPTY:
		case Terrain.EMPTY_SP:
		case Terrain.EMPTY_DECO:
		case Terrain.SECRET_TRAP:
			return "Floor";
		case Terrain.GRASS:
			return "Grass";
		case Terrain.WATER:
			return "Water";
		case Terrain.WALL:
		case Terrain.WALL_DECO:
		case Terrain.SECRET_DOOR:
			return "Wall";
		case Terrain.DOOR:
			return "Closed door";
		case Terrain.OPEN_DOOR:
			return "Open door";
		case Terrain.ENTRANCE:
			return "Depth entrance";
		case Terrain.EXIT:
			return "Depth exit";
		case Terrain.EMBERS:
			return "Embers";
		case Terrain.LOCKED_DOOR:
			return "Locked door";
		case Terrain.PEDESTAL:
			return "Pedestal";
		case Terrain.BARRICADE:
			return "Barricade";
		case Terrain.HIGH_GRASS:
			return "High grass";
		case Terrain.LOCKED_EXIT:
			return "Locked depth exit";
		case Terrain.UNLOCKED_EXIT:
			return "Unlocked depth exit";
		case Terrain.SIGN:
			return "Sign";
		case Terrain.WELL:
			return "Well";
		case Terrain.EMPTY_WELL:
			return "Empty well";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "Statue";
		case Terrain.INACTIVE_TRAP:
			return "Triggered trap";
		case Terrain.BOOKSHELF:
			return "Bookshelf";
		case Terrain.ALCHEMY:
			return "Alchemy pot";
		default:
			return "???";
		}
	}
	
	public String tileDesc( int tile ) {
		
		switch (tile) {
		case Terrain.CHASM:
			return "You can't see the bottom.";
		case Terrain.WATER:
			return "In case of burning step into the water to extinguish the fire.";
		case Terrain.ENTRANCE:
			return "Stairs lead up to the upper depth.";
		case Terrain.EXIT:
		case Terrain.UNLOCKED_EXIT:
			return "Stairs lead down to the lower depth.";
		case Terrain.EMBERS:
			return "Embers cover the floor.";
		case Terrain.HIGH_GRASS:
			return "Dense vegetation blocks the view.";
		case Terrain.LOCKED_DOOR:
			return "This door is locked, you need a matching key to unlock it.";
		case Terrain.LOCKED_EXIT:
			return "Heavy bars block the stairs leading down.";
		case Terrain.BARRICADE:
			return "The wooden barricade is firmly set but has dried over the years. Might it burn?";
		case Terrain.SIGN:
			return "You can't read the text from here.";
		case Terrain.INACTIVE_TRAP:
			return "The trap has been triggered before and it's not dangerous anymore.";
		case Terrain.STATUE:
		case Terrain.STATUE_SP:
			return "Someone wanted to adorn this place, but failed, obviously.";
		case Terrain.ALCHEMY:
			return "Drop some seeds here to cook a potion.";
		case Terrain.EMPTY_WELL:
			return "The well has run dry.";
		default:
			if (tile >= Terrain.WATER_TILES) {
				return tileDesc( Terrain.WATER );
			}
			if ((Terrain.flags[tile] & Terrain.PIT) != 0) {
				return tileDesc( Terrain.CHASM );
			}
			return "";
		}
	}
}
