/*
 * Copyright (C) 2012-2015 Oleg Dolya
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

package com.watabou.noosa.audio;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import com.watabou.noosa.Game;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public enum Sample implements SoundPool.OnLoadCompleteListener {
	
	INSTANCE;

	public static final int MAX_STREAMS = 8;
	
	protected SoundPool pool = 
		new SoundPool( MAX_STREAMS, AudioManager.STREAM_MUSIC, 0 );
	
	protected HashMap<Object, Integer> ids = 
		new HashMap<Object, Integer>();
	
	private boolean enabled = true;
	
	public void reset() {

		pool.release();
		
		pool = new SoundPool( MAX_STREAMS, AudioManager.STREAM_MUSIC, 0 );
		pool.setOnLoadCompleteListener( this );
		
		ids.clear();
	}
	
	public void pause() {
		if (pool != null) {
			pool.autoPause();
		}
	}
	
	public void resume() {
		if (pool != null) {
			pool.autoResume();
		}
	}
	
	private LinkedList<String> loadingQueue = new LinkedList<String>();
	
	public void load( String... assets ) {
		
	/*	AssetManager manager = Game.instance.getAssets();
		
		for (int i=0; i < assets.length; i++) {
			
			String asset = assets[i];
			
			if (!ids.containsKey( asset )) {
				try {
					AssetFileDescriptor fd = manager.openFd( asset );
					int streamID = pool.load( fd, 1 ) ;
					ids.put( asset, streamID );
					fd.close();
				} catch (IOException e) {
				}
			}
			
		}*/
		for (String asset : assets) {
			loadingQueue.add( asset );
		}
		loadNext();
	}
	
	private void loadNext() {
		final String asset = loadingQueue.poll();
		if (asset != null) {
			if (!ids.containsKey( asset )) {
				try {
					pool.setOnLoadCompleteListener( new SoundPool.OnLoadCompleteListener() {
						@Override
						public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
							loadNext();
						}
					} );
					
					AssetManager manager = Game.instance.getAssets();
					AssetFileDescriptor fd = manager.openFd( asset );
					int streamID = pool.load( fd, 1 ) ;
					ids.put( asset, streamID );
					fd.close();
				} catch (IOException e) {
					loadNext();
				}
			} else {
				loadNext();
			}
		}
	}
	
	public void unload( Object src ) {
		
		if (ids.containsKey( src )) {
			
			pool.unload( ids.get( src ) );
			ids.remove( src );
		}
	}
	
	public int play( Object id ) {
		return play( id, 1, 1, 1 );
	}
	
	public int play( Object id, float volume ) {
		return play( id, volume, volume, 1 );
	}
	
	public int play( Object id, float leftVolume, float rightVolume, float rate ) {
		if (enabled && ids.containsKey( id )) {
			return pool.play( ids.get( id ), leftVolume, rightVolume, 0, 0, rate );
		} else {
			return -1;
		}
	}
	
	public void enable( boolean value ) {
		enabled = value;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void onLoadComplete( SoundPool soundPool, int sampleId, int status ) {
	}
}
