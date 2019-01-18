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

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.watabou.BuildConfig;
import com.watabou.noosa.Game;

public class DeviceCompat {
	
	public static boolean supportsFullScreen(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}
	
	public static boolean legacyDevice(){
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN;
	}
	
	public static boolean supportsPlayServices(){
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}
	
	public static boolean usesISO_8859_1(){
		return Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO;
	}
	
	public static boolean isDebug(){
		return BuildConfig.DEBUG;
	}
	
	public static void openURI( String URI ){
		Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( URI ) );
		Game.instance.startActivity( intent );
	}
	
	public static void log( String tag, String message ){
		Log.i( tag, message );
	}

}
