/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.StartScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.Locale;

public class WndGameInProgress extends Window {
	
	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 120;
	
	private int GAP	  = 6;
	
	private float pos;
	
	public WndGameInProgress(final int slot){
		
		final GamesInProgress.Info info = GamesInProgress.check(slot);
		
		String className = null;
		if (info.subClass != HeroSubClass.NONE){
			className = info.subClass.title();
		} else {
			className = info.heroClass.title();
		}
		
		IconTitle title = new IconTitle();
		title.icon( HeroSprite.avatar(info.heroClass, info.armorTier) );
		title.label((Messages.get(this, "title", info.level, className)).toUpperCase(Locale.ENGLISH));
		title.color(Window.SHPX_COLOR);
		title.setRect( 0, 0, WIDTH, 0 );
		add(title);
		
		//manually produces debug information about a run, mainly useful for levelgen errors
		Button debug = new Button(){
			@Override
			protected boolean onLongClick() {
				try {
					Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.gameFile(slot));
					ShatteredPixelDungeon.scene().addToFront(new WndMessage("_Debug Info:_\n\n" +
							"Version: " + Game.version + " (" + Game.versionCode + ")\n" +
							"Seed: " + bundle.getLong("seed") + "\n" +
							"Challenge Mask: " + info.challenges));
				} catch (IOException ignored) { }
				return true;
			}
		};
		debug.setRect(0, 0, title.imIcon.width(), title.imIcon.height);
		add(debug);
		
		if (info.challenges > 0) GAP -= 2;
		
		pos = title.bottom() + GAP;
		
		if (info.challenges > 0) {
			RedButton btnChallenges = new RedButton( Messages.get(this, "challenges") ) {
				@Override
				protected void onClick() {
					Game.scene().add( new WndChallenges( info.challenges, false ) );
				}
			};
			float btnW = btnChallenges.reqWidth() + 2;
			btnChallenges.setRect( (WIDTH - btnW)/2, pos, btnW , btnChallenges.reqHeight() + 2 );
			add( btnChallenges );
			
			pos = btnChallenges.bottom() + GAP;
		}
		
		pos += GAP;
		
		statSlot( Messages.get(this, "str"), info.str );
		if (info.shld > 0) statSlot( Messages.get(this, "health"), info.hp + "+" + info.shld + "/" + info.ht );
		else statSlot( Messages.get(this, "health"), (info.hp) + "/" + info.ht );
		statSlot( Messages.get(this, "exp"), info.exp + "/" + Hero.maxExp(info.level) );
		
		pos += GAP;
		statSlot( Messages.get(this, "gold"), info.goldCollected );
		statSlot( Messages.get(this, "depth"), info.maxDepth );
		
		pos += GAP;
		
		RedButton cont = new RedButton(Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				
				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				ShatteredPixelDungeon.switchScene(InterlevelScene.class);
			}
		};
		
		RedButton erase = new RedButton( Messages.get(this, "erase")){
			@Override
			protected void onClick() {
				super.onClick();
				
				ShatteredPixelDungeon.scene().add(new WndOptions(
						Messages.get(WndGameInProgress.class, "erase_warn_title"),
						Messages.get(WndGameInProgress.class, "erase_warn_body"),
						Messages.get(WndGameInProgress.class, "erase_warn_yes"),
						Messages.get(WndGameInProgress.class, "erase_warn_no") ) {
					@Override
					protected void onSelect( int index ) {
						if (index == 0) {
							FileUtils.deleteDir(GamesInProgress.gameFolder(slot));
							GamesInProgress.setUnknown(slot);
							ShatteredPixelDungeon.switchNoFade(StartScene.class);
						}
					}
				} );
			}
		};
		
		cont.setRect(0, HEIGHT - 20, WIDTH/2 -1, 20);
		add(cont);
		
		erase.setRect(WIDTH/2 + 1, HEIGHT-20, WIDTH/2 - 1, 20);
		add(erase);
		
		resize(WIDTH, HEIGHT);
	}
	
	private void statSlot( String label, String value ) {
		
		RenderedTextBlock txt = PixelScene.renderTextBlock( label, 8 );
		txt.setPos(0, pos);
		add( txt );
		
		txt = PixelScene.renderTextBlock( value, 8 );
		txt.setPos(WIDTH * 0.6f, pos);
		PixelScene.align(txt);
		add( txt );
		
		pos += GAP + txt.height();
	}
	
	private void statSlot( String label, int value ) {
		statSlot( label, Integer.toString( value ) );
	}
}
