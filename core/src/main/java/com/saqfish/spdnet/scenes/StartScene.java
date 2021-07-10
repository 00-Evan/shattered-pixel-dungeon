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

package com.saqfish.spdnet.scenes;

import com.saqfish.spdnet.Badges;
import com.saqfish.spdnet.Chrome;
import com.saqfish.spdnet.GamesInProgress;
import com.saqfish.spdnet.ShatteredPixelDungeon;
import com.saqfish.spdnet.actors.hero.HeroSubClass;
import com.saqfish.spdnet.journal.Journal;
import com.saqfish.spdnet.messages.Messages;
import com.saqfish.spdnet.net.ui.NetIcons;
import com.saqfish.spdnet.net.windows.NetWindow;
import com.saqfish.spdnet.net.windows.WndNetOptions;
import com.saqfish.spdnet.ui.Archs;
import com.saqfish.spdnet.ui.ExitButton;
import com.saqfish.spdnet.ui.Icons;
import com.saqfish.spdnet.ui.RenderedTextBlock;
import com.saqfish.spdnet.ui.Window;
import com.saqfish.spdnet.windows.WndGameInProgress;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Bundle;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;

public class StartScene extends PixelScene {
	
	private static final int SLOT_WIDTH = 120;
	private static final int SLOT_HEIGHT = 30;
	
	@Override
	public void create() {
		super.create();
		
		Badges.loadGlobal();
		Journal.loadGlobal();
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( w - btnExit.width(), 0 );
		add( btnExit );
		
		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);
		
		ArrayList<GamesInProgress.Info> games = GamesInProgress.checkAll();
		
		int slotGap = landscape() ? 5 : 10;
		int slotCount = Math.min(GamesInProgress.MAX_SLOTS, games.size()+1);
		int slotsHeight = slotCount*SLOT_HEIGHT + (slotCount-1)* slotGap;
		
		float yPos = (h - slotsHeight)/2f;
		if (landscape()) yPos += 8;

		for (GamesInProgress.Info game : games) {
			SaveSlotButton existingGame = new SaveSlotButton(true);
			existingGame.set(game.slot);
			existingGame.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
			yPos += SLOT_HEIGHT + slotGap;
			align(existingGame);
			add(existingGame);
		}
		
		if (games.size() < GamesInProgress.MAX_SLOTS){
			SaveSlotButton newGame = new SaveSlotButton(false);
			newGame.set(GamesInProgress.firstEmpty());
			newGame.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
			yPos += SLOT_HEIGHT + slotGap;
			align(newGame);
			add(newGame);
		}
		
		GamesInProgress.curSlot = 0;
		
		fadeIn();
		
	}
	
	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade( TitleScene.class );
	}
	
	private static class SaveSlotButton extends Button {
		
		private NinePatch bg;
		
		private Image hero;
		private RenderedTextBlock name;
		
		private Image steps;
		private BitmapText depth;
		private Image classIcon;
		private BitmapText level;

		private boolean newButtonSlot;
		private long seed;
		private int slot;
		private boolean newGame;
		private boolean eligible;


		private SaveSlotButton(boolean newButtonSlot){
			this.newButtonSlot = newButtonSlot;
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			
			bg = Chrome.get(Chrome.Type.GEM);
			add( bg);


			name = PixelScene.renderTextBlock(9);
			add(name);
		}
		
		public void set( int slot ){
			this.slot = slot;
			GamesInProgress.Info info = GamesInProgress.check(slot);

			try {
				Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.gameFile(slot));
				seed = bundle.getLong("seed");
				eligible = ShatteredPixelDungeon.net().connected() &&
						ShatteredPixelDungeon.net().seed() == seed &&
						seed != 0;
			} catch (IOException e) {
				// e.printStackTrace();
			}

			newGame = info == null;
			if (newGame){
				name.text( Messages.get(StartScene.class, "new"));
				
				if (hero != null){
					remove(hero);
					hero = null;
					remove(steps);
					steps = null;
					remove(depth);
					depth = null;
					remove(classIcon);
					classIcon = null;
					remove(level);
					level = null;
				}
			} else {
				
				if (info.subClass != HeroSubClass.NONE){
					name.text(Messages.titleCase(info.subClass.title()));
				} else {
					name.text(Messages.titleCase(info.heroClass.title()));
				}
				
				if (hero == null){
					hero = new Image(info.heroClass.spritesheet(), 0, 15*info.armorTier, 12, 15);
					add(hero);
					
					steps = new Image(Icons.get(Icons.DEPTH));
					add(steps);
					depth = new BitmapText(PixelScene.pixelFont);
					add(depth);
					
					classIcon = new Image(Icons.get(info.heroClass));
					add(classIcon);
					level = new BitmapText(PixelScene.pixelFont);
					add(level);
				} else {
					hero.copy(new Image(info.heroClass.spritesheet(), 0, 15*info.armorTier, 12, 15));
					
					classIcon.copy(Icons.get(info.heroClass));
				}
				
				depth.text(Integer.toString(info.depth));
				depth.measure();
				
				level.text(Integer.toString(info.level));
				level.measure();
				
				if (info.challenges > 0){
					name.hardlight(Window.TITLE_COLOR);
					depth.hardlight(Window.TITLE_COLOR);
					level.hardlight(Window.TITLE_COLOR);
				} else {
					name.resetColor();
					depth.resetColor();
					level.resetColor();
				}
				
			}

			if(newButtonSlot) bg.hardlight(eligible ? 0x00FF00: 0xff0000);

			layout();
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			bg.x = x;
			bg.y = y;
			bg.size( width, height );

			if (hero != null){
				hero.x = x+8;
				hero.y = y + (height - hero.height())/2f;
				align(hero);
				
				name.setPos(
						hero.x + hero.width() + 6,
						y + (height - name.height())/2f
				);
				align(name);
				
				classIcon.x = x + width - 24 + (16 - classIcon.width())/2f;
				classIcon.y = y + (height - classIcon.height())/2f;
				align(classIcon);
				
				level.x = classIcon.x + (classIcon.width() - level.width()) / 2f;
				level.y = classIcon.y + (classIcon.height() - level.height()) / 2f + 1;
				align(level);
				
				steps.x = x + width - 40 + (16 - steps.width())/2f;
				steps.y = y + (height - steps.height())/2f;
				align(steps);
				
				depth.x = steps.x + (steps.width() - depth.width()) / 2f;
				depth.y = steps.y + (steps.height() - depth.height()) / 2f + 1;
				align(depth);
				
			} else {
				name.setPos(
						x + (width - name.width())/2f,
						y + (height - name.height())/2f
				);
				align(name);
			}
			
			
		}
		
		@Override
		protected void onClick() {
			if (newGame) {
				GamesInProgress.selectedClass = null;
				GamesInProgress.curSlot = slot;
				ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
			} else {
				if(eligible){
					ShatteredPixelDungeon.scene().add( new WndGameInProgress(slot));
				}else{
					if(!ShatteredPixelDungeon.net().connected()) NetWindow.error("Not connected", "You must connect before loading save");
					else NetWindow.runWindow(new WndNetOptions(NetIcons.get(NetIcons.ALERT), "Seed Mismatch","Save seed: "+seed+"\nServer seed: " +ShatteredPixelDungeon.net().seed(), "Delete"){
						@Override
						protected void onSelect(int index) {
							super.onSelect(index);
							if (index ==0){
								FileUtils.deleteDir(GamesInProgress.gameFolder(slot));
								GamesInProgress.setUnknown(slot);
								ShatteredPixelDungeon.switchNoFade(StartScene.class);
							}
						}
					});
				}
			}
		}
	}
}
