/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class AndroidMissingNativesHandler extends Activity {

	public static Throwable error;

	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String versionName;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			versionName = "???";
		}

		int versionCode;
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			versionCode = 0;
		}

		String installer;
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
				installer = getPackageManager().getInstallSourceInfo(getPackageName()).getInstallingPackageName();
			} else {
				installer = getPackageManager().getInstallerPackageName(getPackageName());
			}
		} catch (Exception e) {
			installer = "???";
		}

		TextView text = new TextView(this);
		String message = "ShatteredPD failed to access some of its internal code and cannot start!\n\n" +
				"Try downloading the game from an official source if you haven't already. You can also screenshot this debug info and send it to the developer (Evan@ShatteredPixel.com):";

		message += "\n\nPackage: " + getPackageName();
		message += "\nVersion: " + versionName + " (" + versionCode + ")";
		message += "\nDevice: " + Build.MODEL;
		message += "\nInstaller: " + installer;

		if (error != null){
			Throwable next = error.getCause();
			while (next != null){
				error = next;
				next = error.getCause();
			}
			message += "\nError: " + error.getMessage();
		} else {
			message += "\nError: ???";
		}
		text.setText(message);
		text.setTextSize(16);
		text.setTextColor(0xFFFFFFFF);
		text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pixel_font.ttf"));
		text.setGravity(Gravity.CENTER_VERTICAL);
		text.setPadding(10, 10, 10, 10);
		setContentView(text);

	}
}
