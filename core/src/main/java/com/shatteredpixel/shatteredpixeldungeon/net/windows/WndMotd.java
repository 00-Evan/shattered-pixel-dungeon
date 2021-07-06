/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.net.windows;

import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.net.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.net.ui.LabeledText;
import com.shatteredpixel.shatteredpixeldungeon.scenes.HeroSelectScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Image;

public class WndMotd extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;

	private static final int MARGIN 		= 2;

	public WndMotd(String motd, long seed) {
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		float pos = 0;
		IconTitle tfTitle = new IconTitle(Icons.get(Icons.NEWS), "Welcome");
		tfTitle.setRect(0, pos, width, 0);
		add(tfTitle);

		pos = tfTitle.bottom() + 2*MARGIN;

		layoutBody(pos, motd, seed);
	}

	private void layoutBody(float pos, String motd, long seed){
		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		RenderedTextBlock tfMesage = PixelScene.renderTextBlock( 6 );
		tfMesage.text(motd, width);
		tfMesage.setPos( 0, pos );
		add( tfMesage );

		pos = tfMesage.bottom() + 2*MARGIN;

		LabeledText seedText = new LabeledText("Seed", String.valueOf(seed), 6, 6);
		seedText.setPos(0, pos);
		add(seedText);

		RedButton okayBtn = new RedButton("Play"){
			@Override
			protected void onClick() {
				super.onClick();
				if(!(ShatteredPixelDungeon.scene() instanceof HeroSelectScene))
					ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
				else ShatteredPixelDungeon.switchNoFade(HeroSelectScene.class);
			}
		};

		okayBtn.setRect(width-30, pos, 30, 20);
		add(okayBtn);

		pos = okayBtn.bottom() + 2*MARGIN;

		resize( width, (int)(pos - MARGIN) );
	}

	@Override
	public void destroy() {
		super.destroy();
		if(!(ShatteredPixelDungeon.scene() instanceof HeroSelectScene))
			ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
	}

	protected boolean enabled(int index ){
		return true;
	}
	
	protected void onSelect( int index ) {}


}
