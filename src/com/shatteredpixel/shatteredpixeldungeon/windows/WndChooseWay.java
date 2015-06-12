/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
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
package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.TomeOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.Utils;

public class WndChooseWay extends Window {
	
	private static final String TXT_MESSAGE	= "Which way will you follow?";
	private static final String TXT_CANCEL	= "I'll decide later";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndChooseWay( final TomeOfMastery tome, final HeroSubClass way1, final HeroSubClass way2 ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		Highlighter hl = new Highlighter( way1.desc() + "\n\n" + way2.desc() + "\n\n" + TXT_MESSAGE );
		
		BitmapTextMultiline normal = PixelScene.createMultiline( hl.text, 6 );
		normal.maxWidth = WIDTH;
		normal.measure();
		normal.x = titlebar.left();
		normal.y = titlebar.bottom() + GAP;
		add( normal );
		
		if (hl.isHighlighted()) {
			normal.mask = hl.inverted();
			
			BitmapTextMultiline highlighted = PixelScene.createMultiline( hl.text, 6 );
			highlighted.maxWidth = normal.maxWidth;
			highlighted.measure();
			highlighted.x = normal.x;
			highlighted.y = normal.y;
			add( highlighted );
	
			highlighted.mask = hl.mask;
			highlighted.hardlight( TITLE_COLOR );
		}
		
		RedButton btnWay1 = new RedButton( Utils.capitalize( way1.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way1 );
			}
		};
		btnWay1.setRect( 0, normal.y + normal.height() + GAP, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btnWay1 );
		
		RedButton btnWay2 = new RedButton( Utils.capitalize( way2.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way2 );
			}
		};
		btnWay2.setRect( btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT );
		add( btnWay2 );
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay2.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
}
