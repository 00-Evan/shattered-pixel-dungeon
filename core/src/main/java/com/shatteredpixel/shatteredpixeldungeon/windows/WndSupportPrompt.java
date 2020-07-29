/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SupporterScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.watabou.utils.DeviceCompat;

public class WndSupportPrompt extends WndTitledMessage {

	public WndSupportPrompt(){
		super( new IconTitle(Icons.get(Icons.SHPX), Messages.get(WndSupportPrompt.class, "title")),
				Messages.get(WndSupportPrompt.class, "intro") + "\n\n" +
				Messages.get(SupporterScene.class, "patreon_msg") + "\n" +
				(Messages.lang() != Languages.ENGLISH ? Messages.get(SupporterScene.class, "patreon_english") + "\n\n" : "\n") +
				"- Evan");

		float y = height;

		RedButton link = new RedButton(Messages.get(SupporterScene.class, "supporter_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://www.patreon.com/ShatteredPixel";
				//tracking codes, so that the website knows where this pageview came from
				link += "?utm_source=shatteredpd";
				link += "&utm_medium=android";
				link += "&utm_campaign=supporter_prompt";
				DeviceCompat.openURI(link);
				SPDSettings.supportNagged(true);
				WndSupportPrompt.super.hide();
			}
		};
		link.setRect(0, y + 2, width, 18);
		add(link);

		RedButton close = new RedButton(Messages.get(this, "close")){
			@Override
			protected void onClick() {
				super.onClick();
				SPDSettings.supportNagged(true);
				WndSupportPrompt.super.hide();
			}
		};
		close.setRect(0, link.bottom() + 2, width, 18);
		add(close);

		resize(width, (int)close.bottom());

	}

	@Override
	public void hide() {
		//do nothing, have to close via the close button
	}
}
