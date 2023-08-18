/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class WndBlacksmith extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 160;

	private static final int GAP  = 2;

	//A LOT still to do here!
	public WndBlacksmith(Blacksmith troll, Hero hero ) {
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		IconTitle titlebar = new IconTitle();
		titlebar.icon( troll.sprite() );
		titlebar.label( Messages.titleCase( troll.name() ) );
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );

		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "prompt"), 6 );
		message.maxWidth( width );
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );


		ArrayList<RedButton> buttons = new ArrayList<>();

		//TODO
		RedButton pickaxe = new RedButton("_Pickaxe (250 favor):_ \"I guess I can give you the pickaxe back if you really want it. I've got plenty more.\"", 6){
			@Override
			protected void onClick() {
				Blacksmith.Quest.pickaxe.collect(Dungeon.hero.belongings.backpack);
				Blacksmith.Quest.favor -= 250;
				Blacksmith.Quest.pickaxe = null;
				WndBlacksmith.this.hide();
				if (Blacksmith.Quest.favor > 0) {
					GameScene.show(new WndBlacksmith(troll, hero));
				}
			}
		};
		pickaxe.enable(Blacksmith.Quest.pickaxe != null && Blacksmith.Quest.favor >= 250);
		buttons.add(pickaxe);


		float pos = message.bottom() + 3*GAP;
		for (RedButton b : buttons){
			b.leftJustify = true;
			b.multiline = true;
			b.setSize(width, b.reqHeight());
			b.setRect(0, pos, width, b.reqHeight());
			b.enable(b.active); //so that it's visually reflected
			add(b);
			pos = b.bottom() + GAP;
		}

		resize(width, (int)pos);

	}

}
