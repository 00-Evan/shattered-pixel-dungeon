/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.watabou.utils;

import com.watabou.noosa.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	// Files
	
	public static boolean fileExists( String name ){
		File file = new File(Game.instance.getFilesDir(), name);
		return file.exists() && !file.isDirectory();
	}
	
	public static File getFile( String name ){
		return getFile( Game.instance.getFilesDir(), name);
	}
	
	public static File getFile( File base, String name ){
		File file = new File(base, name);
		if (!file.exists() || !file.isDirectory()){
			return file;
		}
		return null;
	}
	
	public static boolean deleteFile( String name ){
		return Game.instance.deleteFile( name );
	}
	
	public static boolean deleteFile( File file ){
		return !file.isDirectory() && file.delete();
	}
	
	
	// Directories
	
	public static boolean dirExists( String name ){
		File dir = new File(Game.instance.getFilesDir(), name);
		return dir.exists() && dir.isDirectory();
	}
	
	//base directory
	public static File getDir( String name ){
		return getDir( Game.instance.getFilesDir(), name);
	}
	
	public static File getDir( File base, String name ){
		File dir = new File(base, name);
		if (!dir.exists() && dir.mkdirs()){
			return dir;
		} else if (dir.isDirectory()){
			return dir;
		}
		return null;
	}
	
	public static boolean deleteDir( String name ){
		return deleteDir(getDir(name));
	}
	
	public static boolean deleteDir( File dir ){
		if (dir == null || !dir.isDirectory()){
			return false;
		}
		
		for (File f : dir.listFiles()){
			if (f.isDirectory()){
				if (!deleteDir(f)) return false;
			} else {
				if (!deleteFile(f)) return false;
			}
		}
		
		return dir.delete();
	}
	
	// bundle reading
	
	//only works for base path
	public static Bundle bundleFromFile( String fileName ) throws IOException{
		return bundleFromStream(Game.instance.openFileInput( fileName ));
	}
	
	public static Bundle bundleFromFile( File file ) throws IOException{
		return bundleFromStream(new FileInputStream(file));
	}
	
	private static Bundle bundleFromStream( InputStream input ) throws IOException{
		Bundle bundle = Bundle.read( input );
		input.close();
		return bundle;
	}
	
	// bundle writing
	
	//only works for base path
	public static void bundleToFile( String fileName, Bundle bundle ) throws IOException{
		bundleToStream( Game.instance.openFileOutput( fileName, Game.MODE_PRIVATE ), bundle);
	}
	
	public static void bundleToFile( File file, Bundle bundle ) throws IOException{
		bundleToStream( new FileOutputStream(file), bundle);
	}
	
	private static void bundleToStream( OutputStream output, Bundle bundle ) throws IOException{
		Bundle.write( bundle, output );
		output.close();
	}

}
