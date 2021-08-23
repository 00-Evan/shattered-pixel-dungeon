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

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;

public class DesktopWindowListener implements Lwjgl3WindowListener {
	
	@Override
	public void created ( Lwjgl3Window lwjgl3Window ) {
		if (SPDSettings.fullscreen()){
			lwjgl3Window.postRunnable( new Runnable() {
				@Override
				public void run () {
					Gdx.graphics.setFullscreenMode( Gdx.graphics.getDisplayMode() );
				}
			} );
		}
		if (SPDSettings.windowMaximized()) {
			lwjgl3Window.maximizeWindow();
		}
	}
	
	@Override
	public void maximized ( boolean b ) {
		SPDSettings.windowMaximized( b );
	}
	
	@Override
	public void iconified ( boolean b ) { }
	public void focusLost () { }
	public void focusGained () { }
	public boolean closeRequested () { return true; }
	public void filesDropped ( String[] strings ) { }
	public void refreshRequested () { }
}
