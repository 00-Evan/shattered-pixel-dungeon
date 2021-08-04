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

package com.saqfish.spdnet.net.scenes;

import com.saqfish.spdnet.Assets;
import com.saqfish.spdnet.Rankings;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.net.events.Events;
import com.saqfish.spdnet.scenes.PixelScene;
import com.saqfish.spdnet.scenes.TitleScene;
import com.saqfish.spdnet.ui.Archs;
import com.saqfish.spdnet.ui.ExitButton;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.saqfish.spdnet.ShatteredPixelDungeon.net;

public class NetRankingsScene extends PixelScene {

	private static final float GAP	= 4;

	private Archs archs;

	private boolean connected;


	@Override
	public void create() {
		super.create();

		this.connected = net().connected();

		Music.INSTANCE.play( Assets.Music.THEME, true );

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Rankings.INSTANCE.load();

		RenderedTextBlock title = PixelScene.renderTextBlock( "Net Rankings", 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);

		if (connected) {
			net().sender().sendRecordsRequest();
			net().socket().once(Events.RECORDS, args -> {
				Game.runOnRenderThread(() -> {
					String jsonString = (String) args[0];
					JSONObject reader = null;
					try {
						reader = new JSONObject(jsonString);
						Iterator iteratorObj = reader.keys();
						float pos = title.bottom()+GAP;
						while (iteratorObj.hasNext())
						{
							String nick = (String)iteratorObj.next();
							int wins = reader.getInt(nick);
							Record row = new Record(nick, wins);
							row.setRect((w-50)/2f, pos , 50, 10);
							add(row);
							pos+=10;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				});
			});
		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}

	public static class Record extends Button {
		private static final float GAP	= 4;

		private RenderedTextBlock nick;
		private RenderedTextBlock wins;

		public Record( String nick, int wins) {
			super();
			this.nick.text(nick);
			this.wins.text(String.valueOf(wins));
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			nick = renderTextBlock( 9 );
			add( nick );

			wins = renderTextBlock( 9 );
			add( wins );

		}

		@Override
		protected void layout() {
			super.layout();

			nick.setPos(x, y);
			align(nick);

			wins.setPos(nick.right() + GAP, y);
			align(wins);
		}

		@Override
		protected void onClick() {
			// Record click
		}
	}
}
