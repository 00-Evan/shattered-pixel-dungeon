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
package com.shatteredpixel.shatteredicepixeldungeon.scenes;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Button;
import com.shatteredpixel.shatteredicepixeldungeon.Assets;
import com.shatteredpixel.shatteredicepixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredicepixeldungeon.Rankings;
import com.shatteredpixel.shatteredicepixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredicepixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredicepixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredicepixeldungeon.ui.Window;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndError;
import com.shatteredpixel.shatteredicepixeldungeon.windows.WndRanking;
import com.watabou.utils.GameMath;

public class RankingsScene extends PixelScene {
	
	private static final String TXT_TITLE		= "Top Rankings";
	private static final String TXT_TOTAL		= "Games Played: ";
	private static final String TXT_NO_GAMES	= "No games have been played yet.";
	
	private static final String TXT_NO_INFO	= "No additional information";
	
	private static final float ROW_HEIGHT_MAX	= 20;
	private static final float ROW_HEIGHT_MIN	= 12;

	private static final float MAX_ROW_WIDTH    = 160;

	private static final float GAP	= 4;
	
	private Archs archs;

	@Override
	public void create() {
		
		super.create();
		
		Music.INSTANCE.play( Assets.THEME, true );
		Music.INSTANCE.volume( 1f );
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		archs = new Archs();
		archs.setSize( w, h );
		add( archs );
		
		Rankings.INSTANCE.load();

		BitmapText title = PixelScene.createText(TXT_TITLE, 9);
		title.hardlight(Window.SHPX_COLOR);
		title.measure();
		title.x = align((w - title.width()) / 2);
		title.y = align( GAP );
		add(title);
		
		if (Rankings.INSTANCE.records.size() > 0) {

			//attempts to give each record as much space as possible, ideally as much space as portrait mode
			float rowHeight = GameMath.gate(ROW_HEIGHT_MIN, (uiCamera.height - 26)/Rankings.INSTANCE.records.size(), ROW_HEIGHT_MAX);

			float left = (w - Math.min( MAX_ROW_WIDTH, w )) / 2 + GAP;
			float top = align( (h - rowHeight  * Rankings.INSTANCE.records.size()) / 2 );
			
			int pos = 0;
			
			for (Rankings.Record rec : Rankings.INSTANCE.records) {
				Record row = new Record( pos, pos == Rankings.INSTANCE.lastRecord, rec );
				float offset =
						rowHeight <= 14 ?
								pos %2 == 1?
										5 :
										-5
								: 0;
				row.setRect( left+offset, top + pos * rowHeight, w - left * 2, rowHeight );
				add(row);
				
				pos++;
			}
			
			if (Rankings.INSTANCE.totalNumber >= Rankings.TABLE_SIZE) {
                BitmapText label = PixelScene.createText( TXT_TOTAL, 8 );
                label.hardlight( 0xCCCCCC );
                label.measure();
                add( label );

                BitmapText won = PixelScene.createText( Integer.toString( Rankings.INSTANCE.wonNumber ), 8 );
                won.hardlight( Window.SHPX_COLOR );
                won.measure();
                add( won );

                BitmapText total = PixelScene.createText( "/" + Rankings.INSTANCE.totalNumber, 8 );
                total.hardlight( 0xCCCCCC );
                total.measure();
                total.x = align( (w - total.width()) / 2 );
                total.y = align( top + pos * rowHeight + GAP );
                add( total );

                float tw = label.width() + won.width() + total.width();
                label.x = align( (w - tw) / 2 );
                won.x = label.x + label.width();
                total.x = won.x + won.width();
                label.y = won.y = total.y = align( h - label.height() - GAP );

            }
			
		} else {

			BitmapText noRec = PixelScene.createText(TXT_NO_GAMES, 8);
			noRec.hardlight( 0xCCCCCC );
			noRec.measure();
			noRec.x = align((w - noRec.width()) / 2);
			noRec.y = align((h - noRec.height()) / 2);
			add(noRec);
			
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
		
		private static final int[] TEXT_WIN	= {0xFFFF88, 0xB2B25F};
		private static final int[] TEXT_LOSE= {0xDDDDDD, 0x888888};
		private static final int FLARE_WIN	= 0x888866;
		private static final int FLARE_LOSE	= 0x666666;
		
		private Rankings.Record rec;
		
		protected ItemSprite shield;
		private Flare flare;
		private BitmapText position;
		private BitmapTextMultiline desc;
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
			
			desc.text( rec.info );

			desc.measure();

			int odd = pos % 2;
			
			if (rec.win) {
				shield.view( ItemSpriteSheet.AMULET, null );
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
					steps.copy(Icons.DEPTH_LG.get());

					add(steps);
					add(depth);
				}

			}

			if (rec.herolevel != 0){
				level.text( Integer.toString(rec.herolevel) );
				level.measure();
				add(level);
			}
			
			classIcon.copy( Icons.get( rec.heroClass ) );
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			shield = new ItemSprite( ItemSpriteSheet.TOMB, null );
			add( shield );
			
			position = new BitmapText( PixelScene.font1x );
			position.alpha(0.8f);
			add( position );
			
			desc = createMultiline( 7 );
			add( desc );

			depth = new BitmapText( PixelScene.font1x );
			depth.alpha(0.8f);

			steps = new Image();
			
			classIcon = new Image();
			add( classIcon );

			level = new BitmapText( PixelScene.font1x );
			level.alpha(0.8f);
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			shield.x = x;
			shield.y = y + (height - shield.height) / 2;
			
			position.x = align( shield.x + (shield.width - position.width()) / 2 );
			position.y = align( shield.y + (shield.height - position.height()) / 2 + 1 );
			
			if (flare != null) {
				flare.point( shield.center() );
			}

			classIcon.x = align(x + width - classIcon.width);
			classIcon.y = shield.y;

			level.x = align( classIcon.x + (classIcon.width - level.width()) / 2 );
			level.y = align( classIcon.y + (classIcon.height - level.height()) / 2 + 1 );

			steps.x = align(x + width - steps.width - classIcon.width);
			steps.y = shield.y;

			depth.x = align( steps.x + (steps.width - depth.width()) / 2 );
			depth.y = align( steps.y + (steps.height - depth.height()) / 2 + 1 );

			desc.x = shield.x + shield.width + GAP;
			desc.maxWidth = (int)(steps.x - desc.x);
			desc.measure();
			desc.y = align( shield.y + (shield.height - desc.height()) / 2 + 1 );
		}
		
		@Override
		protected void onClick() {
			if (rec.gameFile.length() > 0) {
				parent.add( new WndRanking( rec.gameFile ) );
			} else {
				parent.add( new WndError( TXT_NO_INFO ) );
			}
		}
	}
}
