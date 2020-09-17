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

package com.watabou.noosa.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.watabou.noosa.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public enum Sample {

	INSTANCE;

	protected HashMap<Object, Sound> ids = new HashMap<>();

	private boolean enabled = true;
	private float globalVolume = 1f;

	public synchronized void reset() {

		for (Sound sound : ids.values()){
			sound.dispose();
		}
		
		ids.clear();
		delayedSFX.clear();

	}

	public synchronized void pause() {
		for (Sound sound : ids.values()) {
			sound.pause();
		}
	}

	public synchronized void resume() {
		for (Sound sound : ids.values()) {
			sound.resume();
		}
	}

	public synchronized void load( final String... assets ) {

		final ArrayList<String> toLoad = new ArrayList<>();

		for (String asset : assets){
			if (!ids.containsKey(asset)){
				toLoad.add(asset);
			}
		}

		//don't make a new thread of all assets are already loaded
		if (toLoad.isEmpty()) return;

		//load in a separate thread to prevent this blocking the UI
		new Thread(){
			@Override
			public void run() {
				for (String asset : toLoad) {
					Sound newSound = Gdx.audio.newSound(Gdx.files.internal(asset));
					synchronized (INSTANCE) {
						ids.put(asset, newSound);
					}
				}
			}
		}.start();
		
	}

	public synchronized void unload( Object src ) {
		if (ids.containsKey( src )) {
			ids.get( src ).dispose();
			ids.remove( src );
		}
	}

	public long play( Object id ) {
		return play( id, 1 );
	}

	public long play( Object id, float volume ) {
		return play( id, volume, volume, 1 );
	}
	
	public long play( Object id, float volume, float pitch ) {
		return play( id, volume, volume, pitch );
	}
	
	public synchronized long play( Object id, float leftVolume, float rightVolume, float pitch ) {
		float volume = Math.max(leftVolume, rightVolume);
		float pan = rightVolume - leftVolume;
		if (enabled && ids.containsKey( id )) {
			return ids.get(id).play( globalVolume*volume, pitch, pan );
		} else {
			return -1;
		}
	}

	private class DelayedSoundEffect{
		Object id;
		float delay;

		float leftVol;
		float rightVol;
		float pitch;
	}

	private static final HashSet<DelayedSoundEffect> delayedSFX = new HashSet<>();

	public void playDelayed( Object id, float delay ){
		playDelayed( id, delay, 1 );
	}

	public void playDelayed( Object id, float delay, float volume ) {
		playDelayed( id, delay, volume, volume, 1 );
	}

	public void playDelayed( Object id, float delay, float volume, float pitch ) {
		playDelayed( id, delay, volume, volume, pitch );
	}

	public void playDelayed( Object id, float delay, float leftVolume, float rightVolume, float pitch ) {
		if (delay <= 0) {
			play(id, leftVolume, rightVolume, pitch);
			return;
		}
		DelayedSoundEffect sfx = new DelayedSoundEffect();
		sfx.id = id;
		sfx.delay = delay;
		sfx.leftVol = leftVolume;
		sfx.rightVol = rightVolume;
		sfx.pitch = pitch;
		synchronized (delayedSFX) {
			delayedSFX.add(sfx);
		}
	}

	public void update(){
		synchronized (delayedSFX) {
			if (delayedSFX.isEmpty()) return;
			for (DelayedSoundEffect sfx : delayedSFX.toArray(new DelayedSoundEffect[0])) {
				sfx.delay -= Game.elapsed;
				if (sfx.delay <= 0) {
					delayedSFX.remove(sfx);
					play(sfx.id, sfx.leftVol, sfx.rightVol, sfx.pitch);
				}
			}
		}
	}

	public void enable( boolean value ) {
		enabled = value;
	}

	public void volume( float value ) {
		globalVolume = value;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
}