/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.Collections;

public class WndJournal extends Window {

	private static final int WIDTH        = 112;
	private static final int HEIGHT_P    = 160;
	private static final int HEIGHT_L    = 144;

	private static final int ITEM_HEIGHT	= 14;
	
	private RenderedText txtTitle;
	private ScrollPane list;
	
	public WndJournal() {
		
		super();
		resize( WIDTH, ShatteredPixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P );

		txtTitle = PixelScene.renderText( Messages.get(this, "title"), 9 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		txtTitle.x = (WIDTH - txtTitle.width()) / 2;
		PixelScene.align(txtTitle);
		add( txtTitle );
		
		Component content = new Component();
		
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
				ListItem item = new ListItem( text, i );
				item.setRect( 0, pos, WIDTH, ITEM_HEIGHT );
				content.add( item );

				pos += item.height();
			}
			if (Dungeon.hero.belongings.ironKeys[i] > 0){
				String text = Messages.capitalize(Messages.get(IronKey.class, "name"));

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
		
		list = new ScrollPane( content );
		add( list );

		list.setRect( 0, txtTitle.height(), WIDTH, height - txtTitle.height() );
	}
	
	private static class ListItem extends Component {
		
		private RenderedText feature;
		private BitmapText depth;
		private ColorBlock line;
		private Image icon;
		
		public ListItem( String text, int d ) {
			super();
			
			feature.text( text );
			
			depth.text( Integer.toString( d ) );
			depth.measure();
			
			if (d == Dungeon.depth) {
				feature.hardlight( TITLE_COLOR );
				depth.hardlight( TITLE_COLOR );
			}
		}
		
		@Override
		protected void createChildren() {
			feature = PixelScene.renderText( 7 );
			add( feature );
			
			depth = new BitmapText( PixelScene.pixelFont);
			add( depth );

			line = new ColorBlock( 1, 1, 0xFF222222);
			add(line);
			
			icon = Icons.get( Icons.DEPTH );
			add( icon );
		}
		
		@Override
		protected void layout() {
			
			icon.x = width - icon.width;
			
			depth.x = icon.x - 1 - depth.width();
			depth.y = y + (height() - depth.height() + 1) / 2f;
			PixelScene.align(depth);
			
			icon.y = y + (height() - icon.height()) / 2f;
			PixelScene.align(icon);

			line.size(width, 1);
			line.x = 0;
			line.y = y;
			
			feature.y = y + (height() - feature.baseLine()) / 2f;
			PixelScene.align(feature);
		}
	}
}
