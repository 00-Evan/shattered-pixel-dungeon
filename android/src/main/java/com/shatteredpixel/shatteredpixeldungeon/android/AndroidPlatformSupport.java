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

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.android.windows.WndAndroidTextInput;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;

public class AndroidPlatformSupport extends PlatformSupport {
	
	public void updateDisplaySize(){
		boolean landscape = SPDSettings.landscape();
		
		AndroidLauncher.instance.setRequestedOrientation(landscape ?
				ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
				ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		
		if (AndroidLauncher.view.getMeasuredWidth() == 0 || AndroidLauncher.view.getMeasuredHeight() == 0)
			return;
		
		Game.dispWidth = AndroidLauncher.view.getMeasuredWidth();
		Game.dispHeight = AndroidLauncher.view.getMeasuredHeight();
		
		if ((Game.dispWidth > Game.dispHeight) != landscape){
			int tmp = Game.dispWidth;
			Game.dispWidth = Game.dispHeight;
			Game.dispHeight = tmp;
		}
		
		float dispRatio = Game.dispWidth / (float)Game.dispHeight;
		
		float renderWidth = dispRatio > 1 ? PixelScene.MIN_WIDTH_L : PixelScene.MIN_WIDTH_P;
		float renderHeight = dispRatio > 1 ? PixelScene.MIN_HEIGHT_L : PixelScene.MIN_HEIGHT_P;
		
		//force power saver in this case as all devices must run at at least 2x scale.
		if (Game.dispWidth < renderWidth*2 || Game.dispHeight < renderHeight*2)
			SPDSettings.put( SPDSettings.KEY_POWER_SAVER, true );
		
		if (SPDSettings.powerSaver()){
			
			int maxZoom = (int)Math.min(Game.dispWidth/renderWidth, Game.dispHeight/renderHeight);
			
			renderWidth *= Math.max( 2, Math.round(1f + maxZoom*0.4f));
			renderHeight *= Math.max( 2, Math.round(1f + maxZoom*0.4f));
			
			if (dispRatio > renderWidth / renderHeight){
				renderWidth = renderHeight * dispRatio;
			} else {
				renderHeight = renderWidth / dispRatio;
			}
			
			final int finalW = Math.round(renderWidth);
			final int finalH = Math.round(renderHeight);
			if (finalW != Game.width || finalH != Game.height){
				
				AndroidLauncher.instance.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AndroidLauncher.view.getHolder().setFixedSize(finalW, finalH);
					}
				});
				
			}
		} else {
			AndroidLauncher.instance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AndroidLauncher.view.getHolder().setSizeFromLayout();
				}
			});
		}
	}
	
	public void updateSystemUI() {
		
		AndroidLauncher.instance.runOnUiThread(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				boolean fullscreen = Build.VERSION.SDK_INT < Build.VERSION_CODES.N
						|| !AndroidLauncher.instance.isInMultiWindowMode();
				
				if (fullscreen){
					AndroidLauncher.instance.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				} else {
					AndroidLauncher.instance.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				}
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
					if (SPDSettings.fullscreen()) {
						AndroidLauncher.instance.getWindow().getDecorView().setSystemUiVisibility(
								View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
										| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
										| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
					} else {
						AndroidLauncher.instance.getWindow().getDecorView().setSystemUiVisibility(
								View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
					}
				}
			}
		});
		
	}
	
	@Override
	public void promptTextInput(String title, String hintText, int maxLen, boolean multiLine, String posTxt, String negTxt, final TextCallback callback) {
		Game.scene().addToFront(new WndAndroidTextInput(title, hintText, maxLen, multiLine, posTxt, negTxt){
			@Override
			protected void onSelect(boolean positive) {
				callback.onSelect(positive, getText());
			}
		});
	}
}
