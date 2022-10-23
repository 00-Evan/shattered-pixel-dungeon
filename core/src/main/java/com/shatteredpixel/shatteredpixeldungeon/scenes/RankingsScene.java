/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDailies;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRanking;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.shatteredpixel.shatteredpixeldungeon.ui.Button;
import com.watabou.utils.GameMath;

public class RankingsScene extends PixelScene {
	
	private static final float ROW_HEIGHT_MAX	= 20;
	private static final float ROW_HEIGHT_MIN	= 12;

	private static final float MAX_ROW_WIDTH    = 160;

	private static final float GAP	= 4;
	
	private Archs archs;

	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
				new float[]{1, 1},
				false);

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Rankings.INSTANCE.load();

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);
		
		if (Rankings.INSTANCE.records.size() > 0) {

			//attempts to give each record as much space as possible, ideally as much space as portrait mode
			float rowHeight = GameMath.gate(ROW_HEIGHT_MIN, (uiCamera.height - 26)/Rankings.INSTANCE.records.size(), ROW_HEIGHT_MAX);

			float left = (w - Math.min( MAX_ROW_WIDTH, w )) / 2 + GAP;
			float top = (h - rowHeight  * Rankings.INSTANCE.records.size()) / 2;
			
			int pos = 0;
			
			for (Rankings.Record rec : Rankings.INSTANCE.records) {
				Record row = new Record( pos, pos == Rankings.INSTANCE.lastRecord, rec );
				float offset = 0;
				if (rowHeight <= 14){
					offset = (pos % 2 == 1) ? 5 : -5;
				}
				row.setRect( left+offset, top + pos * rowHeight, w - left * 2, rowHeight );
				add(row);
				
				pos++;
			}
			
			if (Rankings.INSTANCE.totalNumber >= Rankings.TABLE_SIZE) {
				
				RenderedTextBlock label = PixelScene.renderTextBlock( 8 );
				label.hardlight( 0xCCCCCC );
				label.setHightlighting(true, Window.SHPX_COLOR);
				label.text( Messages.get(this, "total") + " _" + Rankings.INSTANCE.wonNumber + "_/" + Rankings.INSTANCE.totalNumber );
				add( label );
				
				label.setPos(
						(w - label.width()) / 2,
						h - label.height() - 2*GAP
				);
				align(label);

			}
			
		} else {

			RenderedTextBlock noRec = PixelScene.renderTextBlock(Messages.get(this, "no_games"), 8);
			noRec.hardlight( 0xCCCCCC );
			noRec.setPos(
					(w - noRec.width()) / 2,
					(h - noRec.height()) / 2
			);
			align(noRec);
			add(noRec);
			
		}

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		int left = 0;

		if (Rankings.INSTANCE.latestDaily != null) {
			IconButton btnDailies = new IconButton(Icons.CALENDAR.get()) {
				@Override
				protected void onClick() {
					ShatteredPixelDungeon.scene().addToFront(new WndDailies());
				}

				@Override
				protected void onPointerUp() {
					icon.hardlight(0.5f, 1f, 2f);
				}
			};
			btnDailies.icon().hardlight(0.5f, 1f, 2f);
			btnDailies.setRect( left, 0, 20, 20 );
			left += 20;
			add(btnDailies);
		}

		if (Dungeon.daily){
			addToFront(new WndDailies());
		}

		fadeIn();
	}

	@Override
	public void destroy() {
		super.destroy();
		//so that opening daily records does not trigger WndDailies opening on future visits
		Dungeon.daily = Dungeon.dailyReplay = false;
	}

	@Override
	protected void onBackPressed() {
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}
	
	public static class Record extends Button {
		
		private static final float GAP	= 4;
		
		private static final int[] TEXT_WIN	= {0xFFFF88, 0xB2B25F};
		private static final int[] TEXT_LOSE= {0xDDDDDD, 0x888888};
		private static final int FLARE_WIN	= 0x888866;
		private static final int FLARE_LOSE	= 0x666666;
		
		private Rankings.Record rec;
		
		protected Image shield;
		private Flare flare;
		private BitmapText position;
		private RenderedTextBlock desc;
		private Image steps;
		private BitmapText depth;
		private Image classIcon;
		private BitmapText level;
		
		public Record( int pos, boolean latest, Rankings.Record rec ) {
			super();
			
			this.rec = rec;
			
			if (latest) {
				flare = new Flare( 6, 24 );
				flare.angularSpeed = 90;
				flare.color( rec.win ? FLARE_WIN : FLARE_LOSE );
				addToBack( flare );
			}

			if (pos != Rankings.TABLE_SIZE-1) {
				position.text(Integer.toString(pos + 1));
			} else
				position.text(" ");
			position.measure();
			
			desc.text( Messages.titleCase(rec.desc()) );

			int odd = pos % 2;
			
			if (rec.win) {
				shield.copy( new ItemSprite(ItemSpriteSheet.AMULET, null) );
				position.hardlight( TEXT_WIN[odd] );
				desc.hardlight( TEXT_WIN[odd] );
				depth.hardlight( TEXT_WIN[odd] );
				level.hardlight( TEXT_WIN[odd] );
			} else {
				position.hardlight( TEXT_LOSE[odd] );
				desc.hardlight( TEXT_LOSE[odd] );
				depth.hardlight( TEXT_LOSE[odd] );
				level.hardlight( TEXT_LOSE[odd] );

				if (rec.depth != 0){
					depth.text( Integer.toString(rec.depth) );
					depth.measure();
					steps.copy(Icons.STAIRS.get());

					add(steps);
					add(depth);
				}

				if (rec.ascending){
					shield.copy( new ItemSprite(ItemSpriteSheet.AMULET, null) );
					shield.hardlight(0.4f, 0.4f, 0.7f);
				}

			}

			if (rec.daily){
				shield.copy( Icons.get(Icons.CALENDAR) );
				shield.hardlight(0.5f, 1f, 2f);
			} else if (!rec.customSeed.isEmpty()){
				shield.copy( Icons.get(Icons.SEED) );
				shield.hardlight(1f, 1.5f, 0.67f);
			}

			if (rec.herolevel != 0){
				level.text( Integer.toString(rec.herolevel) );
				level.measure();
				add(level);
			}
			
			classIcon.copy( Icons.get( rec.heroClass ) );
			if (rec.heroClass == HeroClass.ROGUE){
				//cloak of shadows needs to be brightened a bit
				classIcon.brightness(2f);
			}
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			shield = new Image(new ItemSprite( ItemSpriteSheet.TOMB, null ));
			add( shield );
			
			position = new BitmapText( PixelScene.pixelFont);
			add( position );
			
			desc = renderTextBlock( 7 );
			add( desc );

			depth = new BitmapText( PixelScene.pixelFont);

			steps = new Image();
			
			classIcon = new Image();
			add( classIcon );

			level = new BitmapText( PixelScene.pixelFont);
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			shield.x = x + (16 - shield.width) / 2f;
			shield.y = y + (height - shield.height) / 2f;
			align(shield);
			
			position.x = shield.x + (shield.width - position.width()) / 2f;
			position.y = shield.y + (shield.height - position.height()) / 2f + 1;
			align(position);
			
			if (flare != null) {
				flare.point( shield.center() );
			}

			classIcon.x = x + width - 16 + (16 - classIcon.width())/2f;
			classIcon.y = shield.y + (16 - classIcon.height())/2f;
			align(classIcon);

			level.x = classIcon.x + (classIcon.width - level.width()) / 2f;
			level.y = classIcon.y + (classIcon.height - level.height()) / 2f + 1;
			align(level);

			steps.x = x + width - 32 + (16 - steps.width())/2f;
			steps.y = shield.y + (16 - steps.height())/2f;
			align(steps);

			depth.x = steps.x + (steps.width - depth.width()) / 2f;
			depth.y = steps.y + (steps.height - depth.height()) / 2f + 1;
			align(depth);

			desc.maxWidth((int)(steps.x - (x + 16 + GAP)));
			desc.setPos(x + 16 + GAP, shield.y + (shield.height - desc.height()) / 2f + 1);
			align(desc);
		}
		
		@Override
		protected void onClick() {
			if (rec.gameData != null) {
				parent.add( new WndRanking( rec ) );
			} else {
				parent.add( new WndError( Messages.get(RankingsScene.class, "no_info") ) );
			}
		}
	}
}
