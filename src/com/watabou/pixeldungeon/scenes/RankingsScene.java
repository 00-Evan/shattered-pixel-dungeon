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
package com.watabou.pixeldungeon.scenes;

import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Button;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.Rankings;
import com.watabou.pixeldungeon.effects.Flare;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.pixeldungeon.ui.Archs;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndError;
import com.watabou.pixeldungeon.windows.WndRanking;

public class RankingsScene extends PixelScene {
	
	private static final String TXT_TITLE		= "Top Rankings";
	private static final String TXT_TOTAL		= "Total games played: %d";
	private static final String TXT_NO_GAMES	= "No games have been played yet.";
	
	private static final String TXT_NO_INFO	= "No additional information";
	
	private static final float ROW_HEIGHT	= 30;
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
		
		if (Rankings.INSTANCE.records.size() > 0) {
			
			float left = (w - Math.min( 160, w )) / 2 + GAP;
			float top = align( (h - ROW_HEIGHT  * Rankings.INSTANCE.records.size()) / 2 );
			
			BitmapText title = PixelScene.createText( TXT_TITLE, 9 );
			title.hardlight( Window.TITLE_COLOR );
			title.measure();
			title.x = align( (w - title.width()) / 2 );
			title.y = align( top - title.height() - GAP );
			add( title );
			
			int pos = 0;
			
			for (Rankings.Record rec : Rankings.INSTANCE.records) {
				Record row = new Record( pos, pos == Rankings.INSTANCE.lastRecord, rec );
				row.setRect( left, top + pos * ROW_HEIGHT, w - left * 2, ROW_HEIGHT );
				add( row );
				
				pos++;
			}
			
			if (Rankings.INSTANCE.totalNumber >= Rankings.TABLE_SIZE) {
				BitmapText total = PixelScene.createText( Utils.format( TXT_TOTAL, Rankings.INSTANCE.totalNumber ), 8 );
				total.hardlight( Window.TITLE_COLOR );
				total.measure();
				total.x = align( (w - total.width()) / 2 );
				total.y = align( top + pos * ROW_HEIGHT + GAP );
				add( total );
			}
			
		} else {
			
			BitmapText title = PixelScene.createText( TXT_NO_GAMES, 8 );
			title.hardlight( Window.TITLE_COLOR );
			title.measure();
			title.x = align( (w - title.width()) / 2 );
			title.y = align( (h - title.height()) / 2 );
			add( title );
			
		}
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		Game.switchScene( TitleScene.class );
	}
	
	public static class Record extends Button {
		
		private static final float GAP	= 4;
		
		private static final int TEXT_WIN	= 0xFFFF88;
		private static final int TEXT_LOSE	= 0xCCCCCC;
		private static final int FLARE_WIN	= 0x888866;
		private static final int FLARE_LOSE	= 0x666666;
		
		private Rankings.Record rec;
		
		private ItemSprite shield;
		private Flare flare;
		private BitmapText position;
		private BitmapTextMultiline desc;
		private Image classIcon;
		
		public Record( int pos, boolean latest, Rankings.Record rec ) {
			super();
			
			this.rec = rec;
			
			if (latest) {
				flare = new Flare( 6, 24 );
				flare.angularSpeed = 90;
				flare.color( rec.win ? FLARE_WIN : FLARE_LOSE );
				addToBack( flare );
			}
			
			position.text( Integer.toString( pos+1 ) );
			position.measure();
			
			desc.text( rec.info );
			desc.measure();
			
			if (rec.win) {
				shield.view( ItemSpriteSheet.AMULET, null );
				position.hardlight( TEXT_WIN );
				desc.hardlight( TEXT_WIN );
			} else {
				position.hardlight( TEXT_LOSE );
				desc.hardlight( TEXT_LOSE );
			}
			
			classIcon.copy( Icons.get( rec.heroClass ) );
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			shield = new ItemSprite( ItemSpriteSheet.TOMB, null );
			add( shield );
			
			position = new BitmapText( PixelScene.font1x );
			add( position );
			
			desc = createMultiline( 9 );		
			add( desc );
			
			classIcon = new Image();
			add( classIcon );
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
			
			classIcon.x = align( x + width - classIcon.width );
			classIcon.y = shield.y;
			
			desc.x = shield.x + shield.width + GAP;
			desc.maxWidth = (int)(classIcon.x - desc.x);
			desc.measure();
			desc.y = position.y + position.baseLine() - desc.baseLine();
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
