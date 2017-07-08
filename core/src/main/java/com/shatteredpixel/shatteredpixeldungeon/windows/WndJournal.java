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
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalogs;
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
import java.util.Comparator;
import java.util.HashMap;

//FIXME a lot of cleanup and improvements to do here
public class WndJournal extends WndTabbed {
	
	private static final int WIDTH_P    = 112;
	private static final int HEIGHT_P   = 160;
	
	private static final int WIDTH_L    = 160;
	private static final int HEIGHT_L   = 128;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private Notes notes;
	private CatalogTab catalogTab;
	
	public static int last_index = 0;
	
	public WndJournal(){
		
		int width = ShatteredPixelDungeon.landscape() ? WIDTH_L : WIDTH_P;
		int height = ShatteredPixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P;
		
		resize(width, height);
		
		notes = new Notes();
		add(notes);
		notes.setRect(0, 0, width, height);
		notes.updateList();
		
		catalogTab = new CatalogTab();
		add(catalogTab);
		catalogTab.setRect(0, 0, width, height);
		catalogTab.updateList();
		
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
						catalogTab.active = catalogTab.visible = value;
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
					item.setRect( 0, pos, width(), ITEM_HEIGHT );
					content.add( item );
					
					pos += item.height();
				}
				if (Dungeon.hero.belongings.ironKeys[i] > 0){
					String text = Messages.titleCase(Messages.get(IronKey.class, "name"));
					
					if (Dungeon.hero.belongings.ironKeys[i] > 1){
						text += " x" + Dungeon.hero.belongings.ironKeys[i];
					}
					
					ListItem item = new ListItem( Icons.get(Icons.DEPTH), text, i );
					item.setRect( 0, pos, width(), ITEM_HEIGHT );
					content.add( item );
					
					pos += item.height();
				}
				
			}
			
			//Journal entries
			for (Journal.Record rec : Journal.records) {
				ListItem item = new ListItem( Icons.get(Icons.DEPTH), rec.feature.desc(), rec.depth );
				item.setRect( 0, pos, width(), ITEM_HEIGHT );
				content.add( item );
				
				pos += item.height();
			}
			
			content.setSize( width(), pos );
		}
		
	}
	
	private static class CatalogTab extends Component{
		
		private RedButton[] itemButtons;
		private static final int NUM_BUTTONS = 7;
		
		private static int currentItemIdx   = 0;
		
		private static final int WEAPON_IDX = 0;
		private static final int ARMOR_IDX  = 1;
		private static final int WAND_IDX   = 2;
		private static final int RING_IDX   = 3;
		private static final int ARTIF_IDX  = 4;
		private static final int POTION_IDX = 5;
		private static final int SCROLL_IDX = 6;
		
		private ScrollPane list;
		
		private ArrayList<CatalogItem> items = new ArrayList<>();
		
		public CatalogTab(){
			
			super();
			
			itemButtons = new RedButton[NUM_BUTTONS];
			for (int i = 0; i < NUM_BUTTONS; i++){
				final int idx = i;
				itemButtons[i] = new RedButton( "" ){
					@Override
					protected void onClick() {
						currentItemIdx = idx;
						updateList();
					}
				};
				itemButtons[i].icon(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER + i, null));
				add( itemButtons[i] );
			}
			
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
		
		private static final int BUTTON_HEIGHT = 17;
		
		@Override
		protected void layout() {
			super.layout();
			
			int perRow = ShatteredPixelDungeon.landscape() ? NUM_BUTTONS : 4;
			float buttonWidth = (width() - (perRow-1))/perRow;
			
			for (int i = 0; i < NUM_BUTTONS; i++) {
				itemButtons[i].setRect((i%perRow) * (buttonWidth + 1), (i/perRow) * (BUTTON_HEIGHT + 1),
						buttonWidth, BUTTON_HEIGHT);
				PixelScene.align(itemButtons[i]);
			}
			
			list.setRect(0, itemButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - itemButtons[NUM_BUTTONS-1].bottom() - 1);
		}
		
		private void updateList() {
			
			items.clear();
			
			for (int i = 0; i < NUM_BUTTONS; i++){
				if (i == currentItemIdx){
					itemButtons[i].icon().color(TITLE_COLOR);
				} else {
					itemButtons[i].icon().resetColor();
				}
			}
			
			Component content = list.content();
			content.clear();
			list.scrollTo( 0, 0 );
			
			ArrayList<Class<? extends Item>> itemClasses;
			final HashMap<Class<?  extends Item>, Boolean> known = new HashMap<>();
			if (currentItemIdx == WEAPON_IDX) {
				itemClasses = new ArrayList<>(Catalogs.weapons());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == ARMOR_IDX){
				itemClasses = new ArrayList<>(Catalogs.armor());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == WAND_IDX){
				itemClasses = new ArrayList<>(Catalogs.wands());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == RING_IDX){
				itemClasses = new ArrayList<>(Catalogs.rings());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Ring.getKnown().contains(cls));
			} else if (currentItemIdx == ARTIF_IDX){
				itemClasses = new ArrayList<>(Catalogs.artifacts());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == POTION_IDX){
				itemClasses = new ArrayList<>(Catalogs.potions());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Potion.getKnown().contains(cls));
			} else if (currentItemIdx == SCROLL_IDX) {
				itemClasses = new ArrayList<>(Catalogs.scrolls());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Scroll.getKnown().contains(cls));
			} else {
				itemClasses = new ArrayList<>();
			}
			
			Collections.sort(itemClasses, new Comparator<Class<? extends Item>>() {
				@Override
				public int compare(Class<? extends Item> a, Class<? extends Item> b) {
					int result = 0;
					
					//specifically known items appear first, then seen items, then unknown items.
					if (known.get(a))       result -= 2;
					if (known.get(b))       result += 2;
					if (Catalogs.isSeen(a)) result --;
					if (Catalogs.isSeen(b)) result ++;
					
					return result;
				}
			});
			
			float pos = 0;
			for (Class<? extends Item> itemClass : itemClasses) {
				try{
					CatalogItem item = new CatalogItem(itemClass.newInstance(), known.get(itemClass), Catalogs.isSeen(itemClass));
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
			private boolean seen;
			
			public CatalogItem(Item item, boolean IDed, boolean seen ) {
				super( new ItemSprite(item), Messages.titleCase(item.trueName()));
				
				this.item = item;
				this.seen = seen;
				
				if (!seen) {
					icon.copy( new ItemSprite( ItemSpriteSheet.WEAPON_HOLDER + currentItemIdx, null) );
					label.text("???");
					label.hardlight( 0x999999 );
				} else if (!IDed) {
					icon.copy( new ItemSprite( ItemSpriteSheet.WEAPON_HOLDER + currentItemIdx, null) );
					label.hardlight( 0xCCCCCC );
				}
				
			}
			
			public boolean onClick( float x, float y ) {
				if (inside( x, y ) && seen) {
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
