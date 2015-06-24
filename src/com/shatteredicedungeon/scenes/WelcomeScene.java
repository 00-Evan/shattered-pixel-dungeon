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
package com.shatteredicedungeon.scenes;

import com.shatteredicedungeon.Badges;
import com.shatteredicedungeon.Chrome;
import com.shatteredicedungeon.Rankings;
import com.shatteredicedungeon.ShatteredIceDungeon;
import com.shatteredicedungeon.ui.Archs;
import com.shatteredicedungeon.ui.RedButton;
import com.shatteredicedungeon.ui.ScrollPane;
import com.shatteredicedungeon.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class WelcomeScene extends PixelScene {

	private static final String TTL_Welcome = "Welcome!";

	private static final String TTL_Update = "v0.3.0: The Wand Rework!";

	private static final String TTL_Future = "Wait What?";

	private static final String TXT_Welcome =
			"Shattered Pixel Dungeon is a rework/expansion of Watabou's Pixel Dungeon.\n\n"+
			"The goal is to enhance the game by improving existing content and adding tonnes of new stuff!\n\n"+
			"Shattered Pixel Dungeon is being constantly updated, so expect more new content soon!\n\n"+
			"Happy Dungeoneering!";

	private static final String TXT_Update =
			"v0.3.0d:\n" +
					"- Various bugfixes\n" +
					"\n" +
					"v0.3.0c:\n" +
					"- Lots of under-the-hood changes\n" +
					"(let me know if you run into any bugs)\n" +
					"- Many bugfixes\n" +
					"Rebalances:\n" +
					"- Wand of Corruption & Venom rebalanced.\n" +
					"- Mages staff now has +1 max charges\n" +
					"- Mage now partially IDs wands on use\n" +
					"- Magic missile wand reduced to 3 max charges\n" +
					"- Warlock gets more recharge from food\n" +
					"- Battlemage only recharges his staff\n" +
					"- Many battlemage effects tweaked\n" +
					"- Yog Dzewa now heals 1hp per turn\n" +
					"\n" +
					"v0.3.0b & v0.3.0a:\n" +
					"- Fixed many bugs\n" +
					"- Buffed mage and huntress base damages to compensate for increased rat numbers on floor 1.\n" +
					"\n" +
					"v0.3.0:\n" +
					"Mage reworked!:\n" +
					"- No longer starts with knuckledusters or a wand\n" +
					"- Can no longer equip wands\n" +
					"- Now starts with a unique mages staff, empowered with magic missile to start.\n" +
					"- Battlemage reworked, staff now deals bonus effects when used as a melee weapon.\n" +
					"- Warlock reworked, gains more health and fullness from gaining exp, but food no longer restores hunger.\n" +
					"\n" +
					"General Wand Changes:\n" +
					"- Wand types are now known by default.\n" +
					"- Wands now each have unique sprites.\n" +
					"- Wands now cap at 10 charges instead of 9\n" +
					"- Wands now recharge faster the more charges are missing, for all classes.\n" +
					"- Self-targeting with wands is no longer possible.\n" +
					"- Wand recharge effects now give charge over time, instead of instantly.\n" +
					"- Wands can now be cursed!\n" +
					"\n" +
					"All wands have been reworked!\n" +
					"\n" +
					"Removed wands:\n" +
					"- Flock\n" +
					"- Blink\n" +
					"- Teleportation\n" +
					"- Avalanche\n" +
					"\n" +
					"Reworked wands:\n" +
					"- Magic Missile\n" +
					"- Lightning\n" +
					"- Disintegration\n" +
					"- Fireblast (previously Firebolt)\n" +
					"- Venom (previously poison)\n" +
					"- Frost (previously Slowing)\n" +
					"- Blast Wave (previously Telekinesis)\n" +
					"- Corruption (previously Amok)\n" +
					"- Regrowth\n" +
					"\n" +
					"New Wands:\n" +
					"- Prismatic Light\n" +
					"- Transfusion\n" +
					"\n" +
					"New Artifacts:\n" +
					"- Ethereal Chains\n" +
					"- Lloyd's Beacon\n" +
					"\n" +
					"Misc. Balance changes:\n" +
					"- Blessed Ankhs now revive at 1/4hp, but also grant initiative.\n" +
					"- Alchemist's Toolkit removed (will be reworked)\n" +
					"- Chalice of blood nerfed, now regens less hp at high levels.\n" +
					"- Cape of Thorns buffed, now absorbs all damage, but only deflects adjacent attacks.\n" +
					"- Sandals of nature adjusted, now give more seeds, less dew.\n" +
					"- Hunger no longer increases while fighting bosses.\n" +
					"- Floor 1 now spawns 10 rats every time, exactly enough to level up.\n" +
					"- Scrolls of recharging and mirror image are now more common.\n" +
					"- Mimics are now less common, stronger, & give better loot.\n" +
					"\n" +
					"UI tweaks:\n" +
					"- New app icon!\n" +
					"- Shading added to main game interface\n" +
					"- Buffs now have descriptions, tap their icons!\n" +
					"- Visual indicator added for surprising enemies";

	private static final String TXT_Future =
			"It seems that your current saves are from a future version of Shattered Pixel Dungeon!\n\n"+
			"Either you're messing around with older versions of the app, or something has gone buggy.\n\n"+
			"Regardless, tread with caution! Your saves may contain things which don't exist in this version, "+
			"this could cause some very weird errors to occur.";

	private static final String LNK = "https://play.google.com/store/apps/details?id=com.shatteredicedungeon";

	@Override
	public void create() {
		super.create();

		final int gameversion = ShatteredIceDungeon.version();

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

		title.x = align( (w - title.width()) / 2 );
		title.y = align( 8 );
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


//				if (gameversion <= 32){
//					//removes all bags bought badge from pre-0.2.4 saves.
//					Badges.disown(Badges.Badge.ALL_BAGS_BOUGHT);
//					Badges.saveGlobal();
//
//					//imports new ranking data for pre-0.2.3 saves.
//					if (gameversion <= 29){
//						Rankings.INSTANCE.load();
//						Rankings.INSTANCE.save();
//					}
//				}

				ShatteredIceDungeon.version(Game.versionCode);
				Game.switchScene(TitleScene.class);
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
}


