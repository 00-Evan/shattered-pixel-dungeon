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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.android.windows.WndAndroidTextInput;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PlatformSupport;

import java.util.HashMap;
import java.util.regex.Pattern;

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
	public void promptTextInput(final String title, final String hintText, final int maxLen, final boolean multiLine, final String posTxt, final String negTxt, final TextCallback callback) {
		Game.runOnRenderThread( new Callback() {
					@Override
					public void call() {
						Game.scene().addToFront(new WndAndroidTextInput(title, hintText, maxLen, multiLine, posTxt, negTxt) {
							@Override
							protected void onSelect(boolean positive) {
								callback.onSelect(positive, getText());
							}
						});
					}
				}
		);
	}
	
	/* FONT SUPPORT */
	
	private int pageSize;
	private PixmapPacker packer;
	private boolean systemfont;
	
	//custom ttf or droid sans, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator latinAndCryllicFontGenerator;
	private static HashMap<Integer, BitmapFont> pixelFonts = new HashMap<>();
	
	//droid sans, for use with hangul languages (Korean)
	private static FreeTypeFontGenerator hangulFontGenerator;
	private static HashMap<Integer, BitmapFont> hangulFonts = new HashMap<>();
	
	//droid sans, for use with han languages (Chinese, Japanese)
	private static FreeTypeFontGenerator hanFontGenerator;
	private static HashMap<Integer, BitmapFont> hanFonts = new HashMap<>();
	
	private static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;
	
	public static Pattern hanMatcher = Pattern.compile("\\p{InHiragana}|\\p{InKatakana}|\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}");
	public static Pattern hangulMatcher = Pattern.compile("\\p{InHangul_Syllables}");
	
	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;
		
		if (fonts != null){
			for (FreeTypeFontGenerator generator : fonts.keySet()){
				for (BitmapFont f : fonts.get(generator).values()){
					f.dispose();
				}
				fonts.get(generator).clear();
				generator.dispose();
			}
			fonts.clear();
			if (packer != null){
				for (PixmapPacker.Page p : packer.getPages()){
					p.getTexture().dispose();
				}
				packer.dispose();
			}
		}
		fonts = new HashMap<>();
		
		if (systemfont){
			latinAndCryllicFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/DroidSans.ttf"));
		} else {
			//FIXME need to add currency symbols
			latinAndCryllicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("pixelfont.ttf"));
		}
		
		//android 7.0+. Finally back to normalcy, everything nicely in one .ttc
		if (Gdx.files.absolute("/system/fonts/NotoSansCJK-Regular.ttc").exists()) {
			//TODO why typeface #2 here? It seems to match DroidSansFallback.ttf the best, but why?
			//might be that different languages prefer a different face, see about tweaking this?
			hangulFontGenerator = hanFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansCJK-Regular.ttc"), 2);
		
		//android 6.0. Fonts are split over multiple .otf files, very awkward
		} else if (Gdx.files.absolute("/system/fonts/NotoSansKR-Regular.otf").exists()) {
			//FIXME all fonts are messed up here currently, need to fix this
			hangulFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansKR-Regular.otf"));
			hanFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansSC-Regular.otf"));
		
		//android 4.4-5.1. Korean no longer broken with the addition of NanumGothic.
		} else if (Gdx.files.absolute("/system/fonts/NanumGothic.ttf").exists()){
			hangulFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NanumGothic.ttf"));
			hanFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/DroidSansFallback.ttf"));
		
		//android 4.3-. Note that korean isn't in DroidSandFallback and is therefore unfixably broken on 4.2 and 4.3
		} else if (Gdx.files.absolute("/system/fonts/DroidSansFallback.ttf").exists()) {
			hangulFontGenerator = hanFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/DroidSansFallback.ttf"));
		
		//shouldn't ever trigger, but just incase
		} else {
			hangulFontGenerator = hanFontGenerator = latinAndCryllicFontGenerator;
		}
		
		fonts.put(latinAndCryllicFontGenerator, pixelFonts);
		fonts.put(hangulFontGenerator, hangulFonts);
		fonts.put(hanFontGenerator, hanFonts);
		fonts.put(kataFontGenerator, kataFonts);
		
		//use RGBA4444 to save memory. Extra precision isn't needed here.
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA4444, 1, false);
	}
	
	@Override
	public void resetGenerators() {
		for (FreeTypeFontGenerator generator : fonts.keySet()){
			for (BitmapFont f : fonts.get(generator).values()){
				f.dispose();
			}
			fonts.get(generator).clear();
			generator.dispose();
		}
		fonts.clear();
		if (packer != null){
			for (PixmapPacker.Page p : packer.getPages()){
				p.getTexture().dispose();
			}
			packer.dispose();
		}
		fonts = null;
		setupFontGenerators(pageSize, systemfont);
	}
	
	private static FreeTypeFontGenerator getGeneratorForString( String input ){
		if (hanMatcher.matcher(input).find()){
			return hanFontGenerator;
		} else if (hangulMatcher.matcher(input).find()){
			return hangulFontGenerator;
		} else {
			return latinAndCryllicFontGenerator;
		}
	}
	
	@Override
	public BitmapFont getFont(int size, String text) {
		FreeTypeFontGenerator generator = getGeneratorForString(text);
		
		if (!fonts.get(generator).containsKey(size)) {
			FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameters.size = size;
			parameters.flip = true;
			parameters.borderWidth = parameters.size / 10f;
			parameters.renderCount = 3;
			parameters.hinting = FreeTypeFontGenerator.Hinting.None;
			parameters.spaceX = -(int) parameters.borderWidth;
			parameters.incremental = true;
			parameters.characters = "";
			parameters.packer = packer;
			
			fonts.get(generator).put(size, generator.generateFont(parameters));
		}
		
		return fonts.get(generator).get(size);
	}
}
