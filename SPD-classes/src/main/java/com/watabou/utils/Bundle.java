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

package com.watabou.utils;

import com.watabou.noosa.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Bundle {

	private static final String CLASS_NAME = "__className";

	public static final String DEFAULT_KEY = "key";
	
	private static HashMap<String,String> aliases = new HashMap<>();
	
	private JSONObject data;
	
	public Bundle() {
		this( new JSONObject() );
	}
	
	public String toString() {
		return data.toString();
	}
	
	private Bundle( JSONObject data ) {
		this.data = data;
	}
	
	public boolean isNull() {
		return data == null;
	}
	
	public boolean contains( String key ) {
		return !data.isNull( key );
	}
	
	public boolean getBoolean( String key ) {
		return data.optBoolean( key );
	}
	
	public int getInt( String key ) {
		return data.optInt( key );
	}

	public long getLong( String key ) {
		return data.optLong( key );
	}
	
	public float getFloat( String key ) {
		return (float)data.optDouble( key, 0.0 );
	}
	
	public String getString( String key ) {
		return data.optString( key );
	}

	public Class getClass( String key ) {
		String clName =  getString(key).replace("class ", "");
		if (!clName.equals("")){
			if (aliases.containsKey( clName )) {
				clName = aliases.get( clName );
			}
			
			return Reflection.forName( clName );
		}
		return null;
	}
	
	public Bundle getBundle( String key ) {
		return new Bundle( data.optJSONObject( key ) );
	}
	
	private Bundlable get() {
		if (data == null) return null;
		
		String clName = getString( CLASS_NAME );
		if (aliases.containsKey( clName )) {
			clName = aliases.get( clName );
		}
		
		Class<?> cl = Reflection.forName( clName );
		//Skip none-static inner classes as they can't be instantiated through bundle restoring
		//Classes which make use of none-static inner classes must manage instantiation manually
		if (cl != null && (!Reflection.isMemberClass(cl) || Reflection.isStatic(cl))) {
			Bundlable object = (Bundlable) Reflection.newInstance(cl);
			if (object != null) {
				object.restoreFromBundle(this);
				return object;
			}
		}
		
		return null;
	}
	
	public Bundlable get( String key ) {
		return getBundle( key ).get();
	}
	
	public <E extends Enum<E>> E getEnum( String key, Class<E> enumClass ) {
		try {
			return Enum.valueOf( enumClass, data.getString( key ) );
		} catch (JSONException e) {
			Game.reportException(e);
			return enumClass.getEnumConstants()[0];
		} catch (IllegalArgumentException e) {
			Game.reportException(e);
			return enumClass.getEnumConstants()[0];
		}
	}
	
	public int[] getIntArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			int[] result = new int[length];
			for (int i=0; i < length; i++) {
				result[i] = array.getInt( i );
			}
			return result;
		} catch (JSONException e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public float[] getFloatArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			float[] result = new float[length];
			for (int i=0; i < length; i++) {
				result[i] = (float)array.optDouble( i, 0.0 );
			}
			return result;
		} catch (JSONException e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public boolean[] getBooleanArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			boolean[] result = new boolean[length];
			for (int i=0; i < length; i++) {
				result[i] = array.getBoolean( i );
			}
			return result;
		} catch (JSONException e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public String[] getStringArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			String[] result = new String[length];
			for (int i=0; i < length; i++) {
				result[i] = array.getString( i );
			}
			return result;
		} catch (JSONException e) {
			Game.reportException(e);
			return null;
		}
	}

	public Class[] getClassArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			Class[] result = new Class[length];
			for (int i=0; i < length; i++) {
				String clName = array.getString( i ).replace("class ", "");
				if (aliases.containsKey( clName )) {
					clName = aliases.get( clName );
				}
				Class cl = Reflection.forName( clName );
				result[i] = cl;
			}
			return result;
		} catch (JSONException e) {
			Game.reportException(e);
			return null;
		}
	}

	public Bundle[] getBundleArray(){
		return getBundleArray( DEFAULT_KEY );
	}

	public Bundle[] getBundleArray( String key ){
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			Bundle[] result = new Bundle[length];
			for (int i=0; i < length; i++) {
				result[i] = new Bundle( array.getJSONObject( i ) );
			}
			return result;
		} catch (JSONException e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public Collection<Bundlable> getCollection( String key ) {
		
		ArrayList<Bundlable> list = new ArrayList<>();
		
		try {
			JSONArray array = data.getJSONArray( key );
			for (int i=0; i < array.length(); i++) {
				Bundlable O = new Bundle( array.getJSONObject( i ) ).get();
				if (O != null) list.add( O );
			}
		} catch (JSONException e) {
			Game.reportException(e);
		}
		
		return list;
	}
	
	public void put( String key, boolean value ) {
		try {
			data.put( key, value );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, int value ) {
		try {
			data.put( key, value );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}

	public void put( String key, long value ) {
		try {
			data.put( key, value );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, float value ) {
		try {
			data.put( key, value );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, String value ) {
		try {
			data.put( key, value );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}

	public void put( String key, Class value ){
		try {
			data.put( key, value );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, Bundle bundle ) {
		try {
			data.put( key, bundle.data );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, Bundlable object ) {
		if (object != null) {
			try {
				Bundle bundle = new Bundle();
				bundle.put( CLASS_NAME, object.getClass().getName() );
				object.storeInBundle( bundle );
				data.put( key, bundle.data );
			} catch (JSONException e) {
				Game.reportException(e);
			}
		}
	}
	
	public void put( String key, Enum<?> value ) {
		if (value != null) {
			try {
				data.put( key, value.name() );
			} catch (JSONException e) {
				Game.reportException(e);
			}
		}
	}
	
	public void put( String key, int[] array ) {
		try {
			JSONArray jsonArray = new JSONArray();
			for (int i=0; i < array.length; i++) {
				jsonArray.put( i, array[i] );
			}
			data.put( key, jsonArray );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, float[] array ) {
		try {
			JSONArray jsonArray = new JSONArray();
			for (int i=0; i < array.length; i++) {
				jsonArray.put( i, array[i] );
			}
			data.put( key, jsonArray );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, boolean[] array ) {
		try {
			JSONArray jsonArray = new JSONArray();
			for (int i=0; i < array.length; i++) {
				jsonArray.put( i, array[i] );
			}
			data.put( key, jsonArray );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, String[] array ) {
		try {
			JSONArray jsonArray = new JSONArray();
			for (int i=0; i < array.length; i++) {
				jsonArray.put( i, array[i] );
			}
			data.put( key, jsonArray );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}

	public void put( String key, Class[] array ){
		try {
			JSONArray jsonArray = new JSONArray();
			for (int i=0; i < array.length; i++) {
				jsonArray.put( i, array[i].getName() );
			}
			data.put( key, jsonArray );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, Collection<? extends Bundlable> collection ) {
		JSONArray array = new JSONArray();
		for (Bundlable object : collection) {
			//Skip none-static inner classes as they can't be instantiated through bundle restoring
			//Classes which make use of none-static inner classes must manage instantiation manually
			if (object != null) {
				Class cl = object.getClass();
				if ((!Reflection.isMemberClass(cl) || Reflection.isStatic(cl))) {
					Bundle bundle = new Bundle();
					bundle.put(CLASS_NAME, cl.getName());
					object.storeInBundle(bundle);
					array.put(bundle.data);
				}
			}
		}
		try {
			data.put( key, array );
		} catch (JSONException e) {
			Game.reportException(e);
		}
	}

	//useful to turn this off for save data debugging.
	private static final boolean compressByDefault = true;

	private static final int GZIP_BUFFER = 1024*4; //4 kb
	
	public static Bundle read( InputStream stream ) throws IOException {

		try {
			if (!stream.markSupported()){
				stream = new BufferedInputStream( stream, 2 );
			}

			//determines if we're reading a regular, or compressed file
			stream.mark( 2 );
			byte[] header = new byte[2];
			stream.read( header );
			stream.reset();
			
			//GZIP header is 0x1f8b
			if( header[ 0 ] == (byte) 0x1f && header[ 1 ] == (byte) 0x8b ) {
				stream = new GZIPInputStream( stream, GZIP_BUFFER );
			}

			//cannot just tokenize the stream directly as that constructor doesn't exist on Android
			BufferedReader reader = new BufferedReader( new InputStreamReader( stream ));
			Object json = new JSONTokener( reader.readLine() ).nextValue();
			reader.close();

			//if the data is an array, put it in a fresh object with the default key
			if (json instanceof JSONArray){
				json = new JSONObject().put( DEFAULT_KEY, json );
			}

			return new Bundle( (JSONObject) json );
		} catch (Exception e) {
			Game.reportException(e);
			throw new IOException();
		}
	}

	public static boolean write( Bundle bundle, OutputStream stream ){
		return write(bundle, stream, compressByDefault);
	}

	public static boolean write( Bundle bundle, OutputStream stream, boolean compressed ) {
		try {
			BufferedWriter writer;
			if (compressed) writer = new BufferedWriter( new OutputStreamWriter( new GZIPOutputStream(stream, GZIP_BUFFER ) ) );
			else writer = new BufferedWriter( new OutputStreamWriter( stream ) );

			writer.write( bundle.data.toString() );
			writer.close();

			return true;
		} catch (IOException e) {
			Game.reportException(e);
			return false;
		}
	}
	
	public static void addAlias( Class<?> cl, String alias ) {
		aliases.put( alias, cl.getName() );
	}
	
}
