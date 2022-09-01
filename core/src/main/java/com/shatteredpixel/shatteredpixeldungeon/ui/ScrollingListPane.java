/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ScrollingListPane extends ScrollPane {

	private ArrayList<Component> items = new ArrayList<>();

	private static final int ITEM_HEIGHT	= 18;

	public ScrollingListPane(){
		super(new Component());
	}

	@Override
	public void onClick(float x, float y) {
		for (Component item : items) {
			if ((item instanceof ListItem) && ((ListItem) item).onClick( x, y )) {
				break;
			}
		}
	}

	public void addItem( Image icon, String iconText, String text ){
		addItem( new ListItem(icon, iconText, text) );
	}

	public void addItem( ListItem item ){
		content.add(item);
		items.add(item);
		layout();
	}

	public void addTitle( String text ){
		ListTitle title = new ListTitle(text);
		content.add(title);
		items.add(title);
		layout();
	}

	@Override
	public synchronized void clear() {
		content.clear();
		items.clear();
	}

	@Override
	protected void layout() {
		super.layout();

		float pos = 0;
		for (Component item : items){
			item.setRect(0, pos, width, ITEM_HEIGHT);
			pos += item.height();
		}

		content.setSize(width, pos);
	}

	public static class ListItem extends Component {

		protected Image icon;
		protected BitmapText iconLabel;
		protected RenderedTextBlock label;
		protected ColorBlock line;

		public ListItem( Image icon, String text ) {
			this(icon, null, text);
		}

		public ListItem( Image icon, String iconText, String text ) {
			super();

			if (icon != null) {
				this.icon.copy(icon);
			} else {
				remove(label);
				label = PixelScene.renderTextBlock(9);
				add(label);
			}

			label.text( text );

			if (iconText != null) {
				iconLabel.text(iconText);
				iconLabel.measure();
			}
		}

		public boolean onClick( float x, float y ){
			return false;
		}

		public void hardlight( int color ){
			iconLabel.hardlight(color);
			label.hardlight(color);
		}

		public void hardlightIcon( int color ){
			icon.hardlight(color);
		}

		@Override
		protected void createChildren() {
			icon = new Image();
			add( icon );

			iconLabel = new BitmapText( PixelScene.pixelFont);
			add( iconLabel );

			label = PixelScene.renderTextBlock( 7 );
			add( label );

			line = new ColorBlock( 1, 1, 0xFF222222);
			add(line);

		}

		@Override
		protected void layout() {

			icon.y = y + 1 + (height() - 1 - icon.height()) / 2f;
			icon.x = x + (16 - icon.width())/2f;
			PixelScene.align(icon);

			iconLabel.x = icon.x + (icon.width - iconLabel.width()) / 2f;
			iconLabel.y = icon.y + (icon.height - iconLabel.height()) / 2f + 0.5f;
			PixelScene.align(iconLabel);

			line.size(width, 1);
			line.x = x;
			line.y = y;

			label.maxWidth((int)(width - 16 - 1));
			label.setPos(x + 17, y + (height() - label.height()) / 2f);
			PixelScene.align(label);
		}
	}

	public static class ListTitle extends Component {

		protected RenderedTextBlock label;
		protected ColorBlock line;

		public ListTitle (String title){
			super();
			label.text(title);
		}

		@Override
		protected void createChildren() {
			label = PixelScene.renderTextBlock( 9 );
			label.hardlight(Window.TITLE_COLOR);
			add( label );

			line = new ColorBlock( 1, 1, 0xFF222222);
			add(line);

		}

		@Override
		protected void layout() {

			line.size(width, 1);
			line.x = x;
			line.y = y;

			label.maxWidth((int)(width - 1));
			label.setPos((width-label.width())/2f,
					y + (height() - label.height()) / 2f);
			PixelScene.align(label);
		}

	}


}
