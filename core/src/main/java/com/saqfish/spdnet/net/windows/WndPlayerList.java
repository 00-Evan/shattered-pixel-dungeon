/*
 * Pixel Dungeon
 * Copyright (C) 2021 saqfish
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
import com.saqfish.spdnet.net.events.Receive;
import com.saqfish.spdnet.net.ui.NetIcons;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.ui.IconButton;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.ScrollPane;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

public class WndPlayerList extends NetWindow {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 144;
	private static final int HEIGHT	= 120;

	private static final int VGAP = 5;
	private static final int HGAP = 3;

	public WndPlayerList(Receive.PlayerList p) {
		super(PixelScene.landscape() ? WIDTH_L : WIDTH_P, HEIGHT);

		float y = 2;

		RenderedTextBlock titleLbl = PixelScene.renderTextBlock("Online Players" , 9);
		add(titleLbl);
		titleLbl.setPos(VGAP/2, y);

		Image icon = NetIcons.get(NetIcons.CHAT);
		icon.scale.set(0.8f);
		IconButton chatBtn = new IconButton(icon){
			@Override
			protected void onClick() {
				super.onClick();
				Game.platform.showChat(false);
			}
		};
		add(chatBtn);
		chatBtn.setSize(10,10);
		chatBtn.setPos(width - chatBtn.width()-(VGAP/2), y);

		ColorBlock sep = new ColorBlock(1, 1, 0xFF000000);
		sep.size(width-(VGAP/2), 1);
		sep.x = VGAP/2;
		sep.y = chatBtn.bottom()+2;
		add(sep);

		y+=sep.y+HGAP;

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		list.scrollTo( 0, 0 );


		float ypos = 0;

		for (int i=0; i < p.list.length; i++) {
			float xpos = VGAP;

			Receive.Player player = p.list[i];

			PlayerRank playerRank = new PlayerRank(player, i+1){
				@Override
				protected void onClick() {
					if(player.depth != null)
						runWindow(new WndInfoPlayer(player));
				}
			};
			playerRank.setRect( xpos, ypos, width, 12 );

			content.add( playerRank );

			ypos=playerRank.bottom()+2;

		}

		content.setRect(0,y, width, ypos );
		list.setRect( 0, y, width, HEIGHT);
	}

	public static class PlayerRank extends Button {
		private int order;
		private RenderedTextBlock label;

		private boolean enabled;


		public PlayerRank(Receive.Player player, int order){
			this.order = order;
			this.enabled = player.depth != null;

			label = PixelScene.renderTextBlock(player.nick, 11);
			add(label);
		}

		@Override
		protected void layout() {
			super.layout();
			label.setPos(VGAP, y);
			label.alpha( enabled ? 1.0f : 0.3f );
		}

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
