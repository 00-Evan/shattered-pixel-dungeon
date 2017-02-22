/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene {

	private static final String TXT_Update =
			"_v0.5.0b:_\n" +
			"_-_ Added new Language: Indonesian\n" +
			"_-_ Fixed wand of fireblast not activating soul mark\n" +
			"_-_ Various bugfixes and optimizations\n" +
			"_-_ Translation updates\n" +
			"\n"+
			"_v0.5.0a:_\n" +
			"_-_ Added new Language: Esperanto\n" +
			"\n"+
			"_v0.5.0:_\n" +
			"New Visuals!:\n" +
			"_-_ Walls and some terrain now have depth\n" +
			"_-_ Characters & items are raised & cast shadows\n" +
			"_-_ Added a visible tile grid in the settings menu\n" +
			"\n"+
			"Balance Changes:\n" +
			"_-_ Quarterstaff armor bonus increased from 2 to 3\n" +
			"_-_ Wand of Frost damage against chilled enemies\n" +
			"\t \treduced from -7.5% per turn of chill to -10%\n" +
			"_-_ Wand of Transfusion self-damage reduced\n" +
			"\t \tfrom 15% max hp to 10% max hp per zap\n" +
			"_-_ Dried Rose charges 20% faster and the ghost\n" +
			"\t \thero is stronger, especially at low levels\n" +
			"_-_ Glyph of Entanglement activates less often\n" +
			"\t \tbut grants significantly more herbal armor\n" +
			"_-_ Glyph of Stone armor bonus reduced\n" +
			"\t \tfrom 2+level to 0+level\n" +
			"_-_ Glyph of Antimagic magical damage resist\n" +
			"\t \treduced from 50% of armor to 33% of armor\n" +
			"_-_ Glyph of Viscosity damage rate increased\n" +
			"\t \tfrom 10% of deferred damage to 15%\n" +
			"_-_ Exhausting Curse activates more often\n" +
			"\n" +
			"_v0.4.3:_ Various utility features and improvements\n" +
			"\n" +
			"_v0.4.2:_ Performance and game engine improvements\n" +
			"\n" +
			"_v0.4.1:_ Balance adjustments to enemies & armor\n" +
			"\n" +
			"_v0.4.0:_ Reworked equips, enchants & curses\n" +
			"\n" +
			"_v0.3.5:_ Reworked Warrior & subclasses\n" +
			"\n"+
			"_v0.3.4:_ Multiple language support\n" +
			"\n" +
			"_v0.3.3:_ Support for Google Play Games\n" +
			"\n" +
			"_v0.3.2:_ Prison Rework & Balance Changes\n" +
			"\n" +
			"_v0.3.1:_ Traps reworked & UI upgrades\n" +
			"\n" +
			"_v0.3.0:_ Wands & Mage completely reworked\n" +
			"\n" +
			"_v0.2.4:_ Small improvements and tweaks\n" +
			"\n" +
			"_v0.2.3:_ Artifact additions & improvements\n" +
			"\n" +
			"_v0.2.2:_ Small improvements and tweaks\n" +
			"\n" +
			"_v0.2.1:_ Sewer improvements\n" +
			"\n" +
			"_v0.2.0:_ Added artifacts, reworked rings\n" +
			"\n" +
			"_v0.1.1:_ Added blandfruit, reworked dew vial\n" +
			"\n" +
			"_v0.1.0:_ Improvements to potions/scrolls";

	@Override
	public void create() {
		super.create();

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedText title = PixelScene.renderText( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.x = (w - title.width()) / 2 ;
		title.y = 4;
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		RenderedTextMultiline text = renderMultiline(TXT_Update, 6 );

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
		int ph = h - 16;

		panel.size( pw, ph );
		panel.x = (w - pw) / 2f;
		panel.y = title.y + title.height();
		align( panel );
		add( panel );

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		text.maxWidth((int) panel.innerWidth());

		content.add(text);

		content.setSize( panel.innerWidth(), (int)Math.ceil(text.height()) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth(),
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
}


