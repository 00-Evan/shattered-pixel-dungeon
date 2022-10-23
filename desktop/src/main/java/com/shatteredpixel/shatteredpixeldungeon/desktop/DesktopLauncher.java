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

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3NativesLoader;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.services.news.News;
import com.shatteredpixel.shatteredpixeldungeon.services.news.NewsImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.UpdateImpl;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Point;

import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

public class DesktopLauncher {

	public static void main (String[] args) {

		if (!DesktopLaunchValidator.verifyValidJVMState(args)){
			return;
		}

		//detection for FreeBSD (which is equivalent to linux for us)
		//TODO might want to merge request this to libGDX
		if (System.getProperty("os.name").contains("FreeBSD")) {
			SharedLibraryLoader.isLinux = true;
			//this overrides incorrect values set in SharedLibraryLoader's static initializer
			SharedLibraryLoader.isIos = false;
			SharedLibraryLoader.is64Bit = System.getProperty("os.arch").contains("64") || System.getProperty("os.arch").startsWith("armv8");
		}
		
		final String title;
		if (DesktopLauncher.class.getPackage().getSpecificationTitle() == null){
			title = System.getProperty("Specification-Title");
		} else {
			title = DesktopLauncher.class.getPackage().getSpecificationTitle();
		}
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Game.reportException(throwable);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				throwable.printStackTrace(pw);
				pw.flush();
				String exceptionMsg = sw.toString();

				//shorten/simplify exception message to make it easier to fit into a message box
				exceptionMsg = exceptionMsg.replaceAll("\\(.*:([0-9]*)\\)", "($1)");
				exceptionMsg = exceptionMsg.replace("com.shatteredpixel.shatteredpixeldungeon.", "");
				exceptionMsg = exceptionMsg.replace("com.watabou.", "");
				exceptionMsg = exceptionMsg.replace("com.badlogic.gdx.", "");
				exceptionMsg = exceptionMsg.replace("\t", "    ");
				exceptionMsg = exceptionMsg.replace("'", "");

				if (exceptionMsg.length() > 1000){
					exceptionMsg = exceptionMsg.substring(0, 1000) + "...";
				}

				if (exceptionMsg.contains("Could not create window")){
					TinyFileDialogs.tinyfd_messageBox(title + " Has Crashed!",
							title + " was not able to initialize its graphics display, sorry about that!\n\n" +
									"This usually happens when your graphics card does not support OpenGL 2.0+, or has misconfigured graphics drivers.\n\n" +
									"If you are certain the game should be working on your computer, feel free to message the developer (Evan@ShatteredPixel.com)\n\n" +
									"version: " + Game.version, "ok", "error", false);
				} else {
					TinyFileDialogs.tinyfd_messageBox(title + " Has Crashed!",
							title + " has run into an error it cannot recover from and has crashed, sorry about that!\n\n" +
									"If you could, please email this error message to the developer (Evan@ShatteredPixel.com):\n\n" +
									"version: " + Game.version + "\n" +
									exceptionMsg,
							"ok", "error", false);
				}
				System.exit(1);
			}
		});
		
		Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (Game.version == null) {
			Game.version = System.getProperty("Specification-Version");
		}
		
		try {
			Game.versionCode = Integer.parseInt(DesktopLauncher.class.getPackage().getImplementationVersion());
		} catch (NumberFormatException e) {
			Game.versionCode = Integer.parseInt(System.getProperty("Implementation-Version"));
		}

		if (UpdateImpl.supportsUpdates()){
			Updates.service = UpdateImpl.getUpdateService();
		}
		if (NewsImpl.supportsNews()){
			News.service = NewsImpl.getNewsService();
		}
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		
		config.setTitle( title );

		//if I were implementing this from scratch I would use the full implementation title for saves
		// (e.g. /.shatteredpixel/shatteredpixeldungeon), but we have too much existing save
		// date to worry about transferring at this point.
		String vendor = DesktopLauncher.class.getPackage().getImplementationTitle();
		if (vendor == null) {
			vendor = System.getProperty("Implementation-Title");
		}
		vendor = vendor.split("\\.")[1];

		String basePath = "";
		Files.FileType baseFileType = null;
		if (SharedLibraryLoader.isWindows) {
			if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
				basePath = "Application Data/." + vendor + "/" + title + "/";
			} else {
				basePath = "AppData/Roaming/." + vendor + "/" + title + "/";
			}
			baseFileType = Files.FileType.External;
		} else if (SharedLibraryLoader.isMac) {
			basePath = "Library/Application Support/" + title + "/";
			baseFileType = Files.FileType.External;
		} else if (SharedLibraryLoader.isLinux) {
			String XDGHome = System.getenv("XDG_DATA_HOME");
			if (XDGHome == null) XDGHome = System.getProperty("user.home") + "/.local/share";

			String titleLinux = title.toLowerCase(Locale.ROOT).replace(" ", "-");
			basePath = XDGHome + "/." + vendor + "/" + titleLinux + "/";

			//copy over files from old linux save DIR, pre-1.2.0
			FileHandle oldBase = new Lwjgl3FileHandle("." + vendor + "/" + titleLinux + "/", Files.FileType.External);
			FileHandle newBase = new Lwjgl3FileHandle(basePath, Files.FileType.Absolute);
			if (oldBase.exists()){
				if (!newBase.exists()) {
					oldBase.copyTo(newBase.parent());
				}
				oldBase.deleteDirectory();
				oldBase.parent().delete(); //only regular delete, in case of saves from other PD versions
			}
			baseFileType = Files.FileType.Absolute;
		}

		config.setPreferencesConfig( basePath, baseFileType );
		SPDSettings.set( new Lwjgl3Preferences( new Lwjgl3FileHandle(basePath + SPDSettings.DEFAULT_PREFS_FILE, baseFileType) ));
		FileUtils.setDefaultFileProperties( baseFileType, basePath );
		
		config.setWindowSizeLimits( 720, 400, -1, -1 );
		Point p = SPDSettings.windowResolution();
		config.setWindowedMode( p.x, p.y );

		config.setMaximized(SPDSettings.windowMaximized());

		//going fullscreen on launch is still buggy on macOS, so game enters it slightly later
		if (SPDSettings.fullscreen() && !SharedLibraryLoader.isMac) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		}
		
		//records whether window is maximized or not for settings
		DesktopWindowListener listener = new DesktopWindowListener();
		config.setWindowListener( listener );
		
		config.setWindowIcon("icons/icon_16.png", "icons/icon_32.png", "icons/icon_48.png",
				"icons/icon_64.png", "icons/icon_128.png", "icons/icon_256.png");

		new Lwjgl3Application(new ShatteredPixelDungeon(new DesktopPlatformSupport()), config);
	}
}
