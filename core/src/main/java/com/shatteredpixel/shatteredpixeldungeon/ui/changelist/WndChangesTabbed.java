/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTabbed;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;

import java.util.ArrayList;

public class WndChangesTabbed extends WndTabbed {

	protected static final int WIDTH_MIN    = 120;
	protected static final int WIDTH_MAX    = 220;
	protected static final int GAP	= 2;

	private ArrayList<RenderedTextBlock> texts = new ArrayList<>();

	public WndChangesTabbed(Image icon, String title, String... messages ) {

		super();

		int width = WIDTH_MIN;

		PointerArea blocker = new PointerArea( 0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height ) {
			@Override
			protected void onClick( PointerEvent event ) {
				onBackPressed();
			}
		};
		blocker.camera = PixelScene.uiCamera;
		add(blocker);

		IconTitle titlebar = new IconTitle( icon, title );
		titlebar.setRect( 0, 0, width, 0 );
		add(titlebar);

		RenderedTextBlock largest = null;
		for (int i = 0; i < messages.length; i++){
			RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
			text.text( messages[i], width );
			text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
			add( text );
			texts.add(text);

			if (largest == null || text.height() > largest.height()){
				largest = text;
			}

			int finalI = i;
			add(new LabeledTab(numToNumeral(finalI + 1)){
				@Override
				protected void select(boolean value) {
					super.select( value );
					texts.get(finalI).visible = value;
				}
			});
		}

		while (PixelScene.landscape()
				&& largest.bottom() > (PixelScene.MIN_HEIGHT_L - 20)
				&& width < WIDTH_MAX){
			width += 20;
			titlebar.setRect(0, 0, width, 0);

			largest = null;
			for (RenderedTextBlock text : texts){
				text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
				text.maxWidth(width);
				if (largest == null || text.height() > largest.height()){
					largest = text;
				}
			}
		}

		bringToFront(titlebar);

		resize( width, (int)largest.bottom() + 2 );

		layoutTabs();
		select(0);

	}

	private String numToNumeral(int num){
		switch (num){
			case 1: return "I";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			case 5: return "V";
			case 6: return "VI";
			case 7: return "VII";
			case 8: return "VIII";
			case 9: return "IX";
			case 10: return "X";
			default: return Integer.toString(num);
		}
	}
}
