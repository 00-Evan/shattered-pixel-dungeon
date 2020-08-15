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

package com.watabou.utils;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	
	public static boolean fileExists( String name ){
		FileHandle file = getFileHandle( name );
		return file.exists() && !file.isDirectory();
	}
	
	public static boolean deleteFile( String name ){
		return getFileHandle( name ).delete();
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
			bundleToStream(getFileHandle( fileName ).write(false), bundle);
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
