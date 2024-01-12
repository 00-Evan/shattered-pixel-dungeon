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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Image;

import java.util.ArrayList;

//similar to WndOptions, but tries to place multiple buttons per row
public class WndOptionsCondensed extends WndOptions {

	public WndOptionsCondensed(Image icon, String title, String message, String... options) {
		super(icon, title, message, options);
	}

	public WndOptionsCondensed( String title, String message, String... options ) {
		super(title, message, options);
	}

	@Override
	protected void layoutBody(float pos, String message, String... options) {
		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		RenderedTextBlock tfMesage = PixelScene.renderTextBlock( 6 );
		tfMesage.text(message, width);
		tfMesage.setPos( 0, pos );
		add( tfMesage );

		pos = tfMesage.bottom() + 2*MARGIN;

		ArrayList<RedButton> buttons = new ArrayList<>();

		for (int i=0; i < options.length; i++) {
			final int index = i;
			RedButton btn = new RedButton( options[i] ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};
			if (hasIcon(i)) btn.icon(getIcon(i));
			btn.enable(enabled(i));
			btn.setSize(btn.reqWidth(), BUTTON_HEIGHT);
			add( btn );
			buttons.add(btn);
		}

		ArrayList<RedButton> curRow = new ArrayList<>();
		float widthLeftThisRow = width;

		while( !buttons.isEmpty() ){
			RedButton btn = buttons.get(0);

			widthLeftThisRow -= btn.width();
			if (curRow.isEmpty()) {
				curRow.add(btn);
				buttons.remove(btn);
			} else {
				widthLeftThisRow -= MARGIN;
				if (widthLeftThisRow >= 0) {
					curRow.add(btn);
					buttons.remove(btn);
				}
			}

			//layout current row. Currently forces a max of 5 buttons but can work with more
			if (buttons.isEmpty() || widthLeftThisRow <= 0 || curRow.size() >= 5){

				//re-use this variable for laying out the buttons
				widthLeftThisRow = width - (curRow.size()-1);
				for (RedButton b : curRow){
					widthLeftThisRow -= b.width();
				}

				//while we still have space in this row, find the shortest button(s) and extend them
				while (widthLeftThisRow > 0){

					ArrayList<RedButton> shortest = new ArrayList<>();
					RedButton secondShortest = null;

					for (RedButton b : curRow) {
						if (shortest.isEmpty()) {
							shortest.add(b);
						} else {
							if (b.width() < shortest.get(0).width()) {
								secondShortest = shortest.get(0);
								shortest.clear();
								shortest.add(b);
							} else if (b.width() == shortest.get(0).width()) {
								shortest.add(b);
							} else if (secondShortest == null || secondShortest.width() > b.width()){
								secondShortest = b;
							}
						}
					}

					float widthToGrow;

					if (secondShortest == null){
						widthToGrow = widthLeftThisRow / shortest.size();
						widthLeftThisRow = 0;
					} else {
						widthToGrow = secondShortest.width() - shortest.get(0).width();
						if ((widthToGrow * shortest.size()) >= widthLeftThisRow){
							widthToGrow = widthLeftThisRow / shortest.size();
							widthLeftThisRow = 0;
						} else {
							widthLeftThisRow -= widthToGrow * shortest.size();
						}
					}

					for (RedButton toGrow : shortest){
						toGrow.setRect(0, 0, toGrow.width()+widthToGrow, toGrow.height());
					}
				}

				//finally set positions
				float x = 0;
				for (RedButton b : curRow){
					b.setRect(x, pos, b.width(), b.height());
					x += b.width() + 1;
				}

				//move to next line and reset variables
				pos += BUTTON_HEIGHT+MARGIN;
				widthLeftThisRow = width;
				curRow.clear();

			}

		}

		resize( width, (int)(pos - MARGIN) );
	}

	@Override
	protected boolean hasInfo(int index) {
		return false; //does nothing here, no room
	}
}
