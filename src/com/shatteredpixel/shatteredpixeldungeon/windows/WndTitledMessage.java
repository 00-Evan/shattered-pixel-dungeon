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
package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndTitledMessage extends Window {

	private static final int WIDTH_P    = 120;
	private static final int WIDTH_L    = 144;
	private static final int GAP	= 2;
	
	private BitmapTextMultiline normal;
	private BitmapTextMultiline highlighted;
	
	public WndTitledMessage( Image icon, String title, String message ) {
		
		this( new IconTitle( icon, title ), message );

	}
	
	public WndTitledMessage( Component titlebar, String message ) {
		
		super();

		int width = ShatteredPixelDungeon.landscape() ? WIDTH_L : WIDTH_P;

		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		Highlighter hl = new Highlighter( message );
		
		normal = PixelScene.createMultiline( hl.text, 6 );
		normal.maxWidth = width;
		normal.measure();
		normal.x = titlebar.left();
		normal.y = titlebar.bottom() + GAP;
		add( normal );
		
		if (hl.isHighlighted()) {
			normal.mask = hl.inverted();
			
			highlighted = PixelScene.createMultiline( hl.text, 6 );
			highlighted.maxWidth = normal.maxWidth;
			highlighted.measure();
			highlighted.x = normal.x;
			highlighted.y = normal.y;
			add( highlighted );
	
			highlighted.mask = hl.mask;
			highlighted.hardlight( TITLE_COLOR );
		}
		
		resize( width, (int)(normal.y + normal.height()) );
	}
}
