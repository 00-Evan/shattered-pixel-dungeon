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

package com.watabou.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class PlatformSupport {
	
	public abstract void updateDisplaySize();
	
	public abstract void updateSystemUI();

	public abstract boolean connectedToUnmeteredNetwork();
	
	//FIXME this is currently used because no platform-agnostic text input has been implemented.
	//should look into doing that using either plain openGL or Libgdx's libraries
	public abstract void promptTextInput( String title, String hintText, int maxLen, boolean multiLine,
	                             String posTxt, String negTxt, TextCallback callback);
	
	public static abstract class TextCallback {
		public abstract void onSelect( boolean positive, String text );
	}
	
	//TODO should consider spinning this into its own class, rather than platform support getting ever bigger
	
	public abstract void setupFontGenerators(int pageSize, boolean systemFont );
	
	public abstract void resetGenerators();
	
	public abstract BitmapFont getFont(int size, String text);
	
	public abstract String[] splitforTextBlock( String text, boolean multiline );

}
