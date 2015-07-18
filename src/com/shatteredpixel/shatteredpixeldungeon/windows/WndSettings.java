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
package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndDisplay;
import com.watabou.noosa.Game;

public class WndSettings extends Window {
	private static final String TXT_SWITCH_PORT	= "Switch to portrait";
	private static final String TXT_SWITCH_LAND	= "Switch to landscape";
	
	private static final int WIDTH		= 112;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP 		= 2;

	public WndSettings() {
		super();

		RedButton btnDisplay = new RedButton("Display"){
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndDisplay());
			}
		};
		btnDisplay.setRect(0, 0, (WIDTH/2)-1, BTN_HEIGHT);
		add( btnDisplay );

		RedButton btnAudio = new RedButton("Audio") {
			@Override
			protected void onClick() {
				hide();
				Game.scene().add(new WndAudio());
			}
		};
		btnAudio.setRect( btnDisplay.right()+2, 0, (WIDTH/2)-1, BTN_HEIGHT );
		add( btnAudio );

		RedButton btnOrientation = new RedButton( orientationText() ) {
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.landscape(!ShatteredPixelDungeon.landscape());
			}
		};
		btnOrientation.setRect( 0, btnAudio.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnOrientation );

		resize( WIDTH, (int)btnOrientation.bottom() );

	}
	
	private String orientationText() {
		return ShatteredPixelDungeon.landscape() ? TXT_SWITCH_PORT : TXT_SWITCH_LAND;
	}
}
