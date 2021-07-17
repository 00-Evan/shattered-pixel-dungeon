/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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

package com.saqfish.spdnet.net.windows;

import com.saqfish.spdnet.actors.hero.HeroClass;
import com.saqfish.spdnet.net.events.recieve.playerlist.Player;
import com.saqfish.spdnet.net.events.recieve.playerlist.PlayerList;
import com.saqfish.spdnet.net.ui.BlueButton;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.ScrollPane;
import com.watabou.noosa.ui.Component;

public class WndPlayerList extends NetWindow {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	private static final int HEIGHT	= 120;

	public WndPlayerList(PlayerList p) {
		super(PixelScene.landscape() ? WIDTH_L : WIDTH_P, HEIGHT);

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		list.scrollTo( 0, 0 );

		float ypos = 0;

		for (int i=0; i < p.list.length; i++) {
			float xpos = 0;

			Player player = p.list[i];

			BlueButton playerInfo = new BlueButton(player.nick){
				@Override
				protected void onClick() {
					super.onClick();
					runWindow(new WndInfoPlayer(player));
				}
			};
			playerInfo.enable(player.depth != null);
			playerInfo.setRect( xpos, ypos, width, 18 );

			content.add( playerInfo );

			ypos+=playerInfo.height();

		}

		content.setRect(0,0, width, ypos );
		list.setRect( 0, 0, width, HEIGHT);
	}

	public static HeroClass playerClassToHeroClass(int playerClass){
		switch (playerClass){
			case 0: default:
				return HeroClass.WARRIOR;
			case 1:
				return HeroClass.MAGE;
			case 2:
				return HeroClass.ROGUE;
			case 3:
				return HeroClass.HUNTRESS;
		}
	}

	protected boolean enabled( int index ){
		return true;
	}

	protected void onSelect( int index ) {}
}
