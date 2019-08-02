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

import android.annotation.TargetApi;
import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FullBackupDataOutput;
import android.os.ParcelFileDescriptor;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.journal.Journal;

import java.io.File;

//a handler for android backup requests
public class AndroidBackupHandler extends BackupAgent {
	
	//Both of these do nothing. This handler is here to support use of android 4.0+ ADB backup
	//and android 6.0+ auto-backup. It does not support android 2.2+ key-value backup
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) {}
	public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) {}
	
	@Override
	@TargetApi(14)
	public void onFullBackup(FullBackupDataOutput data) {
		//fully overrides super.onFullBackup, meaning only files specified here are backed up
		
		//does not backup runs in progress, to prevent cheating.
		
		//store shared preferences
		fullBackupFile(new File(getFilesDir().getParent() + "/shared_prefs/ShatteredPixelDungeon.xml"), data);
		
		//store game data
		File file = getFile( getFilesDir(), Rankings.RANKINGS_FILE );
		if (file != null){
			fullBackupFile( file , data);
		}
		file = getFile( getFilesDir(), Badges.BADGES_FILE );
		if (file != null){
			fullBackupFile( file , data);
		}
		file = getFile( getFilesDir(), Journal.JOURNAL_FILE );
		if (file != null){
			fullBackupFile( file , data);
		}
	}
	
	private static File getFile( File base, String name ){
		File file = new File(base, name);
		if (!file.exists() || !file.isDirectory()){
			return file;
		}
		return null;
	}
	
}
