/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ScrollingGridPane extends ScrollPane {

	private ArrayList<Component> items = new ArrayList<>();

	private static final int ITEM_SIZE	= 17;

	public ScrollingGridPane(){
		super(new Component());
	}

	@Override
	public void onClick(float x, float y) {
		for (Component item : items) {
			if ((item instanceof ScrollingGridPane.GridItem) && ((ScrollingGridPane.GridItem) item).onClick( x, y )) {
				break;
			}
		}
	}

	public void addItem( ScrollingGridPane.GridItem item ){
		content.add(item);
		items.add(item);
		layout();
	}

	public void addHeader( String text ){
		addHeader( text, 7, false );
	}

	public void addHeader( String text, int size, boolean center ){
		GridHeader header = new GridHeader(text, size, center);
		content.add(header);
		items.add(header);
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

		float left = 0;
		float top = 0;
		for (Component item : items){
			if (item instanceof GridHeader){
				if (left > 0){
					left = 0;
					top += ITEM_SIZE+2;
				}
				item.setRect(left, top, width(), item.height());
				top += item.height()+1;
			} if (item instanceof GridItem){
				if (left + ITEM_SIZE > width()) {
					left = 0;
					top += ITEM_SIZE+1;
				}
				item.setRect(left, top, ITEM_SIZE, ITEM_SIZE);
				left += ITEM_SIZE+1;
			}
		}
		if (left > 0){
			left = 0;
			top += ITEM_SIZE+1;
		}

		content.setSize(width, top);
	}

	public static class GridItem extends Component {

		protected Image icon;

		protected Image secondIcon;

		protected ColorBlock bg;

		public GridItem( Image icon ) {
			super();

			this.icon.copy(icon);
		}

		public void addSecondIcon( Image icon ){
			secondIcon = icon;
			add(secondIcon);
			layout();
		}

		public void hardLightBG( float r, float g, float b ){
			bg.hardlight(r, g, b);
		}

		public boolean onClick( float x, float y ){
			return false;
		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock( 1, 1, 0x9953564D);
			add(bg);

			icon = new Image();
			add( icon );
		}

		@Override
		protected void layout() {

			bg.x = x;
			bg.y = y;
			bg.size(width(), height());

			icon.y = y + (height() - icon.height()) / 2f;
			icon.x = x + (width() - icon.width())/2f;
			PixelScene.align(icon);

			if (secondIcon != null){
				secondIcon.x = x + width()-secondIcon.width()-1;
				secondIcon.y = y + height()-secondIcon.height()-1;
			}

		}

	}

	public static class GridHeader extends Component {

		protected RenderedTextBlock text;
		boolean center;
		protected ColorBlock sep;

		public GridHeader( String text ){
			this(text, 7, false);
		}

		public GridHeader( String text, int size, boolean center ){
			super();

			this.center = center;
			this.text = PixelScene.renderTextBlock(text, size);
			add(this.text);

			if (center) {
				sep = new ColorBlock(1, 1, 0xFF222222);
				//add(sep);
			}
		}

		@Override
		protected void createChildren() {
			super.createChildren();
		}

		@Override
		protected void layout() {
			super.layout();

			if (center){
				text.setPos(x + (width() - text.width()) / 2, y+1);
				sep.size(width(), 1);
				sep.x = x;
				sep.y = bottom();
			} else {
				text.setPos(x, y+1);
			}
		}

		@Override
		public float height() {
			if (center){
				return text.height() + 3;
			} else {
				return text.height() + 2;
			}
		}
	}

}
