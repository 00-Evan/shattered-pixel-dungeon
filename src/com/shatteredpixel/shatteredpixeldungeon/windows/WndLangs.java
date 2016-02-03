/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.RenderedText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class WndLangs extends Window {

	private int WIDTH = 60;
	private int BTN_HEIGHT = 14;

	public WndLangs(){
		super();

		final ArrayList<Messages.Languages> langs = new ArrayList<>(Arrays.asList(Messages.Languages.values()));

		Messages.Languages nativeLang = Messages.Languages.matchLocale(Locale.getDefault());
		langs.remove(nativeLang);
		//move the native language to the top.
		langs.add(0, nativeLang);

		int y = 0;
		for (int i = 0; i < langs.size(); i++){
			final int langIndex = i;
			RedButton btn = new RedButton(Messages.titleCase(langs.get(i).nativeName())){
				@Override
				protected void onClick() {
					super.onClick();
					Messages.setup(langs.get(langIndex));
					ShatteredPixelDungeon.language(langs.get(langIndex));
					RenderedText.clearCache();
					PixelScene.windowOnCreate = WndLangs.class;
					ShatteredPixelDungeon.switchNoFade(TitleScene.class);
				}
			};
			if (ShatteredPixelDungeon.language() == langs.get(i)){
				btn.textColor(TITLE_COLOR);
			} else {
				switch (langs.get(i).status()) {
					case INCOMPLETE:
						btn.textColor(0x999999);
						break;
					case UNREVIEWED:
						btn.textColor(0xCCCCCC);
						break;
				}
			}
			btn.setSize(WIDTH, BTN_HEIGHT);
			btn.setPos(0, y);
			add(btn);
			y += 16;
		}
		resize(WIDTH, y-2);

	}

}
