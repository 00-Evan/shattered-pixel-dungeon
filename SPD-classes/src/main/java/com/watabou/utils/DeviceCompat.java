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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.watabou.noosa.Game;

//TODO migrate to platformSupport class
public class DeviceCompat {
	
	public static boolean supportsFullScreen(){
		switch (Gdx.app.getType()){
			case Android:
				//Android 4.4 KitKat and later, this is for immersive mode
				return Gdx.app.getVersion() >= 19;
			default:
				//TODO implement functionality for other platforms here
				return true;
		}
	}
	
	public static boolean isDesktop(){
		return Gdx.app.getType() == Application.ApplicationType.Desktop;
	}
	
	public static boolean legacyDevice(){
		switch (Gdx.app.getType()){
			case Android:
				//Devices prior to Android 4.1 Jelly Bean
				return Gdx.app.getVersion() < 16;
			default:
				//TODO implement functionality for other platforms here
				return false;
		}
	}
	
	public static boolean isDebug(){
		return Game.version.contains("INDEV");
	}
	
	public static void openURI( String URI ){
		Gdx.net.openURI( URI );
	}
	
	public static void log( String tag, String message ){
		Gdx.app.log( tag, message );
	}

}
