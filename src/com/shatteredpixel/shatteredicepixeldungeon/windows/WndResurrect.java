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
package com.shatteredpixel.shatteredicepixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredicepixeldungeon.Rankings;
import com.shatteredpixel.shatteredicepixeldungeon.Statistics;
import com.shatteredpixel.shatteredicepixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredicepixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Window;

public class WndResurrect extends Window {
	
	private static final String TXT_MESSAGE	= "You died, but you were given another chance to win this dungeon. Will you take it?";
	private static final String TXT_YES		= "Yes, I will fight!";
	private static final String TXT_NO		= "No, I give up";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public static WndResurrect instance;
	public static Object causeOfDeath;
	
	public WndResurrect( final Ankh ankh, Object causeOfDeath ) {
		
		super();
		
		instance = this;
		WndResurrect.causeOfDeath = causeOfDeath;
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( ankh.image(), null ) );
		titlebar.label( ankh.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline message = PixelScene.createMultiline( TXT_MESSAGE, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnYes = new RedButton( TXT_YES ) {
			@Override
			protected void onClick() {
				hide();
				
				Statistics.ankhsUsed++;
				
				InterlevelScene.mode = InterlevelScene.Mode.RESURRECT;
				Game.switchScene( InterlevelScene.class );
			}
		};
		btnYes.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnYes );
		
		RedButton btnNo = new RedButton( TXT_NO ) {
			@Override
			protected void onClick() {
				hide();
				
				Rankings.INSTANCE.submit( false );
				Hero.reallyDie( WndResurrect.causeOfDeath );
			}
		};
		btnNo.setRect( 0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );
		
		resize( WIDTH, (int)btnNo.bottom() );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
	
	@Override
	public void onBackPressed() {
	}
}
