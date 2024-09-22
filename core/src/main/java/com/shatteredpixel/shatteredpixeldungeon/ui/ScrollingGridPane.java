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
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ScrollingGridPane extends ScrollPane {

	private ArrayList<Component> items = new ArrayList<>();
	private ArrayList<ColorBlock> separators = new ArrayList<>();

	private static final int ITEM_SIZE	= 17;
	private static final int MIN_GROUP_SIZE = 3*(ITEM_SIZE+1);

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
	}

	public void addHeader( String text ){
		addHeader( text, 7, false );
	}

	public void addHeader( String text, int size, boolean center ){
		GridHeader header = new GridHeader(text, size, center);
		content.add(header);
		items.add(header);
	}

	@Override
	public synchronized void clear() {
		content.clear();
		items.clear();
		separators.clear();
	}

	@Override
	protected void layout() {

		float left = 0;
		float top = 0;

		int sepsUsed = 0;

		//these variables help control logic for laying out multiple grid groups on one line
		boolean freshRow = true; //whether the previous group is still on its first row
		boolean lastWasSmallheader = false; //whether the last UI element was a header on its own
		float widthThisGroup = 0; //how wide the current group is (we use a min of 3 items)

		for (int i = 0; i < items.size(); i++){
			Component item = items.get(i);
			if (item instanceof GridHeader){
				//we can sometimes get two smaller headers next to each other if a group has no items in it
				//so we need to treat it as if there were grid items for proper layout
				if (left > 0 || lastWasSmallheader){

					//this bit of logic exists so that multiple headers can be on one row
					// if all of their groups have a small number of items, with a min space for 3
					float spacing = Math.max(0, MIN_GROUP_SIZE - widthThisGroup);
					float spaceLeft = width() - (left + spacing);
					int spaceReq = 0;
					for (int j = i+1; j < items.size(); j++){
						if (items.get(j) instanceof GridItem){
							spaceReq += ITEM_SIZE+1;
						} else {
							break;
						}
					}
					spaceReq = Math.max(spaceReq, MIN_GROUP_SIZE);
					if (!((GridHeader) item).center && freshRow && spaceLeft >= spaceReq){
						left = left + spacing;
						top -= item.height()+1;
						ColorBlock sep;
						if (separators.size() > sepsUsed){
							sep = separators.get(sepsUsed++);
						} else {
							sep = new ColorBlock(1, 1, 0xFF222222);
							separators.add(sep);
							content.add(sep);
							sepsUsed++;
						}
						sep.size(1, item.height()+1+ITEM_SIZE);
						sep.x = left-1;
						sep.y = top;
					} else {
						left = 0;
						top += ITEM_SIZE + 2;
						freshRow = true;
					}
				}
				item.setRect(left, top, width(), item.height());
				top += item.height()+1;
				widthThisGroup = 0;

				if (!((GridHeader) item).center){
					lastWasSmallheader = true;
				} else {
					lastWasSmallheader = false;
				}

			} if (item instanceof GridItem){
				if (left + ITEM_SIZE > width()) {
					left = 0;
					widthThisGroup = 0;
					top += ITEM_SIZE+1;
					freshRow = false;
				}
				item.setRect(left, top, ITEM_SIZE, ITEM_SIZE);
				left += ITEM_SIZE+1;
				widthThisGroup += ITEM_SIZE+1;
				lastWasSmallheader = false;
			}

		}
		if (left > 0){
			left = 0;
			top += ITEM_SIZE+1;
		}

		while (separators.size() > sepsUsed){
			ColorBlock sep = separators.remove(sepsUsed);
			content.remove(sep);
		}

		content.setSize(width, top);
		super.layout();
	}

	public static class GridItem extends Component {

		protected Image icon;

		protected Visual secondIcon;

		protected ColorBlock bg;

		public GridItem( Image icon ) {
			super();

			if (icon instanceof ItemSprite){
				this.icon = new ItemSprite();
			} else {
				this.icon = new Image();
			}
			this.icon.copy(icon);
			add(this.icon);
		}

		public void addSecondIcon( Visual icon ){
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
				secondIcon.x = x + width()-secondIcon.width();
				secondIcon.y = y;
			}

		}

	}

	public static class GridHeader extends Component {

		protected RenderedTextBlock text;
		boolean center;

		public GridHeader( String text ){
			this(text, 7, false);
		}

		public GridHeader( String text, int size, boolean center ){
			super();

			this.center = center;
			this.text = PixelScene.renderTextBlock(text, size);
			add(this.text);

		}

		@Override
		protected void createChildren() {
			super.createChildren();
		}

		@Override
		protected void layout() {
			super.layout();

			if (center){
				text.align(RenderedTextBlock.CENTER_ALIGN);
				text.maxWidth((int)width());
				text.setPos(x + (width() - text.width()) / 2, y+1);
			} else {
				text.maxWidth((int)width());
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
