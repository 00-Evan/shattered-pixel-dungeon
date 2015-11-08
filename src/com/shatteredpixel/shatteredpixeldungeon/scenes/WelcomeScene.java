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

	private static final String TTL_Update = "v0.3.2: The Prison Rework!";

	private static final String TTL_Future = "Wait What?";

	private static final String TXT_Welcome =
			"Shattered Pixel Dungeon is a rework/expansion of Watabou's Pixel Dungeon.\n\n"+
			"The goal is to enhance the game by improving existing content and adding tonnes of new stuff!\n\n"+
			"Shattered Pixel Dungeon is being constantly updated, so expect more new content soon!\n\n"+
			"Happy Dungeoneering!";

	private static final String TXT_Update =
				"v0.3.2b:\n" +
					"- Fixed various bugs\n" +
					"- Floor locking tweaked, now only pauses passive effects when avoiding battle.\n" +
					"\n" +
					"v0.3.2a:\n" +
					"- Fixed various bugs\n" +
					"- Thieves now flee at 5/6 speed\n" +
					"- Reduced blind/cripple from bandits\n" +
					"- Tengu moves less on his second phase\n" +
					"\n" +
					"v0.3.2:\n" +
					"Prison Rework!:\n" +
					"- Tengu boss fight completely redone\n" +
					"- Corpse dust quest overhauled\n" +
					"- Rotberry quest overhauled\n" +
					"- NEW elemental embers quest\n" +
					"- NEW prison mob: insane prison guards!\n" +
					"- Thieves can escape with a stolen item\n" +
					"- Gnoll shaman attack speed increased\n" +
					"\n" +
					"BIG BALANCE CHANGES: \n" +
					"- Mastery Book is not attainable until floor 10, even when unlocked\n" +
					"- Hunger damage now increases with hero level, but starts out lower \n" +
					" \n" +
					"Sewers rebalance: \n" +
					"- Marsupial rat dmg and evasion reduced\n" +
					"- Gnoll scout accuracy reduced\n" +
					"- Sewer crabs less likely to spawn on floor 3, grant more exp\n" +
					"- Fly swarms rebalanced, moved to the sewers\n" +
					"- Great Crab HP reduced \n" +
					"- Goo fight rebalanced \n" +
					" \n" +
					"Base Class Changes: \n" +
					"- Mage's staff base damage increased \n" +
					"- Huntress now starts with 20 hp \n" +
					"- Huntress no longer heals more from dew \n" +
					"- Rogue's cloak of shadows now drains less while invisible\n" +
					" \n" +
					"Subclass Changes: \n" +
					"- Berserker now starts raging at 50% hp (up from 40%) \n" +
					"- Warden now heals 2 extra HP from dew \n" +
					"- Warlock completely overhauled\n" +
					"\n" +
					"Misc. changes:\n" +
					"- Fixed 'white line' graphical artifacts\n" +
					"- Floor locking now pauses all passive effects, not just hunger\n" +
					"- Cursed chains now only cripple, do not root\n" +
					"- Warping trap rebalanced, much less harsh\n" +
					"- Various other bugfixes";

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

			text = createMultiline(TXT_Welcome, 8);
			title = createMultiline(TTL_Welcome, 16);

		} else if (gameversion <= Game.versionCode) {

			text = createMultiline(TXT_Update, 6 );
			title = createMultiline(TTL_Update, 9 );

		} else {

			text = createMultiline( TXT_Future, 8 );
			title = createMultiline( TTL_Future, 16 );

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
		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop(),
				panel.innerWidth(),
				panel.innerHeight());
		list.scrollTo( 0, 0 );

		Component content = list.content();
		content.clear();

		text.maxWidth = (int) panel.innerWidth();
		text.measure();

		content.add(text);

		content.setSize( panel.innerWidth(), text.height() );

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


