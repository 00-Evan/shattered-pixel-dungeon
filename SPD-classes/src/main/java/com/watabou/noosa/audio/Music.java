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
import com.watabou.noosa.Game;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.awt.MediaTracker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum Music {
	
	INSTANCE;
	
	private com.badlogic.gdx.audio.Music player;
	
	private String lastPlayed;
	private boolean looping;
	
	private boolean enabled = true;
	private float volume = 1f;

	String[] trackList;
	float[] trackChances;
	private final ArrayList<String> trackQueue = new ArrayList<>();
	boolean shuffle = false;
	
	public synchronized void play( String assetName, boolean looping ) {

		//iOS cannot play ogg, so we use an mp3 alternative instead
		if (assetName != null && DeviceCompat.isiOS()){
			assetName = assetName.replace(".ogg", ".mp3");
		}
		
		if (isPlaying() && lastPlayed != null && lastPlayed.equals( assetName )) {
			return;
		}
		
		stop();
		
		lastPlayed = assetName;
		trackList = null;

		this.looping = looping;
		this.shuffle = false;

		if (!enabled || assetName == null) {
			return;
		}

		play(assetName, null);
	}

	public synchronized void playTracks( String[] tracks, float[] chances, boolean shuffle){

		if (tracks == null || tracks.length == 0 || tracks.length != chances.length){
			stop();
			return;
		}

		//iOS cannot play ogg, so we use an mp3 alternative instead
		if (tracks != null && DeviceCompat.isiOS()){
			for (int i = 0; i < tracks.length; i ++){
				tracks[i] = tracks[i].replace(".ogg", ".mp3");
			}
		}

		if (isPlaying() && this.trackList != null && tracks.length == trackList.length){

			boolean sameList = true;
			for (int i = 0; i < tracks.length; i ++){
				if (!tracks[i].equals(trackList[i]) || chances[i] != trackChances[i]){
					sameList = false;
					break;
				}
			}

			if (sameList) return;
		}

		stop();

		lastPlayed = null;
		trackList = tracks;
		trackChances = chances;
		trackQueue.clear();

		for (int i = 0; i < trackList.length; i++){
			if (Random.Float() < trackChances[i]){
				trackQueue.add(trackList[i]);
			}
		}

		this.looping = false;
		this.shuffle = shuffle;

		if (!enabled || trackQueue.isEmpty()){
			return;
		}

		play(trackQueue.remove(0), trackLooper);
	}

	private com.badlogic.gdx.audio.Music.OnCompletionListener trackLooper = new com.badlogic.gdx.audio.Music.OnCompletionListener() {
		@Override
		public void onCompletion(com.badlogic.gdx.audio.Music music) {
			//we do this in a separate thread to avoid graphics hitching while the music is prepared
			//FIXME this fixes graphics stutter but there's still some audio stutter, perhaps keep more than 1 player alive?
			if (!DeviceCompat.isDesktop()) {
				new Thread() {
					@Override
					public void run() {
						playNextTrack(music);
					}
				}.start();
			} else {
				//don't use a separate thread on desktop, causes errors and makes no performance difference(?)
				playNextTrack(music);
			}
		}
	};

	private synchronized void playNextTrack(com.badlogic.gdx.audio.Music music){
		if (trackList == null || trackList.length == 0 || music != player || player.isLooping()){
			return;
		}

		Music.this.stop();

		if (trackQueue.isEmpty()) {
			for (int i = 0; i < trackList.length; i++) {
				if (Random.Float() < trackChances[i]) {
					trackQueue.add(trackList[i]);
				}
			}
			if (shuffle) Collections.shuffle(trackQueue);
		}

		if (!enabled || trackQueue.isEmpty()) {
			return;
		}

		play(trackQueue.remove(0), trackLooper);
	};

	private synchronized void play(String track, com.badlogic.gdx.audio.Music.OnCompletionListener listener){
		try {
			player = Gdx.audio.newMusic(Gdx.files.internal(track));
			player.setLooping(looping);
			player.setVolume(volume);
			player.play();
			if (listener != null) {
				player.setOnCompletionListener(listener);
			}
		} catch (Exception e){
			Game.reportException(e);
			player = null;
		}
	}
	
	public synchronized void mute() {
		lastPlayed = null;
		stop();
	}
	
	public synchronized void pause() {
		if (player != null) {
			player.pause();
		}
	}
	
	public synchronized void resume() {
		if (player != null) {
			player.play();
			player.setLooping(looping);
		}
	}

	//TODO do we need to dispose every player? Maybe just stop them and keep an LRU cache of 2 or 3?
	public synchronized void stop() {
		if (player != null) {
			player.dispose();
			player = null;
		}
	}
	
	public synchronized void volume( float value ) {
		volume = value;
		if (player != null) {
			player.setVolume( value );
		}
	}
	
	public synchronized boolean isPlaying() {
		return player != null && player.isPlaying();
	}
	
	public synchronized void enable( boolean value ) {
		enabled = value;
		if (isPlaying() && !value) {
			stop();
		} else
		if (!isPlaying() && value) {
			if (trackList != null){
				playTracks(trackList, trackChances, shuffle);
			} else {
				play(lastPlayed, looping);
			}
		}
	}
	
	public synchronized boolean isEnabled() {
		return enabled;
	}
	
}
