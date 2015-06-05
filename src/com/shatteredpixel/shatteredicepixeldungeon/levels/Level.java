/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.levels;

import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.Challenges;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.Statistics;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Char;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.Alchemy;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.WellWater;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredicepixeldungeon.actors.buffs.Shadows;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Bestiary;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.FlowParticle;
import com.shatteredpixel.shatteredicepixeldungeon.effects.particles.WindParticle;
import com.shatteredpixel.shatteredicepixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredicepixeldungeon.items.Generator;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredicepixeldungeon.items.Torch;
import com.shatteredpixel.shatteredicepixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredicepixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredicepixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredicepixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredicepixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredicepixeldungeon.items.bags.SeedPouch;
import com.shatteredpixel.shatteredicepixeldungeon.items.food.Blandfruit;
import com.shatteredpixel.shatteredicepixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfMight;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredicepixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredicepixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.shatteredicepixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredicepixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredicepixeldungeon.levels.features.HighGrass;
import com.shatteredpixel.shatteredicepixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredicepixeldungeon.levels.traps.*;
import com.shatteredpixel.shatteredicepixeldungeon.mechanics.ShadowCaster;
import com.shatteredpixel.shatteredicepixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredicepixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
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
	};

	//nd ice change from 32 to 64
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	public static final int LENGTH = WIDTH * HEIGHT;
	
	public static final int[] NEIGHBOURS4 = {-WIDTH, +1, +WIDTH, -1}; 
	public static final int[] NEIGHBOURS8 = {+1, -1, +WIDTH, -WIDTH, +1+WIDTH, +1-WIDTH, -1+WIDTH, -1-WIDTH};
	public static final int[] NEIGHBOURS9 = {0, +1, -1, +WIDTH, -WIDTH, +1+WIDTH, +1-WIDTH, -1+WIDTH, -1-WIDTH};

    //Note that use of these without checking values is unsafe, mobs can be within 2 tiles of the
    //edge of the map, unsafe use in that cause will cause an array out of bounds exception.
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
	
	protected ArrayList<Item> itemsToSpawn = new ArrayList<Item>();
	
	public int color1 = 0x004400;
	public int color2 = 0x88CC44;
	
	protected static boolean pitRoomNeeded = false;
	protected static boolean weakFloorCreated = false;
	
	private static final String MAP			= "map";
	private static final String VISITED		= "visited";
	private static final String MAPPED		= "mapped";
	private static final String ENTRANCE	= "entrance";
	private static final String EXIT		= "exit";
    private static final String LOCKED      = "locked";
	private static final String HEAPS		= "heaps";
	private static final String PLANTS		= "plants";
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
		
		mobs = new HashSet<Mob>();
		heaps = new SparseArray<Heap>();
		blobs = new HashMap<Class<? extends Blob>,Blob>();
		plants = new SparseArray<Plant>();
		
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
			
		} while (!build());
		decorate();
		
		buildFlagMaps();
		if (Dungeon.depth > 5) {
			cleanWalls();
		}
		
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
		
		mobs = new HashSet<Mob>();
		heaps = new SparseArray<Heap>();
		blobs = new HashMap<Class<? extends Blob>, Blob>();
		plants = new SparseArray<Plant>();
		
		map		= bundle.getIntArray( MAP );
		visited	= bundle.getBooleanArray( VISITED );
		mapped	= bundle.getBooleanArray( MAPPED );
		
		entrance	= bundle.getInt( ENTRANCE );
		exit		= bundle.getInt( EXIT );

        locked      = bundle.getBoolean( LOCKED );
		
		weakFloorCreated = false;
		
		adjustMapSize();
		
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
		bundle.put( MAP, map );
		bundle.put( VISITED, visited );
		bundle.put( MAPPED, mapped );
		bundle.put( ENTRANCE, entrance );
		bundle.put( EXIT, exit );
        bundle.put( LOCKED, locked );
		bundle.put( HEAPS, heaps.values() );
		bundle.put( PLANTS, plants.values() );
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
	
	public Actor respawner() {
		return new Actor() {	
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
	
	public int pitCell() {
		return randomRespawnCell();
	}
	
	public void press( int cell, Char ch ) {

		if (pit[cell] && ch == Dungeon.hero) {
			Chasm.heroFall( cell );
			return;
		}

		TimekeepersHourglass.timeFreeze timeFreeze = null;

		if (ch != null)
			timeFreeze = ch.buff(TimekeepersHourglass.timeFreeze.class);

		boolean frozen = timeFreeze != null;
		
		boolean trap = false;
		
		switch (map[cell]) {
		
		case Terrain.SECRET_TOXIC_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.TOXIC_TRAP:
			trap = true;
			if (!frozen) ToxicTrap.trigger( cell, ch );
			break;
			
		case Terrain.SECRET_FIRE_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.FIRE_TRAP:
			trap = true;
			if (!frozen) FireTrap.trigger( cell, ch );
			break;
			
		case Terrain.SECRET_PARALYTIC_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.PARALYTIC_TRAP:
			trap = true;
			if (!frozen) ParalyticTrap.trigger( cell,  ch );
			break;
			
		case Terrain.SECRET_POISON_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.POISON_TRAP:
			trap = true;
			if (!frozen) PoisonTrap.trigger( cell, ch );
			break;
			
		case Terrain.SECRET_ALARM_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.ALARM_TRAP:
			trap = true;
			if (!frozen) AlarmTrap.trigger( cell, ch );
			break;
			
		case Terrain.SECRET_LIGHTNING_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.LIGHTNING_TRAP:
			trap = true;
			if (!frozen) LightningTrap.trigger( cell, ch );
			break;
			
		case Terrain.SECRET_GRIPPING_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.GRIPPING_TRAP:
			trap = true;
			if (!frozen) GrippingTrap.trigger( cell, ch );
			break;
			
		case Terrain.SECRET_SUMMONING_TRAP:
			GLog.i( TXT_HIDDEN_PLATE_CLICKS );
		case Terrain.SUMMONING_TRAP:
			trap = true;
			if (!frozen) SummoningTrap.trigger( cell, ch );
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
		
		if (trap && !frozen) {

			if (Dungeon.visible[cell])
				Sample.INSTANCE.play( Assets.SND_TRAP );

			if (ch == Dungeon.hero)
				Dungeon.hero.interrupt();

			set( cell, Terrain.INACTIVE_TRAP );
			GameScene.updateMap( cell );

		} else if (trap && frozen){

			Sample.INSTANCE.play( Assets.SND_TRAP );

			Level.set( cell, Terrain.discover( map[cell] ) );
			GameScene.updateMap( cell );

			timeFreeze.setDelayedPress( cell );

		}
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			plant.activate( ch );
		}
	}
	
	public void mobPress( Mob mob ) {

		int cell = mob.pos;
		
		if (pit[cell] && !mob.flying) {
			Chasm.mobFall( mob );
			return;
		}
		
		boolean trap = true;
		switch (map[cell]) {
		
		case Terrain.TOXIC_TRAP:
			ToxicTrap.trigger( cell,  mob );
			break;
			
		case Terrain.FIRE_TRAP:
			FireTrap.trigger( cell, mob );
			break;
			
		case Terrain.PARALYTIC_TRAP:
			ParalyticTrap.trigger( cell,  mob );
			break;
			
		case Terrain.POISON_TRAP:
			PoisonTrap.trigger( cell, mob );
			break;
			
		case Terrain.ALARM_TRAP:
			AlarmTrap.trigger( cell,  mob );
			break;
			
		case Terrain.LIGHTNING_TRAP:
			LightningTrap.trigger( cell, mob );
			break;
		
		case Terrain.GRIPPING_TRAP:
			GrippingTrap.trigger( cell, mob );
			break;
			
		case Terrain.SUMMONING_TRAP:
			SummoningTrap.trigger( cell, mob );
			break;
			
		case Terrain.DOOR:
			Door.enter( cell );
			
		default:
			trap = false;
		}
		
		if (trap) {
			if (Dungeon.visible[cell]) {
				Sample.INSTANCE.play( Assets.SND_TRAP );
			}
			set( cell, Terrain.INACTIVE_TRAP );
			GameScene.updateMap( cell );
		}
		
		Plant plant = plants.get( cell );
		if (plant != null) {
			plant.activate( mob );
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
		case Terrain.SECRET_TOXIC_TRAP:
		case Terrain.SECRET_FIRE_TRAP:
		case Terrain.SECRET_PARALYTIC_TRAP:
		case Terrain.SECRET_POISON_TRAP:
		case Terrain.SECRET_ALARM_TRAP:
		case Terrain.SECRET_LIGHTNING_TRAP:
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
		case Terrain.TOXIC_TRAP:
			return "Toxic gas trap";
		case Terrain.FIRE_TRAP:
			return "Fire trap";
		case Terrain.PARALYTIC_TRAP:
			return "Paralytic gas trap";
		case Terrain.POISON_TRAP:
			return "Poison dart trap";
		case Terrain.ALARM_TRAP:
			return "Alarm trap";
		case Terrain.LIGHTNING_TRAP:
			return "Lightning trap";
		case Terrain.GRIPPING_TRAP:
			return "Gripping trap";
		case Terrain.SUMMONING_TRAP:
			return "Summoning trap";
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
		case Terrain.TOXIC_TRAP:
		case Terrain.FIRE_TRAP:
		case Terrain.PARALYTIC_TRAP:
		case Terrain.POISON_TRAP:
		case Terrain.ALARM_TRAP:
		case Terrain.LIGHTNING_TRAP:
		case Terrain.GRIPPING_TRAP:
		case Terrain.SUMMONING_TRAP:
			return "Stepping onto a hidden pressure plate will activate the trap.";
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
