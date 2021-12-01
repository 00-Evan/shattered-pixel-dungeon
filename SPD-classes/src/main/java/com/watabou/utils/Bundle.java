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

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.watabou.noosa.Game;

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

	private JsonValue data;

	public Bundle() {
		this( new JsonValue(JsonValue.ValueType.object) );
	}
	
	public String toString() {
		return data.toString();
	}
	
	private Bundle( JsonValue data ) {
		this.data = data;
	}
	
	public boolean isNull() {
		return data == null;
	}
	
	public boolean contains( String key ) {
		return data.has(key) && !data.get(key).isNull();
	}

	public ArrayList<String> getKeys(){
		ArrayList<String> keys = new ArrayList<>();
		for (JsonValue child : data){
			keys.add(child.name());
		}
		return keys;
	}
	
	public boolean getBoolean( String key ) {
		return data.getBoolean( key, false );
	}
	
	public int getInt( String key ) {
		return data.getInt( key, 0 );
	}

	public long getLong( String key ) {
		return data.getLong( key, 0 );
	}
	
	public float getFloat( String key ) {
		return data.getFloat( key, 0f );
	}
	
	public String getString( String key ) {
		return data.getString( key, "" );
	}

	public Class getClass( String key ) {
		String clName = getString(key).replace("class ", "");
		if (!clName.equals("")){
			if (aliases.containsKey( clName )) {
				clName = aliases.get( clName );
			}
			
			return Reflection.forName( clName );
		}
		return null;
	}
	
	public Bundle getBundle( String key ) {
		return new Bundle( data.get(key) );
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
			return Enum.valueOf( enumClass, getString( key ) );
		} catch (Exception e) {
			Game.reportException(e);
			return enumClass.getEnumConstants()[0];
		}
	}
	
	public int[] getIntArray( String key ) {
		try {
			return data.get( key ).asIntArray();
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public float[] getFloatArray( String key ) {
		try {
			return data.get( key ).asFloatArray();
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public boolean[] getBooleanArray( String key ) {
		try {
			return data.get( key ).asBooleanArray();
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public String[] getStringArray( String key ) {
		try {
			return data.get( key ).asStringArray();
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}

	public Class[] getClassArray( String key ) {
		try {
			String[] clNames = data.get( key ).asStringArray();
			Class[] result = new Class[clNames.length];
			for (int i=0; i < clNames.length; i++) {
				String clName = clNames[i].replace("class ", "");
				if (aliases.containsKey( clName )) {
					clName = aliases.get( clName );
				}
				Class cl = Reflection.forName( clName );
				result[i] = cl;
			}
			return result;
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}

	public Bundle[] getBundleArray(){
		return getBundleArray( DEFAULT_KEY );
	}

	public Bundle[] getBundleArray( String key ){
		try {
			JsonValue array = data.get( key );
			int length = array.size;
			Bundle[] result = new Bundle[length];
			for (int i=0; i < length; i++) {
				result[i] = new Bundle( array.get( i ) );
			}
			return result;
		} catch (Exception e) {
			Game.reportException(e);
			return null;
		}
	}
	
	public Collection<Bundlable> getCollection( String key ) {
		
		ArrayList<Bundlable> list = new ArrayList<>();
		
		try {
			JsonValue array = data.get( key );
			for (JsonValue element : array) {
				Bundlable O = new Bundle( element ).get();
				if (O != null) list.add( O );
			}
		} catch (Exception e) {
			Game.reportException(e);
		}
		
		return list;
	}
	
	public void put( String key, boolean value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, int value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}

	public void put( String key, long value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, float value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, String value ) {
		try {
			data.addChild( key, new JsonValue(value) );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}

	public void put( String key, Class value ){
		try {
			data.addChild( key, new JsonValue(value.toString()) );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, Bundle bundle ) {
		try {
			data.addChild( key, bundle.data);
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, Bundlable object ) {
		if (object != null) {
			try {
				Bundle bundle = new Bundle();
				bundle.put( CLASS_NAME, object.getClass().getName() );
				object.storeInBundle( bundle );
				data.addChild( key, bundle.data);
			} catch (Exception e) {
				Game.reportException(e);
			}
		}
	}
	
	public void put( String key, Enum<?> value ) {
		if (value != null) {
			try {
				data.addChild( key, new JsonValue(value.name()) );
			} catch (Exception e) {
				Game.reportException(e);
			}
		}
	}
	
	public void put( String key, int[] array ) {
		try {
			JsonValue JSON = new JsonValue(JsonValue.ValueType.array);
			for (int val : array) {
				JSON.addChild(new JsonValue(val));
			}
			data.addChild( key, JSON );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, float[] array ) {
		try {
			JsonValue JSON = new JsonValue(JsonValue.ValueType.array);
			for (float val : array) {
				JSON.addChild(new JsonValue(val));
			}
			data.addChild( key, JSON );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, boolean[] array ) {
		try {
			JsonValue JSON = new JsonValue(JsonValue.ValueType.array);
			for (boolean val : array) {
				JSON.addChild(new JsonValue(val));
			}
			data.addChild( key, JSON );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, String[] array ) {
		try {
			JsonValue JSON = new JsonValue(JsonValue.ValueType.array);
			for (String val : array) {
				JSON.addChild(new JsonValue(val));
			}
			data.addChild( key, JSON );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}

	public void put( String key, Class[] array ){
		try {
			JsonValue JSON = new JsonValue(JsonValue.ValueType.array);
			for (Class val : array) {
				JSON.addChild(new JsonValue(val.getName()));
			}
			data.addChild( key, JSON );
		} catch (Exception e) {
			Game.reportException(e);
		}
	}
	
	public void put( String key, Collection<? extends Bundlable> collection ) {
		JsonValue JSON = new JsonValue(JsonValue.ValueType.array);
		for (Bundlable object : collection) {
			//Skip none-static inner classes as they can't be instantiated through bundle restoring
			//Classes which make use of none-static inner classes must manage instantiation manually
			if (object != null) {
				Class cl = object.getClass();
				if ((!Reflection.isMemberClass(cl) || Reflection.isStatic(cl))) {
					Bundle bundle = new Bundle();
					bundle.put(CLASS_NAME, cl.getName());
					object.storeInBundle(bundle);
					JSON.addChild(bundle.data);
				}
			}
		}
		try {
			data.addChild( key, JSON );
		} catch (Exception e) {
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
			JsonValue json = new JsonReader().parse(reader);
			reader.close();

			//if the data is an array, put it in a fresh object with the default key
			if (json.isArray()){
				JsonValue result = new JsonValue( JsonValue.ValueType.object );
				result.addChild( DEFAULT_KEY, json );
				return new Bundle(result);
			} else {
				return new Bundle(json);
			}
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

			writer.write( bundle.toString() );
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
