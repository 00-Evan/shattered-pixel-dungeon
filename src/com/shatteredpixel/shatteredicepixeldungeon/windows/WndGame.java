/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.shatteredicepixeldungeon.windows;

import java.io.IOException;

import com.watabou.noosa.Game;
import com.shatteredpixel.shatteredicepixeldungeon.Dungeon;
import com.shatteredpixel.shatteredicepixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredicepixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredicepixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Window;

public class WndGame extends Window {
	
	private static final String TXT_SETTINGS	= "Settings";
    private static final String TXT_CHALLEGES	= "Challenges";
	private static final String TXT_RANKINGS	= "Rankings";
	private static final String TXT_START		= "Start New Game";
	private static final String TXT_MENU		= "Main Menu";
	private static final String TXT_EXIT		= "Exit Game";
	private static final String TXT_RETURN		= "Return to Game";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	private int pos;
	
	public WndGame() {
		
		super();
		
		addButton( new RedButton( TXT_SETTINGS ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndSettings( true ) );
			}
		} );

        // Challenges window
        if (Dungeon.challenges > 0) {
            addButton( new RedButton( TXT_CHALLEGES ) {
                @Override
                protected void onClick() {
                    hide();
                    GameScene.show( new WndChallenges( Dungeon.challenges, false ) );
                }
            } );
        }

        // Restart
        if (!Dungeon.hero.isAlive()) {
			
			RedButton btnStart;
			addButton( btnStart = new RedButton( TXT_START ) {
				@Override
				protected void onClick() {
					Dungeon.hero = null;
                    ShatteredPixelDungeon.challenges( Dungeon.challenges );
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					InterlevelScene.noStory = true;
					Game.switchScene( InterlevelScene.class );
				}
			} );
			btnStart.icon( Icons.get( Dungeon.hero.heroClass ) );
			
			addButton( new RedButton( TXT_RANKINGS ) {
				@Override
				protected void onClick() {
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					Game.switchScene( RankingsScene.class );
				}
			} );
		}

		addButtons(
				// Main menu
				new RedButton(TXT_MENU) {
					@Override
					protected void onClick() {
						try {
							Dungeon.saveAll();
						} catch (IOException e) {
							// Do nothing
						}
						Game.switchScene(TitleScene.class);
					}
				},
				// Quit
				new RedButton( TXT_EXIT ) {
					@Override
					protected void onClick() {
						Game.instance.finish();
					}
				}
		);

		// Cancel
		addButton( new RedButton( TXT_RETURN ) {
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

	private void addButtons( RedButton btn1, RedButton btn2 ) {
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btn2 );
		btn2.setRect( btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}
}
