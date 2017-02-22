/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndCatalogs extends WndTabbed {

	private static final int WIDTH    = 112;
	private static final int HEIGHT    = 141;

	private static final int ITEM_HEIGHT	= 17;

	private RedButton btnJournal;
	private RedButton btnTitle;
	private ScrollPane list;
	
	private ArrayList<ListItem> items = new ArrayList<>();
	
	private static boolean showPotions = true;
	
	public WndCatalogs() {
		
		super();

		resize( WIDTH, HEIGHT );

		btnJournal = new RedButton( Messages.get(WndJournal.class, "title"), 9 ){
			@Override
			protected void onClick() {
				hide();
				GameScene.show(new WndJournal());
			}
		};
		btnJournal.setRect(0, 0, WIDTH/2f - 1, btnJournal.reqHeight());
		PixelScene.align( btnJournal );
		add( btnJournal );

		//does nothing, we're already in the catalog
		btnTitle = new RedButton( Messages.get(this, "title"), 9 );
		btnTitle.textColor( Window.TITLE_COLOR );
		btnTitle.setRect(WIDTH/2f+1, 0, WIDTH/2f - 1, btnTitle.reqHeight());
		PixelScene.align(btnTitle);
		add( btnTitle );
		
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
		list.setRect( 0, btnTitle.height() + 1, width, height - btnTitle.height() - 1 );

		boolean showPotions = WndCatalogs.showPotions;
		Tab[] tabs = {
			new LabeledTab( Messages.get(this, "potions") ) {
				protected void select( boolean value ) {
					super.select( value );
					WndCatalogs.showPotions = value;
					updateList();
				};
			},
			new LabeledTab( Messages.get(this, "scrolls") ) {
				protected void select( boolean value ) {
					super.select( value );
					WndCatalogs.showPotions = !value;
					updateList();
				};
			}
		};
		for (Tab tab : tabs) {
			add( tab );
		}

		layoutTabs();
		
		select( showPotions ? 0 : 1 );
	}
	
	private void updateList() {

		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		float pos = 0;
		for (Class<? extends Item> itemClass : showPotions ? Potion.getKnown() : Scroll.getKnown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, width, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			
			pos += item.height();
		}
		
		for (Class<? extends Item> itemClass : showPotions ? Potion.getUnknown() : Scroll.getUnknown()) {
			ListItem item = new ListItem( itemClass );
			item.setRect( 0, pos, width, ITEM_HEIGHT );
			content.add( item );
			items.add( item );
			
			pos += item.height();
		}

		content.setSize( width, pos );
		list.setSize( list.width(), list.height() );
	}
	
	private static class ListItem extends Component {
		
		private Item item;
		private boolean identified;
		
		private ItemSprite sprite;
		private RenderedTextMultiline label;
		private ColorBlock line;
		
		public ListItem( Class<? extends Item> cl ) {
			super();
			
			try {
				item = cl.newInstance();
				if (identified = item.isIdentified()) {
					sprite.view( item.image(), null );
					label.text( Messages.titleCase(item.name()) );
				} else {
					sprite.view( ItemSpriteSheet.SOMETHING, null );
					label.text( Messages.titleCase(item.trueName()) );
					label.hardlight( 0xCCCCCC );
				}
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
		
		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add( sprite );

			label = PixelScene.renderMultiline( 7 );
			add( label );

			line = new ColorBlock( 1, 1, 0xFF222222);
			add(line);
		}
		
		@Override
		protected void layout() {
			sprite.y = y + 1 + (height - 1 - sprite.height) / 2f;
			PixelScene.align(sprite);

			line.size(width, 1);
			line.x = 0;
			line.y = y;

			label.maxWidth((int)(width - sprite.width - 1));
			label.setPos(sprite.x + sprite.width + 1,  y + 1 + (height - 1 - label.height()) / 2f);
			PixelScene.align(label);
		}
		
		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				if (identified)
					GameScene.show( new WndInfoItem( item ) );
				else
					GameScene.show( new WndTitledMessage( new ItemSprite(ItemSpriteSheet.SOMETHING, null),
							Messages.titleCase(item.trueName()), item.desc() ));
				return true;
			} else {
				return false;
			}
		}
	}
}
