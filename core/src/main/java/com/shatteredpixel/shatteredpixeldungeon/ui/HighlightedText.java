/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Highlighter;

public class HighlightedText extends Component {

	protected BitmapTextMultiline normal;
	protected BitmapTextMultiline highlighted;

	protected int nColor = 0xFFFFFF;
	protected int hColor = 0xFFFF44;

	public HighlightedText( float size ) {
		normal = PixelScene.createMultiline( size );
		add( normal );

		highlighted = PixelScene.createMultiline( size );
		add( highlighted );

		setColor( 0xFFFFFF, 0xFFFF44 );
	}

	@Override
	protected void layout() {
		normal.x = highlighted.x = x;
		normal.y = highlighted.y = y;
	}

	public void text( String value, int maxWidth ) {
		Highlighter hl = new Highlighter( value );

		normal.text( hl.text );
		normal.maxWidth = maxWidth;
		normal.measure();

		if (hl.isHighlighted()) {
			normal.mask = hl.inverted();

			highlighted.text( hl.text );
			highlighted.maxWidth = maxWidth;
			highlighted.measure();

			highlighted.mask = hl.mask;
			highlighted.visible = true;
		} else {
			highlighted.visible = false;
		}

		width = normal.width();
		height = normal.height();
	}

	public void setColor( int n, int h ) {
		normal.hardlight( n );
		highlighted.hardlight( h );
	}
}