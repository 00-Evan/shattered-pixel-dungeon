/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.LanguageButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.PrefsButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.DeviceCompat;

public class TitleScene extends PixelScene {
	
	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.play( Assets.THEME, true );

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(title.height, h*0.45f);

		title.x = (w - title.width()) / 2f;
		if (SPDSettings.landscape()) {
			title.y = (topRegion - title.height()) / 2f;
		} else {
			title.y = 20 + (topRegion - title.height() - 20) / 2f;
		}

		align(title);

		placeTorch(title.x + 22, title.y + 46);
		placeTorch(title.x + title.width - 22, title.y + 46);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );
		
		TitleButton btnPlay = new TitleButton(Messages.get(this, "enter")){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().size() == 0){
					TitleScene.this.add( new WndStartGame(1) );
				} else {
					ShatteredPixelDungeon.switchNoFade( StartScene.class );
				}
			}
			
			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (DeviceCompat.isDebug()) {
					TitleScene.this.add( new WndStartGame(1) );
					return true;
				}
				return super.onLongClick();
			}
		};
		btnPlay.icon(Icons.get(Icons.ENTER));
		add(btnPlay);
		
		TitleButton btnSupport = new TitleButton(Messages.get(this, "support")){
			@Override
			protected void onClick() {
				WndOptions wnd = new WndOptions(Messages.get(TitleScene.class, "support"),
						Messages.get(TitleScene.class, "patreon_body"),
						Messages.get(TitleScene.class, "patreon_button")){
					@Override
					protected void onSelect(int index) {
						if (index == 0){
							DeviceCompat.openURI("https://www.patreon.com/ShatteredPixel");
						} else {
							hide();
						}
					}
				};
				parent.add(wnd);
			}
		};
		btnSupport.icon(Icons.get(Icons.GOLD));
		add(btnSupport);
		
		TitleButton btnRankings = new TitleButton(Messages.get(this, "rankings")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(Icons.get(Icons.RANKINGS));
		add(btnRankings);
		
		TitleButton btnBadges = new TitleButton(Messages.get(this, "badges")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		btnBadges.icon(Icons.get(Icons.BADGES));
		add(btnBadges);
		
		TitleButton btnChanges = new TitleButton(Messages.get(this, "changes")){
			@Override
			protected void onClick() {
				ChangesScene.changesSelected = 0;
				ShatteredPixelDungeon.switchNoFade( ChangesScene.class );
			}
		};
		btnChanges.icon(Icons.get(Icons.CHANGES));
		add(btnChanges);
		
		TitleButton btnAbout = new TitleButton(Messages.get(this, "about")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( AboutScene.class );
			}
		};
		btnAbout.icon(Icons.get(Icons.SHPX));
		add(btnAbout);
		
		final int BTN_HEIGHT = 21;
		int GAP = (int)(h - topRegion - (SPDSettings.landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= SPDSettings.landscape() ? 3 : 4;
		GAP = Math.max(GAP, 2);

		if (SPDSettings.landscape()) {
			btnPlay.setRect(title.x-50, topRegion+GAP, ((title.width()+100)/2)-1, BTN_HEIGHT);
			align(btnPlay);
			btnSupport.setRect(btnPlay.right()+2, btnPlay.top(), btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left() + (btnPlay.width()*.33f)+1, btnPlay.bottom()+ GAP, (btnPlay.width()*.67f)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnRankings.left(), btnRankings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnChanges.right()+2, btnChanges.top(), btnRankings.width(), BTN_HEIGHT);
		} else {
			btnPlay.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnPlay);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnChanges.right()+2, btnChanges.top(), btnChanges.width(), BTN_HEIGHT);
			btnSupport.setRect(btnPlay.left(), btnAbout.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );
		
		int pos = 2;
		
		PrefsButton btnPrefs = new PrefsButton();
		btnPrefs.setRect( pos, 0, 16, 20 );
		add( btnPrefs );
		
		pos += btnPrefs.width();

		LanguageButton btnLang = new LanguageButton();
		btnLang.setRect(pos, 0, 16, 20);
		add( btnLang );

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}
	
	private static class TitleButton extends StyledButton {
		
		public TitleButton( String label ){
			this(label, 9);
		}
		
		public TitleButton( String label, int size ){
			super(Chrome.Type.GREY_BUTTON_TR, label, size);
		}
		
	}
}
