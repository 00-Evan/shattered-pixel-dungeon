/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Os;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.watabou.noosa.Game;

//TODO migrate to platformSupport class
public class DeviceCompat {

	//return APi level on Android, major OS version on iOS, 0 on desktop
	public static int getPlatformVersion(){
		return Gdx.app.getVersion();
	}

	public static boolean isAndroid(){
		return SharedLibraryLoader.os == Os.Android;
	}

	public static boolean isiOS(){
		return SharedLibraryLoader.os == Os.IOS;
	}

	public static boolean isDesktop(){
		return SharedLibraryLoader.os == Os.Windows || SharedLibraryLoader.os == Os.MacOsX || SharedLibraryLoader.os == Os.Linux;
	}

	public static boolean hasHardKeyboard(){
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard);
	}
	
	public static boolean isDebug(){
		return Game.version.contains("INDEV");
	}
	
	public static void log( String tag, String message ){
		Gdx.app.log( tag, message );
	}

	//some devices (macOS mainly) report virtual pixels to Shattered, but sometimes we want real pixel precision
	//this returns the number of real pixels per virtual pixel in the X dimension...
	public static float getRealPixelScaleX(){
		return (Gdx.graphics.getBackBufferWidth() / (float)Game.width );
	}

	//...and in the Y dimension
	public static float getRealPixelScaleY(){
		return (Gdx.graphics.getBackBufferHeight() / (float)Game.height );
	}

}
