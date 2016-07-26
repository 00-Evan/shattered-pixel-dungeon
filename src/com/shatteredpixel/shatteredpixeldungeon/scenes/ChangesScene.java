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
			"_0.4.1:_\n" +
			"Armor Balance Changes:\n" +
			"- Armor now has a min damage block value\n" +
			"- Armor gains more blocking from upgrades\n" +
			"- Prison+ enemy damage increased\n" +
			"- Evil Eyes reworked\n" +
			"\n" +
			"Wand Balance Changes:\n" +
			"- All wands damage adjusted/increased\n" +
			"- Upgraded wands appear slightly less often\n" +
			"- Wand of Lightning bonus damage reduced\n" +
			"- Wand of Fireblast uses fewer charges\n" +
			"- Wand of Venom damage increases over time\n" +
			"- Wand of Pris-Light bonus damage reduced\n" +
			"- Corrupted enemies live longer & no longer attack eachother\n" +
			"- Wands in the holster now charge faster\n" +
			"\n" +
			"Class Balance Changes:\n" +
			"- Mage's Staff melee damage increased\n" +
			"- Mage's Staff can now preserve one upgrade\n" +
			"- Cloak of Shadows buffed at lower levels\n" +
			"- Some Battlemage effects changed\n" +
			"\n" +
			"Ring Balance Changes:\n" +
			"- Ring of Force weaker at 18+ strength\n" +
			"- Ring of Tenacity reduces more damage\n" +
			"- Ring of Wealth secret rewards adjusted\n" +
			"- Ring of Evasion now works consistently\n" +
			"\n" +
			"Artifact Balance Changes:\n" +
			"- Dried Rose charges faster, ghost HP up\n" +
			"- Horn of Plenty now charges on exp gain\n" +
			"- Master Thieves Armband levels faster\n" +
			"- Sandals of Nature level faster\n" +
			"- Hourglass uses fewer charges at a time\n" +
			"\n" +
			"Various Item Balance Changes:\n" +
			"- Many shop prices adjusted\n" +
			"- Pirahna rooms no longer give cursed gear\n" +
			"- Brimstone glyph healing reduced\n" +
			"- Lucky enchant deals max dmg more often\n" +
			"- Dazzling enchant now cripples & blinds\n"+
			"- Flail now can't surprise attack\n" +
			"- Extra reach weapons no longer penetrate\n" +
			"\n" +
			"Misc Changes:\n" +
			"- Added a new journal button with key display\n" +
			"- Keys now exist in the journal, not inventory\n" +
			"- Improved donator menu button visuals\n" +
			"- Chasms now deal less damage, but bleed\n" +
			"- Increased the efficiency of data storage\n" +
			"- Translation updates\n" +
			"- Various bugfixes\n" +
			"\n" +
			"_v0.4.0:_\n" +
			"- Changes to weapons, enchants & glyphs.\n" +
			"- Curses now give negative effects\n" +
			"- Curses now harder to remove\n" +
			"- Upgrades reduce str requirements less\n" +
			"- Huntress now starts with knuckleduster\n" +
			"- Assassin sneak bonus damage reduced\n" +
			"- Gladiator's clobber now inflicts vertigo\n" +
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


