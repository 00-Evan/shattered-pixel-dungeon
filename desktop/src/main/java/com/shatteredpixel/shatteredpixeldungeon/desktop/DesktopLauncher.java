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

package com.shatteredpixel.shatteredpixeldungeon.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;
import com.watabou.utils.GameSettings;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				throwable.printStackTrace(pw);
				pw.flush();
				JOptionPane.showMessageDialog(null, config.title + " has crashed, sorry about that!\n\n" +
						"If you could, please email this error message to me and I'll get it fixed (Evan@ShatteredPixel.com):\n\n" +
						sw.toString(), "Game Crash!", JOptionPane.ERROR_MESSAGE);
				Gdx.app.exit();
			}
		});
		
		config.title = DesktopLauncher.class.getPackage().getSpecificationTitle();
		if (config.title == null) {
			config.title = System.getProperty("Specification-Title");
		}
		
		Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (Game.version == null) {
			Game.version = System.getProperty("Specification-Version");
		}
		
		try {
			Game.versionCode = Integer.parseInt(DesktopLauncher.class.getPackage().getImplementationVersion());
		} catch (NumberFormatException e) {
			Game.versionCode = Integer.parseInt(System.getProperty("Implementation-Version"));
		}
		
		config.width = 1920;
		config.height = 1080;
		
		//uncapped (but vsynced) framerate when focused, paused when not focused
		config.foregroundFPS = 0;
		config.backgroundFPS = -1;
		
		//TODO this is currently the same location and filenames as the old desktop codebase
		// If I want to move it now would be the time
		if (SharedLibraryLoader.isWindows) {
			if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
				config.preferencesDirectory = "Application Data/.shatteredpixel/Shattered Pixel Dungeon/";
			} else {
				config.preferencesDirectory = "AppData/Roaming/.shatteredpixel/Shattered Pixel Dungeon/";
			}
		} else if (SharedLibraryLoader.isMac) {
			config.preferencesDirectory = "Library/Application Support/Shattered Pixel Dungeon/";
		} else if (SharedLibraryLoader.isLinux) {
			config.preferencesDirectory = ".shatteredpixel/shattered-pixel-dungeon/";
		}
		GameSettings.set( new LwjglPreferences( "pd-prefs", config.preferencesDirectory) );
		
		FileUtils.setDefaultFileProperties( Files.FileType.External, config.preferencesDirectory );
		
		new LwjglApplication(new ShatteredPixelDungeon(new DesktopPlatformSupport()), config);
	}
}
