/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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
			"_v0.4.2b:_\n" +
			"- Various bug fixes\n" +
			"- Improved visual clarity of low hp warning\n" +
			"\n" +
			"_v0.4.2a:_\n" +
			"- Improved visual clarity on gas particles\n" +
			"\n" +
			"_v0.4.2:_\n" +
			"Optimizations:\n" +
			"- Many general performance improvements\n" +
			"- Game now uses 2 CPU cores, up from 1\n" +
			"- Reduced hitching on many devices\n" +
			"- Framerate improvements for older devices\n" +
			"\n" +
			"Balance Changes:\n" +
			"- Spear and Glaive damage reduced\n" +
			"- Runic blade damage reduced\n" +
			"- Grim enchant now procs more often\n" +
			"- Glyph of stone adds more weight\n" +
			"- Glyph of potential procs less often\n" +
			"- Wand of Fireblast less dangerous to caster\n" +
			"- Wand of Pris. Light reveal area reduced\n" +
			"- Ring of Wealth slightly more effective\n" +
			"- Ring of Sharpshooting gives more accuracy\n" +
			"\n" +
			"_v0.4.1:_\n" +
			"- Armor effectiveness increased\n" +
			"- Enemy damage increased to compensate\n" +
			"- Evil Eyes reworked\n" +
			"- All wands damage adjusted/increased\n" +
			"- Various wand mechanics adjusted\n" +
			"- Balance on many items adjusted\n" +
			"- Many shop prices adjusted\n" +
			"- Added a new journal button w/key display\n" +
			"\n" +
			"_v0.4.0:_ Reworked Equipment & enchants/curses.\n" +
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


		int pw = w - 6;
		int ph = h - 20;


		NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
		panel.size( pw, ph );
		panel.x = (w - pw) / 2;
		panel.y = title.y + title.height() + 2;
		add( panel );

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		text.maxWidth((int) panel.innerWidth());

		content.add(text);

		content.setSize( panel.innerWidth(), text.height() );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop(),
				panel.innerWidth(),
				panel.innerHeight());
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


