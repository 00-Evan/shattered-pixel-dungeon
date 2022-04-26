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

package com.watabou.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtils {
	
	// Helper methods for setting/using a default base path and file address mode
	
	private static Files.FileType defaultFileType = null;
	private static String defaultPath = "";
	
	public static void setDefaultFileProperties( Files.FileType type, String path ){
		defaultFileType = type;
		defaultPath = path;
	}
	
	public static FileHandle getFileHandle( String name ){
		return getFileHandle( defaultFileType, defaultPath, name );
	}
	
	public static FileHandle getFileHandle( Files.FileType type, String name ){
		return getFileHandle( type, "", name );
	}
	
	public static FileHandle getFileHandle( Files.FileType type, String basePath, String name ){
		switch (type){
			case Classpath:
				return Gdx.files.classpath( basePath + name );
			case Internal:
				return Gdx.files.internal( basePath + name );
			case External:
				return Gdx.files.external( basePath + name );
			case Absolute:
				return Gdx.files.absolute( basePath + name );
			case Local:
				return Gdx.files.local( basePath + name );
			default:
				return null;
		}
	}
	
	// Files

	//looks to see if there is any evidence of interrupted saving
	public static boolean cleanTempFiles(){
		return cleanTempFiles("");
	}

	public static boolean cleanTempFiles( String dirName ){
		FileHandle dir = getFileHandle(dirName);
		boolean foundTemp = false;
		for (FileHandle file : dir.list()){
			if (file.isDirectory()){
				foundTemp = cleanTempFiles(dirName + file.name()) || foundTemp;
			} else {
				if (file.name().endsWith(".tmp")){
					FileHandle temp = file;
					FileHandle original = getFileHandle( defaultFileType, "", temp.path().replace(".tmp", "") );

					//replace the base file with the temp one if base is invalid or temp is valid and newer
					try {
						bundleFromStream(temp.read());

						try {
							bundleFromStream(original.read());

							if (temp.lastModified() > original.lastModified()) {
								temp.moveTo(original);
							} else {
								temp.delete();
							}

						} catch (Exception e) {
							temp.moveTo(original);
						}

					} catch (Exception e) {
						temp.delete();
					}

					foundTemp = true;
				}
			}
		}
		return foundTemp;
	}
	
	public static boolean fileExists( String name ){
		FileHandle file = getFileHandle( name );
		return file.exists() && !file.isDirectory() && file.length() > 0;
	}

	//returns length of a file in bytes, or 0 if file does not exist
	public static long fileLength( String name ){
		FileHandle file = getFileHandle( name );
		if (!file.exists() || file.isDirectory()){
			return 0;
		} else {
			return file.length();
		}
	}
	
	public static boolean deleteFile( String name ){
		return getFileHandle( name ).delete();
	}

	//replaces a file with junk data, for as many bytes as given
	//This is helpful as some cloud sync systems do not persist deleted, empty, or zeroed files
	public static void overwriteFile( String name, int bytes ){
		byte[] data = new byte[bytes];
		Arrays.fill(data, (byte)1);
		getFileHandle( name ).writeBytes(data, false);
	}
	
	// Directories
	
	public static boolean dirExists( String name ){
		FileHandle dir = getFileHandle( name );
		return dir.exists() && dir.isDirectory();
	}
	
	public static boolean deleteDir( String name ){
		FileHandle dir = getFileHandle( name );
		
		if (dir == null || !dir.isDirectory()){
			return false;
		} else {
			return dir.deleteDirectory();
		}
	}

	public static ArrayList<String> filesInDir( String name ){
		FileHandle dir = getFileHandle( name );
		ArrayList result = new ArrayList();
		if (dir != null && dir.isDirectory()){
			for (FileHandle file : dir.list()){
				result.add(file.name());
			}
		}
		return result;
	}
	
	// bundle reading
	
	//only works for base path
	public static Bundle bundleFromFile( String fileName ) throws IOException{
		try {
			FileHandle file = getFileHandle( fileName );
			return bundleFromStream(file.read());
		} catch (GdxRuntimeException e){
			//game classes expect an IO exception, so wrap the GDX exception in that
			throw new IOException(e);
		}
	}
	
	private static Bundle bundleFromStream( InputStream input ) throws IOException{
		Bundle bundle = Bundle.read( input );
		input.close();
		return bundle;
	}
	
	// bundle writing
	
	//only works for base path
	public static void bundleToFile( String fileName, Bundle bundle ) throws IOException{
		try {
			FileHandle file = getFileHandle(fileName);

			//write to a temp file, then move the files.
			// This helps prevent save corruption if writing is interrupted
			if (file.exists()){
				FileHandle temp = getFileHandle(fileName + ".tmp");
				bundleToStream(temp.write(false), bundle);
				file.delete();
				temp.moveTo(file);
			} else {
				bundleToStream(file.write(false), bundle);
			}

		} catch (GdxRuntimeException e){
			//game classes expect an IO exception, so wrap the GDX exception in that
			throw new IOException(e);
		}
	}
	
	private static void bundleToStream( OutputStream output, Bundle bundle ) throws IOException{
		Bundle.write( bundle, output );
		output.close();
	}

}
