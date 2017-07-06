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
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
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
import java.util.Arrays;
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
	
	private static class ListItem extends Component {
		
		protected RenderedTextMultiline label;
		protected BitmapText depth;
		protected ColorBlock line;
		protected Image icon;
		
		public ListItem( Image icon, String text ) {
			this(icon, text, -1);
		}
		
		public ListItem( Image icon, String text, int d ) {
			super();
			
			this.icon.copy(icon);
			
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
			
			icon = new Image();
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
			label.setPos(icon.x + icon.width() + 1, y + 1 + (height() - label.height()) / 2f);
			PixelScene.align(label);
		}
	}
	
	private static class Notes extends Component {
		
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
					ListItem item = new ListItem( Icons.get(Icons.DEPTH), Messages.titleCase(text), i );
					item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
					content.add( item );
					
					pos += item.height();
				}
				if (Dungeon.hero.belongings.ironKeys[i] > 0){
					String text = Messages.titleCase(Messages.get(IronKey.class, "name"));
					
					if (Dungeon.hero.belongings.ironKeys[i] > 1){
						text += " x" + Dungeon.hero.belongings.ironKeys[i];
					}
					
					ListItem item = new ListItem( Icons.get(Icons.DEPTH), text, i );
					item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
					content.add( item );
					
					pos += item.height();
				}
				
			}
			
			//Journal entries
			for (Journal.Record rec : Journal.records) {
				ListItem item = new ListItem( Icons.get(Icons.DEPTH), rec.feature.desc(), rec.depth );
				item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
				content.add( item );
				
				pos += item.height();
			}
			
			content.setSize( WIDTH, pos );
		}
		
	}
	
	private static class Catalog extends Component{
		
		private RedButton btnPotions;
		private RedButton btnScrolls;
		private RedButton btnRings;
		
		private static int latestPressedIdx = 0;
		
		private ScrollPane list;
		
		private ArrayList<CatalogItem> items = new ArrayList<>();
		
		private static boolean showPotions = true;
		
		public Catalog(){
			
			super();
			
			btnPotions = new RedButton( "" ){
				@Override
				protected void onClick() {
					latestPressedIdx = 0;
					updateList();
				}
			};
			btnPotions.icon(new ItemSprite(ItemSpriteSheet.POTION_HOLDER, null));
			add( btnPotions );
			
			btnScrolls = new RedButton( "" ){
				@Override
				protected void onClick() {
					latestPressedIdx = 1;
					updateList();
				}
			};
			btnScrolls.icon(new ItemSprite(ItemSpriteSheet.SCROLL_HOLDER, null));
			add( btnScrolls );
			
			btnRings = new RedButton( "" ){
				@Override
				protected void onClick() {
					latestPressedIdx = 2;
					updateList();
				}
			};
			btnRings.icon(new ItemSprite(ItemSpriteSheet.RING_HOLDER, null));
			add( btnRings );
			
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
		
		private static final int BUTTON_WIDTH = 36;
		private static final int BUTTON_HEIGHT = 18;
		
		@Override
		protected void layout() {
			super.layout();
			
			btnPotions.setRect(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
			PixelScene.align( btnPotions );
			
			btnScrolls.setRect((width() - BUTTON_WIDTH)/2f, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
			PixelScene.align(btnScrolls);
			
			btnRings.setRect(width() - BUTTON_WIDTH, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
			PixelScene.align(btnRings);
			
			list.setRect( 0, btnPotions.height() + 1, width, height - btnPotions.height() - 1 );
		}
		
		private void updateList() {
			
			items.clear();
			
			Component content = list.content();
			content.clear();
			list.scrollTo( 0, 0 );
			
			ArrayList<Class<?>> itemClasses;
			if (latestPressedIdx == 0){
				itemClasses = new ArrayList<>(Arrays.asList(Generator.Category.POTION.classes));
				for ( Class unknown : Potion.getUnknown()){
					if (itemClasses.remove(unknown)) itemClasses.add(unknown);
				}
			} else if (latestPressedIdx == 1) {
				itemClasses = new ArrayList<>(Arrays.asList(Generator.Category.SCROLL.classes));
				for ( Class unknown : Scroll.getUnknown()){
					if (itemClasses.remove(unknown)) itemClasses.add(unknown);
				}
			} else {
				itemClasses = new ArrayList<>(Arrays.asList(Generator.Category.RING.classes));
				for ( Class unknown : Ring.getUnknown()){
					if (itemClasses.remove(unknown)) itemClasses.add(unknown);
				}
			}
			
			float pos = 0;
			for (Class<?> itemClass : itemClasses) {
				try{
					CatalogItem item = new CatalogItem((Item) itemClass.newInstance());
					item.setRect( 0, pos, width, ITEM_HEIGHT );
					content.add( item );
					items.add( item );
					
					pos += item.height();
				} catch (Exception e) {
					ShatteredPixelDungeon.reportException(e);
				}
			}
			
			content.setSize( width, pos );
			list.setSize( list.width(), list.height() );
		}
		
		private static class CatalogItem extends ListItem {
			
			private Item item;
			private boolean identified;
			
			public CatalogItem(Item item ) {
				super( new ItemSprite(item), Messages.titleCase(item.name()));
				
				this.item = item;
				
				if (!(identified = item.isIdentified())) {
					ItemSprite placeHolder;
					if (item instanceof Potion){
						placeHolder = new ItemSprite( ItemSpriteSheet.POTION_HOLDER, null);
					} else if (item instanceof Scroll){
						placeHolder = new ItemSprite( ItemSpriteSheet.SCROLL_HOLDER, null);
					} else if (item instanceof Ring){
						placeHolder = new ItemSprite( ItemSpriteSheet.RING_HOLDER, null);
					} else {
						placeHolder = new ItemSprite( ItemSpriteSheet.SOMETHING, null);
					}
					icon.copy( placeHolder );
					label.text( Messages.titleCase(item.trueName()) );
					label.hardlight( 0xCCCCCC );
				}
				
			}
			
			public boolean onClick( float x, float y ) {
				if (inside( x, y )) {
					if (identified)
						GameScene.show( new WndInfoItem( item ) );
					else
						GameScene.show(new WndTitledMessage( new Image(icon),
								Messages.titleCase(item.trueName()), item.desc() ));
					return true;
				} else {
					return false;
				}
			}
		}
		
	}
	
}
