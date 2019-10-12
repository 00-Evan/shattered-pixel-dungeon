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

package com.shatteredpixel.shatteredpixeldungeon.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;

public class AndroidGame extends AndroidApplication {
	
	public static AndroidApplication instance;
	protected static GLSurfaceView view;
	
	private AndroidPlatformSupport support;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		instance = this;
		
		try {
			Game.version = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Game.version = "???";
		}
		try {
			Game.versionCode = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Game.versionCode = 0;
		}
		
		// grab preferences directly using our instance first
		// so that we don't need to rely on Gdx.app, which isn't initialized yet.
		SPDSettings.setPrefsFromInstance(instance);
		
		//set desired orientation (if it exists) before initializing the app.
		if (SPDSettings.landscapeFromSettings() != null) {
			if (SPDSettings.landscapeFromSettings()){
				instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			} else {
				instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			}
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			//use rgb888 on more modern devices for better visuals
			config.r = config.g = config.b = 8;
		} else {
			//and rgb565 (default) on older ones for better performance
		}
		
		config.useCompass = false;
		config.useAccelerometer = false;
		
		support = new AndroidPlatformSupport();
		
		support.updateSystemUI();
		
		initialize(new ShatteredPixelDungeon(support), config);
		
		view = (GLSurfaceView)graphics.getView();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			TelephonyManager mgr =
					(TelephonyManager) instance.getSystemService(Activity.TELEPHONY_SERVICE);
			mgr.listen(new PhoneStateListener(){
				
				@Override
				public void onCallStateChanged(int state, String incomingNumber)
				{
					if( state == TelephonyManager.CALL_STATE_RINGING ) {
						Music.INSTANCE.pause();
						
					} else if( state == TelephonyManager.CALL_STATE_IDLE ) {
						if (!Game.instance.isPaused()) {
							Music.INSTANCE.resume();
						}
					}
					
					super.onCallStateChanged(state, incomingNumber);
				}
			}, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		support.updateSystemUI();
	}
	
	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		support.updateSystemUI();
	}
}