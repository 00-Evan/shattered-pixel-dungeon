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

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndGameInProgress;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Button;

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
			SaveSlotButton existingGame = new SaveSlotButton();
			existingGame.set(game.slot);
			existingGame.setRect((w - SLOT_WIDTH) / 2f, yPos, SLOT_WIDTH, SLOT_HEIGHT);
			yPos += SLOT_HEIGHT + slotGap;
			align(existingGame);
			add(existingGame);
			
		}
		
		if (games.size() < GamesInProgress.MAX_SLOTS){
			SaveSlotButton newGame = new SaveSlotButton();
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
		
		private int slot;
		private boolean newGame;
		
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
				ShatteredPixelDungeon.scene().add( new WndGameInProgress(slot));
			}
		}
	}
}
