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
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;

public class WndMessage extends Window {
	
	private static final int WIDTH = 120;
	private static final int MARGIN = 4;
	
	public WndMessage( String text ) {
		
		super();
		
		BitmapTextMultiline info = PixelScene.createMultiline( text, 6 );
		info.maxWidth = WIDTH - MARGIN * 2;
		info.measure();
		info.x = info.y = MARGIN;
		add( info );

		resize( 
			(int)info.width() + MARGIN * 2, 
			(int)info.height() + MARGIN * 2 );
	}
}
