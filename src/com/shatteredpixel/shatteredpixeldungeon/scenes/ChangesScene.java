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
			"_v0.3.5a:_\n" +
			"- Reduced warrior shield charge earlygame\n" +
			"- Reduced berserker damage at higher HP\n" +
			"- Increased berserk exhaust damage penalty\n" +
			"- Increased power of 2,4,6 combo finishers\n" +
			"- Translation updates\n" +
			"- Various bugfixes\n" +
			"\n" +
			"_v0.3.5:_\n" +
			"Warrior Rework:\n" +
			"- Starting STR down to 10, from 11\n" +
			"- Short sword dmg down to 1-10, from 1-12\n" +
			"- Short sword can no longer be reforged\n" +
			"- Now IDs potions of health, not STR\n" +
			"- Now starts with a unique seal for armor\n" +
			"- Seal grants shielding ontop of health\n" +
			"- Seal allows for one upgrade transfer\n" +
			"\n" +
			"Berserker Rework:\n" +
			"- Bonus damage now scales with lost HP, instead of a flat 50% at 50% hp\n" +
			"- Berserker can now endure through death for a short time, with caveats\n" +
			"\n" +
			"Gladiator Rework:\n" +
			"- Combo no longer grants bonus damage\n" +
			"- Combo is now easier to stack\n" +
			"- Combo now unlocks special finisher moves\n" +
			"\n" +
			"Balance Tweaks:\n" +
			"- Spears can now reach enemies 1 tile away\n" +
			"- Wand of Blast Wave now pushes bosses less\n" +
			"\n" +
			"Misc:\n" +
			"- Can now examine multiple things in one tile\n" +
			"- Classic font added for Russian language\n" +
			"- Added Hungarian language\n" +
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


