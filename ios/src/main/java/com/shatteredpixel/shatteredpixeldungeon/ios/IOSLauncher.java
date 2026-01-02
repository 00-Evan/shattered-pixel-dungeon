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

package com.shatteredpixel.shatteredpixeldungeon.ios;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.iosrobovm.DefaultIOSInput;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosrobovm.IOSInput;
import com.badlogic.gdx.backends.iosrobovm.IOSPreferences;
import com.badlogic.gdx.backends.iosrobovm.bindings.metalangle.MGLDrawableColorFormat;
import com.badlogic.gdx.backends.iosrobovm.bindings.metalangle.MGLDrawableDepthFormat;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSException;
import org.robovm.apple.foundation.NSMutableDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIInterfaceOrientation;
import org.robovm.apple.uikit.UITextField;

import java.io.File;

public class IOSLauncher extends IOSApplication.Delegate {
	@Override
	protected IOSApplication createApplication() {

		//ensures the app actually crashes if there's an error in the mobiVM runtime
		NSException.registerDefaultJavaUncaughtExceptionHandler();

		try {
			Game.version = NSBundle.getMainBundle().getInfoDictionaryObject("CFBundleVersionString").description();
		} catch (Exception e) {
			Game.version = "???";
		}
		try {
			Game.versionCode = Integer.parseInt(NSBundle.getMainBundle().getInfoDictionaryObject("CFBundleVersion").description());
		} catch (Exception e) {
			Game.versionCode = 0;
		}

		if (UpdateImpl.supportsUpdates()) {
			Updates.service = UpdateImpl.getUpdateService();
		}
		if (NewsImpl.supportsNews()) {
			News.service = NewsImpl.getNewsService();
		}

		FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

		//sets up preferences early so they can be read.
		//this is mostly a copy-paste from IOSApplication.getPreferences
			File libraryPath = new File(System.getenv("HOME"), "Library");
			File finalPath = new File(libraryPath, SPDSettings.DEFAULT_PREFS_FILE + ".plist");

			@SuppressWarnings("unchecked")
			NSMutableDictionary<NSString, NSObject> nsDictionary = (NSMutableDictionary<NSString, NSObject>)NSMutableDictionary
					.read(finalPath);

			// if it fails to get an existing dictionary, create a new one.
			if (nsDictionary == null) {
				nsDictionary = new NSMutableDictionary<NSString, NSObject>();
				nsDictionary.write(finalPath, false);
			}
			SPDSettings.set(new IOSPreferences(nsDictionary, finalPath.getAbsolutePath()));
		//end of prefs setup

		IOSApplicationConfiguration config = new IOSApplicationConfiguration();

		config.colorFormat = MGLDrawableColorFormat.RGBA8888;
		config.depthFormat = MGLDrawableDepthFormat.None;
		config.hdpiMode = HdpiMode.Pixels;

		config.hideHomeIndicator = true;
		config.overrideRingerSwitch = SPDSettings.ignoreSilentMode();

		config.useHaptics = true;
		config.useAccelerometer = false;
		config.useCompass = false;

		//devices not currently listed in LibGDX's IOSDevice class
		config.addIosDevice("IPHONE_12_MINI",       "iPhone13,1", 476);
		config.addIosDevice("IPHONE_12",            "iPhone13,2", 460);
		config.addIosDevice("IPHONE_12_PRO",        "iPhone13,3", 460);
		config.addIosDevice("IPHONE_12_PRO_MAX",    "iPhone13,4", 458);
		config.addIosDevice("IPHONE_13_PRO",        "iPhone14,2", 460);
		config.addIosDevice("IPHONE_13_PRO_MAX",    "iPhone14,3", 458);
		config.addIosDevice("IPHONE_13_MINI",       "iPhone14,4", 476);
		config.addIosDevice("IPHONE_13",            "iPhone14,5", 460);
		config.addIosDevice("IPHONE_SE_3G",         "iPhone14,6", 326);
		config.addIosDevice("IPHONE_14",            "iPhone14,7", 460);
		config.addIosDevice("IPHONE_14_PLUS",       "iPhone14,8", 458);
		config.addIosDevice("IPHONE_14_PRO",        "iPhone15,2", 460);
		config.addIosDevice("IPHONE_14_PRO_MAX",    "iPhone15,3", 460);
		config.addIosDevice("IPHONE_14_PRO",        "iPhone15,2", 460);
		config.addIosDevice("IPHONE_14_PRO_MAX",    "iPhone15,3", 460);
		config.addIosDevice("IPHONE_15",            "iPhone15,4", 460);
		config.addIosDevice("IPHONE_15_PLUS",       "iPhone15,5", 460);
		config.addIosDevice("IPHONE_15_PRO",        "iPhone16,1", 460);
		config.addIosDevice("IPHONE_15_PRO_MAX",    "iPhone16,2", 460);
		config.addIosDevice("IPHONE_16_PRO",        "iPhone17,1", 460);
		config.addIosDevice("IPHONE_16_PRO_MAX",    "iPhone17,2", 460);
		config.addIosDevice("IPHONE_16",            "iPhone17,3", 460);
		config.addIosDevice("IPHONE_16_PLUS",       "iPhone17,4", 460);
		config.addIosDevice("IPHONE_16E",           "iPhone17,5", 460);
		config.addIosDevice("IPHONE_17_PRO",        "iPhone18,1", 460);
		config.addIosDevice("IPHONE_17_PRO_MAX",    "iPhone18,2", 460);
		config.addIosDevice("IPHONE_17",            "iPhone18,3", 460);
		config.addIosDevice("IPHONE_AIR",           "iPhone18,4", 460);

		config.addIosDevice("IPAD_7G_WIFI",                 "iPad7,11", 264);
		config.addIosDevice("IPAD_7G_WIFI_CELLULAR",        "iPad7,12", 264);
		config.addIosDevice("IPAD_8G_WIFI",                 "iPad11,6", 264);
		config.addIosDevice("IPAD_8G_WIFI_CELLULAR",        "iPad11,7", 264);
		config.addIosDevice("IPAD_AIR_4G_WIFI",             "iPad13,1", 264);
		config.addIosDevice("IPAD_AIR_4G_WIFI_CELLULAR",    "iPad13,2", 264);
		config.addIosDevice("IPAD_9G_WIFI",                 "iPad12,1", 264);
		config.addIosDevice("IPAD_9G_WIFI_CELLULAR",        "iPad12,2", 264);
		config.addIosDevice("IPAD_MINI_6G_WIFI",            "iPad14,1", 326);
		config.addIosDevice("IPAD_MINI_6G_WIFI_CELLULAR",   "iPad14,2", 326);
		config.addIosDevice("IPAD_AIR_4G_WIFI",             "iPad13,1", 264);
		config.addIosDevice("IPAD_AIR_4G_WIFI_CELLULAR",    "iPad13,2", 264);
		config.addIosDevice("IPAD_PRO_11_3G",               "iPad13,4", 264);
		config.addIosDevice("IPAD_PRO_11_3G",               "iPad13,5", 264);
		config.addIosDevice("IPAD_PRO_11_3G",               "iPad13,6", 264);
		config.addIosDevice("IPAD_PRO_11_3G",               "iPad13,7", 264);
		config.addIosDevice("IPAD_PRO_12.9_5G",             "iPad13,8", 264);
		config.addIosDevice("IPAD_PRO_12.9_5G",             "iPad13,9", 264);
		config.addIosDevice("IPAD_PRO_12.9_5G",             "iPad13,10",264);
		config.addIosDevice("IPAD_PRO_12.9_5G",             "iPad13,11",264);
		config.addIosDevice("IPAD_AIR_5G_WIFI",             "iPad13,16",264);
		config.addIosDevice("IPAD_AIR_5G_WIFI_CELLULAR",    "iPad13,17",264);
		config.addIosDevice("IPAD_10G",                     "iPad13,18",264);
		config.addIosDevice("IPAD_10G",                     "iPad13,19",264);
		config.addIosDevice("IPAD_PRO_11_4G",               "iPad14,3", 264);
		config.addIosDevice("IPAD_PRO_11_4G",               "iPad14,4", 264);
		config.addIosDevice("IPAD_PRO_12.9_6G",             "iPad14,5", 264);
		config.addIosDevice("IPAD_PRO_12.9_6G",             "iPad14,6", 264);
		config.addIosDevice("IPAD_AIR_11_6G_WIFI",          "iPad14,8", 264);
		config.addIosDevice("IPAD_AIR_11_6G_WIFI_CELLULAR", "iPad14,9", 264);
		config.addIosDevice("IPAD_AIR_13_6G_WIFI",          "iPad14,10",264);
		config.addIosDevice("IPAD_AIR_13_6G_WIFI_CELLULAR", "iPad14,11",264);
		config.addIosDevice("IPAD_AIR_11_7G_WIFI",          "iPad15,3", 264);
		config.addIosDevice("IPAD_AIR_11_7G_WIFI_CELLULAR", "iPad15,4", 264);
		config.addIosDevice("IPAD_AIR_13_7G_WIFI",          "iPad15,5", 264);
		config.addIosDevice("IPAD_AIR_13_7G_WIFI_CELLULAR", "iPad15,6", 264);
		config.addIosDevice("IPAD_11G_WIFI",                "iPad15,7", 264);
		config.addIosDevice("IPAD_11G_WIFI_CELLULAR",       "iPad15,8", 264);
		config.addIosDevice("IPAD_MINI_7G_WIFI",            "iPad16,1", 326);
		config.addIosDevice("IPAD_MINI_7G_WIFI_CELLULAR",   "iPad16,2", 326);
		config.addIosDevice("IPAD_PRO_11_5G",               "iPad16,3", 264);
		config.addIosDevice("IPAD_PRO_11_5G",               "iPad16,4", 264);
		config.addIosDevice("IPAD_PRO_12.9_7G",             "iPad16,5", 264);
		config.addIosDevice("IPAD_PRO_12.9_7G",             "iPad16,6", 264);

		//also override simulator devices for better testing when simulating modern iPhones
		config.addIosDevice("SIMULATOR_32",     "i386", 460);
		config.addIosDevice("SIMULATOR_64",     "x86_64", 460);
		config.addIosDevice("SIMULATOR_ARM64",  "arm64", 460);

		return new IOSApplication(new ShatteredPixelDungeon(new IOSPlatformSupport()), config){
			@Override
			protected IOSInput createInput() {
				//FIXME essentially a backport of a fix to text fields from libGDX ios backend 1.13.5
				return new DefaultIOSInput(this){
					@Override
					public void setOnscreenKeyboardVisible(boolean visible, OnscreenKeyboardType type) {
						super.setOnscreenKeyboardVisible(visible, type);
						if (visible){
							((UITextField)getActiveKeyboardTextField()).setText("x");
						}
					}
				};

			}
		};
	}

	@Override
	public void didChangStatusBarOrientation(UIApplication application, UIInterfaceOrientation oldStatusBarOrientation) {
		super.didChangStatusBarOrientation(application, oldStatusBarOrientation);
		ShatteredPixelDungeon.seamlessResetScene();
	}

	public static void main(String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}
}
