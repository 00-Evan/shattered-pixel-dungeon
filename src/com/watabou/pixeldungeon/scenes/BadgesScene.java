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
package com.watabou.pixeldungeon.scenes;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Badges;
import com.watabou.pixeldungeon.Chrome;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.BadgesList;
import com.watabou.pixeldungeon.ui.ScrollPane;
import com.watabou.pixeldungeon.ui.Window;

public class BadgesScene extends PixelScene {
	
	private static final String TXT_TITLE = "Your Badges";
	
	@Override
	public void create() {
		
		super.create();
		
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		int pw = Math.min( 160, w - 10 );
		int ph = h - 24;
		
		NinePatch panel = Chrome.get( Chrome.Type.WINDOW );
		panel.size( pw, ph );
		panel.x = (w - pw) / 2;
		panel.y = (h - ph) / 2;
		add( panel );
		
		BitmapText title = PixelScene.createText( TXT_TITLE, 9 );
		title.hardlight( Window.TITLE_COLOR );
		title.measure();
		title.x = align( (w - title.width()) / 2 );
		title.y = align( (panel.y - title.baseLine()) / 2 );
		add( title );
		
		Badges.loadGlobal();
		
		ScrollPane list = new BadgesList( true );
		add( list );
		
		list.setRect( 
			panel.x + panel.marginLeft(), 
			panel.y + panel.marginTop(), 
			panel.innerWidth(), 
			panel.innerHeight() );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		Game.switchScene( TitleScene.class );
	}
}
