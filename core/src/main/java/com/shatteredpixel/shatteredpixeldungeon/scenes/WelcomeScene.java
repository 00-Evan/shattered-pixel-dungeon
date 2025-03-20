/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.journal.Document;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndHardNotification;
import com.watabou.glwrap.Blending;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.FileUtils;

import java.util.Collections;

public class WelcomeScene extends PixelScene {

	private static final int LATEST_UPDATE = ShatteredPixelDungeon.v3_0_0;

	//used so that the game does not keep showing the window forever if cleaning fails
	private static boolean triedCleaningTemp = false;

	@Override
	public void create() {
		super.create();

		final int previousVersion = SPDSettings.version();

		if (!triedCleaningTemp && FileUtils.cleanTempFiles()){
			add(new WndHardNotification(Icons.get(Icons.WARNING),
					Messages.get(WndError.class, "title"),
					Messages.get(this, "save_warning"),
					Messages.get(this, "continue"),
					5){
				@Override
				public void hide() {
					super.hide();
					triedCleaningTemp = true;
					ShatteredPixelDungeon.resetScene();
				}
			});
			return;
		}

		if (ShatteredPixelDungeon.versionCode == previousVersion && !SPDSettings.intro()) {
			ShatteredPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
				new float[]{1, 1},
				false);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		//darkens the arches
		add(new ColorBlock(w, h, 0x88000000));

		Image title = BannerSprites.get( landscape() ? BannerSprites.Type.TITLE_LAND : BannerSprites.Type.TITLE_PORT);
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		if (landscape()){
			placeTorch(title.x + 30, title.y + 35);
			placeTorch(title.x + title.width - 30, title.y + 35);
		} else {
			placeTorch(title.x + 16, title.y + 70);
			placeTorch(title.x + title.width - 16, title.y + 70);
		}

		Image signs = new Image(BannerSprites.get( landscape() ? BannerSprites.Type.TITLE_GLOW_LAND : BannerSprites.Type.TITLE_GLOW_PORT)){
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
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0 || SPDSettings.intro()){

					if (previousVersion > 0){
						updateVersion(previousVersion);
					}

					SPDSettings.version(ShatteredPixelDungeon.versionCode);
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = GamesInProgress.firstEmpty();
					if (GamesInProgress.curSlot == -1 || Rankings.INSTANCE.totalNumber > 0){
						SPDSettings.intro(false);
						ShatteredPixelDungeon.switchScene(TitleScene.class);
					} else {
						ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
					}
				} else {
					updateVersion(previousVersion);
					ShatteredPixelDungeon.switchScene(TitleScene.class);
				}
			}
		};

		float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

		float buttonAreaWidth = landscape() ? PixelScene.MIN_WIDTH_L-6 : PixelScene.MIN_WIDTH_P-2;
		float btnAreaLeft = (Camera.main.width - buttonAreaWidth) / 2f;
		if (previousVersion != 0 && !SPDSettings.intro()){
			StyledButton changes = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(TitleScene.class, "changes")){
				@Override
				protected void onClick() {
					super.onClick();
					updateVersion(previousVersion);
					ShatteredPixelDungeon.switchScene(ChangesScene.class);
				}
			};
			okay.setRect(btnAreaLeft, buttonY, (buttonAreaWidth/2)-1, 20);
			add(okay);

			changes.setRect(okay.right()+1, buttonY, okay.width(), 20);
			changes.icon(Icons.get(Icons.CHANGES));
			add(changes);
		} else {
			okay.text(Messages.get(TitleScene.class, "enter"));
			okay.setRect(btnAreaLeft, buttonY, buttonAreaWidth, 20);
			okay.icon(Icons.get(Icons.ENTER));
			add(okay);
		}

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if (previousVersion == 0 || SPDSettings.intro()) {
			message = Document.INTROS.pageBody(0);
		} else if (previousVersion <= ShatteredPixelDungeon.versionCode) {
			if (previousVersion < LATEST_UPDATE){
				message = Messages.get(this, "update_intro");
				message += "\n\n" + Messages.get(this, "update_msg");
			} else {
				//TODO: change the messages here in accordance with the type of patch.
				message = Messages.get(this, "patch_intro");
				message += "\n";
				//message += "\n" + Messages.get(this, "patch_balance");
				message += "\n" + Messages.get(this, "patch_bugfixes");
				message += "\n" + Messages.get(this, "patch_translations");

			}

		} else {
			message = Messages.get(this, "what_msg");
		}

		text.text(message, Math.min(w-20, 300));
		float titleBottom = title.y + title.height();
		float textSpace = okay.top() - titleBottom - 4;
		text.setPos((w - text.width()) / 2f, (titleBottom + 2) + (textSpace - text.height())/2);
		add(text);

		if (SPDSettings.intro() && ControllerHandler.isControllerConnected()){
			addToFront(new WndHardNotification(Icons.CONTROLLER.get(),
					Messages.get(WelcomeScene.class, "controller_title"),
					Messages.get(WelcomeScene.class, "controller_body"),
					Messages.get(WelcomeScene.class, "controller_okay"),
					0){
				@Override
				public void onBackPressed() {
					//do nothing, must press the okay button
				}
			});
		}
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.x = x - fb.width()/2f;
		fb.y = y - fb.height();

		align(fb);
		add( fb );
	}

	private void updateVersion(int previousVersion){

		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){

			Badges.loadGlobal();
			Journal.loadGlobal();

			//pre-unlock Cleric for those who already have a win
			if (previousVersion <= ShatteredPixelDungeon.v2_5_4){
				if (Badges.isUnlocked(Badges.Badge.VICTORY) && !Badges.isUnlocked(Badges.Badge.UNLOCK_CLERIC)){
					Badges.unlock(Badges.Badge.UNLOCK_CLERIC);
				}
			}

			if (previousVersion <= ShatteredPixelDungeon.v2_4_2){
				//Dwarf King's final journal entry changed, set it as un-read
				if (Document.HALLS_KING.isPageRead(Document.KING_ATTRITION)){
					Document.HALLS_KING.unreadPage(Document.KING_ATTRITION);
				}
			}

			try {
				Rankings.INSTANCE.load();
				for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])){
					try {
						Rankings.INSTANCE.loadGameData(rec);
						Rankings.INSTANCE.saveGameData(rec);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record's data
						rec.gameData = null;
						Game.reportException( new RuntimeException("Rankings Updating Failed!",e));
					}
				}
				if (Rankings.INSTANCE.latestDaily != null){
					try {
						Rankings.INSTANCE.loadGameData(Rankings.INSTANCE.latestDaily);
						Rankings.INSTANCE.saveGameData(Rankings.INSTANCE.latestDaily);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record's data
						Rankings.INSTANCE.latestDaily.gameData = null;
						Game.reportException( new RuntimeException("Rankings Updating Failed!",e));
					}
				}
				Collections.sort(Rankings.INSTANCE.records, Rankings.scoreComparator);
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
				Game.reportException( new RuntimeException("Rankings Updating Failed!",e));
			}
			Dungeon.daily = Dungeon.dailyReplay = false;

			if (previousVersion <= ShatteredPixelDungeon.v2_3_2){
				Document.ADVENTURERS_GUIDE.findPage(Document.GUIDE_ALCHEMY);
			}

			Badges.saveGlobal(true);
			Journal.saveGlobal(true);

		}

		SPDSettings.version(ShatteredPixelDungeon.versionCode);
	}
	
}
