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

import java.util.ArrayList;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

public class WndCatalogus extends WndTabbed {
	
	private static final int WIDTH	= 112;
	private static final int HEIGHT	= 160;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private static final int TAB_WIDTH		= 50;
	
	private static final String TXT_POTIONS	= "Potions";
	private static final String TXT_SCROLLS	= "Scrolls";
	private static final String TXT_TITLE	= "Catalogus";
	
	private BitmapText txtTitle;
	private ScrollPane list;
	
	private ArrayList<ListItem> items = new ArrayList<WndCatalogus.ListItem>();
	
	private static boolean showPotions = true;
	
	public WndCatalogus() {
		
		super();
		resize( WIDTH, HEIGHT );
		
		txtTitle = PixelScene.createText( TXT_TITLE, 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.measure();
		add( txtTitle );
		
		list = new ScrollPane( new Component() ) {
			@Override
			public void onClick( float x, float y ) {
				int size = items.size();
				for (int i=0; i < size; i++) {
					if (items.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add( list );
		list.setRect( 0, txtTitle.height(), WIDTH, HEIGHT - txtTitle.height() );
		
		boolean showPotions = WndCatalogus.showPotions;
		Tab[] tabs = {
			new LabeledTab( TXT_POTIONS ) {
				protected void select( boolean value ) {
					super.select( value );
					WndCatalogus.showPotions = value;
					updateList();
				};
			},
			new LabeledTab( TXT_SCROLLS ) {
				protected void select( boolean value ) {
					super.select( value );
					WndCatalogus.showPotions = !value;
					updateList();
				};
			}
		};
		for (Tab tab : tabs) {
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
		}
		
		select( showPotions ? 0 : 1 );
	}
	
	private void updateList() {
		
		txtTitle.text( Utils.format( TXT_TITLE, showPotions ? TXT_POTIONS : TXT_SCROLLS ) );
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2 );
		
		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		float pos = 0;
		for (Class<? extends Item> itemClass : showPotions ? Potion.getKnown() : Scroll.getKnown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			
			pos += item.height();
		}
		
		for (Class<? extends Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			
			pos += item.height();
		}
		
		content.setSize( WIDTH, pos );
	}
	
	private static class ListItem extends Component {
		
		private Item item;
		private boolean identified;
		
		private ItemSprite sprite;
		private BitmapText label;
		
		public ListItem( Class<? extends Item> cl ) {
			super();
			
			try {
				item = cl.newInstance();
				if (identified = item.isIdentified()) {
					sprite.view( item.image(), null );
					label.text( item.name() );
				} else {
					sprite.view( 127, null );
					label.text( item.trueName() );
					label.hardlight( 0xCCCCCC );
				}
			} catch (Exception e) {
				//
			}
		}
		
		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add( sprite );
			
			label = PixelScene.createText( 8 );
			add( label );
		}
		
		@Override
		protected void layout() {
			sprite.y = PixelScene.align( y + (height - sprite.height) / 2 );
			
			label.x = sprite.x + sprite.width;
			label.y = PixelScene.align( y + (height - label.baseLine()) / 2 );
		}
		
		public boolean onClick( float x, float y ) {
			if (identified && inside( x, y )) {
				GameScene.show( new WndInfoItem( item ) );
				return true;
			} else {
				return false;
			}
		}
	}
}
