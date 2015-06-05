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
package com.shatteredpixel.shatteredicepixeldungeon.scenes;

import java.io.IOException;
import java.util.ArrayList;

import com.shatteredpixel.shatteredicepixeldungeon.*;
import com.shatteredpixel.shatteredicepixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredicepixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredicepixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredicepixeldungeon.items.bags.SeedPouch;
import com.shatteredpixel.shatteredicepixeldungeon.items.bags.WandHolster;
import com.shatteredpixel.shatteredicepixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredicepixeldungeon.ui.LootIndicator;
import com.shatteredpixel.shatteredicepixeldungeon.ui.ResumeIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.shatteredpixel.shatteredicepixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredicepixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredicepixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredicepixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredicepixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredicepixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredicepixeldungeon.effects.EmoIcon;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredicepixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Ripple;
import com.shatteredpixel.shatteredicepixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.items.wands.WandOfBlink;
import com.shatteredpixel.shatteredicepixeldungeon.levels.Level;
import com.shatteredpixel.shatteredicepixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredicepixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredicepixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.DiscardedItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.PlantSprite;
import com.shatteredpixel.shatteredicepixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Banner;
import com.shatteredpixel.shatteredicepixeldungeon.ui.BusyIndicator;
import com.shatteredpixel.shatteredicepixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredicepixeldungeon.ui.HealthIndicator;
import com.shatteredpixel.shatteredicepixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredicepixeldungeon.ui.StatusPane;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Toast;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Toolbar;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Window;
import com.shatteredpixel.shatteredicepixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndBag.Mode;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndGame;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndStory;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class GameScene extends PixelScene {
	
	private static final String TXT_WELCOME			= "Welcome to the level %d of Pixel Dungeon!";
	private static final String TXT_WELCOME_BACK	= "Welcome back to the level %d of Pixel Dungeon!";
	
	private static final String TXT_CHASM	= "Your steps echo across the dungeon.";
	private static final String TXT_WATER	= "You hear water splashing around you.";
	private static final String TXT_GRASS	= "The smell of vegetation is thick in the air.";
	private static final String TXT_DARK	= "You can hear enemies moving in the darkness...";
	private static final String TXT_SECRETS	= "The atmosphere hints that this floor hides many secrets.";
	
	static GameScene scene;

	private SkinnedBlock water;
	private DungeonTilemap tiles;
	private FogOfWar fog;
	private HeroSprite hero;
	
	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group ripples;
	private Group plants;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	
	private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ResumeIndicator resume;
	
	@Override
	public void create() {
		
		Music.INSTANCE.play( Assets.TUNE, true );
		Music.INSTANCE.volume( 1f );
		
		ShatteredPixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( GameMath.gate(minZoom, defaultZoom + ShatteredPixelDungeon.zoom(), maxZoom));
		
		scene = this;
		
		terrain = new Group();
		add( terrain );
		
		water = new SkinnedBlock( 
			Level.WIDTH * DungeonTilemap.SIZE, 
			Level.HEIGHT * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() );
		terrain.add( water );
		
		ripples = new Group();
		terrain.add( ripples );
		
		tiles = new DungeonTilemap();
		terrain.add( tiles );
		
		Dungeon.level.addVisuals( this );
		
		plants = new Group();
		add( plants );
		
		int size = Dungeon.level.plants.size();
		for (int i=0; i < size; i++) {
			addPlantSprite( Dungeon.level.plants.valueAt( i ) );
		}
		
		heaps = new Group();
		add( heaps );
		
		size = Dungeon.level.heaps.size();
		for (int i=0; i < size; i++) {
			addHeapSprite( Dungeon.level.heaps.valueAt( i ) );
		}
		
		emitters = new Group();
		effects = new Group();
		emoicons = new Group();
		
		mobs = new Group();
		add( mobs );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}
		
		add( emitters );
		add( effects );
		
		gases = new Group();
		add( gases );
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}
		
		fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
		fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
		add( fog );
		
		brightness( ShatteredPixelDungeon.brightness() );
		
		spells = new Group();
		add( spells );
		
		statuses = new Group();
		add( statuses );
		
		add( emoicons );
		
		hero = new HeroSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );

		
		add( new HealthIndicator() );
		
		add( cellSelector = new CellSelector( tiles ) );
		
		StatusPane sb = new StatusPane();
		sb.camera = uiCamera;
		sb.setSize( uiCamera.width, 0 );
		add( sb );
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );
		
		attack = new AttackIndicator();
		attack.camera = uiCamera;
		attack.setPos(
			uiCamera.width - attack.width(),
			toolbar.top() - attack.height() );
		add( attack );

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add( loot );

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add( resume );

		layoutTags();

		log = new GameLog();
		log.camera = uiCamera;
		log.setRect( 0, toolbar.top(), attack.left(),  0 );
		add( log );
		
		if (Dungeon.depth < Statistics.deepestFloor)
			GLog.i( TXT_WELCOME_BACK, Dungeon.depth );
		else
			GLog.i( TXT_WELCOME, Dungeon.depth );
        Sample.INSTANCE.play( Assets.SND_DESCEND );
		switch (Dungeon.level.feeling) {
		case CHASM:
			GLog.w( TXT_CHASM );
			break;
		case WATER:
			GLog.w( TXT_WATER );
			break;
		case GRASS:
			GLog.w( TXT_GRASS );
			break;
		case DARK:
			GLog.w( TXT_DARK );
			break;
		default:
		}
		if (Dungeon.level instanceof RegularLevel && 
			((RegularLevel)Dungeon.level).secretDoors > Random.IntRange( 3, 4 )) {
			GLog.w( TXT_SECRETS );
		}

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add( busy );
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
			WandOfBlink.appear( Dungeon.hero, Dungeon.level.entrance );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
			break;
		case RETURN:
			WandOfBlink.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case FALL:
			Chasm.heroLand();
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
				WndStory.showChapter( WndStory.ID_SEWERS );
				break;
			case 6:
				WndStory.showChapter( WndStory.ID_PRISON );
				break;
			case 11:
				WndStory.showChapter( WndStory.ID_CAVES );
				break;
			case 16:
				WndStory.showChapter( WndStory.ID_METROPOLIS );
				break;
			case 22:
				WndStory.showChapter( WndStory.ID_HALLS );
				break;
			}
			if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
				Badges.validateNoKilling();
			}
			break;
		default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else if (item instanceof Honeypot) {
                    Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}

		Camera.main.target = hero;
		fadeIn();
	}
	
	public void destroy() {
		
		freezeEmitters = false;

        scene = null;
		Badges.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			//
		}
	}

	@Override
	public synchronized void update() {
		if (Dungeon.hero == null) {
			return;
		}

		super.update();
		
		if (!freezeEmitters) water.offset( 0, -5 * Game.elapsed );
		
		Actor.process();
		
		if (Dungeon.hero.ready && !Dungeon.hero.paralysed) {
			log.newLine();
		}

		if (tagAttack != attack.active || tagLoot != loot.visible || tagResume != resume.visible) {

            boolean atkAppearing = attack.active && !tagAttack;
            boolean lootAppearing = loot.visible && !tagLoot;
            boolean resAppearing = resume.visible && !tagResume;

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagResume = resume.visible;

			if (atkAppearing || lootAppearing || resAppearing)
                layoutTags();
		}

        cellSelector.enable(Dungeon.hero.ready);
	}

	private boolean tagAttack    = false;
	private boolean tagLoot        = false;
	private boolean tagResume    = false;

	private void layoutTags() {

		float pos = tagAttack ? attack.top() : toolbar.top();

		if (tagLoot) {
			loot.setPos( uiCamera.width - loot.width(), pos - loot.height() );
			pos = loot.top();
		}

		if (tagResume) {
			resume.setPos( uiCamera.width - resume.width(), pos - resume.height() );
		}
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add( new WndGame() );
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}
	
	public void brightness( boolean value ) {
		water.rm = water.gm = water.bm = 
		tiles.rm = tiles.gm = tiles.bm = 
			value ? 1.5f : 1.0f;
		if (value) {
			fog.am = +2f;
			fog.aa = -1f;
		} else {
			fog.am = +1f;
			fog.aa =  0f;
		}
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add( heap.sprite );
	}
	
	private void addPlantSprite( Plant plant ) {
		(plant.sprite = (PlantSprite)plants.recycle( PlantSprite.class )).reset( plant );
	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		CharSprite sprite = mob.sprite();
		sprite.visible = Dungeon.visible[mob.pos];
		mobs.add( sprite );
		sprite.link( mob );
	}
	
	private void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		add( banner );
	}
	
	// -------------------------------------------------------
	
	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		Actor.add( mob );
		Actor.occupyCell( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed( mob, delay );
		Actor.occupyCell( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		Ripple ripple = (Ripple)scene.ripples.recycle( Ripple.class );
		ripple.reset( pos );
		return ripple;
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item ) {
		scene.toolbar.pickup( item );
	}
	
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updated.set( 0, 0, Level.WIDTH, Level.HEIGHT );
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updated.union( cell % Level.WIDTH, cell / Level.WIDTH );
		}
	}
	
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		cancelCellSelector();
		scene.add( wnd );
	}
	
	public static void afterObserve() {
		if (scene != null) {
			scene.fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
			
			for (Mob mob : Dungeon.level.mobs) {
				mob.sprite.visible = Dungeon.visible[mob.pos];
			}
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play( Assets.SND_DEATH );
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		cancelCellSelector();
		
		WndBag wnd =
				mode == Mode.SEED ?
					WndBag.getBag( SeedPouch.class, listener, mode, title ) :
				mode == Mode.SCROLL ?
					WndBag.getBag( ScrollHolder.class, listener, mode, title ) :
				mode == Mode.POTION ?
					WndBag.getBag( PotionBandolier.class, listener, mode, title ) :
				mode == Mode.WAND ?
					WndBag.getBag( WandHolster.class, listener, mode, title ) :
				WndBag.lastBag( listener, mode, title );

		scene.add( wnd );
		
		return wnd;
	}
	
	static boolean cancel() {
		if (Dungeon.hero.curAction != null || Dungeon.hero.restoreHealth) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.restoreHealth = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlotButton.cancel();
	}
	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
            if (Dungeon.hero.handle( cell )) {
                Dungeon.hero.next();
            }
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
