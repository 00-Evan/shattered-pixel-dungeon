/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
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
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.files.FileHandle;
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

public class DesktopLauncher {

	public static void main (String[] args) {

		if (!DesktopLaunchValidator.verifyValidJVMState(args)){
			return;
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

				if (exceptionMsg.contains("Couldn't create window")){
					TinyFileDialogs.tinyfd_messageBox(title + " Has Crashed!",
							title + " wasn't able to initialize it's graphics display, sorry about that!\n\n" +
									"This usually happens when a computer's graphics card does not support OpenGL 2.0+, or has misconfigured graphics drivers.\n\n" +
									"If you're certain the game should be working on your computer, feel free to message the developer (Evan@ShatteredPixel.com)\n\n" +
									"version: " + Game.version, "ok", "error", false);
				} else {
					TinyFileDialogs.tinyfd_messageBox(title + " Has Crashed!",
							title + " has run into an error it can't recover from and has crashed, sorry about that!\n\n" +
									"If you could, please email this error message to the developer (Evan@ShatteredPixel.com):\n\n" +
									"version: " + Game.version + "\n" +
									exceptionMsg,
							"ok", "error", false);
				}
				if (Gdx.app != null) Gdx.app.exit();
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
		
		String basePath = "";
		if (SharedLibraryLoader.isWindows) {
			if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
				basePath = "Application Data/.shatteredpixel/Shattered Pixel Dungeon/";
			} else {
				basePath = "AppData/Roaming/.shatteredpixel/Shattered Pixel Dungeon/";
			}
		} else if (SharedLibraryLoader.isMac) {
			basePath = "Library/Application Support/Shattered Pixel Dungeon/";
		} else if (SharedLibraryLoader.isLinux) {
			basePath = ".shatteredpixel/shattered-pixel-dungeon/";
		}

		//copy over prefs from old file location from legacy desktop codebase
		FileHandle oldPrefs = new Lwjgl3FileHandle(basePath + "pd-prefs", Files.FileType.External);
		FileHandle newPrefs = new Lwjgl3FileHandle(basePath + SPDSettings.DEFAULT_PREFS_FILE, Files.FileType.External);
		if (oldPrefs.exists() && !newPrefs.exists()){
			oldPrefs.copyTo(newPrefs);
		}

		config.setPreferencesConfig( basePath, Files.FileType.External );
		SPDSettings.set( new Lwjgl3Preferences( SPDSettings.DEFAULT_PREFS_FILE, basePath) );
		FileUtils.setDefaultFileProperties( Files.FileType.External, basePath );
		
		config.setWindowSizeLimits( 480, 320, -1, -1 );
		Point p = SPDSettings.windowResolution();
		config.setWindowedMode( p.x, p.y );

		config.setMaximized(SPDSettings.windowMaximized());

		if (SPDSettings.fullscreen()) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		}
		
		//we set fullscreen/maximized in the listener as doing it through the config seems to be buggy
		DesktopWindowListener listener = new DesktopWindowListener();
		config.setWindowListener( listener );
		
		config.setWindowIcon("icons/icon_16.png", "icons/icon_32.png", "icons/icon_64.png",
				"icons/icon_128.png", "icons/icon_256.png");

		new Lwjgl3Application(new ShatteredPixelDungeon(new DesktopPlatformSupport()), config);
	}
}
