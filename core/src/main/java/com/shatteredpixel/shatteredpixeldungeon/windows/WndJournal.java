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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Journal;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Collections;

//FIXME a lot of cleanup and improvements to do here
public class WndJournal extends WndTabbed {
	
	private static final int WIDTH    = 112;
	private static final int HEIGHT   = 130;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private Notes notes;
	private Catalog catalog;
	
	private static int last_index = 0;
	
	public WndJournal(){
		
		resize(WIDTH, HEIGHT);
		
		notes = new Notes();
		add(notes);
		notes.setRect(0, 0, WIDTH, HEIGHT);
		notes.updateList();
		
		catalog = new Catalog();
		add(catalog);
		catalog.setRect(0, 0, WIDTH, HEIGHT);
		catalog.updateList();
		
		Tab[] tabs = {
				new LabeledTab( "Guide" ) {
					protected void select( boolean value ) {
						super.select( value );
						if (value) last_index = 0;
					};
				},
				new LabeledTab( "Notes" ) {
					protected void select( boolean value ) {
						super.select( value );
						notes.active = notes.visible = value;
						if (value) last_index = 1;
					};
				},
				new LabeledTab( "Items" ) {
					protected void select( boolean value ) {
						super.select( value );
						catalog.active = catalog.visible = value;
						if (value) last_index = 2;
					};
				}
		};
		
		for (Tab tab : tabs) {
			add( tab );
		}
		
		layoutTabs();
		
		select(last_index);
	}
	
	private static class Notes extends Component{
		
		private ScrollPane list;
		
		public Notes(){
			list = new ScrollPane( new Component() );
			add( list );
		}
		
		@Override
		protected void layout() {
			super.layout();
			list.setRect( 0, 0, width, height);
		}
		
		private void updateList(){
			Component content = list.content();
			
			Collections.sort( Journal.records );
			
			float pos = 0;
			
			//Keys
			for (int i = Dungeon.hero.belongings.ironKeys.length-1; i > 0; i--){
				if (Dungeon.hero.belongings.specialKeys[i] > 0){
					String text;
					if (i % 5 == 0)
						text = Messages.capitalize(Messages.get(SkeletonKey.class, "name"));
					else
						text = Messages.capitalize(Messages.get(GoldenKey.class, "name"));
					
					if (Dungeon.hero.belongings.specialKeys[i] > 1){
						text += " x" + Dungeon.hero.belongings.specialKeys[i];
					}
					ListItem item = new ListItem( Messages.titleCase(text), i );
					item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
					content.add( item );
					
					pos += item.height();
				}
				if (Dungeon.hero.belongings.ironKeys[i] > 0){
					String text = Messages.titleCase(Messages.get(IronKey.class, "name"));
					
					if (Dungeon.hero.belongings.ironKeys[i] > 1){
						text += " x" + Dungeon.hero.belongings.ironKeys[i];
					}
					
					ListItem item = new ListItem( text, i );
					item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
					content.add( item );
					
					pos += item.height();
				}
				
			}
			
			//Journal entries
			for (Journal.Record rec : Journal.records) {
				ListItem item = new ListItem( rec.feature.desc(), rec.depth );
				item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
				content.add( item );
				
				pos += item.height();
			}
			
			content.setSize( WIDTH, pos );
		}
		
		private static class ListItem extends Component {
			
			private RenderedTextMultiline label;
			private BitmapText depth;
			private ColorBlock line;
			private Image icon;
			
			public ListItem( String text ) {
				this(text, -1);
			}
			
			public ListItem( String text, int d ) {
				super();
				
				label.text( text );
				
				if (d >= 0) {
					depth.text(Integer.toString(d));
					depth.measure();
					
					if (d == Dungeon.depth) {
						label.hardlight(TITLE_COLOR);
						depth.hardlight(TITLE_COLOR);
					}
				}
			}
			
			@Override
			protected void createChildren() {
				label = PixelScene.renderMultiline( 7 );
				add( label );
				
				icon = Icons.get( Icons.DEPTH );
				add( icon );
				
				depth = new BitmapText( PixelScene.pixelFont);
				add( depth );
				
				line = new ColorBlock( 1, 1, 0xFF222222);
				add(line);
				
			}
			
			@Override
			protected void layout() {
				
				icon.y = y + 1 + (height() - 1 - icon.height()) / 2f;
				PixelScene.align(icon);
				
				depth.x = icon.x + (icon.width - depth.width()) / 2f;
				depth.y = icon.y + (icon.height - depth.height()) / 2f + 1;
				PixelScene.align(depth);
				
				line.size(width, 1);
				line.x = 0;
				line.y = y;
				
				label.maxWidth((int)(width - icon.width() - 8 - 1));
				label.setPos(icon.x + icon.width() + 1, y + 1 + (height() - 1 - label.height()) / 2f);
				PixelScene.align(label);
			}
		}
		
	}
	
	private static class Catalog extends Component{
		
		private RedButton btnPotions;
		private RedButton btnScrolls;
		private ScrollPane list;
		
		private ArrayList<ListItem> items = new ArrayList<>();
		
		private static boolean showPotions = true;
		
		public Catalog(){
			
			super();
			
			btnPotions = new RedButton( "" ){
				@Override
				protected void onClick() {
					if (!showPotions){
						showPotions = true;
						updateList();
					}
				}
			};
			btnPotions.icon(new ItemSprite(ItemSpriteSheet.POTION_HOLDER, null));
			add( btnPotions );
			
			btnScrolls = new RedButton( "" ){
				@Override
				protected void onClick() {
					if (showPotions){
						showPotions = false;
						updateList();
					}
				}
			};
			btnScrolls.icon(new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER, null));
			add( btnScrolls );
			
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
		}
		
		private static final int BUTTON_WIDTH = 55;
		private static final int BUTTON_HEIGHT = 18;
		
		@Override
		protected void layout() {
			super.layout();
			
			btnPotions.setRect(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
			PixelScene.align( btnPotions );
			
			btnScrolls.setRect(width() - BUTTON_WIDTH, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
			PixelScene.align(btnScrolls);
			
			list.setRect( 0, btnPotions.height() + 1, width, height - btnPotions.height() - 1 );
		}
		
		private void updateList() {
			
			if (showPotions){
				btnPotions.textColor(TITLE_COLOR);
				btnScrolls.textColor(0xFFFFFF);
			} else {
				btnPotions.textColor(0xFFFFFF);
				btnScrolls.textColor(TITLE_COLOR);
			}
			
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
						sprite.view( showPotions ? ItemSpriteSheet.POTION_HOLDER : ItemSpriteSheet.SCROLL_HOLDER, null );
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
						GameScene.show( new WndTitledMessage( new ItemSprite(showPotions ? ItemSpriteSheet.POTION_HOLDER : ItemSpriteSheet.SCROLL_HOLDER, null),
								Messages.titleCase(item.trueName()), item.desc() ));
					return true;
				} else {
					return false;
				}
			}
		}
		
	}
	
}
