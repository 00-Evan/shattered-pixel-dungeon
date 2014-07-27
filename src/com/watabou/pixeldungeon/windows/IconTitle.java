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
package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Window;

public class IconTitle extends Component {

	private static final float FONT_SIZE = 9;
	
	private static final float GAP = 2;
	
	protected Image imIcon;
	protected BitmapTextMultiline tfLabel;
	
	public IconTitle() {
		super();
	}
	
	public IconTitle( Image icon, String label ) {
		super();
		
		icon( icon );
		label( label );
	}
	
	@Override
	protected void createChildren() {
		imIcon = new Image();
		add( imIcon );
		
		tfLabel = PixelScene.createMultiline( FONT_SIZE );
		tfLabel.hardlight( Window.TITLE_COLOR );
		add( tfLabel );
	}
	
	@Override
	protected void layout() {
		imIcon.x = 0;
		imIcon.y = 0;
		
		tfLabel.x = PixelScene.align( PixelScene.uiCamera, imIcon.x + imIcon.width() + GAP );
		tfLabel.maxWidth = (int)(width - tfLabel.x);
		tfLabel.measure();
		tfLabel.y =  PixelScene.align( PixelScene.uiCamera,
			imIcon.height > tfLabel.height() ?
				(imIcon.height() - tfLabel.baseLine()) / 2 :
				imIcon.y );
				
		height = Math.max( imIcon.y + imIcon.height(), tfLabel.y + tfLabel.height() );
	}
	
	public void icon( Image icon ) {
		remove( imIcon );
		add( imIcon = icon );
	}
	
	public void label( String label ) {
		tfLabel.text( label );
	}
	
	public void label( String label, int color ) {
		tfLabel.text( label );
		tfLabel.hardlight( color );
	}
	
	public void color( int color ) {
		tfLabel.hardlight( color );
	}
}
