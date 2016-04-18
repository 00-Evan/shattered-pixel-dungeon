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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WndItem extends Window {

	private static final float BUTTON_HEIGHT	= 16;
	
	private static final float GAP	= 2;
	
	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	public WndItem( final WndBag owner, final Item item ){
		this( owner, item, owner != null );
	}
	
	public WndItem( final WndBag owner, final Item item , final boolean options ) {
		
		super();

		int width = ShatteredPixelDungeon.landscape() ? WIDTH_L : WIDTH_P;
		
		IconTitle titlebar = new IconTitle( item );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );

		if (item.levelKnown && item.level() > 0) {
			titlebar.color( ItemSlot.UPGRADED );
		} else if (item.levelKnown && item.level() < 0) {
			titlebar.color( ItemSlot.DEGRADED );
		}
		
		RenderedTextMultiline info = PixelScene.renderMultiline( item.info(), 6 );
		info.maxWidth(width);
		info.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add( info );
	
		float y = info.top() + info.height() + GAP;
		float x = 0;
		
		if (Dungeon.hero.isAlive() && options) {
			ArrayList<RedButton> line = new ArrayList<>();
			for (final String action:item.actions( Dungeon.hero )) {
				
				RedButton btn = new RedButton( Messages.get(item, "ac_" + action), 8 ) {
					@Override
					protected void onClick() {
						hide();
						if (owner != null) owner.hide();
						item.execute( Dungeon.hero, action );
					};
				};
				btn.setSize( btn.reqWidth(), BUTTON_HEIGHT );
				if (x + btn.width() > width || line.size() == 3) {
					layoutButtons(line, width - x, y);
					x = 0;
					y += BUTTON_HEIGHT + 1;
					line = new ArrayList<>();
				}
				x++;
				add( btn );
				line.add( btn );

				if (action.equals(item.defaultAction)) {
					btn.textColor( TITLE_COLOR );
				}

				x += btn.width();
			}
			layoutButtons(line, width - x, y);
		}
		
		resize( width, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
	}

	//this method assumes a max of 3 buttons per line
	//FIXME: this is really messy for just trying to make buttons fill the window. Gotta be a cleaner way.
	private static void layoutButtons(ArrayList<RedButton> line, float extraWidth, float y){
		if (line == null || line.size() == 0 || extraWidth == 0) return;
		if (line.size() == 1){
			line.get(0).setSize(line.get(0).width()+extraWidth, BUTTON_HEIGHT);
			line.get(0).setPos( 0 , y );
			return;
		}
		ArrayList<RedButton> lineByWidths = new ArrayList<>(line);
		Collections.sort(lineByWidths, widthComparator);
		RedButton smallest, middle, largest;
		smallest = lineByWidths.get(0);
		middle = lineByWidths.get(1);
		largest = null;
		if (lineByWidths.size() == 3) {
			largest = lineByWidths.get(2);
		}

		float btnDiff = middle.width() - smallest.width();
		smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth), BUTTON_HEIGHT);
		extraWidth -= btnDiff;
		if (extraWidth > 0) {
			if (largest == null) {
				smallest.setSize(smallest.width() + extraWidth / 2, BUTTON_HEIGHT);
				middle.setSize(middle.width() + extraWidth / 2, BUTTON_HEIGHT);
			} else {
				btnDiff = largest.width() - smallest.width();
				smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth/2), BUTTON_HEIGHT);
				middle.setSize(middle.width() + Math.min(btnDiff, extraWidth/2), BUTTON_HEIGHT);
				extraWidth -= btnDiff*2;
				if (extraWidth > 0){
					smallest.setSize(smallest.width() + extraWidth / 3, BUTTON_HEIGHT);
					middle.setSize(middle.width() + extraWidth / 3, BUTTON_HEIGHT);
					largest.setSize(largest.width() + extraWidth / 3, BUTTON_HEIGHT);
				}
			}
		}

		float x = 0;
		for (RedButton btn : line){
			btn.setPos( x , y );
			x += btn.width()+1;
		}
	}

	private static Comparator<RedButton> widthComparator = new Comparator<RedButton>() {
		@Override
		public int compare(RedButton lhs, RedButton rhs) {
			if (lhs.width() < rhs.width()){
				return -1;
			} else if (lhs.width() == rhs.width()){
				return 0;
			} else {
				return 1;
			}
		}
	};
}
