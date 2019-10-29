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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

//FIXME lots of copy-pasta from WndJournal here. should generalize some of this. Primarily ListItem
public class WndDocument extends Window {
	
	private static final int WIDTH_P    = 120;
	private static final int HEIGHT_P   = 160;
	
	private static final int WIDTH_L    = 160;
	private static final int HEIGHT_L   = 128;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private ScrollPane list;
	private ArrayList<docPage> pages = new ArrayList<>();
	
	public WndDocument( Document doc ){
		
		int w = PixelScene.landscape() ? WIDTH_L : WIDTH_P;
		int h = PixelScene.landscape() ? HEIGHT_L : HEIGHT_P;
		
		resize(w, h);
		
		list = new ScrollPane( new Component() ){
			@Override
			public void onClick( float x, float y ) {
				int size = pages.size();
				for (int i=0; i < size; i++) {
					if (pages.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add( list );
		
		list.setRect( 0, 0, w, h);
		
		Component content = list.content();
		
		float pos = 0;
		
		ColorBlock line = new ColorBlock( w, 1, 0xFF222222);
		line.y = pos;
		content.add(line);
		
		RenderedTextBlock title = PixelScene.renderTextBlock(doc.title(), 9);
		title.hardlight(TITLE_COLOR);
		title.maxWidth( w - 2 );
		title.setPos( (w - title.width())/2f, pos + 1 + ((ITEM_HEIGHT) - title.height())/2f);
		PixelScene.align(title);
		content.add(title);
		
		pos += Math.max(ITEM_HEIGHT, title.height());
		
		for (String page : doc.pages()){
			docPage item = new docPage( doc, page );
			
			item.setRect( 0, pos, w, ITEM_HEIGHT );
			content.add( item );
			
			pos += item.height();
			pages.add(item);
		}
		
		content.setSize( w, pos );
		list.setSize( list.width(), list.height() );
		
	}
	
	private static class docPage extends ListItem {
		
		private boolean found = false;
		
		private Document doc;
		private String page;
		
		public docPage(Document doc, String page ){
			super( new ItemSprite( doc.pageSprite(), null),
					Messages.titleCase(doc.pageTitle(page)), -1);
			
			this.doc = doc;
			
			this.page = page;
			found = doc.hasPage(page);
			
			if (!found) {
				icon.hardlight( 0.5f, 0.5f, 0.5f);
				label.text( Messages.titleCase(Messages.get( WndDocument.class, "missing" )));
				label.hardlight( 0x999999 );
			}
			
		}
		
		public boolean onClick( float x, float y ) {
			if (inside( x, y ) && found) {
				ShatteredPixelDungeon.scene().addToFront( new WndStory( doc.pageBody(page) ));
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	private static class ListItem extends Component {
		
		protected RenderedTextBlock label;
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
			label = PixelScene.renderTextBlock( 7 );
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
	
}
