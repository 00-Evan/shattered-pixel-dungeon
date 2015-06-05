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

import com.shatteredpixel.shatteredicepixeldungeon.items.artifacts.Artifact;
import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap;
import com.shatteredpixel.shatteredicepixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredicepixeldungeon.items.Item;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.ui.ItemSlot;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Window;
import com.shatteredpixel.shatteredicepixeldungeon.utils.Utils;

public class WndInfoItem extends Window {
	
	private static final String TTL_CHEST           = "Chest";
	private static final String TTL_LOCKED_CHEST	= "Locked chest";
	private static final String TTL_CRYSTAL_CHEST	= "Crystal chest";
	private static final String TTL_TOMB			= "Tomb";
	private static final String TTL_SKELETON		= "Skeletal remains";
    private static final String TTL_REMAINS 		= "Heroes remains";
	private static final String TXT_WONT_KNOW		= "You won't know what's inside until you open it!";
	private static final String TXT_NEED_KEY		= TXT_WONT_KNOW + " But to open it you need a golden key.";
	private static final String TXT_INSIDE			= "You can see %s inside, but to open the chest you need a golden key.";
	private static final String TXT_OWNER	= 
		"This ancient tomb may contain something useful, " +
		"but its owner will most certainly object to checking.";
    private static final String TXT_SKELETON =
        "This is all that's left of some unfortunate adventurer. " +
        "Maybe it's worth checking for any valuables.";
	private static final String TXT_REMAINS =
		"This is all that's left from one of your predecessors. " +
		"Maybe it's worth checking for any valuables.";
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			
			Item item = heap.peek();
			
			int color = TITLE_COLOR;
			if (item.levelKnown && item.level > 0) {
				color = ItemSlot.UPGRADED;				
			} else if (item.levelKnown && item.level < 0) {
				color = ItemSlot.DEGRADED;				
			}
			fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
			
		} else {
			
			String title;
			String info;
			
			if (heap.type == Type.CHEST || heap.type == Type.MIMIC) {
				title = TTL_CHEST;
				info = TXT_WONT_KNOW;
			} else if (heap.type == Type.TOMB) {
				title = TTL_TOMB;
				info = TXT_OWNER;
			} else if (heap.type == Type.SKELETON) {
                title = TTL_SKELETON;
                info = TXT_SKELETON;
            } else if (heap.type == Type.REMAINS) {
                title = TTL_REMAINS;
                info = TXT_REMAINS;
			} else if (heap.type == Type.CRYSTAL_CHEST) {
				title = TTL_CRYSTAL_CHEST;
                if (heap.peek() instanceof Artifact)
                    info = Utils.format( TXT_INSIDE, "an artifact" );
                else
                    info = Utils.format( TXT_INSIDE, Utils.indefinite( heap.peek().name() ) );
			} else {
				title = TTL_LOCKED_CHEST;
				info = TXT_NEED_KEY;
			}
			
			fillFields( heap.image(), heap.glowing(), TITLE_COLOR, title, info );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		
		super();
		
		int color = TITLE_COLOR;
		if (item.levelKnown && item.level > 0) {
			color = ItemSlot.UPGRADED;				
		} else if (item.levelKnown && item.level < 0) {
			color = ItemSlot.DEGRADED;				
		}
		
		fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
	}
	
	private void fillFields( int image, ItemSprite.Glowing glowing, int titleColor, String title, String info ) {
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( image, glowing ) );
		titlebar.label( Utils.capitalize( title ), titleColor );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline txtInfo = PixelScene.createMultiline( info, 6 );
		txtInfo.maxWidth = WIDTH;
		txtInfo.measure();
		txtInfo.x = titlebar.left();
		txtInfo.y = titlebar.bottom() + GAP;
		add( txtInfo );
		
		resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
	}
}
