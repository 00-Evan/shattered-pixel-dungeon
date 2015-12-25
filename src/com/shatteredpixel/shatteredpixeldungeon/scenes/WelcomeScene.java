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
package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class WelcomeScene extends PixelScene {

	private static final String TTL_Welcome = "Welcome!";

	private static final String TTL_Update = "v0.3.3";

	private static final String TTL_Future = "Wait What?";

	private static final String TXT_Welcome =
				"Shattered Pixel Dungeon is a roguelike RPG, with randomly generated enemies, maps, items, and traps!\n" +
				"\n" +
				"Each run is a new challenging experience, but be careful, death is permanent!\n" +
				"\n" +
				"Shattered Pixel Dungeon is based on Watabou's Pixel Dungeon, if you're familiar with the original game, here is a list of major changes:\n" +
				"- Mage class and wands totally reworked\n" +
				"- Rings totally reworked, plus big changes to the rogue\n" +
				"- A new category of item: Artifacts!\n" +
				"- Enemy, boss, and quest changes to floors 1-10\n" +
				"- Subclasses currently being reworked, and are only available after floor 10\n" +
				"- Lots of balance changes, including removing degradation\n" +
				"- Over 20 new trap types!\n" +
				"- A redesigned UI with up to 4 quickslots\n" +
				"- Updates with new and reworked content roughly once a month\n" +
				"\n" +
				"\n" +
				"Happy Dungeoneering!";

	private static final String TXT_Update =
				"v0.3.3b:\n" +
				"- Fixed start-crash on android 2.2\n" +
				"- Fixed various crash bugs\n" +
				"\n" +
				"v0.3.3a:\n" +
				"- Added a small holiday treat ;)\n" +
				"- Thieves now disappear when they get away\n" +
				"- Fixed chests not opening with enemies nearby\n" +
				"- Fixed numerous bugs and crashes\n" +
				"\n" +
				"v0.3.3:\n" +
				"Gameplay Changes:\n" +
				"- Tengu's maze is now different each time\n" +
				"- Items no longer auto-pickup when enemies are near\n" +
				"\n" +
				"Fixes:\n" +
				"- Fixed several bugs with prison enemies\n" +
				"- Fixed some landscape window size issues\n" +
				"- Fixed other minor bugs\n" +
				"\n" +
				"Misc:\n" +
				"- Added support for reverse landscape\n" +
				"\n" +
				"There's a lot of behind-the-scenes technical changes in this update, so let me know if you run into any issues!";

	private static final String TXT_Future =
			"It seems that your current saves are from a future version of Shattered Pixel Dungeon!\n\n"+
			"Either you're messing around with older versions of the app, or something has gone buggy.\n\n"+
			"Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
			"this could cause some very weird errors to occur.";

	private static final String LNK = "https://play.google.com/store/apps/details?id=com.shatteredpixel.shatteredpixeldungeon";

	@Override
	public void create() {
		super.create();

		final int gameversion = ShatteredPixelDungeon.version();

		BitmapTextMultiline title;
		BitmapTextMultiline text;

		if (gameversion == 0) {

			text = createMultiline(TXT_Welcome, 6 );
			title = createMultiline(TTL_Welcome, 12 );

		} else if (gameversion <= Game.versionCode) {

			text = createMultiline(TXT_Update, 6 );
			title = createMultiline(TTL_Update, 9 );

		} else {

			text = createMultiline( TXT_Future, 6 );
			title = createMultiline( TTL_Future, 12 );

		}

		int w = Camera.main.width;
		int h = Camera.main.height;

		int pw = w - 10;
		int ph = h - 50;

		title.maxWidth = pw;
		title.measure();
		title.hardlight(Window.SHPX_COLOR);

		title.x = (w - title.width()) / 2;
		title.y = 8;
		add( title );

		NinePatch panel = Chrome.get(Chrome.Type.WINDOW);
		panel.size( pw, ph );
		panel.x = (w - pw) / 2;
		panel.y = (h - ph) / 2;
		add( panel );

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		text.maxWidth = (int) panel.innerWidth();
		text.measure();

		content.add(text);

		content.setSize( panel.innerWidth(), text.height() );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop(),
				panel.innerWidth(),
				panel.innerHeight());
		list.scrollTo(0, 0);

		RedButton okay = new RedButton("Okay!") {
			@Override
			protected void onClick() {


				if (gameversion <= 32){
					//removes all bags bought badge from pre-0.2.4 saves.
					Badges.disown(Badges.Badge.ALL_BAGS_BOUGHT);
					Badges.saveGlobal();

					//imports new ranking data for pre-0.2.3 saves.
					if (gameversion <= 29){
						Rankings.INSTANCE.load();
						Rankings.INSTANCE.save();
					}
				}

				if (ShatteredPixelDungeon.version() != Game.versionCode){
					ShatteredPixelDungeon.version(Game.versionCode);
					Game.switchScene(TitleScene.class);
				} else
					ShatteredPixelDungeon.switchNoFade(TitleScene.class);

			}
		};

		/*
		okay.setRect(text.x, text.y + text.height() + 5, 55, 18);
		add(okay);

		RedButton changes = new RedButton("Changes") {
			@Override
			protected void onClick() {
				parent.add(new WndChanges());
			}
		};

		changes.setRect(text.x + 65, text.y + text.height() + 5, 55, 18);
		add(changes);*/

		okay.setRect((w - pw) / 2, h - 22, pw, 18);
		add(okay);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		if (ShatteredPixelDungeon.version() != Game.versionCode){
			super.onBackPressed();
		} else
			ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
}


