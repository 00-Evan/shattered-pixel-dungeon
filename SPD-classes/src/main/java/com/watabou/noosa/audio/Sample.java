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

package com.watabou.noosa.audio;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.watabou.noosa.Game;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public enum Sample implements SoundPool.OnLoadCompleteListener {

	INSTANCE;

	public static final int MAX_STREAMS = 8;

	protected SoundPool pool =
			new SoundPool( MAX_STREAMS, AudioManager.STREAM_MUSIC, 0 );

	protected HashMap<Object, Integer> ids =
			new HashMap<>();

	private boolean enabled = true;
	private float volume = 1f;

	private LinkedList<String> loadingQueue = new LinkedList<>();

	public void reset() {

		ids.clear();
		loadingQueue = new LinkedList<>();
		pool.release();

		pool = new SoundPool( MAX_STREAMS, AudioManager.STREAM_MUSIC, 0 );
		pool.setOnLoadCompleteListener( this );

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

	public void load( String... assets ) {

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
				} catch (NullPointerException e) {
					// Do nothing (stop loading sounds)
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
		return play( id, 1 );
	}

	public int play( Object id, float volume ) {
		return play( id, volume, volume, 1 );
	}

	public int play( Object id, float leftVolume, float rightVolume, float rate ) {
		if (enabled && ids.containsKey( id )) {
			return pool.play( ids.get( id ), leftVolume*volume, rightVolume*volume, 0, 0, rate );
		} else {
			return -1;
		}
	}

	public void enable( boolean value ) {
		enabled = value;
	}

	public void volume( float value ) {
		this.volume = value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void onLoadComplete( SoundPool soundPool, int sampleId, int status ) {
	}
}