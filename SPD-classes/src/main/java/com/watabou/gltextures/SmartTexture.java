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

package com.watabou.gltextures;

import com.badlogic.gdx.graphics.Pixmap;
import com.watabou.glwrap.Texture;
import com.watabou.utils.RectF;

public class SmartTexture extends Texture {

	public int width;
	public int height;
	
	public int fModeMin;
	public int fModeMax;
	
	public int wModeH;
	public int wModeV;
	
	public Pixmap bitmap;
	
	public Atlas atlas;

	public SmartTexture( Pixmap bitmap ) {
		this( bitmap, NEAREST, CLAMP, false );
	}

	public SmartTexture( Pixmap bitmap, int filtering, int wrapping, boolean premultiplied ) {

		this.bitmap = bitmap;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		this.fModeMin = this.fModeMax = filtering;
		this.wModeH = this.wModeV = wrapping;
		this.premultiplied = premultiplied;

	}

	@Override
	protected void generate() {
		super.generate();
		bitmap( bitmap );
		filter( fModeMin, fModeMax );
		wrap( wModeH, wModeV );
	}

	@Override
	public void filter(int minMode, int maxMode) {
		fModeMin = minMode;
		fModeMax = maxMode;
		if (id != -1)
			super.filter( fModeMin, fModeMax );
	}

	@Override
	public void wrap( int s, int t ) {
		wModeH = s;
		wModeV = t;
		if (id != -1)
			super.wrap( wModeH, wModeV );
	}
	
	@Override
	public void bitmap( Pixmap bitmap ) {
		super.bitmap( bitmap );
		
		this.bitmap = bitmap;
		width = bitmap.getWidth();
		height = bitmap.getHeight();
	}
	
	public int getPixel( int x, int y ){
		return bitmap.getPixel(x, y);
	}
	
	public void reload() {
		id = -1;
		generate();
	}
	
	@Override
	public void delete() {
		
		super.delete();

		if (bitmap != null)
			bitmap.dispose();
		bitmap = null;
	}
	
	public RectF uvRect( float left, float top, float right, float bottom ) {
		return new RectF(
			left	/ width,
			top		/ height,
			right	/ width,
			bottom	/ height );
	}
}
