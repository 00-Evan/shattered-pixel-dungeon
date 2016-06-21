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
			"_v0.4.0:_\n" +
			"New Equipment Content:\n" +
			"- 13 new weapons, 12 rebalanced weapons\n" +
			"- 3 new enchants, 10 rebalanced enchants\n" +
			"- 8 new glyphs, 5 rebalanced glyphs\n" +
			"- 12 new curse effects\n" +
			"\n" +
			"Equipment Curses:\n" +
			"- Curses now give negative effects\n" +
			"- Curses no longer give negative levels\n" +
			"- Upgrades now weaken curses\n" +
			"- Remove curse scrolls now affect 1 item\n" +
			"\n" +
			"Equipment Balance:\n" +
			"- Tier 2-4 weapons do more base damage\n" +
			"- All weapons gain more dmg from upgrades\n" +
			"- Upgrades now remove enchants less often\n" +
			"- Upgrades reduce str requirements less\n" +
			"- All armors require 1 more str\n" +
			"- Encumbered characters can't sneak attack\n" +
			"\n" +
			"Droprate Changes:\n" +
			"- Powerful equipment less common early\n" +
			"- +3 and +2 equipment less common\n" +
			"- Equipment curses more common\n" +
			"- Tier 1 equipment no longer drops\n" +
			"- Arcane styli slightly more common\n" +
			"- Better item drops on floors 22-24\n" +
			"\n" +
			"Class Balance:\n" +
			"- Huntress now starts with knuckleduster\n" +
			"- Assassin sneak bonus damage reduced\n" +
			"- Fixed a bug where berserker was immune when enraged\n" +
			"- Gladiator's clobber now inflicts vertigo, not stun\n" +
			"\n" +
			"Enemy Balance:\n" +
			"- Tengu damage increased\n" +
			"- Prison Guard health and DR increased\n" +
			"\n" +
			"Misc:\n" +
			"- Scrolls of upgrade no longer burn\n" +
			"- Potions of strength no longer freeze\n" +
			"- Translation updates\n" +
			"- Various bugfixes\n" +
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


