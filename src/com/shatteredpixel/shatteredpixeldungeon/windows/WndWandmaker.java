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

import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

public class WndWandmaker extends Window {

	private static final String TXT_DUST =
		"Oh, I see you have the dust! Don't worry about the wraiths, I can deal with them. " +
		"As I promised, you can choose one of my high quality wands.";

	private static final String TXT_EMBER =
		"Oh, I see you have the embers! I do hope the fire elemental wasn't too much trouble. " +
		"As I promised, you can choose one of my high quality wands.";

	private static final String TXT_BERRY =
		"Oh, I see you have the berry! I do hope the rotberry plant didn't trouble you too much. " +
		"As I promised, you can choose one of my high quality wands.";
	
	private static final String TXT_FARAWELL	= "Good luck in your quest, %s!";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public WndWandmaker( final Wandmaker wandmaker, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add( titlebar );

		String msg = "";
		if (item instanceof CorpseDust){
			msg = TXT_DUST;
		} else if (item instanceof Embers){
			msg = TXT_EMBER;
		} else if (item instanceof Wandmaker.Rotberry.Seed){
			msg = TXT_BERRY;
		}

		BitmapTextMultiline message = PixelScene.createMultiline( msg, 6 );
		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnWand1 = new RedButton( Wandmaker.Quest.wand1.name() ) {
			@Override
			protected void onClick() {
				selectReward( wandmaker, item, Wandmaker.Quest.wand1 );
			}
		};
		btnWand1.setRect(0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT);
		add( btnWand1 );
		
		RedButton btnWand2 = new RedButton( Wandmaker.Quest.wand2.name() ) {
			@Override
			protected void onClick() {
				selectReward( wandmaker, item, Wandmaker.Quest.wand2 );
			}
		};
		btnWand2.setRect(0, btnWand1.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add( btnWand2 );
		
		resize(WIDTH, (int) btnWand2.bottom());
	}
	
	private void selectReward( Wandmaker wandmaker, Item item, Wand reward ) {
		
		hide();
		
		item.detach( Dungeon.hero.belongings.backpack );

		reward.identify();
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, wandmaker.pos ).sprite.drop();
		}
		
		wandmaker.yell( Utils.format( TXT_FARAWELL, Dungeon.hero.givenName() ) );
		wandmaker.destroy();
		
		wandmaker.sprite.die();
		
		Wandmaker.Quest.complete();
	}
}
