/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidPlatformSupport extends PlatformSupport {
	
	public void updateDisplaySize(){
		if (SPDSettings.landscape() != null) {
			AndroidLauncher.instance.setRequestedOrientation( SPDSettings.landscape() ?
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
		}

		GLSurfaceView view = (GLSurfaceView) ((AndroidGraphics)Gdx.graphics).getView();
		
		if (view.getMeasuredWidth() == 0 || view.getMeasuredHeight() == 0)
			return;
		
		Game.dispWidth = view.getMeasuredWidth();
		Game.dispHeight = view.getMeasuredHeight();

		boolean fullscreen = Build.VERSION.SDK_INT < Build.VERSION_CODES.N
				|| !AndroidLauncher.instance.isInMultiWindowMode();

		if (fullscreen && SPDSettings.landscape() != null
				&& (Game.dispWidth >= Game.dispHeight) != SPDSettings.landscape()){
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
		
		if (SPDSettings.powerSaver() && fullscreen){
			
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
						view.getHolder().setFixedSize(finalW, finalH);
					}
				});
				
			}
		} else {
			AndroidLauncher.instance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					view.getHolder().setSizeFromLayout();
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
	@SuppressWarnings("deprecation")
	public boolean connectedToUnmeteredNetwork() {
		//Returns true if using unmetered connection, use shortcut method if available
		ConnectivityManager cm = (ConnectivityManager) AndroidLauncher.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			return !cm.isActiveNetworkMetered();
		} else {
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
			return activeNetwork != null && activeNetwork.isConnectedOrConnecting() &&
					(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
					|| activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX
					|| activeNetwork.getType() == ConnectivityManager.TYPE_BLUETOOTH
					|| activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET);
		}
	}

	/* FONT SUPPORT */
	
	//droid sans / roboto, or a custom pixel font, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	//droid sans / nanum gothic / noto sans, for use with Korean
	private static FreeTypeFontGenerator KRFontGenerator;
	//droid sans / noto sans, for use with Simplified Chinese
	private static FreeTypeFontGenerator SCFontGenerator;
	//droid sans / noto sans, for use with Japanese
	private static FreeTypeFontGenerator JPFontGenerator;
	
	//special logic for handling korean android 6.0 font oddities
	private static boolean koreanAndroid6OTF = false;
	
	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;

		resetGenerators(false);
		fonts = new HashMap<>();
		basicFontGenerator = KRFontGenerator = SCFontGenerator = JPFontGenerator = null;
		
		if (systemfont && Gdx.files.absolute("/system/fonts/Roboto-Regular.ttf").exists()) {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/Roboto-Regular.ttf"));
		} else if (systemfont && Gdx.files.absolute("/system/fonts/DroidSans.ttf").exists()){
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/DroidSans.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
		}
		
		//android 7.0+. all asian fonts are nicely contained in one spot
		if (Gdx.files.absolute("/system/fonts/NotoSansCJK-Regular.ttc").exists()) {
			//typefaces are 0-JP, 1-KR, 2-SC, 3-TC.
			int typeFace;
			switch (SPDSettings.language()) {
				case JAPANESE:
					typeFace = 0;
					break;
				case KOREAN:
					typeFace = 1;
					break;
				case CHINESE:
				default:
					typeFace = 2;
			}
			KRFontGenerator = SCFontGenerator = JPFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansCJK-Regular.ttc"), typeFace);
			
		//otherwise we have to go over a few possibilities.
		} else {
			
			//Korean font generators
			if (Gdx.files.absolute("/system/fonts/NanumGothic.ttf").exists()){
				KRFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NanumGothic.ttf"));
			} else if (Gdx.files.absolute("/system/fonts/NotoSansKR-Regular.otf").exists()){
				KRFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansKR-Regular.otf"));
				koreanAndroid6OTF = true;
			}
			
			//Chinese font generators
			if (Gdx.files.absolute("/system/fonts/NotoSansSC-Regular.otf").exists()){
				SCFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansSC-Regular.otf"));
			} else if (Gdx.files.absolute("/system/fonts/NotoSansHans-Regular.otf").exists()){
				SCFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansHans-Regular.otf"));
			}
			
			//Japaneses font generators
			if (Gdx.files.absolute("/system/fonts/NotoSansJP-Regular.otf").exists()){
				JPFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansJP-Regular.otf"));
			}
			
			//set up a fallback generator for any remaining fonts
			FreeTypeFontGenerator fallbackGenerator;
			if (Gdx.files.absolute("/system/fonts/DroidSansFallback.ttf").exists()){
				fallbackGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/DroidSansFallback.ttf"));
			} else {
				//no fallback font, just set to null =/
				fallbackGenerator = null;
			}
			
			if (KRFontGenerator == null) KRFontGenerator = fallbackGenerator;
			if (SCFontGenerator == null) SCFontGenerator = fallbackGenerator;
			if (JPFontGenerator == null) JPFontGenerator = fallbackGenerator;
			
		}
		
		if (basicFontGenerator != null) fonts.put(basicFontGenerator, new HashMap<>());
		if (KRFontGenerator != null) fonts.put(KRFontGenerator, new HashMap<>());
		if (SCFontGenerator != null) fonts.put(SCFontGenerator, new HashMap<>());
		if (JPFontGenerator != null) fonts.put(JPFontGenerator, new HashMap<>());
		
		//would be nice to use RGBA4444 to save memory, but this causes problems on some gpus =S
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}

	private static Matcher KRMatcher = Pattern.compile("\\p{InHangul_Syllables}").matcher("");
	private static Matcher SCMatcher = Pattern.compile("\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}").matcher("");
	private static Matcher JPMatcher = Pattern.compile("\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (KRMatcher.reset(input).find()){
			return KRFontGenerator;
		} else if (SCMatcher.reset(input).find()){
			return SCFontGenerator;
		} else if (JPMatcher.reset(input).find()){
			return JPFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}
	
	//splits on newlines, underscores, and chinese/japaneses characters
	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})|" +
					"(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");
	
	//additionally splits on words, so that each word can be arranged individually
	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})|" +
					"(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");
	
	//splits on each non-hangul character. Needed for weird android 6.0 font files
	private Pattern android6KRSplitter = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
					"(?!\\p{InHangul_Syllables})|(?<!\\p{InHangul_Syllables})");
	
	@Override
	public String[] splitforTextBlock(String text, boolean multiline) {
		if (koreanAndroid6OTF && getGeneratorForString(text) == KRFontGenerator){
			return android6KRSplitter.split(text);
		} else if (multiline) {
			return regularsplitterMultiline.split(text);
		} else {
			return regularsplitter.split(text);
		}
	}
	
}
