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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.RenderedText;
import com.watabou.utils.FileUtils;

import java.util.Locale;

public class WndGameInProgress extends Window {
	
	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 120;
	
	private static final int GAP	  = 5;
	
	private float pos;
	
	public WndGameInProgress(final int slot){
		
		GamesInProgress.Info info = GamesInProgress.check(slot);
		
		IconTitle title = new IconTitle();
		title.icon( HeroSprite.avatar(info.heroClass, info.armorTier) );
		title.label((Messages.get(this, "title", info.level, info.heroClass.title())).toUpperCase(Locale.ENGLISH));
		title.color(Window.SHPX_COLOR);
		title.setRect( 0, 0, WIDTH, 0 );
		add(title);
		
		pos = title.bottom() + 2*GAP;
		
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
							ShatteredPixelDungeon.switchNoFade((Class<? extends PixelScene>) ShatteredPixelDungeon.scene().getClass());
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
		
		RenderedText txt = PixelScene.renderText( label, 8 );
		txt.y = pos;
		add( txt );
		
		txt = PixelScene.renderText( value, 8 );
		txt.x = WIDTH * 0.6f;
		txt.y = pos;
		PixelScene.align(txt);
		add( txt );
		
		pos += GAP + txt.baseLine();
	}
	
	private void statSlot( String label, int value ) {
		statSlot( label, Integer.toString( value ) );
	}
}
