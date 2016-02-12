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
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.RenderedText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class WndLangs extends Window {

	private int WIDTH = 120;
	private int BTN_WIDTH = 50;
	private int BTN_HEIGHT = 14;

	private int TEXT_LEFT = 55;
	private int TEXT_WIDTH = WIDTH - TEXT_LEFT;

	public WndLangs(){
		super();

		final ArrayList<Languages> langs = new ArrayList<>(Arrays.asList(Languages.values()));

		Languages nativeLang = Languages.matchLocale(Locale.getDefault());
		langs.remove(nativeLang);
		//move the native language to the top.
		langs.add(0, nativeLang);

		final Languages currLang = Messages.lang();

		//language buttons layout
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
			if (currLang == langs.get(i)){
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
			btn.setSize(BTN_WIDTH, BTN_HEIGHT);
			btn.setPos(0, y);
			add(btn);
			y += 15;
		}
		y--;
		resize(WIDTH, y);

		ColorBlock separator = new ColorBlock(1, y, 0xFF000000);
		separator.x = (BTN_WIDTH + TEXT_LEFT)/2f;
		add(separator);

		//language info layout.
		RenderedText title = PixelScene.renderText( Messages.titleCase(currLang.nativeName()) , 9 );
		title.x = TEXT_LEFT + (TEXT_WIDTH - title.width())/2f;
		title.y = 0;
		title.hardlight(TITLE_COLOR);
		add(title);

		if (currLang == Languages.ENGLISH){

			RenderedTextMultiline info = PixelScene.renderMultiline(6);
			info.text("This is the source language, written by the developer.", WIDTH - TEXT_LEFT);
			info.setPos(TEXT_LEFT, title.height() + 2);
			add(info);

		} else {

			RenderedTextMultiline info = PixelScene.renderMultiline(6);
			switch (currLang.status()) {
				case REVIEWED:
					info.text(Messages.get(this, "completed"), WIDTH - TEXT_LEFT);
					break;
				case UNREVIEWED:
					info.text(Messages.get(this, "unreviewed"), WIDTH - TEXT_LEFT);
					break;
				case INCOMPLETE:
					info.text(Messages.get(this, "unfinished"), WIDTH - TEXT_LEFT);
					break;
			}
			info.setPos(TEXT_LEFT, title.height() + 2);
			add(info);

			RedButton creditsBtn = new RedButton(Messages.titleCase(Messages.get(this, "credits"))){
				@Override
				protected void onClick() {
					super.onClick();
					String creds = "";
					String[] reviewers = currLang.reviewers();
					String[] translators = currLang.translators();
					if (reviewers.length > 0){
						creds += "_" + Messages.titleCase(Messages.get(WndLangs.class, "reviewers")) + "_\n";
						for (String reviewer : reviewers){
							creds += "-" + reviewer + "\n";
						}
						creds += "\n";
					}

					if (reviewers.length > 0 || translators.length > 0){
						creds += "_" + Messages.titleCase(Messages.get(WndLangs.class, "translators")) + "_";
						//reviewers are also translators
						for (String reviewer : reviewers){
							creds += "\n-" + reviewer;
						}
						for (String translator : translators){
							creds += "\n-" + translator;
						}
					}

					Window credits = new Window();

					RenderedTextMultiline title = PixelScene.renderMultiline(9);
					title.text(Messages.titleCase(Messages.get(WndLangs.class, "credits")) , 65);
					title.hardlight(SHPX_COLOR);
					title.setPos((65 - title.width())/2, 0);
					credits.add(title);

					RenderedTextMultiline text = PixelScene.renderMultiline(6);
					text.text(creds, 65);
					text.setPos(0, title.bottom() + 2);
					credits.add(text);

					credits.resize(65, (int)text.bottom());
					parent.add(credits);
				}
			};
			creditsBtn.setSize(creditsBtn.reqWidth() + 2, 16);
			creditsBtn.setPos(TEXT_LEFT + (TEXT_WIDTH - creditsBtn.width()) / 2f, y - 18);
			add(creditsBtn);

			RenderedTextMultiline transifex_text = PixelScene.renderMultiline(6);
			transifex_text.text(Messages.get(this, "transifex"), WIDTH - TEXT_LEFT);
			transifex_text.setPos(TEXT_LEFT, creditsBtn.top() - 2 - transifex_text.height());
			add(transifex_text);

		}

	}

}
