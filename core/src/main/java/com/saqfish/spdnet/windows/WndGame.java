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

package com.saqfish.spdnet.windows;

import com.saqfish.spdnet.Dungeon;
import com.saqfish.spdnet.GamesInProgress;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.net.events.send.Send;
import com.saqfish.spdnet.net.ui.BlueButton;
import com.saqfish.spdnet.net.ui.PlayerListButton;
import com.saqfish.spdnet.scenes.GameScene;
import com.saqfish.spdnet.scenes.HeroSelectScene;
import com.saqfish.spdnet.scenes.InterlevelScene;
import com.saqfish.spdnet.scenes.RankingsScene;
import com.saqfish.spdnet.scenes.TitleScene;
import com.saqfish.spdnet.services.updates.Updates;
import com.saqfish.spdnet.ui.Icons;
import com.saqfish.spdnet.ui.RedButton;
import com.saqfish.spdnet.ui.Window;
import com.watabou.noosa.Game;

import java.io.IOException;

public class WndGame extends Window {

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	private int pos;
	
	public WndGame() {
		
		super();

		RedButton curBtn;
		addButton( curBtn = new RedButton( Messages.get(this, "settings") ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show(new WndSettings());
			}
		});
		curBtn.icon(Icons.get(Icons.PREFS));

		//install prompt
		if (Updates.isInstallable()){
			addButton( curBtn = new RedButton( Messages.get(this, "install") ) {
				@Override
				protected void onClick() {
					Updates.launchInstall();
				}
			} );
			curBtn.textColor(Window.SHPX_COLOR);
			curBtn.icon(Icons.get(Icons.CHANGES));
		}

		// Challenges window
		if (Dungeon.challenges > 0) {
			addButton( curBtn = new RedButton( Messages.get(this, "challenges") ) {
				@Override
				protected void onClick() {
					hide();
					GameScene.show( new WndChallenges( Dungeon.challenges, false ) );
				}
			} );
			curBtn.icon(Icons.get(Icons.CHALLENGE_ON));
		}

		// Restart
		if (Dungeon.hero == null || !Dungeon.hero.isAlive()) {

			addButton( curBtn = new RedButton( Messages.get(this, "start") ) {
				@Override
				protected void onClick() {
					InterlevelScene.noStory = true;
					GamesInProgress.selectedClass = Dungeon.hero.heroClass;
					GamesInProgress.curSlot = GamesInProgress.firstEmpty();
					ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
				}
			} );
			curBtn.icon(Icons.get(Icons.ENTER));
			curBtn.textColor(Window.TITLE_COLOR);
			
			addButton( curBtn = new RedButton( Messages.get(this, "rankings") ) {
				@Override
				protected void onClick() {
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					Game.switchScene( RankingsScene.class );
				}
			} );
			curBtn.icon(Icons.get(Icons.RANKINGS));
		}

		addButtons(
				// Main menu
				new RedButton( Messages.get(this, "menu") ) {
					@Override
					protected void onClick() {
						try {
							Dungeon.saveAll();
						} catch (IOException e) {
							ShatteredPixelDungeon.reportException(e);
						}
						Game.switchScene(TitleScene.class);
						ShatteredPixelDungeon.net().sendAction(Send.DESC, 0, 0, 0);
					}
				},
				// Quit
				new RedButton( Messages.get(this, "exit") ) {
					@Override
					protected void onClick() {
						try {
							Dungeon.saveAll();
						} catch (IOException e) {
							ShatteredPixelDungeon.reportException(e);
						}
						Game.instance.finish();
					}
				}
		);


		BlueButton playerListButton = new PlayerListButton();
		addButton(playerListButton);

		// Cancel
		addButton( new RedButton( Messages.get(this, "return") ) {
			@Override
			protected void onClick() {
				hide();
			}
		} );


		resize( WIDTH, pos );
	}
	
	private void addButton( RedButton btn ) {
		add( btn );
		btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}

	private void addButton( BlueButton btn ) {
		add( btn );
		btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}

	private void addButtons( RedButton btn1, RedButton btn2 ) {
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btn2 );
		btn2.setRect( btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
}
