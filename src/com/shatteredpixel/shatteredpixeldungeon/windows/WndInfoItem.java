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
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;
import com.watabou.noosa.BitmapTextMultiline;

public class WndInfoItem extends Window {

	private static final float GAP	= 2;
	
	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			
			Item item = heap.peek();
			
			int color = TITLE_COLOR;
			if (item.levelKnown && item.level() > 0) {
				color = ItemSlot.UPGRADED;
			} else if (item.levelKnown && item.level() < 0) {
				color = ItemSlot.DEGRADED;
			}
			fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
			
		} else {
			
			String title;
			String info;
			
			if (heap.type == Type.CHEST || heap.type == Type.MIMIC) {
				title = Messages.get(this, "chest");
				info = Messages.get(this, "wont_know");
			} else if (heap.type == Type.TOMB) {
				title = Messages.get(this, "tomb");
				info = Messages.get(this, "owner");
			} else if (heap.type == Type.SKELETON) {
				title = Messages.get(this, "skeleton");
				info = Messages.get(this, "skeleton_desc");
			} else if (heap.type == Type.REMAINS) {
				title = Messages.get(this, "remains");
				info = Messages.get(this, "remains_desc");
			} else if (heap.type == Type.CRYSTAL_CHEST) {
				title = Messages.get(this, "crystal_chest");
				if (heap.peek() instanceof Artifact)
					info = Messages.get(this, "inside", Messages.get(this, "artifact") );
				else if (heap.peek() instanceof Wand)
					info = Messages.get(this, "inside", Messages.get(this, "wand") );
				else if (heap.peek() instanceof Ring)
					info = Messages.get(this, "inside", Messages.get(this, "ring") );
				else
					info = Messages.get(this, "inside", Utils.indefinite( heap.peek().name() ) );
			} else {
				title = Messages.get(this, "locked_chest");
				info = Messages.get(this, "need_key");
			}
			
			fillFields( heap.image(), heap.glowing(), TITLE_COLOR, title, info );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		
		super();
		
		int color = TITLE_COLOR;
		if (item.levelKnown && item.level() > 0) {
			color = ItemSlot.UPGRADED;
		} else if (item.levelKnown && item.level() < 0) {
			color = ItemSlot.DEGRADED;
		}
		
		fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
	}
	
	private void fillFields( int image, ItemSprite.Glowing glowing, int titleColor, String title, String info ) {

		int width = ShatteredPixelDungeon.landscape() ? WIDTH_L : WIDTH_P;

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( image, glowing ) );
		titlebar.label( Utils.capitalize( title ), titleColor );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		BitmapTextMultiline txtInfo = PixelScene.createMultiline( info, 6 );
		txtInfo.maxWidth = width;
		txtInfo.measure();
		txtInfo.x = titlebar.left();
		txtInfo.y = titlebar.bottom() + GAP;
		add( txtInfo );
		
		resize( width, (int)(txtInfo.y + txtInfo.height()) );
	}
}
