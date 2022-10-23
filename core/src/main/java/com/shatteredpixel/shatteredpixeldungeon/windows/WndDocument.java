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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollingListPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndDocument extends Window {

	private ScrollingListPane list;

	public WndDocument(Document doc){
		list = new ScrollingListPane();
		add( list );

		list.addTitle(Messages.titleCase(doc.title()));

		for (String page : doc.pageNames()){
			boolean found = doc.isPageFound(page);
			ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(
					doc.pageSprite(),
					null,
					found ? Messages.titleCase(doc.pageTitle(page)) : Messages.titleCase(Messages.get( this, "missing" ))
			){
				@Override
				public boolean onClick(float x, float y) {
					if (inside( x, y ) && found) {
						ShatteredPixelDungeon.scene().addToFront( new WndStory(
								doc.pageSprite(page),
								doc.pageTitle(page),
								doc.pageBody(page) ));
						doc.readPage(page);
						hardlight(Window.WHITE);
						return true;
					} else {
						return false;
					}
				}
			};
			if (!found){
				item.hardlight(0x999999);
				item.hardlightIcon(0x999999);
			} else if (!doc.isPageRead(page)){
				item.hardlight(Window.TITLE_COLOR);
			}
			list.addItem(item);
		}

		resize(120, Math.min(144, (int)list.content().height()));
		list.setRect(0, 0, width, height);
	}

}
