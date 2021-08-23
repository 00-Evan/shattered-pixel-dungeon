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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

public class SimpleButton extends Component {
			
	private Image image;
	
	public SimpleButton( Image image ) {
		super();
		
		this.image.copy( image );
		width = image.width;
		height = image.height;
	}
	
	@Override
	protected void createChildren() {
		image = new Image();
		add( image );
		
		add( new PointerArea( image ) {
			@Override
			protected void onPointerDown( PointerEvent event ) {
				image.brightness( 1.2f );
			}
			@Override
			protected void onPointerUp( PointerEvent event ) {
				image.brightness( 1.0f );
			}
			@Override
			protected void onClick( PointerEvent event ) {
				SimpleButton.this.onClick();
			}
		} );
	}
	
	@Override
	protected void layout() {
		image.x = x;
		image.y = y;
	}
	
	protected void onClick() {}
}
