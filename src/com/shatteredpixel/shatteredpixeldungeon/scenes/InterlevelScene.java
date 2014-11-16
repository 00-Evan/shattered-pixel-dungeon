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
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import android.os.Build;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStory;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.3f;
	
	private static final String TXT_DESCENDING	= "Descending...";
	private static final String TXT_ASCENDING	= "Ascending...";
	private static final String TXT_LOADING		= "Loading...";
    private static final String TXT_L		    = "\n\n\nDue to an issue with Android L,\nThe game may need some\n" +
            "extra time to load initially.\n\nSorry for any inconvenience,\nGoogle should fix this shortly.";
	private static final String TXT_RESURRECTING= "Resurrecting...";
	private static final String TXT_RETURNING	= "Returning...";
	private static final String TXT_FALLING		= "Falling...";
	
	private static final String ERR_FILE_NOT_FOUND	= "File not found. For some reason.";
	private static final String ERR_GENERIC			= "Something went wrong..."	;	
	
	public static enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL
	};
	public static Mode mode;
	
	public static int returnDepth;
	public static int returnPos;
	
	public static boolean noStory = false;
	
	public static boolean fallIntoPit;
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	};
	private Phase phase;
	private float timeLeft;
	
	private BitmapText message;
	
	private Thread thread;
	private String error = null;
	
	@Override
	public void create() {
		super.create();

		String text = "";
		switch (mode) {
		case DESCEND:
			text = TXT_DESCENDING;
			break;
		case ASCEND:
			text = TXT_ASCENDING;
			break;
		case CONTINUE:
			text = TXT_LOADING;
			break;
		case RESURRECT:
			text = TXT_RESURRECTING;
			break;
		case RETURN:
			text = TXT_RETURNING;
			break;
		case FALL:
			text = TXT_FALLING;
			break;
		}
		
		message = PixelScene.createText( text, 9 );
		message.measure();
		message.x = (Camera.main.width - message.width()) / 2; 
		message.y = (Camera.main.height - message.height()) / 2;
		add( message );

        if (Build.VERSION.RELEASE.equals("5.0") && Dungeon.hero == null && Dungeon.depth == 0){
            BitmapText Lwarn = PixelScene.createMultiline(TXT_L, 9);
            Lwarn.hardlight(Window.TITLE_COLOR);
            Lwarn.measure();
            Lwarn.x = (Camera.main.width - Lwarn.width()) / 2;
            Lwarn.y = message.y;
            add( Lwarn );
        }
		
		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;

		thread = new Thread() {
			@Override
			public void run() {
				
				try {
					
					Generator.reset();

					Sample.INSTANCE.load(
						Assets.SND_OPEN,
						Assets.SND_UNLOCK,
						Assets.SND_ITEM,
						Assets.SND_DEWDROP,
						Assets.SND_HIT,
						Assets.SND_MISS,
						Assets.SND_STEP,
						Assets.SND_WATER,
						Assets.SND_DESCEND,
						Assets.SND_EAT,
						Assets.SND_READ,
						Assets.SND_LULLABY,
						Assets.SND_DRINK,
						Assets.SND_SHATTER,
						Assets.SND_ZAP,
						Assets.SND_LIGHTNING,
						Assets.SND_LEVELUP,
						Assets.SND_DEATH,
						Assets.SND_CHALLENGE,
						Assets.SND_CURSED,
						Assets.SND_EVOKE,
						Assets.SND_TRAP,
						Assets.SND_TOMB,
						Assets.SND_ALERT,
						Assets.SND_MELD,
						Assets.SND_BOSS,
						Assets.SND_BLAST,
						Assets.SND_PLANT,
						Assets.SND_RAY,
						Assets.SND_BEACON,
						Assets.SND_TELEPORT,
						Assets.SND_CHARMS,
						Assets.SND_MASTERY,
						Assets.SND_PUFF,
						Assets.SND_ROCKS,
						Assets.SND_BURNING,
						Assets.SND_FALLING,
						Assets.SND_GHOST,
						Assets.SND_SECRET,
						Assets.SND_BONES );

					switch (mode) {
					case DESCEND:
						descend();
						break;
					case ASCEND:
						ascend();
						break;
					case CONTINUE:
						restore();
						break;
					case RESURRECT:
						resurrect();
						break;
					case RETURN:
						returnTo();
						break;
					case FALL:
						fall();
						break;
					}
					
					if ((Dungeon.depth % 5) == 0) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}
					
				} catch (FileNotFoundException e) {
					
					error = ERR_FILE_NOT_FOUND;

				} catch (IOException e ) {

					error = ERR_GENERIC;

				}
				
				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / TIME_TO_FADE;
		
		switch (phase) {
		
		case FADE_IN:
			message.alpha( 1 - p );
			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				} else {
					phase = Phase.STATIC;
				}
			}
			break;
			
		case FADE_OUT:
			message.alpha( p );

			if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
				Music.INSTANCE.volume( p );
			}
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
			}
			break;
			
		case STATIC:
			if (error != null) {
				add( new WndError( error ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( StartScene.class );
					};
				} );
                error = null;
			}
			break;
		}
	}

	private void descend() throws IOException {

        Level level;
        ArrayList<Item> fallingItems = new ArrayList<Item>();

        Actor.fixTime();
		if (Dungeon.hero == null) {
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
		} else {
            level = Dungeon.level;

            fallingItems = level.fallingItems;
            level.fallingItems = new ArrayList<Item>();

			Dungeon.saveLevel();
		}

		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}

        for (Item item : fallingItems){
            int cell = level.randomRespawnCell();
            while (cell == -1)
                cell = level.randomRespawnCell();

            if (!(item instanceof Potion))
                level.drop(item, cell);
            else
                level.fallingPotions.add((Potion)item);
        }

		Dungeon.switchLevel( level, level.entrance );

	}
	
	private void fall() throws IOException {

        Level level = Dungeon.level;

        ArrayList<Item> fallingItems = level.fallingItems;
        level.fallingItems = new ArrayList<Item>();

        Actor.fixTime();
		Dungeon.saveLevel();

		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}

        for (Item item : fallingItems){
            int cell = level.randomRespawnCell();
            while (cell == -1)
                cell = level.randomRespawnCell();

            if (!(item instanceof Potion))
                level.drop(item, cell);
            else
                level.fallingPotions.add((Potion)item);
        }

		Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell() );
	}
	
	private void ascend() throws IOException {
		Actor.fixTime();
		
		Dungeon.saveLevel();
		Dungeon.depth--;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, level.exit );
	}
	
	private void returnTo() throws IOException {
		
		Actor.fixTime();
		
		Dungeon.saveLevel();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( returnPos ) : returnPos );
	}
	
	private void restore() throws IOException {
		
		Actor.fixTime();
		
		Dungeon.loadGame( StartScene.curClass );
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( Dungeon.loadLevel( StartScene.curClass ), -1 );
		} else {
			Level level = Dungeon.loadLevel( StartScene.curClass );
			Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
		}
	}
	
	private void resurrect() throws IOException {
		
		Actor.fixTime(); 
		
		if (Dungeon.level.locked) {
			Dungeon.hero.resurrect( Dungeon.depth );
			Dungeon.depth--;
			Level level = Dungeon.newLevel(/* true */);
			Dungeon.switchLevel( level, level.entrance );
		} else {
			Dungeon.hero.resurrect( -1 );
			Dungeon.resetLevel();
		}
	}
	
	@Override
	protected void onBackPressed() {
	}
}
