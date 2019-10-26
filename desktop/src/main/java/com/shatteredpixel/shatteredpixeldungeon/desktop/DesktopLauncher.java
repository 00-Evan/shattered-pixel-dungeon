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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.watabou.noosa.Game;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				throwable.printStackTrace(pw);
				pw.flush();
				JOptionPane.showMessageDialog(null, "Shattered Pixel Dungeon has crashed, sorry about that!\n\n" +
						"If you could, please email this error message to me and I'll get it fixed (Evan@ShatteredPixel.com):\n\n" +
						sw.toString(), "Game Crash!", JOptionPane.ERROR_MESSAGE);
				Gdx.app.exit();
			}
		});
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 1920;
		config.height = 1080;
		
		//uncapped (but vsynced) framerate when focused, paused when not focused
		config.foregroundFPS = 0;
		config.backgroundFPS = -1;
		
		//TODO rather than hardcoding these values when running debug
		// it would be nice to be able to fetch them from gradle in some way
		config.title = DesktopLauncher.class.getPackage().getSpecificationTitle();
		if (config.title == null) {
			config.title = "ShatteredPD INDEV";
		}
		
		Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (Game.version == null) {
			Game.version = "0.7.5e-INDEV";
		}
		
		try {
			Game.versionCode = Integer.parseInt(DesktopLauncher.class.getPackage().getImplementationVersion());
		} catch (NumberFormatException e) {
			Game.versionCode = 382;
		}
		
		new LwjglApplication(new ShatteredPixelDungeon(new DesktopPlatformSupport()), config);
	}
}
