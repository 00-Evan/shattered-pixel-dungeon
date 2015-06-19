/*
 * Copyright (C) 2012-2015  Oleg Dolya
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

package com.watabou.noosa;

import com.watabou.gltextures.TextureCache;

public class ColorBlock extends Image implements Resizable {
	
	public ColorBlock( float width, float height, int color ) {
		super( TextureCache.createSolid( color ) );
		scale.set( width, height );
		origin.set( 0, 0 );
	}

	@Override
	public void size( float width, float height ) {
		scale.set( width, height );
	}
	
	@Override
	public float width() {
		return scale.x;
	}
	
	@Override
	public float height() {
		return scale.y;
	}
}
