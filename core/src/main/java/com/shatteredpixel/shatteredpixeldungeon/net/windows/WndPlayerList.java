/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.net.windows;

import com.shatteredpixel.shatteredpixeldungeon.net.events.recieve.playerlist.PlayerList;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Image;

public class WndPlayerList extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 18;

	public WndPlayerList(Image icon, PlayerList p) {
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		float pos = 0;
			IconTitle tfTitle = new IconTitle(icon, "Players");
			tfTitle.setRect(0, pos, width, 0);
			add(tfTitle);

			pos = tfTitle.bottom() + 2*MARGIN;

		layoutBody(pos, p);
	}

	private void layoutBody(float pos, PlayerList pl){
		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		for (int i=0; i < pl.list.length; i++) {
			final int index = i;
			RedButton btn = new RedButton( pl.list[i].nick ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};
			btn.enable(enabled(i));
			btn.setRect( 0, pos, width, BUTTON_HEIGHT );
			add( btn );

			pos += BUTTON_HEIGHT + MARGIN;
		}

		resize( width, (int)(pos - MARGIN) );
	}

	protected boolean enabled( int index ){
		return true;
	}
	
	protected void onSelect( int index ) {}
}
