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

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndInfoItem extends Window {
	
	private static final float GAP	= 2;
	
	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			fillFields( heap.peek() );
			
		} else {
			fillFields( heap );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		super();
		
		fillFields( item );
	}
	
	private void fillFields( Heap heap ) {
		
		int width = SPDSettings.landscape() ? WIDTH_L : WIDTH_P;
		
		IconTitle titlebar = new IconTitle( heap );
		titlebar.color( TITLE_COLOR );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		RenderedTextBlock txtInfo = PixelScene.renderTextBlock( heap.info(), 6 );
		txtInfo.maxWidth(width);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add( txtInfo );
		
		resize( width, (int)(txtInfo.top() + txtInfo.height()) );
	}
	
	private void fillFields( Item item ) {
		
		int color = TITLE_COLOR;
		if (item.levelKnown && item.level() > 0) {
			color = ItemSlot.UPGRADED;
		} else if (item.levelKnown && item.level() < 0) {
			color = ItemSlot.DEGRADED;
		}
		
		int width = SPDSettings.landscape() ? WIDTH_L : WIDTH_P;

		IconTitle titlebar = new IconTitle( item );
		titlebar.color( color );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		RenderedTextBlock txtInfo = PixelScene.renderTextBlock( item.info(), 6 );
		txtInfo.maxWidth(width);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add( txtInfo );
		
		resize( width, (int)(txtInfo.top() + txtInfo.height()) );
	}
}
