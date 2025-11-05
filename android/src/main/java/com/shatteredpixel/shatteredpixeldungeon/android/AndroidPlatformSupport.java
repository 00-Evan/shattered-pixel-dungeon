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

package com.shatteredpixel.shatteredpixeldungeon.android;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.RectF;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidPlatformSupport extends PlatformSupport {
	
	public void updateDisplaySize(){
		AndroidLauncher.instance.setRequestedOrientation( SPDSettings.landscape() ?
				ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
				ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED );

		ShatteredPixelDungeon.seamlessResetScene();
	}

	public boolean supportsFullScreen(){
		//We support hiding the navigation bar or gesture bar, if it is present
		// on Android 9+ we check for this, on earlier just assume it's present
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			WindowInsets insets = AndroidLauncher.instance.getApplicationWindow().getDecorView().getRootWindowInsets();
			return insets != null && (insets.getStableInsetBottom() > 0 || insets.getStableInsetRight() > 0 || insets.getStableInsetLeft() > 0);
		} else {
			return true;
		}
	}

	@Override
	public RectF getDisplayCutout() {
		RectF cutoutRect = new RectF();

		//some extra logic here is because cutouts can apparently be returned inverted
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			DisplayCutout cutout = AndroidLauncher.instance.getApplicationWindow().getDecorView().getRootWindowInsets().getDisplayCutout();

			Rect largest = null;
			if (cutout != null) {
				for (Rect r : cutout.getBoundingRects()) {
					if (largest == null
							|| Math.abs(r.height() * r.width()) > Math.abs(largest.height() * largest.width())) {
						largest = r;
					}
				}
			}

			if (largest != null){
				cutoutRect.left = Math.min(largest.left, largest.right);
				cutoutRect.right = Math.max(largest.left, largest.right);
				cutoutRect.top  = Math.min(largest.top, largest.bottom);
				cutoutRect.bottom  = Math.max(largest.top, largest.bottom);
			}
		}

		return cutoutRect;
	}

	@Override
	public RectF getSafeInsets( int level ) {
		RectF insets = new RectF();

		//getting insets technically works down to 6.0 Marshmallow, but we let the device handle all of that prior to 9.0 Pie
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !AndroidLauncher.instance.isInMultiWindowMode()) {
			WindowInsets rootInsets = AndroidLauncher.instance.getApplicationWindow().getDecorView().getRootWindowInsets();
			if (rootInsets != null) {

				//Navigation bar (never on the top)
				if (supportsFullScreen() && !SPDSettings.fullscreen()) {
					insets.left = Math.max(insets.left, rootInsets.getStableInsetLeft());
					insets.right = Math.max(insets.right, rootInsets.getStableInsetRight());
					insets.bottom = Math.max(insets.bottom, rootInsets.getStableInsetBottom());
				}

				//display cutout
				if (level > INSET_BLK) {
					DisplayCutout cutout = rootInsets.getDisplayCutout();

					if (cutout != null) {
						boolean largeCutout = false;
						boolean cutoutsPresent = false;

						int screenSize = Game.width * Game.height;
						for (Rect r : cutout.getBoundingRects()) {
							//use abs as some cutouts can apparently be returned inverted
							int cutoutSize = Math.abs(r.height() * r.width());
							//display cutouts are considered large if they take up more than 0.75%
							// of the screen/ in reality we want less than about 0.5%,
							// but some cutouts over-report their size, Pixel devices especially =S
							if (cutoutSize > 0){
								cutoutsPresent = true;
								if (cutoutSize * 133.33f >= screenSize) {
									largeCutout = true;
								}
							}
						}

						if (!cutoutsPresent){
							//if we get no cutouts reported, assume the device is lying to us
							// and there actually is a cutout, which we must assume is large =S
							largeCutout = true;
						}

						if (largeCutout || level == INSET_ALL) {
							insets.left = Math.max(insets.left, cutout.getSafeInsetLeft());
							insets.top = Math.max(insets.top, cutout.getSafeInsetTop());
							insets.right = Math.max(insets.right, cutout.getSafeInsetRight());
							insets.bottom = Math.max(insets.bottom, cutout.getSafeInsetBottom());
						}
					}
				}
			}
		}
		return insets;
	}

	public void updateSystemUI() {
		
		AndroidLauncher.instance.runOnUiThread(new Runnable() {
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

				if (supportsFullScreen() && SPDSettings.fullscreen()) {
					AndroidLauncher.instance.getWindow().getDecorView().setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
									| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
				} else {
					//still want to hide the status bar and cutout void
					AndroidLauncher.instance.getWindow().getDecorView().setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
				}
			}
		});
		
	}
	
	@Override
	public boolean connectedToUnmeteredNetwork() {
		//Returns true if using unmetered connection
		return !((ConnectivityManager) AndroidLauncher.instance.getSystemService(Context.CONNECTIVITY_SERVICE)).isActiveNetworkMetered();
	}

	@Override
	public boolean supportsVibration() {
		return true; //always true on Android
	}

	/* FONT SUPPORT */
	
	//droid sans / roboto, or a custom pixel font, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	//droid sans / nanum gothic / noto sans, for use with Korean
	private static FreeTypeFontGenerator KRFontGenerator;
	//droid sans / noto sans, for use with Chinese
	private static FreeTypeFontGenerator ZHFontGenerator;
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
		basicFontGenerator = KRFontGenerator = ZHFontGenerator = JPFontGenerator = null;
		
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
				case CHI_SMPL:
				default:
					typeFace = 2;
					break;
				case CHI_TRAD:
					typeFace = 3;
					break;
			}
			KRFontGenerator = ZHFontGenerator = JPFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansCJK-Regular.ttc"), typeFace);
			
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
			//we don't use a separate generator for traditional chinese because
			// NotoSansTC-Regular and NotoSansHant-Regular seem to only contain some hant-specific
			// ways to draw certain symbols, too much messing for old android
			if (Gdx.files.absolute("/system/fonts/NotoSansSC-Regular.otf").exists()){
				ZHFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansSC-Regular.otf"));
			} else if (Gdx.files.absolute("/system/fonts/NotoSansHans-Regular.otf").exists()){
				ZHFontGenerator = new FreeTypeFontGenerator(Gdx.files.absolute("/system/fonts/NotoSansHans-Regular.otf"));
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
			if (ZHFontGenerator == null) ZHFontGenerator = fallbackGenerator;
			if (JPFontGenerator == null) JPFontGenerator = fallbackGenerator;
			
		}
		
		if (basicFontGenerator != null) fonts.put(basicFontGenerator, new HashMap<>());
		if (KRFontGenerator != null) fonts.put(KRFontGenerator, new HashMap<>());
		if (ZHFontGenerator != null) fonts.put(ZHFontGenerator, new HashMap<>());
		if (JPFontGenerator != null) fonts.put(JPFontGenerator, new HashMap<>());
		
		//would be nice to use RGBA4444 to save memory, but this causes problems on some gpus =S
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}

	private static Matcher KRMatcher = Pattern.compile("\\p{InHangul_Syllables}").matcher("");
	private static Matcher ZHMatcher = Pattern.compile("\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}").matcher("");
	private static Matcher JPMatcher = Pattern.compile("\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (KRMatcher.reset(input).find()){
			return KRFontGenerator;
		} else if (ZHMatcher.reset(input).find()){
			return ZHFontGenerator;
		} else if (JPMatcher.reset(input).find()){
			return JPFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}

	//splits on newline (for layout), chinese/japanese (for font choice), and '_'/'**' (for highlighting)
	private Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})|" +
					"(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");

	//additionally splits on spaces, so that each word can be laid out individually
	private Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})|" +
					"(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");
	
	//splits on each non-hangul character. Needed for weird android 6.0 font files
	private Pattern android6KRSplitter = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\*\\*)|(?=\\*\\*)|" +
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
